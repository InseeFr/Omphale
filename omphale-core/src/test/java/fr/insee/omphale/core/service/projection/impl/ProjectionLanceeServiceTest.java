package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IProjectionLanceeService;
import fr.insee.omphale.dao.projection.IProjectionLanceeDAO;
import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.domaine.projection.ProjectionLancee;

public class ProjectionLanceeServiceTest {
	
	@Test
	public void exist(){
		List<ProjectionLancee> projectionsLancees = new ArrayList<ProjectionLancee>();
		ProjectionLancee projectionLancee = new ProjectionLancee();
		projectionsLancees.add(projectionLancee);
		
		IProjectionLanceeDAO projectionLanceeDAOMock = EasyMock.createMock(IProjectionLanceeDAO.class);
		EasyMock.expect(projectionLanceeDAOMock.findAll(1)).andReturn(projectionsLancees).anyTimes();
		EasyMock.replay(projectionLanceeDAOMock);
		
		IProjectionLanceeService projectionLanceeService = new ProjectionLanceeService(projectionLanceeDAOMock);
		boolean resultat = projectionLanceeService.exist(1);
		
		Assert.assertTrue(resultat);
	}
	
	@Test
	public void findAll(){
		List<ProjectionLancee> projectionsLancees = new ArrayList<ProjectionLancee>();
		ProjectionLancee projectionLancee = new ProjectionLancee();
		projectionsLancees.add(projectionLancee);
		
		IProjectionLanceeDAO projectionLanceeDAOMock = EasyMock.createMock(IProjectionLanceeDAO.class);
		EasyMock.expect(projectionLanceeDAOMock.findAll("toto")).andReturn(projectionsLancees).anyTimes();
		EasyMock.replay(projectionLanceeDAOMock);
		
		IProjectionLanceeService projectionLanceeService = new ProjectionLanceeService(projectionLanceeDAOMock);
		@SuppressWarnings("rawtypes")
		List resultat = projectionLanceeService.findAll("toto");
		
		Assert.assertEquals("liste de projections lancées doit être renvoyée", resultat, projectionsLancees);
	}
	
	@Test
	public void setAnnulationUtilisateur(){
		ProjectionLancee projectionLancee = new ProjectionLancee();
		projectionLancee.setCodeRetour(null);
		
		IProjectionLanceeService projectionLanceeService = new ProjectionLanceeService();
		projectionLanceeService.setAnnulationUtilisateur(projectionLancee);
		
		Assert.assertEquals("le code retour de la projection lancée est passé à 60", projectionLancee.getCodeRetour(), "60");
	}
	

	@Test
	public void findByProjection(){
		Projection projection = new Projection();
		List<ProjectionLancee> projectionsLancees = new ArrayList<ProjectionLancee>();
		
		IProjectionLanceeDAO projectionLanceeDAOMock = EasyMock.createMock(IProjectionLanceeDAO.class);
		EasyMock.expect(projectionLanceeDAOMock.findByProjection(projection)).andReturn(projectionsLancees).anyTimes();
		EasyMock.replay(projectionLanceeDAOMock);
		
		IProjectionLanceeService projectionLanceeService = new ProjectionLanceeService(projectionLanceeDAOMock);
		List<ProjectionLancee> resultat = projectionLanceeService.findByProjection(projection);
		
		Assert.assertEquals("projectionsLancees doit être renvoyé", resultat, projectionsLancees);
	}
	
}
