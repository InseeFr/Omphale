package fr.insee.omphale.core.service.geographie.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.geographie.ICommuneService;
import fr.insee.omphale.dao.geographie.ICommuneDAO;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;

public class CommuneServiceTest {

	@Test
	public void findIdNonPresents() {
		// Init
		List<String> argument = new ArrayList<String>();
		argument.add("1");
		argument.add("2");
		List<String> nonPresents = new ArrayList<String>();
		nonPresents.add("2");
		ICommuneDAO communeDAOMock = EasyMock.createMock(ICommuneDAO.class);
		EasyMock
				.expect(communeDAOMock.findIdNonPresents(EasyMock.eq(argument)))
				.andReturn(nonPresents);
		EasyMock.replay(communeDAOMock);
		// Test de la méthode
		ICommuneService communeService = new CommuneService(communeDAOMock);
		List<String> result = communeService.findIdNonPresents(argument);
		// Assertions
		Assert.assertTrue("Les id non présent doivent contenir 2", result
				.contains("2"));
		Assert.assertEquals("Les id non présent ne contiennent qu'un élément",
				1, result.size());
	}

	@Test
	public void findById() {
		// Init
		Commune commune = new Commune();
		ICommuneDAO communeDAOMock = EasyMock.createMock(ICommuneDAO.class);
		EasyMock.expect(communeDAOMock.findById("1")).andReturn(commune);
		EasyMock.replay(communeDAOMock);
		// Test de la méthode
		ICommuneService communeService = new CommuneService(communeDAOMock);
		Commune result = communeService.findById("1");
		// Assertions
		Assert.assertEquals(commune, result);
	}

	@Test
	public void findAllByDepartement() {
		// Init
		Commune commune1 = new Commune();
		Commune commune2 = new Commune();
		List<Commune> communes = new ArrayList<Commune>();
		communes.add(commune1);
		communes.add(commune2);

		Departement departement = new Departement();

		ICommuneDAO communeDAOMock = EasyMock.createMock(ICommuneDAO.class);
		EasyMock.expect(
				communeDAOMock.findAllByDepartement(EasyMock.eq(departement)))
				.andReturn(communes);
		EasyMock.replay(communeDAOMock);
		// Test de la méthode
		ICommuneService communeService = new CommuneService(communeDAOMock);
		List<Commune> result = communeService.findAllByDepartement(departement);
		// Assertions
		Assert.assertEquals(communes, result);
	}

	@Test
	public void findByIdListe() {
		// Init
		Commune commune = new Commune("1", "", null);
		List<String> idCommunes = new ArrayList<String>();
		idCommunes.add(commune.getId());

		List<Commune> communes = new ArrayList<Commune>();
		communes.add(commune);

		ICommuneDAO communeDAOMock = EasyMock.createMock(ICommuneDAO.class);
		EasyMock.expect(communeDAOMock.findById(EasyMock.eq(idCommunes)))
				.andReturn(communes);
		EasyMock.replay(communeDAOMock);
		// Test de la méthode
		ICommuneService communeService = new CommuneService(communeDAOMock);
		List<Commune> result = communeService.findById(idCommunes);
		// Assertions
		Assert.assertEquals(communes, result);
	}
}
