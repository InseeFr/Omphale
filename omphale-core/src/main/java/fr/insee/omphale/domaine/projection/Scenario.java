/**
 * 
 */
package fr.insee.omphale.domaine.projection;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
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
import fr.insee.omphale.ihm.util.ParametresForm;
import fr.insee.omphale.ihm.util.dataTable.AffichageUtilDataTable;

/**
 * <h2>Classe métier pour Scenario</h2>
 * 
 * <h4>On trouve dans les spécifications :</h4>
 * <p>Un scénario d’évolution non localisée permet de fédérer plusieurs évolutions non localisées. </p>
 * <p>Ils permettront de définir des scénarios « standards », appliqués sans considération sur le zonage.</p>
 * <p>Une définition de projection mobilisera des évolutions non localisées uniquement par le biais d’un scénario d’évolution non localisée </p>
 * <p>(une évolution non localisée ne peut pas être affectée directement à une définition de projection, contrairement aux évolutions localisées).</p>
 * 
 * 
 */
@Entity
@Table(name="SCENAR_NON_LOC")
public class Scenario implements Serializable{

	private static final long serialVersionUID = 2105705049502062613L;
	
	@Id
	@Column(name="ID_SCENARIO", length=15)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idScenario")
	@GenericGenerator(name="idScenario", strategy="native",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_SCENARIO")})
	private int id;
	
	@Column(name="NOM", length=20)
	private String nom;
	
	@Column(name="STANDARD")
	private Boolean standard;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;
	
	@OneToMany(targetEntity=fr.insee.omphale.domaine.projection.EvolDeScenario.class,
			cascade=CascadeType.ALL, mappedBy="id.scenario", orphanRemoval=true)
	@Fetch(FetchMode.SUBSELECT)
	private Set<EvolDeScenario> evolutionsDeScenario;
	
	@Transient
	private List<EvolutionNonLocalisee> evolutionsNL;
	
	@ManyToOne
	@JoinColumn(name="ID_USER")
	private Utilisateur utilisateur;
	
	@Column(name="VALIDATION")
	private Boolean validation;
	
	@Column(name="DATE_CREATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreation;

	public Scenario(){}
		
	// jsp
	public String getStandardLibelle(){
		if (standard != null && standard.booleanValue() == true) {
			return ParametresForm.getString("standard.libelle");
		}
		else return ParametresForm.getString("nonStandard.libelle");
	}
	
	// jsp
	public String getValidationLibelle(){
		if (validation != null && validation.booleanValue() == true) {
			return "Oui";
		}
		else return "Non";
	}
	
	// jsp
	public boolean getValidation() {
		return (validation != null && validation.booleanValue() == true);
	}

	// jsp
	public boolean getHasEvolutionsNonLocalisees() {
		return (evolutionsNL != null && evolutionsNL.size() > 0);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
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

	public Boolean isStandard() {
		return standard;
	}

	public void setStandard(Boolean standard) {
		this.standard = standard;
	}

	public Set<EvolDeScenario> getEvolutionsDeScenario() {
		return evolutionsDeScenario;
	}

	public void setEvolutionsDeScenario(Set<EvolDeScenario> evolutionsDeScenario) {
		this.evolutionsDeScenario = evolutionsDeScenario;
	}

	public Boolean isValidation() {
		return validation;
	}

	public void setValidation(Boolean validation) {
		this.validation = validation;
	}

	public List<EvolutionNonLocalisee> getEvolutionsNL() {
		return evolutionsNL;
	}

	public void setEvolutionsNL(List<EvolutionNonLocalisee> evolutionsNL) {
		this.evolutionsNL = evolutionsNL;
	}

	
}
