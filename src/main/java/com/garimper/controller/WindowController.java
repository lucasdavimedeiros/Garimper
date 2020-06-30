package com.garimper.controller;

import java.io.IOException;

import com.garimper.selenium.Bot;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WindowController {

    @FXML
    private TextField txtOlxLink;

    @FXML
    private TextField txtKeyWords;

    @FXML
    private Button btnGarimpar;

    @FXML
    private Label lblStatus;

    @FXML
    public void onBtnGarimparAction() {
        new Thread(() -> new Bot(txtOlxLink, txtKeyWords, lblStatus)).start();
    }
}
