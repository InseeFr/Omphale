package fr.insee.omphale.core.service.geographie.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.insee.omphale.core.service.geographie.IZonageService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.dao.geographie.IZonageDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.EEtatValidation;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.geographie.ZoneClassique;

public class ZonageServiceTest {

	private static ZonageService zonageService;

	@BeforeClass
	public static void initGeneral() {
		zonageService = new ZonageService();
	}

	@Test
	public void verifierZonesDisjointesTestOk() {
		Commune com1 = new Commune("11111", null, null);
		Commune com2 = new Commune("22222", null, null);
		Commune com3 = new Commune("33333", null, null);
		Commune com4 = new Commune("44444", null, null);
		Commune com5 = new Commune("55555", null, null);
		Commune com6 = new Commune("66666", null, null);

		Zone z1 = new ZoneClassique("z1", "zone1", null, null, null, null, null);
		Zone z2 = new ZoneClassique("z2", "zone2", null, null, null, null, null);
		Zone z3 = new ZoneClassique("z3", "zone3", null, null, null, null, null);

		Set<Commune> communesZone1 = new HashSet<Commune>();
		Set<Commune> communesZone2 = new HashSet<Commune>();
		Set<Commune> communesZone3 = new HashSet<Commune>();

		communesZone1.add(com1);
		communesZone1.add(com2);
		communesZone2.add(com3);
		communesZone2.add(com4);
		communesZone3.add(com5);
		communesZone3.add(com6);

		z1.setCommunes(communesZone1);
		z2.setCommunes(communesZone2);
		z3.setCommunes(communesZone3);

		Set<Zone> zones = new HashSet<Zone>();
		zones.add(z1);
		zones.add(z2);
		zones.add(z3);

		Zonage zonage = new Zonage();
		zonage.setZones(zones);

		IZoneService zoneServiceMock = EasyMock.createMock(IZoneService.class);
		EasyMock.expect(zoneServiceMock.findById("z1")).andReturn(z1)
				.anyTimes();
		EasyMock.expect(zoneServiceMock.findById("z2")).andReturn(z2)
				.anyTimes();
		EasyMock.expect(zoneServiceMock.findById("z3")).andReturn(z3)
				.anyTimes();

		EasyMock.replay(zoneServiceMock);

		Map<String, String> resultat = zonageService.verifierZonesDisjointes(
				zonage, zoneServiceMock);

		Assert
				.assertNull(
						"verifierZonesDisjointes devrait renvoyer null car les zones sont bien disjointes",
						resultat);
	}

	@Test
	public void verifierZonesDisjointesTestError() {
		Commune com1 = new Commune("11111", null, null);
		Commune com2 = new Commune("22222", null, null);
		Commune com3 = new Commune("33333", null, null);
		Commune com4 = new Commune("44444", null, null);
		Commune com5 = new Commune("55555", null, null);
		Commune com6 = new Commune("66666", null, null);

		Zone z1 = new ZoneClassique("z1", "zone1", null, null, null, null, null);
		Zone z2 = new ZoneClassique("z2", "zone2", null, null, null, null, null);
		Zone z3 = new ZoneClassique("z3", "zone3", null, null, null, null, null);

		Set<Commune> communesZone1 = new HashSet<Commune>();
		Set<Commune> communesZone2 = new HashSet<Commune>();
		Set<Commune> communesZone3 = new HashSet<Commune>();

		communesZone1.add(com1);
		communesZone1.add(com2);
		communesZone1.add(com3);
		communesZone1.add(com5);

		communesZone2.add(com3);
		communesZone2.add(com4);

		communesZone3.add(com5);
		communesZone3.add(com6);

		z1.setCommunes(communesZone1);
		z2.setCommunes(communesZone2);
		z3.setCommunes(communesZone3);

		Set<Zone> zones = new HashSet<Zone>();
		zones.add(z1);
		zones.add(z2);
		zones.add(z3);

		Zonage zonage = new Zonage();
		zonage.setZones(zones);

		IZoneService zoneServiceMock = EasyMock.createMock(IZoneService.class);
		EasyMock.expect(zoneServiceMock.findById("z1")).andReturn(z1)
				.anyTimes();
		EasyMock.expect(zoneServiceMock.findById("z2")).andReturn(z2)
				.anyTimes();
		EasyMock.expect(zoneServiceMock.findById("z3")).andReturn(z3)
				.anyTimes();

		EasyMock.replay(zoneServiceMock);

		Map<String, String> resultat = zonageService.verifierZonesDisjointes(
				zonage, zoneServiceMock);

		Assert.assertEquals(
				"On devrait avoir 2 communes présentes dans plusieurs zones",
				2, resultat.keySet().size());
		Assert.assertTrue("La commune " + com3.getId()
				+ " doit être détectée comme présente dans plusieurs zones",
				resultat.keySet().contains(com3.getId()));
		Assert.assertTrue("La commune " + com5.getId()
				+ " doit être détectée comme présente dans plusieurs zones",
				resultat.keySet().contains(com5.getId()));
	}

	@Test
	public void testerNomZonageOk() {
		// Init
		Utilisateur utilisateur = new Utilisateur();

		List<Zonage> zonagesUtilisateur = new ArrayList<Zonage>();
		Zonage zonage = new Zonage();
		zonage.setNom("nom");
		zonagesUtilisateur.add(zonage);

		IZonageDAO zonageDAOMock = EasyMock.createMock(IZonageDAO.class);
		EasyMock.expect(
				zonageDAOMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonagesUtilisateur);
		EasyMock.replay(zonageDAOMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(zonageDAOMock);
		Boolean result = zonageService.testerNomZonage(utilisateur, "toto");
		// Assertions
		Assert.assertTrue("Le nom de zonage \"toto\" est valide", result);
	}

	@Test
	public void testerNomZonageKo() {
		// Init
		Utilisateur utilisateur = new Utilisateur();

		List<Zonage> zonagesUtilisateur = new ArrayList<Zonage>();
		Zonage zonage = new Zonage();
		zonage.setNom("nom");
		zonagesUtilisateur.add(zonage);

		IZonageDAO zonageDAOMock = EasyMock.createMock(IZonageDAO.class);
		EasyMock.expect(
				zonageDAOMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonagesUtilisateur);
		EasyMock.replay(zonageDAOMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(zonageDAOMock);
		Boolean result = zonageService.testerNomZonage(utilisateur, "nom");
		// Assertions
		Assert.assertFalse(
				"Le nom de zonage \"nom\" est déjà utilisé, donc non valide",
				result);
	}

	@Test
	public void insertOrUpdate() {
		// Init
		Zonage zonage = new Zonage();

		IZonageDAO zonageDAOMock = EasyMock.createMock(IZonageDAO.class);
		EasyMock.expect(zonageDAOMock.insertOrUpdate(EasyMock.eq(zonage)))
				.andReturn(zonage);
		EasyMock.replay(zonageDAOMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(zonageDAOMock);
		Zonage result = zonageService.insertOrUpdate(zonage);
		// Assertions
		EasyMock.verify(zonageDAOMock);
		Assert.assertEquals(zonage, result);
	}

	@Test
	public void findByUtilisateur() {
		// Init
		Utilisateur utilisateur = new Utilisateur();
		List<Zonage> zonages = new ArrayList<Zonage>();

		IZonageDAO zonageDAOMock = EasyMock.createMock(IZonageDAO.class);
		EasyMock.expect(
				zonageDAOMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonages);
		EasyMock.replay(zonageDAOMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(zonageDAOMock);
		List<Zonage> result = zonageService.findByUtilisateur(utilisateur);
		// Assertions
		EasyMock.verify(zonageDAOMock);
		Assert.assertEquals(zonages, result);
	}

	@Test
	public void findById() {
		// Init
		Zonage zonage = new Zonage();
		zonage.setId("id");

		IZonageDAO zonageDAOMock = EasyMock.createMock(IZonageDAO.class);
		EasyMock.expect(zonageDAOMock.findById(EasyMock.eq(zonage.getId())))
				.andReturn(zonage);
		EasyMock.replay(zonageDAOMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(zonageDAOMock);
		Zonage result = zonageService.findById(zonage.getId());
		// Assertions
		EasyMock.verify(zonageDAOMock);
		Assert.assertEquals(zonage, result);
	}

	@Test
	public void findByZoneNull() {
		// Init
		Utilisateur utilisateur = new Utilisateur();

		Zone zone = new ZoneClassique();
		zone.setUtilisateur(utilisateur);

		IZonageService zonageServiceMock = EasyMock
				.createMock(IZonageService.class);
		EasyMock.expect(
				zonageServiceMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(null);

		EasyMock.replay(zonageServiceMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(null);
		List<Zonage> result = zonageService.findByZone(zone, zonageServiceMock);
		// Assertions
		EasyMock.verify(zonageServiceMock);
		Assert.assertNull(result);
	}

	@Test
	public void findByZone() {
		// Init
		Utilisateur utilisateur = new Utilisateur();
		Zonage zonage1 = new Zonage();
		Zonage zonage2 = new Zonage();

		Zone zone = new ZoneClassique();
		zone.setUtilisateur(utilisateur);

		Set<Zone> zones = new HashSet<Zone>();
		zones.add(zone);
		zonage1.setZones(zones);

		zonage2.setZones(new HashSet<Zone>());

		List<Zonage> zonages = new ArrayList<Zonage>();
		zonages.add(zonage1);
		zonages.add(zonage2);

		IZonageService zonageServiceMock = EasyMock
				.createMock(IZonageService.class);
		EasyMock.expect(
				zonageServiceMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonages);

		EasyMock.replay(zonageServiceMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(null);
		List<Zonage> result = zonageService.findByZone(zone, zonageServiceMock);
		// Assertions
		EasyMock.verify(zonageServiceMock);
		Assert.assertTrue("La recherche devrait contenir le zonage1", result
				.contains(zonage1));
		Assert.assertEquals(
				"La recherche devrait contenir uniquement le zonage1", 1,
				result.size());
	}

	@Test
	public void compterZonages() {
		// Init
		Utilisateur utilisateur = new Utilisateur();
		IZonageDAO zonageDAOMock = EasyMock.createMock(IZonageDAO.class);
		EasyMock.expect(zonageDAOMock.compterZonages(EasyMock.eq(utilisateur)))
				.andReturn(new Long(5));
		EasyMock.replay(zonageDAOMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(zonageDAOMock);
		Long result = zonageService.compterZonages(utilisateur);
		// Assertions
		EasyMock.verify(zonageDAOMock);
		Assert.assertEquals(new Long(5), result);
	}

	@Test
	public void findValidesByUtilisateur() {
		// Init
		Utilisateur utilisateur = new Utilisateur();

		Zonage zonage1 = new Zonage();
		zonage1.setUtilisateur(utilisateur);
		zonage1.setEtatValidation(EEtatValidation.VALIDE);
		Zonage zonage2 = new Zonage();
		zonage2.setUtilisateur(utilisateur);
		zonage2.setEtatValidation(EEtatValidation.NON_VALIDE);

		List<Zonage> zonages = new ArrayList<Zonage>();
		zonages.add(zonage1);
		zonages.add(zonage2);

		IZonageService zonageServiceMock = EasyMock
				.createMock(IZonageService.class);
		EasyMock.expect(
				zonageServiceMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonages);
		EasyMock.replay(zonageServiceMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(null);
		List<Zonage> result = zonageService.findValidesByUtilisateur(
				utilisateur, zonageServiceMock);
		// Assertions
		EasyMock.verify(zonageServiceMock);
		Assert.assertTrue("La liste des zonages valides doit contenir zonage1",
				result.contains(zonage1));
		Assert
				.assertEquals(
						"La liste des zonages valides doit contenir uniquement 1 élément",
						1, result.size());
	}

	@Test
	public void findValidesByUtilisateurNull() {
		// Init
		Utilisateur utilisateur = new Utilisateur();
		Zonage zonage1 = new Zonage();
		zonage1.setUtilisateur(utilisateur);
		zonage1.setEtatValidation(EEtatValidation.NON_VALIDE);
		Zonage zonage2 = new Zonage();
		zonage2.setUtilisateur(utilisateur);
		zonage2.setEtatValidation(EEtatValidation.NON_VALIDE);

		List<Zonage> zonages = new ArrayList<Zonage>();
		zonages.add(zonage1);
		zonages.add(zonage2);

		IZonageService zonageServiceMock = EasyMock
				.createMock(IZonageService.class);
		EasyMock.expect(
				zonageServiceMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonages);
		EasyMock.replay(zonageServiceMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(null);
		List<Zonage> result = zonageService.findValidesByUtilisateur(
				utilisateur, zonageServiceMock);
		// Assertions
		EasyMock.verify(zonageServiceMock);
		Assert.assertNull("La liste des zonages valides doit être nulle",
				result);
	}

	@Test
	public void findNonValidesByUtilisateur() {
		// Init
		Utilisateur utilisateur = new Utilisateur();

		Zonage zonage1 = new Zonage();
		zonage1.setUtilisateur(utilisateur);
		zonage1.setEtatValidation(EEtatValidation.VALIDE);
		Zonage zonage2 = new Zonage();
		zonage2.setUtilisateur(utilisateur);
		zonage2.setEtatValidation(EEtatValidation.NON_VALIDE);

		List<Zonage> zonages = new ArrayList<Zonage>();
		zonages.add(zonage1);
		zonages.add(zonage2);

		IZonageService zonageServiceMock = EasyMock
				.createMock(IZonageService.class);
		EasyMock.expect(
				zonageServiceMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonages);
		EasyMock.replay(zonageServiceMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(null);
		List<Zonage> result = zonageService.findNonValidesByUtilisateur(
				utilisateur, zonageServiceMock);
		// Assertions
		EasyMock.verify(zonageServiceMock);
		Assert.assertTrue(
				"La liste des zonages non valides doit contenir zonage2",
				result.contains(zonage2));
		Assert
				.assertEquals(
						"La liste des zonages non valides doit contenir uniquement 1 élément",
						1, result.size());
	}

	@Test
	public void findNonValidesByUtilisateurNull() {
		// Init
		Utilisateur utilisateur = new Utilisateur();
		Zonage zonage1 = new Zonage();
		zonage1.setUtilisateur(utilisateur);
		zonage1.setEtatValidation(EEtatValidation.VALIDE);
		Zonage zonage2 = new Zonage();
		zonage2.setUtilisateur(utilisateur);
		zonage2.setEtatValidation(EEtatValidation.VALIDE);

		List<Zonage> zonages = new ArrayList<Zonage>();
		zonages.add(zonage1);
		zonages.add(zonage2);

		IZonageService zonageServiceMock = EasyMock
				.createMock(IZonageService.class);
		EasyMock.expect(
				zonageServiceMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonages);
		EasyMock.replay(zonageServiceMock);
		// Test de la méthode
		IZonageService zonageService = new ZonageService(null);
		List<Zonage> result = zonageService.findNonValidesByUtilisateur(
				utilisateur, zonageServiceMock);
		// Assertions
		EasyMock.verify(zonageServiceMock);
		Assert.assertNull("La liste des zonages non valides doit être nulle",
				result);
	}
	

}
