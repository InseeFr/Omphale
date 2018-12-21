package fr.insee.omphale.domaine.geographie;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.DiscriminatorOptions;
import org.hibernate.annotations.GenericGenerator;

import fr.insee.omphale.domaine.Utilisateur;


/**
 * <h2>Classe métier pour Zone</h2>
 * 
 * 	<p>Il s'agit d'une classe mère dont les classes suivantes hérites :</p>
 * <ul>
 * 	<li>ZoneClassique</li>
 * 	<li>ZoneStandardDepartement</li>
 * 	<li>ZoneStandardFrance</li>
 * 	<li>ZoneStandardAncienneRegion</li>
 *  <li>ZoneStandardRegion</li>
 * </ul>
 * <p>On trouve dans la spécification, les définitions suivantes :</p>
 * 	<h4>RG_40 - zone - définition zone</h4>
 *	<p>Une zone est un ensemble de "Commune".</p>
 *	<h4>RG_41 - zone - identification zone</h4>
 *	<p>Une zone est associée à un utilisateur. Elle est identifiée par son nom et son créateur (utilisateur).</p>
 *
 *<p>L'utilisateur peut-être administrateur du système.</p>
 *<p>Dans ce cas, seul cet administrateur pourra modifier ou supprimer ces zones.</p>
 *<p>Les zones standards sont mises à disposition de tous les utilisateurs classiques du système.</p>
 *
 */
@Entity
@Table(name="ZONE")
@BatchSize(size=500)
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
//le type zone d'emploi (4) est considere comme une zone classique (0)
@DiscriminatorFormula(value="decode(type_zone_standard,4,0,type_zone_standard)")
@DiscriminatorOptions(insert=false)
@SuppressWarnings("serial")
public abstract class Zone implements Serializable{
	@Id
	@Column(name="ID_ZONE", length=5)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idZoneGenerator")
	@GenericGenerator(name="idZoneGenerator", strategy="fr.insee.omphale.dao.generator.Generator",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_ZONE_GEO4")})
	private String id;
	
	@Column(name="NOM", length=20)
	private String nom;
	
	@Column(name="LIBELLE", length=80)
	private String libelle;

	@ManyToOne
	@JoinColumn(name="ID_USER")
	private Utilisateur utilisateur;
	
	@ManyToOne
	@JoinColumn(name="TYPE_ZONE_STANDARD")
	private TypeZoneStandard typeZoneStandard;
	
	@ManyToMany(
			targetEntity=fr.insee.omphale.domaine.geographie.Departement.class)
	@JoinTable(name="DEPARTEMENT_IMPACT",
		joinColumns=@JoinColumn(name="ZONE"),
		inverseJoinColumns=@JoinColumn(name="DEPT"))
	private Set<Departement> departementsImpactes;

	public Zone() {

	}

	public Zone(String id, String nom, String libelle, Utilisateur utilisateur,
			TypeZoneStandard typeZoneStandard,
			Set<Departement> departementsImpactes) {
		this.id = id;
		this.nom = nom;
		this.libelle = libelle;
		this.utilisateur = utilisateur;
		this.typeZoneStandard = typeZoneStandard;
		this.departementsImpactes = departementsImpactes;
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
		if(libelle != null){
			if(libelle.length()>48){
				libelle = libelle.substring(0, 48);
			}
		}
		this.libelle = libelle;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}



	public TypeZoneStandard getTypeZoneStandard() {
		return typeZoneStandard;
	}

	public void setTypeZoneStandard(TypeZoneStandard typeZoneStandard) {
		this.typeZoneStandard = typeZoneStandard;
	}

	public Set<Departement> getDepartementsImpactes() {
		return departementsImpactes;
	}

	public void setDepartementsImpactes(Set<Departement> departementsImpactes) {
		this.departementsImpactes = departementsImpactes;
	}

	public int getNbCommunes() {
		return getCommunes().size();
	}

	public Set<Commune> getCommunes() {
		if (this instanceof ZoneStandardDepartement) {
			return ((ZoneStandardDepartement) this).getCommunes();
		} else if (this instanceof ZoneStandardAncienneRegion) {
			return ((ZoneStandardAncienneRegion) this).getCommunes();
		} else if (this instanceof ZoneStandardFrance) {
			return ((ZoneStandardFrance) this).getCommunes();
		} else if (this instanceof ZoneStandardRegion) {
			return ((ZoneStandardRegion) this).getCommunes();
		} else if (this instanceof ZoneClassique) {
			return ((ZoneClassique) this).getCommunes();
		} else {
			return null;
		}
	}

	public void setCommunes(Set<Commune> communes) {
		if (this instanceof ZoneStandardDepartement) {
			((ZoneStandardDepartement) this).setCommunes(communes);
		} else if (this instanceof ZoneStandardAncienneRegion) {
			((ZoneStandardAncienneRegion) this).setCommunes(communes);
		} else if (this instanceof ZoneStandardFrance) {
			((ZoneStandardFrance) this).setCommunes(communes);
		} else if (this instanceof ZoneStandardRegion) {
			((ZoneStandardRegion) this).setCommunes(communes);
		} else if (this instanceof ZoneClassique) {
			((ZoneClassique) this).setCommunes(communes);
		}
	}

	public Set<Region> getRegionsImpactees() {
		Set<Region> resultat = new HashSet<Region>();
		for (Departement departement : departementsImpactes) {
			resultat.add(departement.getRegion());
		}
		return resultat;
	}

	public String getNomEtLibelle() {
		return nom + " - " + libelle;
	}
	
	public String getNomEtTypeZoneStandard(){
		return nom + " -- " + typeZoneStandard.getLibelle()+ "-("+ typeZoneStandard.getId()+ ")";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



}
