package fr.insee.omphale.generationDuPDF.domaine.projection;

import java.io.Serializable;
import java.util.Date;



public class Projection implements Serializable {


	private static final long serialVersionUID = 2677342752303976968L;
	private Integer id;
	private int anneeReference;
	private int anneeHorizon;
	private String nom;
	private Boolean standard;
	private Boolean validation;
	private Boolean calage;
	private Boolean englobante;
	private Boolean actifs;
	private Boolean menages;
	private Boolean eleves;
	private Boolean parGeneration;
	private String libelle;
	private Date dateCreation;

	private Projection projectionEtalon;
	private Projection projectionEnglobante;


	public Projection() {}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public int getAnneeReference() {
		return anneeReference;
	}


	public void setAnneeReference(int anneeReference) {
		this.anneeReference = anneeReference;
	}


	public int getAnneeHorizon() {
		return anneeHorizon;
	}


	public void setAnneeHorizon(int anneeHorizon) {
		this.anneeHorizon = anneeHorizon;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public Boolean getStandard() {
		return standard;
	}


	public void setStandard(Boolean standard) {
		this.standard = standard;
	}


	public Boolean getValidation() {
		return validation;
	}


	public void setValidation(Boolean validation) {
		this.validation = validation;
	}


	public Boolean getCalage() {
		return calage;
	}


	public void setCalage(Boolean calage) {
		this.calage = calage;
	}


	public Boolean getEnglobante() {
		return englobante;
	}


	public void setEnglobante(Boolean englobante) {
		this.englobante = englobante;
	}


	public Boolean getActifs() {
		return actifs;
	}


	public void setActifs(Boolean actifs) {
		this.actifs = actifs;
	}


	public Boolean getMenages() {
		return menages;
	}


	public void setMenages(Boolean menages) {
		this.menages = menages;
	}


	public Boolean getEleves() {
		return eleves;
	}


	public void setEleves(Boolean eleves) {
		this.eleves = eleves;
	}


	public Boolean getParGeneration() {
		return parGeneration;
	}


	public void setParGeneration(Boolean parGeneration) {
		this.parGeneration = parGeneration;
	}


	public String getLibelle() {
		return libelle;
	}


	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}


	public Date getDateCreation() {
		return dateCreation;
	}


	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}


	public Projection getProjectionEtalon() {
		return projectionEtalon;
	}


	public void setProjectionEtalon(Projection projectionEtalon) {
		this.projectionEtalon = projectionEtalon;
	}


	public Projection getProjectionEnglobante() {
		return projectionEnglobante;
	}


	public void setProjectionEnglobante(Projection projectionEnglobante) {
		this.projectionEnglobante = projectionEnglobante;
	}
	
	
	
	
	
}
