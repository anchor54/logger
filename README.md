# Logger

A simple, lightweight logging framework for Java applications that provides console-based logging with multiple log levels.

## Features

- Multiple log levels (debug, info, warning, error, exception)
- Console output with level-based formatting
- Simple and intuitive API
- Lightweight with no external dependencies

## Getting Started

### Prerequisites

- Java 11 or higher
- Gradle (wrapper included)

### Building the Project

```bash
./gradlew build
```

### Running the Application

```bash
./gradlew run
```

## API Reference

### Public Methods

- [ ] `Logger.debug(String message)` - Log debug information
- [ ] `Logger.info(String message)` - Log general information
- [ ] `Logger.warning(String message)` - Log warning messages
- [ ] `Logger.error(String message)` - Log error messages
- [ ] `Logger.exception(String message)` - Log exception details

### Usage Example

```java
import com.logger.Logger;

public class Example {
    public static void main(String[] args) {
        Logger.info("Application started");
        Logger.debug("Debug information");
        Logger.warning("This is a warning");
        Logger.error("An error occurred");
        Logger.exception("Exception details");
    }
}
```

## Project Structure

```
src/
├── main/
│   └── java/
│       └── com/
│           └── logger/
│               ├── Main.java
│               └── Logger.java (to be implemented)
└── test/
    └── java/
        └── com/
            └── logger/
                └── LoggerTest.java (to be implemented)
```

## Development

### Running Tests

```bash
./gradlew test
```

### Code Style

This project follows standard Java conventions. Please ensure your code is properly formatted before submitting contributions.

## License

This project is open source and available under the [MIT License](LICENSE).