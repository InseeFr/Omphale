package fr.insee.omphale.core.service.geographie;

import java.util.List;
import java.util.Set;

import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.CommuneDependance;

public interface ICommuneDependanceService {

	/**
	 * Recherche toutes les dépendances concernants les communes passées en
	 * argument
	 * 
	 * @param communes
	 * @return
	 */
	public List<CommuneDependance> findByCommunes(List<Commune> communes,
			ICommuneDependanceService cdService);

	/**
	 * Recherche toutes les communes qui ont une dépendance de pas 1 avec les
	 * communes passées en argument
	 */
	public Set<Commune> trouverFreresProches(Set<Commune> communes);
}
