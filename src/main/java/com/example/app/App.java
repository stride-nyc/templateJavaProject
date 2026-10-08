package com.example.app;

public final class App {

  private App() {}

  public static void main(String[] args) {
    System.out.println(greeting());
  }

  static String greeting() {
      var phrase = "Hello, world!";
    return phrase;
  }
}
