## Stage 0 — Decide scope & API (project kickoff)

### Goal
Fix constraints & API so later work is goal-directed and you don’t rework basics.

### Must-haves

- **Target language(s)** (e.g., Java/Kotlin, Node.js, Go).
- **Primary use-cases:** local dev? high-throughput server? embedded? cloud?
- **Basic public API shape** (logger factory, levels, basic appenders).
- **README** with goals and minimal example showing how to log one message.

### Deliverable

- **README.md** with 3-line example: Logger logger = LoggerFactory.getLogger("x"); logger.info("hello");

### Acceptance tests

- README example runs & prints a line.

### Checklist (tiny tasks)

- [x] Create repo ✅
- [x] skeleton README. ✅
- [x] Write a 3-line example that would be the public API. ✅
- [x] Commit and push. ✅