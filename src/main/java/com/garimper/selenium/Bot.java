package com.garimper.selenium;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Bot {

    private static final String ADD_PREVIEW = "_36Lgj";
    private static final String INPUT_TEXT = "_3FRCZ";
    private static final String PRICE = "jqSHIm";
    private static final String TITLE_LINK = "iZLVht";
    private static final String REMEMBER_ME = "rememberMe";

    public Bot(TextField txtOlxLink, TextField txtKeyWords, Label lblStatus) {
        String olxUrlInput = txtOlxLink.getText();

        String keyWordsInput = txtKeyWords.getText();

        setLabelText(lblStatus, "Iniciando Firefox");

        System.setProperty("webdriver.gecko.driver", Util.getFirefoxDriverPath());
        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, 15);

        setLabelText(lblStatus, "Acessando WhatsApp Web");
        driver.get("https://web.whatsapp.com");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(REMEMBER_ME)));

        setLabelText(lblStatus, "Aguardando conexão com o Smartphone");
        boolean rememberMeIsPresent = true;

        while (rememberMeIsPresent) {
            sleepInSeconds(1);
            rememberMeIsPresent = !driver.findElements(By.name(REMEMBER_ME)).isEmpty();
        }

        setLabelText(lblStatus, "Conectado!");
        sleepInSeconds(1);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(INPUT_TEXT)));

        setLabelText(lblStatus, "Selecione o contato e escreva '#OK' na caixa de texto da conversa");

        boolean noSecondInputText = true;

        while (noSecondInputText) {
            sleepInSeconds(1);
            noSecondInputText = driver.findElements(By.className(INPUT_TEXT)).size() <= 1;
        }

        WebElement contactInputText = null;

        while (contactInputText == null) {
            sleepInSeconds(1);
            List<WebElement> inputTexts = driver.findElements(By.className(INPUT_TEXT));

            for (WebElement inputText : inputTexts) {
                String text = inputText.getText();

                if (text.equalsIgnoreCase("#OK")) {
                    contactInputText = inputText;
                }
            }
        }

        for (int i = 3; i > 0; i--) {
            contactInputText.click();
            contactInputText.clear();
            contactInputText.sendKeys("Acessando OLX em " + i);
            sleepInSeconds(1);
        }

        contactInputText.clear();

        setLabelText(lblStatus, "Acessando OLX");
        ((JavascriptExecutor) driver).executeScript("window.open()");

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        driver.get(olxUrlInput);

        List<String> keyWordsList = Util.getKeyWordsAsList(keyWordsInput);
        List<String> filteredLinks = new ArrayList<>();
        List<String> sentLinks = new ArrayList<>();
        Map<String, String> map = new HashMap<>();

        setLabelText(lblStatus, "Buscando por: " + keyWordsList);

        while (true) {
            List<WebElement> titleLinks = driver.findElements(By.className(TITLE_LINK));

            for (WebElement titleLink : titleLinks) {
                String title = titleLink.getAttribute("title");
                String price = titleLink.findElement(By.className(PRICE)).getText();

                for (String keyWord : keyWordsList) {

                    if (title.toLowerCase().contains(keyWord)) {

                        String link = titleLink.getAttribute("href");

                        if (!filteredLinks.contains(link)) {
                            filteredLinks.add(link);
                            map.put(link, price);
                        }
                    }
                }
            }
            for (Map.Entry<String, String> mapItem : map.entrySet()) {

                if (!sentLinks.contains(mapItem.getKey())) {
                    driver.switchTo().window(tabs.get(0));
                    sentLinks.add(mapItem.getKey());
                    contactInputText.sendKeys(mapItem.getKey());
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(ADD_PREVIEW)));
                    contactInputText.sendKeys(Keys.ENTER);
                    wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(ADD_PREVIEW)));
                    contactInputText.sendKeys(mapItem.getValue());
                    contactInputText.sendKeys(Keys.ENTER);
                }
            }
            driver.switchTo().window(tabs.get(1));
            sleepInSeconds(5);
            driver.navigate().refresh();
        }
    }

    private void setLabelText(Label label, String text) {
        Platform.runLater(() -> {
            label.setText(text);
        });
    }

    private void sleepInSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sleepInMinutes(int minutes) {
        try {
            TimeUnit.MINUTES.sleep(minutes);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Bot() {
    }
}
