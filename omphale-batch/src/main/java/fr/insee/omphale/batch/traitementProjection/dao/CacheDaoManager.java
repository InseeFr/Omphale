package fr.insee.omphale.batch.traitementProjection.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fr.insee.omphale.batch.cps.Cerveau;
import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.traitementProjection.service.impl.OrdonnanceurService;
import fr.insee.omphale.batch.transversal.bean.BeanRapport;
import fr.insee.omphale.batch.transversal.bean.BeanScript;
import fr.insee.omphale.batch.transversal.exception.OmphaleConfigException;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.batch.transversal.exception.OmphaleSqlException;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.batch.transversal.webServices.impl.IgesaClientWS;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ScriptSqlDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.filescriptsql.ScriptSqlLibrary;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.ContexteApplication;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.IConnectionManager;



/**
 * classe statique ne comportant que des méthodes statiques destinée à gérer et à piloter des CPS
 * <BR>
 * aussi bien pour les neurones liées au service batch neuroneProceduralService que ceux liées au 
 * <BR>
 * mapping objet des tables Oracle. 
 * 
 * Génération des beans intelligents de type FROM
 * (pour File Relational Object Mapping) correspondant
 * au mapping objet des fichiers au format CSV (extension .txt)
 * situés dans le dossier src/test/resources et
 * représentant les extraits de tables relationnelles.
 * Cette génération doit être complêtée par les
 * méthodes d'accès aux neurones via le système CPS,
 * la méthode collabo permettant de compléter le mapping
 * objet pour les relations uniques ou mutltiples, à sens
 * unique ou dans les deux sens en fonction des tests envisagés.
 *
 */
public class CacheDaoManager {

    /**
     * Constante de sortie sans erreur. La demande en cours à été traitée. On
     * peut relancer le batch pour traiter la suivante.
     */
    public static final int STATUS_SUCCES = 0;

    /**
     * Constante de sortie avec erreur grave. La demande en cours à été traitée
     * et a provoqué une erreur grave. On ne doit pas relancer le batch pour
     * traiter la suivante tant qu'on n'a pas expertisé le problème.
     */
    public static final int STATUS_ERROR_CONFIG = 202;

    /**
     * Constante de sortie avec erreur métier. La demande en cours à été traitée
     * et a provoqué une erreur métier. On peut relancer le batch pour traiter la
     * suivante tant qu'on n'a pas expertisé le problème.
     */

    public static final int STATUS_ERROR_OMPHALE = 201;
    /**
     * Constante de sortie sans traitement. Il n'y a pas de demande à traiter.
     * On peut relancer le batch après une pause.
     */
    public static final int STATUS_RIEN_A_FAIRE = 10;

    public static BeanRapport beanRapport = new BeanRapport();
    
    public static ISimpleDao simpleDao = null;
    
    public static ISimpleSelectDao simpleSelectDao = null;

    public static Cerveau cerveau;

    // table pour stocker et accéder aux neurones indépendemment du Cerveau
    // renseignée lors des appels à la méthode collabo
    public static ArrayList<OrdonnanceurService> tableProceduralServiceTraitement;
    public static ArrayList<OrdonnanceurService> tableProceduralServiceFin;
    public static ArrayList<Evol_de_scenarDAO> tableEvol_de_scenar;
    public static ArrayList<Evol_non_locDAO> tableEvol_non_loc;
    public static ArrayList<Def_projectionDAO> tableDef_projection;
    public static ArrayList<ProjectionEtalonDAO> tableProjectionEtalon;
    public static ArrayList<ProjectionEnglobanteDAO> tableProjectionEnglobante;
    public static ArrayList<Projection_lanceeDAO> tableProjection_lancee;
    public static ArrayList<Scenar_non_locDAO> tableScenar_non_loc;
    public static ArrayList<User_omphaleDAO> tableUser_omphale;
    public static ArrayList<User_hypotheseDAO> tableUser_hypothese;
    public static ArrayList<ZonageDAO> tableZonage;
    public static ArrayList<DAOZone> tableZone;
    public static ArrayList<Zone_de_zonageDAO> tableZone_de_zonage;
    public static ArrayList<Evol_localiseDAO> tableEvol_localise;

    /**
     * permet d'utiliser Omphale en mode Test
     */
    public static boolean testMode = false;

    
    /**
     * ce mode indique que les données sont dans une base de données à True
     * False dans des fichiers
     */
    public static boolean sqlMode = true;

    public static String etapeSetup;

    public static String traceExecution;

    public static void reinit() {
        beanRapport = new BeanRapport();
        testMode = false;
        sqlMode = true;
        etapeSetup=null;
        traceExecution=null;
    }

    /**
     * permet de vérifier que le 1er neurone projection lancée existe et de récupérer son objet
     * <BR>
     * @return NeuroneProjection_lancee
     */
    public static Projection_lanceeDAO getFirstProjectionLancee() {
        if (tableProjection_lancee.size() == 0)
            return null;
        return tableProjection_lancee.get(0);
    }

    /**
     * contrôle l'existence du 1ère définition de projection
     * @return NeuroneProjection_lancee
     */
    private static Def_projectionDAO getFirstDef_projection() {
        if (tableDef_projection.size() == 0)
            return null;
        return tableDef_projection.get(0);
    }

    /**
     * Méthode pour accéder indépendamment du cerveau au premier neurone des scénarios non loc
     * <BR>
     * contrôle qu'il y en a bien un 
     * Table sans ligne signifie que intégrité incohérente de la base
     * @return NeuroneProjection_lancee
     */
    private static Scenar_non_locDAO getFirstScenar_non_loc() {
        if (tableScenar_non_loc.size() == 0)
            return null;
        return tableScenar_non_loc.get(0);
    }

    /**
     * Méthode pour accéder indépendamment du cerveau au premier neurone des projections lancées
     * <BR>
     * contrôle qu'il y en a bien un 
     * Table sans ligne signifie que intégrité incohérente de la base
     * @return NeuroneProjection_lancee
     */
    private static User_omphaleDAO projectionATraiter() {
        if (tableUser_omphale.size() == 0)
            return null;
        return tableUser_omphale.get(0);
    }

    /**
     * Méthode pour accéder indépendamment du cerveau au premier neurone des projections lancées
     * <BR>
     * contrôle qu'il y en a bien un 
     * Table sans ligne signifie que intégrité incohérente de la base
     * @return NeuroneProjection_lancee
     */
    private static ZonageDAO getFirstZonage() {
        if (tableZonage.size() == 0)
            return null;
        return tableZonage.get(0);
    }

    /**
     * Initialise le CPS (Cerveau  Primitif Spécialisé), les différents neurones et les liens entre ces neurones
     * <BR>
     * <ol type="square"> 
     *		<li>Cerveau</li>
     *		<li>NeuroneProceduralService</li>
     *		<li>NeuroneProjection_lancee</li>
     *		<li>NeuroneDef_projection</li>
     *		<li>liensProjection_lancee()</li>
     *		<li>NeuroneUser_omphale</li>
     *		<li>NeuroneScenar_non_loc</li>
     *		<li>NeuroneZonage</li>
     *		<li>NeuroneEvol_de_scenar</li>
     *		<li>NeuroneEvol_non_loc</li>
     *		<li>NeuroneUser_hypothese</li>
     *		<li>NeuroneZone_de_zonage</li>
     *		<li>NeuroneZone</li>
     *		<li>NeuroneEvol_localise</li>
     *		<li>NeuroneProjectionEtalon</li>
     *		<li>NeuroneProjectionEnglobante</li>
     *		<li>relations() qui  créé les liens entre tous ces neurones</li>
     * </ol>
     * @return int
     */
    public static int setUp() {
        try {
            beanRapport.montre.start();
            cerveau = new Cerveau();
            cerveau.createSynapse(new OrdonnanceurService());
            
            if (sqlMode) {
                etapeSetup = " de la connexion dao";
                simpleDao = (ISimpleDao) ContexteApplication.getDao(
                        "simpleDao", OmphaleBatchConfig.nomConnexion);
                simpleSelectDao= (ISimpleSelectDao) ContexteApplication.getDao(
                        "simpleSelectDao", OmphaleBatchConfig.nomConnexion);
            }
            if (!testMode || !sqlMode) {
                etapeSetup = "de la recherche de projection lancee";
                cerveau.createSynapse(new Projection_lanceeDAO());
                if (getFirstProjectionLancee() == null)
                    return STATUS_RIEN_A_FAIRE;

                if (!testMode && sqlMode) {
                    etapeSetup = "de la mise à jour de la date de début d'exécution de la projection lancee";
                    simpleDao.executeUpdate(getFirstProjectionLancee()
                            .getSqlDebut());
                }
            }
            etapeSetup = "de la recherche de la definition de projection";
            cerveau.createSynapse(new Def_projectionDAO());
            if (getFirstDef_projection() == null) {
                beanRapport.message = "Erreur métier : Pas de définition de projection";
                beanRapport.code = STATUS_ERROR_OMPHALE;
                return STATUS_ERROR_OMPHALE;
            }
            if (!testMode || !sqlMode) liensProjection_lancee();
            etapeSetup = "de la recherche du user omphale";
            cerveau.createSynapse(new User_omphaleDAO());
            if (projectionATraiter() == null) {
                beanRapport.message = "Erreur métier : Pas de User Omphale";
                beanRapport.code = STATUS_ERROR_OMPHALE;
                return STATUS_ERROR_OMPHALE;
            }

          {	
            	// condition mise en place pour accèder aux objets métiers de l'administrateur 
            	 User_omphaleDAO user = projectionATraiter();
            	 beanRapport.utilisateur = user.IDEP;
            	 		//La condition if est mise en commentaire car elle ne fonctionne pas,
            	 		//l'idep de l'administrateur etant caché. Pour rendre ces lignes fonctionnelles, il 
            	 		//faut remplacer idep_administrateur par le vrai idep de l'administrateur.
//            	if(!"idep_administrateur".equals(user.IDEP)){
	                int rc = authentification();
	                if (rc != STATUS_SUCCES)
	                    return rc;
//            	}
            }
            
            etapeSetup = "de la recherche de scenario non localisé";
            cerveau.createSynapse(new Scenar_non_locDAO());
            if (getFirstScenar_non_loc() == null) {
                beanRapport.message = "Erreur métier : Pas de scénario non localisé";
                beanRapport.code = STATUS_ERROR_OMPHALE;
                return STATUS_ERROR_OMPHALE;
            }
            etapeSetup = "de la recherche de zonage";
            cerveau.createSynapse(new ZonageDAO());
            if (getFirstZonage() == null) {
                beanRapport.message = "Erreur métier : Pas de zonage";
                beanRapport.code = STATUS_ERROR_OMPHALE;
                return STATUS_ERROR_OMPHALE;
            }
            etapeSetup = "de la recherche de evolution de scenario";
            cerveau.createSynapse(new Evol_de_scenarDAO());
            etapeSetup = "de la recherche de evolution non localisée";
            cerveau.createSynapse(new Evol_non_locDAO());
            etapeSetup = "de la recherche de User hypothese";
            cerveau.createSynapse(new User_hypotheseDAO());
            etapeSetup = "de la recherche de Zone de zonage";
            cerveau.createSynapse(new Zone_de_zonageDAO());
            etapeSetup = "de la recherche de Zone";
            cerveau.createSynapse(new DAOZone());
            etapeSetup = "de la recherche de evolution localisée";
            cerveau.createSynapse(new Evol_localiseDAO());
            etapeSetup = "de la recherche de Projection étalon";
            cerveau.createSynapse(new ProjectionEtalonDAO());
            etapeSetup = "de la recherche de Projection englobante";
            cerveau.createSynapse(new ProjectionEnglobanteDAO());
            relations();
            if (!testMode) {
              beanRapport.ficheProjection = getFicheIdentite();
              beanRapport.ficheProjectionHTML = getFicheIdentiteHTML();
              beanRapport.results = resultatsConfiguration();
              beanRapport.id_projection_lancee=getFirstProjectionLancee().ID_PROJECTION_LANCEE;
              beanRapport.id_projection=getFirstProjectionLancee().ID_PROJECTION;
            }
        } catch (Exception e) {
            beanRapport.message = "Erreur grave lors " + etapeSetup;
            e.printStackTrace();
            beanRapport.code = STATUS_ERROR_CONFIG;
            return STATUS_ERROR_CONFIG;
        }
        return STATUS_SUCCES;
    }

    /**
     * Crée les relations entre les différents neurones
     * @return void
     * @throws Exception void
     */
    private static void relations() throws Exception {
        etapeSetup = "de la recherche de Relations de definition de projection";
        liensDef_projection();
        etapeSetup = "de la recherche de Relations de Scenario non localise";
        liensScenar_non_loc();
        etapeSetup = "de la recherche de Relations de evolution de scenario";
        liensEvol_de_scenar();
        etapeSetup = "de la recherche de Relations de zonage";
        liensZonage();
        etapeSetup = "de la recherche de Relations de zone de zonage";
        liensZone_de_zonage();
        etapeSetup = "de la recherche de Relations de evolution localise";
        liensEvol_localise();
        etapeSetup = "de la recherche de Relations de evolution non localise";
        liensEvol_non_loc();
    }

    /**
     * lie le neurone projection lancée et le neurone définition de projection
     * @return void
     * @throws Exception
     */
    private static void liensProjection_lancee() throws Exception {
        Def_projectionDAO projCur = new Def_projectionDAO();
        for (int i = 0; i < tableProjection_lancee.size(); i++) {
            Projection_lanceeDAO lancCur = (Projection_lanceeDAO) tableProjection_lancee
                    .get(i);
            projCur.ID_PROJECTION = lancCur.ID_PROJECTION;
            lancCur.def_projection = (Def_projectionDAO) cerveau
                    .getRegle(projCur);
        }

    }

    /**
     * Lie les neurones zonage, scenario, user, EL, projection étaln et projection englobante
     * @return void
     * @throws Exception
     */
    private static void liensDef_projection() throws Exception {
        ZonageDAO zonagCur = new ZonageDAO();
        Scenar_non_locDAO scenarioCur = new Scenar_non_locDAO();
        User_omphaleDAO userCur = new User_omphaleDAO();
        Evol_localiseDAO evol = new Evol_localiseDAO();

        ProjectionEtalonDAO pet = new ProjectionEtalonDAO();
        ProjectionEnglobanteDAO peg = new ProjectionEnglobanteDAO();

        for (int i = 0; i < tableDef_projection.size(); i++) {
            Def_projectionDAO projCur = (Def_projectionDAO) tableDef_projection
                    .get(i);
            zonagCur.ID_ZONAGE = projCur.ID_ZONAGE;
            projCur.zonage = (ZonageDAO) cerveau.getRegle(zonagCur);
            scenarioCur.ID_SCENARIO = projCur.ID_SCENARIO;
            projCur.scenar_non_loc = (Scenar_non_locDAO) cerveau
                    .getRegle(scenarioCur);
            userCur.ID_USER = projCur.ID_USER;
            projCur.user_omphale = (User_omphaleDAO) cerveau
                    .getRegle(userCur);

            pet.ID_PROJECTION = projCur.ID_PROJ_ETALON;
            peg.ID_PROJECTION = projCur.ID_PROJ_ENGLOBANTE;
            projCur.projectionEnglobante = (ProjectionEnglobanteDAO) cerveau
                    .getRegle(peg);
            projCur.projectionEtalon = (ProjectionEtalonDAO) cerveau
                    .getRegle(pet);

            projCur.evolutions_localise = new ArrayList<Evol_localiseDAO>();
            evol.ID_PROJECTION = projCur.ID_PROJECTION;
            ArrayList<INeurone> evols = cerveau.getAllRegles(evol);
            if (evols != null) {
                for (int j = 0; j < evols.size(); j++) {
                    projCur.evolutions_localise
                            .add((Evol_localiseDAO) evols.get(j));
                }
            }

        }
    }

    
    
    /**
     * Lie les neurones scénarios et evolutions de scénarios
     * @return void
     * @throws Exception
     */
    private static void liensScenar_non_loc() throws Exception {
        Evol_de_scenarDAO evolCur = new Evol_de_scenarDAO();
        for (int i = 0; i < tableScenar_non_loc.size(); i++) {
            Scenar_non_locDAO scenarCur = (Scenar_non_locDAO) tableScenar_non_loc
                    .get(i);
            evolCur.ID_SCENARIO = scenarCur.ID_SCENARIO;
            scenarCur.evolutions_de_scenar = new ArrayList<Evol_de_scenarDAO>();
            ArrayList<INeurone> evols = cerveau.getAllRegles(evolCur);
            if (evols != null) {
                for (int j = 0; j < evols.size(); j++) {
                    scenarCur.evolutions_de_scenar
                            .add((Evol_de_scenarDAO) evols.get(j));
                }
            }
        }
    }

    /**
     * Lie les neurones évolutions de scénario et les évolutions non localisées
     * @return void
     * @throws Exception
     */
    private static void liensEvol_de_scenar() throws Exception {
        Evol_non_locDAO evolCur = new Evol_non_locDAO();
        for (int i = 0; i < tableEvol_de_scenar.size(); i++) {
            Evol_de_scenarDAO esCur = (Evol_de_scenarDAO) tableEvol_de_scenar
                    .get(i);
            evolCur.ID_EVOL_NON_LOC = esCur.ID_EVOL_NON_LOC;
            esCur.evol_non_loc = (Evol_non_locDAO) cerveau
                    .getRegle(evolCur);

        }
    }

    /**
     * Lie les neurones ENL et hypothèse
     * @return void
     * @throws Exception
     */
    private static void liensEvol_non_loc() throws Exception {
        User_hypotheseDAO hypoCur = new User_hypotheseDAO();
        for (int i = 0; i < tableEvol_non_loc.size(); i++) {
            Evol_non_locDAO evolCur = (Evol_non_locDAO) tableEvol_non_loc
                    .get(i);
            hypoCur.ID_HYPOTHESE = evolCur.ID_HYPOTHESE;
            evolCur.user_hypothese = (User_hypotheseDAO) cerveau
                    .getRegle(hypoCur);

        }
    }

    /**
     * Lie les neurones EL et leurs Zones respectives
     * @return void
     * @throws Exception
     */
    private static void liensEvol_localise() throws Exception {
        Evol_non_locDAO evolCur = new Evol_non_locDAO();
        DAOZone zoneCur = new DAOZone();
        for (int i = 0; i < tableEvol_localise.size(); i++) {
            Evol_localiseDAO esCur = (Evol_localiseDAO) tableEvol_localise
                    .get(i);
            evolCur.ID_EVOL_NON_LOC = esCur.ID_EVOL_NON_LOC;
            esCur.evol_non_loc = (Evol_non_locDAO) cerveau
                    .getRegle(evolCur);
            zoneCur.ID_ZONE = esCur.ZONE;
            if (esCur.ZONE != null)
                esCur.zone = (DAOZone) cerveau.getRegle(zoneCur);
            zoneCur.ID_ZONE = esCur.ZONE_DESTINATION;
            if (esCur.ZONE_DESTINATION != null)
                esCur.zone_destination = (DAOZone) cerveau
                        .getRegle(zoneCur);
        }
    }

    /**
     * Lie le neurone du Zonage et ces neurones Zones de Zonage
     * @return void
     * @throws Exception
     */
    private static void liensZonage() throws Exception {
        User_omphaleDAO userCur = new User_omphaleDAO();
        Zone_de_zonageDAO zzCur = new Zone_de_zonageDAO();
        for (int i = 0; i < tableZonage.size(); i++) {
            ZonageDAO zonageCur = (ZonageDAO) tableZonage.get(i);
            userCur.ID_USER = zonageCur.ID_USER;
            zonageCur.user_omphale = (User_omphaleDAO) CacheDaoManager.cerveau
                    .getRegle(userCur);
            zzCur.ZONAGE = zonageCur.ID_ZONAGE;
            zonageCur.zones_de_zonage = new ArrayList<Zone_de_zonageDAO>();
            ArrayList<INeurone> zones = cerveau.getAllRegles(zzCur);
            for (int j = 0; j < zones.size(); j++) {
                zonageCur.zones_de_zonage.add((Zone_de_zonageDAO) zones
                        .get(j));
            }
        }
    }

    /**
     * Lie les neurones zones de zonage et leur neurone zone respectif
     * @return void
     * @throws Exception
     */
    private static void liensZone_de_zonage() throws Exception {
        DAOZone zoneCur = new DAOZone();
        for (int i = 0; i < tableZone_de_zonage.size(); i++) {
            Zone_de_zonageDAO zzCur = (Zone_de_zonageDAO) tableZone_de_zonage
                    .get(i);
            zoneCur.ID_ZONE = zzCur.ZONE;
            zzCur.zone = (DAOZone) cerveau.getRegle(zoneCur);
        }
    }


    /**
     * vérifie que l'idep fourni existe bien sur le serveur ldap
     * @return int
     */
    private static int authentification() {

        String mailUtilisateurProjection = null;
        User_omphaleDAO user = projectionATraiter();

        try {
            	mailUtilisateurProjection = IgesaClientWS.rechercherMailFonctionIdep(user.IDEP);
        	
        } catch (Exception t) {
            t.printStackTrace();
            beanRapport.message = "Utilisateur inconnu du LDAP " + user.IDEP;
            beanRapport.code = STATUS_ERROR_CONFIG;
            return STATUS_ERROR_CONFIG;
        }
        if (mailUtilisateurProjection == null) {
            beanRapport.message = "Utilisateur inconnu du LDAP " + user.IDEP;
            beanRapport.code = STATUS_ERROR_CONFIG;
            return STATUS_ERROR_CONFIG;
        }
        user.EMAIL = mailUtilisateurProjection;
        beanRapport.emailDest = mailUtilisateurProjection;
        beanRapport.utilisateur = user.IDEP;
        // on cherche des emails surcharges dans la config

            String dest = OmphaleBatchConfig.mailDestinataire;
            String copie = OmphaleBatchConfig.mailCopie;
            String from = OmphaleBatchConfig.mailFrom;
            String mailIssueIdepProperties = null;
            if (dest != null) {
                if (dest.length() == 6) {
                    // dest est un idep
                	mailIssueIdepProperties = IgesaClientWS.rechercherMailFonctionIdep(dest);

                    if (mailIssueIdepProperties != null) {
                        beanRapport.emailDest = mailIssueIdepProperties;
                    }
                } else {
                    // dest est un email
                    beanRapport.emailDest = dest;
                }
            }
            if (copie != null) {
                if (copie.length() == 6) {
                    // copie est un idep
                	mailIssueIdepProperties = IgesaClientWS.rechercherMailFonctionIdep(copie);

                    if (mailIssueIdepProperties != null) {
                        beanRapport.emailCopie = mailIssueIdepProperties;
                    }
                } else {
                    // copie est un email
                    beanRapport.emailCopie = copie;
                }
            }
            if (from != null) {
                if (from.length() == 6) {
                    // from est un idep
                	mailIssueIdepProperties = IgesaClientWS.rechercherMailFonctionIdep(from);

                    if (mailIssueIdepProperties != null) {
                        beanRapport.emailFrom = mailIssueIdepProperties;
                    }
                } else {
                    // from est un email
                    beanRapport.emailFrom = from;
                }
            }
        return STATUS_SUCCES;
    }
    


    /**
     * exécute la liste des BeanScript qui correspond aux différents scripts d'exécution d'une projection
     * @return void
     * @param pgm
     * @throws OmphaleConfigException
     * @throws OmphaleSqlException
     */
    public static void executeProgrammeScript(ArrayList<BeanScript> pgm)
            throws  OmphaleConfigException, OmphaleSqlException {
        if (testMode) {
            testProgrammeScript(pgm);
            return;
        }
        // ********************************************************
        // ******  initialisation des variables utiles à l'exécution
        	BeanScript s = null;
        	
        	// composant GroupeJavaUtil
        	ScriptSqlLibrary sqlLibrary = null;
        	IConnectionManager cm = null;
        	ScriptSqlDao scriptSqlDao = null;
        	
        	// récupération du premier BeanScript --> s'agit ici du mainScript, c-à-d les paramètres de la projection
        	// récupération dans une map appelée opération des paramètres de ce BeanScript
        	s = pgm.get(0);
        	Map<String, String> operation = new HashMap<String, String>();
        	mergeMap(operation, s.getListeParametres());
        	
        	// récupération du nom du package racine
        	String nomPackageResourceSql = s.getScriptSql();
        // ********************************************************

        	
        	
        	
        // ********************************************************
        // ******  initialisation de la connexion à la BDD utilisation de la librairie java utils
	        try {
	            cm = ContexteApplication.getConnectionManager();
	        } catch (Exception e) {
	            throw new OmphaleConfigException("Problème de connexion manager", e);
	        }
	    // ********************************************************	
	        
	        
    
	    // ********************************************************	    
	    // ******  la lecture des tables à travers les DAO et exécution des scripts SQL    
	        
	        // récupération de la liste des paramètres de la projection à traiter
	        Map<String, String> listeParametres = s.getListeParametres();
	        
	        // récupération d'un objet scriptSQlDao contenant les définition de tous les DAO les DAO
	        try {
	            scriptSqlDao = (ScriptSqlDao) ContexteApplication.getDao(
	                    "scriptSql", OmphaleBatchConfig.nomConnexion);
	        } catch (Exception e) {
	            throw new OmphaleConfigException("Problème de contexte dao", e);
	        }
	         
	        
	        
	        try {
	          	sqlLibrary = new ScriptSqlLibrary(nomPackageResourceSql,
	                    scriptSqlDao);
	        } catch (Exception e) {
	            throw new OmphaleConfigException(
	                    "Problème de gestionnaire de scripts", e);
	        }
	        try {
	            cm.beginTransaction(OmphaleBatchConfig.nomConnexion);
	
	            for (int i = 1; i < pgm.size(); i++) {
	                s = pgm.get(i);

	                if (sqlLibrary.existsUnScript(s.getScriptSql())) {
	                    sqlLibrary.execute(s.getScriptSql(), mergeMap(
	                            listeParametres, s.getListeParametres()));
	                } else
	                    throw new OmphaleSqlException("script absent ou vide :"
	                            + s.getScriptSql());
	            }
	            
	            cm.commitTransaction(OmphaleBatchConfig.nomConnexion);
	        }
	
	        catch (Exception t) {
	            StringBuffer messerr = new StringBuffer();
	            messerr.append("erreur sur la base "
	                    + OmphaleBatchConfig.nomConnexion + "\r\n");
	            messerr.append("erreur sur le package " + nomPackageResourceSql
	                    + "\r\n");
	            for (Map.Entry<String, String> entry : operation.entrySet()) {
	                messerr.append("operation:" + entry.getKey() + ":"
	                        + entry.getValue() + "\r\n");
	            }
	            messerr.append("methode:" + s.getScriptSql() + "\r\n");
	            for (Map.Entry<String, String> entry : s.getListeParametres()
	                    .entrySet()) {
	                messerr.append("methode:" + s.getScriptSql() + ";"
	                        + entry.getKey() + ":" + entry.getValue() + "\r\n");
	            }
	            messerr.append("Erreur message:" + t.getMessage());
	            beanRapport.erreurProjection = messerr.toString();
	            t.printStackTrace();
	            try {
	                cm.rollbackTransaction(OmphaleBatchConfig.nomConnexion);
	            } catch (Exception e) {
	                e.printStackTrace();
	                throw new OmphaleConfigException("Problème posé par un script Sql :"
	                        + t.getMessage(), t);
	            } 
	            throw new OmphaleSqlException("Problème posé par un script Sql :"
	                    + t.getMessage(), t);
	        }
    }
    


    /**
     * Méthode qui renvoie l'union de deux Maps.
     * @param map1 la premiere map
     * @param map2 la deuxième map.
     * @return la première map enrichie du contenu de la deuxième.
     */
    public static Map<String, String> mergeMap(Map<String, String> map1,
            Map<String, String> map2) {
        if (map1 == null)
            return map1;
        if (map2 == null)
            return map1;
        map1.putAll(map2);
        return map1;

    }

    /**
     * Test l'exécution des scripts de la projection
     * @return void
     * @param pgm
     * @throws OmphaleConfigException
     * @throws OmphaleSqlException
     */
    private static void testProgrammeScript(ArrayList<BeanScript> pgm)
            throws OmphaleConfigException, OmphaleSqlException {

        BeanScript s = null;
        // composant GroupeJavaUtil
        
        ScriptSqlLibrary sqlLibrary = null;
        ScriptSqlDao scriptSqlDao = null;
        s = pgm.get(0);
        Map<String, String> operation = new HashMap<String, String>();
        mergeMap(operation, s.getListeParametres());

        String nomPackageResourceSql = s.getScriptSql();
        Map<String, String> listeParametres = s.getListeParametres();
        try {
        	sqlLibrary = new ScriptSqlLibrary(nomPackageResourceSql,
                    scriptSqlDao);
        } catch (Exception e) {
            throw new OmphaleConfigException(
                    "Problème de gestionnaire de scripts", e);
        }
        traceExecution = "";
        for (int i = 1; i < pgm.size(); i++) {

            s = pgm.get(i);
            if (sqlLibrary.existsUnScript(s.getScriptSql())) {
                mergeMap(listeParametres, s.getListeParametres());

                traceExecution += "\r\n" + s;
            } else
                throw new OmphaleSqlException("script absent ou vide :"
                        + s.getScriptSql());

            traceExecution += "\r\n" + s.getScriptSql();
            for (Map.Entry<String, String> entry : listeParametres.entrySet()) {
                traceExecution += "\r\n" + entry.getKey() + ":"
                        + entry.getValue();
            }

        }

    }

     /**
     * Méthode d'affichage des informations de la projection.
     * <BR>
     * fournit dans les BeanParametresResultat
     * <BR>
     * sert pour la première page du pdf
     */
    public static String getFicheIdentite() {
        Projection_lanceeDAO proj = getFirstProjectionLancee();
        StringBuffer texte = new StringBuffer("NOM_PROJECTION:"
                + proj.def_projection.NOM).append(
                "\nLIB_PROJECTION:" + proj.def_projection.LIBELLE).append(
                "\nNOM_ZONAGE:" + proj.def_projection.zonage.NOM).append(
                "\nLIB_ZONAGE:" + proj.def_projection.zonage.LIBELLE).append(
                "\nNOM_SCENARIO:" + proj.def_projection.scenar_non_loc.NOM)
                .append(
                        "\nLIB_SCENARIO:"
                                + proj.def_projection.scenar_non_loc.LIBELLE)
                .append("\nAN_HOR:" + proj.def_projection.ANNEE_HORIZON)
                .append("\nAN_REF:" + proj.def_projection.ANNEE_REFERENCE)
                .append("\nDATA_FTP:" + proj.DONNEES).append(
                        "\nCALAGE:" + proj.def_projection.CALAGE);
        if (proj.def_projection.projectionEtalon != null) {
            texte
                    .append(
                            "\nNOM_ETALON:"
                                    + proj.def_projection.projectionEtalon.NOM)
                    .append(
                            "\nLIB_ETALON:"
                                    + proj.def_projection.projectionEtalon.LIBELLE);
        }
        texte.append("\nUSER:" + proj.def_projection.user_omphale.IDEP);
        return texte.toString();
    }
    
    /**
    * Méthode d'affichage des informations de la projection.
    * <BR>
    * fournit dans les BeanParametresResultat
    * <BR>
    * sert pour le contenu du mail au format html
    */
   public static String getFicheIdentiteHTML() {
       Projection_lanceeDAO proj = getFirstProjectionLancee();
       StringBuffer texte = new StringBuffer();
       texte
       		   .append("<p>")
       		.append("IDENTIFIANT PROJECTION : " + proj.def_projection.user_omphale.IDEP + "_" 
       		   + proj.ID_PROJECTION + "_" + proj.ID_PROJECTION_LANCEE)
       		.append("</p>")
       		.append("<p>")
    		   .append("NOM_PROJECTION: " + proj.def_projection.NOM)
    		 .append("</p>")
    		 .append("<p>")
    		   .append("LIB_PROJECTION: " + proj.def_projection.LIBELLE)
    		 .append("</p>")
    		 .append("<p>")
    		   .append("NOM_ZONAGE: " + proj.def_projection.zonage.NOM)
    		 .append("</p>")
    		 .append("<p>")
    		   .append("LIB_ZONAGE: " + proj.def_projection.zonage.LIBELLE)
    		 .append("</p>")
    		 .append("<p>")
    		   .append("NOM_SCENARIO: " + proj.def_projection.scenar_non_loc.NOM)
    		 .append("</p>")
    		 .append("<p>")
    		   .append("LIB_SCENARIO: "+ proj.def_projection.scenar_non_loc.LIBELLE)
    		 .append("</p>")
    		 .append("<p>")
    		   .append("AN_HOR: " + proj.def_projection.ANNEE_HORIZON)
    		 .append("</p>")
    		 .append("<p>")
    		   .append("AN_REF: " + proj.def_projection.ANNEE_REFERENCE)
    		 .append("</p>")
    		 .append("<p>")
    		   .append("DATA_FTP: " + proj.DONNEES)
    		 .append("</p>")
    		 .append("<p>")
    		   .append("CALAGE: " + proj.def_projection.CALAGE)
    		 .append("</p>");

    		 if (proj.def_projection.projectionEtalon != null) {
    			texte
       		 		.append("<p>")
       		 			.append("NOM_ETALON: "+ proj.def_projection.projectionEtalon.NOM)
       		 		.append("</p>")
       		 		.append("<p>")
       		 			.append("LIB_ETALON: "+ proj.def_projection.projectionEtalon.LIBELLE)
       		 		.append("</p>")
       		 		;
    		 }
    		 texte
    		 	.append("<p>")
	 				.append("USER: " + proj.def_projection.user_omphale.IDEP)
	 				.append("</p>");
       return texte.toString();
   }

    
    /**
     * Exécute la première projection lancée des neurones projections lancées
     * @return void
     * @throws OmphaleMetierException
     * @throws OmphaleConfigException
     */
    public static void executeProjection() throws OmphaleMetierException, OmphaleConfigException {
        executeProjection(getFirstProjectionLancee());
    }

    /**
     * Initialise la liste des BeanScript et exécute cette liste de script d'exécution d'une projection
     * @return void
     * @param proj
     * @throws OmphaleMetierException
     * @throws OmphaleConfigException
     * @throws OmphalePopulationNegativeException
     */
    public static void executeProjection(Projection_lanceeDAO proj) throws OmphaleMetierException, OmphaleConfigException {
        if (proj == null) 
        {
            throw new OmphaleMetierException("pas de projection");
        }
        ArrayList<BeanScript> pgm = proj.def_projection
                .buildProgrammeProjection();
        try {
            if (!testMode) {
            if (proj.def_projection.isEnglobantSansCalage()) {
                throw new OmphaleSqlException("projection englobante sans calage");
            }
            if (proj.def_projection.isCalageEnglobant() && proj.def_projection.projectionEnglobante==null) {
                throw new OmphaleSqlException("pas de projection englobante");
            }
            if (proj.def_projection.projectionEnglobante!=null && !(proj.def_projection.projectionEnglobante.isEnglobante())) {
                throw new OmphaleSqlException("projection faussement englobante");
            }
            if (proj.def_projection.isEnglobante()) {
                throw new OmphaleSqlException("projection figee");
            }
            }
            executeProgrammeScript(pgm);
            
        } catch (OmphaleConfigException e) {
            e.printStackTrace();
            throw new OmphaleConfigException("erreur grave lors execution script",e);
        } catch (OmphaleSqlException e) {
            e.printStackTrace();
            throw new OmphaleMetierException("erreur metier lors execution script",e);      
        } catch (Exception e) {
             e.printStackTrace();
            throw new OmphaleConfigException("erreur grave innatendue lors execution script",e);

		}
       
    }



    /**
     * Méthode qui instancie, charge et renvoie un bean de paramètres utilisés
     * par les modules de production des résultats (données et graphiques).
     * @param batch le bean de la projection en cours.
     * @return le bean des paramètres.
     */
    private static BeanParametresResultat resultatsConfiguration() {
        BeanParametresResultat beanParametresResultat = new BeanParametresResultat();
        StringBuffer nomBase = new StringBuffer();
        Projection_lanceeDAO proj = getFirstProjectionLancee();
        nomBase.append(OmphaleBatchConfig.APPLISHARE_RacineDepotResultatProjection);
        nomBase.append(OmphaleBatchConfig.dirSeparateur);
        nomBase.append(proj.def_projection.user_omphale.IDEP);
        nomBase.append("_");
        nomBase.append(proj.ID_PROJECTION);
        nomBase.append("_");
        nomBase.append(proj.ID_PROJECTION_LANCEE);

        StringBuffer nomDossier = new StringBuffer();
        nomDossier.append(proj.def_projection.user_omphale.IDEP);
        nomDossier.append("_");
        nomDossier.append(proj.ID_PROJECTION);
        nomDossier.append("_");
        nomDossier.append(proj.ID_PROJECTION_LANCEE);

        StringBuffer nomPdf = new StringBuffer();
       

        nomPdf
                .append(OmphaleBatchConfig.prefixe_PDF)
        		.append("_")
        		.append(proj.def_projection.user_omphale.IDEP)
        		.append("_")
        		.append(proj.ID_PROJECTION)
        		.append("_")
        		.append(proj.ID_PROJECTION_LANCEE)
        		.append("_");
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        nomPdf.append(format.format(date));
        nomPdf.append(".pdf");

        beanParametresResultat.setAge100(Integer
                .parseInt(OmphaleBatchConfig.age_last));
        beanParametresResultat.setAgeDebutMere(Integer
                .parseInt(OmphaleBatchConfig.mere_inf));
        beanParametresResultat.setAgeFinMere(Integer
                .parseInt(OmphaleBatchConfig.mere_sup));
        beanParametresResultat.setCalage(proj.def_projection.CALAGE);
        beanParametresResultat
                .setAnneeHorizon(proj.def_projection.ANNEE_HORIZON);
        beanParametresResultat
                .setAnneeReference(proj.def_projection.ANNEE_REFERENCE);
        beanParametresResultat.setNomBaseFichiers(nomBase.toString());
        beanParametresResultat.setNomDossier(nomDossier.toString());
        beanParametresResultat.setNomFichierPdf(nomPdf.toString());
        beanParametresResultat.setNomRacineAppliShare(OmphaleBatchConfig.APPLISHARE_RacineDepotResultatProjection + OmphaleBatchConfig.dirSeparateur);
        beanParametresResultat.setIdLancement(proj.ID_PROJECTION_LANCEE);
        beanParametresResultat.setIdProjection(proj.ID_PROJECTION);
        beanParametresResultat.setIdUser(proj.def_projection.user_omphale.IDEP);
        beanParametresResultat.setIdZonage(proj.def_projection.ID_ZONAGE);
        beanParametresResultat.setPrefixe(beanRapport.prefix);

        beanParametresResultat.setFtpServer(OmphaleBatchConfig.ftpServer);
        beanParametresResultat.setFtpUser(OmphaleBatchConfig.ftpUser);
        beanParametresResultat.setFtpPwd(OmphaleBatchConfig.ftpPass);
        beanParametresResultat.setFtpPort(OmphaleBatchConfig.ftpPort);

        beanParametresResultat.setFicheIdentite(beanRapport.ficheProjection);
        return beanParametresResultat;
    }

  
}
