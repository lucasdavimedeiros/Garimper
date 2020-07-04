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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Bot {

    private static final String ADD_PREVIEW = "_36Lgj";
    private static final String INPUT_TEXT = "_3FRCZ";
    private static final String PRICE = "jqSHIm";
    private static final String TITLE_LINK = "iZLVht";
    private static final String REMEMBER_ME = "rememberMe";

    private String txtOlxLink;
    private String txtKeyWords;
    private Label lblStatus;

    private WebDriver driver;
    private WebDriverWait wait;
    private List<WebElement> inputsText;
    private WebElement chatInputText;
    private ArrayList<String> tabs;
    private List<String> keyWordsList;
    private List<String> filteredLinks = new ArrayList<>();
    private List<String> sentLinks = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();

    public Bot(TextField txtOlxLink, TextField txtKeyWords, Label lblStatus) {
        this.txtOlxLink = txtOlxLink.getText();
        this.txtKeyWords = txtKeyWords.getText();
        this.lblStatus = lblStatus;

        prepareToWatchPage();
        initPageWatcher();
    }

    private void prepareToWatchPage() {
        setGUIStatusLabel(lblStatus, "Iniciando Firefox");
        setBrowserToUse(Browsers.FIREFOX);
        openBrowser(new FirefoxDriver());
        initDriverWait();
        setGUIStatusLabel(lblStatus, "Acessando WhatsApp Web");
        openWhatsAppWeb();
        waitUntilKeepMeConnectedCheckBoxIsVisible();
        waitDeviceConnection();
        setGUIStatusLabel(lblStatus, "Conectado!");
        sleepInSeconds(1);
        waitUntilSearchInputTextIsVisible();
        setGUIStatusLabel(lblStatus, "Selecione o contato e escreva '#OK' na caixa de texto da conversa");
        waitUntilChatInputTextIsVisible();
        waitUserWriteInChatInputText();
        writeTimerInChatInputText();
        setGUIStatusLabel(getLblStatus(), "Acessando OLX");
        openOlxOnNewTab();
        keyWordsList = Util.getKeyWordsAsList(getTxtKeyWords());
    }

    private void initPageWatcher() {
        setGUIStatusLabel(lblStatus, "Buscando por: " + keyWordsList);
        while (true) {
            List<WebElement> adsTitleElements = getAdsTitleElementsFromFirstPage();
            filterResults(adsTitleElements);

            for (Map.Entry<String, String> mapItem : map.entrySet()) {
                if (!sentLinks.contains(mapItem.getKey())) {
                    switchToWhatsAppTab();
                    sentLinks.add(mapItem.getKey());
                    chatInputText.sendKeys(mapItem.getKey());
                    waitUntilLinkPreviewIsVisible();
                    pasteLinkInChatTextField();
                    waitUntilLinkPreviewIsNotVisible();
                    chatInputText.sendKeys(mapItem.getValue());
                    pasteLinkInChatTextField();
                }
            }
            switchToOlxTab();
            sleepInSeconds(5);
            refreshPage();
        }
    }

    private void refreshPage() {
        driver.navigate().refresh();
    }

    private void switchToOlxTab() {
        driver.switchTo().window(tabs.get(1));
    }

    private void pasteLinkInChatTextField() {
        chatInputText.sendKeys(Keys.ENTER);
    }

    private void waitUntilLinkPreviewIsNotVisible() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className(ADD_PREVIEW)));
    }

    private void waitUntilLinkPreviewIsVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(ADD_PREVIEW)));
    }

    private void switchToWhatsAppTab() {
        driver.switchTo().window(tabs.get(0));
    }

    private void filterResults(List<WebElement> adsTitleElements) {
        for (WebElement adTitleElement : adsTitleElements) {
            checkIfAdTitleMatchWithAnyKeyword(adTitleElement);
        }
    }

    private void checkIfAdTitleMatchWithAnyKeyword(WebElement adTitleElement) {
        for (String keyWord : keyWordsList) {
            String title = adTitleElement.getAttribute("title");
            if (title.toLowerCase().contains(keyWord)) {
                addAdToFilteredList(adTitleElement);
            }
        }
    }

    private void addAdToFilteredList(WebElement adTitleElement) {
        String link = adTitleElement.getAttribute("href");
        if (!filteredLinks.contains(link)) {
            filteredLinks.add(link);
            String price = getAdPrice(adTitleElement);
            map.put(link, price);
        }
    }

    private String getAdPrice(WebElement titleLink) {
        return titleLink.findElement(By.className(PRICE)).getText();
    }

    private List<WebElement> getAdsTitleElementsFromFirstPage() {
        return driver.findElements(By.className(TITLE_LINK));
    }

    private void openWhatsAppWeb() {
        openURL("https://web.whatsapp.com");
    }

    private void openOlxOnNewTab() {
        ((JavascriptExecutor) driver).executeScript("window.open()");
        tabs = new ArrayList<>(driver.getWindowHandles());
        switchToOlxTab();
        openURL(getTxtOlxLink());
    }

    private void writeTimerInChatInputText() {
        for (int i = 3; i > 0; i--) {
            chatInputText.click();
            chatInputText.clear();
            chatInputText.sendKeys("Acessando OLX em " + i);
            sleepInSeconds(1);
        }
        chatInputText.clear();
    }

    private void waitUserWriteInChatInputText() {
        do {
            sleepInSeconds(1);
            for (WebElement inputText : inputsText) {
                String text = inputText.getText();
                if (text.equalsIgnoreCase("#OK")) {
                    chatInputText = inputText;
                }
            }
        } while (chatInputText == null);
    }

    private void waitUntilChatInputTextIsVisible() {
        boolean isChatInputTextVisible;
        do {
            sleepInSeconds(1);
            inputsText = driver.findElements(By.className(INPUT_TEXT));
            isChatInputTextVisible = inputsText.size() >= 2;
        } while (!isChatInputTextVisible);
    }

    private void openBrowser(RemoteWebDriver remoteWebDriver) {
        driver = remoteWebDriver;
    }

    private void waitDeviceConnection() {
        setGUIStatusLabel(lblStatus, "Aguardando conexão com o Smartphone");
        boolean rememberMeIsPresent = true;
        while (rememberMeIsPresent) {
            sleepInSeconds(1);
            rememberMeIsPresent = !driver.findElements(By.name(REMEMBER_ME)).isEmpty();
        }
    }

    private void waitUntilKeepMeConnectedCheckBoxIsVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(REMEMBER_ME)));
    }

    private void waitUntilSearchInputTextIsVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(INPUT_TEXT)));
    }

    private void openURL(String url) {
        driver.get(url);
    }

    private void initDriverWait() {
        wait = new WebDriverWait(driver, 15);
    }

    private void setBrowserToUse(Browsers browser) {
        System.setProperty(browser.getValue(), Util.getBrowserDriverPath(browser));
    }

    private void setGUIStatusLabel(Label label, String text) {
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

    public String getTxtOlxLink() {
        return txtOlxLink;
    }

    public String getTxtKeyWords() {
        return txtKeyWords;
    }

    public Label getLblStatus() {
        return lblStatus;
    }

    private Bot() {
    }
}
