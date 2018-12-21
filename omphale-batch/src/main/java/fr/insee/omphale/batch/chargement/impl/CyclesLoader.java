package fr.insee.omphale.batch.chargement.impl;

import fr.insee.omphale.batch.chargement.IOmphaleProcessusCYCLES;

/**
 *
 *	programme pour charger des nouveaux cycles
 *	ce processus effectue des traitement à partir des données de Pop_legale
 *  cree des données de n-2 a n + 2 avec n etant une annee de cycle
 *  
 *	s'appuie sur interface IOmphaleProcessusCYCLES
 *
 *	utilise le LoaderGeneric pour exécuter le plugin
 *
 *
 */
public class CyclesLoader  implements IOmphaleProcessusCYCLES{
    public boolean executePlugin() throws Exception {
        String classMain="Cycles";
        return new GenericLoader(classMain).executePlugin();
    }
}
