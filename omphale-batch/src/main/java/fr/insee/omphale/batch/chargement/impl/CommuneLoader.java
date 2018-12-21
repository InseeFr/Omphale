

package fr.insee.omphale.batch.chargement.impl;

import fr.insee.omphale.batch.chargement.IOmphaleProcessusCOMMUNE;



/**
 *	programme pour charger les nouvelles communes
 *	s'appuie sur interface IOmphaleProcessusCOMMUNE
 *
 *	utilise le LoaderGeneric pour exécuter le plugin
 *
 *
 */
public class CommuneLoader implements IOmphaleProcessusCOMMUNE {

	
    public boolean executePlugin() throws Exception {
        String classMain="Commune";
        return new GenericLoader(classMain).executePlugin();
    }

}
