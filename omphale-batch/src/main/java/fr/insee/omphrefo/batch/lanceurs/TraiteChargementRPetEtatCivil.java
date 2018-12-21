package fr.insee.omphrefo.batch.lanceurs;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.LogManager;

import fr.insee.config.InseeConfig;
import fr.insee.omphale.batch.chargement.IOmphaleProcessus;
import fr.insee.omphale.batch.chargement.service.impl.MailService;
import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.transversal.exception.OmphaleException;
import fr.insee.omphale.batch.transversal.util.OmphaleGestionFile;
import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.ClasspathException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.PluginInspector;
import fr.insee.omphrefo.batch.Batch;
import fr.insee.omphrefo.batch.ECodeRetour;
import fr.insee.omphrefo.batch.RetourBatch;



/**
 * Classe principale de lancement des processus de traitement des fichiers
 * Omphale. Les etapes d'execution sont les suivantes :<br>
 * <ul>
 * <li>Examen du classpath a la recherche des processus implementant
 * l'interface IOmphaleProcessus.</li>
 * <li>Boucle d'execution sur chacun des processus trouves.</li>
 * </ul>
 *
 */
public class TraiteChargementRPetEtatCivil implements Batch{
	
    public static final String PLUGIN_PACKAGE = "fr.insee.omphale";

    /**
     * Le statut retourne au systeme d'exploitation en cas d'execution
     * correct.
     */
    public static final int STATUS_OK = CacheDaoManager.STATUS_SUCCES;

    /**
     * Le statut retourne au systeme d'exploitation en cas d'erreur de
     * configuration.
     */
    public static final int STATUS_ERROR_CONFIG = CacheDaoManager.STATUS_ERROR_CONFIG;

    /**
     * Le statut retourne au systeme d'exploitation si le chargement d'une (ou
     * plus) des nomenclatures a abouti à un echec.
     */
    public static final int STATUS_ERROR = CacheDaoManager.STATUS_ERROR_CONFIG;

    /** Le statut retourne en cas d'erreur technique metier. */
    
    public static final int STATUS_ERROR_METIER = CacheDaoManager.STATUS_ERROR_OMPHALE;



    private static Configuration config = InseeConfig.getConfig();
    
      
    private static  String repACharger = config
    .getString("fr.insee.omphale.chargement.dir.a_charger");
    
    private static String repATraiter = config
    .getString("fr.insee.omphale.chargement.dir.a_traiter");
    private static String repErreur = config
    .getString("fr.insee.omphale.chargement.dir.erreur");
    
    RetourBatch retour ;
    int codeStatus=0;
    
    
    
    public RetourBatch executer(String[] args) throws OmphaleException {
        retour = new RetourBatch(
                ECodeRetour.EXECUTION_CORRECTE.getCode(), "Tout est OK");
     
        try {

        	codeStatus= lancementDuBatch();
            retour = endProgram(codeStatus);
            
        } catch (Throwable t) {
            t.printStackTrace();
            // gestion du 202
            endProgram(CacheDaoManager.STATUS_ERROR_CONFIG);
        }

        return retour;
    }

    /**
     * Provoque la fin de l'execution du programme courant. <br>
     * Cette méthode loggue les informations nécessaires, effectue le nettoyage
     * requis (en particulier fermeture des logs) et sort de la JVM avec le code
     * retour donné.
     *
     * @param status
     *            le code retour final.
     */
    private RetourBatch endProgram(int status) {

        if (status != STATUS_OK) {
	        CacheDaoManager.beanRapport.code=status;
        	retour = new RetourBatch(ECodeRetour.ECHEC);
        }
        else if (status == STATUS_OK) {
	        CacheDaoManager.beanRapport.code=status;
	    	retour = new RetourBatch(ECodeRetour.EXECUTION_CORRECTE);
        }
        MailService mail=new MailService();
        try {
            mail.executeService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogManager.shutdown();
        return retour;
    }
    /**
     * Methode principale du programme batch. <br>
     * Cette methode examine le classpath à la recherche des classes
     * implementant l'interface IOmphaleProcessus, puis les execute.
     *
     * @param args
     *            les arguments de la ligne de commande
     */
    public int lancementDuBatch() {
        
        @SuppressWarnings("rawtypes")
		Iterator plugins;
        boolean traite = false;

        String laClasse=null;
        String fichiersExecutes = " Les fichiers ";
        //Si absence de fichier
        if (new File(repATraiter)== null || new File(repATraiter).listFiles()== null || new File(repATraiter).listFiles().length == 0) {
        	CacheDaoManager.beanRapport.erreurProjection+="Absence de fichiers à traiter";
        	return STATUS_OK;
        }
        while (new File(repATraiter)!= null && new File(repATraiter).listFiles()!= null && new File(repATraiter).listFiles().length > 0) {
	        try {
	        	// récupère le fichier à traiter, si plusieurs fichiers dans dossier aTraiter
	            laClasse=getFichier();
	        } catch (Exception e) {
	        	e.printStackTrace();
	            CacheDaoManager.beanRapport.erreurProjection="Une erreur s'est produite lors de la recherche des fichiers a charger ";
	            return STATUS_ERROR_CONFIG;
	        }
	        if (laClasse==null) {
	        	// si pas de fichier à traiter donc classe null, arrête le procèdure de chargement
	            CacheDaoManager.beanRapport.erreurProjection="Il n'y a pas de fichier à traiter ou le chemin des fichiers à traiter est incorrect";
	
	            return STATUS_OK;
	        }
	        
	        // récupération du nom de l'interface à partir du nom du fichier à traiter
	        String classPrincipale="IOmphaleProcessus"+laClasse.toUpperCase();
	        try {
	        	// Recherche du plugin qui implémente cette interface
	            plugins = new PluginInspector(PLUGIN_PACKAGE, IOmphaleProcessus.class)
	                    .plugins(classPrincipale);
	            int i=0;
	            // pour chaque plugin, executer le plugin
	            while (plugins.hasNext()) {
	                i++;
	                // si plusieurs plugins implémentent cette interface, ce n'est pas normal, donc BeanRapport et endProgram
	                if (i > 1) {
	                    CacheDaoManager.beanRapport.erreurProjection="Fichier csv avec plusieurs processus associes : ["+laClasse+".csv]";
	                    return STATUS_ERROR_METIER;
	                }
	                @SuppressWarnings("rawtypes")
					Class plugin = (Class) plugins.next();
	                try {
	                	
	                	// Instanciation du plugin
	                    IOmphaleProcessus process = (IOmphaleProcessus) plugin
	                            .newInstance();
	                    // ATTENTION, ne pas faire
	                    //traite = traite || process.executePlugin() ;
	                    // car des que traite devient, vrai, l'optimiseur java
	                    // ne va plus evaluer le reste de l'expression,
	                    // or dans ce cas, on veut quand meme executer le plugin
	                    traite = process.executePlugin() || traite;
	                    // Reinitialisation du contexte avant de re executer un
	                    // autre plugin.
	                    
	                } catch (InstantiationException e) {
	                    CacheDaoManager.beanRapport.erreurProjection="Impossible d'instancier le plugin "+e.getMessage() + " : [" + laClasse + ".csv]";

	                    return STATUS_ERROR_CONFIG;
	                } catch (IllegalAccessException e) {
	                    CacheDaoManager.beanRapport.erreurProjection="Impossible d'instancier le plugin "+e.getMessage() + " : [" + laClasse + ".csv]";

	                    return STATUS_ERROR_CONFIG;
	                } catch (Exception e) {
	                	CacheDaoManager.beanRapport.erreurProjection += "\n\rEchec avec le fichier [" + laClasse + ".csv]. ";

	                    return STATUS_ERROR_CONFIG;
	
	                } catch (Throwable e) {
	                    CacheDaoManager.beanRapport.erreurProjection="Une erreur inattendue s'est produite lors de l'execution du plugin "+e.getMessage()+ " : [" + laClasse + ".csv]";

	                    return STATUS_ERROR_CONFIG;
                }
            }
        } catch (ClasspathException e1) {
            CacheDaoManager.beanRapport.erreurProjection="Impossible d'inspecter les plugins "+e1.getMessage() + " : [" + laClasse + ".csv]";
 
            return STATUS_ERROR_CONFIG;
        }
        if (!traite) {
            CacheDaoManager.beanRapport.erreurProjection="Fichier csv sans processus associe : ["+laClasse+".csv]";

            if (!OmphaleGestionFile.deplacerForce(new File(repACharger+File.separator+laClasse+".csv"), new File(repErreur+File.separator+laClasse+".csv")))
            {
                CacheDaoManager.beanRapport.erreurProjection+="\r\nFichier impossible a deplacer dans erreur : [" + laClasse+".csv]";
               
            }
            return STATUS_ERROR_METIER;
        }
        fichiersExecutes += laClasse + ".csv ";
        //S'il n'y a plus de fichier a traiter alors
        if ( (new File(repATraiter)== null) || (new File(repATraiter).listFiles()== null) || (new File(repATraiter).listFiles().length==0) ) {
        	CacheDaoManager.beanRapport.erreurProjection+=fichiersExecutes + "ont été traités avec succès";
        }
        
        }
        return STATUS_OK;
    }

    
    
    
    
    /**
     * Permet de récupérer le fichier à traiter
     * certains chargements doivent s'effectuer absolument à la fin d'autres procèdures de chargement
     * exemple : on charge d'abord la pop_legale ensuite on charge (crée) les cycles.
     * listeRepertoireFicsDateCycles permet de trier ces fichiers
     * @return
     * @throws Exception String
     */
    private static String getFichier() throws Exception{
        
    	
        File[] lesFics=OmphaleGestionFile.listeRepertoireFicsDateDebutsFins(new File(repATraiter),new String[] {"commune"},new String[] {"cycles","couple_com_liee"});

        if (lesFics.length == 0) {
            return null;
        }
        String leFic=null;
        for (int i=0; i< lesFics.length; i++) {
            if ("csv".equalsIgnoreCase(OmphaleGestionFile.getExtension(lesFics[i]))) {
                leFic=OmphaleGestionFile.getPetitNom(lesFics[i]);
                break;
            }
        }
        if (leFic==null) {
            return null;
        }
        
        if (!OmphaleGestionFile.deplacerForce(new File(repATraiter+File.separator+leFic+".csv"), new File(repACharger+File.separator+leFic+".csv")))
        {
            throw new Exception("Fichier impossible a deplacer dans a charger : " + leFic+".csv");
           
        }
        return leFic;
    }
   

}
