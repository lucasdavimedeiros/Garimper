module com.garimper {
    requires javafx.controls;
    requires javafx.fxml;
    requires selenium.api;
    requires selenium.firefox.driver;
    requires selenium.support;
    requires org.apache.commons.lang3;
    requires selenium.remote.driver;

    opens com.garimper.controller to javafx.fxml;
    exports com.garimper;
}