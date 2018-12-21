/**
 * 
 */
package fr.insee.omphale.service.comparator;

import java.util.Comparator;

import fr.insee.omphale.domaine.geographie.Zonage;

/**
 * Classe gérant un ou des algorithmes de comparaison pour Zonage
 *
 */
public class ZonageComparator implements Comparator<Zonage> {

	@Override
	public int compare(Zonage z1, Zonage z2) {
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    int result = 0;
	    
	    int comparaison = z1.getNom().toLowerCase().compareTo(z2.getNom().toLowerCase());
	    if (comparaison == 0) result = EQUAL;
	    if (comparaison > 0) result = BEFORE;
	    if (comparaison > 0) result = AFTER;
	    
	    return result;
	}




}
