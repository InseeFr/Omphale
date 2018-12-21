package fr.insee.omphale.domaine.projection;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


/**
 * <h2>Classe métier pour ValeurCubeHypotheseId</h2>
 * 
 * <p>s'agit de l'identifiant d'une valeur des données de l'hypothèse externe</p>
 * <p>l'hypothèse, l'année, l'âge et le sexe forment l'identifiant de cette valeur du cubeHyptohese</p>
 * <p></p>
 * <p></p>
 * <p></p>
 * <ul>
 * 	<li></li>
 *  <li></li>
 * </ul>
 * 
 */
@Embeddable
public class ValeurCubeHypotheseId implements Serializable {

	
	private static final long serialVersionUID = -1886783752693744657L;
	
	@ManyToOne
	@JoinColumn(name="ID_HYPOTHESE")
	private Hypothese hypothese;
	
	@Column(name="ANNEE", length=4)
	private int annee;
	
	@Column(name="AGE", length=3)
	private int age;
	
	@Column(name="SEXE", length=1)
	private int sexe;

	public ValeurCubeHypotheseId() {
	}

	public ValeurCubeHypotheseId(Hypothese hypothese, Integer annee,
			Integer age, Integer sexe) {
		this.hypothese = hypothese;
		this.annee = annee;
		this.age = age;
		this.sexe = sexe;
	}

	public Hypothese getHypothese() {
		return hypothese;
	}

	public void setHypothese(Hypothese hypothese) {
		this.hypothese = hypothese;
	}

	public int getAnnee() {
		return annee;
	}

	public void setAnnee(int annee) {
		this.annee = annee;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getSexe() {
		return sexe;
	}

	public void setSexe(int sexe) {
		this.sexe = sexe;
	}

}
