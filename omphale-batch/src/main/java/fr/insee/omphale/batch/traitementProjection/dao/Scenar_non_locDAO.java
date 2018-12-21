package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier scenar_non_loc.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
//import fr.insee.omphale.cps.Cerveau;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;


public class Scenar_non_locDAO implements INeuroneNonDAO{
public String ID_SCENARIO;
public String ID_USER;
public String NOM;
public String STANDARD;
public String VALIDATION;
public String DATE_CREATION;
public String LIBELLE;


public int num_ID_SCENARIO=0;
public int num_ID_USER=1;
public int num_NOM=2;
public int num_STANDARD=3;
public int num_VALIDATION=4;
public int num_DATE_CREATION=5;
public int num_LIBELLE=6;

public ArrayList<Evol_de_scenarDAO> evolutions_de_scenar;


/**
 * initialise le where de la commande SQL d'Oracle
 * pour récupérer les bons objets scenar_non_loc
 * @return String
 */
public String getWhere() {
    if (CacheDaoManager.testMode)  return "";
    return " Where ID_SCENARIO="+CacheDaoManager.getFirstProjectionLancee().def_projection.ID_SCENARIO;
}

/** 
 * génère les neurones NeuroneScenar_non_loc
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
 * récupérer les objets Scenar_non_loc à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    Scenar_non_locDAO neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   dao.execute("select * from Scenar_non_loc "+getWhere());

   while (dao.nextRow()) { 
     neur=new Scenar_non_locDAO();
     neur.ID_SCENARIO=dao.getString("ID_SCENARIO");
     neur.ID_USER=dao.getString("ID_USER");
     neur.NOM=dao.getString("NOM");
     neur.STANDARD=dao.getString("STANDARD");
     neur.VALIDATION=dao.getString("VALIDATION");
     neur.DATE_CREATION=dao.getString("DATE_CREATION");
     neur.LIBELLE=dao.getString("LIBELLE");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }

/**
 * récupérer les objets scenar_non_loc à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
   public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/Scenar_non_loc.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    Scenar_non_locDAO neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new Scenar_non_locDAO();
     neur.ID_SCENARIO=rec[num_ID_SCENARIO];
     neur.ID_USER=rec[num_ID_USER];
     neur.NOM=rec[num_NOM];
     neur.STANDARD=rec[num_STANDARD];
     neur.VALIDATION=rec[num_VALIDATION];
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
public void setFait(Scenar_non_locDAO neur) {
  ID_SCENARIO=neur.ID_SCENARIO;
  ID_USER=neur.ID_USER;
  NOM=neur.NOM;
  STANDARD=neur.STANDARD;
  VALIDATION=neur.VALIDATION;
  DATE_CREATION=neur.DATE_CREATION;
  LIBELLE=neur.LIBELLE;
}

/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "Scenar_non_loc:"
  +"\n\tID_SCENARIO:"
  +ID_SCENARIO+","
  +"\n\tID_USER:"
  +ID_USER+","
  +"\n\tNOM:"
  +NOM+","
  +"\n\tSTANDARD:"
  +STANDARD+","
  +"\n\tVALIDATION:"
  +VALIDATION+","
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
    
    String res= "\n"+pad+"<Scenar_non_loc"+" USER='"+ID_USER+"' NOM='"+NOM+"' LIB='"+((LIBELLE == null) ? "" : LIBELLE.replace("'", " "))+"'>"
    
      +"\n"+pad+"     <ID_SCENARIO>"
      +ID_SCENARIO+"</ID_SCENARIO>"
      +"\n"+pad+"     <ID_USER>"
      +ID_USER+"</ID_USER>"
      +"\n"+pad+"     <NOM>"
      +NOM+"</NOM>"
      +"\n"+pad+"     <STANDARD>"
      +STANDARD+"</STANDARD>"
      +"\n"+pad+"     <VALIDATION>"
      +VALIDATION+"</VALIDATION>"
      +"\n"+pad+"     <DATE_CREATION>"
      +DATE_CREATION+"</DATE_CREATION>"
      +"\n"+pad+"     <LIBELLE>"
      +LIBELLE+"</LIBELLE>"
    ;
    if (evolutions_de_scenar !=null) {
        for (int i=0;i<evolutions_de_scenar.size();i++) {
            res+="\n"+pad+evolutions_de_scenar.get(i).toXmlString(padding+5);
            
        }
    }
    res += "\n"+pad+"</Scenar_non_loc>";
    return res;
    }

public void setCreateSynapseParameters(Object fait) {
 setFait((Scenar_non_locDAO)fait);
}


public void setDeepFait(Object fait) {
 setFait((Scenar_non_locDAO)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((Scenar_non_locDAO)fait);
}

/**
 * appelé à chaque fois qu'on crée la synapse.
 * utile pour renseigner des tables sans passer par le Cerveau
 * @return void
 */
public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <Scenar_non_locDAO> table=new ArrayList<Scenar_non_locDAO>();
  for (int i=0;i<neurones.size();i++) {
    Scenar_non_locDAO projCur=(Scenar_non_locDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableScenar_non_loc=table;
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


}

