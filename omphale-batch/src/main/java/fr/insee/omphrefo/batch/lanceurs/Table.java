package fr.insee.omphrefo.batch.lanceurs;

import java.util.Map;

public class Table {

	private String nom;
	private Map<String, Colonne> colonnes;
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public Map<String, Colonne> getColonnes() {
		return colonnes;
	}
	public void setColonnes(Map<String, Colonne> colonnes) {
		this.colonnes = colonnes;
	}

	
	
	
	
}
