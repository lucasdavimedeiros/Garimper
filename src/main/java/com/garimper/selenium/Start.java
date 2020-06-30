package com.garimper.selenium;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.garimper.selenium.Util;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Start {

	private static final String ADD_PREVIEW = "_36Lgj";
	private static final String INPUT_TEXT = "_3FRCZ";
	private static final String PRICE = "jqSHIm";
	private static final String TITLE_LINK = "iZLVht";
	private static final String REMEMBER_ME = "rememberMe";

	public static void main(String[] args) throws InterruptedException {

		Scanner in = new Scanner(System.in);

		System.out.print("OLX Link: ");
		String olxUrlInput = in.nextLine();

		System.out.print("Palavras-chave (separadas por vírgula): ");
		String keyWordsInput = in.nextLine();

		in.close();

		System.out.println("Iniciando Firefox");

		System.setProperty("webdriver.gecko.driver", Util.getFirefoxDriverPath());
		WebDriver driver = new FirefoxDriver();
		WebDriverWait wait = new WebDriverWait(driver, 15);

		System.out.println("Acessando WhatsApp Web");
		driver.get("https://web.whatsapp.com");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(REMEMBER_ME)));

		System.out.print("Aguardando conexão com o Smartphone");
		boolean rememberMeIsPresent = true;

		while (rememberMeIsPresent) {
			System.out.print(".");
			TimeUnit.SECONDS.sleep(1);
			rememberMeIsPresent = !driver.findElements(By.name(REMEMBER_ME)).isEmpty();
		}

		System.out.println("\nConectado!");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(INPUT_TEXT)));

		System.out.print("Selecione o contato e escreva '#OK' na caixa de texto da conversa");

		boolean noSecondInputText = true;

		while (noSecondInputText) {
			System.out.print(".");
			TimeUnit.SECONDS.sleep(1);
			noSecondInputText = driver.findElements(By.className(INPUT_TEXT)).size() <= 1;
		}

		WebElement contactInputText = null;

		while (contactInputText == null) {
			System.out.print(".");
			TimeUnit.SECONDS.sleep(1);
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
			TimeUnit.SECONDS.sleep(1);
		}

		contactInputText.clear();

		System.out.println("\nAcessando OLX");
		((JavascriptExecutor) driver).executeScript("window.open()");

		ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));

		driver.get(olxUrlInput);

		List<String> keyWordsList = Util.getKeyWordsAsList(keyWordsInput);
		List<String> filteredLinks = new ArrayList<>();
		List<String> sentLinks = new ArrayList<>();
		Map<String, String> map = new HashMap<>();

		System.out.println("Buscando por: " + keyWordsList);

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
			for (Entry<String, String> mapItem : map.entrySet()) {

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
			TimeUnit.SECONDS.sleep(5);
			driver.navigate().refresh();
		}
	}
}
