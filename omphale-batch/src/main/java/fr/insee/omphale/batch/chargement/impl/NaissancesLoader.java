package fr.insee.omphale.batch.chargement.impl;

import fr.insee.omphale.batch.chargement.IOmphaleProcessusNAISSANCES;


/**
 *
 *	programme pour charger la table des naissances
 *  
 *	s'appuie sur interface IOmphaleProcessusNAISSANCES
 *
 *	utilise le LoaderGeneric pour exécuter le plugin
 *
 *
 */
public class NaissancesLoader implements IOmphaleProcessusNAISSANCES {
    public boolean executePlugin() throws Exception {
        String classMain="Naissances";
        return new GenericLoader(classMain).executePlugin();
    }
}
