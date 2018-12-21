package fr.insee.omphale.generationDuPDF.domaine.geographie;

import java.io.Serializable;
import java.util.Set;




@SuppressWarnings("serial")
public class Zonage implements Serializable {
	private String id;
	private String nom;
	private String libelle;
	private int anneeValidation;
	private Set<Zone> zones;

	public Zonage() {

	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public int getAnneeValidation() {
		return anneeValidation;
	}

	public void setAnneeValidation(int anneeValidation) {
		this.anneeValidation = anneeValidation;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public Set<Zone> getZones() {
		return zones;
	}

	public void setZones(Set<Zone> zones) {
		this.zones = zones;
	}
	
	
	
	
	
}
