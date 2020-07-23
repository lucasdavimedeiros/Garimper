package com.garimper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML());
        stage.setScene(scene);
        stage.setTitle("Garimper");
        stage.getIcons().add(new Image(App.class.getResource("olx-logo.png").toString()));
        stage.setResizable(false);
        stage.show();
    }

    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("window.fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}