package fr.insee.omphale.core.service.projection;

import java.util.List;

import fr.insee.omphale.domaine.projection.TypeEntite;

public interface ITypeEntiteService {

	/**
	 * Recherche tous les typeEntite
	 * 
	 * @return List(TypeEntite)
	 */
	public List<TypeEntite> findAll();

	/**
	 * Recherche un typeEntite selon son identifiant
	 * 
	 * @param id
	 * @return TypeEntite
	 */
	public TypeEntite findById(String id);
}
