package fr.insee.omphale.batch.traitementProjection.dao;

//Generation partielle de la Classe de neurone FROM pour le fichier projection_lancee.txt


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
//import fr.insee.omphale.cps.Cerveau;

import fr.insee.omphale.batch.cps.INeurone;
import fr.insee.omphale.batch.cps.INeuroneNonDAO;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;


public class Projection_lanceeDAO implements INeuroneNonDAO{
public String ID_PROJECTION_LANCEE;
public String ID_PROJECTION;
public String DATE_LANCEMENT;
public String DATE_EXEC;
public String DONNEES;
public String DATE_DEBUT_EXEC;
public String CODE_RETOUR;
public String MESSAGE;
public String NBR_ZONE;


public int num_ID_PROJECTION_LANCEE=0;
public int num_ID_PROJECTION=1;
public int num_DATE_LANCEMENT=2;
public int num_DATE_EXEC=3;
public int num_DONNEES=4;
public int num_DATE_DEBUT_EXEC=5;
public int num_CODE_RETOUR=6;
public int num_MESSAGE=7;
public int num_NBR_ZONE=8;

public Def_projectionDAO def_projection;


/**
 * Méthode qui renvoie la requête SQL de mise à jour de la date de début
 * d'exécution de la projection courante dans la table des projections en
 * attente correspondant au contexte (utilisateur ou étalon).
 * @return la requête SQL de mise à jour de la date de début d'exécution.
 */
public String getSqlDebut() {
    
        return sqlDebut.toString() + ID_PROJECTION_LANCEE;
    
}

/**
 * Méthode qui renvoie la requête SQL de mise à jour de la date d'exécution,
 * du code retour et du message de la projection courante dans la table des
 * projections en attente correspondant au contexte (utilisateur ou étalon).
 * @return la requête SQL de mise à jour de la date d'exécution, du code
 *         retour et du message.
 */
public String getSqlFin() {
    CacheDaoManager.beanRapport.montre.stop();
    CacheDaoManager.beanRapport.message+=" TEMPS_MINUTE:"+CacheDaoManager.beanRapport.montre.getMinutes();
    return (sqlFin.toString() + ID_PROJECTION_LANCEE).replace("XXXXX",
            (CacheDaoManager.beanRapport.code+"")).replace("YYYYY", CacheDaoManager.beanRapport.message);
    
}


private static StringBuffer sqlDebut = new StringBuffer(
        "update projection_lancee set date_debut_exec=sysdate where id_projection_lancee=");

private static StringBuffer sqlFin = new StringBuffer(
        "update projection_lancee set date_exec=sysdate, code_retour='XXXXX', message='YYYYY' where id_projection_lancee=");


private static StringBuffer sqlSelect = new StringBuffer("select * from ( ")
.append("\n")
.append(" select 1 priorite, a.*, b.id_scenario, b.id_user from projection_lancee a, def_projection b ")
.append("\n")
.append(" where a.id_projection=b.id_projection and a.date_debut_exec is null and b.validation=1 ")
.append("\n")
.append(" and nvl(b.standard,0)=0 and b.id_scenario is not null ")
.append("\n")
.append(" and b.id_zonage !=0 ")
.append("\n")
.append(" and a.nbr_zone < ").append(OmphaleBatchConfig.nbrZoneGrosZonage)
.append("\n")

.append(" and a.nbr_zone !=0 ")
.append("\n")

.append(" and nvl(a.code_retour,0) != ").append(OmphaleBatchConfig.codeAnnulationProjection)
.append("\n")
.append(" union ")
.append("\n")
.append(" select 3 priorite, a.*, b.id_scenario, b.id_user from projection_lancee a, def_projection b ")
.append("\n")
.append(" where a.id_projection=b.id_projection and a.date_debut_exec is null and b.validation=1 ")
.append("\n")
.append(" and nvl(b.standard,0)=0 and b.id_scenario is not null ")
.append("\n")
.append(" and b.id_zonage !=0 ")
.append("\n")
.append(" and a.nbr_zone >= ").append(OmphaleBatchConfig.nbrZoneGrosZonage)
.append("\n")
.append(" and a.nbr_zone < ").append(OmphaleBatchConfig.nbrZoneSuperGrosZonage)
.append("\n")

.append(" and a.nbr_zone !=0 ")
.append("\n")


.append(" and nvl(a.code_retour,0) != ").append(OmphaleBatchConfig.codeAnnulationProjection)
.append("\n")
.append(" union ")
.append("\n")

.append(" select 4 priorite, a.*, b.id_scenario, b.id_user from projection_lancee a, def_projection b ")
.append("\n")
.append(" where a.id_projection=b.id_projection and a.date_debut_exec is null and b.validation=1 ")
.append("\n")
.append(" and nvl(b.standard,0)=0 and b.id_scenario is not null ")
.append("\n")
.append(" and b.id_zonage !=0 ")
.append("\n")
.append(" and a.nbr_zone >= ").append(OmphaleBatchConfig.nbrZoneSuperGrosZonage)
.append("\n")
.append(" and TO_CHAR(sysdate, 'HH24:MI') < '").append(OmphaleBatchConfig.heureSuperGrosZonage).append("'")
.append("\n")

.append(" and a.nbr_zone !=0 ")
.append("\n")


.append(" and nvl(a.code_retour,0) != ").append(OmphaleBatchConfig.codeAnnulationProjection)
.append("\n")
.append(" union ")
.append("\n")

.append(" select 2 priorite, a.*, b.id_scenario, b.id_user from projection_lancee a, def_projection b ")
.append("\n")
.append(" where a.id_projection=b.id_projection and a.date_debut_exec is null and b.validation=1 ")
.append("\n")
.append(" and nvl(b.standard,0)=0 and b.id_scenario is not null ")
.append("\n")

.append(" and a.nbr_zone !=0 ")
.append("\n")


.append(" and nvl(a.code_retour,0) != ").append(OmphaleBatchConfig.codeAnnulationProjection)
.append("\n")
.append(" and b.id_zonage =0 )")
.append(" where rownum=1 order by priorite asc, date_lancement asc ")
;


/** 
 * génère les neurones NeuroneProjection_lancee
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
 * récupérer les objets projection_lancee à partir d'une requête SQL
 * instancie un objet neurone par ligne récupérée
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
public ArrayList<Object> getSqlMemory() throws Exception {
    Projection_lanceeDAO neur;
   ArrayList<Object> lines=new ArrayList<Object>();
   if (CacheDaoManager.testMode) return lines;
   ISimpleSelectDao dao = CacheDaoManager.simpleSelectDao;
   dao.execute(sqlSelect.toString());

   while (dao.nextRow()) { 
     neur=new Projection_lanceeDAO();
     neur.ID_PROJECTION_LANCEE=dao.getString("ID_PROJECTION_LANCEE");
     neur.ID_PROJECTION=dao.getString("ID_PROJECTION");
     neur.DATE_LANCEMENT=dao.getString("DATE_LANCEMENT");
     neur.DATE_EXEC=dao.getString("DATE_EXEC");
     neur.DONNEES=dao.getString("DONNEES");
     neur.DATE_DEBUT_EXEC=dao.getString("DATE_DEBUT_EXEC");
     neur.CODE_RETOUR=dao.getString("CODE_RETOUR");
     neur.MESSAGE=dao.getString("MESSAGE");
     neur.NBR_ZONE=dao.getString("NBR_ZONE");
     lines.add(neur);
   }
    dao.close();
    return lines;
   }

/**
 * récupérer les objets projection_lancee à partir d'un fichier texte
 * instancie un objet neurone par ligne récupérée
 * uniquement pour les tests
 * 
 * @return {@link ArrayList}
 * @throws Exception 
 */
   public ArrayList<Object> getFileMemory() throws Exception {
    FileReader in = new FileReader("src/test/resources/Projection_lancee.txt");
    BufferedReader buf = new BufferedReader(in);
    String chaine = buf.readLine();
    boolean ligne=false;
    Projection_lanceeDAO neur;
    ArrayList<Object> lines=new ArrayList<Object>();
    while(chaine != null) {
     if (ligne) {
     String[] rec=(chaine+";FIN_ENREG").split(";");
     neur=new Projection_lanceeDAO();
     neur.ID_PROJECTION_LANCEE=rec[num_ID_PROJECTION_LANCEE];
     neur.ID_PROJECTION=rec[num_ID_PROJECTION];
     neur.DATE_LANCEMENT=rec[num_DATE_LANCEMENT];
     neur.DATE_EXEC=rec[num_DATE_EXEC];
     neur.DONNEES=rec[num_DONNEES];
     neur.DATE_DEBUT_EXEC=rec[num_DATE_DEBUT_EXEC];
     neur.CODE_RETOUR=rec[num_CODE_RETOUR];
     neur.MESSAGE=rec[num_MESSAGE];
     neur.NBR_ZONE=rec[num_NBR_ZONE];
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
public void setFait(Projection_lanceeDAO neur) {
  ID_PROJECTION_LANCEE=neur.ID_PROJECTION_LANCEE;
  ID_PROJECTION=neur.ID_PROJECTION;
  DATE_LANCEMENT=neur.DATE_LANCEMENT;
  DATE_EXEC=neur.DATE_EXEC;
  DONNEES=neur.DONNEES;
  DATE_DEBUT_EXEC=neur.DATE_DEBUT_EXEC;
  CODE_RETOUR=neur.CODE_RETOUR;
  MESSAGE=neur.MESSAGE;
  NBR_ZONE=neur.NBR_ZONE;
}

/**
 * Renvoie une chaîne de caractère contenant toutes les informations de la projection à traiter
 * @param neur 
 * @return void
 */
public String toString() {
  return "Projection_lancee:"
  +"\n\tID_PROJECTION_LANCEE:"
  +ID_PROJECTION_LANCEE+","
  +"\n\tID_PROJECTION:"
  +ID_PROJECTION+","
  +"\n\tDATE_LANCEMENT:"
  +DATE_LANCEMENT+","
  +"\n\tDATE_EXEC:"
  +DATE_EXEC+","
  +"\n\tDONNEES:"
  +DONNEES+","
  +"\n\tDATE_DEBUT_EXEC:"
  +DATE_DEBUT_EXEC+","
  +"\n\tCODE_RETOUR:"
  +CODE_RETOUR+","
  +"\n\tMESSAGE:"
  +MESSAGE+","
  +"\n\tNBR_ZONE:"
  +NBR_ZONE+""
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
    if (def_projection != null) {
       res= "\n"+pad+"<Projection_lancee"+" USER='"+def_projection.ID_USER+"' NOM='"
       +def_projection.NOM+"' LIB='"+((def_projection.LIBELLE == null) ? "" : def_projection.LIBELLE.replace("'", " "))+"'>";
    }
    else  {
        res= "\n"+pad+"<Projection_lancee>";
    }
     
      res+="\n"+pad+"     <ID_PROJECTION_LANCEE>"
      +ID_PROJECTION_LANCEE+"</ID_PROJECTION_LANCEE>"
      +"\n"+pad+"     <ID_PROJECTION>"
      +ID_PROJECTION+"</ID_PROJECTION>"
      +"\n"+pad+"     <DATE_LANCEMENT>"
      +DATE_LANCEMENT+"</DATE_LANCEMENT>"
      +"\n"+pad+"     <DATE_EXEC>"
      +DATE_EXEC+"</DATE_EXEC>"
      +"\n"+pad+"     <DONNEES>"
      +DONNEES+"</DONNEES>"
      +"\n"+pad+"     <DATE_DEBUT_EXEC>"
      +DATE_DEBUT_EXEC+"</DATE_DEBUT_EXEC>"
      +"\n"+pad+"     <CODE_RETOUR>"
      +CODE_RETOUR+"</CODE_RETOUR>"
      +"\n"+pad+"     <MESSAGE>"
      +MESSAGE+"</MESSAGE>"
      +"\n"+pad+"     <NBR_ZONE>"
      +NBR_ZONE+"</NBR_ZONE>"
    ;
    if (def_projection != null) res+="\n"+pad+def_projection.toXmlString(padding+5);
    res += "\n"+pad+"</Projection_lancee>";
    return res;
    }

public void setCreateSynapseParameters(Object fait) {
 setFait((Projection_lanceeDAO)fait);
}


public void setDeepFait(Object fait) {
 setFait((Projection_lanceeDAO)fait);
}


public void setRechercheParameters(Object fait) {
 setFait((Projection_lanceeDAO)fait);
}


public void collabo(ArrayList<INeurone> neurones) throws Exception {
  ArrayList <Projection_lanceeDAO> table=new ArrayList<Projection_lanceeDAO>();
  for (int i=0;i<neurones.size();i++) {
    Projection_lanceeDAO projCur=(Projection_lanceeDAO) neurones.get(i);
    table.add(projCur);
  }
  CacheDaoManager.tableProjection_lancee=table;
}


public String getMapKey() {
  return ID_PROJECTION_LANCEE;
}


public boolean parseNeurone(INeurone modele) throws Exception {
  return true;
}



}


