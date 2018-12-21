package fr.insee.omphale.domaine.geographie;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.ihm.util.dataTable.AffichageUtilDataTable;


/**
 * <h2>Classe métier pour Zonage</h2>
 * 
 * 
 * <p>Un zonage est constitué d’un ensemble de zones. Une définition de projection fera référence à un zonage.</p>
 * 	<h4>RG_50 - zonage - définition</h4>
 *	<p>Un zonage est un ensemble de zones.</p>
 *	<h4>RG_51 - zonage - identification</h4>
 *	<p>Un zonage est associé à un utilisateur. Il est identifié par son nom et l’identifiant de l’utilisateur qui l’a créé. </p>
 *
 */
@Entity
@Table(name="ZONAGE")
@BatchSize(size=500)
@SuppressWarnings("serial")
public class Zonage implements Serializable {
	@Id
	@Column(name="ID_ZONAGE", length=5)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idZonageGenerator")
	@GenericGenerator(name="idZonageGenerator", strategy="fr.insee.omphale.dao.generator.Generator",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_ZONE_GEO4")})
	private String id;
	
	@Column(name="NOM", length=20)
	private String nom;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;
	
	@Column(name="ANNEE_VALIDATION", length=4)
	private int anneeValidation;
	
	@Type(type="fr.insee.omphale.dao.StringValuedEnumType",
			parameters = {@org.hibernate.annotations.Parameter(
					name = "enum", value = "fr.insee.omphale.domaine.geographie.EEtatValidation")})
	@Column(name="ETAT_VALIDATION", length=1)
	private EEtatValidation etatValidation;
	
	@ManyToOne
	@JoinColumn(name="ID_USER")
	private Utilisateur utilisateur;
	
	@ManyToMany(
			targetEntity=fr.insee.omphale.domaine.geographie.Zone.class)
	@JoinTable(name="ZONE_DE_ZONAGE",
			joinColumns=@JoinColumn(name="ZONAGE"),
			inverseJoinColumns=@JoinColumn(name="ZONE"))
	private Set<Zone> zones;
	
	@Column(name="DATE_CREATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreation;

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

	public EEtatValidation getEtatValidation() {
		return etatValidation;
	}

	public void setEtatValidation(EEtatValidation etatValidation) {
		this.etatValidation = etatValidation;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Set<Zone> getZones() {
		return zones;
	}

	public void setZones(Set<Zone> zones) {
		this.zones = zones;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	public Integer getNombreZones(){
		return  zones.size();
	}
	
	public Date getDateCreation() {
		return dateCreation;
	}
	
	public String getDateCreationAffichageDataTable(){
		return AffichageUtilDataTable.getDateAAfficher(dateCreation);
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
}
