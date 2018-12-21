package fr.insee.omphale.dao.geographie;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour Zonage
 *
 */
public interface IZonageDAO extends IGenericDAO<Zonage, String> {
	public List<Zonage> findByUtilisateur(Utilisateur utilisateur);
	
	/**
	 * Méthode qui compte les zonages créés pour un utilisateur donné
	 * 
	 * @param utilisateur
	 * @return
	 */
	public long compterZonages(Utilisateur utilisateur);
	
	/**
	 * Recherche les zonages associées à une zone dans la table ZONE_DE_ZONAGE
	 * 
	 * @param zone
	 * @return List<Zonage>
	 */
	public List<Zonage> findByZone_ZONE_DE_ZONAGE(Zone zone);

	/**
	 * Recherche les zonages associées à une zone dans la table ZONE_DE_GROUPET
	 * 
	 * @param zone
	 * @return List<Zonage>
	 */
	public List<Zonage> findByZone_ZONE_DE_GROUPET(Zone zone);

	
	public List<String> findUtilisateurs(Zone zone);
	
	/**
	 * Recherche les zonages associées à une zone dans les tables ZONE_DE_ZONAGE et ZONE_DE_GROUPET
	 * 
	 * @param zone
	 * @return List<Zonage>
	 */
	public List<Zonage> findByZone(Zone zone);

	public List<Integer> findByIdPartagee(Utilisateur utilisateur);
	
	public List<Integer> findByIdNonPartagee(Utilisateur utilisateur);

	public List<String> findByNomPartagee(Utilisateur utilisateur);
	
	public List<String> findByNomNonPartagee(Utilisateur utilisateur);

	public int deleteZoneDeZonageByListeIdZonage(List<Integer> zonagesASupprimerId);

	public int deleteZonageByListeIdZonage(List<Integer> zonagesASupprimerId);
}
