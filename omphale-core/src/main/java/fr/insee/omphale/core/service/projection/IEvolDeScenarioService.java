package fr.insee.omphale.core.service.projection;

import java.util.List;

import fr.insee.omphale.domaine.projection.EvolDeScenario;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Scenario;


public interface IEvolDeScenarioService {
	
	/**
	 * Recherche la liste des scénarios associés à une évolution non localisée
	 * 
	 * @param evolution
	 * @return {@link List}<{@link Scenario}>
	 */
	public List<Scenario> findByEvolution(EvolutionNonLocalisee evolution);
	
	/**
	 * Recherche la liste des evolutions de scénario dans la base de données pour un scénario donné.
	 * @param scenarioId
	 * @return
	 */
	public List<EvolDeScenario> getListe(int scenarioId);
	
	/**
	 * Sauvegarde ou met à jour une evolDeScenario dans la base de données.
	 * 
	 * @param evolDeScenario
	 */
	public EvolDeScenario insertOrUpdate(EvolDeScenario evolDeScenario);
	
	/**
	 * Supprime une evolDeScenario dans la base de données.
	 * 
	 * @param evolDeScenario
	 */
	public void delete(EvolDeScenario evolDeScenario);
	
	public void flush();

	public int deleteByIdScenario(List<Integer> scenariosASupprimerId);

	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId);
}
