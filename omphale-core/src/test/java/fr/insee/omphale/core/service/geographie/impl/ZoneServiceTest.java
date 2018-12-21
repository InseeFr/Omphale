package fr.insee.omphale.core.service.geographie.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.insee.omphale.core.service.geographie.ICommuneDependanceService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.dao.geographie.IZoneDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.CommuneDependance;
import fr.insee.omphale.domaine.geographie.Departement;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.geographie.ZoneClassique;

public class ZoneServiceTest {

	private ZoneService zoneService;

	private ICommuneDependanceService cdServiceMock;

	private Commune commune1;
	private Commune commune2;
	private Commune commune3;
	@SuppressWarnings("unused")
	private Commune commune4;
	private Commune commune5;

	private CommuneDependance cd1_2;
	private CommuneDependance cd2_5;

	private Set<Commune> communesDeZone;

	@Before
	public void initMock() {

	}

	@Test
	public void findCommunesDependantesTest() {
		commune1 = new Commune("1", null, null);
		commune2 = new Commune("2", null, null);
		commune3 = new Commune("3", null, null);
		commune4 = new Commune("4", null, null);
		commune5 = new Commune("5", null, null);

		cd1_2 = new CommuneDependance();
		Set<Commune> setCd1_2 = new HashSet<Commune>();
		setCd1_2.add(commune1);
		setCd1_2.add(commune2);
		cd1_2.setCommunes(setCd1_2);
		cd1_2.setDependance(1);

		cd2_5 = new CommuneDependance();
		Set<Commune> setCd2_5 = new HashSet<Commune>();
		setCd2_5.add(commune2);
		setCd2_5.add(commune5);
		cd2_5.setCommunes(setCd2_5);
		cd2_5.setDependance(2);

		communesDeZone = new HashSet<Commune>();
		communesDeZone.add(commune1);
		communesDeZone.add(commune3);

		cdServiceMock = EasyMock.createMock(ICommuneDependanceService.class);

		EasyMock.expect(cdServiceMock.findByCommunes(EasyMock
				.eq(new ArrayList<Commune>(communesDeZone)), EasyMock
				.eq(cdServiceMock)));

		EasyMock.expectLastCall().andAnswer(
				new IAnswer<List<CommuneDependance>>() {
					public List<CommuneDependance> answer() throws Throwable {
						List<CommuneDependance> retour = new ArrayList<CommuneDependance>();
						retour.add(cd1_2);
						retour.add(cd2_5);
						return retour;
					}

				});

		EasyMock.replay(cdServiceMock);

		zoneService = new ZoneService();

		Zone zone = new ZoneClassique();
		zone.setCommunes(communesDeZone);

		Set<Commune> resultat = zoneService.findCommunesDependantes(zone,
				cdServiceMock);

		Assert.assertEquals("On doit rajouter 2 communes via les dépendances",
				2, resultat.size());
		Assert.assertTrue("On doit rajouter la commune 2 via les dépendances",
				resultat.contains(commune2));
		Assert.assertTrue("On doit rajouter la commune 5 via les dépendances",
				resultat.contains(commune5));
	}

	@Test
	public void compterZones() {
		// Init
		Utilisateur utilisateur = new Utilisateur();

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);
		EasyMock.expect(zoneDAOMock.compterZones(EasyMock.eq(utilisateur)))
				.andReturn(new Long(5));
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		Long result = zoneService.compterZones(utilisateur);
		// Assertions
		Assert
				.assertEquals("L'utilisateur a créé 5 zones", new Long(5),
						result);
	}

	@Test
	public void testerNomZoneOk() {
		// Init
		Utilisateur utilisateur = new Utilisateur();

		List<Zone> zonesUtilisateur = new ArrayList<Zone>();
		zonesUtilisateur.add(new ZoneClassique(null, "nom1", "", utilisateur,
				null, null, null));
		zonesUtilisateur.add(new ZoneClassique(null, "nom2", "", utilisateur,
				null, null, null));

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);
		EasyMock
				.expect(zoneDAOMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonesUtilisateur);
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		Boolean result = zoneService.testerNomZone(utilisateur, "nom3");
		// Assertions
		Assert.assertTrue("Le nom de zone \"nom3\" est valide", result);
	}

	@Test
	public void testerNomZoneKo() {
		// Init
		Utilisateur utilisateur = new Utilisateur();

		List<Zone> zonesUtilisateur = new ArrayList<Zone>();
		zonesUtilisateur.add(new ZoneClassique(null, "nom1", "", utilisateur,
				null, null, null));
		zonesUtilisateur.add(new ZoneClassique(null, "nom2", "", utilisateur,
				null, null, null));

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);
		EasyMock
				.expect(zoneDAOMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonesUtilisateur);
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		Boolean result = zoneService.testerNomZone(utilisateur, "nom2");
		// Assertions
		Assert.assertFalse(
				"Le nom de zone \"nom2\" est déja utilisé, donc non valide",
				result);
	}

	@Test
	public void insertOrUpdate() {
		// Init
		Zone zone = new ZoneClassique();

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);
		EasyMock.expect(zoneDAOMock.insertOrUpdate(EasyMock.eq(zone)))
				.andReturn(zone);
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		Boolean result = zoneService.insertOrUpdate(zone);
		// Assertions
		Assert.assertTrue(result);
	}

	@Test
	public void findNbCommunesParDepartement() {
		// Init
		Zone zone = new ZoneClassique();

		Departement departement1 = new Departement();
		Departement departement2 = new Departement();

		Commune com1 = new Commune(null, null, departement1);
		Commune com2 = new Commune(null, null, departement2);
		Set<Commune> communes = new HashSet<Commune>();
		communes.add(com1);
		communes.add(com2);
		zone.setCommunes(communes);

		Set<Departement> departementsImpactes = new HashSet<Departement>();
		departementsImpactes.add(departement1);
		zone.setDepartementsImpactes(departementsImpactes);

		// Test de la méthode
		IZoneService zoneService = new ZoneService(null);
		int result = zoneService.findNbCommunesParDepartement(zone,
				departement1);
		// Assertions
		Assert.assertEquals("Il y a une commune de departement1 dans la zone",
				1, result);
	}

	@Test
	public void findNbCommunesParDepartementAucun() {
		// Init
		Zone zone = new ZoneClassique();

		Departement departement1 = new Departement();
		Departement departement2 = new Departement();

		Commune com1 = new Commune(null, null, departement1);
		Commune com2 = new Commune(null, null, departement2);
		Set<Commune> communes = new HashSet<Commune>();
		communes.add(com1);
		communes.add(com2);
		zone.setCommunes(communes);

		Set<Departement> departementsImpactes = new HashSet<Departement>();
		departementsImpactes.add(departement1);
		zone.setDepartementsImpactes(departementsImpactes);

		// Test de la méthode
		IZoneService zoneService = new ZoneService(null);
		int result = zoneService.findNbCommunesParDepartement(zone,
				departement2);
		// Assertions
		Assert.assertEquals(
				"Il n'y a pas de commune de departement2 dans la zone", 0,
				result);
	}

	@Test
	public void findById() {
		// Init
		Zone zone = new ZoneClassique("id", null, null, null, null, null, null);

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);
		EasyMock.expect(zoneDAOMock.findById(EasyMock.eq(zone.getId())))
				.andReturn(zone);
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		Zone result = zoneService.findById(zone.getId());
		// Assertions
		Assert.assertEquals(zone, result);
	}

	@Test
	public void findZonesStandard() {
		// Init
		List<Zone> zones = new ArrayList<Zone>();

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);
		EasyMock.expect(zoneDAOMock.findZonesStandard()).andReturn(zones);
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		List<Zone> result = zoneService.findZonesStandard();
		// Assertions
		Assert.assertEquals(zones, result);
	}

	@Test
	public void findByUtilisateur() {
		// Init
		Utilisateur utilisateur = new Utilisateur();
		List<Zone> zonesUtilisateur = new ArrayList<Zone>();

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);
		EasyMock
				.expect(zoneDAOMock.findByUtilisateur(EasyMock.eq(utilisateur)))
				.andReturn(zonesUtilisateur);
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		List<Zone> result = zoneService.findByUtilisateur(utilisateur);
		// Assertions
		Assert.assertEquals(zonesUtilisateur, result);
	}

	@Test
	public void findZonesDisponibles() {
		// Init
		Utilisateur utilisateur = new Utilisateur();
		List<Zone> zonesUtilisateur = new ArrayList<Zone>();
		List<Zone> zonesStandard = new ArrayList<Zone>();
		List<Zone> zonesUtilisateurEtStandard = new ArrayList<Zone>();
		//zonesUtilisateurEtStandard

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);

		EasyMock.expect(zoneDAOMock.findByUtilisateurEtZonesStandard(EasyMock.eq(utilisateur))).andReturn(
				zonesUtilisateurEtStandard);
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		List<Zone> result = zoneService.findZonesDisponibles(utilisateur);
		// Assertions
		Assert.assertEquals(zonesUtilisateur.size() + zonesStandard.size(),
				result.size());
	}

	@Test
	public void findZonesDestandardisableByUtilisateur() {
		// Init
		Utilisateur utilisateur = new Utilisateur();
		Utilisateur autre = new Utilisateur();

		List<Zone> zonesStandard = new ArrayList<Zone>();

		zonesStandard.add(new ZoneClassique(null, null, null, utilisateur,
				null, null, null));
		zonesStandard.add(new ZoneClassique(null, null, null, utilisateur,
				null, null, null));
		zonesStandard.add(new ZoneClassique(null, null, null, autre, null,
				null, null));

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);
		EasyMock.expect(zoneDAOMock.findZonesDestandardisable()).andReturn(
				zonesStandard);
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		List<Zone> result = zoneService
				.findZonesDestandardisableByUtilisateur(utilisateur);
		// Assertions
		Assert.assertEquals(2, result.size());
	}

	@Test
	public void findZonesStandardisablesByUtilisateur() {
		// Init
		Utilisateur utilisateur = new Utilisateur();
		Utilisateur autre = new Utilisateur();

		List<Zone> zonesNonStandard = new ArrayList<Zone>();

		zonesNonStandard.add(new ZoneClassique(null, null, null, utilisateur,
				null, null, null));
		zonesNonStandard.add(new ZoneClassique(null, null, null, utilisateur,
				null, null, null));
		zonesNonStandard.add(new ZoneClassique(null, null, null, autre, null,
				null, null));

		IZoneDAO zoneDAOMock = EasyMock.createMock(IZoneDAO.class);
		EasyMock.expect(zoneDAOMock.findZonesStandardisable()).andReturn(
				zonesNonStandard);
		EasyMock.replay(zoneDAOMock);
		// Test de la méthode
		IZoneService zoneService = new ZoneService(zoneDAOMock);
		List<Zone> result = zoneService
				.findZonesStandardisablesByUtilisateur(utilisateur);
		// Assertions
		Assert.assertEquals(2, result.size());
	}

}
