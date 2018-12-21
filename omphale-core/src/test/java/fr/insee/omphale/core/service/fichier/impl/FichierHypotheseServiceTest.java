package fr.insee.omphale.core.service.fichier.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.insee.omphale.core.service.fichier.IFichierHypotheseService;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.TypeEntite;

public class FichierHypotheseServiceTest {


	private IFichierHypotheseService fichierService;

	@Before
	public void init() {
		fichierService = new FichierHypotheseService();
	}


	@Test
	public void testExhaustiviteOk() {
		Map<Integer, Map<Integer, Map<Integer, Double>>> valeurs = new HashMap<Integer, Map<Integer, Map<Integer, Double>>>();

		// On crée des valeurs de fichier : annee 2010, ages allant de 20 a 22,
		// sexe allant de 1 a 2 (donc exhaustif)
		valeurs.put(2010, new HashMap<Integer, Map<Integer, Double>>());
		valeurs.get(2010).put(20, new HashMap<Integer, Double>());
		valeurs.get(2010).get(20).put(1, new Double(1));
		valeurs.get(2010).get(20).put(2, new Double(1));
		valeurs.get(2010).put(21, new HashMap<Integer, Double>());
		valeurs.get(2010).get(21).put(1, new Double(1));
		valeurs.get(2010).get(21).put(2, new Double(1));
		valeurs.get(2010).put(22, new HashMap<Integer, Double>());
		valeurs.get(2010).get(22).put(1, new Double(1));
		valeurs.get(2010).get(22).put(2, new Double(1));

		TypeEntite typeEntite = new TypeEntite();
		typeEntite.setAge(true);
		typeEntite.setSexe(true);
		Hypothese hypothese = new Hypothese();
		hypothese.setTypeEntite(typeEntite);

		List<String> resultat = fichierService.testExhaustivite(valeurs,
				hypothese);
		Assert.assertTrue(
				"Les valeurs sont exhaustives : le resultat devrait être vide",
				resultat.isEmpty());
	}

	@Test
	public void testExhaustiviteNonOk() {
		Map<Integer, Map<Integer, Map<Integer, Double>>> valeurs = new HashMap<Integer, Map<Integer, Map<Integer, Double>>>();

		// On crée des valeurs de fichier : annee 2010, ages allant de 20 a 22,
		// sexe allant de 1 a 2 (on enleve le croisement 2010 , 22, 1)
		valeurs.put(2010, new HashMap<Integer, Map<Integer, Double>>());
		valeurs.get(2010).put(20, new HashMap<Integer, Double>());
		valeurs.get(2010).get(20).put(1, new Double(1));
		valeurs.get(2010).get(20).put(2, new Double(1));
		valeurs.get(2010).put(21, new HashMap<Integer, Double>());
		valeurs.get(2010).get(21).put(1, new Double(1));
		valeurs.get(2010).get(21).put(2, new Double(1));
		valeurs.get(2010).put(22, new HashMap<Integer, Double>());
		valeurs.get(2010).get(22).put(2, new Double(1));

		TypeEntite typeEntite = new TypeEntite();
		typeEntite.setAge(true);
		typeEntite.setSexe(true);
		Hypothese hypothese = new Hypothese();
		hypothese.setTypeEntite(typeEntite);

		List<String> resultat = fichierService.testExhaustivite(valeurs,
				hypothese);
		Assert.assertTrue(
				"Les valeurs ne sont pas exhaustives : il manque 2010|22|1",
				resultat.contains("2010|22|1"));

		hypothese.setAgeFin(21);
		List<String> resultat2 = fichierService.testExhaustivite(valeurs,
				hypothese);
		Assert
				.assertTrue(
						"Les valeurs devraient être exhaustives avec l'age de fin positionné à 21",
						resultat2.isEmpty());

	}
}
