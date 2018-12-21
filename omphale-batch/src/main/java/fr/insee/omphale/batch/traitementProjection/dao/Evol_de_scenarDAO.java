package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier evol_de_scenar.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;
//import fr.insee.omphale.cps.Cerveau;
import fr.insee.omphale.batch.cps.INeurone;

import fr.insee.omphale.batch.transversal.bean.BeanScript;



public class Evol_de_scenarDAO implements INeuroneNonDAO{
public String ID_SCENARIO;
public String ID_EVOL_NON_LOC;
public String RANG;


public int num_ID_SCENARIO=0;
public int num_ID_EVOL_NON_LOC=1;
public int num_RANG=2;


public Evol_non_locDAO evol_non_loc;

/**
 * Ordre SQL where pour filtrer sur id_scénario de la définition de projection 
 * commande lancée dans le getSqlMemory
 * renvoie des objets evol_de_scenario filtrés par rang
 * @return String
 */
public String getWhere() {
if (CacheDaoManager.testMode) return " order by id_scenario, rang";
return " Where ID_SCENARIO="+CacheDaoManager.getFirstProjectionLancee().def_projection.ID_SCENARIO+ " order by id_scenario, rang";
}


/** 
 * génère les neurones Neurone_evol_de_scenar
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
 * récupérer les objets evol_de_scenar à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    Evol_de_scenarDAO neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   dao.execute("select * from Evol_de_scenar "+getWhere());

   while (dao.nextRow()) { 
     neur=new Evol_de_scenarDAO();
     neur.ID_SCENARIO=dao.getString("ID_SCENARIO");
     neur.ID_EVOL_NON_LOC=dao.getString("ID_EVOL_NON_LOC");
     neur.RANG=dao.getString("RANG");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }

/**
 * récupérer les objets evol_de_scenar à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
   public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/Evol_de_scenar.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    Evol_de_scenarDAO neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new Evol_de_scenarDAO();
     neur.ID_SCENARIO=rec[num_ID_SCENARIO];
     neur.ID_EVOL_NON_LOC=rec[num_ID_EVOL_NON_LOC];
     neur.RANG=rec[num_RANG];
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
public void setFait(Evol_de_scenarDAO neur) {
  ID_SCENARIO=neur.ID_SCENARIO;
  ID_EVOL_NON_LOC=neur.ID_EVOL_NON_LOC;
  RANG=neur.RANG;
}

/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "Evol_de_scenar:"
  +"\n\tID_SCENARIO:"
  +ID_SCENARIO+","
  +"\n\tID_EVOL_NON_LOC:"
  +ID_EVOL_NON_LOC+","
  +"\n\tRANG:"
  +RANG+""
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
    String res;
    for (int i=0;i<padding;i++) pad+=" ";
    if (evol_non_loc != null) res= "\n"+pad+"<Evol_de_scenar"+" NOM='"
    +evol_non_loc.NOM+"' METHODE='"+evol_non_loc.METHODE_EVOL+"'>";
    else res= "\n"+pad+"<Evol_de_scenar>";
      res+="\n"+pad+"     <ID_SCENARIO>"
      +ID_SCENARIO+"</ID_SCENARIO>"
      +"\n"+pad+"     <ID_EVOL_NON_LOC>"
      +ID_EVOL_NON_LOC+"</ID_EVOL_NON_LOC>"
      +"\n"+pad+"     <RANG>"
      +RANG+"</RANG>"
    ;
    if (evol_non_loc !=null) res+="\n"+pad+evol_non_loc.toXmlString(padding+5);
  
    res += "\n"+pad+"</Evol_de_scenar>";
    return res;
    }

public void setCreateSynapseParameters(Object fait) {
 setFait((Evol_de_scenarDAO)fait);
}

public void setDeepFait(Object fait) {
 setFait((Evol_de_scenarDAO)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((Evol_de_scenarDAO)fait);
}


public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <Evol_de_scenarDAO> table=new ArrayList<Evol_de_scenarDAO>();
  for (int i=0;i<neurones.size();i++) {
    Evol_de_scenarDAO projCur=(Evol_de_scenarDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableEvol_de_scenar=table;
}

/** 
 * accès direct sur la séquence qui ne contient qu'un neurone
 * @return String
 */
public String getMapKey() {
  return ID_SCENARIO;
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
    return evol_non_loc.getBeanScript();
}
}

