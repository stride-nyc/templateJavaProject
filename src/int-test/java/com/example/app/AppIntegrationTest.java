package com.example.app;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class AppIntegrationTest {

  @Test
  void mainRunsWithoutThrowing() {
    assertDoesNotThrow(() -> App.main(new String[] {}));
  }
}
