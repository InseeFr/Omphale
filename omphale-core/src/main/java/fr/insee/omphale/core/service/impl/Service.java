package fr.insee.omphale.core.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import fr.insee.config.InseeConfig;
import fr.insee.omphale.dao.factory.DAOFactory;
import fr.insee.omphale.ihm.util.ParametresForm;
import fr.insee.omphale.ihm.util.WebOmphaleConfig;

public class Service {
	static public DAOFactory daoFactory;
	static {
		daoFactory = DAOFactory.instance(DAOFactory.init());
	}

	public static int NB_MAX_LISTE_ORACLE = recupererMax();

	/**
	 * Affiche une liste pour les requetes SQL : entre quotes, avec une virgule
	 * entre chaque élément
	 * 
	 * @param liste
	 * @return
	 */
	public static String afficherListeSQL(List<String> liste) {
		String retour = "'" + liste.get(0) + "'";
		for (String string : liste.subList(1, liste.size())) {
			retour = retour + ",'" + string + "'";
		}
		return retour;
	}

	/**
	 * Affiche une liste de String avec des virgules entre chaque élément
	 */

	public static String afficherListe(List<String> liste) {
		String retour = liste.get(0);
		for (String string : liste.subList(1, liste.size())) {
			retour = retour + "," + string;
		}
		return retour;
	}

	/**
	 * Calcule en combien de paquets on doit diviser notre liste pour qu'elle
	 * soit acceptée par Oracle
	 * 
	 * @param liste
	 * @return
	 */

	public static int calculNbPaquets(@SuppressWarnings("rawtypes") List liste) {
		int taille = liste == null ? 0 : liste.size();
		if (taille % NB_MAX_LISTE_ORACLE == 0) {
			return taille / NB_MAX_LISTE_ORACLE;
		} else {
			return (taille / NB_MAX_LISTE_ORACLE) + 1;
		}
	}

	/**
	 * Récupère la longueur maximale d'une liste d'éléments en SQL (valeur
	 * placée dans parametres.properties)
	 * 
	 * @return
	 */
	private static int recupererMax() {
		String max = (String) InseeConfig.getConfig().getProperty(
				"fr.insee.sgbd.max.elements.liste");
		return Integer.parseInt(max);
	}

	/**
	 * Renvoie un identifiant de type integer1 + "||" + integer2 + .. à partir
	 * d'un tableau d'integer {integer1, integer2, ..} <br>
	 * Ex. : <br>
	 * getId({new Integer(100), new Integer(101)}) --> "100||101"
	 * 
	 * @param libelle
	 *            tableau de libellés
	 * @param tableau
	 *            tableau d'integer
	 * @return chaîne de caractères
	 */
	public static String getId(List<Integer> tableau) {
		if (tableau != null && tableau.size() > 0) {
			StringBuffer s = new StringBuffer();
			for (int i = 0; i < tableau.size(); i++) {
				if (tableau.get(i) != null) {
					s.append(tableau.get(i).toString());
				}
				if (i < tableau.size() - 1) {
					s.append("||");
				}
			}
			return s.toString();
		} else {
			return "";
		}
	}


	/**
	 * 
	 * ex. {new Integer(1), new Integer(2), new Integer(3)} -->
	 * "('1', '2', '3')"
	 * 
	 * Renvoie la chaîne de caractères "('integer1', 'integer2', .. )" à partir
	 * d'un tableau d'integer {integer1, integer2, ..} <br>
	 * ex. {new Integer(1), new Integer(2), new Integer(3)} --> ('1', '2', '3')
	 * 
	 * @param tableau
	 *            tableau de Integer
	 * @return chaîne de caractères
	 */
	public static String tableauToString(List<Integer> tableau) {

		StringBuffer str = new StringBuffer();
		int i = 0;
		str.append("(");
		for (Integer identifiant : tableau) {
			str.append("'");
			str.append(identifiant.toString());
			str.append("'");
			if (i < tableau.size() - 1) {
				str.append(", ");
			}
			i++;
		}
		str.append(")");
		return str.toString();
	}

	/**
	 * Supprime les éléments de tableau2 dans tableau1
	 * 
	 * @param tableau1
	 * @param tableau2
	 * @return tableau1 - tableau2
	 */
	public static Integer[] remove(Integer[] tableau1, Integer[] tableau2) {

		if (tableau1 != null) {
			List<Integer> list = new ArrayList<Integer>(Arrays.asList(tableau1));
			if (tableau2 != null && tableau2.length > 0) {
				list.removeAll(Arrays.asList(tableau2));
			}
			if (list == null || list.size() == 0) {
				return new Integer[0];
			}
			Integer[] tableau = new Integer[list.size()];
			for (int i = 0; i < list.size(); i++) {
				tableau[i] = list.get(i);
			}
			return tableau;
		} else {
			return null;
		}
	}

	public static int getMin(Set<Integer> entiers) {
		if (entiers == null || entiers.isEmpty()) {
			return 0;
		}
		int min = new ArrayList<Integer>(entiers).get(0);
		for (Integer entier : entiers) {
			if (entier < min) {
				min = entier;
			}
		}
		return min;
	}

	public static int getMax(Set<Integer> entiers) {
		if (entiers == null || entiers.isEmpty()) {
			return 0;
		}
		int max = new ArrayList<Integer>(entiers).get(0);
		for (Integer entier : entiers) {
			if (entier > max) {
				max = entier;
			}
		}
		return max;
	}

	/**
	 * Teste le format de nom des entités (20 caractères max, pas de blanc
	 * souligné au début, caractères acceptés : lettres, chiffres, - et _
	 * 
	 * @param nom
	 * @return
	 */
	public static boolean testFormatNom(String nom) {
		if (nom.length() > 20) {
			return false;
		}
		if (nom.charAt(0) == '_') {
			return false;
		} else if (!Pattern.matches("[a-zA-Z_0-9-]*", nom)) {
			return false;
		}
		return true;
	}

	/**
	 * Teste le format d'une année (4 chiffres max, sans caractères spéciaux
	 * avant ou après et sans virgule)
	 * 
	 * @param nom
	 * @return
	 */
	public static boolean testFormatAnnee(String annee) {
		if (!Pattern.matches("^[0-9]{4}$", annee)) {
			return false;
		}
		return true;
	}

	public static boolean testFormatAgeIsNum(String age) {
		if (age == null || ("").equals(age.trim())
				|| !StringUtils.isNumeric(age.trim())) {
			return false;
		}
		return true;
	}

	/**
	 * Teste le format de l'âge (de -1 à 120 - valeur en properties)
	 * 
	 * @param nom
	 * @return
	 */
	public static boolean testFormatAge(String age) {
		try {
			Integer.valueOf(age);
			if (Integer.valueOf(age) < WebOmphaleConfig.getConfig().getAgeMin()
					|| Integer.valueOf(age) > WebOmphaleConfig.getConfig()
							.getAgeMax()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Vérifie que l'âge est compris entre les paramètres min et max
	 * 
	 * @param age
	 * @param min
	 * @param max
	 * @return boolean
	 */
	public static boolean testGlobalAge(String age, int min, int max) {
		try {
			Integer.valueOf(age);
			if (Integer.valueOf(age) < min || Integer.valueOf(age) > max) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	/**
	 * Vérifie que la valeur du paramètre choisi respecte un format type
	 * 
	 * @param codeParam
	 * @param valParam
	 * @return
	 */
	public static boolean testParametres(String codeParam, String valParam) {
		if (codeParam == "AN_CIBLE") {
			if (!Pattern.matches("^[0-9]{4}$", valParam)) {
				return false;
			}
		}
		if ("GAIN_ICF".equals(codeParam)) {
			if (!Pattern.matches("^\\-?[0-9]\\.?[0-9]?$", valParam)) {
				return false;
			}
		}
		if ("GAIN_EV".equals(codeParam)) {
			if (!Pattern.matches("^\\-?[0-9]?[0-9]$", valParam)) {
				return false;
			}
		}
		if ("COEF_HOM".equals(codeParam)) {
			if (!Pattern.matches("^[0-5]$", valParam)) {
				return false;
			}
		}
		if ("COEF_TRAN".equals(codeParam)) {
			if (!Pattern.matches("^\\-?[0-9]\\.?[0-9]?$", valParam)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Teste le format de l'âge (de -1 à 120 - valeur en properties)
	 * 
	 * @param nom
	 * @return
	 */
	public static boolean testFormatAgeMere(String age) {
		try {
			Integer.valueOf(age);
			if (Integer.valueOf(age) < WebOmphaleConfig.getConfig()
					.getAgeMinMere()
					|| Integer.valueOf(age) > WebOmphaleConfig.getConfig()
							.getAgeMaxMere()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Ramène un tableau de chaîne de caractères à partir d'une chaîne de
	 * caractères de type "aa||bb||cc" <br>
	 * ex. "1||2" --> {"1", "2"}
	 */
	public static String[] getTableau(String id) {
		if (id != null && id.length() > 0) {
			return id.split("\\|\\|");
		} else {
			return new String[0];
		}
	}

	/**
	 * Ramène un tableau d'Integer à partir d'une chaîne de caractères de type
	 * "1||2||3" <br>
	 * ex. "1||2" --> {new Integer(1), new Integer(2)}
	 */
	public static List<Integer> getTableauInteger(String id) {

		String[] str = getTableau(id);
		List<Integer> tableau = new ArrayList<Integer>();
		for (int i = 0; i < str.length; i++) {
			tableau.add(new Integer(str[i]));
		}
		return tableau;
	}

	/**
	 * Récupère le libellé de la plage des sexes en fonction de sexeDebut et
	 * sexeFin
	 * 
	 * @param
	 * @return
	 */
	public static String formaterSexe(int sexeDebut, int sexeFin) {
		if (sexeDebut == 1 && sexeFin == 1) {
			return "Homme";
		} else if (sexeDebut == 1 && sexeFin == 2) {
			return "Homme & Femme";
		} else {
			return "Femme";
		}
	}

	/**
	 * Retourne une map qui sert à faire les filtres standards / non standards
	 * dans toutes les pages de gestion
	 * 
	 * @return
	 */
	public Map<Integer, String> getTypes() {
		Map<Integer, String> types = new HashMap<Integer, String>();
		types.put(0, ParametresForm.getString("nonStandard.libelle"));
		types.put(1, ParametresForm.getString("standard.libelle"));
		return types;
	}

	
	
	/**
	 * vérifie que la date d'exécution est une semaine avant la date d'aujourd'hui
	 * 
	 * @param today
	 * @param dateExecution
	 * @return boolean
	 */
	public static boolean isOneWeekOld(Date today, Date dateExecution) {
		Calendar calToday = new GregorianCalendar();
		calToday.setTime(today);

		Calendar calLastWeek = new GregorianCalendar();
		calLastWeek.set(calToday.get(Calendar.YEAR), calToday
				.get(Calendar.MONTH), calToday.get(Calendar.DATE));

		calLastWeek.set(Calendar.DAY_OF_YEAR, calToday
				.get(Calendar.DAY_OF_YEAR) - 7);

		Calendar calExec = new GregorianCalendar();
		calExec.setTime(dateExecution);

		return calExec.before(calLastWeek);
	}
}