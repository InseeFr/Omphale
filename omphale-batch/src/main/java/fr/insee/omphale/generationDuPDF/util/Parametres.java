package fr.insee.omphale.generationDuPDF.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class Parametres {
	private static final String BUNDLE_NAME = "param";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Parametres() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getStringIfExist(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return "";
		}
	}
}
