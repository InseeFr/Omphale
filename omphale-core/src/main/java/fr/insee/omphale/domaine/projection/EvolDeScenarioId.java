/**
 * 
 */
package fr.insee.omphale.domaine.projection;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * <h2>Classe métier pour EvolDeScenarioId</h2>
 * 
 * 
 * <p>permet de définir l'idComposite soit composé d'un Scenario et d'une EvolutionNonLocalisee.</p>
 * 
 * 
 */
@Embeddable
public class EvolDeScenarioId implements Serializable {
	
	private static final long serialVersionUID = 2105705049502062613L;
	
	@ManyToOne
	@JoinColumn(name="ID_EVOL_NON_LOC")
	private EvolutionNonLocalisee evolutionNonLocalisee;
	
	@ManyToOne
	@JoinColumn(name="ID_SCENARIO")
	private Scenario scenario;

	public EvolutionNonLocalisee getEvolutionNonLocalisee() {
		return evolutionNonLocalisee;
	}

	public void setEvolutionNonLocalisee(
			EvolutionNonLocalisee evolutionNonLocalisee) {
		this.evolutionNonLocalisee = evolutionNonLocalisee;
	}

	public Scenario getScenario() {
		return scenario;
	}

	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}
}
