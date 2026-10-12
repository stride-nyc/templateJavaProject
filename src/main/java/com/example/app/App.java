package com.example.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class App {

  private static final Logger LOG = LoggerFactory.getLogger(App.class);

  private App() {}

  public static void main(String[] args) {
    LOG.info(greeting());
  }

  static String greeting() {
    return "Hello, world!";
  }
}
