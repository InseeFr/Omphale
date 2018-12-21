package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;


import fr.insee.omphale.core.service.projection.IEvolutionLocaliseeService;
import fr.insee.omphale.core.service.projection.IProjectionLanceeService;
import fr.insee.omphale.core.service.projection.IProjectionService;
import fr.insee.omphale.dao.projection.IProjectionDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.projection.EvolutionLocalisee;
import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.domaine.projection.ProjectionLancee;
import fr.insee.omphale.domaine.projection.Scenario;
import fr.insee.omphale.ihm.util.dataTable.ProjectionAffichageDataTable;
import fr.insee.omphale.service.comparator.projectionComparator;

/**
 * Classe gérant les fonctions de la couche service pour les "Projection"
 *
 */
public class ProjectionService implements IProjectionService {
	

	private IProjectionDAO projectionDao ;
	
	public ProjectionService(){
		this.projectionDao = daoFactory.getProjectionDAO();
	}
	
	public ProjectionService(IProjectionDAO projectionDao){
		this.projectionDao = projectionDao;
	}

	public Projection findById(Integer id) {
		return projectionDao.findById(id);
	}

	/**
	 * order by dateCreation desc
	 */
	public List<Projection> findAll(String idUser) {
		return projectionDao.findAll(idUser);
	}
	
	public List<Projection> findAllEnglobante(String idUser, Integer anneeRef, Integer anneeHorizon){
		List<Projection> projectionAllUtilisateurs = findAll(idUser);
		List<Projection> projectionsEnglobantesUtilisateurs = new ArrayList<Projection>();
		for (Projection p : projectionAllUtilisateurs){
			if (p.getEnglobante() == 9 && p.getAnneeReference() <= anneeRef && p.getAnneeHorizon() >= anneeHorizon){
				projectionsEnglobantesUtilisateurs.add(p);
			}
		}
		return projectionsEnglobantesUtilisateurs;
	}
	
	public List<Projection> findAll(String idUser, String idZonage) {
		return projectionDao.findAll(idUser,idZonage);
	}

	public List<Projection> getListe(Integer idScenario) {
		return projectionDao.getListe(idScenario);
	}

	public void update() {
		flush();
	}

	public void remove(Projection projection) {
		projectionDao.deleteDonneesResiduelles(projection.getId());
		projectionDao.delete(projection);
	}

	public void flush() {
		projectionDao.flush();
	}

	public boolean testerNomProjection(String nomProjection, String idep, IProjectionService projectionService) {
		List<Projection> projections = projectionService.findAll(idep);
		if (projections == null) {
			return true;
		}
		for (Projection projection : projections) {
			if (projection.getNom().equals(nomProjection)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean testerPrefixeProjection(String prefixeProjection, List<String> listeProjectionsEtalonsSelectionnees, String idep, IProjectionService projectionService) {
		List<Projection> projectionsUtilisateur = projectionService.findAll(idep);
		if (projectionsUtilisateur == null) {
			return true;
		}
		for (String idProjectionSelectionnee : listeProjectionsEtalonsSelectionnees) {
			Projection projectionSelectionnee = projectionService.findById(Integer.valueOf(idProjectionSelectionnee));
			for (Projection projectionUtilisateur : projectionsUtilisateur) {
				if (projectionUtilisateur.getNom().equals(prefixeProjection + "_" + projectionSelectionnee.getNom())) {
					return false;
				}
			}
		}
		return true;
	}

	public Boolean controleEgaliteAnneeRefEtHorizonProjectionsSelectionnees(List<String> listeProjectionsEtalonsSelectionnees){
		Boolean resultat = true;

		boolean chargementAnneeRefEtHorizonPremiereProjection = false;
		int anneeRefProjectionPrecedente = 0;
		int anneeHorizonProjectionPrecedente = 0;
		
		for (String idProjectionEtalon : listeProjectionsEtalonsSelectionnees) {
			
			Projection projection = this.findById(Integer.valueOf(idProjectionEtalon));
			
			if(!chargementAnneeRefEtHorizonPremiereProjection){
				chargementAnneeRefEtHorizonPremiereProjection = true;
				anneeRefProjectionPrecedente = projection.getAnneeReference();
				anneeHorizonProjectionPrecedente = projection.getAnneeHorizon();
			}else{
				if(		anneeRefProjectionPrecedente != projection.getAnneeReference()||
						anneeHorizonProjectionPrecedente != projection.getAnneeHorizon()		){
					resultat = false;
					break;
				}	
			}
		}
		return resultat;
	}
	
	public List<Projection> findEtalons() {
		return projectionDao.findEtalons();
	}

	public Projection insertOrUpdate(Projection projection) {
		return projectionDao.insertOrUpdate(projection);
	}

	public TreeMap<Integer, String> getListeDonnees() {
		TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>();
		treeMap.put(0, "Pas de données exportées");
		treeMap.put(1, "Export Partiel");
		treeMap.put(2, "Export Complet");
		return treeMap;
	}

	public List<Projection> findAll(Zonage zonage) {
		return projectionDao.findAll(zonage);
	}

	public List<Projection> findByProjectionEnglobante(Projection projection) {
		return projectionDao.findByProjectionEnglobante(projection);
	}

	public List<Projection> findByProjectionEtalon(Projection projection) {
		return projectionDao.findByProjectionEtalon(projection);
	}

	public String findPopulation(Projection projection) {
		return projectionDao.findPopulation(projection);
	}
	
	public String findPopulationEnglobante(Projection projection) {
		return projectionDao.findPopulationEnglobante(projection);
	}
	
	public void supprimerProjectionEtProjectionsSousJacentes(Projection projection, 
															IProjectionLanceeService projectionLanceeService,
															IEvolutionLocaliseeService evolutionLocaliseeService){

		if (projection != null && projectionLanceeService != null && evolutionLocaliseeService != null){
			
			supprimerProjectionsUtilisantEnglobante(projection, projectionLanceeService, evolutionLocaliseeService);
			
			supprimerProjectionsUtilisantEtalon(projection, projectionLanceeService, evolutionLocaliseeService);
			
			supprimerProjection(projection, projectionLanceeService, evolutionLocaliseeService);
		}
	}
	
	public void supprimerProjectionsUtilisantEnglobante(Projection projection, 
														IProjectionLanceeService projectionLanceeService, 
														IEvolutionLocaliseeService evolutionLocaliseeService){
		// la projection à supprimer a-t-elle été utilisée comme projection englobante par d'autres projections ?
		List<Projection> projectionsUtilisantEnglobante = findByProjectionEnglobante(projection);
		// supprime les projections et leurs évolutions localisées qui utilisent la projection englobante à supprimer
		if (projectionsUtilisantEnglobante != null
				&& !projectionsUtilisantEnglobante.isEmpty()) {
			for (Projection p : projectionsUtilisantEnglobante) {
				if (projectionLanceeService.exist(p.getId())) {
					for (ProjectionLancee lancement : projectionLanceeService
							.findByProjection(p)) {
						projectionLanceeService.remove(lancement);
					}
				}
				if (p.getEvolutionsLocalisees() != null
						& !p.getEvolutionsLocalisees().isEmpty()) {
					for (EvolutionLocalisee el : p.getEvolutionsLocalisees()) {
						evolutionLocaliseeService.delete(el);
					}
				}
				supprimerProjectionsUtilisantEnglobante(p, projectionLanceeService, evolutionLocaliseeService);
				remove(p);
			}
		}
	}
	
	public void supprimerProjectionsUtilisantEtalon(Projection projection, 
													IProjectionLanceeService projectionLanceeService, 
													IEvolutionLocaliseeService evolutionLocaliseeService){
		// la projection à supprimer a-t-elle été utilisée comme projection étalon par d'autres projections ?
		List<Projection> projectionsUtilisantEtalon = findByProjectionEtalon(projection);

		// supprime les projections et leurs évolutions localisées qui utilisent la projection étalon à supprimer
		if (projectionsUtilisantEtalon != null
				&& !projectionsUtilisantEtalon.isEmpty()) {
			for (Projection p : projectionsUtilisantEtalon) {
				if (projectionLanceeService.exist(p.getId())) {
					for (ProjectionLancee lancement : projectionLanceeService
							.findByProjection(p)) {
						projectionLanceeService.remove(lancement);
					}
				}
				if (p.getEvolutionsLocalisees() != null
						& !p.getEvolutionsLocalisees().isEmpty()) {
					for (EvolutionLocalisee el : p.getEvolutionsLocalisees()) {
						evolutionLocaliseeService.delete(el);
					}
				}
				remove(p);
			}
		}
	}
	
	public void supprimerProjection(Projection projection, 
			IProjectionLanceeService projectionLanceeService, 
			IEvolutionLocaliseeService evolutionLocaliseeService){


		if (projectionLanceeService.exist(projection.getId())) {
			for (ProjectionLancee lancement : projectionLanceeService
					.findByProjection(projection)) {
				projectionLanceeService.remove(lancement);
			}
		}

		if (projection.getEvolutionsLocalisees() != null
				&& !projection.getEvolutionsLocalisees().isEmpty()) {
			for (EvolutionLocalisee el : projection.getEvolutionsLocalisees()) {
				evolutionLocaliseeService.delete(el);
			}
		}

		remove(projection);

		flush();
	}
	
	
	public void triProjectionParOrdreAlphabetique(List<Projection> projections){
		Collections.sort(projections, new projectionComparator());
	}

	public List<String> findUtilisateurs(Projection projection){

		return projectionDao.findUtilisateurs(projection);
	}
	
	public void supprimer(Projection projection){
		projectionDao.delete(projection);
	}
	
	public List<Projection> findByScenario(Scenario scenario){
		return projectionDao.findByScenario(scenario);
	}
	
	public List<Projection> findByZonage(Zonage zonage){
		return projectionDao.findByZonage(zonage);
	}
	
	public Set<Projection> findByEnglobanteEtEtalon(Projection projection){
		Set<Projection> resultat = new HashSet<Projection>();
		
		List<Projection> englobantesEtEtalons = projectionDao.findByProjectionEnglobanteEtByProjectionEtalon(projection);
		resultat.addAll(englobantesEtEtalons);
		
		
		return resultat;
	}
	
	public Set<Utilisateur> findUtilisateurByEnglobanteEtEtalon(Projection projection){
		Set<Utilisateur> resultat = new HashSet<Utilisateur>();
		
		List<Utilisateur> UtilisateursProjectionsenglobantesEtEtalons = projectionDao.findUtilisateursByProjectionEnglobanteEtByProjectionEtalon(projection);
		resultat.addAll(UtilisateursProjectionsenglobantesEtEtalons);
		
		
		return resultat;
	}
	

	
	public List<Integer> findIdProjectionPartagee(Utilisateur utilisateur){
		return projectionDao.findIdProjectionPartagee(utilisateur);
	}
	
	public List<Integer> findIdProjectionNonPartagee(Utilisateur utilisateur){
		return projectionDao.findIdProjectionNonPartagee(utilisateur);
	}
	
	public List<String> findNomProjectionPartagee(Utilisateur utilisateur){
		return projectionDao.findNomProjectionPartagee(utilisateur);
	}
	
	public List<String> findNomProjectionNonPartagee(Utilisateur utilisateur){
		return projectionDao.findNomProjectionNonPartagee(utilisateur);
	}
	
	
	public List<Object> rechercheProjectionPourFonctionDeSuppression(	Utilisateur utilisateur,
																		List<Integer> projectionsASupprimerId,
																		List<Integer> projectionsAConserverId,
																		List<String> nomProjectionsASupprimer,
																		List<String> nomProjectionsAConserver){

		List<Object> resultat = new ArrayList<Object>();
		
		projectionsASupprimerId = (this.findIdProjectionNonPartagee(utilisateur));
		projectionsAConserverId = (this.findIdProjectionPartagee(utilisateur));
		nomProjectionsASupprimer = (this.findNomProjectionNonPartagee(utilisateur));
		nomProjectionsAConserver = (this.findNomProjectionPartagee(utilisateur));
		
		resultat.add(projectionsASupprimerId);
		resultat.add(projectionsAConserverId);
		resultat.add(nomProjectionsASupprimer);
		resultat.add(nomProjectionsAConserver);

		return resultat;
		
	}
	
	

	
	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer){
		return projectionDao.deleteByListIdProjection(IdsProjectionsASupprimer);
	}
	
	public Integer deleteUserPopulationByListIdProjection(List<Integer> IdsProjectionsASupprimer){
		return projectionDao.deleteUserPopulationByListIdProjection(IdsProjectionsASupprimer);

	}
	
	public Integer deleteCbPopulationByListIdProjection(List<Integer> IdsProjectionsASupprimer){
		return projectionDao.deleteCbPopulationByListIdProjection(IdsProjectionsASupprimer);

	}

	@Override
	public List<ProjectionAffichageDataTable> findAllDTO(String idUser) {
		return projectionDao.findAllDTO(idUser);
	}

	
}
