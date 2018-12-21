package fr.insee.omphale.core.service.administration;

import java.util.List;

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


public interface ISuppressionService {

	/**
	 * recherche la chaîne des objets d'un utilisateur à supprimer et à conserver
	 * <BR>
	 * car utilisée par d'autres utilisateurs que l'utilisateur spécifié en paramètre
	 * 
	 * @param utilisateur
	 * 
	 * @return int
	 */
	public int rechercheObjetsMetierUtilisateur(Utilisateur utilisateur);
	
	
	/**
	 * recherche la chaîne des objets d'un utilisateur à supprimer et à conserver
	 * <BR>
	 * car utilisée par d'autres utilisateurs que l'utilisateur spécifié en paramètre
	 * <BR>
	 * puis supprime dans le bon ordre cette chaîne d'objet
	 * 
	 * 
	 * @param utilisateur
	 */
	public void supprimerLesObjetsUtilisateurs(Utilisateur utilisateur);
	

	/** récupère la liste des noms de projections à supprimer
	 * 
	 * @return List<String>
	 */
	public List<String> getNomProjectionsASupprimer();
	
	
	/** récupère la liste des noms de hypotheses à supprimer
	 * 
	 * @return List<String>
	 */
	public List<String> getNomHypothesesASupprimer();
	
	
	/** récupère la liste des noms de scenarios à supprimer
	 * 
	 * @return List<String>
	 */
	public List<String> getNomScenariosASupprimer();
	
	
	/** récupère la liste des noms de zonages à supprimer
	 * 
	 * @return List<String>
	 */
	public List<String> getNomZonagesASupprimer();
	
	
	/** récupère la liste des noms de zones à supprimer
	 * 
	 * @return List<String>
	 */
	public List<String> getNomZonesASupprimer();
	
	
	/** récupère la liste des noms de EvoujtionsNonLocalisees à supprimer
	 * 
	 * @return List<String>
	 */
	public List<String> getNomEvolutionsNonLocaliseesASupprimer() ;
	
	
	/** récupère la liste des noms de projections à supprimer
	 * 
	 * @return List<String>
	 */
	public List<String> getNomprojectionsAConserver();
	
	
	/** récupère la liste des noms de hyptoheses à conserver
	 * 
	 * @return List<String>
	 */
	public List<String> getNomhypothesesAConserver();
	
	
	/** récupère la liste des noms de scenarios à conserver
	 * 
	 * @return List<String>
	 */
	public List<String> getNomscenariosAConserver() ;
	
	
	/** récupère la liste des noms de zonages à conserver
	 * 
	 * @return List<String>
	 */
	public List<String> getNomzonagesAConserver() ;
	
	
	/** récupère la liste des noms de zones à conserver
	 * 
	 * @return List<String>
	 */
	public List<String> getNomzonesAConserver() ;
	
	
	/** récupère la liste des noms de EvolutionsNonLocalisees à conserver
	 * 
	 * @return List<String>
	 */
	public List<String> getNomevolutionsNonLocaliseesAConserver();
	
	/**
	 * passe le service IEvolutionLocaliseeService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setEvolutionLocaliseeService(IEvolutionLocaliseeService elService);
	
	/**
	 * passe le service IProjectionService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setProjectionService(IProjectionService projectionService);
	
	/**
	 * passe le service IProjectionLanceeService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setProjectionLanceeService(IProjectionLanceeService projectionLanceeService);
	
	/**
	 * passe le service IScenarioService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setScenarioService(IScenarioService scenarioService);
	
	/**
	 * passe le service IZonageService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setZonageService(IZonageService zonageService);
	
	/**
	 * passe le service IZoneService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setZoneService(IZoneService zoneService);
	
	/**
	 * passe le service IEvolutionNonLocaliseeService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setEvolutionNonLocaliseeService(IEvolutionNonLocaliseeService enlService);
	
	/**
	 * passe le service IEvolDeScenarioService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setEvolDeScenarioService(IEvolDeScenarioService evolDeScenarioService) ;
	
	/**
	 * passe le service IGroupeEtalonService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setGroupeEtalonService(IGroupeEtalonService groupeEtalonService);
	
	/**
	 * passe le service IHypotheseService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setHypotheseService(IHypotheseService hypotheseService);
	
	/**
	 * passe le service IValeurCubeHypotheseService à l'instance de SuppressionService
	 * 
	 * @param void
	 */
	public void setValeurCubeHypotheseService(IValeurCubeHypotheseService valeurCubeHypotheseService);
	
}

