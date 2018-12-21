/**
 * 
 */
package fr.insee.omphale.domaine.projection;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;

import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.ihm.util.ParametresForm;

/**
 * <h2>Classe métier pour Projection</h2>
 * 
 * 
 * <p>Une définition de projection décrit l’ensemble des paramètres définis par l’utilisateur :</p>
 * 
 * <ul>
 * 	<li>Le zonage</li>
 *  <li>L’année de référence et l’année d’horizon.</li>
 *  <li>Les entités à projeter.</li>
 *  <li>La projection étalon utilisée</li>
 *  <li>Le scénario d’évolution non localisé</li>
 *  <li>Les évolutions localisées</li>s
 * </ul>
 * 
 * 
 */
@Entity
@Table(name="DEF_PROJECTION")
public class Projection implements Serializable {


	private static final long serialVersionUID = 2677342752303976968L;
	
	@Id
	@Column(name="ID_PROJECTION", length=5)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idProjectionGenerator")
	@GenericGenerator(name="idProjectionGenerator", strategy="native",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_PROJECTION")})
	private Integer id;
	
	@Column(name="ANNEE_REFERENCE", length=4)
	private int anneeReference;
	
	@Column(name="ANNEE_HORIZON", length=4)
	private int anneeHorizon;
	
	@Column(name="NOM", length=20)
	private String nom;
	
	@Column(name="STANDARD")
	private Boolean standard;

	@Transient
	private Composante composante;
	
	@Transient
	private TypeEntite typeEntite;
	
	@Column(name="VALIDATION")
	private Boolean validation;
	
	@Column(name="CALAGE")
	private Boolean calage;
	
	@Column(name="ENGLOBANTE")
	private int englobante;
	
	@Column(name="ACTIFS")
	private Boolean actifs;
	
	@Column(name="MENAGES")
	private Boolean menages;
	
	@Column(name="ELEVES")
	private Boolean eleves;
	
	@Column(name="PYRAMIDE_GENERATION")
	private Boolean parGeneration;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;
	
	@Column(name="DATE_CREATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreation;

	@ManyToOne
	@JoinColumn(name="ID_USER")
	private Utilisateur utilisateur;
	
	@ManyToOne
	@JoinColumn(name="ID_PROJ_ETALON")
	private Projection projectionEtalon;
	
	@ManyToOne
	@JoinColumn(name="ID_PROJ_ENGLOBANTE")
	private Projection projectionEnglobante;
	
	@ManyToOne
	@JoinColumn(name="ID_ZONAGE")
	private Zonage zonage;
	
	@ManyToOne
	@JoinColumn(name="ID_SCENARIO")
	private Scenario scenario;
	
	@OneToMany(targetEntity=fr.insee.omphale.domaine.projection.EvolutionLocalisee.class,
			 mappedBy="projection")
	@Fetch(FetchMode.SUBSELECT)
	private Set<EvolutionLocalisee> evolutionsLocalisees;

	public Projection() {
	}

	// jsp
	public String getStandardLibelle() {
		if (standard != null && standard.booleanValue() == true) {
			return ParametresForm.getString("standard.libelle");
		} else
			return ParametresForm.getString("nonStandard.libelle");
	}

	// jsp
	public String getValidationLibelle() {
		if (validation != null && validation.booleanValue() == true) {
			return "Oui";
		} else
			return "Non";
	}

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

	public Composante getComposante() {
		return composante;
	}

	public void setComposante(Composante composante) {
		this.composante = composante;
	}

	public TypeEntite getTypeEntite() {
		return typeEntite;
	}

	public void setTypeEntite(TypeEntite typeEntite) {
		this.typeEntite = typeEntite;
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
	
	public Boolean getActifs() {
		return actifs;
	}

	public int getEnglobante() {
		return englobante;
	}

	public void setEnglobante(int englobante) {
		this.englobante = englobante;
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

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
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

	public Zonage getZonage() {
		return zonage;
	}

	public void setZonage(Zonage zonage) {
		this.zonage = zonage;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	public Set<EvolutionLocalisee> getEvolutionsLocalisees() {
		return evolutionsLocalisees;
	}

	public void setEvolutionsLocalisees(
			Set<EvolutionLocalisee> evolutionsLocalisees) {
		this.evolutionsLocalisees = evolutionsLocalisees;
	}

	public String getLibelleActifs() {
		if (actifs) {
			return ParametresForm.getString("true.libelle");
		} else {
			return ParametresForm.getString("false.libelle");
		}
	}

	public String getLibelleMenages() {
		if (menages) {
			return ParametresForm.getString("true.libelle");
		} else {
			return ParametresForm.getString("false.libelle");
		}
	}

	public String getLibelleEleves() {
		if (eleves) {
			return ParametresForm.getString("true.libelle");
		} else {
			return ParametresForm.getString("false.libelle");
		}
	}

	public String getLibelleCalage() {
		if (calage) {
			return ParametresForm.getString("true.libelle");
		} else {
			return ParametresForm.getString("false.libelle");
		}
	}
	
	public String getLibelleParGeneration() {
		if (parGeneration) {
			return ParametresForm.getString("true.libelle");
		} else {
			return ParametresForm.getString("false.libelle");
		}
	}
	
	

	public String getLibelleEnglobante() {
		if (englobante == 0) {
			return ParametresForm.getString("false.libelle.englobante");
		} else if (englobante == 1){
			return ParametresForm.getString("true.libelle.englobante");
		} else {
			return ParametresForm.getString("standard.libelle.englobante");
		}
	}

}
