package fr.insee.omphale.batch.lanceurs;

import fr.insee.omphale.batch.Batch;
import fr.insee.omphale.batch.ECodeRetour;
import fr.insee.omphale.batch.RetourBatch;
import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.impl.BatchProjectionService;
import fr.insee.omphale.batch.transversal.exception.OmphaleException;

/**
 * Classe principale du moteur de projection Omphale. Le moteur est
 * essentiellement un moteur générique permettant de réaliser dynamiquement des
 * traitements batch basés sur des enchaînements de scripts SQL paramétrés
 * appliqués à une base de données Oracle. Les scripts sont écrits de manière à
 * pouvoir également être soumis (et mis au point ...) sous OSD. Les scripts
 * sont stockés dans le projet (classpath). L'exploration des packages du
 * classpath et les factory de DAO sont réalisés par un composant développé au
 * Groupe Java du CNIP dont le jar est fourni avec le projet Omphale.
 * Actuellement le moteur se limite à construire et exécuter des traitements de
 * projection Omphale, mais son architecture lui permet facilement de prendre en
 * charge d'autres types de traitements basés sur des scripts SQL.
 * @return STATUS_SUCCES quand tout s'est bien passé. Dans ce cas on exécute la
 *         projection suivante.
 * @return STATUS_RIEN_A_FAIRE quand il n'y a pas de projection à exécuter. Dans
 *         ce cas, on exécute la projection suivante après une pause.
 * @return STATUS_ERROR_OMPHALE quand une erreur métier s'est produite. Dans ce
 *         cas on exécute la projection suivante.
 * @return STATUS_ERROR_CONFIG quand une erreur grave s'est produite. Dans ce
 *         cas on arrête l'exécution des projections.
 */
public class TraiterUneProjection implements Batch{

    RetourBatch retour ;
    int codeStatus=0;
        

    
    public RetourBatch executer(String[] args) throws OmphaleException {
        retour = new RetourBatch(
                ECodeRetour.EXECUTION_CORRECTE.getCode(), "Tout est OK");
     
        try {
        	codeStatus=new BatchProjectionService().execute();
            retour = findDeProgramme(codeStatus);
            
        } catch (Throwable t) {
            t.printStackTrace();
            // gestion du 202
            findDeProgramme(CacheDaoManager.STATUS_ERROR_CONFIG);
        }

        return retour;
    }
    
    
    private RetourBatch findDeProgramme(int codeStatus) {
        
        // gestion du 202
        if(codeStatus==CacheDaoManager.STATUS_ERROR_CONFIG) {
       	 	retour = new RetourBatch(ECodeRetour.ECHEC);
        }       
        
        // gestion du 201
        if(codeStatus==CacheDaoManager.STATUS_ERROR_OMPHALE) {
       	 	retour = new RetourBatch(ECodeRetour.EXECUTION_CORRECTE_AVERT_TECH);
        }
        
        //gestion du 10
        else if(codeStatus==CacheDaoManager.STATUS_RIEN_A_FAIRE) {
       	 	retour = new RetourBatch(ECodeRetour.EXECUTION_CORRECTE.getCode());
        }
        
        return retour;
    }


}
