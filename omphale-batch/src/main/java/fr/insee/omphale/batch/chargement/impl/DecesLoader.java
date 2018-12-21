package fr.insee.omphale.batch.chargement.impl;

import fr.insee.omphale.batch.chargement.IOmphaleProcessusDECES;


/**
 *
 *	programme pour charger la table des deces
 *  
 *	s'appuie sur interface IOmphaleProcessusDECES
 *
 *	utilise le LoaderGeneric pour exécuter le plugin
 *
 *
 */
public class DecesLoader implements IOmphaleProcessusDECES {

    public boolean executePlugin() throws Exception {
        String classMain="Deces";
        return new GenericLoader(classMain).executePlugin();
    }

}
