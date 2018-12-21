package fr.insee.omphale.batch.chargement.impl;

import fr.insee.omphale.batch.chargement.IOmphaleProcessusPURGE_PROJECTION;



/**
 *
 *	programme pour purger la table des projections lancees
 *	supprime les projections lancees plus anciennes que le parametre indique
 *	dans le fichier a traiter
 *  
 *	s'appuie sur interface IOmphaleProcessusPURGE_PROJECTION
 *
 *	utilise le LoaderGeneric pour exécuter le plugin
 *
 *
 */
public class PurgeProjectionLoader  implements IOmphaleProcessusPURGE_PROJECTION
{

    public boolean executePlugin() throws Exception {
        String classMain="Purge_Projection";
        return new GenericLoader(classMain).executePlugin();
    }
}
