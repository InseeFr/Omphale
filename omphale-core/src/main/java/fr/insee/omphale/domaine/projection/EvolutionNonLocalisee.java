/**
 * 
 */
package fr.insee.omphale.domaine.projection;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.ihm.util.dataTable.AffichageUtilDataTable;

/**
 * <h2>Classe métier pour EvolutionNonLocalisee</h2>
 * 
 * 
 * <p>Les évolutions non localisées peuvent concerner l’ensemble des quotients, ainsi que les échanges avec l’étranger.</p>
 * <p>Pour les quotients d’émigration, certaines méthodes ne sont pas applicables directement :</p>
 * <p>pour être utilisées, ces évolutions devront nécessairement être ensuite localisées, voire bi-localisées.</p>
 * <p>On parle d’une manière générale et par souci de simplification de quotients. </p>
 * <p>En pratique, les évolutions ne concernent pas nécessairement des quotients stricto sensu.</p>
 * <h4>RG_87 - évolution non localisée - type de quotients</h4>
 * <p>Les différents types de « quotients » sont :</p>
 * <ul>
 * 	<li>fécondité</li>
 *  <li>décès</li>
 *  <li>émigration</li>
 *  <li>immigrants étranger</li>
 *  <li>émigrants étranger</li>
 *  <li>population</li>
 *  <li>actifs</li>
 *  <li>chefs de ménages</li>
 *  <li>élèves</li>
 * </ul>
 * 
 * 
 * 
 */
@Entity
@Table(name="EVOL_NON_LOC")
public class EvolutionNonLocalisee implements Serializable {


	private static final long serialVersionUID = 6472766288330227860L;
	
	@Id
	@Column(name="ID_EVOL_NON_LOC")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idEnlGenerator")
	@GenericGenerator(name="idEnlGenerator", strategy="native",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_EVOL_NON_LOCALISEE")})
	private int id;
	
	@Column(name="NOM", length=20)
	private String nom;
	
	@ManyToOne
	@JoinColumn(name="TYPE_ENTITE")
	private TypeEntite typeEntite;
	
	@ManyToOne
	@JoinColumn(name="COMPOSANTE")
	private Composante composante;
	
	@Column(name="AGE_DEB", length=3)
	private Integer ageDeb;
	
	@Column(name="SEXE_DEB", length=1)
	private Integer sexeDeb;
	
	@Column(name="ANNEE_DEB", length=4)
	private Integer anneeDeb;
	
	@Column(name="AGE_FIN", length=3)
	private Integer ageFin;
	
	@Column(name="SEXE_FIN", length=1)
	private Integer sexeFin;
			
	@Column(name="ANNEE_FIN", length=4)
	private Integer anneeFin;
	
	@Column(name="STANDARD")
	private Boolean standard;
	
	@Column(name="COMMENTAIRE", length=100)
	private String commentaire;
	
	@ManyToOne
	@JoinColumn(name="TYPE_PARAM1")
	private TypeParam typeParam1;
	
	@ManyToOne
	@JoinColumn(name="TYPE_PARAM2")
	private TypeParam typeParam2;
	
	@ManyToOne
	@JoinColumn(name="TYPE_PARAM3")
	private TypeParam typeParam3;
	
	@ManyToOne
	@JoinColumn(name="TYPE_PARAM4")
	private TypeParam typeParam4;
	
	@Column(name="VAL_PARAM1")
	private Double valParam1;
	
	@Column(name="VAL_PARAM2")
	private Double valParam2;
	
	@Column(name="VAL_PARAM3")
	private Double valParam3;
	
	@Column(name="VAL_PARAM4")
	private Double valParam4;
	
	@ManyToOne
	@JoinColumn(name="METHODE_EVOL")
	private MethodeEvolution methode;
	
	@ManyToOne
	@JoinColumn(name="ID_HYPOTHESE")
	private Hypothese hypothese;
	
	@ManyToOne
	@JoinColumn(name="ID_USER")
	private Utilisateur utilisateur;
	
	@Column(name="DATE_CREATION")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreation;

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

	public Integer getAgeDeb() {
		return ageDeb;
	}

	public void setAgeDeb(Integer ageDeb) {
		this.ageDeb = ageDeb;
	}

	public Integer getSexeDeb() {
		return sexeDeb;
	}

	public void setSexeDeb(Integer sexeDeb) {
		this.sexeDeb = sexeDeb;
	}

	public Integer getAnneeDeb() {
		return anneeDeb;
	}

	public void setAnneeDeb(Integer anneeDeb) {
		this.anneeDeb = anneeDeb;
	}

	public Integer getAgeFin() {
		return ageFin;
	}

	public void setAgeFin(Integer ageFin) {
		this.ageFin = ageFin;
	}

	public Integer getSexeFin() {
		return sexeFin;
	}

	public void setSexeFin(Integer sexeFin) {
		this.sexeFin = sexeFin;
	}

	public Integer getAnneeFin() {
		return anneeFin;
	}

	public void setAnneeFin(Integer anneeFin) {
		this.anneeFin = anneeFin;
	}

	public Boolean getStandard() {
		return standard;
	}

	public void setStandard(Boolean standard) {
		this.standard = standard;
	}

	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

	public TypeParam getTypeParam1() {
		return typeParam1;
	}

	public void setTypeParam1(TypeParam typeParam1) {
		this.typeParam1 = typeParam1;
	}

	public TypeParam getTypeParam2() {
		return typeParam2;
	}

	public void setTypeParam2(TypeParam typeParam2) {
		this.typeParam2 = typeParam2;
	}

	public TypeParam getTypeParam3() {
		return typeParam3;
	}

	public void setTypeParam3(TypeParam typeParam3) {
		this.typeParam3 = typeParam3;
	}

	public TypeParam getTypeParam4() {
		return typeParam4;
	}
	

	public void setTypeParam4(TypeParam typeParam4) {
		this.typeParam4 = typeParam4;
	}

	public Double getValParam1() {
		return valParam1;
	}
	
	public String getValParam1Affichage() {
		if (typeParam1.isEntier()) {
			return "" + valParam1.intValue();
		} else {
			return valParam1 + "";
		}
	}

	public void setValParam1(Double valParam1) {
		this.valParam1 = valParam1;
	}

	public Double getValParam2() {
		return valParam2;
	}
	
	public String getValParam2Affichage() {
		if (typeParam2.isEntier()) {
			return "" + valParam2.intValue();
		} else {
			return valParam2 + "";
		}
	}

	public void setValParam2(Double valParam2) {
		this.valParam2 = valParam2;
	}

	public Double getValParam3() {
		return valParam3;
	}
	
	public String getValParam3Affichage() {
		if (typeParam3.isEntier()) {
			return "" + valParam3.intValue();
		} else {
			return valParam3 + "";
		}
	}

	public void setValParam3(Double valParam3) {
		this.valParam3 = valParam3;
	}

	public Double getValParam4() {
		return valParam4;
	}
	
	public String getValParam4Affichage() {
		if (typeParam4.isEntier()) {
			return "" + valParam4.intValue();
		} else {
			return valParam4 + "";
		}
	}

	public void setValParam4(Double valParam4) {
		this.valParam4 = valParam4;
	}

	public MethodeEvolution getMethode() {
		return methode;
	}

	public void setMethode(MethodeEvolution methode) {
		this.methode = methode;
	}

	public Hypothese getHypothese() {
		return hypothese;
	}

	public void setHypothese(Hypothese hypothese) {
		this.hypothese = hypothese;
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

}
