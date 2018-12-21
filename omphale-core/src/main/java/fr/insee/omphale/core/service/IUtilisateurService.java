package fr.insee.omphale.core.service;

import java.util.List;

import fr.insee.omphale.core.service.impl.ERetourSuppressionUtilisateur;
import fr.insee.omphale.domaine.Utilisateur;

public interface IUtilisateurService {
	/**
	 * Recherche un utilisateur selon son identifiant
	 * 
	 * @param id
	 * @return Utilisateur
	 */
	public Utilisateur findById(String id);
	
	/**
	 * Récupère la liste de tous les utilisateurs
	 * 
	 * @return List<Utilisateur>
	 */
	public List<Utilisateur> findAll();
	
	/**
	 * insère en base une instance d'utilisateur
	 * 
	 * @param utilisateur
	 */
	public void insertOrUpdate(Utilisateur utilisateur);
	
	/**
	 * Recherche un utilisateur selon son idep
	 * 
	 * @param idep
	 * @return Utilisateur
	 */
	public Utilisateur findByIdep(String idep);
	
	
	/**
	 * supprime un utiliateur en base de données
	 * 
	 * @param utilisateur
	 */
	public void supprimer(Utilisateur utilisateur);
	
	
	/**
	 * recherche les utilisateurs ayant un role précis
	 * 
	 * @param role
	 * @return List<Utilisateur>
	 */
	public List<Utilisateur> findByRole(int role);
	
	/**
	 * supprimerToutesReferences :
	 *  - supprime l'utilisateur dans le groupe ldap utilisateur
	 *  - supprime les objets métiers de l'utilisateur en BDD
	 *  - supprime l'utilisateur en BDD
	 * 
	 * @param utilisateur
	 * @return ERetourSuppressionUtilisateur
	 * @throws Exception
	 */
	public ERetourSuppressionUtilisateur supprimerToutesReferences(Utilisateur utilisateur) throws Exception;
	
}
