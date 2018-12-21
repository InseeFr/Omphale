/**
 * 
 */
package fr.insee.omphale.service.comparator;

import java.util.Comparator;

import fr.insee.omphale.domaine.projection.Scenario;

/**
 * Classe gérant un ou des algorithmes de comparaison pour Scenario
 *
 */
public class scenarioComparator implements Comparator<Scenario> {

	@Override
	public int compare(Scenario scenario1, Scenario scenario2) {
	    final int BEFORE = -1;
	    final int EQUAL = 0;
	    final int AFTER = 1;
	    int result = 0;
	    
	    int comparaison = scenario1.getNom().toLowerCase().compareTo(scenario2.getNom().toLowerCase());
	    if (comparaison == 0) result = EQUAL;
	    if (comparaison > 0) result = BEFORE;
	    if (comparaison > 0) result = AFTER;
	    
	    return result;
	}




}
