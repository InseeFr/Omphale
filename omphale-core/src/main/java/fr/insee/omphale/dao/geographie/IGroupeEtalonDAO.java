package fr.insee.omphale.dao.geographie;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.GroupeEtalon;
import fr.insee.omphale.domaine.geographie.GroupeEtalonId;
import fr.insee.omphale.domaine.geographie.Zonage;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour GroupeEtalon
 *
 */
public interface IGroupeEtalonDAO extends
		IGenericDAO<GroupeEtalon, GroupeEtalonId> {

	/**
	 * Recherche les groupes étalons d'un zonage
	 * 
	 * @param zonage
	 * @return {@link List}<{@link GroupeEtalon}>
	 */
	public List<GroupeEtalon> findByZonage(Zonage zonage);
	
	public List<String> findByIdPartagee(Utilisateur utilisateur);
	
	public List<String> findByIdNonPartagee(Utilisateur utilisateur);

	public int deleteZoneDeGroupetByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId);

	public int deleteCommuneResiduelleByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId);

	public int deletedept_de_groupetByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId);

	public int deleteGroupeEtalonByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId);
	

}
