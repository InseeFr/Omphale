package fr.insee.omphale.batch;


public enum EBatchs {
	BATCH_TRAITEMENT_PROJECTION("BATCH_TRAITEMENT_PROJECTION", "traiter une projection",
            "TraiterUneProjection", "fr.insee.omphale.batch.lanceurs"),
    BATCH_CHARGEMENT("BATCH_CHARGEMENT", "Charge en BDD les nouvelles donnees du RP et de l etat civil",
                    "TraiteChargementRPetEtatCivil", "fr.insee.omphale.batch.lanceurs");
	
	
    final private static String ENUMNAME = "EBatchs";
    /**
     * nom interne du batch
     */
    private String codeDuBatch;

    /**
     * intitulé
     */
    private String lib;

    /**
     * nom de la classe comportant la méthode "execute"
     */
    private String nomClasse;

    /**
     * nom du package
     */
    private String nomPackage;

    EBatchs(String code, String lib, String classe, String nomPackage) {
        this.codeDuBatch = code;
        this.lib = lib;
        this.nomClasse = classe;
        this.nomPackage = nomPackage;
    }

    public String getValue() {
        return this.codeDuBatch;
    }

    public String getCode() {
        return codeDuBatch;
    }

    public String getLib() {
        return lib;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public String getNomPackage() {
        return nomPackage;
    }

    public static EBatchs getEnum(String code) throws Exception  {
        for (EBatchs uneEnum : EBatchs.values()) {
            if (code.equals(uneEnum.codeDuBatch)) {
                return uneEnum;
            }
        }
        throw new Exception("Erreur suite lancement Batch par Lanceur"
        		+ "enum=" + ENUMNAME + ",code=" + code);
    }
}
