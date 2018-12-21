package fr.insee.omphale.domaine.geographie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Classe métier pour Departement
 *
 */
@Entity
@Table(name="DEPARTEMENT")
public class Departement {
	/**
	 * Identifiant généré par le système 
	 */
	@Id
	@Column(name="ID_DEPT", length=3)
	private String id;

	@Column(name="NOM", length=20)
	private String nom;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;
	/**
	 * Un Departement fait parti d'une Region
	 */
	@ManyToOne
	@JoinColumn(name="ID_REGION")
	private Region region;

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

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
	
	public String getIdEtLibelle(){
		return this.getId() + " - " + this.getLibelle();
	}

}
