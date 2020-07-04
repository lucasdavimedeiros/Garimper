package com.garimper.controller;

import com.garimper.selenium.Bot;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

public class WindowController {

    @FXML
    private TextField txtOlxLink;

    @FXML
    private TextField txtKeyWords;

    @FXML
    private Button btnGarimpar;

    @FXML
    private Label lblStatus;

    public void initialize() {
        txtOlxLink.setFocusTraversable(false);
        txtKeyWords.setFocusTraversable(false);
        btnGarimpar.setDisable(true);
        textFieldListenerToEnableOrDisableButton(txtOlxLink);
        textFieldListenerToEnableOrDisableButton(txtKeyWords);
    }

    @FXML
    public void onBtnGarimparAction() {
        btnGarimpar.setDisable(true);
        new Thread(() -> new Bot(txtOlxLink, txtKeyWords, lblStatus)).start();
    }

    private void textFieldListenerToEnableOrDisableButton(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean isAnyTextFieldEmpty = StringUtils.isEmpty(txtOlxLink.getText()) || StringUtils.isEmpty(txtKeyWords.getText());
            btnGarimpar.setDisable(isAnyTextFieldEmpty);
        });
    }
}
