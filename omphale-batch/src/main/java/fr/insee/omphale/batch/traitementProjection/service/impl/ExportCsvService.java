package fr.insee.omphale.batch.traitementProjection.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.IServiceBatch;
import fr.insee.omphale.batch.transversal.bean.BeanRapport;
import fr.insee.omphale.batch.transversal.exception.OmphaleConfigException;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;



/**
 * Classe de service responsable de la production des fichiers csv à partir des
 * tables Oracle résultat.
 */
public class ExportCsvService  implements IServiceBatch {
    
    private static String csvDir=null;
    private static String separateur=";";
    private static ISimpleSelectDao dao = null;
    
    
    /**
     * requete SQL paramétrée récupérant les noms des tables oracle resultats.
     */
    private static String sqlSelect="select tname from tab where instr(tname, upper('&&prefix._&&idep._csv')) > 0";
    ;

    /**
     * Méthode de service
     * @param batch
     * @throws OmphaleConfigException, OmphaleMetierException 
     * @throws OmphaleConfigException
     */
    public void executeService() throws OmphaleConfigException, OmphaleMetierException  {
      ArrayList<String> csvTables=new ArrayList<String>();
      BeanRapport beanRapport=CacheDaoManager.beanRapport;
      csvDir=beanRapport.utilisateur+"_"+beanRapport.id_projection+"_"+beanRapport.id_projection_lancee;

      File rep=new File(OmphaleBatchConfig.APPLISHARE_RacineDepotResultatProjection);

      //création du nouveau répertoire des résultats s'il n'existe pas
      if( !rep.exists()){
          rep.mkdir();
      }

      File repU=new File(OmphaleBatchConfig.APPLISHARE_RacineDepotResultatProjection+OmphaleBatchConfig.dirSeparateur+csvDir);
      //création du sous répertoire des fichiers csv.
      // on supprime le répertoire s'il s'agit d'une projection que l'on relance
	  if(repU.exists()){
		  if(repU.listFiles().length !=0){
			  for (File file : repU.listFiles()) {
					file.delete();
				  }
		  }
		  repU.delete();
	  }
      // on crée le répertoire systématiquement
	  repU.mkdir();

      String tableFicPath=OmphaleBatchConfig.APPLISHARE_RacineDepotResultatProjection+OmphaleBatchConfig.dirSeparateur+csvDir+OmphaleBatchConfig.dirSeparateur;
      
        try {
            /**
             * Instanciation du dao
             */

            dao = CacheDaoManager.simpleSelectDao;
            
            //récupération des noms des tables oracle résultat.
            dao.execute(sqlSelect.replace("&&prefix.",beanRapport.prefix).replace("&&idep.", beanRapport.utilisateur));
            while( dao.nextRow()) {
                csvTables.add(dao.getString("TNAME"));
            }
            //pour chaque nom de table, export des données dans fichier csv.
            for (int i=0;i<csvTables.size();i++) {
                Writer fichierCsv = new FileWriter(tableFicPath+csvTables.get(i)+".csv");
                
                export(csvTables.get(i), fichierCsv);
                fichierCsv.close();
            }
 
        } catch (Exception t) {
            t.printStackTrace();
            throw new OmphaleConfigException("Probleme export csv service",t);
                 
        }
        
     }
    /**
     * Méthode qui produit un fichier CSV pour une table Oracle.
     * @param table
     * @param fic
     * @throws GroupeJavaDaoException
     * @throws IOException
     */
    public static void export(String table, Writer fic) throws  GroupeJavaDaoException, IOException {
    	// cette variable est le nom de la table auquelle on enlève l'idep
    	// car les contrôles sur la précision des chiffres après les virgules s'appuient sur le nom de la table
    	// l'idep n'est donc pas un critère de recherche
    	String tablePrecision = table.substring(9);
        dao.execute("select * from "+table);
        StringBuffer line=null;
        String sep=null;
        ArrayList<String> cols=null;
        ArrayList<String> types=null;
        cols=dao.getColumnNames();
        types=dao.getColumnTypes();
        line=new StringBuffer(); 
        sep="";
        for (int i =0;i<cols.size();i++) {
             line.append(sep).append(cols.get(i));
             if (i==0) sep=separateur;
        }
        fic.write(line+"\r\n");
 
        while( dao.nextRow()) {
            cols=dao.getColumnValues();
            line=new StringBuffer(); 
            sep="";
            for (int i =0;i<cols.size();i++) {
                //On arrondit les valeurs flottantes (number sous Oracle) selon une précision qui dépend de la table (de son nom).
                 if (!("NUMBER0".equals(types.get(i)))) line.append(sep).append(cols.get(i));
                 else {
                   double prec=0.0;  
                   if 
                   (tablePrecision.contains("QD") || tablePrecision.contains("QE") || tablePrecision.contains("QF") || tablePrecision.contains("QACT") || tablePrecision.contains("QMEN")) prec=6.0;
                   else if (tablePrecision.contains("RATIO_NAI")) prec=3.0;
                   line.append(sep).append(arrondir(cols.get(i), prec));
                 }
                 if (i==0) sep=separateur;
            }
            fic.write(line+"\r\n");
        }
    }
    /**
     * Méthode qui arrondit une valeur double selon la précision demandée.
     * @param val valeur flottante fournie en string.
     * @param prec précision demandée.
     * @return la valeur double arrondie sous forme de string.
     */
    private static String arrondir(String val, double prec) {
        if (val==null) return "0";
        if (!(val.contains("."))) return val;
        Double dval= Double.parseDouble(val);
        dval=(Math.round(dval * Math.pow(10.0,prec)) / Math.pow(10,prec));
        if (prec == 0.0) {
            Integer ival=dval.intValue();
            return ival.toString();
        }
        else return dval.toString();
    }

}

