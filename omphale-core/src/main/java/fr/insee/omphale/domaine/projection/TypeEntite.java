package fr.insee.omphale.domaine.projection;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <h2>Classe métier pour TypeEntite</h2>
 * 
 * <p>permet d'indiquer le type de quotient pour une Composante associée à une EvolutionNonLocalisée</p>
 * <p>Une même Composante peut avoir plusieurs types de quotient.</p>
 * <p></p>
 * <p></p>
 * <p></p>
 * <p></p>
 * <h4>Exemple 1 pour la Composante "Décès", les types de quotient possibles sont : </h4>
 * 
 * <ul>
 * 	<li>Quotient de décès</li>
 *  <li>Espérance de vie</li>
 * </ul>
 * 
 * <h4>Exemple 2 pour la Composante "Fécondité", les types de quotient possibles sont : </h4>
 * <ul>
 * 	<li>Quotient de fécondité</li>
 *  <li>ICF</li>
 * </ul>
 * 
 * 
 */
@Entity
@Table(name="TYPE_ENTITE")
public class TypeEntite implements Serializable{


	private static final long serialVersionUID = 379335109607699873L;
	
	@Id
	@Column(name="TYPE_ENTITE", length=5)
	private String code;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;
	
	@Column(name="DIMENSION_SEXE")
	private Boolean sexe;
	
	@Column(name="DIMENSION_AGE")
	private Boolean age;
	
	@Column(name="CTRL_EXHAUST")
	private Boolean exhaustivite;
	
	@Column(name="VAL_MIN")
	private Double min;
	
	@Column(name="VAL_MAX")
	private Double max;
	
	@Column(name="RESTRICTION_AGE")
	private boolean restrictionAge;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public Boolean getSexe() {
		return sexe;
	}

	public void setSexe(Boolean sexe) {
		this.sexe = sexe;
	}

	public Boolean getExhaustivite() {
		return exhaustivite;
	}

	public void setExhaustivite(Boolean exhaustivite) {
		this.exhaustivite = exhaustivite;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public boolean isRestrictionAge() {
		return restrictionAge;
	}

	public void setRestrictionAge(boolean restrictionAge) {
		this.restrictionAge = restrictionAge;
	}

	public Boolean getAge() {
		return age;
	}

	public void setAge(Boolean age) {
		this.age = age;
	}

}
