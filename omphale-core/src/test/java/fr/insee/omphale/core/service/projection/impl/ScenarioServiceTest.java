
package fr.insee.omphale.core.service.projection.impl;


import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IScenarioService;
import fr.insee.omphale.dao.projection.IScenarioDAO;
import fr.insee.omphale.domaine.projection.Scenario;


public class ScenarioServiceTest {
	
	@Test
	public void findById(){
		Scenario scenario = new Scenario();
		
		IScenarioDAO scenarioDAOMock = EasyMock.createMock(IScenarioDAO.class);
		EasyMock.expect(scenarioDAOMock.findById(1)).andReturn(scenario).anyTimes();
		EasyMock.replay(scenarioDAOMock);
		
		IScenarioService scenarioService = new ScenarioService(scenarioDAOMock);
		Scenario resultat = scenarioService.findById(1);
		
		Assert.assertEquals("renvoie le scenario", resultat, scenario);
	}


	@Test
	public void testerNomScenarioIdUserEtNom(){
		List<Scenario> scenarios = new ArrayList<Scenario>();
		
		IScenarioDAO scenarioDaoMock = EasyMock.createMock(IScenarioDAO.class);
		EasyMock.expect(scenarioDaoMock.findAll("toto")).andReturn(scenarios).anyTimes();
		EasyMock.replay(scenarioDaoMock);
		
		IScenarioService scenarioService = new ScenarioService(scenarioDaoMock);
		boolean resultat = scenarioService.testerNomScenario("toto","toto", scenarioService);
		
		Assert.assertTrue("renvoie vraie", resultat);
		
		Scenario scenario = new Scenario();
		scenario.setNom("toto");
		scenarios.add(scenario);
		
		boolean resultat2 = scenarioService.testerNomScenario("toto","toto", scenarioService);
		
		Assert.assertFalse("renvoie faux", resultat2);
	}


	@Test
	public void testerNomScenarioIdUserEtNomEtId(){
		List<Scenario> scenarios = new ArrayList<Scenario>();
		
		IScenarioDAO scenarioDaoMock = EasyMock.createMock(IScenarioDAO.class);
		EasyMock.expect(scenarioDaoMock.findAll("toto")).andReturn(scenarios).anyTimes();
		EasyMock.replay(scenarioDaoMock);
		
		IScenarioService scenarioService = new ScenarioService(scenarioDaoMock);
		boolean resultat = scenarioService.testerNomScenario("toto","toto",1, scenarioService);
		
		Assert.assertTrue("renvoie vraie", resultat);
		
		Scenario scenario = new Scenario();
		scenario.setNom("toto");
		scenarios.add(scenario);
		
		boolean resultat2 = scenarioService.testerNomScenario("toto","toto",1, scenarioService);
		
		Assert.assertFalse("renvoie faux", resultat2);
	}


	@Test
	public void compterScenario(){
		List<Scenario> scenarios = new ArrayList<Scenario>();

		
		IScenarioDAO scenarioDAOMock = EasyMock.createMock(IScenarioDAO.class);
		EasyMock.expect(scenarioDAOMock.findAll("toto")).andReturn(scenarios).anyTimes();
		EasyMock.replay(scenarioDAOMock);
		
		IScenarioService scenarioService = new ScenarioService(scenarioDAOMock);
		int resultat = scenarioService.compterScenario("toto", scenarioService);
		
		Assert.assertEquals("renvoie taille tableau", resultat, 0);
		
		Scenario scenario = new Scenario();
		scenarios.add(scenario);
		
		int resultat2 = scenarioService.compterScenario("toto", scenarioService);
		
		Assert.assertEquals("renvoie taille tableau", resultat2, 1);
	}


	@Test
	public void findAll(){
		List<Scenario> scenarios = new ArrayList<Scenario>();
		
		IScenarioDAO scenarioDAOMock = EasyMock.createMock(IScenarioDAO.class);
		EasyMock.expect(scenarioDAOMock.findAll("toto")).andReturn(scenarios).anyTimes();
		EasyMock.replay(scenarioDAOMock);
		
		IScenarioService scenarioService = new ScenarioService(scenarioDAOMock);
		List<Scenario> resultat = scenarioService.findAll("toto");
		
		Assert.assertEquals("renvoie liste de scenario", resultat, scenarios);
	}


	@Test
	public void findAllIduserEtStandard(){
		List<Scenario> scenarios = new ArrayList<Scenario>();
		
		IScenarioDAO scenarioDAOMock = EasyMock.createMock(IScenarioDAO.class);
		EasyMock.expect(scenarioDAOMock.findAll("toto", true)).andReturn(scenarios).anyTimes();
		EasyMock.replay(scenarioDAOMock);
		
		IScenarioService scenarioService = new ScenarioService(scenarioDAOMock);
		List<Scenario> resultat = scenarioService.findAll("toto", true);
		
		Assert.assertEquals("renvoie liste de scenario", resultat, scenarios);
	}


	
	@Test
	public void insertOrUpdate(){
		Scenario scenario = new Scenario();
		
		IScenarioDAO scenarioDAOMock = EasyMock.createMock(IScenarioDAO.class);
		EasyMock.expect(scenarioDAOMock.insertOrUpdate(scenario)).andReturn(scenario).anyTimes();
		EasyMock.replay(scenarioDAOMock);
		
		IScenarioService scenarioService = new ScenarioService(scenarioDAOMock);
		Scenario resultat = scenarioService.insertOrUpdate(scenario);
		
		Assert.assertEquals("renvoie le scenario", resultat, scenario);
	}

	@Test
	public void findAllStandard(){
		List<Scenario> scenarios = new ArrayList<Scenario>();
		
		IScenarioDAO scenarioDAOMock = EasyMock.createMock(IScenarioDAO.class);
		EasyMock.expect(scenarioDAOMock.findAll(true)).andReturn(scenarios).anyTimes();
		EasyMock.replay(scenarioDAOMock);
		
		IScenarioService scenarioService = new ScenarioService(scenarioDAOMock);
		List<Scenario> resultat = scenarioService.findAll(true);
		
		Assert.assertEquals("renvoie liste de scenario", resultat, scenarios);
	}
	

	
}
