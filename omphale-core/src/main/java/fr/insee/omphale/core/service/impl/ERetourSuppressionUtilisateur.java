package fr.insee.omphale.core.service.impl;



public enum ERetourSuppressionUtilisateur {
	
	utilisateurEncorePresentDansGroupeLDAP("utilisateurEncorePresentDansGroupeLDAP","verificationSuppressionUtilisateur", "error.suppression.utilisateur.encorePresentDansGroupeLDAP"),
	utilisateurObjetsMetiersEncorePresentsEnBDD("utilisateurObjetsMetiersEncorePresentsEnBDD","verificationSuppressionUtilisateur", "error.suppression.utilisateur.ObjetsMetiersEncorePresentsEnBDD"),
	utilisateurEncorePresentEnBDD("utilisateurEncorePresentEnBDD","verificationSuppressionUtilisateur", "error.suppression.utilisateur.EncorePresentEnBDD");
	
	
    final private static String ENUMNAME = "ERetourSuppressionUtilisateur";

    /**
     * nom enum à rechercher
     */
    private String nomEnum;
    
    
    /**
     * nom du fieldName à renvoyer pour Struts
     */
    private String nomChampStruts;

    /**
     * nom du property à rechercher pour avoir le message d'erreur
     */
    private String nomProperty;




    ERetourSuppressionUtilisateur(String nomEnum, String nomChampStruts, String nomProperty) {
    	this.nomEnum = nomEnum;
        this.nomChampStruts = nomChampStruts;
        this.nomProperty = nomProperty;
    }

    public String getValue() {
        return this.nomChampStruts;
    }
    
    public String getNomEnum(){
    	return nomEnum;
    }

    public String getNomChampStruts() {
        return nomChampStruts;
    }

    public String getNomProperty() {
        return nomProperty;
    }

    public static ERetourSuppressionUtilisateur getEnum(String nomEnumARechercher) throws Exception  {
        for (ERetourSuppressionUtilisateur uneEnum : ERetourSuppressionUtilisateur.values()) {
            if (nomEnumARechercher.equals(uneEnum.nomEnum)) {
                return uneEnum;
            }
        }
        throw new Exception("Erreur suite à la recherche de l'enum idep ldap"
        		+ "enum=" + ENUMNAME + ",nomChampStruts=" + nomEnumARechercher);
    }
}
