
package fr.insee.omphale.core.service.projection.impl;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IProjectionLanceeService;
import fr.insee.omphale.core.service.projection.IProjectionService;
import fr.insee.omphale.dao.projection.IProjectionDAO;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.projection.EvolutionLocalisee;
import fr.insee.omphale.domaine.projection.Projection;


public class ProjectionServiceTest {

	@Test
	public void findById(){
		Projection projection = new Projection();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.findById(1)).andReturn(projection).anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		Projection resultat = projectionService.findById(1);
		
		Assert.assertEquals("renvoie la projection", resultat, projection);
	}

	@Test
	public void findAll(){
		List<Projection> projections = new ArrayList<Projection>();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.findAll("toto")).andReturn(projections).anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		List<Projection> resultat = projectionService.findAll("toto");
		
		Assert.assertEquals("renvoie la projection", resultat, projections);
	}

	@Test
	public void findAllIdUserIdZonage(){
		List<Projection> projections = new ArrayList<Projection>();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.findAll("toto", "titi")).andReturn(projections).anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		List<Projection> resultat = projectionService.findAll("toto", "titi");
		
		Assert.assertEquals("renvoie la projection", resultat, projections);
	}

	@Test
	public void getListe(){
		List<Projection> projections = new ArrayList<Projection>();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.getListe(1)).andReturn(projections).anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		List<Projection> resultat = projectionService.getListe(1);
		
		Assert.assertEquals("renvoie la projection", resultat, projections);
	}

	@Test
	public void testerNomProjection(){
		List<Projection> projections = new ArrayList<Projection>();
		
		IProjectionDAO projectionDaoMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDaoMock.findAll("toto")).andReturn(projections).anyTimes();
		EasyMock.replay(projectionDaoMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDaoMock);
		boolean resultat = projectionService.testerNomProjection("toto","toto", projectionService);
		
		Assert.assertTrue("renvoie vraie", resultat);
		
		Projection projection = new Projection();
		projection.setNom("toto");
		projections.add(projection);
		
		boolean resultat2 = projectionService.testerNomProjection("toto","toto", projectionService);
		
		Assert.assertFalse("renvoie faux", resultat2);
	}
	
	@Test
	public void findEtalons(){
		List<Projection> projections = new ArrayList<Projection>();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.getListe(1)).andReturn(projections).anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		List<Projection> resultat = projectionService.getListe(1);
		
		Assert.assertEquals("renvoie la liste de projection", resultat, projections);
	}

	@Test
	public void insertOrUpdate(){
		Projection projection = new Projection();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.insertOrUpdate(projection)).andReturn(projection).anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		Projection resultat = projectionService.insertOrUpdate(projection);
		
		Assert.assertEquals("renvoie la projection", resultat, projection);
	}

	@Test
	public void getListeDonnees(){
		TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>();
		treeMap.put(0, "Pas de données exportées");
		treeMap.put(1, "Export Partiel");
		treeMap.put(2, "Export Complet");
		
	
		IProjectionService projectionService = new ProjectionService();
		TreeMap<Integer, String> resultat = projectionService.getListeDonnees();
		
		Assert.assertEquals("renvoie la projection", resultat, treeMap);
	}

	@Test
	public void findAllZonage(){
		
		List<Projection> projections = new ArrayList<Projection>();
		Zonage zonage = new Zonage();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.findAll(zonage)).andReturn(projections).anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		List<Projection> resultat = projectionService.findAll(zonage);
		
		Assert.assertEquals("renvoie la liste de projection", resultat, projections);
	}

	@Test
	public void findByProjectionEnglobante(){
		
		List<Projection> projections = new ArrayList<Projection>();
		Projection projection = new Projection();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.findByProjectionEnglobante(projection)).andReturn(projections).anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		List<Projection> resultat = projectionService.findByProjectionEnglobante(projection);
		
		Assert.assertEquals("renvoie la liste de projection", resultat, projections);
	}

	@Test
	public void findByProjectionEtalon(){
		
		List<Projection> projections = new ArrayList<Projection>();
		Projection projection = new Projection();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.findByProjectionEtalon(projection)).andReturn(projections).anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		List<Projection> resultat = projectionService.findByProjectionEtalon(projection);
		
		Assert.assertEquals("renvoie la liste de projection", resultat, projections);
	}
	
	@Test
	public void findPopulation(){
		Projection projection = new Projection();
		
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.findPopulation(projection)).andReturn("Eleves").anyTimes();
		EasyMock.replay(projectionDAOMock);
		
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		String resultat = projectionService.findPopulation(projection);
		
		Assert.assertEquals("renvoie la liste de projection", resultat, "Eleves");
	}
	
	@Test
	public void testFindAllEnglobante(){
		
		// Valeur en entrée
		String idUser = "007";
		Integer anneeRef = 2006;
		Integer anneeHorizon = 2010;
		
		List<Projection> projections = new ArrayList<Projection>();
		Projection proj1 = new Projection();
		proj1.setEnglobante(9);
		proj1.setAnneeReference(2007);
		proj1.setAnneeHorizon(2011);
		projections.add(proj1);
		Projection proj2 = new Projection();
		proj2.setEnglobante(9);
		proj2.setAnneeReference(2005);
		proj2.setAnneeHorizon(2009);
		projections.add(proj2);
		Projection proj3 = new Projection();
		proj3.setEnglobante(1);
		proj3.setAnneeReference(2005);
		proj3.setAnneeHorizon(2011);
		projections.add(proj3);
		Projection proj4 = new Projection();
		proj4.setEnglobante(9);
		proj4.setAnneeReference(2005);
		proj4.setAnneeHorizon(2011);
		projections.add(proj4);
		
		// mock
		IProjectionDAO projectionDAOMock = EasyMock.createMock(IProjectionDAO.class);
		EasyMock.expect(projectionDAOMock.findAll(idUser)).andReturn(projections);
		EasyMock.replay(projectionDAOMock);
		
		// lancement de la méthode à tester
		IProjectionService projectionService = new ProjectionService(projectionDAOMock);
		List<Projection> resultat = projectionService.findAllEnglobante(idUser, anneeRef, anneeHorizon);
		
		Assert.assertEquals("proj4 de projections doit être égale au seul objet liste generee : ", resultat.get(0), projections.get(3));
		Assert.assertEquals("la projection doit être englobante standard à 9", 9, resultat.get(0).getEnglobante());
		Assert.assertEquals("annee horizon doit être égale à 2011",2011, resultat.get(0).getAnneeHorizon());
		Assert.assertEquals("annee reference doit etre egale à 2005", 2005, resultat.get(0).getAnneeReference());	
	}
	
	@Test
	public void testSupprimerProjectionsUtilisantProjectionEnglobantes() throws Exception{
		
		
        // database connection
        @SuppressWarnings({ "unused", "rawtypes" })
		Class driverClass = Class.forName("org.hsqldb.jdbcDriver");

		Projection projectionEnglobante = new Projection();
		
		Set<EvolutionLocalisee> evollocs = new HashSet<EvolutionLocalisee>();
		evollocs.add(new EvolutionLocalisee());
		
		List<Projection> projectionsUtilisantEnglobante = new ArrayList<Projection>();
		for (int i = 0; i > 2 ; i++){
			Projection p = new Projection();
			p.setProjectionEnglobante(projectionEnglobante);
			p.setEvolutionsLocalisees(evollocs);
			projectionsUtilisantEnglobante.add(p);
			for (@SuppressWarnings("unused")
			int j = 0; i > 2 ; i++){
				Projection pr = new Projection();
				pr.setProjectionEnglobante(projectionEnglobante);
				pr.setEvolutionsLocalisees(evollocs);
				projectionsUtilisantEnglobante.add(pr);
			}
		}
		Projection pr = new Projection();
		projectionsUtilisantEnglobante.add(pr);
	
		@SuppressWarnings("unused")
		IProjectionLanceeService mockProjectionLanceeService = mock(IProjectionLanceeService.class);		
	}
	
	
}
