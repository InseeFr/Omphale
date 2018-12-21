package fr.insee.omphale.batch.traitementProjection.service.impl;

import java.util.ArrayList;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;

public class BatchProjectionService {
    
    /**
     * initialisation du cache dao manager
     * exécution de la boucle des différentes étapes du traitement d'une projection
     * 
     * @return int
     */
    public int execute() {
        int rc=CacheDaoManager.setUp();
        if (rc != CacheDaoManager.STATUS_SUCCES) {
            return rc;
        }
         
        ArrayList<OrdonnanceurService> tableProceduralService=CacheDaoManager.tableProceduralServiceTraitement;
        for (int i=0;i<tableProceduralService.size();i++) {
            OrdonnanceurService pgm=tableProceduralService.get(i);
            rc=pgm.execute();
            if (rc != CacheDaoManager.STATUS_SUCCES) break;
        }
        tableProceduralService=CacheDaoManager.tableProceduralServiceFin;
        for (int i=0;i<tableProceduralService.size();i++) {
            OrdonnanceurService pgm=tableProceduralService.get(i);
            pgm.execute();
        }
        return CacheDaoManager.beanRapport.code;
        
    }

}
