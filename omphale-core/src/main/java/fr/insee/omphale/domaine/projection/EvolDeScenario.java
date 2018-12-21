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
 * <h2>Classe métier pour EvolDeScenario </h2>
 * 
 * <p>Un scénario regroupe plusieurs EvolutionNonLocalisee qui s'appliqueront dans un certain ordre, un rang.</p>
 * <p>Ce rang est défini à l'aide de cette classe.</p>
 * <p>On fait le lien entre le Scenario, l'EvolutionNonLocalisee et le rang à appliquer.</p>
 * <p>On applique un idComposite à cet objet </p>
 * 
 * 
 * 
 */
@Entity
@Table(name="EVOL_DE_SCENAR")
public class EvolDeScenario implements Serializable {


	private static final long serialVersionUID = 2105705049502062613L;
	
	@Id
	private EvolDeScenarioId id;
	
	@Column(name="RANG")
	private Integer rang;
	

	public EvolDeScenario(){};
	
	public EvolDeScenario (Scenario scenario, EvolutionNonLocalisee evolutionNL, int rang) {
		this.id = new EvolDeScenarioId();
		this.id.setScenario(scenario);
		this.id.setEvolutionNonLocalisee(evolutionNL);
		this.rang = rang;
	}
	
	public Integer getRang() {
		return rang;
	}

	public void setRang(Integer rang) {
		this.rang = rang;
	}

	public EvolDeScenarioId getId() {
		return id;
	}

	public void setId(EvolDeScenarioId id) {
		this.id = id;
	}
}
