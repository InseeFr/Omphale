package fr.insee.omphale.batch.chargement.impl;

import fr.insee.omphale.batch.chargement.IOmphaleProcessusPOP_LEGALE;



/**
 *
 *	programme pour charger la table de la population legale
 *	ces donnees seront chargees dans d'autres tables lors de la creation des cycles
 *	va de pair avec le plugin LoaderCycles
 *  
 *	s'appuie sur interface IOmphaleProcessusPOP_LEGALE
 *
 *	utilise le LoaderGeneric pour exécuter le plugin
 *
 *
 */
public class PopLegaleLoader  implements IOmphaleProcessusPOP_LEGALE
{

    public boolean executePlugin() throws Exception {
        String classMain="Pop_Legale";
        return new GenericLoader(classMain).executePlugin();
    }
}
