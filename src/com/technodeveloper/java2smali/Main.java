package com.technodeveloper.java2smali;

public class Main {

    private static final String WELCOME_MESSAGE = "### Java2Smali (Java version) ###";

    public static void main(String[] args) {
        new Main().init();
    }

    private void init() {
        print(WELCOME_MESSAGE);
    }

    private static void print(String s) {
        System.out.println(s);
    }
}
