package fr.insee.omphale.dao.geographie;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.ihm.util.dataTable.ZoneAffichageDataTable;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour Zone
 *
 */
public interface IZoneDAO extends IGenericDAO<Zone, String> {
	/**
	 * Méthode qui compte les zones créées pour un utilisateur donné
	 * 
	 * @param utilisateur
	 * @return
	 */
	public long compterZones(Utilisateur utilisateur);

	/**
	 * Recherche toutes les zones d'un utilisateur donné
	 * 
	 * @param utilisateur
	 * @return
	 */
	public List<Zone> findByUtilisateur(Utilisateur utilisateur);

	/**
	 * Renvoie toutes les zones sauf de type utilisateur
	 * 
	 * @return
	 */
	public List<Zone> findZonesStandard();
	
	/**
	 * Renvoie toutes les zones standards uniquement de type 4 et 5
	 * <BR>
	 * qui peuvent être déstandardisées avec console d'administration
	 * <BR>
	 * 4 - zones emploi
	 * <BR>
	 * 5 - Aire urbaine
	 * 
	 * @return
	 */
	public List<Zone> findZonesDestandardisable();
	
	
	/**
	 * Renvoie toutes les zones standards uniquement de type 0
	 * <BR>
	 * qui peuvent être standardisées avec console d'administration
	 * <BR>
	 * 4 - personnel
	 * 
	 * @return
	 */
	public List<Zone> findZonesStandardisable(); 
	
	
	/**
	 * Renvoie la liste de zones choisies par l'utilisateur
	 * <BR>
	 * 
	 * @return
	 */
	public List<Zone> findBylisteIdZones(List<String> listeIdZones);
	
	
	/**
	 * Recherche les zones d'un utilisateur et les zones standards
	 * 
	 * @param utilisateur
	 * @return {@link List}<{@link Zone}>
	 */
	public List<Zone> findByUtilisateurEtZonesStandard(Utilisateur utilisateur);
	
	/**
	 * méthode pour récupérer la liste des zones d'un utilisateur et les zones standards
	 * en vue d'être affichée dans l'IHM
	 * plus de lien avec hibernate
	 * 
	 * @param utilisateur
	 * @return
	 */
	public List<ZoneAffichageDataTable> findByUtilisateurEtZonesStandardPourAffichage(Utilisateur utilisateur);
	
	public List<Zone> rechercheZonesUtilisateurUtiliseesParDautres(Utilisateur utilisateur);

	public List<Zone> findZonesPersonnelsUtiliseesQueParCreateur(Utilisateur utilisateur);
	
	public List<Zone> rechercheZonesPersonnelles(Utilisateur utilisateur);
	
	public List<Integer> findByIdPartagee(Utilisateur utilisateur);
	
	public List<Integer> findByIdNonPartagee(Utilisateur utilisateur);
	
	public List<String> findByNomPartagee(Utilisateur utilisateur);
	
	public List<String> findByNomNonPartagee(Utilisateur utilisateur);

	public int deleteDepartement_impactByListeIdZone(
			 List<Integer> zonesASupprimerId) ;

	public int deleteZonesByListeIdZone(List<Integer> zonesASupprimerId);

	public int deleteCommuneDeZoneByListeIdZone(List<Integer> zonesASupprimerId);
	
	public int deleteNomTableByListeId(String nomTable, String nomChamp, List<Integer> elementsASupprimerId) ;


}
