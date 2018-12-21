package fr.insee.omphale.batch.lanceurs;

import java.util.List;

import org.apache.log4j.LogManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.transversal.exception.OmphaleException;
import fr.insee.omphale.core.service.IUtilisateurService;
import fr.insee.omphale.core.service.administration.ISuppressionService;
import fr.insee.omphale.core.service.administration.impl.SuppressionService;
import fr.insee.omphale.core.service.geographie.IGroupeEtalonService;
import fr.insee.omphale.core.service.geographie.IZonageService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.core.service.geographie.impl.GroupeEtalonService;
import fr.insee.omphale.core.service.geographie.impl.ZonageService;
import fr.insee.omphale.core.service.geographie.impl.ZoneService;
import fr.insee.omphale.core.service.impl.UtilisateurService;
import fr.insee.omphale.core.service.projection.IEvolDeScenarioService;
import fr.insee.omphale.core.service.projection.IEvolutionLocaliseeService;
import fr.insee.omphale.core.service.projection.IEvolutionNonLocaliseeService;
import fr.insee.omphale.core.service.projection.IHypotheseService;
import fr.insee.omphale.core.service.projection.IProjectionLanceeService;
import fr.insee.omphale.core.service.projection.IProjectionService;
import fr.insee.omphale.core.service.projection.IScenarioService;
import fr.insee.omphale.core.service.projection.IValeurCubeHypotheseService;
import fr.insee.omphale.core.service.projection.impl.EvolDeScenarioService;
import fr.insee.omphale.core.service.projection.impl.EvolutionLocaliseeService;
import fr.insee.omphale.core.service.projection.impl.EvolutionNonLocaliseeService;
import fr.insee.omphale.core.service.projection.impl.HypotheseService;
import fr.insee.omphale.core.service.projection.impl.ProjectionLanceeService;
import fr.insee.omphale.core.service.projection.impl.ProjectionService;
import fr.insee.omphale.core.service.projection.impl.ScenarioService;
import fr.insee.omphale.core.service.projection.impl.ValeurCubeHypotheseService;
import fr.insee.omphale.dao.factory.DAOFactory;
import fr.insee.omphale.dao.util.HibernateUtil;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphrefo.batch.Batch;
import fr.insee.omphrefo.batch.ECodeRetour;
import fr.insee.omphrefo.batch.RetourBatch;

public class TraiteSuppressionObjetsMetiers implements Batch {
	
	private RetourBatch retour ;
	private int codeStatus;
	private IEvolutionLocaliseeService elService;
	private IEvolutionNonLocaliseeService enlService;
	private IZoneService zoneService;
	private IZonageService zonageService;
	private IScenarioService scenarioService;
	private IUtilisateurService utilisateurService;
	private IProjectionService projectionService;
	private	ISuppressionService suppressionService;
	private IProjectionLanceeService projectionLanceeService;
	private IEvolDeScenarioService evolDeScenarioService;
	private IGroupeEtalonService groupeEtalonService;
	private IHypotheseService hypotheseService;
	private IValeurCubeHypotheseService valeurCubeHypotheseService;
	

	@Override
	public RetourBatch executer(String[] args) throws OmphaleException {
        retour = new RetourBatch(
                ECodeRetour.EXECUTION_CORRECTE.getCode(), "Tout est OK");
     
        try {

        	codeStatus= supprimeObjetsMetiersUtilisateur();
            retour = endProgram(codeStatus);
            
        } catch (Throwable t) {
            t.printStackTrace();
            // gestion du 202
            endProgram(CacheDaoManager.STATUS_ERROR_CONFIG);
        }

        return retour;
	}
	
	
	
	  private int supprimeObjetsMetiersUtilisateur() {
		 
		Session session = initialisationBatch();
		Transaction tx = session.beginTransaction();
		


		List<Utilisateur> utilisateursASupprimer = utilisateurService.findByRole(3);
		Utilisateur utilisateurEnCours = null;
		try{
			for (Utilisateur utilisateur : utilisateursASupprimer) {
				if(session == null || !session.isOpen()){
					session  = initialisationBatch();
					tx = session.beginTransaction();
				}
				
				utilisateurEnCours = utilisateur;
				@SuppressWarnings("unused")
				int resultat = suppressionService.rechercheObjetsMetierUtilisateur(utilisateur);
				suppressionService.supprimerLesObjetsUtilisateurs(utilisateur);
				utilisateurService.supprimer(utilisateur);
				tx.commit();
			}
		} catch (Exception e){
			e.printStackTrace();
			new Exception("problème survenue lors de la suppression des objets métiers de l'utilisateur " + utilisateurEnCours.getIdep());
		}

		
		return 10;
	}
	  
	private Session initialisationBatch(){
		HibernateUtil.setFichierConfigHibernate("hibernate/hibernateCore.cfg.xml");

		DAOFactory.setActiveFactoryCoreOmphale(true);
		
		SessionFactory sf = HibernateUtil.getSessionFactory();
		Session session = sf.getCurrentSession();


		
		elService = new EvolutionLocaliseeService();
		enlService = new EvolutionNonLocaliseeService();
		zoneService = new ZoneService();
		zonageService = new ZonageService();
		scenarioService = new ScenarioService();
		utilisateurService = new UtilisateurService();
		projectionService = new ProjectionService();
		suppressionService = new SuppressionService();
		projectionLanceeService = new ProjectionLanceeService();
		evolDeScenarioService = new EvolDeScenarioService();
		groupeEtalonService = new GroupeEtalonService();
		hypotheseService = new HypotheseService();
		valeurCubeHypotheseService = new ValeurCubeHypotheseService();
		
		
		suppressionService.setEvolutionLocaliseeService(elService);
		suppressionService.setProjectionService(projectionService);
		suppressionService.setScenarioService(scenarioService);
		suppressionService.setZonageService(zonageService);
		suppressionService.setZoneService(zoneService);
		suppressionService.setEvolutionNonLocaliseeService(enlService);
		suppressionService.setProjectionLanceeService(projectionLanceeService);
		suppressionService.setEvolDeScenarioService(evolDeScenarioService);
		suppressionService.setGroupeEtalonService(groupeEtalonService);
		suppressionService.setHypotheseService(hypotheseService);
		suppressionService.setValeurCubeHypotheseService(valeurCubeHypotheseService);
		
		return session;


	}



	/**
     * Provoque la fin de l'execution du programme courant. <br>
     * Cette méthode loggue les informations nécessaires, effectue le nettoyage
     * requis (en particulier fermeture des logs) et sort de la JVM avec le code
     * retour donné.
     *
     * @param status
     *            le code retour final.
     */
    private RetourBatch endProgram(int status) {

        if (status != 201) {
	        CacheDaoManager.beanRapport.code=status;
        	retour = new RetourBatch(ECodeRetour.ECHEC);
        }
        else if (status == 0) {
	        CacheDaoManager.beanRapport.code=status;
	    	retour = new RetourBatch(ECodeRetour.EXECUTION_CORRECTE);
        }
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogManager.shutdown();
        return retour;
    }

}
