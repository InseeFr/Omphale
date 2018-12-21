/**
 * 
 */
package fr.insee.omphale.domaine.projection;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * <h2>Classe métier pour ParamMethodeEvolution</h2>
 * 
 * 
 * <p>permet d'indiquer le ou les paramètres d'une MethodeEvolution</p>
 * <p>En effet, une MethodeEvolution peut avoir un à plusieurs paramètres.</p>
 * <h4>Exemple 1 :</h4>
 * 
 * <p></p>
 * <p></p>
 * <p>Pour une EvolutionNonLocalisee avec comme Composante "Décès"</p>
 * <ul>
 * 	<li>avec comme Composante "Décès",</li>
 *  <li>avec comme MethodeEvolution "Gain d'espérance de vie"</li>
 *  <li>on aura comme paramètres de la méthode d'évolution</li>
 *  <li>"Année Cible" et</li>
 *  <li>"Gain EDV</li>
 *  <li>dont les bornes sont respectivements</li>
 *  <li>entre 1900 et 2100 et</li>
 *  <li>entre -20 et 20</li>
 * </ul>
 * 
 * 
 * 
 */
@Entity
@Table(name="PARAM_METH_EVOL")
public class ParamMethodeEvolution implements Serializable {

	private static final long serialVersionUID = 5743326090780241950L;
	
	@Id
	@ManyToOne
	@JoinColumn(name="METHODE_EVOL")
	private MethodeEvolution methode;
	
	@Id
	@ManyToOne
	@JoinColumn(name="TYPE_PARAM")
	private TypeParam typeParam;
	
	@Column(name="RANG", length=1)
	private int rang;
	
	@Column(name="VAL_DEF")
	private Double valDef;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;

	public MethodeEvolution getMethode() {
		return methode;
	}

	public void setMethode(MethodeEvolution methode) {
		this.methode = methode;
	}

	public TypeParam getTypeParam() {
		return typeParam;
	}

	public void setTypeParam(TypeParam typeParam) {
		this.typeParam = typeParam;
	}

	public int getRang() {
		return rang;
	}

	public void setRang(int rang) {
		this.rang = rang;
	}

	public String getLibelle() {
		if (libelle != null) {
			return libelle;
		} else {
			return typeParam.getLibelle();
		}
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

	public String getValDefAffichage() {
		Double resultat;
		if (valDef == null) {
			resultat = typeParam.getValDef();
		} else {
			resultat = valDef;
		}
		if (typeParam.isEntier()) {
			return "" + resultat.intValue();
		} else {
			return resultat + "";
		}
	}

}
