package fr.insee.omphale.generationDuPDF.domaine.geographie;

public class Departement {
	private String id;
	private String nom;
	private String libelle;

	public Departement() {

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
	
	public String getIdEtLibelle(){
		return this.getId() + " - " + this.getLibelle();
	}

}
