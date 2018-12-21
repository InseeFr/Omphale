package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier evol_localise.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;
//import fr.insee.omphale.cps.Cerveau;
import fr.insee.omphale.batch.cps.INeurone;

import fr.insee.omphale.batch.transversal.bean.BeanScript;



public class Evol_localiseDAO implements INeuroneNonDAO{
public String ID_EVOL_LOCALISE;
public String ID_PROJECTION;
public String ID_EVOL_NON_LOC;
public String ZONAGE;
public String COMPOSANTE;
public String ZONE;
public String ZONE_DESTINATION;
public String RANG;


public int num_ID_EVOL_LOCALISE=0;
public int num_ID_PROJECTION=1;
public int num_ID_EVOL_NON_LOC=2;
public int num_ZONAGE=3;
public int num_COMPOSANTE=4;
public int num_ZONE=5;
public int num_ZONE_DESTINATION=6;
public int num_RANG=7;


public Evol_non_locDAO evol_non_loc;

public DAOZone zone;
public DAOZone zone_destination;


/**
 * initialise le where de la commande SQL d'Oracle
 * permet de ne prendre que les lignes égales à l'id_projection fourni
 * triées par id_projection et par rang.
 * @return String
 */
public String getWhere() {
    if (CacheDaoManager.testMode) return " order by id_projection, rang";
return " Where ID_PROJECTION="+CacheDaoManager.getFirstProjectionLancee().ID_PROJECTION+" order by id_projection, rang";
}

/** 
 * génère les neurones NeuroneEvol_localise
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
 * récupérer les objets evol_localise à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    Evol_localiseDAO neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   dao.execute("select * from Evol_localise "+getWhere());
   
   while (dao.nextRow()) { 
     neur=new Evol_localiseDAO();
     neur.ID_EVOL_LOCALISE=dao.getString("ID_EVOL_LOCALISE");
     neur.ID_PROJECTION=dao.getString("ID_PROJECTION");
     neur.ID_EVOL_NON_LOC=dao.getString("ID_EVOL_NON_LOC");
     neur.ZONAGE=dao.getString("ZONAGE");
     neur.COMPOSANTE=dao.getString("COMPOSANTE");
     neur.ZONE=dao.getString("ZONE");
     neur.ZONE_DESTINATION=dao.getString("ZONE_DESTINATION");
     neur.RANG=dao.getString("RANG");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }

/**
 * récupérer les objets evol_localise à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
   public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/Evol_localise.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    Evol_localiseDAO neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new Evol_localiseDAO();
     neur.ID_EVOL_LOCALISE=rec[num_ID_EVOL_LOCALISE];
     neur.ID_PROJECTION=rec[num_ID_PROJECTION];
     neur.ID_EVOL_NON_LOC=rec[num_ID_EVOL_NON_LOC];
     neur.ZONAGE=rec[num_ZONAGE];
     neur.COMPOSANTE=rec[num_COMPOSANTE];
     neur.ZONE=rec[num_ZONE];
     neur.ZONE_DESTINATION=rec[num_ZONE_DESTINATION];
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
public void setFait(Evol_localiseDAO neur) {
  ID_EVOL_LOCALISE=neur.ID_EVOL_LOCALISE;
  ID_PROJECTION=neur.ID_PROJECTION;
  ID_EVOL_NON_LOC=neur.ID_EVOL_NON_LOC;
  ZONAGE=neur.ZONAGE;
  COMPOSANTE=neur.COMPOSANTE;
  ZONE=neur.ZONE;
  ZONE_DESTINATION=neur.ZONE_DESTINATION;
  RANG=neur.RANG;
}

/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "Evol_localise:"
  +"\n\tID_EVOL_LOCALISE:"
  +ID_EVOL_LOCALISE+","
  +"\n\tID_PROJECTION:"
  +ID_PROJECTION+","
  +"\n\tID_EVOL_NON_LOC:"
  +ID_EVOL_NON_LOC+","
  +"\n\tZONAGE:"
  +ZONAGE+","
  +"\n\tCOMPOSANTE:"
  +COMPOSANTE+","
  +"\n\tZONE:"
  +ZONE+","
  +"\n\tZONE_DESTINATION:"
  +ZONE_DESTINATION+","
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
    if (evol_non_loc != null && zone !=null && zone_destination != null) {
        res= "\n"+pad+"<Evol_localise ZONE='"+zone.NOM+"' ZONE_DESTINATION='"+zone_destination.NOM+"' NOM='"
        +evol_non_loc.NOM+"' METHODE='"+evol_non_loc.METHODE_EVOL+"'>";
    } else
    if (evol_non_loc != null && zone !=null) {
    res= "\n"+pad+"<Evol_localise ZONE='"+zone.NOM+"' NOM='"
    +evol_non_loc.NOM+"' METHODE='"+evol_non_loc.METHODE_EVOL+"'>";
    } else if (evol_non_loc != null && zone_destination !=null) {
        res= "\n"+pad+"<Evol_localise ZONE_DESTINATION='"+zone_destination.NOM+"' NOM='"
        +evol_non_loc.NOM+"' METHODE='"+evol_non_loc.METHODE_EVOL+"'>";
    } else
    if (evol_non_loc != null) {
        res= "\n"+pad+"<Evol_localise NOM='"
        +evol_non_loc.NOM+"' METHODE='"+evol_non_loc.METHODE_EVOL+"'>";
    }
    else res= "\n"+pad+"<Evol_localise>";
      
      res+="\n"+pad+"     <ID_EVOL_LOCALISE>"
      +ID_EVOL_LOCALISE+"</ID_EVOL_LOCALISE>"
      +"\n"+pad+"     <ID_PROJECTION>"
      +ID_PROJECTION+"</ID_PROJECTION>"
      +"\n"+pad+"     <ID_EVOL_NON_LOC>"
      +ID_EVOL_NON_LOC+"</ID_EVOL_NON_LOC>"
      +"\n"+pad+"     <ZONAGE>"
      +ZONAGE+"</ZONAGE>"
      +"\n"+pad+"     <COMPOSANTE>"
      +COMPOSANTE+"</COMPOSANTE>"
      +"\n"+pad+"     <ZONE>"
      +ZONE+"</ZONE>"
      +"\n"+pad+"     <ZONE_DESTINATION>"
      +ZONE_DESTINATION+"</ZONE_DESTINATION>"
      +"\n"+pad+"     <RANG>"
      +RANG+"</RANG>"
    ;
    if (evol_non_loc !=null) res+="\n"+pad+evol_non_loc.toXmlString(padding+5);
    if (zone !=null) res+="\n"+pad+zone.toXmlString(padding+5);
    if (zone_destination !=null) res+="\n"+pad+zone_destination.toXmlString(padding+5);
    res += "\n"+pad+"</Evol_localise>";
    return res;
    }

public void setCreateSynapseParameters(Object fait) {
 setFait((Evol_localiseDAO)fait);
}

public void setDeepFait(Object fait) {
 setFait((Evol_localiseDAO)fait);
}

public void setRechercheParameters(Object fait) {
 setFait((Evol_localiseDAO)fait);
}

public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <Evol_localiseDAO> table=new ArrayList<Evol_localiseDAO>();
  for (int i=0;i<neurones.size();i++) {
    Evol_localiseDAO projCur=(Evol_localiseDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableEvol_localise=table;
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
public BeanScript getBeanScript() {
    BeanScript bean = evol_non_loc.getBeanScript();
    
    bean.getListeParametres().put("&&zone_locale.", (this.zone==null) ? "%" : zone.NOM);
    bean.getListeParametres().put("&&zone_destination.", (this.zone_destination==null) ? "%" : zone_destination.NOM);
    
    if (CacheDaoManager.getFirstProjectionLancee().def_projection.isEtalon()) {
        String czoneLoc=bean.getListeParametres().get("&&zone_locale.").replace("STANDARD_DEP", "DET");
        String czoneDest=bean.getListeParametres().get("&&zone_destination.").replace("STANDARD_DEP", "DET");
        bean.getListeParametres().put("&&zone_locale.", czoneLoc);
        bean.getListeParametres().put("&&zone_destination.", czoneDest);
    }
    
        return bean;
}

}

