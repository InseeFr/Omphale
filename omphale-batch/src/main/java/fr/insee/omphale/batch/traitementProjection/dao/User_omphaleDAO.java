package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier user_omphale.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
//import fr.insee.omphale.cps.Cerveau;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;


public class User_omphaleDAO implements INeuroneNonDAO{
public String ID_USER;
public String ROLE;
public String EMAIL;
public String LIBELLE;
public String IDEP;


public int num_ID_USER=0;
public int num_ROLE=1;
public int num_EMAIL=2;
public int num_LIBELLE=3;
public int num_IDEP=4;



/**
 * initialise le where de la commande SQL d'Oracle
 * pour récupérer les objets user_omphale souhaité
 * triés par id_projection et par rang.
 * @return String
 */
public String getWhere() {
    if (CacheDaoManager.testMode) return "";
return " Where ID_USER='"+CacheDaoManager.getFirstProjectionLancee().def_projection.ID_USER+"'";
}



/** 
 * génère les neurones NeuroneUser_Omphale
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
 * récupérer les objets user_omphale à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    User_omphaleDAO neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   dao.execute("select * from User_omphale "+getWhere());

   while (dao.nextRow()) { 
     neur=new User_omphaleDAO();
     neur.ID_USER=dao.getString("ID_USER");
     neur.ROLE=dao.getString("ROLE");
     neur.EMAIL=dao.getString("EMAIL");
     neur.LIBELLE=dao.getString("LIBELLE");
     neur.IDEP=dao.getString("IDEP");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }


/**
 * récupérer les objets user_omphale à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
   public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/User_omphale.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    User_omphaleDAO neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new User_omphaleDAO();
     neur.ID_USER=rec[num_ID_USER];
     neur.ROLE=rec[num_ROLE];
     neur.EMAIL=rec[num_EMAIL];
     neur.LIBELLE=rec[num_LIBELLE];
     neur.IDEP=rec[num_IDEP];
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
public void setFait(User_omphaleDAO neur) {
  ID_USER=neur.ID_USER;
  ROLE=neur.ROLE;
  EMAIL=neur.EMAIL;
  LIBELLE=neur.LIBELLE;
  IDEP=neur.IDEP;
}


/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "User_omphale:"
  +"\n\tID_USER:"
  +ID_USER+","
  +"\n\tROLE:"
  +ROLE+","
  +"\n\tEMAIL:"
  +EMAIL+","
  +"\n\tLIBELLE:"
  +LIBELLE+","
  +"\n\tIDEP:"
  +IDEP+""
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
    String res= "\n"+pad+"<User_omphale ID_USER='"+ID_USER+"' IDEP='"+IDEP+"'>"
      +"\n"+pad+"     <ID_USER>"
      +ID_USER+"</ID_USER>"
      +"\n"+pad+"     <ROLE>"
      +ROLE+"</ROLE>"
      +"\n"+pad+"     <EMAIL>"
      +EMAIL+"</EMAIL>"
      +"\n"+pad+"     <LIBELLE>"
      +LIBELLE+"</LIBELLE>"
      +"\n"+pad+"     <IDEP>"
      +IDEP+"</IDEP>"
    ;
    res += "\n"+pad+"</User_omphale>";
    return res;
    }

public void setCreateSynapseParameters(Object fait) {
 setFait((User_omphaleDAO)fait);
}


public void setDeepFait(Object fait) {
 setFait((User_omphaleDAO)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((User_omphaleDAO)fait);
}

/**
 * appelé à chaque fois qu'on crée la synapse.
 * utile pour renseigner des tables sans passer par le Cerveau
 * @return void
 */
public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <User_omphaleDAO> table=new ArrayList<User_omphaleDAO>();
  for (int i=0;i<neurones.size();i++) {
    User_omphaleDAO projCur=(User_omphaleDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableUser_omphale=table;
}

/** 
 * accès direct sur la séquence qui ne contient qu'un neurone
 * @return String
 */
public String getMapKey() {
  return ID_USER;
}

/**
 * le neurone retourne systèmatiquement True
 * @return boolean
 */
public boolean parseNeurone(INeurone modele) throws Exception {
  return true;
}


}


