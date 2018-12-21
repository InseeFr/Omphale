package fr.insee.omphale.batch.chargement.impl;




import fr.insee.omphale.batch.chargement.IOmphaleProcessusFLUX;


/**
 *
 *	programme pour charger la table des Flux
 *	concerne l'immigration et emmigration
 *	France avec etranger
 *	entre departement
 *  
 *	s'appuie sur interface IOmphaleProcessusFLUX
 *
 *	utilise le LoaderGeneric pour exécuter le plugin
 *
 *
 */
public class FluxLoader implements IOmphaleProcessusFLUX{

    

        public boolean executePlugin() throws Exception {
            String classMain="Flux";
            return new GenericLoader(classMain).executePlugin();
        }
   

}