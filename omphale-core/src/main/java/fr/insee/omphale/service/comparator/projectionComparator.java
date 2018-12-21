/**
 * 
 */
package fr.insee.omphale.service.comparator;

import java.util.Comparator;

import fr.insee.omphale.domaine.projection.Projection;

/**
 * Classe gérant un ou des algorithmes de comparaison pour Projection
 *
 */
public class projectionComparator implements Comparator<Projection> {

	@Override
	public int compare(Projection p1, Projection p2) {
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    int result = 0;
	    
	    int comparaison = p1.getNom().toLowerCase().compareTo(p2.getNom().toLowerCase());
	    if (comparaison == 0) result = EQUAL;
	    if (comparaison > 0) result = BEFORE;
	    if (comparaison > 0) result = AFTER;
	    
	    return result;
	}




}
