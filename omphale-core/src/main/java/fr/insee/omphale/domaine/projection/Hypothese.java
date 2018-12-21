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
 * <h2>Classe métier pour Hypothese</h2>
 * 
 * 
 * <p>Les hypothèses externes permettent aux utilisateurs d’importer des données construites en dehors d’Omphale.</p>
 * <p>Elles peuvent décrire des quotients de fécondité, des indices conjoncturels de fécondité (ICF), des quotients de décès, </p>
 * <p>des espérances de vie (EDV), des quotients d’émigration, des immigrants vers l’étranger ou des émigrants vers l’étranger, ou encore des taux de chefs de ménages, d’actifs ou d’élèves. </p>
 * <p>Elles sont utilisées par les évolutions (non localisées ou localisées) qui s’y réfèrent.</p>
 * <br>
 * <p>Les hypothèses externes ont une double utilité : </p>
 * <ul>
 * 	<li>permettre à l’administrateur d’importer dans Omphale les quotients et données de référence issus de la projection France ; ce cas d’utilisation sera très majoritaire ;</li>
 *  <li>plus rarement, permettre à un utilisateur de produire en dehors de l’outil des quotients pour l’ensemble des années de la projection ; dans ce cas, les quotients importés seront vraisemblablement des quotients initialement exportés d’Omphale par l’utilisateur, puis retravaillés en dehors de l’outil.</li>
 * </ul>
 * <p>Les hypothèses externes ne sont jamais localisées : c’est la localisation, la bi-localisation  ou la non localisation des évolutions qui les utilisent qui définira leur utilisation </p>
 * <p>respectivement pour une zone, un couple de zones ou l’ensemble des zones. Elles renvoient à un cube_hypothese_externe qui contient les données.</p>
 * <p>Par ailleurs, les hypothèses externes permettront de décrire des données de population, afin d’intégrer à Omphale les résultats d’une projection exogène pour la France. Il s’agit donc d’un cas d’utilisation à part : </p>
 * <p>la finalité n’est pas ici de spécifier des évolutions (aucune évolution ne pourra faire référence à une hypothèse externe de population), mais de permettre le calage sur une projection exogène. Les évolutions faisant référence à une telle hypothèse seront nécessairement non localisées.</p>
 * 
 * 
 */
@Entity
@Table(name="USER_HYPOTHESE")
public class Hypothese implements Serializable {


	private static final long serialVersionUID = 9126702801652701409L;
	
	@Id
	@Column(name="ID_HYPOTHESE")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="idHypotheseGenerator")
	@GenericGenerator(name="idHypotheseGenerator", strategy="native",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_HYPOTHESE")})
	private int id;
	
	@Column(name="NOM", length=20)
	private String nom;
	
	@ManyToOne
	@JoinColumn(name="TYPE_ENTITE")
	private TypeEntite typeEntite;
	
	@Column(name="ANNEE_DEB", length=4)
	private Integer anneeDebut;
	
	@Column(name="ANNEE_FIN", length=4)
	private Integer anneeFin;
	
	@Column(name="SEXE_DEB", length=1)
	private Integer sexeDebut;
	
	@Column(name="SEXE_FIN", length=1)
	private Integer sexeFin;
	
	@Column(name="AGE_DEB", length=3)
	private Integer ageDebut;
	
	@Column(name="AGE_FIN", length=3)
	private Integer ageFin;
	
	@Column(name="STANDARD")
	private Boolean standard;
	
	@Column(name="LIBELLE", length=50)
	private String libelle;
	
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

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public TypeEntite getTypeEntite() {
		return typeEntite;
	}

	public void setTypeEntite(TypeEntite typeEntite) {
		this.typeEntite = typeEntite;
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

	public void setStandard(Boolean standard) {
		this.standard = standard;
	}

	public Boolean isStandard() {
		return standard;
	}

	public Integer getAnneeDebut() {
		return anneeDebut;
	}

	public void setAnneeDebut(Integer anneeDebut) {
		this.anneeDebut = anneeDebut;
	}

	public Integer getAnneeFin() {
		return anneeFin;
	}

	public void setAnneeFin(Integer anneeFin) {
		this.anneeFin = anneeFin;
	}

	public Integer getSexeDebut() {
		return sexeDebut;
	}

	public void setSexeDebut(Integer sexeDebut) {
		this.sexeDebut = sexeDebut;
	}

	public Integer getSexeFin() {
		return sexeFin;
	}

	public void setSexeFin(Integer sexeFin) {
		this.sexeFin = sexeFin;
	}

	public Integer getAgeDebut() {
		return ageDebut;
	}

	public void setAgeDebut(Integer ageDebut) {
		this.ageDebut = ageDebut;
	}

	public Integer getAgeFin() {
		return ageFin;
	}

	public void setAgeFin(Integer ageFin) {
		this.ageFin = ageFin;
	}
}
