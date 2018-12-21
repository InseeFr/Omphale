package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IMethodeService;
import fr.insee.omphale.dao.projection.IMethodeDAO;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.MethodeEvolution;

public class MethodeServiceTest {

	@Test
	public void findAll(){
		List<MethodeEvolution> methodes = new ArrayList<MethodeEvolution>();
		
		IMethodeDAO methodeDaoMock = EasyMock.createMock(IMethodeDAO.class);
		EasyMock.expect(methodeDaoMock.findAll()).andReturn(methodes);
		EasyMock.replay(methodeDaoMock);
		
		IMethodeService methodeService = new MethodeService(methodeDaoMock);
		List<MethodeEvolution> resultat = methodeService.findAll();
		
		Assert.assertEquals("renvoie liste methode evolution", resultat, methodes);
	}

	@Test
	public  void findById(){
		MethodeEvolution methode = new MethodeEvolution();
		
		IMethodeDAO methodeDaoMock = EasyMock.createMock(IMethodeDAO.class);
		EasyMock.expect(methodeDaoMock.findById("1")).andReturn(methode);
		EasyMock.replay(methodeDaoMock);
		
		IMethodeService methodeService = new MethodeService(methodeDaoMock);
		MethodeEvolution resultat = methodeService.findById("1");
		
		Assert.assertEquals("renvoie methode evolution", resultat, methode);
	}

	@Test
	public void findByComposante(){
		List<MethodeEvolution> methodes = new ArrayList<MethodeEvolution>();
		Composante composante = new Composante();
		
		IMethodeDAO methodeDaoMock = EasyMock.createMock(IMethodeDAO.class);
		EasyMock.expect(methodeDaoMock.findByComposante(composante)).andReturn(methodes);
		EasyMock.replay(methodeDaoMock);
		
		IMethodeService methodeService = new MethodeService(methodeDaoMock);
		List<MethodeEvolution> resultat = methodeService.findByComposante(composante);
		
		Assert.assertEquals("renvoie liste methode evolution", resultat, methodes);
	}
	
}
