package fr.insee.omphale.domaine.projection;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * <h2>Classe métier pour TypeParam</h2>
 * 
 * 
 * <p>permet d'indiquer le type du ParamMethodeEvolution associé à une EvolutionNonLocalisee</p>
 * 
 * 
 */
@Entity
@Table(name="TYPE_PARAM")
public class TypeParam implements Serializable {


	private static final long serialVersionUID = -1760908329713335017L;
	
	@Id
	@Column(name="TYPE_PARAM", length=10)
	private String code;
	
	@Column(name="VAL_DEF")
	private Double valDef;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;
	
	@Column(name="IS_ENTIER")
	private boolean entier;

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

	public Double getValDef() {
		return valDef;
	}

	public void setValDef(Double valDef) {
		this.valDef = valDef;
	}

	public boolean isEntier() {
		return entier;
	}

	public void setEntier(boolean entier) {
		this.entier = entier;
	}
}
