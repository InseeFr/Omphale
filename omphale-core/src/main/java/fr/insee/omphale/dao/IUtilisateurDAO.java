package fr.insee.omphale.dao;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Scenario;

public interface IUtilisateurDAO extends IGenericDAO<Utilisateur, String>{

	/**
	 * Recherche en fonction de l'idep
	 * l'utilisateur qui correspond
	 * @param idep
	 * @return Utilisateur
	 */
	public Utilisateur findByIdep(String idep);
	
	
	/**
	 * Recherche la liste des utilisateurs d'une EvolutionNonLocalisee
	 * <BR>
	 * utile uniquement pour la suppression d'objets d'un utilisateur
	 * 
	 * @param evolutionNonLocalisee
	 * @return List<String>
	 */	
	public List<Utilisateur> findByEvolutionNonLocalisee(EvolutionNonLocalisee evolutionNonLocalisee);
	
	/**
	 * Recherche la liste des utilisateurs d'une Zonage
	 * <BR>
	 * utile uniquement pour la suppression d'objets d'un utilisateur
	 * 
	 * @param zonage
	 * @return List<String>
	 */
	public List<Utilisateur> findByZonage(Zonage zonage);
	
	/**
	 * Recherche la liste des utilisateurs d'une Zone
	 * <BR>
	 * utile uniquement pour la suppression d'objets d'un utilisateur
	 * 
	 * @param zone
	 * @return List<String>
	 */
	public List<Utilisateur> findByZone(Zone zone);
	
	/**
	 * Recherche la liste des utilisateurs d'un Scenario
	 * <BR>
	 * utile uniquement pour la suppression d'objets d'un utilisateur
	 * 
	 * @param scenario
	 * @return List<String>
	 */
	public List<Utilisateur> findByScenario(Scenario scenario);


	public List<Utilisateur> findByRole(int role);
}
