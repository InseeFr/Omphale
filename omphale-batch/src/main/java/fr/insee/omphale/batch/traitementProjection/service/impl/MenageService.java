package fr.insee.omphale.batch.traitementProjection.service.impl;



import java.util.ArrayList;
import java.util.Collection;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.IServiceBatch;
import fr.insee.omphale.batch.transversal.bean.BeanRapport;
import fr.insee.omphale.batch.transversal.exception.OmphaleConfigException;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.ContexteApplication;

/**
 * Classe de service responsable de la suppression des tables et vues Oracle
 * créees par les scripts lors de l'exécution d'une projection.
 */
public class MenageService implements IServiceBatch{
	

    private static ISimpleSelectDao dao = null;

    private static ISimpleDao simpleDao = null;

    /**
     * requête sql générant les requêtes sql de suppression des tables ou vues
     * créées par les scripts de projection.
     */
    private static String sqlSelect = "select 'DROP ' || tabtype || ' ' || tname  requete from tab where instr(tname, upper('&&prefix._&&idep._')) =1 order by tabtype desc";
    private static String sqlSelectFin = "select count(*) RESULTAT from tab where instr(tname, upper('&&prefix._&&idep._')) =1 ";
    
    
    /**
     * Méthode de service.
     * @param batch la projection.
     * @throws OmphaleConfigException erreur grave.
     */
    
    public void executeService() throws OmphaleConfigException, OmphaleMetierException {
        Collection<String> colRequete = new ArrayList<String>();
        BeanRapport beanRapport=CacheDaoManager.beanRapport;
        try {
            
            dao = CacheDaoManager.simpleSelectDao;
            
            dao.execute(sqlSelect.replace("&&prefix.", beanRapport.prefix)
                    .replace("&&idep.", beanRapport.utilisateur));
            while (dao.nextRow()) {
                colRequete.add(dao.getString("REQUETE"));
                colRequete.add("COMMIT");
            }
            if (colRequete.size() > 0) {
                simpleDao = (ISimpleDao) ContexteApplication.getDao(
                        "simpleDao", OmphaleBatchConfig.nomConnexion);
                simpleDao.executeSQL(colRequete);
            }
            
            // verification de la suppression des tables temporaires
            dao.execute(sqlSelectFin.replace("&&prefix.", beanRapport.prefix)
                    .replace("&&idep.", beanRapport.utilisateur));

        } catch (Exception t) {
            throw new OmphaleConfigException(
                    "Probleme de dao lors de suppression des tables de travail",
                    t);
        }

    }
}

