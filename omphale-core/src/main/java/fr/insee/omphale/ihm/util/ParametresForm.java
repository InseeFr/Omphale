package fr.insee.omphale.ihm.util;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Cette classe permet de faire le lien avec le fichier de paramètres FORMResources
 *
 */
public class ParametresForm {
	private static final String BUNDLE_NAME = "formResources"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private ParametresForm() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
