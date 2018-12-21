package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier user_hypothese.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
//import fr.insee.omphale.cps.Cerveau;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;


public class User_hypotheseDAO implements INeuroneNonDAO{
public String ID_HYPOTHESE;
public String ID_USER;
public String NOM;
public String TYPE_ENTITE;
public String AGE_DEB;
public String SEXE_DEB;
public String ANNEE_DEB;
public String AGE_FIN;
public String SEXE_FIN;
public String ANNEE_FIN;
public String STANDARD;
public String DATE_CREATION;
public String VALIDATION;
public String LIBELLE;


public int num_ID_HYPOTHESE=0;
public int num_ID_USER=1;
public int num_NOM=2;
public int num_TYPE_ENTITE=3;
public int num_AGE_DEB=4;
public int num_SEXE_DEB=5;
public int num_ANNEE_DEB=6;
public int num_AGE_FIN=7;
public int num_SEXE_FIN=8;
public int num_ANNEE_FIN=9;
public int num_STANDARD=10;
public int num_DATE_CREATION=11;
public int num_VALIDATION=12;
public int num_LIBELLE=13;



private String where;



/**
 * initialise le where de la commande SQL d'Oracle
 * pour récupérer les objets user_hypothese souhaités
 * triés par id_projection et par rang.
 * @return String
 */
private void setWhere() {
    where="";
    if (!CacheDaoManager.testMode) {
    where=" where id_hypothese in (select id_hypothese from evol_non_loc "
        +" Where ID_EVOL_NON_LOC in (select id_evol_non_loc from evol_de_scenar where id_scenario="
        +CacheDaoManager.getFirstProjectionLancee().def_projection.ID_SCENARIO+") or "
        +"ID_EVOL_NON_LOC in (select id_evol_non_loc from evol_localise where id_projection="
        +CacheDaoManager.getFirstProjectionLancee().def_projection.ID_PROJECTION+"))";
    }
}


/** 
 * génère les neurones NeuroneUser_hypothese
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
 * récupérer les objets user_hypothese à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
 User_hypotheseDAO neur;
ArrayList<Object> lines=new ArrayList<Object>();

ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
setWhere();
dao.execute("select * from User_hypothese "+where);

while (dao.nextRow()) { 
  neur=new User_hypotheseDAO();
  neur.ID_HYPOTHESE=dao.getString("ID_HYPOTHESE");
  neur.ID_USER=dao.getString("ID_USER");
  neur.NOM=dao.getString("NOM");
  neur.TYPE_ENTITE=dao.getString("TYPE_ENTITE");
  neur.AGE_DEB=dao.getString("AGE_DEB");
  neur.SEXE_DEB=dao.getString("SEXE_DEB");
  neur.ANNEE_DEB=dao.getString("ANNEE_DEB");
  neur.AGE_FIN=dao.getString("AGE_FIN");
  neur.SEXE_FIN=dao.getString("SEXE_FIN");
  neur.ANNEE_FIN=dao.getString("ANNEE_FIN");
  neur.STANDARD=dao.getString("STANDARD");
  neur.DATE_CREATION=dao.getString("DATE_CREATION");
  neur.VALIDATION=dao.getString("VALIDATION");
  neur.LIBELLE=dao.getString("LIBELLE");
  lines.add(neur);
}
 dao.close();
 return lines;
}

/**
 * récupérer les objets user_hypothese à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getFileMemory() throws Exception {
 FileReader in = new FileReader("src/test/resources/User_hypothese.txt");
 BufferedReader buf = new BufferedReader(in);
 String chaine = buf.readLine();
 boolean ligne=false;
 User_hypotheseDAO neur;
 ArrayList<Object> lines=new ArrayList<Object>();
 while(chaine != null) {
  if (ligne) {
  String[] rec=(chaine+";FIN_ENREG").split(";");
  neur=new User_hypotheseDAO();
  neur.ID_HYPOTHESE=rec[num_ID_HYPOTHESE];
  neur.ID_USER=rec[num_ID_USER];
  neur.NOM=rec[num_NOM];
  neur.TYPE_ENTITE=rec[num_TYPE_ENTITE];
  neur.AGE_DEB=rec[num_AGE_DEB];
  neur.SEXE_DEB=rec[num_SEXE_DEB];
  neur.ANNEE_DEB=rec[num_ANNEE_DEB];
  neur.AGE_FIN=rec[num_AGE_FIN];
  neur.SEXE_FIN=rec[num_SEXE_FIN];
  neur.ANNEE_FIN=rec[num_ANNEE_FIN];
  neur.STANDARD=rec[num_STANDARD];
  neur.DATE_CREATION=rec[num_DATE_CREATION];
  neur.VALIDATION=rec[num_VALIDATION];
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
public void setFait(User_hypotheseDAO neur) {
  ID_HYPOTHESE=neur.ID_HYPOTHESE;
  ID_USER=neur.ID_USER;
  NOM=neur.NOM;
  TYPE_ENTITE=neur.TYPE_ENTITE;
  AGE_DEB=neur.AGE_DEB;
  SEXE_DEB=neur.SEXE_DEB;
  ANNEE_DEB=neur.ANNEE_DEB;
  AGE_FIN=neur.AGE_FIN;
  SEXE_FIN=neur.SEXE_FIN;
  ANNEE_FIN=neur.ANNEE_FIN;
  STANDARD=neur.STANDARD;
  DATE_CREATION=neur.DATE_CREATION;
  VALIDATION=neur.VALIDATION;
  LIBELLE=neur.LIBELLE;
}

/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "User_hypothese:"
  +"\n\tID_HYPOTHESE:"
  +ID_HYPOTHESE+","
  +"\n\tID_USER:"
  +ID_USER+","
  +"\n\tNOM:"
  +NOM+","
  +"\n\tTYPE_ENTITE:"
  +TYPE_ENTITE+","
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
  +"\n\tSTANDARD:"
  +STANDARD+","
  +"\n\tDATE_CREATION:"
  +DATE_CREATION+","
  +"\n\tVALIDATION:"
  +VALIDATION+","
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

String res= "\n"+pad+"<User_hypothese"+" USER='"+ID_USER+"' NOM='"+NOM+"' LIB='"+((LIBELLE == null) ? "" : LIBELLE.replace("'", " "))+"'>"

  +"\n"+pad+"     <ID_HYPOTHESE>"
  +ID_HYPOTHESE+"</ID_HYPOTHESE>"
  +"\n"+pad+"     <ID_USER>"
  +ID_USER+"</ID_USER>"
  +"\n"+pad+"     <NOM>"
  +NOM+"</NOM>"
  +"\n"+pad+"     <TYPE_ENTITE>"
  +TYPE_ENTITE+"</TYPE_ENTITE>"
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
  +"\n"+pad+"     <STANDARD>"
  +STANDARD+"</STANDARD>"
  +"\n"+pad+"     <DATE_CREATION>"
  +DATE_CREATION+"</DATE_CREATION>"
  +"\n"+pad+"     <VALIDATION>"
  +VALIDATION+"</VALIDATION>"
  +"\n"+pad+"     <LIBELLE>"
  +LIBELLE+"</LIBELLE>"
;
res += "\n"+pad+"</User_hypothese>";
return res;
}


public void setCreateSynapseParameters(Object fait) {
 setFait((User_hypotheseDAO)fait);
}


public void setDeepFait(Object fait) {
 setFait((User_hypotheseDAO)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((User_hypotheseDAO)fait);
}

/**
 * appelé à chaque fois qu'on crée la synapse.
 * utile pour renseigner des tables sans passer par le Cerveau
 * @return void
 */
public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <User_hypotheseDAO> table=new ArrayList<User_hypotheseDAO>();
  for (int i=0;i<neurones.size();i++) {
    User_hypotheseDAO projCur=(User_hypotheseDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableUser_hypothese=table;
}

/** 
 * accès direct sur la séquence qui ne contient qu'un neurone
 * @return String
 */
public String getMapKey() {
  return ID_HYPOTHESE;
}


/**
 * le neurone retourne systèmatiquement True
 * @return boolean
 */
public boolean parseNeurone(INeurone modele) throws Exception {
  return true;
}


}
