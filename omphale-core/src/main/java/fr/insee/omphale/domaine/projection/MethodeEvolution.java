package fr.insee.omphale.domaine.projection;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;


/**
 * <h2>Classe métier pour MethodeEvolution</h2>
 * 
 * <p>permet d'indiquer le type d'évolution à appliquer à une EvolutionNonLocalisee.</p>
 * <p>Pour chaque Composante, il y a plusieurs évolutions possibles.</p>
 * <p>Par exemple, pour une évolution non localisée de type Composante "Décès", les méthodes d'évolution possibles sont :</p>
 * 
 *  <ul>
 * 	<li>Maintien</li>
 *  <li>Parallèle à une tendance</li>
 *  <li>Parallèle à une tendance d’espérance de vie</li>
 *  <li>Convergence vers une tendance</li>
 *  <li>Convergence vers une tendance d’espérance de vie</li>
 *  <li>Gains d’espérance de vie</li>
 *  <li>Surcharge brute</li>
 * </ul>
 * 
 * <p>Ces méthodes d'évolution varient selon la Composante associée à l'EvolutionNonLocalisee</p>
 * 
 */
@Entity
@Table(name="METHODE_EVOL")
public class MethodeEvolution implements Serializable {

	private static final long serialVersionUID = -932457453411739149L;
	
	@Id
	@Column(name="METHODE_EVOL", length=10)
	private String code;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;
	
	@ManyToMany(
			targetEntity=fr.insee.omphale.domaine.projection.TypeParam.class)
	@JoinTable(name="PARAM_METH_EVOL",
			joinColumns=@JoinColumn(name="METHODE_EVOL"),
			inverseJoinColumns=@JoinColumn(name="TYPE_PARAM"))
	private Set<TypeParam> parametres;
	
	@ManyToOne
	@JoinColumn(name="COMPOSANTE")
	private Composante composante;
	
	@Transient
	private Set<Composante> composantes;
	
	@ManyToOne
	@JoinColumn(name="TYPE_ENTITE")
	@LazyToOne(LazyToOneOption.FALSE)
	private TypeEntite typeEntite;
	
	@Column(name="HYPOTHESE_REQUISE")
	private boolean hypotheseRequise;


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

	public Set<TypeParam> getParametres() {
		return parametres;
	}

	public void setParametres(Set<TypeParam> parametres) {
		this.parametres = parametres;
	}

	public TypeEntite getTypeEntite() {
		return typeEntite;
	}

	public void setTypeEntite(TypeEntite typeEntite) {
		this.typeEntite = typeEntite;
	}

	public Composante getComposante() {
		return composante;
	}

	public void setComposante(Composante composante) {
		this.composante = composante;
	}

	
	public Set<Composante> getComposantes() {
		return composantes;
	}

	public void setComposantes(Set<Composante> composantes) {
		this.composantes = composantes;
	}
	

	public boolean isHypotheseRequise() {
		return hypotheseRequise;
	}

	public void setHypotheseRequise(boolean hypotheseRequise) {
		this.hypotheseRequise = hypotheseRequise;
	}
}
