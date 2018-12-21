package fr.insee.omphale.core.service.geographie;

import java.util.List;
import java.util.Set;

import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;
import fr.insee.omphale.domaine.geographie.Region;
import fr.insee.omphale.domaine.geographie.TypeZoneStandard;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.ihm.util.dataTable.ZoneAffichageDataTable;

public interface IZoneService {
	/**
	 * Méthode qui va trouver les communes liées (dépendances communes) aux
	 * communes déjà présentes dans la zone
	 * 
	 * @return Set(Commune) : la liste des communes à ajouter, null si il n'y en
	 *         a aucune
	 */
	public Set<Commune> findCommunesDependantes(Zone zone,
			ICommuneDependanceService cdService);

	/**
	 * Méthode qui compte le nombre de zones déja créés pour un utilisateur
	 * donné
	 * 
	 * @param utilisateur
	 * @return
	 */
	public long compterZones(Utilisateur utilisateur);

	/**
	 * Recherche les zones de l'utilisateur
	 * 
	 * @param utilisateur
	 * @return
	 */
	public List<Zone> findByUtilisateur(Utilisateur utilisateur);

	/**
	 * Méthode qui va vérifier que le nom de zone n'est pas déjà utilisé pour un
	 * utilisateur
	 * 
	 * @param nomZone
	 * @return
	 */
	public boolean testerNomZone(Utilisateur utilisateur, String nomZone);

	/**
	 * Recherche le nombre de communes présentes dans une zone pour un
	 * département précis
	 * 
	 * @param zone
	 * @param departement
	 * @return
	 */
	public int findNbCommunesParDepartement(Zone zone, Departement departement);

	/**
	 * filtre les zones en fonction de leur région et de leur type, parmi une
	 * liste de zones données
	 * 
	 * @param region
	 * @param type
	 * @param zones
	 * @return
	 */
	public List<Zone> filtrerParRegionEtType(Region region,
			TypeZoneStandard type, List<Zone> zones);
	/**
	 * Pour optimiser l'affichage
	 * 
	 * filtre les zones en fonction de leur région et de leur type, parmi une
	 * liste de zones données
	 * 
	 * @param region
	 * @param type
	 * @param zones
	 * @return
	 */
	public List<ZoneAffichageDataTable> filtrerParRegionEtTypePourAffichage(Region region,
			TypeZoneStandard type, List<ZoneAffichageDataTable> zones) ;

	/**
	 * renvoie les zones disponibles pour un utilisateur (les zones qu'il a créés
	 * + les zones standards)
	 * 
	 * @return
	 */
	public List<Zone> findZonesDisponibles(Utilisateur utilisateur);
	
	/**
	 * renvoie les zones disponibles pour un utilisateur (les zones qu'il a créés
	 * + les zones standards)
	 * pour affichage sans hibernate
	 * 
	 * @return
	 */
	public List<ZoneAffichageDataTable> findZonesDisponiblesPourAffichage(Utilisateur utilisateur);


	/**
	 * Recherche les zones standards
	 * 
	 * @return List<Zone>
	 */
	public List<Zone> findZonesStandard();

	/**
	 * Supprime une zone en base à partir de son instance
	 * 
	 * @param zone
	 * @return boolean
	 */
	public boolean insertOrUpdate(Zone zone);

	/**
	 * Recherche une zone selon son identifiant
	 * 
	 * @param id
	 * @return Zone
	 */
	public Zone findById(String id);

	/**
	 * Supprime une zone à partir de son instance
	 * 
	 * @param zone
	 */
	public void delete(Zone zone);
	
	/**
	 * renvoie les zones standards de l'utilisateur
	 * 
	 * @return
	 */
	public List<Zone> findZonesDestandardisableByUtilisateur(Utilisateur utilisateur);
	
	/**
	 * renvoie les zones non standards de l'utilisateur
	 * 
	 * @return
	 */
	public List<Zone> findZonesStandardisablesByUtilisateur(Utilisateur utilisateur);
	
	/**
	 * Renvoie la liste de zones choisies par l'utilisateur
	 * <BR>
	 * 
	 * @return
	 */
	public List<Zone> findBylisteIdZones(List<String> listeIdZones) ;
	

	/**
	 * Renvoie la liste des identifiants utilisateurs utilisants une zone précise dans un de leurs zonages
	 * 
	 * @param zone
	 * @return List<String>
	 */
	public List<String> findUtilisateurs(Zone zone);
	
	/**
	 * 
	 * recherche si les zones d'un utilisateur
	 * <BR>
	 * utilisées seulement par l'utilisateur (non partagé)
	 * <BR>
	 * utilisées par d'autres utilisateurs (partagé)
	 * 
	 * @param utilisateur
	 * @param zonesUtiliseesParUtilisateur
	 * @param zonesUtiliseesParDautres
	 * @param nomZonesUtiliseesParUtilisateur
	 * @param nomZonesUtiliseesParDautres
	 */
	public List<Object> rechercheZonesPourFonctionSuppression(		Utilisateur utilisateur,
			List<Integer> ZonesASupprimer,
			List<Integer> ZonesAConserver,
			List<String> nomZonesASupprimer,
			List<String> nomZonesAConserver);

		

	public int deleteDepartement_impactByListeIdZone(String nomTable, String nomChamp,
			List<Integer> zonesASupprimerId);

	public int deleteZonesByListeIdZone(String nomTable, String nomChamp, List<Integer> zonesASupprimerId);

	public int deleteCommuneDeZoneByListeIdZone(String nomTable, String nomChamp, List<Integer> zonesASupprimerId);
	
	

}
