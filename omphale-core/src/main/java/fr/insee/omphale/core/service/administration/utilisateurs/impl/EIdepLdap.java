package fr.insee.omphale.core.service.administration.utilisateurs.impl;



public enum EIdepLdap {
	
	idepNonPresentDansLdap("idepNonPresentDansLdap","verificationIDEP", "error.idepNonPresentDansLDAP"),
	idepDejaPresentEnBase("idepDejaPresentEnBase","verificationIDEP", "error.idepDejaPresentEnBase"),
	idepNonPresentDansGroupeLdap("idepNonPresentDansGroupeLdap","verificationIDEP", "error.idepNonPresentDansGroupeLdap");
	
	
    final private static String ENUMNAME = "EIdepLdap";

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




    EIdepLdap(String nomEnum, String nomChampStruts, String nomProperty) {
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

    public static EIdepLdap getEnum(String nomEnumARechercher) throws Exception  {
        for (EIdepLdap uneEnum : EIdepLdap.values()) {
            if (nomEnumARechercher.equals(uneEnum.nomEnum)) {
                return uneEnum;
            }
        }
        throw new Exception("Erreur suite à la recherche de l'enum idep ldap"
        		+ "enum=" + ENUMNAME + ",nomChampStruts=" + nomEnumARechercher);
    }
}
