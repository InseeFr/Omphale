package fr.insee.omphale.ihm.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Cette classe permet de faire le lien avec le fichier de paramètres de développement
 *
 */
public class Parametres {
	private static final String BUNDLE_NAME = "param";
	public static final String NOM_VERSION_DEV = "Développement";
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
