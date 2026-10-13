package com.example.app;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.Test;

class AppIntegrationTest {

  LogCaptor logCaptor = LogCaptor.forClass(App.class);

  @Test
  void mainRunsWithoutThrowing() {
    assertDoesNotThrow(() -> App.main(new String[] {}));
    assertEquals("Hello, world!", logCaptor.getInfoLogs().getFirst());
  }
}
