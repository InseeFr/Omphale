package fr.insee.omphale.dao.projection;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Scenario;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour Scenario
 *
 */
public interface IScenarioDAO extends IGenericDAO<Scenario, Integer> {

	/**
	 * Recherche la liste des Scenarios pour un idUser donné
	 * <br>
	 * order dateCreation desc
	 * 
	 * @param idUser idUser
	 * @return {@link List}<{@link Scenario}>
	 */
	public List<Scenario> findAll(String idUser);
	
	/**
	 * recherche tous les scénarios disponibles selon les paramètres suivants
	 * 
	 * @param idUser
	 * @param standard
	 * @return {@link List}<{@link Scenario}>
	 */
	public List<Scenario> findAll(String idUser, Boolean standard);
	
	/**
	 * recherche tous les scénarios disponibles selon les paramètres suivants
	 * 
	 * @param idUser
	 * @param standard
	 * @param validation
	 * @return {@link List}<{@link Scenario}>
	 */
	public List<Scenario> findAll(String idUser, Boolean standard, Boolean validation);
	
	/**
	 * recherche tous les scénarios disponibles selon les paramètres suivants
	 * 
	 * @param standard
	 * @return {@link List}<{@link Scenario}>
	 */
	public List<Scenario> findAll(Boolean standard);
	
	public List<Integer> findByIdPartagee(Utilisateur utilisateur);
	
	public List<Integer> findByIdNonPartagee(Utilisateur utilisateur);
	
	public List<String> findByNomPartagee(Utilisateur utilisateur);
	
	public List<String> findByNomNonPartagee(Utilisateur utilisateur);

	public int deleteByIdScenario(List<Integer> scenariosASupprimerId);
	

}
