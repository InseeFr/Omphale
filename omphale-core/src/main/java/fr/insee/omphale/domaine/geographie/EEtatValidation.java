package fr.insee.omphale.domaine.geographie;

import fr.insee.omphale.dao.IStringValuedEnum;

/**
 * permet d'indiquer l'état valide ou non valide d'une commune, zone, zonage 
 * ou d'un département
 */
public enum EEtatValidation implements IStringValuedEnum {
	NON_VALIDE(0, "Non valide"),
	VALIDE(9, "Valide");

	private int value;
	private String libelle;

	private EEtatValidation(int value, String libelle) {
		this.value = value;
		this.libelle = libelle;
	}

	public String getValue() {
		return "" + value;
	}

	public String getLibelle() {
		return libelle;
	}

	public EEtatValidation get(int value) {
		for (EEtatValidation etat : EEtatValidation.values()) {
			if (etat.value == value) {
				return etat;
			}
		}
		return null;
	}

}
