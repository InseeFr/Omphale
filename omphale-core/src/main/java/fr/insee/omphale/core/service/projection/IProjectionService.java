package fr.insee.omphale.core.service.projection;

import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.domaine.projection.Scenario;
import fr.insee.omphale.ihm.util.dataTable.ProjectionAffichageDataTable;

/**
 * Interface de service <br>
 * objets métier {@link fr.insee.omphale.domaine.projection.Projection}
 * 
 */
public interface IProjectionService {

	/**
	 * Recherche dans la base de données une Projection pour un identifiant
	 * donné.
	 * 
	 * @param id
	 *            identifiant de projection
	 * @return objet {@link fr.insee.omphale.domaine.projection.Projection}
	 */
	public Projection findById(Integer id);
	
	
	/**
	 * Recherche dans la base de données une Projection pour un identifiant
	 * donné et un zonage = à 0
	 * 
	 * @param id
	 *            identifiant de projection
	 * @return objet {@link fr.insee.omphale.domaine.projection.Projection}
	 */
	public List<Projection> findAll(String idUser, String idZonage);

	
	/**
	 * Recherche la liste des projections dans la base de données <br>
	 * les projections englobantes de l'utilisateur
	 * order by dateCreation desc
	 * 
	 * @param idUser
	 *            idUser
	 * @return liste d'objets
	 *         {@link fr.insee.omphale.domaine.projection.Projection}
	 */
	public List<Projection> findAllEnglobante(String idUser, Integer anneeRef, Integer anneeHorizon);
	
	
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

	/**
	 * Update une Projection dans la base de données.
	 */
	public void update();

	/**
	 * Remove une Projection dans la base de données.
	 * 
	 * @param projection
	 *            projection
	 */
	public void remove(Projection projection);

	/**
	 * flush
	 */
	public void flush();

	/**
	 * Vérifie que le nom d'une projection n'existe pas déjà 
	 * <BR>
	 * parmi celle existante d'utilisateur
	 * 
	 * 
	 * @param nomProjection
	 * @param idep
	 * @param projectionService
	 * @return {@link Boolean}
	 */
	public boolean testerNomProjection(String nomProjection, String idep, IProjectionService projectionService);
	
	/**
	 * Vérifie que le prefixe du nom d'une projection choisie
	 * <BR>
	 * n'existe pas déjà parmi ceux d'un utilisateur
	 * 
	 * @param prefixeProjection
	 * @param listeProjectionsEtalonsSelectionnees
	 * @param idep
	 * @param projectionService
	 * @return {@link Boolean}
	 */
	public boolean testerPrefixeProjection(String prefixeProjection, List<String> listeProjectionsEtalonsSelectionnees, String idep, IProjectionService projectionService) ;

	/**
	 * contrôle que les projections en paramètre ont toutes la même année de référence et d'horizon
	 * 
	 * @param listeProjectionsEtalonsSelectionnees
	 * @return {@link Boolean}
	 */
	public Boolean controleEgaliteAnneeRefEtHorizonProjectionsSelectionnees(List<String> listeProjectionsEtalonsSelectionnees);
	
	/**
	 * Recherche la liste des projections étalons
	 * 
	 * @return {@link List}<{@link Projection}>
	 */
	public List<Projection> findEtalons();

	/**
	 * Insère en base une projection à partir de son instance
	 * 
	 * @param projection
	 * @return {@link Projection}
	 */
	public Projection insertOrUpdate(Projection projection);

	/**
	 * initialise la liste des possibilité de génération des données d'une projection
	 * <BR>
	 * 		"Pas de données exportées"
	 * <BR>
	 * 		"Export Partiel"
	 * <BR>
	 * 		"Export Complet"
	 * 
	 * 
	 * @return {@link TreeMap}<{@link Integer},{@link String}>
	 */
	public TreeMap<Integer, String> getListeDonnees();

	/**
	 * Recherche la liste des projections utilisant un zonage donné
	 * 
	 * @param zonage
	 * @return
	 */
	public List<Projection> findAll(Zonage zonage);

	/**
	 * Recherche les projection étalons utilisées par une projection donnée
	 * 
	 * @param projection
	 * @return  {@link List}<{@link Projection}>
	 */
	public List<Projection> findByProjectionEtalon(Projection projection);

	/**
	 * Recherche les projections englobantes associées à une projection
	 * 
	 * @param projection
	 * @return {@link List}<{@link Projection}>
	 */
	public List<Projection> findByProjectionEnglobante(Projection projection);
	
	
	/**
	 * Recherche les projection étalons et englobantes utilisées par une projection donnée
	 * 
	 * @param projection
	 * @return Set<Projection>
	 */
	public Set<Projection> findByEnglobanteEtEtalon(Projection projection);
	
	/**
	 * Recherche si la projection est déclarée 2 fois dans la table Population
	 * 
	 * @param zonage
	 * @return
	 */
	public String findPopulation(Projection projection);
	
	/**
	 * Récupère la population d'une projection
	 * 
	 * @param projection
	 * @return {@link String} 
	 */
	public String findPopulationEnglobante(Projection projection);
	
	/**
	 * supprime une projection, ses projections sous jacentes ainsi que ses évolutions localisées
	 * 
	 * @param projection
	 * @param projectionLanceeService
	 * @param evolutionLocaliseeService
	 */
	public void supprimerProjectionEtProjectionsSousJacentes(Projection projection, 
			IProjectionLanceeService projectionLanceeService,
			IEvolutionLocaliseeService evolutionLocaliseeService);
	/**
	 * supprime une projection englobante, ses projections sous jacentes ainsi que ses évolutions localisées
	 * 
	 * @param projection
	 * @param projectionLanceeService
	 * @param evolutionLocaliseeService
	 */
	public void supprimerProjectionsUtilisantEnglobante(Projection projection, 
			IProjectionLanceeService projectionLanceeService, 
			IEvolutionLocaliseeService evolutionLocaliseeService);
	/**
	 * supprime une projection Etalon, ses projections sous jacentes ainsi que ses évolutions localisées
	 * 
	 * @param projection
	 * @param projectionLanceeService
	 * @param evolutionLocaliseeService
	 */
	public void supprimerProjectionsUtilisantEtalon(Projection projection, 
			IProjectionLanceeService projectionLanceeService, 
			IEvolutionLocaliseeService evolutionLocaliseeService);
	
	/**
	 * supprime une projection et ses évolutions localisées
	 * 
	 * @param projection
	 * @param projectionLanceeService
	 * @param evolutionLocaliseeService
	 */
	public void supprimerProjection(Projection projection, 
			IProjectionLanceeService projectionLanceeService, 
			IEvolutionLocaliseeService evolutionLocaliseeService);
	
	/**
	 * tri projection par ordre alpha sur le nom
	 * 
	 * @param proejction
	 * @return void
	 */
	public void triProjectionParOrdreAlphabetique(List<Projection> projections);
	
	/**
	 * Recherche les utilisateurs d'une projection précise
	 * 
	 * @param projection
	 * @return List<String>
	 */
	public List<String> findUtilisateurs(Projection projection);

	/**
	 * Supprime une projection précise en base de données
	 * 
	 * @param projection
	 */
	public void supprimer(Projection projection);

	/**
	 * Recherche les projections associées à un scénario
	 * 
	 * @param scenario
	 * @return List<Projection>
	 */
	public List<Projection> findByScenario(Scenario scenario);
	
	
	
	
	/**
	 * Recherche les projections associées à un zonage
	 * @param zonage
	 * @return List<Projection>
	 */
	public List<Projection> findByZonage(Zonage zonage);
	
	
	
	/**
	 * recherche si les projections d'un utilisateur
	 * <BR>
	 * utilisées seulement par l'utilisateur (non partagé)
	 * <BR>
	 * utilisées par d'autres utilisateurs (partagé)
	 * 
	 * @param utilisateur
	 * @param projectionsASupprimer
	 * @param projectionsUtiliseesParDautres
	 * @param nomProjectionsASupprimer
	 * @param nomProjectionsAConserver
	 */
	public List<Object> rechercheProjectionPourFonctionDeSuppression(	Utilisateur utilisateur,
																		List<Integer> projectionsASupprimerId,
																		List<Integer> projectionsAConserverId,
																		List<String> nomProjectionsASupprimer,
																		List<String> nomProjectionsAConserver);
	
	
	public List<Integer> findIdProjectionPartagee(Utilisateur utilisateur);
	
	public List<Integer> findIdProjectionNonPartagee(Utilisateur utilisateur);
	
	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer);
	
	public Integer deleteUserPopulationByListIdProjection(List<Integer> IdsProjectionsASupprimer);
	
	public Integer deleteCbPopulationByListIdProjection(List<Integer> IdsProjectionsASupprimer);


	public List<ProjectionAffichageDataTable> findAllDTO(String idUser);

	
}
