# Research Notes: Java Thread Safety

## Question
Are static variables in Java thread safe?

## Key Findings

### Static Variables and Thread Safety
- **Static variables are NOT thread-safe by default**
- Shared across all instances of a class and all threads
- Can lead to race conditions when multiple threads access them concurrently

### Thread Safety Solutions

#### 1. Synchronization
```java
private static int counter = 0;

public static synchronized void increment() {
    counter++;
}
```

#### 2. Volatile Keyword
```java
private static volatile boolean flag = false;
```
- Ensures visibility across threads
- Only suitable for simple read/write operations

#### 3. Atomic Classes
```java
private static AtomicInteger counter = new AtomicInteger(0);

public static void increment() {
    counter.incrementAndGet();
}
```

#### 4. ThreadLocal
```java
private static ThreadLocal<Integer> threadLocalValue = new ThreadLocal<>();
```
- Each thread gets its own copy
- No sharing between threads

## Experiments/Tests
- [ ] Create test cases demonstrating race conditions with unsynchronized static variables
- [ ] Benchmark different synchronization approaches
- [ ] Test memory visibility issues

## Logger Implementation - First Draft

### Current Static Method Approach
```java
public class Logger {
    public static void debug(String message) {
        System.out.println("[DEBUG] " + message);
    }

    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }

    public static void warning(String message) {
        System.out.println("[WARNING] " + message);
    }

    public static void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void exception(String message) {
        System.out.println("[EXCEPTION] " + message);
    }
}
```

### Drawbacks of Static Method Logger

#### 1. Not Thread Safe
- Multiple threads writing to `System.out` simultaneously can interleave output
- No synchronization mechanism in place
- Could result in garbled log messages in multi-threaded applications

#### 2. No Configuration Possible
- Hard-coded log levels - cannot disable debug logs in production
- Fixed output format - no customization of timestamp, thread info, etc.
- Fixed output destination - always goes to console, cannot redirect to files
- No way to set minimum log level filtering
- Cannot configure different log levels for different components

#### 3. Additional Issues
- No structured logging support
- No log rotation capabilities
- No performance optimizations (e.g., lazy evaluation of log messages)
- Difficult to unit test code that uses this logger
- Global state makes it hard to have different logging configurations per module

## Additional Notes
- Static variables are stored in the method area/metaspace
- Initialization happens when class is first loaded
- Consider immutable objects for static references when possible

## References
- [ ] Java Concurrency in Practice
- [ ] Official Java documentation on memory model
- [ ] Performance benchmarks and studies

## 2025-09-27 — Topic: static-to-singleton-transition

Transition from static logging methods to singleton pattern for better design.

### Found conversation excerpts
- **Q:** how is creating a singleton instance of logger different from static instance? should both be configurable and be made thread safe?
  **A / Findings:** Both approaches have similar thread safety issues but key differences: Singleton allows lazy initialization, more flexibility for multiple configurations, easier mocking/testing with instance methods. Static methods have eager initialization, less flexible, harder to mock. Both should be thread-safe and configurable.

- **Q:** why are static methods not testable?
  **A / Findings:** Static methods are harder to test due to: 1) No mocking/stubbing capability, 2) No dependency injection possible, 3) Global state issues in tests. Instance methods with dependency injection provide much better testability and flexibility.

- **Code Evolution:** Moved from static method pattern to singleton pattern:
  ```java
  // Before: Static methods
  Logger.info("message");

  // After: Singleton instance
  Logger logger = Logger.getLogger();
  logger.info("message");
  ```

### Key Benefits of Singleton Transition
- Better testability through dependency injection
- More flexible for future enhancements (multiple loggers, configuration)
- Maintains global access while allowing instance-based design
- Easier to mock in unit tests

---
*Last updated: 2025-09-27*