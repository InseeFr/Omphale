


package fr.insee.omphale.batch.chargement.impl;

import fr.insee.omphale.batch.chargement.IOmphaleProcessusCOUPLE_COM_LIEE;


/**
 *
 *	programme pour charger les nouveaux couples de communes liees
 *	s'appuie sur interface IOmphaleProcessusCOUPLE_COM_LIEE
 *
 *	utilise le LoaderGeneric pour exécuter le plugin
 *
 *
 */
public class Couple_com_lieeLoader implements IOmphaleProcessusCOUPLE_COM_LIEE {

    public boolean executePlugin() throws Exception {
        String classMain="Couple_com_liee";
        return new GenericLoader(classMain).executePlugin();
    }

}
