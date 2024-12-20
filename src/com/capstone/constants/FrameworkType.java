package com.capstone.constants;

public enum FrameworkType {
    SELENIUM("selenium"),
    SELENIDE("selenide"),
    JDI("jdi");

    private final String typeName;

    FrameworkType(String name) {
        this.typeName = name;
    }
}