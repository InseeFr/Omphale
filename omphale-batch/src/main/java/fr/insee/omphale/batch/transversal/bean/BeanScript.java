package fr.insee.omphale.batch.transversal.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe de bean dédiée à la spécification d'un script à exécuter.
 *
 */
public class BeanScript {

    public BeanScript(String scriptSql) {
        super();
        this.scriptSql = scriptSql;
        this.listeParametres=new HashMap<String, String>();
    }
    /**
     * Map des paramètres du script (clé, valeur).
     */
    private Map<String, String> listeParametres;
    /**
     * Nom du script (relativement au package père des scripts).
     */
    private String scriptSql;
      
    
    public Map<String, String> getListeParametres() {
        return listeParametres;
    }

    public String getScriptSql() {
        return scriptSql;
    }
    public String toString() {
        String res="";
        res+="\nScript SQL de projection"
           +"\n  Nom: "+scriptSql
           +"\n  Parametres"
           ;
        for (String nom : listeParametres.keySet()) {
            res+="\n    "+nom+":"+listeParametres.get(nom);
        }
        res+="\n  /Parametres";
        res+="\n/Script SQL de projection";
        return res;
    }
    
}
