package fr.insee.omphale.domaine.geographie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Classe métier pour définir une Commune
 *
 */
@Entity
@Table(name="COMMUNE")
public class Commune {
	/**
	 * identifiant du système
	 */
	@Id
	@Column(name="ID_COMMUNE", length=5)
	private String id;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;
	
	/**
	 * Departement auquel appartient la commune
	 */
	@ManyToOne
	@JoinColumn(name="ID_DEPT")
	private Departement departement;

	public Commune() {

	}

	public Commune(String id, String libelle, Departement departement) {
		this.id = id;
		this.libelle = libelle;
		this.departement = departement;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Departement getDepartement() {
		return departement;
	}

	public void setDepartement(Departement departement) {
		this.departement = departement;
	}
}
