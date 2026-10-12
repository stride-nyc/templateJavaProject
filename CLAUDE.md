# CLAUDE.md

Guidance for Claude Code (and other agents) working in this repository.

## What this is

A Java project template: Gradle (Kotlin DSL), Checkstyle, PMD, google-java-format (via Spotless), JaCoCo, SLF4J/Logback, and a unit/integration test split (`src/test` vs `src/int-test`). See `README.md` for the full command reference.

## Conventions to follow

- **Logging: SLF4J, never `System.out`/`System.err`.** Get a logger with `private static final Logger LOG = LoggerFactory.getLogger(ThisClass.class);` and use parameterized messages (`LOG.info("thing happened: {}", value)`), not string concatenation. PMD's `SystemPrintln` rule (bestpractices, unexcluded) will fail the build on raw console I/O in `src/main`.
- **Formatting is not manual.** Don't hand-format Java files or argue with Checkstyle about whitespace/import order — run `./gradlew spotlessApply` and let google-java-format do it. Checkstyle and PMD are deliberately scoped to things the formatter doesn't cover (naming, imports, coding problems, bug patterns); they're not fighting the formatter.
- **Unit vs integration tests are not interchangeable.** `src/test` (the `test` task) is for fast, isolated unit tests. `src/int-test` (the `intTest` task) is for broader/slower tests. Don't add integration-style tests under `src/test`, and don't assume `./gradlew test` alone exercises integration coverage — see the JaCoCo note below.
- **The 80%-line / 70%-branch coverage gate (`jacocoTestCoverageVerification`) is intentionally wired to combined unit + integration execution data** (`executionData(tasks.test.get(), intTestTask.get())` in `build.gradle.kts`). If you add a new source set or test task, JaCoCo's default conventions won't pick it up automatically — you have to wire its exec data in explicitly, the same way, or coverage numbers will silently look wrong (lower than reality) without the gate failing to catch it, or vice versa.
- **PR descriptions must list Jira story keys** per `.github/pull_request_template.md` — that's the org's convention (see the Trident Jira Project Management Playbook), not something to improvise away.

## Known gotchas / do-not-"fix"-without-checking

- **`googleJavaFormat("1.28.0")` is pinned deliberately, not stale.** Spotless 8.9.0's reflective glue adapter (`GoogleJavaFormatFormatterFunc`) breaks against newer google-java-format releases (1.37.0 reproducibly threw `InvocationTargetException` in testing here). If you bump Spotless, re-verify the gjf version still works before bumping it too — don't just grab "latest" for both.
- **`gradle/gradle-daemon-jvm.properties` pins the Gradle daemon itself to JDK 25**, separately from the project's `java.toolchain` (also 25). This was necessary because an ambient JDK 27 on the dev machine caused flaky in-process formatter failures. Don't remove this file assuming the project toolchain setting alone is enough — they serve different purposes (daemon JVM vs. compile/run JVM).
- **PMD's `classpath` is explicitly given the toolchain's `jrt-fs.jar`** (`build.gradle.kts`, near the PMD config) to stop it from guessing "current platform" JDK classes and printing a warning every run. If PMD warnings about aux classpath reappear after a Gradle/PMD version bump, check this block still resolves correctly rather than re-adding `PMD_JAVA_DISABLE_AUX_CLASSPATH_WARNINGS` workarounds (that env var doesn't reliably propagate into PMD's forked worker process anyway — verified in testing).
- **`LoosePackageCoupling` and `GuardLogStatement` are deliberately excluded from PMD's design/bestpractices rules** (`config/pmd/ruleset.xml`) — the former needs explicit package config to be meaningful, the latter is largely redundant once logging is parameterized via SLF4J. Don't silently re-add them without updating the ruleset's comments explaining why they're off.

## Before considering a change "done"

Run `./gradlew check` (compiles, formats-checks, Checkstyle, PMD, unit + integration tests, coverage gate) clean before reporting work finished. CI (`.github/workflows/ci.yml`) runs the same tasks as separate matrix jobs so each shows up as its own PR check.
