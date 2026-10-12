# Project
templateJavaProject — Java template
Gradle (Kotlin DSL), Java 25 (see `gradle/gradle-daemon-jvm.properties`), JUnit 6, SLF4J/Logback

## Build Commands
- Verify everything: `./gradlew check` (compile, format check, Checkstyle, PMD, unit + integration tests, coverage gate)
- Test:        `./gradlew test`
- Integration test: `./gradlew intTest`
- Format:      `./gradlew spotlessApply`

## Package Structure
com.example.app   — application code (`src/main`)
src/test          — unit tests (JUnit 6), run by `test`
src/int-test      — integration tests, run by `intTest`, not part of `test`

## Key Conventions
- Logging via SLF4J (`LoggerFactory`) — never `System.out`/`System.err`
- Formatting via Spotless + google-java-format — run `spotlessApply`, don't hand-format
- Coverage gate requires 80% line / 70% branch, combined across unit + integration tests
- PRs must list Jira story keys per `.github/pull_request_template.md`
