
package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IParamMethodeEvolutionService;
import fr.insee.omphale.dao.projection.IParamMethodeEvolutionDAO;
import fr.insee.omphale.domaine.projection.MethodeEvolution;
import fr.insee.omphale.domaine.projection.ParamMethodeEvolution;


public class ParamMethodeEvolutionServiceTest {
	@Test
	public void findAll(){
		List<ParamMethodeEvolution> paramMethodeEvolutions = new ArrayList<ParamMethodeEvolution>();
		
		IParamMethodeEvolutionDAO paramMethodeEvolutionDaoMock = EasyMock.createMock(IParamMethodeEvolutionDAO.class);
		EasyMock.expect(paramMethodeEvolutionDaoMock.findAll()).andReturn(paramMethodeEvolutions);
		EasyMock.replay(paramMethodeEvolutionDaoMock);
		
		IParamMethodeEvolutionService paramMethodeEvolutionService = new ParamMethodeEvolutionService(paramMethodeEvolutionDaoMock);
		List<ParamMethodeEvolution> resultat = paramMethodeEvolutionService.findAll();
		
		Assert.assertEquals("renvoie liste methode evolution", resultat, paramMethodeEvolutions);
	}
	@Test
	public  void findById(){
		ParamMethodeEvolution paramMethodeEvolution = new ParamMethodeEvolution();
		
		IParamMethodeEvolutionDAO paramMethodeEvolutionDaoMock = EasyMock.createMock(IParamMethodeEvolutionDAO.class);
		EasyMock.expect(paramMethodeEvolutionDaoMock.findById("1")).andReturn(paramMethodeEvolution);
		EasyMock.replay(paramMethodeEvolutionDaoMock);
		
		IParamMethodeEvolutionService paramMethodeEvolutionService = new ParamMethodeEvolutionService(paramMethodeEvolutionDaoMock);
		ParamMethodeEvolution resultat = paramMethodeEvolutionService.findById("1");
		
		Assert.assertEquals("renvoie methode evolution", resultat, paramMethodeEvolution);
	}

	@Test
	public void findByMethodeEvolution(){
		List<ParamMethodeEvolution> paramMethodeEvolutions = new ArrayList<ParamMethodeEvolution>();
		MethodeEvolution methode = new MethodeEvolution();
		
		IParamMethodeEvolutionDAO paramMethodeEvolutionDaoMock = EasyMock.createMock(IParamMethodeEvolutionDAO.class);
		EasyMock.expect(paramMethodeEvolutionDaoMock.findByMethodeEvolution(methode)).andReturn(paramMethodeEvolutions);
		EasyMock.replay(paramMethodeEvolutionDaoMock);
		
		IParamMethodeEvolutionService paramMethodeEvolutionService = new ParamMethodeEvolutionService(paramMethodeEvolutionDaoMock);
		List<ParamMethodeEvolution> resultat = paramMethodeEvolutionService.findByMethodeEvolution(methode);
		
		Assert.assertEquals("renvoie liste methode evolution", resultat, paramMethodeEvolutions);
	}

}
