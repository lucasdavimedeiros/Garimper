package com.garimper.selenium;

import java.util.ArrayList;
import java.util.List;

public class Util {

	private static final String LEFT_TRIM_REGEX = "^\\s+";
	private static final String RIGHT_TRIM_REGEX = "\\s+$";

	public static String getBrowserDriverPath(Browsers browser) {

		String os = System.getProperty("os.name").toUpperCase();
		String rootPath = System.getProperty("user.dir");
		String driverPath = "";

		if (browser == Browsers.FIREFOX){
			if (os.contains("WINDOWS")) {

				driverPath = rootPath + "/geckodriver.exe";

			} else if (os.contains("MAC")) {

				driverPath = rootPath + "/geckodriver";
			}
		}
		return driverPath;
	}

	public static List<String> getKeyWordsAsList(String keyWords) {

		String[] splitedKeyWords = keyWords.split(",");
		List<String> keyWordsList = new ArrayList<>();

		for (String keyWord : splitedKeyWords) {
			keyWord = keyWord.replaceAll(LEFT_TRIM_REGEX, "").toLowerCase();
			keyWord = keyWord.replaceAll(RIGHT_TRIM_REGEX, "").toLowerCase();
			keyWordsList.add(keyWord);
		}
		return keyWordsList;
	}
}
