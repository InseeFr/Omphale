package fr.insee.omphale.core.service.projection;

import java.util.List;

import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.MethodeEvolution;

public interface IMethodeService {
	

	/**
	 * Recherche toutes les Methodes d'évolution
	 * 
	 * @return List(MethodeEvolution)
	 */
	public List<MethodeEvolution> findAll();

	/**
	 * Recherche une méthode d'évolution selon son identifiant
	 * 
	 * @param id
	 * @return MethodeEvolution
	 */
	public MethodeEvolution findById(String id);
	
	/**
	 * Recherche une Méthode d'évolution selon sa composante
	 * 
	 * @param composante
	 * @return List(MethodeEvolution)
	 */
	public List<MethodeEvolution> findByComposante(Composante composante);

}
