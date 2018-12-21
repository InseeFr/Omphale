package fr.insee.omphale.batch;

public enum ECodeRetour {

    EXECUTION_CORRECTE(0, "Exécution correcte"), EXECUTION_CORRECTE_AVERT_TECH(
            201, "Exécution correcte avec des avertissements techniques"), ECHEC(
            202, "échec de l'exécution"), RIEN_A_FAIRE(10,
            "Aucune projection à traiter");
    private int code;
    private String libelle;

    ECodeRetour(int code, String lib) {
        this.code = code;
        this.libelle = lib;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getCode() {
        return code;
    }
}
