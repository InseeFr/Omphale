package fr.insee.omphale.core.service.geographie;

import java.util.List;

import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.GroupeEtalon;
import fr.insee.omphale.domaine.geographie.GroupeEtalonId;
import fr.insee.omphale.domaine.geographie.Zonage;

public interface IGroupeEtalonService {

	/**
	 * Création des groupes étalon pour un zonage
	 * 
	 * @param zonage
	 * @return List(GroupeEtalon)
	 */
	public List<GroupeEtalon> creerGroupesEtalon(Zonage zonage, int idMax);

	/**
	 * Recherche en base les communes résiduelles d'un groupe étalon
	 * 
	 * @param groupeEtalon
	 * @return List(Commune)
	 */
	public List<Commune> findCommunesResiduelles(GroupeEtalon groupeEtalon,
			ICommuneService communeService);

	/**
	 * Insere un nouveau groupe étalon en base
	 * 
	 * @param groupeEtalon
	 */
	public GroupeEtalon insertOrUpdate(GroupeEtalon groupeEtalon);

	/**
	 * Supprime un groupe étalon en base
	 * 
	 * @param groupeEtalon
	 */
	public boolean delete(GroupeEtalon groupeEtalon);

	/**
	 * Recherche les groupes étalons d'un zonage en base
	 * 
	 * @param zonage
	 * @return List(GroupeEtalon)
	 */
	public List<GroupeEtalon> findByZonage(Zonage zonage);

	/**
	 * Recherche un groupeEtalon selon son identifiant
	 * 
	 * @param id
	 * @return GroupeEtalon
	 */
	public GroupeEtalon findById(GroupeEtalonId id);

	/**
	 * Ajoute une zone résiduelle selon les paramètres suivants :
	 * 
	 * @param nomZoneRes
	 * @param zonage
	 * @param groupeEtalon
	 * @param typeZoneStandardService
	 * @param zoneService
	 * @param groupeEtalonService
	 * @return Zonage
	 */
	public Zonage addZoneResiduelle(String nomZoneRes ,Zonage zonage, GroupeEtalon groupeEtalon,
			ITypeZoneStandardService typeZoneStandardService,
			IZoneService zoneService, IGroupeEtalonService groupeEtalonService);
	
	
	/**
	 * recherche si les groupes étalons d'un utilisateur
	 * <BR>
	 * utilisées seulement par l'utilisateur (non partagé)
	 * <BR>
	 * utilisées par d'autres utilisateurs (partagé)
	 * 
	 * @param utilisateur
	 * @param groupesEtalonsUtilisesParUtilisateur
	 * @param groupesEtalonsUtiliseesParDautres
	 */
	public List<Object> rechercheGroupesEtalonsPourFonctionSuppression(	Utilisateur utilisateur,
			List<String> groupesEtalonsUtilisesParUtilisateur,
			List<String> groupesEtalonsUtiliseesParDautres);

	public int deleteZoneDeGroupetByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId);

	public int deleteCommuneResiduelleByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId);

	public int deletedept_de_groupetByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId);

	public int deleteGroupeEtalonByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId);

}
