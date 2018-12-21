package fr.insee.omphale.core.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class ServiceTest {

	@Test
	public void afficherListe() {
		// init
		List<String> liste = new ArrayList<String>();
		liste.add("un");
		liste.add("deux");
		liste.add("trois");
		// Test de la méthode
		String result = Service.afficherListe(liste);
		// Assertions
		Assert.assertEquals("La liste affichée devrait être : un,deux,trois",
				"un,deux,trois", result);
	}

	@Test
	public void getId() {
		// init
		List<Integer> tab = new ArrayList<Integer>();
		tab.add(100);
		tab.add(101);
		// Test de la méthode
		String result = Service.getId(tab);
		// Assertions
		Assert.assertEquals("La méthode doit renvoyer 100||101", "100||101",
				result);
	}

	@Test
	public void tableauToString() {
		// init
		List<Integer> tab = new ArrayList<Integer>();
		tab.add(1);
		tab.add(2);
		tab.add(3);
		// Test de la méthode
		String result = Service.tableauToString(tab);
		// Assertions
		Assert.assertEquals(
				"Renvoie une chaine de caractère : \"('1', '2', '3')\"",
				"('1', '2', '3')", result);
	}

	@Test
	public void testFormatAgeIsNumOk() {
		// init
		String ageOk = "25";
		// Test de la méthode
		boolean result = Service.testFormatAgeIsNum(ageOk);
		// Assertions
		Assert.assertTrue("L'age \"25\" est conforme", result);
	}

	@Test
	public void testFormatAgeIsNumKo() {
		// init
		String ageKo = "toto";
		// Test de la méthode
		boolean result = Service.testFormatAgeIsNum(ageKo);
		// Assertions
		Assert.assertFalse("L'age \"toto\" n'est pas conforme", result);
	}

	@Test
	public void getMinOk() {
		// init
		Set<Integer> set = new HashSet<Integer>();
		set.add(3);
		set.add(5);
		set.add(8);
		// Test de la méthode
		int result = Service.getMin(set);
		// Assertions
		Assert.assertEquals("Le min doit être 3", 3, result);
	}

	@Test
	public void getMinNull() {
		// Test de la méthode
		int result = Service.getMin(null);
		// Assertions
		Assert.assertEquals("Le min d'un set null doit être 0", 0, result);
	}

	@Test
	public void getMinVide() {
		// init
		Set<Integer> set = new HashSet<Integer>();
		// Test de la méthode
		int result = Service.getMin(set);
		// Assertions
		Assert.assertEquals("Le min d'un set vide doit être 0", 0, result);
	}

	@Test
	public void getMaxOk() {
		// init
		Set<Integer> set = new HashSet<Integer>();
		set.add(3);
		set.add(5);
		set.add(8);
		// Test de la méthode
		int result = Service.getMax(set);
		// Assertions
		Assert.assertEquals("Le max doit être 8", 8, result);
	}

	@Test
	public void getMaxNull() {
		// Test de la méthode
		int result = Service.getMax(null);
		// Assertions
		Assert.assertEquals("Le max d'un set null doit être 0", 0, result);
	}

	@Test
	public void getMaxVide() {
		// init
		Set<Integer> set = new HashSet<Integer>();
		// Test de la méthode
		int result = Service.getMax(set);
		// Assertions
		Assert.assertEquals("Le max d'un set vide doit être 0", 0, result);
	}

	@Test
	public void testFormatNomOk() {
		// init
		String nom = "nom";
		// Test de la méthode
		boolean result = Service.testFormatNom(nom);
		// Assertions
		Assert.assertTrue("Le nom \"nom\" est valide", result);
	}

	@Test
	public void testFormatNomTropLong() {
		// init
		String nom = "nomextremementlongetnonvalide";
		// Test de la méthode
		boolean result = Service.testFormatNom(nom);
		// Assertions
		Assert.assertFalse("Le nom est trop long", result);
	}

	@Test
	public void testFormatNomUnderscore() {
		// init
		String nom = "_nom";
		// Test de la méthode
		boolean result = Service.testFormatNom(nom);
		// Assertions
		Assert.assertFalse(
				"Le nom est invalide car commence par un underscore", result);
	}

	@Test
	public void testFormatNomKo() {
		// init
		String nom = "nom espace";
		// Test de la méthode
		boolean result = Service.testFormatNom(nom);
		// Assertions
		Assert
				.assertFalse("Le nom est invalide car contient un espace",
						result);
	}

	@Test
	public void testFormatAnneeOk() {
		// init
		String annee = "2010";
		// Test de la méthode
		boolean result = Service.testFormatAnnee(annee);
		// Assertions
		Assert.assertTrue("L'année 2010 est valide", result);
	}

	@Test
	public void testFormatAnneeKo() {
		// init
		String annee = "192X";
		// Test de la méthode
		boolean result = Service.testFormatAnnee(annee);
		// Assertions
		Assert.assertFalse("L'année 192X n'est pas valide", result);
	}

	@Test
	public void testGlobalAgeOk() {
		// init
		String age = "50";
		int min = 0;
		int max = 120;
		// Test de la méthode
		boolean result = Service.testGlobalAge(age, min, max);
		// Assertions
		Assert.assertTrue("L'age 50 est entre 0 et 120, donc valide", result);
	}

	@Test
	public void testGlobalAgeTropPetit() {
		// init
		String age = "-5";
		int min = 0;
		int max = 120;
		// Test de la méthode
		boolean result = Service.testGlobalAge(age, min, max);
		// Assertions
		Assert.assertFalse("L'age -5 n'est pas entre 0 et 120, donc invalide",
				result);
	}

	@Test
	public void testGlobalAgeTropGrand() {
		// init
		String age = "128";
		int min = 0;
		int max = 120;
		// Test de la méthode
		boolean result = Service.testGlobalAge(age, min, max);
		// Assertions
		Assert.assertFalse("L'age 128 n'est pas entre 0 et 120, donc invalide",
				result);
	}

	@Test
	public void testGlobalAgeFormat() {
		// init
		String age = "toto";
		int min = 0;
		int max = 120;
		// Test de la méthode
		boolean result = Service.testGlobalAge(age, min, max);
		// Assertions
		Assert.assertFalse("L'age toto a un format incorrect", result);
	}

	@Test
	public void testParametresAnCibleOk() {
		// init
		String codeParam = "AN_CIBLE";
		String valParam = "2010";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertTrue("Le paramètre AN_CIBLE accepte la valeur 2010",
				result);
	}

	@Test
	public void testParametresAnCibleKo() {
		// init
		String codeParam = "AN_CIBLE";
		String valParam = "201X";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertFalse(
				"Le paramètre AN_CIBLE n'accepte pas la valeur 201X", result);
	}

	@Test
	public void testParametresGainICFOk() {
		// init
		String codeParam = "GAIN_ICF";
		String valParam = "5.2";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert
				.assertTrue("Le paramètre GAIN_ICF accepte la valeur 5.2",
						result);
	}

	@Test
	public void testParametresGainICFOk2() {
		// init
		String codeParam = "GAIN_ICF";
		String valParam = "6";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertTrue("Le paramètre GAIN_ICF accepte la valeur 6", result);
	}

	@Test
	public void testParametresGainICFKo() {
		// init
		String codeParam = "GAIN_ICF";
		String valParam = "1.4.3";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertFalse(
				"Le paramètre GAIN_ICF n'accepte pas la valeur 1.4.3", result);
	}

	@Test
	public void testParametresGainEVOk() {
		// init
		String codeParam = "GAIN_EV";
		String valParam = "5";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertTrue("Le paramètre GAIN_EV accepte la valeur 5", result);
	}

	@Test
	public void testParametresGainEVOk2() {
		// init
		String codeParam = "GAIN_EV";
		String valParam = "-30";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertTrue("Le paramètre GAIN_EV accepte la valeur -30", result);
	}

	@Test
	public void testParametresGainEVKo() {
		// init
		String codeParam = "GAIN_EV";
		String valParam = "1.4";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertFalse("Le paramètre AN_CIBLE n'accepte pas la valeur 1.4",
				result);
	}

	@Test
	public void testParametresCoefHomOk() {
		// init
		String codeParam = "COEF_HOM";
		String valParam = "5";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertTrue("Le paramètre COEF_HOM accepte la valeur 5", result);
	}

	@Test
	public void testParametresCoefHomKo() {
		// init
		String codeParam = "COEF_HOM";
		String valParam = "-1";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertFalse("Le paramètre COEF_HOM n'accepte pas la valeur -1",
				result);
	}

	@Test
	public void testParametresCoefHomKo2() {
		// init
		String codeParam = "COEF_HOM";
		String valParam = "1.4";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertFalse("Le paramètre COEF_HOM n'accepte pas la valeur 1.4",
				result);
	}

	@Test
	public void testParametresCoefTranOk() {
		// init
		String codeParam = "COEF_TRAN";
		String valParam = "1.5";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertTrue("Le paramètre COEF_TRAN accepte la valeur 1.5",
				result);
	}

	@Test
	public void testParametresCoefTranKo() {
		// init
		String codeParam = "COEF_TRAN";
		String valParam = "x";
		// Test de la méthode
		boolean result = Service.testParametres(codeParam, valParam);
		// Assertions
		Assert.assertFalse("Le paramètre COEF_TRAN n'accepte pas la valeur x",
				result);
	}

	@Test
	public void getTableau() {
		// init
		String id = "1||2";
		// Test de la méthode
		String[] result = Service.getTableau(id);
		// Assertions
		Assert.assertEquals(
				"Le paramètre tableau de retour a pour premiere valeur 1", "1",
				result[0]);
		Assert.assertEquals(
				"Le paramètre tableau de retour a pour premiere valeur 2", "2",
				result[1]);
	}

	@Test
	public void getTableauInteger() {
		// init
		String id = "1||2";
		// Test de la méthode
		List<Integer> result = Service.getTableauInteger(id);
		// Assertions
		Assert.assertEquals(
				"Le paramètre tableau de retour a pour premiere valeur 1",
				new Integer(1), result.get(0));
		Assert.assertEquals(
				"Le paramètre tableau de retour a pour premiere valeur 2",
				new Integer(2), result.get(1));
	}

	@Test
	public void formaterSexeHomme() {
		// init
		int sexeDebut = 1;
		int sexeFin = 1;
		// Test de la méthode
		String result = Service.formaterSexe(sexeDebut, sexeFin);
		// Assertions
		Assert.assertEquals("sexeDebut=" + sexeDebut + " et sexeFin=" + sexeFin
				+ " -> Homme", "Homme", result);
	}

	@Test
	public void formaterSexeFemme() {
		// init
		int sexeDebut = 2;
		int sexeFin = 2;
		// Test de la méthode
		String result = Service.formaterSexe(sexeDebut, sexeFin);
		// Assertions
		Assert.assertEquals("sexeDebut=" + sexeDebut + " et sexeFin=" + sexeFin
				+ " -> Femme", "Femme", result);
	}

	@Test
	public void formaterSexeHommeEtFemme() {
		// init
		int sexeDebut = 1;
		int sexeFin = 2;
		// Test de la méthode
		String result = Service.formaterSexe(sexeDebut, sexeFin);
		// Assertions
		Assert.assertEquals("sexeDebut=" + sexeDebut + " et sexeFin=" + sexeFin
				+ " -> Homme & Femme", "Homme & Femme", result);
	}

	@Test
	public void getTypes() {
		// Test de la méthode
		Map<Integer, String> result = new Service().getTypes();
		// Assertions
		Assert.assertEquals("libelle non standard", "Personnel", result.get(0));
		Assert.assertEquals("libelle standard", "Standard", result.get(1));
	}

	@Test
	public void testIsOneWeekOld() {
		Date dateAujourdhui = new GregorianCalendar(2010, Calendar.AUGUST, 11)
				.getTime();

		Date dateExec1 = new GregorianCalendar(2010, Calendar.AUGUST, 15)
				.getTime();
		Date dateExec2 = new GregorianCalendar(2010, Calendar.AUGUST, 5)
				.getTime();
		Date dateExec3 = new GregorianCalendar(2010, Calendar.AUGUST, 4)
				.getTime();
		Date dateExec4 = new GregorianCalendar(2010, Calendar.AUGUST, 3)
				.getTime();
		Date dateExec5 = new GregorianCalendar(2009, Calendar.JUNE, 15)
				.getTime();

		Assert
				.assertFalse("1", Service.isOneWeekOld(dateAujourdhui,
						dateExec1));
		Assert
				.assertFalse("2", Service.isOneWeekOld(dateAujourdhui,
						dateExec2));
		Assert.assertTrue("3", Service.isOneWeekOld(dateAujourdhui, dateExec3));
		Assert.assertTrue("4", Service.isOneWeekOld(dateAujourdhui, dateExec4));
		Assert.assertTrue("5", Service.isOneWeekOld(dateAujourdhui, dateExec5));

		dateAujourdhui = new GregorianCalendar(2010, Calendar.JANUARY, 4)
				.getTime();
		dateExec1 = new GregorianCalendar(2010, Calendar.JANUARY, 1).getTime();
		dateExec2 = new GregorianCalendar(2009, Calendar.DECEMBER, 29)
				.getTime();
		dateExec3 = new GregorianCalendar(2009, Calendar.DECEMBER, 15)
				.getTime();
		Assert
				.assertFalse("1", Service.isOneWeekOld(dateAujourdhui,
						dateExec1));
		Assert
				.assertFalse("2", Service.isOneWeekOld(dateAujourdhui,
						dateExec2));
		Assert.assertTrue("3", Service.isOneWeekOld(dateAujourdhui, dateExec3));
	}

	@Test
	public void afficherListeSQLTest() {
		List<String> liste = new ArrayList<String>();
		liste.add("element1");
		liste.add("element2");
		liste.add("element3");

		String resultat = Service.afficherListeSQL(liste);
		Assert.assertEquals("'element1','element2','element3'", resultat);
	}

	@Test
	public void testFormatNom() {
		String nom = "nom";
		Assert.assertTrue("Le test de format du nom : '" + nom
				+ "' devrait renvoyer true", Service.testFormatNom(nom));

		nom = "_nom";
		Assert.assertFalse("Le test de format du nom : '" + nom
				+ "' devrait renvoyer false", Service.testFormatNom(nom));

		nom = "nom espace";
		Assert.assertFalse("Le test de format du nom : '" + nom
				+ "' devrait renvoyer false", Service.testFormatNom(nom));

		nom = "nom_underscore";
		Assert.assertTrue("Le test de format du nom : '" + nom
				+ "' devrait renvoyer true", Service.testFormatNom(nom));
	}

	@Test
	public void testFormatAnnee() {
		String annee = "2010";
		Assert.assertTrue("Le test de format de l'année : '" + annee
				+ "' devrait renvoyer true", Service.testFormatAnnee(annee));

		annee = "toto";
		Assert.assertFalse("Le test de format de l'année : '" + annee
				+ "' devrait renvoyer false", Service.testFormatAnnee(annee));

		annee = "02010";
		Assert.assertFalse("Le test de format de l'année : '" + annee
				+ "' devrait renvoyer false", Service.testFormatAnnee(annee));

		annee = "2";
		Assert.assertFalse("Le test de format de l'année : '" + annee
				+ "' devrait renvoyer false", Service.testFormatAnnee(annee));

	}

	@Test
	public void testFormatAge() {
		String age = "50";
		Assert.assertTrue("Le test de format de l'âge : '" + age
				+ "' devrait renvoyer true", Service.testFormatAge(age));

		age = "age";
		Assert.assertFalse("Le test de format de l'âge : '" + age
				+ "' devrait renvoyer false", Service.testFormatAge(age));

		age = "500";
		Assert.assertFalse("Le test de format de l'âge : '" + age
				+ "' devrait renvoyer false", Service.testFormatAge(age));

		age = "-20";
		Assert.assertFalse("Le test de format de l'âge : '" + age
				+ "' devrait renvoyer false", Service.testFormatAge(age));

	}

	@Test
	public void getMinTest() {
		Set<Integer> tab = new HashSet<Integer>();
		tab.add(4);
		tab.add(3);
		tab.add(2);
		tab.add(1);
		Assert.assertEquals("Le min est 1", 1, Service.getMin(tab));
	}

	@Test
	public void getMaxTest() {
		Set<Integer> tab = new HashSet<Integer>();
		tab.add(4);
		tab.add(3);
		tab.add(2);
		tab.add(1);
		Assert.assertEquals("Le max est 4", 4, Service.getMax(tab));
	}

	@Test
	public void getIdTest() {
		
		List<Integer> tableau;
		
		{ tableau = new ArrayList<Integer>();
		tableau.add(1);
		tableau.add(22);
		tableau.add(333); };
		String resultat = Service.getId(tableau);
		Assert.assertEquals("1||22||333", resultat);

		List<Integer> tableau2 = new ArrayList<Integer>();
		tableau2.add(0);
		resultat = Service.getId(tableau2);
		Assert.assertEquals("le tableau devrait être à vide","0", resultat);

		tableau = null;
		;
		resultat = Service.getId(tableau);
		Assert.assertEquals("", resultat);
	}

	@Test
	public void tableauToStringTest() {
		List<Integer> tableau;
		
		{ tableau = new ArrayList<Integer>();
		tableau.add(1);
		tableau.add(22);
		tableau.add(333); };
		String resultat = Service.tableauToString(tableau);
		Assert.assertEquals("('1', '22', '333')", resultat);
	}

	@Test
	public void removeTest() {
		Integer[] tableau1 = { new Integer(1), new Integer(22),
				new Integer(333) };
		Integer[] tableau2 = { new Integer(22), new Integer(333),
				new Integer(4444) };
		Integer[] resultat = Service.remove(tableau1, tableau2);
		Assert.assertEquals(resultat.length, 1);
		Assert.assertEquals((resultat[0]).intValue(), 1);
	}

	@Test
	public void getTableauTest() {
		String str = "1||22||333";
		String[] resultat = Service.getTableau(str);
		Assert.assertEquals(resultat.length, 3);
		Assert.assertEquals(resultat[0], "1");
		Assert.assertEquals(resultat[1], "22");
		Assert.assertEquals(resultat[2], "333");

		str = "";
		resultat = Service.getTableau(str);
		Assert.assertEquals(resultat.length, 0);

		str = null;
		resultat = Service.getTableau(str);
		Assert.assertEquals(resultat.length, 0);
	}

	@Test
	public void getTableauIntegerTest() {
		String str = "1||22||333";
		List<Integer> resultat = Service.getTableauInteger(str);
		Assert.assertEquals(resultat.size(), 3);
		Assert.assertEquals(resultat.get(0).intValue(), 1);
		Assert.assertEquals(resultat.get(1).intValue(), 22);
		Assert.assertEquals(resultat.get(2).intValue(), 333);

		str = "";
		resultat = Service.getTableauInteger(str);
		Assert.assertEquals(resultat.size(), 0);

		str = null;
		resultat = Service.getTableauInteger(str);
		Assert.assertEquals(resultat.size(), 0);
	}
}
