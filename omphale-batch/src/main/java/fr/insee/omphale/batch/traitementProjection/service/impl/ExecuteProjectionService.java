package fr.insee.omphale.batch.traitementProjection.service.impl;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.IServiceBatch;
import fr.insee.omphale.batch.transversal.exception.OmphaleConfigException;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.generationDuPDF.exception.OmphalePopulationNegativeException;

public class ExecuteProjectionService implements IServiceBatch{

    /**
     * lance l'exécution du traitement d'une projection
     * @throws OmphalePopulationNegativeException 
     */
    public void executeService() throws OmphaleConfigException,
            OmphaleMetierException, OmphalePopulationNegativeException {
        CacheDaoManager.executeProjection(CacheDaoManager.getFirstProjectionLancee());
        
    }

}
