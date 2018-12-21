package fr.insee.omphale.batch.chargement.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.configuration.Configuration;

import fr.insee.config.InseeConfig;
import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.transversal.bean.BeanScript;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.batch.transversal.util.OmphaleGestionFile;

public abstract class AbstractChargement {

    protected static Configuration config = InseeConfig.getConfig();

   
    protected String repACharger = config
    .getString("fr.insee.omphale.chargement.dir.a_charger");
    
    protected String repTraite = config
    .getString("fr.insee.omphale.chargement.dir.traite");
    
    protected String repErreur = config
    .getString("fr.insee.omphale.chargement.dir.erreur");
 
    protected ArrayList<BeanScript> scriptInit = new ArrayList<BeanScript>();

    protected ArrayList<BeanScript> scriptEnd = new ArrayList<BeanScript>();
    

    
    
    /**
     * initialise toutes les donnees utiles au script init 
     * + les parametres stat liees
     * 	- aux ages 
     * -  au zones d echanges
     * - aux fichiers et dossiers bad, dsc
     * 
     * @param classPrincipale
     * @throws Exception void
     */
    public void preparePlugin(String classPrincipale) throws Exception {
        CacheDaoManager.reinit();
        CacheDaoManager.beanRapport.erreurProjection="Ok";
        CacheDaoManager.beanRapport.code=0;
        CacheDaoManager.beanRapport.ficheProjection="";

        File fileACharger = new File(repACharger + File.separator
                + classPrincipale.toLowerCase() + ".csv");
        if (OmphaleGestionFile.exist(fileACharger)) {
            if (!OmphaleGestionFile.deplacerForce(fileACharger, new File(
                    repTraite + File.separator + classPrincipale.toLowerCase() + ".csv"))) {
                throw new Exception("Fichier impossible à deplacer dans traite");

            }
        }

        BeanScript bean = new BeanScript(OmphaleBatchConfig.racine_script_chargement);
        Map<String, String> map = bean.getListeParametres();
        scriptInit.add(bean);
        
        FileReader in = new FileReader(repTraite + File.separator
                + classPrincipale.toLowerCase() + ".csv");
        BufferedReader buf = new BufferedReader(in);
        String chaine = buf.readLine();
        buf.close();
        in.close();
        if (chaine != null) {
             String[] rec=(chaine+",FIN_ENREG").split(",");
             for (int i=0;i<rec.length;i++) {
            	 map.put("&&col"+(i+1)+".", rec[i].toUpperCase());
             }
        
        }
        //Parametres generaux
        map.put("&&age_last.", OmphaleBatchConfig.age_last);
        map.put("&&mere_inf.", OmphaleBatchConfig.mere_inf);
        map.put("&&mere_sup.", OmphaleBatchConfig.mere_sup);
        map.put("&&ancolq.", "5");
        
        map.put("&&zonage_gros.", OmphaleBatchConfig.nbrZoneGrosZonage);
        map.put("&&projection_annul.", OmphaleBatchConfig.codeAnnulationProjection+"");
        
        scriptInit.add(new BeanScript(classPrincipale + "." + classPrincipale
                + "_init.sql"));
        scriptEnd.add(bean);
        scriptEnd.add(new BeanScript(classPrincipale + "." + classPrincipale
                + "_end.sql"));

    }
    
    
   

}
