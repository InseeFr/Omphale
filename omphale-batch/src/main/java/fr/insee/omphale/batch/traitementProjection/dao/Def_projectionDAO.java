package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier def_projection.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;


//import fr.insee.omphale.cps.Cerveau;
import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.batch.transversal.bean.BeanScript;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;



public class Def_projectionDAO implements INeuroneNonDAO{

	
public String ID_PROJECTION;
public String ID_SCENARIO;
public String ID_ZONAGE;
public String ANNEE_REFERENCE;
public String ANNEE_HORIZON;
public String ID_USER;
public String NOM;
public String STANDARD;
public String PYRAMIDE_GENERATION;
public String VALIDATION;
public String CALAGE;
public String ENGLOBANTE;
public String ACTIFS;
public String MENAGES;
public String ELEVES;
public String ID_PROJ_ETALON;
public String ID_PROJ_ENGLOBANTE;
public String DATE_CREATION;
public String LIBELLE;


public int num_ID_PROJECTION=0;
public int num_ID_SCENARIO=1;
public int num_ID_ZONAGE=2;
public int num_ANNEE_REFERENCE=3;
public int num_ANNEE_HORIZON=4;
public int num_ID_USER=5;
public int num_NOM=6;
public int num_STANDARD=7;
public int num_PYRAMIDE_GENERATION=8;
public int num_VALIDATION=9;
public int num_CALAGE=10;
public int num_ENGLOBANTE=11;
public int num_ACTIFS=12;
public int num_MENAGES=13;
public int num_ELEVES=14;
public int num_ID_PROJ_ETALON=15;
public int num_ID_PROJ_ENGLOBANTE=16;
public int num_DATE_CREATION=17;
public int num_LIBELLE=18;

public ZonageDAO zonage;
public Scenar_non_locDAO scenar_non_loc;
public User_omphaleDAO user_omphale;
public ProjectionEtalonDAO projectionEtalon;
public ProjectionEnglobanteDAO projectionEnglobante;
public ArrayList<Evol_localiseDAO> evolutions_localise;


/**
 * Racine à partir du système de fichier des scripts pour la projection 
 * <BR>
 * l'objet BeanScript "mainScript" fournit les paramètres généraux dans sa Map
 * <BR>
 * et la racine des scripts sql par son script
 */
private BeanScript mainScript=new BeanScript(OmphaleBatchConfig.racine_script_projection);

private ArrayList<BeanScript> initialisationProjection;
private ArrayList<BeanScript> pasQuinquenalProjection;
private ArrayList<BeanScript> evolutionsQuotientsProjection;
private Map <String, String> parametresGeneraux;

private int pas_de_projection= OmphaleBatchConfig.pas_de_projection;



/**
 * construit un "programme" d'enchaînement de scripts à exécuter sous forme d'un ArrayList de BeanScript
 * <BR>
 * Le premier BeanScript de la liste est l'objet "mainScript"
 * @return ArrayList&ltBeanScript&gt
 */
public ArrayList<BeanScript> buildProgrammeProjection() {
    ArrayList<BeanScript> programme=new ArrayList<BeanScript>();
    setParametresGeneraux();
    String contenuParametresGeneraux = null;
    
    
    for (String parametreGeneral : parametresGeneraux.values()) {
		contenuParametresGeneraux = contenuParametresGeneraux + "" + parametreGeneral;
	}

    setScriptsInitialisation();
    setScriptsEvolution();
    if( pas_de_projection == 5){
        setScriptsPasQuinquenaux();
    } else {
        setScriptsPasAnnuels();
    }
   
    programme.addAll(initialisationProjection);
    programme.addAll(evolutionsQuotientsProjection);
    programme.addAll(pasQuinquenalProjection);
    

    return programme;
}

/**
 * Initialise les paramètres généraux liés à la définition de projection en cours
 * <BR>
 * et les stocke dans l'objet "mainScript"
 * <BR>
 * S'appuie sur le CacheDaoManger et OmphaleBatchConfig
 * @return void
 */
private void setParametresGeneraux() {
    parametresGeneraux=mainScript.getListeParametres();
    parametresGeneraux.put("&&idep.", user_omphale.IDEP);
    parametresGeneraux.put("&&prefix.",CacheDaoManager.beanRapport.prefix);
    parametresGeneraux.put("&&zonage_id.", ID_ZONAGE);
    parametresGeneraux.put("&&def_projection_id.", ID_PROJECTION);
    parametresGeneraux.put("&&annee_ref.", ANNEE_REFERENCE);
    parametresGeneraux.put("&&annee_hor.", ANNEE_HORIZON);
    parametresGeneraux.put("&&calage.", CALAGE);
    parametresGeneraux.put("&&generation.", PYRAMIDE_GENERATION);
    if (!CacheDaoManager.testMode) parametresGeneraux.put("&&data_ftp.", CacheDaoManager.getFirstProjectionLancee().DONNEES);
    else parametresGeneraux.put("&&data_ftp.", "1");
    parametresGeneraux.put("&&age_old.", OmphaleBatchConfig.age_old);
    parametresGeneraux.put("&&age_last.", OmphaleBatchConfig.age_last);
    parametresGeneraux.put("&&mere_inf.", OmphaleBatchConfig.mere_inf);
    parametresGeneraux.put("&&mere_sup.", OmphaleBatchConfig.mere_sup);
    parametresGeneraux.put("&&decal_annee.", pas_de_projection +"");
    parametresGeneraux.put("&&part_gars.", OmphaleBatchConfig.part_gars);
    parametresGeneraux.put("&&def_projection_etalon.", ID_PROJ_ETALON);
    parametresGeneraux.put("&&def_projection_englobante.", ID_PROJ_ENGLOBANTE);
    parametresGeneraux.put("&&zonage_etalon.", OmphaleBatchConfig.idZonageEtalon);
    parametresGeneraux.put("&&zonage_gros.", OmphaleBatchConfig.nbrZoneGrosZonage);
    parametresGeneraux.put("&&zonage_nb_zone.", CacheDaoManager.getFirstProjectionLancee().NBR_ZONE);
    if (Integer.parseInt(parametresGeneraux.get("&&zonage_nb_zone.")) >
    Integer.parseInt(parametresGeneraux.get("&&zonage_gros."))) {
        parametresGeneraux.put("&&zonage_nb_flux.", OmphaleBatchConfig.nbrFluxGrosZonage);
    } else {
        parametresGeneraux.put("&&zonage_nb_flux.", OmphaleBatchConfig.nbrFluxPetitZonage); 
    }
    if (projectionEnglobante != null) {
        parametresGeneraux.put("&&zonage_englobant.", projectionEnglobante.ID_ZONAGE);
    }
    if (isPotentielEnglobante()) {
        parametresGeneraux.put("&&potentiel_englobant.", "1");
    } else {
        parametresGeneraux.put("&&potentiel_englobant.", "0");
    }
}

/**
 * Initialise le début du programme qui permettrait d'exécuter les scripts d'initialisation
 * <BR>
 * <ol type="1"> 
 *		<li>script d'agrégation des données selon si étalon ou utilisateur</li>
 *		<li>script d'initialisation des quotients initiaux</li>
 *		<li>script des survivants pour les départements non impactés (seulement utilisateur)</li>
 *		<li>script pour calculer le nombre de pas quinquennaux entre le début et la fin de la période définie</li>
 *		<li>script initialisant les quotients pour la projection</li>
 * </ol>
 * <BR>
 * @return void
 */
private void setScriptsInitialisation() {
    initialisationProjection=new ArrayList<BeanScript>();
    initialisationProjection.add(mainScript);
    if (isCalageEnglobant()) {
        initialisationProjection.add(new BeanScript("user.controle_zonage_englobant.sql"));
    }
    if (isEtalon()) initialisationProjection.add(new BeanScript("etalon.agregation_etalon.sql"));
    else initialisationProjection.add(new BeanScript("user.agregation_user.sql"));
   
    isMethodeActif(initialisationProjection, "general.quotient_initiaux_tranche_age.sql", "general.quotient_initiaux.sql");
    
    if (!isEtalon()) initialisationProjection.add(new BeanScript("user.dept_non_impact_surv_user.sql"));
    initialisationProjection.add(new BeanScript("general.calcul_nb_pas.sql"));
    
    isMethodeActif(initialisationProjection, "general.projection_quotients_init_tranche_age.sql", "general.projection_quotients_init.sql");
     
}

/**
 * Initialise la partie du programme qui enchaîne les BeanScripts liés aux différents 
 * <BR>
 * scripts d'évolutuion des quotients à appliquer dans l'ordre.
 *  <BR>
 * D'abord pour les évolutions non localisées et ensuite pour les évolutions localisées
 * <BR>
 * @return void
 */
private void setScriptsEvolution() {
    evolutionsQuotientsProjection=new ArrayList<BeanScript>();
    for (int i=0;i<this.scenar_non_loc.evolutions_de_scenar.size();i++) {
        evolutionsQuotientsProjection.add(this.scenar_non_loc.evolutions_de_scenar.get(i).getBeanScript());
    }
    for (int i=0;i<this.evolutions_localise.size();i++) {
        evolutionsQuotientsProjection.add(this.evolutions_localise.get(i).getBeanScript());
    }
}

/**
 * construit la partie du programme qui enchaîne les scripts liés à un pas de projection quinquennal
 * <BR>
 * ces scripts sont dupliqués autant de fois qu'il y a de pas de projection à exécuter pour atteindre l'année horizon.
 * <BR>
 * et se terminent par les scripts liés au calage
 * 
 * Un jeu de script d'un pas quinquennal comporte des scripts liés à l'annualisation (5 pas annuel pour 1 pas quinquennal)
 * 

 * <BR>
 * @return void
 */
private void setScriptsPasAnnuels() {
	
    BeanScript debutPremierPasQuinquenal;
    BeanScript debutUnPasQuinquenal;
    if (isEtalon())  debutPremierPasQuinquenal=new BeanScript("etalon.projection_premier_pas5_etalon.sql");
    else debutPremierPasQuinquenal=new BeanScript("user.projection_premier_pas5_user.sql");
    if (isEtalon())  debutUnPasQuinquenal=new BeanScript("etalon.projection_un_pas5_etalon.sql");
    else debutUnPasQuinquenal=new BeanScript("user.projection_un_pas5_user.sql");
    
    
    pasQuinquenalProjection=new ArrayList<BeanScript>();
    pasQuinquenalProjection.add(new BeanScript("qd.calcul_quotients_survie.sql"));
    
    ArrayList<BeanScript>work5=new ArrayList<BeanScript>();
    work5.add(debutPremierPasQuinquenal);
    work5.add(new BeanScript("general.projection_un_pas5_fin.sql"));
    
    for (int i=0;i<work5.size();i++) pasQuinquenalProjection.add(work5.get(i));
    work5.set(0, debutUnPasQuinquenal);
    for (int j=0;j<getNombrePas()-1;j++) {
        for (int i=0;i<work5.size();i++) pasQuinquenalProjection.add(work5.get(i));
    }
    pasQuinquenalProjection.add(new BeanScript("resultats.csv_avant_calage.sql"));
    
    if ("1".equals(CALAGE)) {
        if (isEtalon()) {
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_france.sql"));
            
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_corps.sql"));
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_corps.sql"));
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_corps.sql"));
            
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_fin.sql"));
        } else {
            if (projectionEnglobante != null)
                pasQuinquenalProjection.add(new BeanScript("user.calage_englobe_prepare.sql"));
            else
                pasQuinquenalProjection.add(new BeanScript("user.calage_user_prepare.sql"));
            
            pasQuinquenalProjection.add(new BeanScript("user.calage_user_corps.sql"));
            pasQuinquenalProjection.add(new BeanScript("user.calage_user_corps.sql"));
            pasQuinquenalProjection.add(new BeanScript("user.calage_user_corps.sql"));
            
            pasQuinquenalProjection.add(new BeanScript("user.calage_user_fin.sql"));
        }
    }
    pasQuinquenalProjection = isMethodeActif(pasQuinquenalProjection, "general.actif_taux_modifies.sql");
    pasQuinquenalProjection=isMethodeActif(pasQuinquenalProjection, "resultats.csv_apres_calage_qact_tranche_age.sql","resultats.csv_apres_calage_qact.sql");
    if (isEtalon()) {
    	pasQuinquenalProjection=isMethodeActif(pasQuinquenalProjection, "etalon.projection_etalon_fin_tranche_age.sql", "etalon.projection_etalon_fin.sql");
    } else pasQuinquenalProjection=isMethodeActif(pasQuinquenalProjection, "user.projection_user_fin_tranche_age.sql", "user.projection_user_fin.sql");
    
}

private void setScriptsPasQuinquenaux() {
    BeanScript debutPremierPasQuinquenal;
    BeanScript debutUnPasQuinquenal;
    BeanScript annualisationUnPasAnnuel;
    if (isEtalon())  debutPremierPasQuinquenal=new BeanScript("etalon.projection_premier_pas5_etalon.sql");
    else debutPremierPasQuinquenal=new BeanScript("user.projection_premier_pas5_user.sql");
    if (isEtalon())  debutUnPasQuinquenal=new BeanScript("etalon.projection_un_pas5_etalon.sql");
    else debutUnPasQuinquenal=new BeanScript("user.projection_un_pas5_user.sql");
    
    annualisationUnPasAnnuel=new BeanScript("general.annualisation_un_pas1.sql");
    
    pasQuinquenalProjection=new ArrayList<BeanScript>();
    pasQuinquenalProjection.add(new BeanScript("qd.calcul_quotients_survie.sql"));
    
    ArrayList<BeanScript>work5=new ArrayList<BeanScript>();
    work5.add(debutPremierPasQuinquenal);
    work5.add(new BeanScript("general.quotient_mig_net_un_pas5.sql"));
    work5.add(new BeanScript("general.annualisation_un_pas5.sql"));
    for (int i=0;i<5;i++) work5.add(annualisationUnPasAnnuel);
    work5.add(new BeanScript("general.annualisation_un_pas1_fin.sql"));
    work5.add(new BeanScript("general.projection_un_pas5_fin.sql"));
    
    for (int i=0;i<work5.size();i++) pasQuinquenalProjection.add(work5.get(i));
    work5.set(0, debutUnPasQuinquenal);
    for (int j=0;j<getNombrePas()-1;j++) {
        for (int i=0;i<work5.size();i++) pasQuinquenalProjection.add(work5.get(i));
    }
    pasQuinquenalProjection.add(new BeanScript("resultats.csv_avant_calage.sql"));
    pasQuinquenalProjection=isMethodeActif(pasQuinquenalProjection, "resultats.csv_avant_calage_tranche_age.sql","resultats.csv_avant_calage.sql");
    if ("1".equals(CALAGE)) {
        if (isEtalon()) {
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_france.sql"));
            
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_corps.sql"));
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_corps.sql"));
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_corps.sql"));
            
            pasQuinquenalProjection.add(new BeanScript("etalon.calage_etalon_fin.sql"));
        } else {
            if (projectionEnglobante != null)
                pasQuinquenalProjection.add(new BeanScript("user.calage_englobe_prepare.sql"));
            else
                pasQuinquenalProjection.add(new BeanScript("user.calage_user_prepare.sql"));
            
            pasQuinquenalProjection.add(new BeanScript("user.calage_user_corps.sql"));
            pasQuinquenalProjection.add(new BeanScript("user.calage_user_corps.sql"));
            pasQuinquenalProjection.add(new BeanScript("user.calage_user_corps.sql"));
            
            pasQuinquenalProjection.add(new BeanScript("user.calage_user_fin.sql"));
        }
    }
    if (isEtalon()) {
    	pasQuinquenalProjection=isMethodeActif(pasQuinquenalProjection, "etalon.projection_etalon_fin_tranche_age.sql", "etalon.projection_etalon_fin.sql");
    } else pasQuinquenalProjection=isMethodeActif(pasQuinquenalProjection, "user.projection_user_fin_tranche_age.sql", "user.projection_user_fin.sql");
    
}

/**
 * Methode qui ajoute une instruction sql selon si on a une méthode pour les actifs ou non
 * @param script liste des beanscript
 * @param beanscriptMethodeActif nom du script pour la methode sur les actifs
 * @param beanscript nom du script si aucune méthode sur les actifs
 * @return script la liste des beanscript auquelle on a ajouté le bon beanscript
 */
private ArrayList<BeanScript> isMethodeActif(ArrayList<BeanScript> script, String beanscriptMethodeActif, String beanscript) {
	
	boolean isMethodeActif = false;
	for (int i = 0; i < this.scenar_non_loc.evolutions_de_scenar.size(); i++) {
		if( (this.scenar_non_loc.evolutions_de_scenar.get(i).evol_non_loc.METHODE_EVOL.toLowerCase().indexOf("actif_taux"))!=-1 ) {
			script.add(new BeanScript(beanscriptMethodeActif));
			isMethodeActif = true;	
			break;
		}
	}
	if(!isMethodeActif) script.add(new BeanScript(beanscript));
	return script;
}

/**
 * Methode qui ajoute une instruction sql selon que si on a une méthode pour les actifs
 * @param script liste des beanscript
 * @param beanscriptMethodeActif nom du script pour la methode sur les actifs
 * @return script la liste des beanscript auquelle on a ajouté le bon beanscript
 */
private ArrayList<BeanScript> isMethodeActif(ArrayList<BeanScript> script, String beanscriptMethodeActif) {

	for (int i = 0; i < this.scenar_non_loc.evolutions_de_scenar.size(); i++) {
		if( (this.scenar_non_loc.evolutions_de_scenar.get(i).evol_non_loc.METHODE_EVOL.toLowerCase().indexOf("actif_taux"))!=-1 ) {
			script.add(new BeanScript(beanscriptMethodeActif));
			break;
		}
	}
	return script;
}


public boolean isEtalon() {
    return (OmphaleBatchConfig.idZonageEtalon.equals(ID_ZONAGE));
}
public boolean isStandard() {
  return ("1".equals(STANDARD));
}

public boolean isEnglobante() {
   return (OmphaleBatchConfig.indicateurEnglobanteFigee.equals(ENGLOBANTE));
}

public boolean isPotentielEnglobante() {
    return (OmphaleBatchConfig.indicateurEnglobantePotentiel.equals(ENGLOBANTE));
 }

public boolean isCalage() {
    return ("1".equals(CALAGE));
 }

public boolean isCalageEnglobant() {
    return ("1".equals(CALAGE) && ID_PROJ_ENGLOBANTE != null);
 }

public boolean isEnglobantSansCalage() {
    return (!("1".equals(CALAGE)) && ID_PROJ_ENGLOBANTE != null);
 }

public Integer getAnneeHorizon() {
    return Integer.parseInt(ANNEE_HORIZON);
}

public Integer getAnneeReference() {
    return Integer.parseInt(ANNEE_REFERENCE);
}
 
public Double getNombrePas() {
    Double nbPas = (getAnneeHorizon()-getAnneeReference())*1.0;
    return Math.ceil(nbPas/pas_de_projection);
}

public String getWhere() {
    if (CacheDaoManager.testMode)  return " order by date_creation";
return " Where ID_PROJECTION="+CacheDaoManager.getFirstProjectionLancee().ID_PROJECTION;
}


public ArrayList<Object> getDeepMemory() throws Exception {
  if (!CacheDaoManager.sqlMode) return getFileMemory();
  else return getSqlMemory();
}


/**
 * récupération et initialisation des objets NeuroneDef_projection
 * <BR>
 * S'appuie sur la connexion "simpleSelectDao" fourni par le CacheDaoManager
 * <BR>
 * uniquement utilisé par l'objet Cerveau du CPS
 * @return {@link ArrayList} de neurone
 * @throws Exception ArrayList<Object>
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    Def_projectionDAO neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   dao.execute("select * from Def_projection "+getWhere());
   while (dao.nextRow()) { 
     neur=new Def_projectionDAO();
     neur.ID_PROJECTION=dao.getString("ID_PROJECTION");
     neur.ID_SCENARIO=dao.getString("ID_SCENARIO");
     neur.ID_ZONAGE=dao.getString("ID_ZONAGE");
     neur.ANNEE_REFERENCE=dao.getString("ANNEE_REFERENCE");
     neur.ANNEE_HORIZON=dao.getString("ANNEE_HORIZON");
     neur.ID_USER=dao.getString("ID_USER");
     neur.NOM=dao.getString("NOM");
     neur.STANDARD=dao.getString("STANDARD");
     neur.PYRAMIDE_GENERATION=dao.getString("PYRAMIDE_GENERATION");
     neur.VALIDATION=dao.getString("VALIDATION");
     neur.CALAGE=dao.getString("CALAGE");
     neur.ENGLOBANTE=dao.getString("ENGLOBANTE");
     neur.ACTIFS=dao.getString("ACTIFS");
     neur.MENAGES=dao.getString("MENAGES");
     neur.ELEVES=dao.getString("ELEVES");
     neur.ID_PROJ_ETALON=dao.getString("ID_PROJ_ETALON");
     neur.ID_PROJ_ENGLOBANTE=dao.getString("ID_PROJ_ENGLOBANTE");
     neur.DATE_CREATION=dao.getString("DATE_CREATION");
     neur.LIBELLE=dao.getString("LIBELLE");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }


/**
 * récupération et initialisation des objets NeuroneDef_projection
 * <BR>
 * S'appuie un fichier plat "src/test/resources/Def_projection.txt"
 * <BR>
 * Uniquement pour les tests
 * <BR>
 * uniquement utilisé par l'objet Cerveau du CPS
 * @return {@link ArrayList}
 * @throws Exception ArrayList<Object>
 */
public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/Def_projection.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    Def_projectionDAO neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new Def_projectionDAO();
     neur.ID_PROJECTION=rec[num_ID_PROJECTION];
     neur.ID_SCENARIO=rec[num_ID_SCENARIO];
     neur.ID_ZONAGE=rec[num_ID_ZONAGE];
     neur.ANNEE_REFERENCE=rec[num_ANNEE_REFERENCE];
     neur.ANNEE_HORIZON=rec[num_ANNEE_HORIZON];
     neur.ID_USER=rec[num_ID_USER];
     neur.NOM=rec[num_NOM];
     neur.STANDARD=rec[num_STANDARD];
     neur.PYRAMIDE_GENERATION=rec[num_PYRAMIDE_GENERATION];
     neur.VALIDATION=rec[num_VALIDATION];
     neur.CALAGE=rec[num_CALAGE];
     neur.ENGLOBANTE=rec[num_ENGLOBANTE];
     neur.ACTIFS=rec[num_ACTIFS];
     neur.MENAGES=rec[num_MENAGES];
     neur.ELEVES=rec[num_ELEVES];
     neur.ID_PROJ_ETALON=rec[num_ID_PROJ_ETALON];
     neur.ID_PROJ_ENGLOBANTE=rec[num_ID_PROJ_ENGLOBANTE];
     neur.DATE_CREATION=rec[num_DATE_CREATION];
     neur.LIBELLE=rec[num_LIBELLE];
     lines.add(neur);
     }
     ligne=true;
     chaine = buf.readLine();
    }
    in.close();
    return lines;
   }



/**
 * Méthode utilisée par l'objet Cerveau du CPS
 * @param neur 
 * @return void
 */
public void setFait(Def_projectionDAO neur) {
  ID_PROJECTION=neur.ID_PROJECTION;
  ID_SCENARIO=neur.ID_SCENARIO;
  ID_ZONAGE=neur.ID_ZONAGE;
  ANNEE_REFERENCE=neur.ANNEE_REFERENCE;
  ANNEE_HORIZON=neur.ANNEE_HORIZON;
  ID_USER=neur.ID_USER;
  NOM=neur.NOM;
  STANDARD=neur.STANDARD;
  PYRAMIDE_GENERATION=neur.PYRAMIDE_GENERATION;
  VALIDATION=neur.VALIDATION;
  CALAGE=neur.CALAGE;
  ENGLOBANTE=neur.ENGLOBANTE;
  ACTIFS=neur.ACTIFS;
  MENAGES=neur.MENAGES;
  ELEVES=neur.ELEVES;
  ID_PROJ_ETALON=neur.ID_PROJ_ETALON;
  ID_PROJ_ENGLOBANTE=neur.ID_PROJ_ENGLOBANTE;
  DATE_CREATION=neur.DATE_CREATION;
  LIBELLE=neur.LIBELLE;
}


/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
      return getItem()+":"
  +"\n\tID_PROJECTION:"
  +ID_PROJECTION+","
  +"\n\tID_SCENARIO:"
  +ID_SCENARIO+","
  +"\n\tID_ZONAGE:"
  +ID_ZONAGE+","
  +"\n\tANNEE_REFERENCE:"
  +ANNEE_REFERENCE+","
  +"\n\tANNEE_HORIZON:"
  +ANNEE_HORIZON+","
  +"\n\tID_USER:"
  +ID_USER+","
  +"\n\tNOM:"
  +NOM+","
  +"\n\tSTANDARD:"
  +STANDARD+","
  +"\n\tPYRAMIDE_GENERATION:"
  +PYRAMIDE_GENERATION+","
  +"\n\tVALIDATION:"
  +VALIDATION+","
  +"\n\tCALAGE:"
  +CALAGE+","
  +"\n\tENGLOBANTE:"
  +ENGLOBANTE+","
  +"\n\tACTIFS:"
  +ACTIFS+","
  +"\n\tMENAGES:"
  +MENAGES+","
  +"\n\tELEVES:"
  +ELEVES+","
  +"\n\tID_PROJ_ETALON:"
  +ID_PROJ_ETALON+","
  +"\n\tID_PROJ_ENGLOBANTE:"
  +ID_PROJ_ENGLOBANTE+","
  +"\n\tDATE_CREATION:"
  +DATE_CREATION+","
  +"\n\tLIBELLE:"
  +LIBELLE+""
;
}

/**
 * variante de la méthode "toString()" qui structure le string avec des balises XML
 * <BR>
 * et fonctionne récursivement par appel de la méthode sur les objets imbriqués.
 * @param padding
 * @return String
 */
public String toXmlString(int padding) {
    String pad="";
    for (int i=0;i<padding;i++) pad+=" ";
    String typeProj="Projection utilisateur";
    if (isEtalon() && isStandard()) typeProj="Projection etalon";
    else if (isEtalon() ) typeProj="Projection potentiellement etalon";
    else if (isEnglobante() && isStandard()) typeProj="Projection englobante";
    else if (isEnglobante() ) typeProj="Projection potentiellement englobante";
       
    String res= "\n"+pad+"<"+getItem()+" TYPE='"+typeProj+"' USER='"+ID_USER+"' NOM='"+NOM+"' LIB='"+((LIBELLE == null) ? "" : LIBELLE.replace("'", " "))+"'>"
      +"\n"+pad+"     <TYPE_PROJECTION>"
      +typeProj+"</TYPE_PROJECTION>"
      +"\n"+pad+"     <ID_PROJECTION>"
      +ID_PROJECTION+"</ID_PROJECTION>"
      +"\n"+pad+"     <ID_SCENARIO>"
      +ID_SCENARIO+"</ID_SCENARIO>"
      +"\n"+pad+"     <ID_ZONAGE>"
      +ID_ZONAGE+"</ID_ZONAGE>"
      +"\n"+pad+"     <ANNEE_REFERENCE>"
      +ANNEE_REFERENCE+"</ANNEE_REFERENCE>"
      +"\n"+pad+"     <ANNEE_HORIZON>"
      +ANNEE_HORIZON+"</ANNEE_HORIZON>"
      +"\n"+pad+"     <ID_USER>"
      +ID_USER+"</ID_USER>"
      +"\n"+pad+"     <NOM>"
      +NOM+"</NOM>"
      +"\n"+pad+"     <STANDARD>"
      +STANDARD+"</STANDARD>"
      +"\n"+pad+"     <PYRAMIDE_GENERATION>"
      +PYRAMIDE_GENERATION+"</PYRAMIDE_GENERATION>"
      +"\n"+pad+"     <VALIDATION>"
      +VALIDATION+"</VALIDATION>"
      +"\n"+pad+"     <CALAGE>"
      +CALAGE+"</CALAGE>"
      +"\n"+pad+"     <ENGLOBANTE>"
      +ENGLOBANTE+"</ENGLOBANTE>"
      +"\n"+pad+"     <ACTIFS>"
      +ACTIFS+"</ACTIFS>"
      +"\n"+pad+"     <MENAGES>"
      +MENAGES+"</MENAGES>"
      +"\n"+pad+"     <ELEVES>"
      +ELEVES+"</ELEVES>"
      +"\n"+pad+"     <ID_PROJ_ETALON>"
      +ID_PROJ_ETALON+"</ID_PROJ_ETALON>"
      +"\n"+pad+"     <ID_PROJ_ENGLOBANTE>"
      +ID_PROJ_ENGLOBANTE+"</ID_PROJ_ENGLOBANTE>"
      +"\n"+pad+"     <DATE_CREATION>"
      +DATE_CREATION+"</DATE_CREATION>"
      +"\n"+pad+"     <LIBELLE>"
      +LIBELLE+"</LIBELLE>"
    ;
    if (user_omphale !=null) res+="\n"+pad+user_omphale.toXmlString(padding+5);
    if (zonage !=null) res+="\n"+pad+zonage.toXmlString(padding+5);
    if (scenar_non_loc !=null) res+="\n"+pad+scenar_non_loc.toXmlString(padding+5);
    if (projectionEtalon !=null) res+="\n"+pad+projectionEtalon.toXmlString(padding+5);
    if (projectionEnglobante !=null) res+="\n"+pad+projectionEnglobante.toXmlString(padding+5);
    if (evolutions_localise !=null) if (evolutions_localise.size()!=0){
        res+="\n"+pad+"     <evolutions_localisees>";
        for (int i=0;i<evolutions_localise.size();i++) {
            res+="\n"+pad+evolutions_localise.get(i).toXmlString(padding+10);
            
        }
        res+="\n"+pad+"     </evolutions_localisees>";
    }
    res += "\n"+pad+"</"+getItem()+">";
    return res;
    }


public void setCreateSynapseParameters(Object fait) {
 setFait((Def_projectionDAO)fait);
}


public void setDeepFait(Object fait) {
 setFait((Def_projectionDAO)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((Def_projectionDAO)fait);
}


/**
 * appelé à chaque fois qu'on crée la synapse.
 * utile pour renseigner des tables sans passer par le Cerveau
 * @return void
 */
public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <Def_projectionDAO> table=new ArrayList<Def_projectionDAO>();
  for (int i=0;i<neurones.size();i++) {
    Def_projectionDAO projCur=(Def_projectionDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableDef_projection=table;
}


/** 
 * accès direct sur la séquence qui ne contient qu'un neurone
 * @return String
 */
public String getMapKey() {
  return ID_PROJECTION;
}


/**
 * le neurone retourne systèmatiquement True
 * @return boolean
 */
public boolean parseNeurone(INeurone modele) throws Exception {
  return true;
}
/**
 * renvoie le type de de neurone projection
 * <BR>
 * utilisé dans la méthode toString().
 * @return String
 */
protected String getItem() {
    return "Def_projection";
}

}


