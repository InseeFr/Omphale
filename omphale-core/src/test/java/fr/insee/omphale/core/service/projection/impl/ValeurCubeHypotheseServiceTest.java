package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.dao.projection.IValeurCubeHypotheseDAO;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.ValeurCubeHypothese;

public class ValeurCubeHypotheseServiceTest {

	@Test
	public void insertOrUpdate() {
		// Init
		ValeurCubeHypothese valeur = new ValeurCubeHypothese();

		IValeurCubeHypotheseDAO valeurDAOMock = EasyMock
				.createMock(IValeurCubeHypotheseDAO.class);
		EasyMock.expect(valeurDAOMock.insertOrUpdate(EasyMock.eq(valeur)))
				.andReturn(valeur);
		EasyMock.replay(valeurDAOMock);
		// Test de la méthode
		ValeurCubeHypotheseService valeurService = new ValeurCubeHypotheseService(
				valeurDAOMock);
		ValeurCubeHypothese result = valeurService.insertOrUpdate(valeur);
		// Assertions
		EasyMock.verify(valeurDAOMock);
		Assert.assertEquals(valeur, result);
	}

	@Test
	public void findByHypothese() {
		// Init
		List<ValeurCubeHypothese> valeurs = new ArrayList<ValeurCubeHypothese>();

		Hypothese hypothese = new Hypothese();

		IValeurCubeHypotheseDAO valeurDAOMock = EasyMock
				.createMock(IValeurCubeHypotheseDAO.class);
		EasyMock.expect(valeurDAOMock.findByHypothese(EasyMock.eq(hypothese)))
				.andReturn(valeurs);
		EasyMock.replay(valeurDAOMock);
		// Test de la méthode
		ValeurCubeHypotheseService valeurService = new ValeurCubeHypotheseService(
				valeurDAOMock);
		List<ValeurCubeHypothese> result = valeurService
				.findByHypothese(hypothese);
		// Assertions
		EasyMock.verify(valeurDAOMock);
		Assert.assertEquals(valeurs, result);
	}

}
