package com.garimper.selenium;

public enum Browsers {

    FIREFOX("webdriver.gecko.driver");

    private final String value;

    public String getValue() {
        return value;
    }

    Browsers(String value) {
        this.value = value;
    }
}
