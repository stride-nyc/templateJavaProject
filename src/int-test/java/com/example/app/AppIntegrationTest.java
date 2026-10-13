package com.example.app;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
