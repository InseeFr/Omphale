package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier zonage.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
//import fr.insee.omphale.cps.Cerveau;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;


public class ZonageDAO implements INeuroneNonDAO{
public String ID_ZONAGE;
public String ID_USER;
public String NOM;
public String ANNEE_VALIDATION;
public String ETAT_VALIDATION;
public String LIBELLE;


public int num_ID_ZONAGE=0;
public int num_ID_USER=1;
public int num_NOM=2;
public int num_ANNEE_VALIDATION=3;
public int num_ETAT_VALIDATION=4;
public int num_LIBELLE=5;

public User_omphaleDAO user_omphale;
public ArrayList<Zone_de_zonageDAO> zones_de_zonage;


/**
 * initialise le where de la commande SQL d'Oracle
 * pour récupérer les objets zonage souhaités
 * triés par id_projection et par rang.
 * @return String
 */
public String getWhere() {
    if (CacheDaoManager.testMode) return "";
return " Where ID_ZONAGE="+CacheDaoManager.getFirstProjectionLancee().def_projection.ID_ZONAGE;
}

/** 
 * génère les neurones NeuroneZonage
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
 * récupérer les objets zonage à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    ZonageDAO neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   dao.execute("select * from Zonage "+getWhere());

   while (dao.nextRow()) { 
     neur=new ZonageDAO();
     neur.ID_ZONAGE=dao.getString("ID_ZONAGE");
     neur.ID_USER=dao.getString("ID_USER");
     neur.NOM=dao.getString("NOM");
     neur.ANNEE_VALIDATION=dao.getString("ANNEE_VALIDATION");
     neur.ETAT_VALIDATION=dao.getString("ETAT_VALIDATION");
     neur.LIBELLE=dao.getString("LIBELLE");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }

/**
 * récupérer les objets zonage à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
   public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/Zonage.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    ZonageDAO neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new ZonageDAO();
     neur.ID_ZONAGE=rec[num_ID_ZONAGE];
     neur.ID_USER=rec[num_ID_USER];
     neur.NOM=rec[num_NOM];
     neur.ANNEE_VALIDATION=rec[num_ANNEE_VALIDATION];
     neur.ETAT_VALIDATION=rec[num_ETAT_VALIDATION];
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
public void setFait(ZonageDAO neur) {
  ID_ZONAGE=neur.ID_ZONAGE;
  ID_USER=neur.ID_USER;
  NOM=neur.NOM;
  ANNEE_VALIDATION=neur.ANNEE_VALIDATION;
  ETAT_VALIDATION=neur.ETAT_VALIDATION;
  LIBELLE=neur.LIBELLE;
}

/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "Zonage:"
  +"\n\tID_ZONAGE:"
  +ID_ZONAGE+","
  +"\n\tID_USER:"
  +ID_USER+","
  +"\n\tNOM:"
  +NOM+","
  +"\n\tANNEE_VALIDATION:"
  +ANNEE_VALIDATION+","
  +"\n\tETAT_VALIDATION:"
  +ETAT_VALIDATION+","
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
    
    String res= "\n"+pad+"<Zonage USER='"+ID_USER+"' NOM='"+NOM+"' LIB='"+((LIBELLE == null) ? "" : LIBELLE.replace("'", " "))+"'>"
      +"\n"+pad+"     <ID_ZONAGE>"
      +ID_ZONAGE+"</ID_ZONAGE>"
      +"\n"+pad+"     <ID_USER>"
      +ID_USER+"</ID_USER>"
      +"\n"+pad+"     <NOM>"
      +NOM+"</NOM>"
      +"\n"+pad+"     <ANNEE_VALIDATION>"
      +ANNEE_VALIDATION+"</ANNEE_VALIDATION>"
      +"\n"+pad+"     <ETAT_VALIDATION>"
      +ETAT_VALIDATION+"</ETAT_VALIDATION>"
      +"\n"+pad+"     <LIBELLE>"
      +LIBELLE+"</LIBELLE>"
    ;
    if (user_omphale !=null) res+="\n"+pad+user_omphale.toXmlString(padding+5);
    if (zones_de_zonage !=null) {
        for (int i=0;i<zones_de_zonage.size();i++) {
            res+="\n"+pad+zones_de_zonage.get(i).toXmlString(padding+5);
            
        }
    }
    res += "\n"+pad+"</Zonage>";
    return res;
    }


public void setCreateSynapseParameters(Object fait) {
 setFait((ZonageDAO)fait);
}


public void setDeepFait(Object fait) {
 setFait((ZonageDAO)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((ZonageDAO)fait);
}

/**
 * appelé à chaque fois qu'on crée la synapse.
 * utile pour renseigner des tables sans passer par le Cerveau
 * @return void
 */
public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <ZonageDAO> table=new ArrayList<ZonageDAO>();
  for (int i=0;i<neurones.size();i++) {
    ZonageDAO projCur=(ZonageDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableZonage=table;
}

/** 
 * accès direct sur la séquence qui ne contient qu'un neurone
 * @return String
 */
public String getMapKey() {
  return ID_ZONAGE;
}


/**
 * le neurone retourne systèmatiquement True
 * @return boolean
 */
public boolean parseNeurone(INeurone modele) throws Exception {
  return true;
}


}


