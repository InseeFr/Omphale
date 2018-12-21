package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IEvolDeScenarioService;
import fr.insee.omphale.dao.projection.IEvolDeScenarioDAO;
import fr.insee.omphale.domaine.projection.EvolDeScenario;
import fr.insee.omphale.domaine.projection.EvolDeScenarioId;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Scenario;

public class EvolDeScenarioServiceTest {
	
	private Scenario scenarioA;
	private List<EvolDeScenario> evolDeScenariosA;
	private EvolDeScenario evolDeScenarioA;
	private EvolDeScenarioId evolDeScenarioIdA;
	private EvolutionNonLocalisee enlA;
	
	
	/**
	 * teste scenario renvoyé dans liste est bien celui qu'il doit être
	 *  void
	 */
	@Test
	public void findByEvolutionAvecUneSeuleChaine(){
		creationDesObjets();
		IEvolDeScenarioDAO evolDeScenarioDAOMock = EasyMock.createMock(IEvolDeScenarioDAO.class);
		EasyMock.expect(evolDeScenarioDAOMock.findByEvolution(enlA)).andReturn(evolDeScenariosA).anyTimes();
		EasyMock.replay(evolDeScenarioDAOMock);
		
		IEvolDeScenarioService evolDeScenarioService = new EvolDeScenarioService(evolDeScenarioDAOMock);
		List<Scenario> resultat = evolDeScenarioService.findByEvolution(enlA);
		
		Assert.assertEquals("doit renvoyer le scenarioA ", resultat.get(0), scenarioA);
	}
	
	private void creationDesObjets(){
		evolDeScenariosA = new ArrayList<EvolDeScenario>();
		evolDeScenarioA = new EvolDeScenario();
		evolDeScenarioIdA = new EvolDeScenarioId();
		scenarioA = new Scenario();
		enlA = new EvolutionNonLocalisee();
		evolDeScenarioIdA.setEvolutionNonLocalisee(enlA);
		evolDeScenarioIdA.setScenario(scenarioA);
		evolDeScenarioA.setId(evolDeScenarioIdA);
		evolDeScenariosA.add(evolDeScenarioA);
	}

	/**
	 * Teste que méthode renvoie bien objet EvolDeScenario souhaité
	 *  void
	 */
	@Test
	public void insertOrUpdateRenvoieUneEvolDeScenario(){
		EvolDeScenario evolDeScenario = new EvolDeScenario();
		
		IEvolDeScenarioDAO evolDeScenarioDAOMock = EasyMock.createMock(IEvolDeScenarioDAO.class);
		EasyMock.expect(evolDeScenarioDAOMock.insertOrUpdate(evolDeScenario)).andReturn(evolDeScenario).anyTimes();
		EasyMock.replay(evolDeScenarioDAOMock);
		
		IEvolDeScenarioService evolDeScenarioService = new EvolDeScenarioService(evolDeScenarioDAOMock);
		EvolDeScenario resultat = evolDeScenarioService.insertOrUpdate(evolDeScenario);
		
		Assert.assertEquals("doit renvoyer l'objet evoldescenario", resultat, evolDeScenario);
		
	}
}
