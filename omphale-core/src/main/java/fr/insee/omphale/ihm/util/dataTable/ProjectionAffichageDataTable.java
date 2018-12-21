package fr.insee.omphale.ihm.util.dataTable;

import java.util.Date;

public class ProjectionAffichageDataTable {

	public ProjectionAffichageDataTable() {
	}
	
	
	
	public ProjectionAffichageDataTable(Integer id, String nom, Boolean standard,
			Boolean validation, int englobante, String libelle,
			Date dateCreation) {
		super();
		this.id = id;
		this.nom = nom;
		this.standard = standard;
		this.validation = validation;
		this.englobante = englobante;
		this.libelle = libelle;
		this.dateCreation = dateCreation;
	}



	private Integer id;
	
	private String nom;
	
	private Boolean standard;

	private Boolean validation;
	
	private int englobante;
	
	private String libelle;

	private Date dateCreation;
	
	private String figeable;

	public Integer getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public Boolean getStandard() {
		return standard;
	}

	public Boolean getValidation() {
		return validation;
	}

	public int getEnglobante() {
		return englobante;
	}

	public String getLibelle() {
		return libelle;
	}

	public Date getDateCreation() {
		return dateCreation;
	}
	
	public String getDateCreationAffichageDataTable(){
		return AffichageUtilDataTable.getDateAAfficher(dateCreation);
	}
	
	public String getFigeable() {
		return figeable;
	}

	public void setFigeable(String figeable) {
		this.figeable = figeable;
	}
}


