module com.garimper {
    requires javafx.controls;
    requires javafx.fxml;
    requires selenium.api;
    requires selenium.firefox.driver;
    requires selenium.support;

    opens com.garimper.controller to javafx.fxml;
    exports com.garimper;
}