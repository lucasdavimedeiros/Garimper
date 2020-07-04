package com.garimper.selenium;

public enum Browsers {

    FIREFOX("webdriver.gecko.driver");

    private String value;

    public String getValue() {
        return value;
    }

    private Browsers(String value) {
        this.value = value;
    }
}
