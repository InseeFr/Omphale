package fr.insee.omphale.domaine.geographie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <h2>Classe métier pour Region</h2>
 *
 */
@Entity
@Table(name="REGION")
public class Region {
	
	@Id
	@Column(name="ID_REGION", length=2)
	private String id;
	
	@Column(name="NOM", length=20)
	private String nom;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;

	public Region() {

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
