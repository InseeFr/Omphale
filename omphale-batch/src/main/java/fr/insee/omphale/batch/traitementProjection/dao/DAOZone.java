package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier zone.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
//import fr.insee.omphale.cps.Cerveau;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;


public class DAOZone implements INeuroneNonDAO{
public String ID_ZONE;
public String ID_USER;
public String NOM;
public String STANDARD;
public String TYPE_ZONE_STANDARD;
public String LIBELLE;


public int num_ID_ZONE=0;
public int num_ID_USER=1;
public int num_NOM=2;
public int num_STANDARD=3;
public int num_TYPE_ZONE_STANDARD=4;
public int num_LIBELLE=5;


/**
 * initialise le where de la commande SQL d'Oracle
 * pour récupérer les objets zone souhaités
 * triées par id_projection et par rang.
 * @return String
 */
public String getWhere() {
    if (CacheDaoManager.testMode) return "";
    return " Where ID_ZONE in (select zone from zone_de_zonage where zonage="+
    CacheDaoManager.getFirstProjectionLancee().def_projection.ID_ZONAGE+")";
}

/** 
 * génère les neurones NeuroneZone
 * en sqlmode appel getSqlMemory
 * sinon getFileMemory (pour les tests)
 * 
 *@return {@link ArrayList}Object
 */
public ArrayList<Object> getDeepMemory() throws Exception {
    if (!CacheDaoManager.sqlMode)  return getFileMemory();
  else return getSqlMemory();
}

/**
 * récupérer les objets zone à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    DAOZone neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   dao.execute("select * from Zone "+getWhere());
   while (dao.nextRow()) { 
     neur=new DAOZone();
     neur.ID_ZONE=dao.getString("ID_ZONE");
     neur.ID_USER=dao.getString("ID_USER");
     neur.NOM=dao.getString("NOM");
     neur.STANDARD=dao.getString("STANDARD");
     neur.TYPE_ZONE_STANDARD=dao.getString("TYPE_ZONE_STANDARD");
     neur.LIBELLE=dao.getString("LIBELLE");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }

/**
 * récupérer les objets zone à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
   public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/Zone.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    DAOZone neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new DAOZone();
     neur.ID_ZONE=rec[num_ID_ZONE];
     neur.ID_USER=rec[num_ID_USER];
     neur.NOM=rec[num_NOM];
     neur.STANDARD=rec[num_STANDARD];
     neur.TYPE_ZONE_STANDARD=rec[num_TYPE_ZONE_STANDARD];
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
public void setFait(DAOZone neur) {
  ID_ZONE=neur.ID_ZONE;
  ID_USER=neur.ID_USER;
  NOM=neur.NOM;
  STANDARD=neur.STANDARD;
  TYPE_ZONE_STANDARD=neur.TYPE_ZONE_STANDARD;
  LIBELLE=neur.LIBELLE;
}

/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "Zone:"
  +"\n\tID_ZONE:"
  +ID_ZONE+","
  +"\n\tID_USER:"
  +ID_USER+","
  +"\n\tNOM:"
  +NOM+","
  +"\n\tSTANDARD:"
  +STANDARD+","
  +"\n\tTYPE_ZONE_STANDARD:"
  +TYPE_ZONE_STANDARD+","
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
    
    String res= "\n"+pad+"<Zone"+" NOM='"+NOM+"' LIB='"+((LIBELLE == null) ? "" : LIBELLE.replace("'", " "))+"'>"
      +"\n"+pad+"     <ID_ZONE>"
      +ID_ZONE+"</ID_ZONE>"
      +"\n"+pad+"     <ID_USER>"
      +ID_USER+"</ID_USER>"
      +"\n"+pad+"     <NOM>"
      +NOM+"</NOM>"
      +"\n"+pad+"     <STANDARD>"
      +STANDARD+"</STANDARD>"
      +"\n"+pad+"     <TYPE_ZONE_STANDARD>"
      +TYPE_ZONE_STANDARD+"</TYPE_ZONE_STANDARD>"
      +"\n"+pad+"     <LIBELLE>"
      +LIBELLE+"</LIBELLE>"
    ;
    res += "\n"+pad+"</Zone>";
    return res;
    }

public void setCreateSynapseParameters(Object fait) {
 setFait((DAOZone)fait);
}


public void setDeepFait(Object fait) {
 setFait((DAOZone)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((DAOZone)fait);
}

/**
 * appelé à chaque fois qu'on crée la synapse.
 * utile pour renseigner des tables sans passer par le Cerveau
 * @return void
 */
public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <DAOZone> table=new ArrayList<DAOZone>();
  for (int i=0;i<neurones.size();i++) {
    DAOZone projCur=(DAOZone) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableZone=table;
}

/** 
 * accès direct sur la séquence qui ne contient qu'un neurone
 * @return String
 */
public String getMapKey() {
  return ID_ZONE;
}

/**
 * le neurone retourne systèmatiquement True
 * @return boolean
 */
public boolean parseNeurone(INeurone modele) throws Exception {
  return true;
}


}

