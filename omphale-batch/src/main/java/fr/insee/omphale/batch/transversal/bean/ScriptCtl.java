package fr.insee.omphale.batch.transversal.bean;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ScriptCtl {
    
    private String texteCtl;

    public String getTexteCtl() {
        return texteCtl;
    }

    public void setTexteCtl(String texteCtl) {
        this.texteCtl = texteCtl;
    }
    
    /**
     * Substitue les parametres formels et renvoie le texte
     *
     * @param listeParametres
     *            la map des parametres formels à substituer.
     * @return l'instruction après substitution des parametres formels.
     */
    public String getOrdreCtl(Map<String, String> listeParametres) {
        String instruction = getTexteCtl();
        if (listeParametres != null) {
            // récupération de la liste des paramètres
            Set<String> listeCle = listeParametres.keySet();
            // pour chaque paramètre
            // remplacement du paramètre par sa valeur
            
            for (String cle : listeCle) {
                instruction = StringUtils.replace(instruction, cle,
                        listeParametres.get(cle));
            }
        }
        return instruction;
    }

    

}
