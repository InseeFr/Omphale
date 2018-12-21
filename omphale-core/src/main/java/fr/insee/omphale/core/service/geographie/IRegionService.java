package fr.insee.omphale.core.service.geographie;

import java.util.List;

import fr.insee.omphale.domaine.geographie.Region;

public interface IRegionService {
	
	/**
	 * Recherche une région en base avec son identifiant
	 * @param id
	 * @return Region
	 */
	public Region findById(String id);

	/**
	 * Recherche toutes les régions en base
	 * @return List(Region)
	 */
	public List<Region> findAll();
}
