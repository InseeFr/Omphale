package fr.insee.omphale.domaine;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * Classe Métier : il s'agit d'un utilisateur de l'application
 * <BR>
 * id --> identifiant généré par le système
 * <BR>
 * libelle --> simple nom et prénom
 * <BR>
 * email --> adresse électronique
 * <BR>
 * role --> soit utilisateur soit administrateur du système
 * <BR>
 * idep --> identifiant utilisateur sur le serveur LDAP
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "USER_OMPHALE")
public class Utilisateur implements Serializable {
	
	@Id
	@Column(name = "ID_USER", length = 6)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idUserGenerator")
	@GenericGenerator(name="idUserGenerator", strategy="fr.insee.omphale.dao.generator.Generator",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_USER_OMPHALE")})
	private String id;
	
	@Column(name = "LIBELLE", length = 50)
	private String libelle;
	
	@Column(name = "EMAIL", length =  50)
	private String email;
	/**
	 * Role de l'utilisateur 1 pour utilisateur et 2 pour administrateur
	 */
	@Column(name = "ROLE")
	private int role;
	
	@Column(name = "IDEP")
	private String idep;

	public Utilisateur() {
	}

	public Utilisateur(String id, String libelle, String email, int role,
			String idep) {
		this.id = id;
		this.libelle = libelle;
		this.email = email;
		this.role = role;
		this.idep = idep;
	}

	public Utilisateur(String libelle, String email, int role,
			String idep) {
		this.libelle = libelle;
		this.email = email;
		this.role = role;
		this.idep = idep;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getIdep() {
		return idep;
	}

	public void setIdep(String idep) {
		this.idep = idep;
	}
}
