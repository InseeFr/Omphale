package fr.insee.omphale.domaine.geographie;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



/**
 * Classe Métier pour les communes de dépendance
 * <BR>
 * dans les spécifications on trouve :
 * <BR>
 * RG_14 - communes - dépendances
 * <BR>
 * Si une commune d’un couple appartient à une zone, les communes liées (dépendantes) doivent également 
 * <BR>
 * appartenir à la zone. Ces dépendances sont construites à partir d’un fichier issu de l’historique 
 * <BR>
 * du code officiel géographique (travaillé et fourni par l’administrateur) et décrivant les couples 
 * <BR>
 * de code communes liées.
 *
 */
@Entity
@Table(name = "DEPENDANCE_COMMUNE")
public class CommuneDependance {
	@Id
	@Column(name="ID_DEPENDANCE", length=4)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="idZoneGenerator")
	@GenericGenerator(name="idZoneGenerator", strategy="native",
		parameters={@org.hibernate.annotations.Parameter(name="sequence", value="SEQ_ZONE_GEO4")})
	private int dependance;
	/**
	 * Année correspondant à l'année de mise à jour du code officiel géographique
	 */
	@Column(name="ANNEE", length=4)
	private int annee;
	/**
	 * Liste des communes liées à une commune appartenant à une zone
	 */
	@ManyToMany(
			targetEntity=fr.insee.omphale.domaine.geographie.Commune.class)
	@JoinTable(name="COMMUNE_DEPENDANCE",
			joinColumns=@JoinColumn(name="DEPENDANCE"),
			inverseJoinColumns=@JoinColumn(name="COMMUNE"))
	private Set<Commune> communes;

	public CommuneDependance() {

	}

	public Set<Commune> getCommunes() {
		return communes;
	}

	public void setCommunes(Set<Commune> communes) {
		this.communes = communes;
	}

	public int getDependance() {
		return dependance;
	}

	public void setDependance(int dependance) {
		this.dependance = dependance;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}
}
