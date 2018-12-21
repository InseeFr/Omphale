package fr.insee.omphale.core.service.geographie;

import java.util.List;
import java.util.Map;

import fr.insee.omphale.core.service.impl.Service;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;

public interface IZonageService {
	/**
	 * Méthode qui va vérifier que le nom de zonage n'est pas déjà utilisé pour
	 * un utilisateur
	 * 
	 * @param nomZonage
	 * @return
	 */
	public boolean testerNomZonage(Utilisateur utilisateur, String nomZonage);

	/**
	 * Met à jour en base un zonage
	 * 
	 * @param zonage
	 * @return Zonage
	 */
	public Zonage insertOrUpdate(Zonage zonage);

	/**
	 * Recherche les zonages d'un utilisateur
	 * 
	 * @param utilisateur
	 * @return List<Zonage>
	 */
	public List<Zonage> findByUtilisateur(Utilisateur utilisateur);

	/**
	 * Recherche les zonages non valides d'un utilisateur
	 * 
	 * @param utilisateur
	 * @param zonageService
	 * @return List<Zonage>
	 */
	public List<Zonage> findNonValidesByUtilisateur(Utilisateur utilisateur,
			IZonageService zonageService);

	/**
	 * Recherche les zonages valides d'un utilisateur
	 * 
	 * @param utilisateur
	 * @param zonageService
	 * @return List<Zonage>
	 */
	public List<Zonage> findValidesByUtilisateur(Utilisateur utilisateur,
			IZonageService zonageService);

	/**
	 * Recherche un zonage selon son identifiant
	 * 
	 * @param id
	 * @return Zonage
	 */
	public Zonage findById(String id);

	/**
	 * Supprime un zonage en base à partir de son instance
	 * 
	 * @param zonage
	 */
	public void delete(Zonage zonage);

	/**
	 * Recherche les zonages contenant une zone particulière
	 * 
	 * @param zone
	 * @param zonageService
	 * @return List<Zonage>
	 */
	public List<Zonage> findByZone(Zone zone, IZonageService zonageService);

	/**
	 * Méthode qui compte le nombre de zonages déja créés pour un utilisateur
	 * donné
	 * 
	 * @param utilisateur
	 * @return
	 */
	public long compterZonages(Utilisateur utilisateur);

	/**
	 * Méthode qui vérifie si les zones d'un zonage sont disjointes. renvoie
	 * null si elles sont disjointes, renvoie une map sinon :
	 * <ul>
	 * <li>clé (String) : code commune présent dans plusieurs zones du zonage</li>
	 * <li>valeur (String) : liste des noms de zone qui contiennent la commune,
	 * séparés par une virgule</li>
	 * </ul>
	 * 
	 * @param zonage
	 * @param zoneService
	 * @return
	 */
	public Map<String, String> verifierZonesDisjointes(Zonage zonage,
			IZoneService zoneService);

	/**
	 * Méthode de validation d'un zonage. Liste des étapes de la validation d'un
	 * zonage :
	 * <ul>
	 * <li>Actualisation des zones en fonction des dépendances.</li>
	 * <li>(Re)création des départements impactés par chacune des zones.</li>
	 * <li>Vérification que les zones sont disjointes.</li>
	 * <li>Création des groupes de départements étalons.</li>
	 * <li>Création des zones de groupe étalon.</li>
	 * <li>Détection des communes résiduelles de chaque groupe étalon.</li>
	 * <li>Passage à l'état validé.
	 * </ul>
	 * 
	 * @param zonage
	 * @param zoneService
	 * @param groupeEtalonService
	 * @param communeService
	 * @param cdService
	 * @return Renvoie une Map avec des clés de type String qui sert à rendre
	 *         compte de ce qu'il s'est passé :
	 *         <ul>
	 *         <li>clé "dependances" : Map(String,String) qui indique les codes
	 *         communes ajoutées par zone :
	 *         <ul>
	 *         <li>clé : id de zone</li>
	 *         <li>valeur : liste des id des communes rajoutées (séparés par
	 *         des virgules)</li>
	 *         </ul>
	 *         </li>
	 *         <li>clé "intersectionZones" : Map(String,String) qui indique les
	 *         codes communes présents dans plusieurs zones :
	 *         <ul>
	 *         <li>clé : code commune présent dans plusieurs zones du zonage</li>
	 *         <li>valeur : liste des noms de zone qui contiennent la commune,
	 *         séparés par une virgule</li>
	 *         </ul>
	 *         </li>
	 *         <li>clé "zonage" : Le zonage auquel on a voulu appliquer la
	 *         validation</li>
	 *         </ul>
	 */
	public Map<String, Object> validerZonage(Zonage zonage,
			IZoneService zoneService, IGroupeEtalonService groupeEtalonService,
			ICommuneService communeService,
			ICommuneDependanceService cdService, IZonageService zonageService, Service service);
	
	
	/**
	 * Tri la liste des zonages par ordre alpha à partir du nom
	 * 
	 * @param utilisateur
	 * @return
	 */
	public void triZonageParOrdreAlphabetique(List<Zonage> zonages);
	
	
	
	/**
	 * Recherche les zonages associées à une zone dans les tables ZONE_DE_ZONAGE et ZONE_DE_GROUPET
	 * 
	 * @param zone
	 * @return List<Zonage>
	 */
	public List<Zonage> findByZone(Zone zone);
	
	/**
	 * Recherche les zonages associés à une zone dans la table ZONE_DE_ZONAGE
	 * 
	 * @param zone
	 * @return List<Zonage>
	 */
	public List<Zonage> findByZone_ZONE_DE_ZONAGE(Zone zone);

	/**
	 * Recherche les zonages associés à une zone dans la table ZONE_DE_GROUPET
	 * 
	 * @param zone
	 * @return List<Zonage>
	 */
	public List<Zonage> findByZone_ZONE_DE_GROUPET(Zone zone);
	
	/**
	 * recherche si les zonages d'un utilisateur
	 * <BR>
	 * utilisées seulement par l'utilisateur (non partagé)
	 * <BR>
	 * utilisées par d'autres utilisateurs (partagé)
	 * 
	 * @param utilisateur
	 * @param zonagesUtilisesParUtilisateur
	 * @param zonagesUtiliseesParDautres
	 * @param nomZonagesUtilisesParUtilisateur
	 * @param nomZonagesUtiliseesParDautres
	 */
	public List<Object> rechercheZonagesPourFonctionSuppression(	Utilisateur utilisateur,
																	List<Integer> zonagesASupprimer,
																	List<Integer> zonagesAConserver,
																	List<String> nomZonagesASupprimer,
																	List<String> nomZonagesAConserver);

	public int deleteZoneDeZonageByListeIdZonage(List<Integer> zonagesASupprimerId);

	public int deleteZonageByListeIdZonage(List<Integer> zonagesASupprimerId);
}
