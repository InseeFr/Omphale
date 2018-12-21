package fr.insee.omphale.batch.chargement;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.impl.BatchProjectionService;


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
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        
     int rc=0;
     try {
     
         rc=new BatchProjectionService().execute();
     endProgram(rc);
     } catch (Throwable t) {
         t.printStackTrace();
         endProgram(CacheDaoManager.STATUS_ERROR_CONFIG);
     }
    }
    
    /**
     * Méthode de sortie du batch.
     * Le status de sortie permet de décider de la suite.
     * @param status
     */
    private static void endProgram(int status) {
        System.exit(status);
    }


}
