package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.List;

import fr.insee.omphale.core.service.projection.IEvolDeScenarioService;
import fr.insee.omphale.dao.projection.IEvolDeScenarioDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.projection.EvolDeScenario;
import fr.insee.omphale.domaine.projection.EvolutionLocalisee;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Scenario;

/**
 * Classe pour gérer les fonctionnalités de la couche service pour EvolDeScenario
 */
public class EvolDeScenarioService extends
GenericDAO<EvolutionLocalisee, Integer>implements IEvolDeScenarioService {

	private IEvolDeScenarioDAO evolDeScenarioDao;

	public EvolDeScenarioService(){
		this.evolDeScenarioDao = daoFactory.getEvolDeScenarioDAO();
	}
	
	public EvolDeScenarioService(IEvolDeScenarioDAO evolDeScenarioDAO){
		this.evolDeScenarioDao = evolDeScenarioDAO;
	}
	
	public List<EvolDeScenario> getListe(int scenarioId) {
		return evolDeScenarioDao.getListe(scenarioId);
	}
	


	public List<Scenario> findByEvolution(EvolutionNonLocalisee evolution) {

		List<EvolDeScenario> evolsDeScenarios = evolDeScenarioDao
				.findByEvolution(evolution);
		List<Scenario> scenarios = new ArrayList<Scenario>();
		if (!evolsDeScenarios.isEmpty()) {
			for (EvolDeScenario evolDeScenario : evolsDeScenarios) {
				scenarios.add(evolDeScenario.getId().getScenario());
			}
		}
		return scenarios;
	}

	public EvolDeScenario insertOrUpdate(EvolDeScenario evolDeScenario) {
		return evolDeScenarioDao.insertOrUpdate(evolDeScenario);
	}
	
	public void delete(EvolDeScenario evolDeScenario) {
		evolDeScenarioDao.delete(evolDeScenario);
	}
	
	public void flush() {
		evolDeScenarioDao.flush();
	}

	
	public int deleteByIdScenario(List<Integer> scenariosASupprimerId){
		return evolDeScenarioDao.deleteByIdScenario(scenariosASupprimerId);
	}

	@Override
	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId) {
			return evolDeScenarioDao.deleteByListeIdEvolutionNonLocalisee(evolutionsNonLocaliseesASupprimerId);
	}

}
