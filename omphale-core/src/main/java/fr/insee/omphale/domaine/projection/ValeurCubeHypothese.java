/**
 * 
 */
package fr.insee.omphale.domaine.projection;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <h2>Classe métier pour ValeurCubeHypothese</h2>
 * 
 * 
 * <p>correspond à une valeur des données de l'hypothèse externe</p>
 * <p>on l'associe à l'hypothèse donnée, à une année, à un âge et un sexe </p>
 * <p>l'hypothèse, l'année, l'âge et le sexe forme l'identifiant de cette valeur du cubeHyptohese</p>
 * 
 * 
 */
@Entity
@Table(name="CB_HYPOTHESE")
public class ValeurCubeHypothese implements Serializable {

	private static final long serialVersionUID = 7780639690189362220L;
	
	@Id
	private ValeurCubeHypotheseId id;
	
	@Column(name="VALEUR")
	private Double valeur;

	public ValeurCubeHypothese(Hypothese hypothese, Integer annee, Integer age,
			Integer sexe, Double valeur) {
		this.id = new ValeurCubeHypotheseId(hypothese, annee, age, sexe);
		this.valeur = valeur;
	}

	public ValeurCubeHypothese() {
	}

	public Double getValeur() {
		return valeur;
	}

	public void setValeur(Double valeur) {
		this.valeur = valeur;
	}

	public ValeurCubeHypotheseId getId() {
		return id;
	}

	public void setId(ValeurCubeHypotheseId id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "annee:" + id.getAnnee() + "|age:" + id.getAge() + "|sexe:"
				+ id.getSexe() + "|valeur:" + valeur;
	}

}
