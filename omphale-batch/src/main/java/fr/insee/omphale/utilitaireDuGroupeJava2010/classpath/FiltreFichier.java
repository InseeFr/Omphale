package fr.insee.omphale.utilitaireDuGroupeJava2010.classpath;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Cette classe vérifie si un objet présent dans le classpath répond au filtre
 * de nom de fichier, <b>le filtre est sensible à la casse</b>. Elle implemente
 * l'interface {@link java.io.FileFilter} <br>
 * La syntaxe du filtre est la suivante :<br>
 * <ul>
 * <li>* : tous les objets</li>
 * <li>*xxx : tous les objets finissant par xxx</li>
 * <li>xxx* : tous les objets commençant par xxx</li>
 * <li>*xxx* : tous les objets contenant xxx dans le nom</li>
 * <li>xxx*yyy : tous les objets commençant par xxx et finissant par yyy</li>
 * <li>xxx : l'objet xxx</li>
 * </ul>
 *
 */
public class FiltreFichier implements FileFilter {
	// Description et extensions acceptées par le filtre
	/**
	 * Liste des filtres à appliquer aux objets
	 */
	private List<String> patterns;

	/**
	 * Constructeur.
	 */
	public FiltreFichier() {
		this.patterns = new ArrayList<String>();
	}


	public boolean accept(File file) {
		// Implémentation de FileFilter
		if (file.isDirectory() || patterns.size() == 0) {
			return true;
		}
		boolean retour = false;
		String nomFichier = file.getName().toLowerCase();
		for (String p : patterns) {
			String pattern = p.toLowerCase();
			retour = retour || testPattern(nomFichier, pattern);
		}
		return retour;
	}

	/**
	 * Teste si un nom d'objet est conforme à un pattern.
	 *
	 * @param nomObjet
	 *            Le nom de l'objet à tester.
	 * @param pattern
	 *            le pattern à appliquer à l'objet
	 * @return true si le nom de l'objet correspond au pattern, false sinon
	 */
	public static boolean testPattern(String nomObjet, String pattern) {
		boolean retour;

		boolean commenceParEtoile = false;
		boolean finiParEtoile = false;
		boolean contientEtoile = false;
		if (StringUtils.indexOf(pattern, '*') == 0) {
			commenceParEtoile = true;
		}
		if (StringUtils.lastIndexOf(pattern, '*') == (pattern.length() - 1)) {
			finiParEtoile = true;
		}
		contientEtoile = StringUtils.contains(pattern, '*');
		if (pattern.equals("*")) {
			retour = true;
			// return true;
		} else if (commenceParEtoile && finiParEtoile) {
			// extraction du pattern à tester
			String testPattern = StringUtils.substring(pattern, 1, -1);
			retour = StringUtils.contains(nomObjet, testPattern);
		} else if (commenceParEtoile) {
			// extraction du pattern à tester
			String testPattern = StringUtils.substringAfter(pattern, "*");
			retour = nomObjet.endsWith(testPattern);
		} else if (finiParEtoile) {
			// extraction du pattern à tester
			String testPattern = StringUtils.substringBeforeLast(pattern, "*");
			retour = nomObjet.startsWith(testPattern);

		} else if (contientEtoile) {
			// extraction du pattern à tester
			String testPattern1 = StringUtils.substringBeforeLast(pattern, "*");
			String testPattern2 = StringUtils.substringAfter(pattern, "*");
			retour = (nomObjet.startsWith(testPattern1) && nomObjet
					.endsWith(testPattern2));
		} else {
			retour = nomObjet.equals(pattern);
		}

		return retour;
	}

	/**
	 * Ajoute un pattern à la liste des patterns.
	 *
	 * @param pattern
	 *            le pattern à ajouter.
	 */
	public void addPattern(String pattern) {
		if (pattern == null) {
			throw new NullPointerException("Un Pattern ne peut être null.");
		}
		patterns.add(pattern);
	}

	/**
	 * Supprime un pattern de la liste des patterns.
	 *
	 * @param pattern
	 *            le pattern à asupprimer.
	 */
	public void removePattern(String pattern) {
		patterns.remove(pattern);
	}

	/**
	 * Vide la liste des patterns.
	 */
	public void clearPatterns() {
		patterns.clear();
	}

	/**
	 * Retournela liste des patterns.
	 *
	 * @return la liste des patterns.
	 */
	public List<String> getPatterns() {
		return patterns;
	}

}
