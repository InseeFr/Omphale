package fr.insee.omphale.dao.projection;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.domaine.projection.Scenario;
import fr.insee.omphale.ihm.util.dataTable.ProjectionAffichageDataTable;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour Projection
 *
 */
public interface IProjectionDAO extends IGenericDAO<Projection, Integer> {

	/**
	 * Recherche la liste des projections dans la base de données <br>
	 * order by dateCreation desc
	 * 
	 * @param idUser
	 *            idUser
	 * @return liste d'objets
	 *         {@link fr.insee.omphale.domaine.projection.Projection}
	 */
	public List<Projection> findAll(String idUser);
	
	
	/**
	 * Recherche la liste des projections dans la base de données <br>
	 * selon l'utilisateur et le zonage égale 0
	 * 
	 * @param idUser
	 *            idUser
	 * @return liste d'objets
	 *         {@link fr.insee.omphale.domaine.projection.Projection}
	 */
	public List<Projection> findAll(String idUser, String idZonage);

	/**
	 * Recherche dans la base de données la liste des projections qui
	 * référencent un scénario donné <br>
	 * order by nom
	 * 
	 * @param idScenario
	 *            identifiant de scénario
	 * @return liste d'objets
	 *         {@link fr.insee.omphale.domaine.projection.Projection}
	 */
	public List<Projection> getListe(Integer idScenario);

	public List<Projection> findEtalons();

	/**
	 * Recherche la liste des projections utilisant un zonage donné
	 * 
	 * @param zonage
	 * @return
	 */
	public List<Projection> findAll(Zonage zonage);

	/**
	 * Recherche les projections Etalon associées à une projection
	 * 
	 * @param projection
	 * @return
	 */
	public List<Projection> findByProjectionEtalon(Projection projection);

	/**
	 * recherche les projections englobantes associées à une projection
	 * 
	 * @param projection
	 * @return {@link List}<{@link Projection}>
	 */
	public List<Projection> findByProjectionEnglobante(Projection projection);

	
	public List<Projection> findByProjectionEnglobanteEtByProjectionEtalon(Projection projectionEnglobanteOuEtalon);
	
	
	public List<Utilisateur> findUtilisateursByProjectionEnglobanteEtByProjectionEtalon(Projection projectionEnglobanteOuEtalon);
	
	public List<Projection> findProjectionPartagee(Utilisateur utilisateur);
	
	public List<Projection> findProjectionNonPartagee(Utilisateur utilisateur);
	
	public List<Integer> findIdProjectionPartagee(Utilisateur utilisateur);
	
	public List<Integer> findIdProjectionNonPartagee(Utilisateur utilisateur);
	
	public List<String> findNomProjectionPartagee(Utilisateur utilisateur);
	
	public List<String> findNomProjectionNonPartagee(Utilisateur utilisateur);
	
	
	/**
	 * Lorsqu'une projection est potentiellement étalon, des données sont
	 * sauvegardées dans les tables CB_POPULATION et USER_POPULATION, ces
	 * données empêchent la suppression de la projection et doivent donc être
	 * supprimées via cette méthode avant la suppression effective de la
	 * projection
	 * 
	 * @param idProjection
	 */
	public void deleteDonneesResiduelles(int idProjection);
	
	
	/**
	 * Trouve si la projection est présente 2 fois dans la table population
	 * 
	 * @param idProjection
	 */
	public String findPopulation(Projection projection);
	
	/**
	 * Recherche des populations englobantes associées à une projection
	 * 
	 * @param projection
	 * @return {@link String}
	 */
	public String findPopulationEnglobante(Projection projection);
	
	
	/**
	 * Recherche les utilisateurs d'une projection précise
	 * 
	 * @param projection
	 * @return List<String>
	 */
	public List<String> findUtilisateurs(Projection projection);
	
	/**
	 * Recherche les projections associées à un scénario
	 * 
	 * @param scenario
	 * @return List<Projection>
	 */
	public List<Projection> findByScenario(Scenario scenario);
	
	/**
	 * Recherche les projections associées à un zonage
	 * 
	 * @param zonage
	 * @return List<Projection>
	 */
	public List<Projection> findByZonage(Zonage zonage);
	
	
	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer);
	
	public Integer deleteUserPopulationByListIdProjection(List<Integer> IdsProjectionsASupprimer);
	
	public Integer deleteCbPopulationByListIdProjection(List<Integer> IdsProjectionsASupprimer);


	public List<ProjectionAffichageDataTable> findAllDTO(String idUser);


}
