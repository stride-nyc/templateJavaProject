package com.example.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppTest {

  @Test
  void greetingReturnsHelloWorld() {
    assertEquals("Hello, world!", App.greeting(), "greeting() should return the expected message");
  }

  @Test
  void greetingReturnsHelloWorldFailure() {
    assertEquals("Helloooo, world!", App.greeting(), "greeting() should return the expected message");
  }
}
