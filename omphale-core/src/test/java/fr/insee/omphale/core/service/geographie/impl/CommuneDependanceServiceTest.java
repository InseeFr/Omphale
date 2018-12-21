package fr.insee.omphale.core.service.geographie.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.insee.omphale.core.service.geographie.ICommuneDependanceService;
import fr.insee.omphale.dao.geographie.ICommuneDependanceDAO;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.CommuneDependance;

public class CommuneDependanceServiceTest {

	private Set<Commune> communes;
	private Commune com1;
	private Commune com2;
	private Commune com3;
	private CommuneDependance cd1_2;
	private CommuneDependance cd2_3;

	@Before
	public void scenario() {
		// Le scénario est le suivant : 3 communes (com1, com2, com3) et 2
		// dépendances (cd1_2 qui relie com1 et com2, et cd2_3 qui relie com2 et
		// com3).
		communes = new HashSet<Commune>();
		com1 = new Commune("c1", "", null);
		com2 = new Commune("c2", "", null);
		com3 = new Commune("c3", "", null);
		communes.add(com1);

		cd1_2 = new CommuneDependance();
		cd1_2.setDependance(1);
		Set<Commune> communesCd1_2 = new HashSet<Commune>();
		communesCd1_2.add(com1);
		communesCd1_2.add(com2);
		cd1_2.setCommunes(communesCd1_2);

		cd2_3 = new CommuneDependance();
		cd2_3.setDependance(2);
		Set<Commune> communesCd2_3 = new HashSet<Commune>();
		communesCd2_3.add(com2);
		communesCd2_3.add(com3);
		cd2_3.setCommunes(communesCd2_3);
	}

	@Test
	public void trouverFreresProches() {
		// Init
		List<String> idCommunes = new ArrayList<String>();
		idCommunes.add(com1.getId());

		List<CommuneDependance> dependances = new ArrayList<CommuneDependance>();
		dependances.add(cd1_2);

		List<Integer> idDependances = new ArrayList<Integer>();
		idDependances.add(cd1_2.getDependance());

		ICommuneDependanceDAO cdDAOMock = EasyMock
				.createMock(ICommuneDependanceDAO.class);
		EasyMock.expect(cdDAOMock.findIdByIdCommunes(EasyMock.eq(idCommunes)))
				.andReturn(idDependances);
		EasyMock.expect(cdDAOMock.findById(EasyMock.eq(idDependances)))
				.andReturn(dependances);
		EasyMock.replay(cdDAOMock);
		// Test de la méthode
		ICommuneDependanceService cdService = new CommuneDependanceService(
				cdDAOMock);
		Set<Commune> result = cdService.trouverFreresProches(communes);
		// Assertions
		Assert.assertTrue(
				"La recherche de frères proches devrait trouver com2", result
						.contains(com2));
		Assert
				.assertEquals(
						"La recherche de frères proche devrait ramener 2 resultats seulement",
						2, result.size());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void findByCommunes() {
		// Init
		Set<Commune> communesEtape1 = new HashSet<Commune>();
		communesEtape1.add(com1);
		communesEtape1.add(com2);
		Set<Commune> communesEtape2 = new HashSet<Commune>();
		communesEtape2.add(com1);
		communesEtape2.add(com2);
		communesEtape2.add(com3);

		List<String> idCommunes = new ArrayList<String>();
		idCommunes.add(com1.getId());
		idCommunes.add(com2.getId());
		idCommunes.add(com3.getId());

		List<Integer> idDependances = new ArrayList<Integer>();
		idDependances.add(cd1_2.getDependance());
		idDependances.add(cd2_3.getDependance());

		List<CommuneDependance> dependances = new ArrayList<CommuneDependance>();
		dependances.add(cd1_2);
		dependances.add(cd2_3);

		ICommuneDependanceService cdServiceMock = EasyMock
				.createMock(ICommuneDependanceService.class);
		EasyMock.expect(cdServiceMock.trouverFreresProches(communes))
				.andReturn(communesEtape1);
		EasyMock
				.expect(
						cdServiceMock.trouverFreresProches(EasyMock
								.eq(communesEtape1))).andReturn(communesEtape2);
		EasyMock
				.expect(
						cdServiceMock.trouverFreresProches(EasyMock
								.eq(communesEtape2))).andReturn(communesEtape2);

		ICommuneDependanceDAO cdDAOMock = EasyMock
				.createMock(ICommuneDependanceDAO.class);
		EasyMock.expect(
				cdDAOMock.findIdByIdCommunes((List<String>) EasyMock
						.anyObject())).andReturn(idDependances);
		EasyMock.expect(cdDAOMock.findById(EasyMock.eq(idDependances)))
				.andReturn(dependances);

		EasyMock.replay(cdServiceMock, cdDAOMock);
		// Test de la méthode
		ICommuneDependanceService cdService = new CommuneDependanceService(
				cdDAOMock);
		List<CommuneDependance> result = cdService.findByCommunes(
				new ArrayList<Commune>(communes), cdServiceMock);
		// Assertions
		Assert.assertTrue(
				"La recherche de communes dépendances devrait trouver cd2_3",
				result.contains(cd2_3));
		Assert
				.assertEquals(
						"La recherche de communes dépendances devrait ramener 2 resultats seulement",
						2, result.size());
	}
}
