package fr.insee.omphale.batch.transversal.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class GestionClassPath {
	
    /**
     * détermine le systeme d'exploitation sur lequel est execute le batch
     * recherche.
     * 
     * @param chaineAAnalyser (chaine identifiant l'OS)
     * @param modeleAAppliquer (modele regexp a appliquer)
     * @return String nom os trouve windows ou linux si l'entrée correspond aux filtres de recherche, false sinon.
     * @return null si pas trouve
     * @exception java.lang.IllegalStateException si la methode group n'est pas valide
     */
	public static String determineSystemeExploitation(String modeleAAppliquer) {
        String os = System.getProperty("os.name").trim();
	    String result = null;
	    try {
		   Pattern p = Pattern.compile(modeleAAppliquer);

		   Matcher m = p.matcher(os);
		   m.matches();
		   result = m.group(1);

		} catch (java.lang.IllegalStateException e) {
			e.printStackTrace();
			return result;
		} 
	    return result;
	}
	
}
