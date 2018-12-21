package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier evol_non_loc.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
//import fr.insee.omphale.cps.Cerveau;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.batch.transversal.bean.BeanScript;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;


public class Evol_non_locDAO implements INeuroneNonDAO{
public String ID_EVOL_NON_LOC;
public String ID_USER;
public String NOM;
public String TYPE_ENTITE;
public String COMPOSANTE;
public String ID_HYPOTHESE;
public String METHODE_EVOL;
public String AGE_DEB;
public String SEXE_DEB;
public String ANNEE_DEB;
public String AGE_FIN;
public String SEXE_FIN;
public String ANNEE_FIN;
public String TYPE_PARAM1;
public String TYPE_PARAM2;
public String TYPE_PARAM3;
public String TYPE_PARAM4;
public String VAL_PARAM1;
public String VAL_PARAM2;
public String VAL_PARAM3;
public String VAL_PARAM4;
public String STANDARD;
public String DATE_CREATION;
public String COMMENTAIRE;


public int num_ID_EVOL_NON_LOC=0;
public int num_ID_USER=1;
public int num_NOM=2;
public int num_TYPE_ENTITE=3;
public int num_COMPOSANTE=4;
public int num_ID_HYPOTHESE=5;
public int num_METHODE_EVOL=6;
public int num_AGE_DEB=7;
public int num_SEXE_DEB=8;
public int num_ANNEE_DEB=9;
public int num_AGE_FIN=10;
public int num_SEXE_FIN=11;
public int num_ANNEE_FIN=12;
public int num_TYPE_PARAM1=13;
public int num_TYPE_PARAM2=14;
public int num_TYPE_PARAM3=15;
public int num_TYPE_PARAM4=16;
public int num_VAL_PARAM1=17;
public int num_VAL_PARAM2=18;
public int num_VAL_PARAM3=19;
public int num_VAL_PARAM4=20;
public int num_STANDARD=21;
public int num_DATE_CREATION=22;
public int num_COMMENTAIRE=23;

User_hypotheseDAO user_hypothese;

private String where;



/**
 * initialise le where de la commande SQL d'Oracle
 * pour récupérer les bons objets Evol_non_loc
 * triés par id_projection et par rang.
 * @return String
 */
private void setWhere() {
    where="";
    if (!CacheDaoManager.testMode) {
    where=" Where ID_EVOL_NON_LOC in (select id_evol_non_loc from evol_de_scenar where id_scenario="
        +CacheDaoManager.getFirstProjectionLancee().def_projection.ID_SCENARIO+") or "
        +"ID_EVOL_NON_LOC in (select id_evol_non_loc from evol_localise where id_projection="
        +CacheDaoManager.getFirstProjectionLancee().def_projection.ID_PROJECTION+")";
    }
}


/** 
 * génère les neurones NeuroneEvol_non_loc
 * en sqlmode appel getSqlMemory
 * sinon getFileMemory (pour les tests)
 * 
 *@return {@link ArrayList}Object
 */
public ArrayList<Object> getDeepMemory() throws Exception {
  if (!CacheDaoManager.sqlMode) return getFileMemory();
  else return getSqlMemory();
}

/**
 * récupérer les objets evol_non_loc à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    Evol_non_locDAO neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   setWhere();
   dao.execute("select * from Evol_non_loc "+where);
   
   while (dao.nextRow()) { 
     neur=new Evol_non_locDAO();
     neur.ID_EVOL_NON_LOC=dao.getString("ID_EVOL_NON_LOC");
     neur.ID_USER=dao.getString("ID_USER");
     neur.NOM=dao.getString("NOM");
     neur.TYPE_ENTITE=dao.getString("TYPE_ENTITE");
     neur.COMPOSANTE=dao.getString("COMPOSANTE");
     neur.ID_HYPOTHESE=dao.getString("ID_HYPOTHESE");
     neur.METHODE_EVOL=dao.getString("METHODE_EVOL");
     neur.AGE_DEB=dao.getString("AGE_DEB");
     neur.SEXE_DEB=dao.getString("SEXE_DEB");
     neur.ANNEE_DEB=dao.getString("ANNEE_DEB");
     neur.AGE_FIN=dao.getString("AGE_FIN");
     neur.SEXE_FIN=dao.getString("SEXE_FIN");
     neur.ANNEE_FIN=dao.getString("ANNEE_FIN");
     neur.TYPE_PARAM1=dao.getString("TYPE_PARAM1");
     neur.TYPE_PARAM2=dao.getString("TYPE_PARAM2");
     neur.TYPE_PARAM3=dao.getString("TYPE_PARAM3");
     neur.TYPE_PARAM4=dao.getString("TYPE_PARAM4");
     neur.VAL_PARAM1=dao.getString("VAL_PARAM1");
     neur.VAL_PARAM2=dao.getString("VAL_PARAM2");
     neur.VAL_PARAM3=dao.getString("VAL_PARAM3");
     neur.VAL_PARAM4=dao.getString("VAL_PARAM4");
     neur.STANDARD=dao.getString("STANDARD");
     neur.DATE_CREATION=dao.getString("DATE_CREATION");
     neur.COMMENTAIRE=dao.getString("COMMENTAIRE");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }



/**
 * récupérer les objets evol_non_locse à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
   public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/Evol_non_loc.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    Evol_non_locDAO neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new Evol_non_locDAO();
     neur.ID_EVOL_NON_LOC=rec[num_ID_EVOL_NON_LOC];
     neur.ID_USER=rec[num_ID_USER];
     neur.NOM=rec[num_NOM];
     neur.TYPE_ENTITE=rec[num_TYPE_ENTITE];
     neur.COMPOSANTE=rec[num_COMPOSANTE];
     neur.ID_HYPOTHESE=rec[num_ID_HYPOTHESE];
     neur.METHODE_EVOL=rec[num_METHODE_EVOL];
     neur.AGE_DEB=rec[num_AGE_DEB];
     neur.SEXE_DEB=rec[num_SEXE_DEB];
     neur.ANNEE_DEB=rec[num_ANNEE_DEB];
     neur.AGE_FIN=rec[num_AGE_FIN];
     neur.SEXE_FIN=rec[num_SEXE_FIN];
     neur.ANNEE_FIN=rec[num_ANNEE_FIN];
     
     neur.TYPE_PARAM1=rec[num_TYPE_PARAM1];
     neur.TYPE_PARAM2=rec[num_TYPE_PARAM2];
     neur.TYPE_PARAM3=rec[num_TYPE_PARAM3];
     neur.TYPE_PARAM4=rec[num_TYPE_PARAM4];
     neur.VAL_PARAM1=rec[num_VAL_PARAM1];
     neur.VAL_PARAM2=rec[num_VAL_PARAM2];
     neur.VAL_PARAM3=rec[num_VAL_PARAM3];
     neur.VAL_PARAM4=rec[num_VAL_PARAM4];
     neur.STANDARD=rec[num_STANDARD];
     neur.DATE_CREATION=rec[num_DATE_CREATION];
     neur.COMMENTAIRE=rec[num_COMMENTAIRE];
     
     if ("".equals(neur.TYPE_PARAM1)) {
         neur.TYPE_PARAM1=null;
         neur.VAL_PARAM1=null;
     }
     if ("".equals(neur.TYPE_PARAM2)) {
         neur.TYPE_PARAM2=null;
         neur.VAL_PARAM2=null;
     }
     if ("".equals(neur.TYPE_PARAM3)) {
         neur.TYPE_PARAM3=null;
         neur.VAL_PARAM3=null;
     }
     if ("".equals(neur.TYPE_PARAM4)) {
         neur.TYPE_PARAM4=null;
         neur.VAL_PARAM4=null;
     }
     
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
public void setFait(Evol_non_locDAO neur) {
  ID_EVOL_NON_LOC=neur.ID_EVOL_NON_LOC;
  ID_USER=neur.ID_USER;
  NOM=neur.NOM;
  TYPE_ENTITE=neur.TYPE_ENTITE;
  COMPOSANTE=neur.COMPOSANTE;
  ID_HYPOTHESE=neur.ID_HYPOTHESE;
  METHODE_EVOL=neur.METHODE_EVOL;
  AGE_DEB=neur.AGE_DEB;
  SEXE_DEB=neur.SEXE_DEB;
  ANNEE_DEB=neur.ANNEE_DEB;
  AGE_FIN=neur.AGE_FIN;
  SEXE_FIN=neur.SEXE_FIN;
  ANNEE_FIN=neur.ANNEE_FIN;
  TYPE_PARAM1=neur.TYPE_PARAM1;
  TYPE_PARAM2=neur.TYPE_PARAM2;
  TYPE_PARAM3=neur.TYPE_PARAM3;
  TYPE_PARAM4=neur.TYPE_PARAM4;
  VAL_PARAM1=neur.VAL_PARAM1;
  VAL_PARAM2=neur.VAL_PARAM2;
  VAL_PARAM3=neur.VAL_PARAM3;
  VAL_PARAM4=neur.VAL_PARAM4;
  STANDARD=neur.STANDARD;
  DATE_CREATION=neur.DATE_CREATION;
  COMMENTAIRE=neur.COMMENTAIRE;
}



/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "Evol_non_loc:"
  +"\n\tID_EVOL_NON_LOC:"
  +ID_EVOL_NON_LOC+","
  +"\n\tID_USER:"
  +ID_USER+","
  +"\n\tNOM:"
  +NOM+","
  +"\n\tTYPE_ENTITE:"
  +TYPE_ENTITE+","
  +"\n\tCOMPOSANTE:"
  +COMPOSANTE+","
  +"\n\tID_HYPOTHESE:"
  +ID_HYPOTHESE+","
  +"\n\tMETHODE_EVOL:"
  +METHODE_EVOL+","
  +"\n\tAGE_DEB:"
  +AGE_DEB+","
  +"\n\tSEXE_DEB:"
  +SEXE_DEB+","
  +"\n\tANNEE_DEB:"
  +ANNEE_DEB+","
  +"\n\tAGE_FIN:"
  +AGE_FIN+","
  +"\n\tSEXE_FIN:"
  +SEXE_FIN+","
  +"\n\tANNEE_FIN:"
  +ANNEE_FIN+","
  +"\n\tTYPE_PARAM1:"
  +TYPE_PARAM1+","
  +"\n\tTYPE_PARAM2:"
  +TYPE_PARAM2+","
  +"\n\tTYPE_PARAM3:"
  +TYPE_PARAM3+","
  +"\n\tTYPE_PARAM4:"
  +TYPE_PARAM4+","
  +"\n\tVAL_PARAM1:"
  +VAL_PARAM1+","
  +"\n\tVAL_PARAM2:"
  +VAL_PARAM2+","
  +"\n\tVAL_PARAM3:"
  +VAL_PARAM3+","
  +"\n\tVAL_PARAM4:"
  +VAL_PARAM4+","
  +"\n\tSTANDARD:"
  +STANDARD+","
  +"\n\tDATE_CREATION:"
  +DATE_CREATION+","
  +"\n\tCOMMENTAIRE:"
  +COMMENTAIRE+""
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
    
    String res= "\n"+pad+"<Evol_non_loc"+" NOM='"+NOM+"' METHODE='"+METHODE_EVOL+"'>"
      +"\n"+pad+"     <ID_EVOL_NON_LOC>"
      +ID_EVOL_NON_LOC+"</ID_EVOL_NON_LOC>"
      +"\n"+pad+"     <ID_USER>"
      +ID_USER+"</ID_USER>"
      +"\n"+pad+"     <NOM>"
      +NOM+"</NOM>"
      +"\n"+pad+"     <TYPE_ENTITE>"
      +TYPE_ENTITE+"</TYPE_ENTITE>"
      +"\n"+pad+"     <COMPOSANTE>"
      +COMPOSANTE+"</COMPOSANTE>"
      +"\n"+pad+"     <ID_HYPOTHESE>"
      +ID_HYPOTHESE+"</ID_HYPOTHESE>"
      +"\n"+pad+"     <METHODE_EVOL>"
      +METHODE_EVOL+"</METHODE_EVOL>"
      +"\n"+pad+"     <AGE_DEB>"
      +AGE_DEB+"</AGE_DEB>"
      +"\n"+pad+"     <SEXE_DEB>"
      +SEXE_DEB+"</SEXE_DEB>"
      +"\n"+pad+"     <ANNEE_DEB>"
      +ANNEE_DEB+"</ANNEE_DEB>"
      +"\n"+pad+"     <AGE_FIN>"
      +AGE_FIN+"</AGE_FIN>"
      +"\n"+pad+"     <SEXE_FIN>"
      +SEXE_FIN+"</SEXE_FIN>"
      +"\n"+pad+"     <ANNEE_FIN>"
      +ANNEE_FIN+"</ANNEE_FIN>"
      +"\n"+pad+"     <TYPE_PARAM1>"
      +TYPE_PARAM1+"</TYPE_PARAM1>"
      +"\n"+pad+"     <TYPE_PARAM2>"
      +TYPE_PARAM2+"</TYPE_PARAM2>"
      +"\n"+pad+"     <TYPE_PARAM3>"
      +TYPE_PARAM3+"</TYPE_PARAM3>"
      +"\n"+pad+"     <TYPE_PARAM4>"
      +TYPE_PARAM4+"</TYPE_PARAM4>"
      +"\n"+pad+"     <VAL_PARAM1>"
      +VAL_PARAM1+"</VAL_PARAM1>"
      +"\n"+pad+"     <VAL_PARAM2>"
      +VAL_PARAM2+"</VAL_PARAM2>"
      +"\n"+pad+"     <VAL_PARAM3>"
      +VAL_PARAM3+"</VAL_PARAM3>"
      +"\n"+pad+"     <VAL_PARAM4>"
      +VAL_PARAM4+"</VAL_PARAM4>"
      +"\n"+pad+"     <STANDARD>"
      +STANDARD+"</STANDARD>"
      +"\n"+pad+"     <DATE_CREATION>"
      +DATE_CREATION+"</DATE_CREATION>"
      +"\n"+pad+"     <COMMENTAIRE>"
      +COMMENTAIRE+"</COMMENTAIRE>"
    ;
    if (user_hypothese != null) res += "\n"+pad+user_hypothese.toXmlString(padding+5);
    res += "\n"+pad+"</Evol_non_loc>";
    return res;
    }

public void setCreateSynapseParameters(Object fait) {
 setFait((Evol_non_locDAO)fait);
}


public void setDeepFait(Object fait) {
 setFait((Evol_non_locDAO)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((Evol_non_locDAO)fait);
}


public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <Evol_non_locDAO> table=new ArrayList<Evol_non_locDAO>();
  for (int i=0;i<neurones.size();i++) {
    Evol_non_locDAO projCur=(Evol_non_locDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableEvol_non_loc=table;
}

/** 
 * accès direct sur la séquence qui ne contient qu'un neurone
 * @return String
 */
public String getMapKey() {
  return ID_EVOL_NON_LOC;
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
public BeanScript getBeanScript() {
    BeanScript bean = null;
    String script = null;
    
    //Diffirencie selon que c'est une méthode sur les actifs
    if( METHODE_EVOL.toLowerCase().indexOf("actif_taux")!=-1 ) {
    	script=(TYPE_ENTITE+"."+METHODE_EVOL+"_tranche_age.sql").toLowerCase();
    } else {
    	script=(TYPE_ENTITE+"."+METHODE_EVOL+".sql").toLowerCase();
    }
    
    bean=new BeanScript(script);
    Map<String, String> map=bean.getListeParametres();
    map.put("&&idhyp.", ID_HYPOTHESE);
    map.put("&&age_deb.", AGE_DEB);
    map.put("&&age_fin.", AGE_FIN);
    map.put("&&annee_deb.", ANNEE_DEB);
    map.put("&&annee_fin.", ANNEE_FIN);
    map.put("&&sexe_deb.", SEXE_DEB);
    map.put("&&sexe_fin.", SEXE_FIN);
    map.put("&&zone_locale.", "%");
    map.put("&&zone_destination.", "%");
    if (TYPE_PARAM1 != null) map.put("&&" + TYPE_PARAM1 + ".", VAL_PARAM1);
    if (TYPE_PARAM2 != null) map.put("&&" + TYPE_PARAM2 + ".", VAL_PARAM2);
    if (TYPE_PARAM3 != null) map.put("&&" + TYPE_PARAM3 + ".", VAL_PARAM3);
    if (TYPE_PARAM4 != null) map.put("&&" + TYPE_PARAM4 + ".", VAL_PARAM4);
    return bean;
}

}

