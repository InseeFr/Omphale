package fr.insee.omphale.batch.traitementProjection.service.impl;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.IServiceBatch;
import fr.insee.omphale.batch.transversal.exception.OmphaleConfigException;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.exception.OmphaleConfigResultException;
import fr.insee.omphale.generationDuPDF.exception.OmphalePopulationNegativeException;
import fr.insee.omphale.generationDuPDF.exception.OmphaleResultException;
import fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService;

/**
 * Classe de service responsable de la production des graphiques de la
 * projection dans un fichier PDF, sous-traitée au module résultat.
 */
public class GraphiquePdfService implements IServiceBatch{
	
    /**
     * Méthode de service.
     * @param beanParametresResultat contient les paramètres pour le module résultat.
     * @throws OmphaleConfigException en cas d'erreur grave.
     * @throws OmphaleMetierException en cas d'erreur métier.
     */
    public void executeService() throws OmphaleConfigException, OmphaleMetierException, OmphalePopulationNegativeException {
        BeanParametresResultat beanParametresResultat=CacheDaoManager.beanRapport.results;
        
        LancementPdfService lancementPdfService = new LancementPdfService();
       
        try {
        	lancementPdfService.lancement(beanParametresResultat);
        } catch (OmphaleResultException e) {
            e.printStackTrace();
            throw new OmphaleMetierException("Probleme Graphique pdf service",e);
        } catch(OmphaleConfigResultException c) {
              c.printStackTrace();
              throw new OmphaleConfigException("Probleme Graphique pdf service",c);
        } catch (OmphalePopulationNegativeException p) {
        	p.printStackTrace();
        	throw new OmphalePopulationNegativeException("Problème : présence de populations négatives, contactez le Psar", p);   
        } catch(Exception t) {
            t.printStackTrace();
            throw new OmphaleConfigException("Probleme Graphique pdf service",t);
        }
    }

}
