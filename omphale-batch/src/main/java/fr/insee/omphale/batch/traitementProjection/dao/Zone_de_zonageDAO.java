package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier zone_de_zonage.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
//import fr.insee.omphale.cps.Cerveau;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;


public class Zone_de_zonageDAO implements INeuroneNonDAO{
public String ZONAGE;
public String ZONE;


public int num_ZONAGE=0;
public int num_ZONE=1;


public DAOZone zone;

/**
 * initialise le where de la commande SQL d'Oracle
 * pour récupérer les objets zone de zonage souhaités
 * triés par id_projection et par rang.
 * @return String
 */
public String getWhere() {
    if (CacheDaoManager.testMode) return "";
return " Where ZONAGE="+CacheDaoManager.getFirstProjectionLancee().def_projection.ID_ZONAGE;
}

/** 
 * génère les neurones NeuroneZoneDeZonage
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
 * récupérer les objets zone de zonage à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    Zone_de_zonageDAO neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   dao.execute("select * from Zone_de_zonage "+getWhere());

   while (dao.nextRow()) { 
     neur=new Zone_de_zonageDAO();
     neur.ZONAGE=dao.getString("ZONAGE");
     neur.ZONE=dao.getString("ZONE");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }

/**
 * récupérer les objets zone de zonage à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
   public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/Zone_de_zonage.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    Zone_de_zonageDAO neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new Zone_de_zonageDAO();
     neur.ZONAGE=rec[num_ZONAGE];
     neur.ZONE=rec[num_ZONE];
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
public void setFait(Zone_de_zonageDAO neur) {
  ZONAGE=neur.ZONAGE;
  ZONE=neur.ZONE;
}

/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "Zone_de_zonage:"
  +"\n\tZONAGE:"
  +ZONAGE+","
  +"\n\tZONE:"
  +ZONE+""
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
    String res;
    if (zone != null) res= "\n"+pad+"<Zone_de_zonage"+" NOM='"+zone.NOM+"' LIB='"+((zone.LIBELLE == null) ? "" : zone.LIBELLE.replace("'", " "))+"'>";
    else res= "\n"+pad+"<Zone_de_zonage>";
      res +="\n"+pad+"     <ZONAGE>"
      +ZONAGE+"</ZONAGE>"
      +"\n"+pad+"     <ZONE>"
      +ZONE+"</ZONE>"
    ;
    if (zone !=null) res+="\n"+pad+zone.toXmlString(padding+5);

    res += "\n"+pad+"</Zone_de_zonage>";
    return res;
    }


public void setCreateSynapseParameters(Object fait) {
 setFait((Zone_de_zonageDAO)fait);
}


public void setDeepFait(Object fait) {
 setFait((Zone_de_zonageDAO)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((Zone_de_zonageDAO)fait);
}

/**
 * appelé à chaque fois qu'on crée la synapse.
 * utile pour renseigner des tables sans passer par le Cerveau
 * @return void
 */
public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <Zone_de_zonageDAO> table=new ArrayList<Zone_de_zonageDAO>();
  for (int i=0;i<neurones.size();i++) {
    Zone_de_zonageDAO projCur=(Zone_de_zonageDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableZone_de_zonage=table;
}

/** 
 * accès direct sur la séquence qui ne contient qu'un neurone
 * @return String
 */
public String getMapKey() {
  return ZONAGE;
}

/**
 * le neurone retourne systèmatiquement True
 * @return boolean
 */
public boolean parseNeurone(INeurone modele) throws Exception {
  return true;
}


}

