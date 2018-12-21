package fr.insee.omphale.core.service.administration.impl;

import java.util.ArrayList;
import java.util.List;

import fr.insee.omphale.core.service.administration.ISuppressionService;
import fr.insee.omphale.core.service.geographie.IGroupeEtalonService;
import fr.insee.omphale.core.service.geographie.IZonageService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.core.service.projection.IEvolDeScenarioService;
import fr.insee.omphale.core.service.projection.IEvolutionLocaliseeService;
import fr.insee.omphale.core.service.projection.IEvolutionNonLocaliseeService;
import fr.insee.omphale.core.service.projection.IHypotheseService;
import fr.insee.omphale.core.service.projection.IProjectionLanceeService;
import fr.insee.omphale.core.service.projection.IProjectionService;
import fr.insee.omphale.core.service.projection.IScenarioService;
import fr.insee.omphale.core.service.projection.IValeurCubeHypotheseService;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zone;

public class SuppressionService implements ISuppressionService {
	
	private IZoneService zoneService;
	private IZonageService zonageService;
	private IEvolutionNonLocaliseeService evolutionNonLocaliseeService;
	private IProjectionService projectionService ;
	private IEvolutionLocaliseeService evolutionLocaliseeService;
	private IScenarioService scenarioService;
	private IProjectionLanceeService projectionLanceeService;
	private IEvolDeScenarioService evolDeScenarioService;
	private IGroupeEtalonService groupeEtalonService;
	private IHypotheseService hypotheseService;
	@SuppressWarnings("unused")
	private IValeurCubeHypotheseService valeurCubeHypotheseService;

	
	private List<Integer> projectionsASupprimerId ;
	private List<Integer> hypothesesASupprimerId;
	private List<Integer> scenariosASupprimerId;
	private List<String> groupesEtalonsASupprimerId;
	private List<Integer> zonagesASupprimerId;
	private List<Integer> zonesASupprimerId;
	private List<Integer> evolutionsNonLocaliseesASupprimerId;

	@SuppressWarnings("unused")
	private List<Integer> projectionsAConserverId;
	@SuppressWarnings("unused")
	private List<Integer> hypothesesAConserverId;
	@SuppressWarnings("unused")
	private List<Integer> scenariosAConserverId;
	@SuppressWarnings("unused")
	private List<String> groupesEtalonstAConserverId;
	@SuppressWarnings("unused")
	private List<Integer> zonagesAConserverId ;
	@SuppressWarnings("unused")
	private List<Integer> zonesAConserverId;
	@SuppressWarnings("unused")
	private List<Integer> evolutionsNonLocaliseesAConserverId;
	
	private List<String> nomProjectionsASupprimer ;
	private List<String> nomHypothesesASupprimer;
	private List<String> nomScenariosASupprimer;
	private List<String> nomZonagesASupprimer;
	private List<String> nomZonesASupprimer;
	private List<String> nomEvolutionsNonLocaliseesASupprimer;

	
	private List<String> nomProjectionsAConserver;
	private List<String> nomHypothesesAConserver;
	private List<String> nomScenariosAConserver;
	private List<String> nomZonagesAConserver;
	private List<String> nomZonesAConserver;
	private List<String> nomEvolutionsNonLocaliseesAConserver;
	
	@SuppressWarnings("unused")
	private List<Zone> zonesDesZonagesUtilisateur;
	
	
	public int rechercheObjetsMetierUtilisateur(Utilisateur utilisateur){

		rechercheProjections(utilisateur);
		rechercheEvolutionsNonLocalisees(utilisateur);
		rechercheScenarios(utilisateur);
		rechercheGroupesEtalons(utilisateur);
		rechercheZonages(utilisateur);
		rechercheZones(utilisateur);
		rechercheHypotheses(utilisateur);
		
		return 	projectionsASupprimerId.size() +
				evolutionsNonLocaliseesASupprimerId.size() +
				scenariosASupprimerId.size()  + 
				groupesEtalonsASupprimerId.size() +
				zonagesASupprimerId.size() + 
				zonesASupprimerId.size() +
				hypothesesASupprimerId.size()	;
		}
	
	public void supprimerLesObjetsUtilisateurs(Utilisateur utilisateur){
		supprimerProjectionsUtilisateur();
		supprimerENLsUtilisateur();
		supprimerScenariosUtilisateur();
		supprimerGroupesEtalonsUtilisateur();
		supprimerZonagesUtilisateur();
		supprimerZonesUtilisateur();
		supprimerHypothesesUtilisateur();
	}
	
	/**
	 * 
	 * supprime les projections d'utilisateur
	 * 
	 */
	public void supprimerProjectionsUtilisateur(){

		if(!projectionsASupprimerId.isEmpty()){

			evolutionLocaliseeService.deleteByListIdProjection(projectionsASupprimerId);
			
			projectionLanceeService.deleteByListIdProjection(projectionsASupprimerId);
			
			projectionService.deleteCbPopulationByListIdProjection(projectionsASupprimerId);
			
			projectionService.deleteUserPopulationByListIdProjection(projectionsASupprimerId);

			projectionService.deleteByListIdProjection(projectionsASupprimerId);
			
		}

	}
	
	/**
	 * 
	 * supprime les Scenarios d'utilisateur
	 * 
	 */
	public void supprimerScenariosUtilisateur(){
		
		if(!scenariosASupprimerId.isEmpty()){
			// suppression des evol_de_scenario
			evolDeScenarioService.deleteByIdScenario(scenariosASupprimerId);
			
			// suppression des scenarios
			 scenarioService.deleteByIdScenario(scenariosASupprimerId);

		}
	}
	
	/**
	 * 
	 * supprime les évolutions non localisées d'utilisateur
	 * 
	 */
	public void supprimerENLsUtilisateur(){
		
		// suppression des évolutions non localisées
		if(!evolutionsNonLocaliseesASupprimerId.isEmpty()){
			// suppression des références dans evol_de_scenar
			evolDeScenarioService.deleteByListeIdEvolutionNonLocalisee(evolutionsNonLocaliseesASupprimerId);			
			evolutionLocaliseeService.deleteByListeIdEvolutionNonLocalisee(evolutionsNonLocaliseesASupprimerId);
			
			// suppression des enl
			evolutionNonLocaliseeService.deleteByListeIdEvolutionNonLocalisee(evolutionsNonLocaliseesASupprimerId);

		}
	}
	
	/**
	 * 
	 * supprime les groupes étalons d'utilisateur
	 * 
	 */
	public void supprimerGroupesEtalonsUtilisateur(){
		
		if(!groupesEtalonsASupprimerId.isEmpty()){
			// suppression des références dans zone_de_groupet
			groupeEtalonService.deleteZoneDeGroupetByListeIdGroupeEtalon(groupesEtalonsASupprimerId);
	
			// suppression des références dans commune résiduelle
			 groupeEtalonService.deleteCommuneResiduelleByListeIdGroupeEtalon(groupesEtalonsASupprimerId);
			
			// suppression des références dasn dept_de_groupet
			 groupeEtalonService.deletedept_de_groupetByListeIdGroupeEtalon(groupesEtalonsASupprimerId);

			
			// suppression des groupes_etalons
			 groupeEtalonService.deleteGroupeEtalonByListeIdGroupeEtalon(groupesEtalonsASupprimerId);
			
		}

	}
	
	/**
	 * 
	 * supprime les zonages d'utilisateur
	 * 
	 */
	public void supprimerZonagesUtilisateur(){
		
		if(!zonagesASupprimerId.isEmpty()){
			// suppression des zone_de_zonage
			zonageService.deleteZoneDeZonageByListeIdZonage(zonagesASupprimerId);

			// suppression des zonages
			zonageService.deleteZonageByListeIdZonage(zonagesASupprimerId);

		}
	}
	
	/**
	 * 
	 * supprime les zones d'utilisateur
	 * 
	 */
	public void supprimerZonesUtilisateur(){
		
		if(!zonesASupprimerId.isEmpty()){
			// suppression des références dans Departement_impact
			zoneService.deleteDepartement_impactByListeIdZone("Departement_impact di","di.zone",zonesASupprimerId);
			
			// suppression des références dans Commune_de_zone
			zoneService.deleteCommuneDeZoneByListeIdZone("COMMUNE_DE_ZONE CDZ","CDZ.zone", zonesASupprimerId);
			
			// suppression des zones
			 zoneService.deleteZonesByListeIdZone("zone z","z.id_zone",zonesASupprimerId);

		}

	}
	
	/**
	 * 
	 * supprime les hypotheses d'utilisateur
	 * 
	 */
	public void supprimerHypothesesUtilisateur(){

		if(!hypothesesASupprimerId.isEmpty()){

			hypotheseService.deleteCbHypotheseByIdListeHypothese(hypothesesASupprimerId); 
			
			evolutionNonLocaliseeService.deleteHypotheseByIdListeHypothese(hypothesesASupprimerId);

			hypotheseService.deleteHypotheseByIdListeHypothese(hypothesesASupprimerId);

		}
	}
	
	/**
	 * 
	 * recherche les projections d'un utilisateur à supprimer et à conserver
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void rechercheProjections(Utilisateur utilisateur){

		List<Object> resultat = projectionService.rechercheProjectionPourFonctionDeSuppression(	utilisateur, 
																		new ArrayList<Integer>(), 
																		new ArrayList<Integer>(),
																		new ArrayList<String>(), 
																		new ArrayList<String>());
		
		projectionsASupprimerId = (List<Integer>)resultat.get(0);		
		projectionsAConserverId = (List<Integer>)resultat.get(1);
		nomProjectionsASupprimer = (List<String>)resultat.get(2);
		nomProjectionsAConserver = (List<String>)resultat.get(3);
	}
	
	 /**
	 * recherche les hypothèses d'un utilisateur à supprimer et à conserver
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void rechercheHypotheses(Utilisateur utilisateur){
		
		List<Object> resultat = hypotheseService.rechercheHypothesesPourFonctionSuppression(	utilisateur, 
																								new ArrayList<Integer>(), 
																								new ArrayList<Integer>(), 
																								new ArrayList<String>(),
																								new ArrayList<String>() );
		
		hypothesesASupprimerId = (List<Integer>) resultat.get(0) ;		
		hypothesesAConserverId = (List<Integer>) resultat.get(1) ;
		nomHypothesesASupprimer = (List<String>) resultat.get(2) ;
		nomHypothesesAConserver = (List<String>) resultat.get(3) ;
	}
	
	

	
	 /**
	 * recherche les scénarios d'un utilisateur à supprimer et à conserver
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void rechercheScenarios(Utilisateur utilisateur){

		List<Object> resultat = scenarioService.rechercheScenariosPourFonctionSuppression(	utilisateur, 
																							new ArrayList<Integer>(), 
																							new ArrayList<Integer>(),
																							new ArrayList<String>(),
																							new ArrayList<String>());
		
		scenariosASupprimerId  = (List<Integer>) resultat.get(0);
		scenariosAConserverId   = (List<Integer>) resultat.get(1);
		nomScenariosASupprimer  = (List<String>) resultat.get(2);
		nomScenariosAConserver  = (List<String>) resultat.get(3);

	}
	
	 /**
	 * recherche les évolutions non localisées d'un utilisateur à supprimer et à conserver
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void rechercheEvolutionsNonLocalisees(Utilisateur utilisateur){

		List<Object> resultat = evolutionNonLocaliseeService.rechercheEvolutionsNonLocaliseesPourFonctionSuppression(	utilisateur, 
																					new ArrayList<Integer>(), 
																					new ArrayList<Integer>(),
																					new ArrayList<String>(),
																					new ArrayList<String>());
	
		evolutionsNonLocaliseesASupprimerId = (List<Integer>) resultat.get(0) ;
		evolutionsNonLocaliseesAConserverId = (List<Integer>) resultat.get(1);
		nomEvolutionsNonLocaliseesASupprimer = (List<String>) resultat.get(2) ;
		nomEvolutionsNonLocaliseesAConserver = (List<String>) resultat.get(3) ;
	}

	 /**
	 * recherche les groupes étalons d'un utilisateur à supprimer et à conserver
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void rechercheGroupesEtalons(Utilisateur utilisateur){
		
		List<Object> resultat = groupeEtalonService.rechercheGroupesEtalonsPourFonctionSuppression(	utilisateur, 
																									new ArrayList<String>(), 
																									new ArrayList<String>());
		
		groupesEtalonsASupprimerId = (List<String>) resultat.get(0);
		groupesEtalonstAConserverId = (List<String>) resultat.get(1);
	}
	
	 /**
	 * recherche les zonages d'un utilisateur à supprimer et à conserver
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void rechercheZonages(Utilisateur utilisateur){
		
		List<Object> resultat = zonageService.rechercheZonagesPourFonctionSuppression(	utilisateur, 
																new ArrayList<Integer>(), 
																new ArrayList<Integer>(),
																new ArrayList<String>(),
																new ArrayList<String>()
																);
		
		zonagesASupprimerId = (List<Integer>)resultat.get(0);
		zonagesAConserverId = (List<Integer>)resultat.get(1);
		nomZonagesASupprimer = (List<String>)resultat.get(2);
		nomZonagesAConserver = (List<String>)resultat.get(3);

	}
	
	
	 /**
	 * recherche les zones d'un utilisateur à supprimer et à conserver
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void rechercheZones(Utilisateur utilisateur){

		List<Object> resultat = zoneService.rechercheZonesPourFonctionSuppression(	utilisateur, 
				new ArrayList<Integer>(), 
				new ArrayList<Integer>(),
				new ArrayList<String>(),
				new ArrayList<String>()
				);

			zonesASupprimerId = (List<Integer>)resultat.get(0);
			zonesAConserverId = (List<Integer>)resultat.get(1);
			nomZonesASupprimer = (List<String>)resultat.get(2);
			nomZonesAConserver = (List<String>)resultat.get(3);

	}
	
	
	public void setZoneService(IZoneService zoneService) {
		this.zoneService = zoneService;
	}


	public void setZonageService(IZonageService zonageService) {
		this.zonageService = zonageService;
	}


	public void setProjectionService(IProjectionService projectionService) {
		this.projectionService = projectionService;
	}

	public void setEvolutionLocaliseeService(
			IEvolutionLocaliseeService evolutionLocaliseeService) {
		this.evolutionLocaliseeService = evolutionLocaliseeService;
	}


	public void setScenarioService(IScenarioService scenarioService) {
		this.scenarioService = scenarioService;
	}

	public void setEvolutionNonLocaliseeService(
			IEvolutionNonLocaliseeService evolutionNonLocaliseeService) {
		this.evolutionNonLocaliseeService = evolutionNonLocaliseeService;
	}

	public void setProjectionLanceeService(
			IProjectionLanceeService projectionLanceeService) {
		this.projectionLanceeService = projectionLanceeService;
	}

	public void setEvolDeScenarioService(
			IEvolDeScenarioService evolDeScenarioService) {
		this.evolDeScenarioService = evolDeScenarioService;
	}

	public void setGroupeEtalonService(IGroupeEtalonService groupeEtalonService) {
		this.groupeEtalonService = groupeEtalonService;
	}

	public void setHypotheseService(IHypotheseService hypotheseService) {
		this.hypotheseService = hypotheseService;
	}

	public void setValeurCubeHypotheseService(
			IValeurCubeHypotheseService valeurCubeHypotheseService) {
		this.valeurCubeHypotheseService = valeurCubeHypotheseService;
	}


	public IZonageService getZonageService() {
		return zonageService;
	}

	public IProjectionLanceeService getProjectionLanceeService() {
		return projectionLanceeService;
	}



	public List<String> getNomProjectionsASupprimer() {
		return nomProjectionsASupprimer;
	}

	public List<String> getNomHypothesesASupprimer() {
		return nomHypothesesASupprimer;
	}

	public List<String> getNomScenariosASupprimer() {
		return nomScenariosASupprimer;
	}

	public List<String> getNomZonagesASupprimer() {
		return nomZonagesASupprimer;
	}

	public List<String> getNomZonesASupprimer() {
		return nomZonesASupprimer;
	}

	public List<String> getNomEvolutionsNonLocaliseesASupprimer() {
		return nomEvolutionsNonLocaliseesASupprimer;
	}

	public List<String> getNomprojectionsAConserver() {
		return nomProjectionsAConserver;
	}

	public List<String> getNomhypothesesAConserver() {
		return nomHypothesesAConserver;
	}

	public List<String> getNomscenariosAConserver() {
		return nomScenariosAConserver;
	}

	public List<String> getNomzonagesAConserver() {
		return nomZonagesAConserver;
	}

	public List<String> getNomzonesAConserver() {
		return nomZonesAConserver;
	}

	public List<String> getNomevolutionsNonLocaliseesAConserver() {
		return nomEvolutionsNonLocaliseesAConserver;
	}

}
