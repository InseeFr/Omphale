package fr.insee.omphale.dao.projection;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.projection.EvolDeScenario;
import fr.insee.omphale.domaine.projection.EvolDeScenarioId;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour EvolDeScenario
 *
 */
public interface IEvolDeScenarioDAO extends IGenericDAO<EvolDeScenario, EvolDeScenarioId> {
	
	/**
	 * Recherche la liste des evolutions de scénario dans la base de données pour un scénario donné.
	 * @param scenarioId
	 * @return
	 */
	public List<EvolDeScenario> getListe(int scenarioId);
	
	/**
	 * Recherche à quels scénarios appartient une évolution non localisée;
	 * 
	 * @param evolution
	 * @return Scenario
	 */
	public List<EvolDeScenario> findByEvolution(EvolutionNonLocalisee evolution);

	public int deleteByIdScenario(List<Integer> scenariosASupprimerId);

	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId);
	

	
	
}
