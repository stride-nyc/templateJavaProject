package com.example.app;

import java.util.List; // Checkstyle: UnusedImports

/** Deliberately broken class: each tool in `./gradlew check` should complain. */
public class Violations {

    static final int max_size = 10; // Checkstyle: ConstantName

    public boolean sameName(String a, String b) { // Spotless: bad indentation
        return a == b; // SpotBugs: comparing Strings with ==
    }

    public void swallow() {
        try {
            Integer.parseInt("x");
        } catch (NumberFormatException e) {
        } // PMD: EmptyCatchBlock
    }

    // JaCoCo: no test covers this class, so coverage falls below 80%
}
