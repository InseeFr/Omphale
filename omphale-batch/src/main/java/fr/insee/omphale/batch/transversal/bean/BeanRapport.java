package fr.insee.omphale.batch.transversal.bean;

import fr.insee.omphale.batch.transversal.util.Chronometre;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * Classe bean dédiée au compte rendu du déroulement d'une projection.
 */
public class BeanRapport {
    /**
     * identifiant de la definition_projection.
     */
    public String id_projection;

    /**
     * identifiant dans la file d'attente.
     */
    public String id_projection_lancee;

    public String prefix="ZP";
    /**
     * fiche identite de la projection
     */
    public String ficheProjection;
    /**
     * fiche identite de la projection au format HTML
     */
    public String ficheProjectionHTML;
    /**
     * nom utilisateur ou idep
     */
    public String utilisateur;
    /**
     * email emetteur;
     */
    public String emailFrom;
    /**
     * email destinataire;
     */
    public String emailDest;
    /**
     * email copie;
     */
    public String emailCopie;
    
    /**
     * permet de savoir s'il reste des projections à traiter.
     */
    public int afaire = 0;

    /**
     * permet de situer l'étape générique où a eu lieu une erreur éventuelle.
     */
    public int etape = 0;
    
    /**
     * permet de situer l'étape générique où a eu lieu une erreur éventuelle.
     */
    public int etape2 = 0;

    /**
     * contient le Status de l'erreur.
     */
    public int code = 0;

    /**
     * Contient un message de compte rendu d'erreur.
     */
    public String message = "Ok";

    /**
     * Contient le message propre à l'erreur.
     */
    public String erreurProjection = "";

    /**
     * Informations destinées au module de production des résultats (graphiques
     * et données).
     */
    public BeanParametresResultat results = null;

    /**
     * Exception déclenchée par l'erreur.
     */
    public Throwable exception = null;
    
    /**
     * Chronometre pour stats temps de reponse.
     */
    public Chronometre montre=new Chronometre();
    public String toString() {
        String res="";
        res+="\r\nemailCopie:"+emailCopie
        +"\r\nemailDest:"+emailDest
        +"\r\nemailFrom:"+emailFrom
        +"\r\nutilisateur:"+utilisateur
        +"\r\nmessage:"+message
        +"\r\ncode:"+code
        ;
        
        return res;
    }

}
