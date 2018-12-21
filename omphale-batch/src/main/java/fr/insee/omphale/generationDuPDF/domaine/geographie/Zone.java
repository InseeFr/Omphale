package fr.insee.omphale.generationDuPDF.domaine.geographie;

import java.io.Serializable;



@SuppressWarnings("serial")
public class Zone implements Serializable{
	private String id;
	private String nom;
	private String libelle;


	public Zone() {

	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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


	
	
	
}
