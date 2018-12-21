package fr.insee.omphale.batch.chargement.impl;

import java.io.File;

import fr.insee.omphale.batch.chargement.chargementJDBC.impl.ChargementJDBCImpl;
import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.transversal.util.OmphaleGestionFile;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.ContexteApplication;

/**
 *	la plupart des plugins de chargement herite de cette classe
 *	
 *	trouve le bon plugin en fonction de la classMain (le nom du fichier)
 *	initialise les paramètres du script notamment pour le fichier de controle ctl de SQLLoader
 *	lance le chargement fichier avec SQLLoader
 *	lance le script de fin qui contient le traitement a effectuer sur les donnees injectees
 *
 *
 */
public class GenericLoader extends AbstractChargement {

    private String classMain;

    public GenericLoader(String classMain) {
        super();
        this.classMain = classMain;
    }

    public boolean executePlugin() throws Exception {
        try {
            preparePlugin(classMain);
            CacheDaoManager.executeProgrammeScript(scriptInit);
            ChargementJDBCImpl chargementJDBC = new ChargementJDBCImpl();
            chargementJDBC.chargementFichierVersUneTable(ContexteApplication.getConnectionManager(), "ZZ_TEMP_" + classMain,  repTraite + File.separator
                  + classMain.toLowerCase() + ".csv");        
            CacheDaoManager.executeProgrammeScript(scriptEnd);
            
        } catch (Exception e) {
            CacheDaoManager.beanRapport.erreurProjection = e.getMessage();
            File fileTraite = new File(repTraite + File.separator
                    + classMain.toLowerCase() + ".csv");
            if (OmphaleGestionFile.exist(fileTraite)) {
                if (!OmphaleGestionFile.deplacerForce(fileTraite, new File(
                        repErreur + File.separator + classMain.toLowerCase() + ".csv"))) {
                    CacheDaoManager.beanRapport.erreurProjection += "\r\nFichier absent ou impossible a deplacer en erreur. ";
                    throw new Exception(
                            "Fichier absent ou impossible a deplacer en erreur",
                            e);
                } else throw e;
            } else
                throw e;
        }
        return true;
    }

}
