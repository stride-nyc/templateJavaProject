plugins {
    java
    jacoco
    checkstyle
    pmd
    id("com.diffplug.spotless") version "8.9.0"
}

group = "com.example"
version = "0.1.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

// Integration test source set: src/int-test/java, run via the `intTest` task.
val intTest: SourceSet =
    sourceSets.create("intTest") {
        java.srcDir("src/int-test/java")
        resources.srcDir("src/int-test/resources")
        compileClasspath += sourceSets.main.get().output
        runtimeClasspath += output + sourceSets.main.get().output
    }

configurations.named("intTestImplementation") {
    extendsFrom(configurations.getByName("testImplementation"))
}
configurations.named("intTestRuntimeOnly") {
    extendsFrom(configurations.getByName("testRuntimeOnly"))
}

val junitVersion = "6.1.3"

dependencies {
    implementation("org.slf4j:slf4j-api:2.0.20")
    runtimeOnly("ch.qos.logback:logback-classic:1.6.5")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

val intTestTask =
    tasks.register<Test>("intTest") {
        description = "Runs integration tests from src/int-test/java."
        group = "verification"
        testClassesDirs = intTest.output.classesDirs
        classpath = intTest.runtimeClasspath
        useJUnitPlatform()
        shouldRunAfter(tasks.test)
    }

jacoco {
    toolVersion = "0.8.15"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

val jacocoIntTestReport =
    tasks.register<JacocoReport>("jacocoIntTestReport") {
        dependsOn(intTestTask)
        executionData(intTestTask.get())
        sourceSets(sourceSets.main.get())
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test, intTestTask)
    executionData(tasks.test.get(), intTestTask.get())
    violationRules {
        rule {
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.80".toBigDecimal()
            }
        }

        rule {
            element = "CLASS"
            excludes = listOf("*.config.*", "*.dto.*")
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal()
            }
        }
    }
}

checkstyle {
    toolVersion = "14.3.0"
    configFile = file("config/checkstyle/checkstyle.xml")
    isIgnoreFailures = false
    maxWarnings = 0
}

pmd {
    toolVersion = "7.28.0"
    isConsoleOutput = true
    ruleSets = emptyList()
    ruleSetFiles = files("config/pmd/ruleset.xml")
    isIgnoreFailures = false
}

// Give PMD the toolchain's own jrt-fs.jar explicitly so it doesn't have to guess the
// JDK's module classes from "the current platform" (and warn about it) on every run.
val javaToolchains = extensions.getByType<JavaToolchainService>()
val toolchainJrtFs =
    javaToolchains
        .launcherFor(java.toolchain)
        .map { it.metadata.installationPath.file("lib/jrt-fs.jar") }

tasks.withType<Pmd>().configureEach {
    classpath = (classpath ?: files()).plus(files(toolchainJrtFs))
}

spotless {
    java {
        target("src/*/java/**/*.java")
        googleJavaFormat("1.28.0")
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.check {
    dependsOn(intTestTask, tasks.jacocoTestReport, jacocoIntTestReport, tasks.named("spotlessCheck"), tasks.jacocoTestCoverageVerification)
}
