package fr.insee.omphale.batch.traitementProjection.service.impl;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.IServiceBatch;
import fr.insee.omphale.batch.transversal.exception.OmphaleConfigException;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleDao;


public class UpdateBaseService implements IServiceBatch{
    private static ISimpleDao simpleDao = null;
    
    /**
     * Méthode de service.
     * @throws OmphaleConfigException erreur grave.
     */
    
    public void executeService() throws OmphaleConfigException,
            OmphaleMetierException {
            try {
                
                simpleDao =  CacheDaoManager.simpleDao;
            if (CacheDaoManager.getFirstProjectionLancee() == null)
                return;
            
                simpleDao.executeUpdate(CacheDaoManager.getFirstProjectionLancee().getSqlFin());
            } catch (Exception e) {
                e.printStackTrace();
                throw new OmphaleConfigException("Probleme lors de la mise a jour finale de la base",e);
            }
            
        
        
    }

}
