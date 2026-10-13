package com.example.app;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AppIntegrationTest {

  private final LogCaptor logCaptor = LogCaptor.forClass(App.class);

  @Test
  void mainRunsWithoutThrowing() {
    assertDoesNotThrow(() -> App.main(new String[] {}));
  }

  @Test
  void mainLogsGreeting() {
    App.main(new String[] {});
    assertEquals("Hello, world!", logCaptor.getInfoLogs().getFirst());
  }

  @Test
  void mainNoErrorMessages() {
    App.main(new String[] {});
    assertTrue(logCaptor.getErrorLogs().isEmpty());
  }
}
