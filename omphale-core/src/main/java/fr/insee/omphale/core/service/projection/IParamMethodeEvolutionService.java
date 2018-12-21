package fr.insee.omphale.core.service.projection;


import java.util.List;

import fr.insee.omphale.domaine.projection.MethodeEvolution;
import fr.insee.omphale.domaine.projection.ParamMethodeEvolution;

public interface IParamMethodeEvolutionService {
	

	/**
	 * Recherche tous les paramètres existants
	 * 
	 * @return List(ParamMethodeEvolution)
	 */
	public List<ParamMethodeEvolution> findAll();

	/**
	 * Recherche les params en fonction de l'identifiant de ParamMethodeEvolution
	 * 
	 * @param id
	 * @return ParamMethodeEvolution
	 */
	public ParamMethodeEvolution findById(String id);
	
	/**
	 * Recherche les paramètres d'une méthode d'évolution
	 * 
	 * @param methode
	 * @return List(ParamMethodeEvolution)
	 */
	public List<ParamMethodeEvolution> findByMethodeEvolution(MethodeEvolution methode);
}
