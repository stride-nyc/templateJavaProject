# templateJavaProject

A Java project template with Gradle (Kotlin DSL), Checkstyle, PMD, [google-java-format](https://github.com/google/google-java-format) (via Spotless), JaCoCo coverage, and a unit/integration test split.

## Requirements

- The [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) (`./gradlew`) is checked in — you don't need Gradle installed locally.
- No JDK needs to be pre-installed either: the project targets **Java 25**, and the `foojay-resolver-convention` plugin (`settings.gradle.kts`) will auto-download a matching JDK if one isn't already on your machine.
- The Java version is defined in exactly one place: `gradle/gradle-daemon-jvm.properties`. It pins the Gradle **daemon** itself (keeping build tooling like Spotless/google-java-format on a JDK it's actually compatible with, regardless of what's on your `PATH`), and `build.gradle.kts` reads that same file to set the project's `java.toolchain`. To bump the Java version, run `./gradlew updateDaemonJvm --jvm-version=<version>` — don't edit a version number directly in `build.gradle.kts`, there isn't one.

## Project layout

```
src/
  main/java        Application code
  main/resources
  test/java         Unit tests (JUnit 6), run by `test`
  test/resources
  int-test/java     Integration tests, run by `intTest`
  int-test/resources
config/
  checkstyle/       Checkstyle ruleset
  pmd/              PMD ruleset
.github/
  pull_request_template.md
```

`int-test` is a separate Gradle source set (`intTest`), not part of the default `test` task, so slower/broader integration tests don't run on every unit test pass.

## Common commands

Run everything (compile, format check, Checkstyle, PMD, unit tests, integration tests, coverage reports):

```shell
./gradlew check
```

### Building

| Command | Description |
|---|---|
| `./gradlew build` | Full build: compiles, runs `check` (see below), and assembles output. |
| `./gradlew assemble` | Compiles and packages, skipping all verification tasks. |
| `./gradlew clean` | Deletes the `build/` directory. |

### Tests

| Command | Description |
|---|---|
| `./gradlew test` | Runs unit tests (`src/test/java`). |
| `./gradlew intTest` | Runs integration tests (`src/int-test/java`). |
| `./gradlew test intTest` | Runs both. |

Test results: `build/test-results/test/` and `build/test-results/intTest/`.
Human-readable HTML: `build/reports/tests/test/index.html` and `build/reports/tests/intTest/index.html`.

### Code coverage (JaCoCo)

| Command | Description |
|---|---|
| `./gradlew jacocoTestReport` | Coverage report for unit tests. |
| `./gradlew jacocoIntTestReport` | Coverage report for integration tests. |

Both produce XML and HTML output:
- `build/reports/jacoco/test/html/index.html` (+ `build/reports/jacoco/test/jacocoTestReport.xml`)
- `build/reports/jacoco/jacocoIntTestReport/html/index.html` (+ the matching `.xml`)

These run automatically as part of `./gradlew check`.

### Formatting (Spotless + google-java-format)

| Command | Description |
|---|---|
| `./gradlew spotlessCheck` | Fails if any Java file isn't correctly formatted. Runs as part of `check`. |
| `./gradlew spotlessApply` | Reformats all Java files in place. |

Run `spotlessApply` before committing if `check` fails on formatting.

### Static analysis

| Command | Description |
|---|---|
| `./gradlew checkstyleMain checkstyleTest checkstyleIntTest` | Checkstyle, per source set. Config: `config/checkstyle/checkstyle.xml`. |
| `./gradlew pmdMain pmdTest pmdIntTest` | PMD, per source set. Config: `config/pmd/ruleset.xml`. |

Checkstyle is scoped to things the formatter doesn't cover (naming, imports, coding problems, class design). PMD covers best-practice/error-prone/design/performance/security categories; codestyle rules are excluded since Spotless owns formatting.

Reports: `build/reports/checkstyle/*.html` and `build/reports/pmd/*.html`.

## Pull requests

`.github/pull_request_template.md` is pre-filled into every new PR on GitHub. It expects each PR to list the Jira story key(s) it covers, per the team's Jira workflow.

## Useful one-liners

```shell
./gradlew check                 # everything: build, tests, coverage, format check, static analysis
./gradlew spotlessApply check   # auto-fix formatting, then verify everything else
./gradlew test intTest --info   # run all tests with verbose output
./gradlew tasks                 # list all available tasks
```
