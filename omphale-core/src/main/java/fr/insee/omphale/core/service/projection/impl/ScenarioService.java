package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.insee.omphale.core.service.projection.IScenarioService;
import fr.insee.omphale.dao.projection.IProjectionDAO;
import fr.insee.omphale.dao.projection.IScenarioDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Scenario;
import fr.insee.omphale.service.comparator.scenarioComparator;

/**
 * Classe gérant les fonctions de la couche service pour Scenario
 *
 */
public class ScenarioService implements IScenarioService {

	private IScenarioDAO scenarioDao ;
	@SuppressWarnings("unused")
	private IProjectionDAO projectionDao;

	
	public ScenarioService(){
		this.scenarioDao = daoFactory.getScenarioDAO();
		this.projectionDao = daoFactory.getProjectionDAO();

	}

	public ScenarioService(IScenarioDAO scenarioDao){
		this.scenarioDao = scenarioDao;
	}
	
	public ScenarioService(IScenarioDAO scenarioDao, IProjectionDAO projectionDao){
		this.scenarioDao = scenarioDao;
		this.projectionDao = projectionDao;

	}
	public Scenario findById(Integer id) {
		return scenarioDao.findById(id);
	}

	public boolean testerNomScenario(String idUser, String nom, IScenarioService scenarioService) {
		List<Scenario> liste = findAll(idUser);
		if (liste == null) {
			return true;
		}
		for (Scenario scenario : liste) {
			if (nom.equals(scenario.getNom())) {
				return false;
			}
		}
		return true;
	}

	public boolean testerNomScenario(String idUser, String nom, int id, IScenarioService scenarioService) {
		List<Scenario> liste = scenarioService.findAll(idUser);
		if (liste == null) {
			return true;
		}
		for (Scenario scenario : liste) {
			if (nom.equals(scenario.getNom()) && scenario.getId() != id) {
				return false;
			}
		}
		return true;
	}

	public int compterScenario(String idUser, IScenarioService scenarioService) {
		List<Scenario> liste = scenarioService.findAll(idUser);
		if (liste != null) {
			return liste.size();
		}
		return 0;
	}

	public List<Scenario> findAll(String idUser) {
		return scenarioDao.findAll(idUser);
	}

	public List<Scenario> findAll(String idUser, Boolean standard) {
		return scenarioDao.findAll(idUser, standard);
	}
	
	public List<Scenario> findAll(String idUser, Boolean standard, Boolean validation) {
		return scenarioDao.findAll(idUser, standard, validation);
	}

	public Scenario insertOrUpdate(Scenario scenario) {
		return scenarioDao.insertOrUpdate(scenario);
	}

	public void update() {
		flush();
	}

	public void remove(Scenario scenario) {
		scenarioDao.delete(scenario);
	}

	public void flush() {
		scenarioDao.flush();
	}

	public List<Scenario> findAll(Boolean standard) {
		return scenarioDao.findAll(standard);
	}
	
	public void triScenarioParOrdreAlphabetique(List<Scenario> scenarios){
		Collections.sort(scenarios, new scenarioComparator());
	}
	
	public List<Object> rechercheScenariosPourFonctionSuppression(	Utilisateur utilisateur,
			List<Integer> scenariosASupprimer,
			List<Integer> scenariosAConserver,
			List<String> nomScenariosUtilisesASupprimer,
			List<String> nomScenariosUtiliseesAConserver){
		
		List<Object> resultat = new ArrayList<Object>();
		
		scenariosASupprimer = scenarioDao.findByIdNonPartagee(utilisateur);
		scenariosAConserver = scenarioDao.findByIdPartagee(utilisateur);
		nomScenariosUtilisesASupprimer = scenarioDao.findByNomNonPartagee(utilisateur);
		nomScenariosUtiliseesAConserver = scenarioDao.findByNomPartagee(utilisateur);

		resultat.add(scenariosASupprimer);
		resultat.add(scenariosAConserver);
		resultat.add(nomScenariosUtilisesASupprimer);
		resultat.add(nomScenariosUtiliseesAConserver);
		
		
		return resultat;
	}



	@Override
	public int deleteByIdScenario(List<Integer> scenariosASupprimerId) {
		return scenarioDao.deleteByIdScenario(scenariosASupprimerId);
	}

}
