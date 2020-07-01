package com.garimper.controller;

import com.garimper.selenium.Bot;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.StringUtils;

public class WindowController {

    @FXML
    private TextField txtOlxLink;

    @FXML
    private TextField txtKeyWords;

    @FXML
    private Label lblStatus;

    @FXML
    public void onBtnGarimparAction() {
        if (isTextFieldEmpty(txtOlxLink) || isTextFieldEmpty(txtKeyWords)) {
            System.out.println("Empty");
        } else {
            new Thread(() -> new Bot(txtOlxLink, txtKeyWords, lblStatus)).start();
        }
    }

    @FXML
    private void handleOnKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onBtnGarimparAction();
        }
    }

    private boolean isTextFieldEmpty(TextField textField) {
        return StringUtils.isEmpty(textField.getText());
    }
}
