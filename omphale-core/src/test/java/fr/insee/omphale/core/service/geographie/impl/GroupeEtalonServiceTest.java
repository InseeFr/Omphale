/**
 * 
 */
package fr.insee.omphale.core.service.geographie.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.geographie.ICommuneService;
import fr.insee.omphale.core.service.geographie.IGroupeEtalonService;
import fr.insee.omphale.core.service.geographie.ITypeZoneStandardService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.dao.geographie.IGroupeEtalonDAO;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;
import fr.insee.omphale.domaine.geographie.GroupeEtalon;
import fr.insee.omphale.domaine.geographie.GroupeEtalonId;
import fr.insee.omphale.domaine.geographie.TypeZoneStandard;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.geographie.ZoneClassique;


public class GroupeEtalonServiceTest {

	@SuppressWarnings("unused")
	private Capture<Zone> zoneCapturee;

	@Test
	public void findCommunesResiduellesTest1() {
		GroupeEtalonService groupeEtalonService = new GroupeEtalonService();

		// création des objets métiers
		Zone zone = new ZoneClassique();
		Departement departement15 = new Departement();
		departement15.setId("15");
		Departement departement16 = new Departement();
		departement16.setId("16");
		Commune commune15001 = new Commune("15001", null, departement15);
		Commune commune15002 = new Commune("15002", null, departement15);
		Commune commune15003 = new Commune("15003", null, departement15);
		Commune commune16001 = new Commune("16001", null, departement16);
		Commune commune16002 = new Commune("16002", null, departement16);
		Commune commune16003 = new Commune("16003", null, departement16);

		Set<Commune> communes = new HashSet<Commune>();
		communes.add(commune15001);
		communes.add(commune15002);
		communes.add(commune15003);
		communes.add(commune16001);
		communes.add(commune16002);
		communes.add(commune16003);
		zone.setCommunes(communes);

		Set<Departement> departementsImpactes = new HashSet<Departement>();
		departementsImpactes.add(departement15);
		departementsImpactes.add(departement16);
		zone.setDepartementsImpactes(departementsImpactes);

		GroupeEtalon groupeEtalon = new GroupeEtalon();
		Set<Zone> zones = new HashSet<Zone>();
		zones.add(zone);
		groupeEtalon.setDepartements(departementsImpactes);
		groupeEtalon.setZones(zones);

		// Initialisation du mock
		ICommuneService communeServiceMock = EasyMock
				.createMock(ICommuneService.class);

		List<Commune> allCommunesDep15 = new ArrayList<Commune>();
		allCommunesDep15.add(commune15001);
		allCommunesDep15.add(commune15002);
		allCommunesDep15.add(commune15003);
		EasyMock.expect(communeServiceMock.findAllByDepartement(departement15))
				.andReturn(allCommunesDep15).anyTimes();

		List<Commune> allCommunesDep16 = new ArrayList<Commune>();
		allCommunesDep16.add(commune16001);
		allCommunesDep16.add(commune16002);
		allCommunesDep16.add(commune16003);
		EasyMock.expect(communeServiceMock.findAllByDepartement(departement16))
				.andReturn(allCommunesDep16).anyTimes();

		EasyMock.replay(communeServiceMock);

		// test proprement dit
		List<Commune> communesResiduelles = groupeEtalonService
				.findCommunesResiduelles(groupeEtalon, communeServiceMock);
		Assert
				.assertNull(
						"La méthode find commune résiduelle ne devrait pas afficher de communes résiduelles",
						communesResiduelles);

	}

	@Test
	public void findCommunesResiduellesTest2() {
		GroupeEtalonService groupeEtalonService = new GroupeEtalonService();

		// création des objets métiers
		Zone zone = new ZoneClassique();
		Departement departement15 = new Departement();
		departement15.setId("15");
		Departement departement16 = new Departement();
		departement16.setId("16");
		Commune commune15001 = new Commune("15001", null, departement15);
		Commune commune15002 = new Commune("15002", null, departement15);
		Commune commune15003 = new Commune("15003", null, departement15);
		Commune commune16001 = new Commune("16001", null, departement16);
		Commune commune16002 = new Commune("16002", null, departement16);
		Commune commune16003 = new Commune("16003", null, departement16);

		Set<Commune> communes = new HashSet<Commune>();

		communes.add(commune15002);
		communes.add(commune15003);
		communes.add(commune16001);
		communes.add(commune16002);

		zone.setCommunes(communes);

		Set<Departement> departementsImpactes = new HashSet<Departement>();
		departementsImpactes.add(departement15);
		departementsImpactes.add(departement16);
		zone.setDepartementsImpactes(departementsImpactes);

		GroupeEtalon groupeEtalon = new GroupeEtalon();
		Set<Zone> zones = new HashSet<Zone>();
		zones.add(zone);
		groupeEtalon.setDepartements(departementsImpactes);
		groupeEtalon.setZones(zones);

		ICommuneService communeServiceMock = EasyMock
				.createMock(ICommuneService.class);

		List<Commune> allCommunesDep15 = new ArrayList<Commune>();
		allCommunesDep15.add(commune15001);
		allCommunesDep15.add(commune15002);
		allCommunesDep15.add(commune15003);
		EasyMock.expect(communeServiceMock.findAllByDepartement(departement15))
				.andReturn(allCommunesDep15).anyTimes();

		List<Commune> allCommunesDep16 = new ArrayList<Commune>();
		allCommunesDep16.add(commune16001);
		allCommunesDep16.add(commune16002);
		allCommunesDep16.add(commune16003);
		EasyMock.expect(communeServiceMock.findAllByDepartement(departement16))
				.andReturn(allCommunesDep16).anyTimes();

		EasyMock.replay(communeServiceMock);

		List<Commune> communesResiduelles = groupeEtalonService
				.findCommunesResiduelles(groupeEtalon, communeServiceMock);
		Assert
				.assertTrue(
						"La méthode find commune résiduelle devrait trouver la commune 15001",
						communesResiduelles.contains(commune15001));
		Assert
				.assertTrue(
						"La méthode find commune résiduelle devrait trouver la commune 16003",
						communesResiduelles.contains(commune16003));

	}

	/**
	 * <table>
	 * <tr>
	 * <td>zone</td>
	 * <td>departement</td>
	 * </tr>
	 * <tr>
	 * <td>A</td>
	 * <td>1,2</td>
	 * </tr>
	 * <tr>
	 * <td>B</td>
	 * <td>3</td>
	 * </tr>
	 * <tr>
	 * <td>C</td>
	 * <td>2,4</td>
	 * </tr>
	 * <tr>
	 * <td>D</td>
	 * <td>4</td>
	 * </tr>
	 * </table>
	 */
	@Test
	public void CreerGroupesEtalonsTest() {
		// création zone à tester
		Zonage zonage = new Zonage();

		// création des départements à tester
		Departement dpt1 = new Departement();
		dpt1.setId("1");
		Departement dpt2 = new Departement();
		dpt2.setId("2");
		Departement dpt3 = new Departement();
		dpt3.setId("3");
		Departement dpt4 = new Departement();
		dpt4.setId("4");

		// Liste de zones pour zonage
		Set<Zone> zones = new HashSet<Zone>();

		// Création zone A avec départements impactés
		Set<Departement> dptImpactesZoneA = new HashSet<Departement>();
		dptImpactesZoneA.add(dpt1);
		dptImpactesZoneA.add(dpt2);
		Zone zoneA = new ZoneClassique();
		zoneA.setId("A");
		zoneA.setDepartementsImpactes(dptImpactesZoneA);
		zones.add(zoneA);

		// Création zone B avec départements impactés
		Set<Departement> dptImpactesZoneB = new HashSet<Departement>();
		dptImpactesZoneB.add(dpt3);
		Zone zoneB = new ZoneClassique();
		zoneB.setId("B");
		zoneB.setDepartementsImpactes(dptImpactesZoneB);
		zones.add(zoneB);

		// Création zone C avec départements impactés
		Set<Departement> dptImpactesZoneC = new HashSet<Departement>();
		dptImpactesZoneC.add(dpt4);
		dptImpactesZoneC.add(dpt2);
		Zone zoneC = new ZoneClassique();
		zoneC.setId("C");
		zoneC.setDepartementsImpactes(dptImpactesZoneC);
		zones.add(zoneC);

		// Création zone D avec départements impactés
		Set<Departement> dptImpactesZoneD = new HashSet<Departement>();
		dptImpactesZoneD.add(dpt4);
		Zone zoneD = new ZoneClassique();
		zoneD.setId("D");
		zoneD.setDepartementsImpactes(dptImpactesZoneD);
		zones.add(zoneD);

		// ajout de la liste de zones au zonage
		zonage.setZones(zones);

		// Test la méthode creerGroupesEtalon
		GroupeEtalonService groupeEtalonService = new GroupeEtalonService();
		List<GroupeEtalon> resultat = groupeEtalonService.creerGroupesEtalon(
				zonage, 0);

		for (GroupeEtalon g : resultat) {
			if (g.getZones().contains(zoneA)) {
				Assert
						.assertEquals(
								"On devrait avoir 3 zones dans le groupe étalon contenant la zone A",
								3, g.getZones().size());
				Assert.assertTrue(g.getZones().contains(zoneC));
				Assert.assertEquals(3, g.getDepartements().size());
				Assert.assertTrue(g.getDepartements().contains(dpt1));
				Assert.assertTrue(g.getDepartements().contains(dpt2));
				Assert.assertTrue(g.getDepartements().contains(dpt4));
			}
		}

		// Tester la méthode trouverTousFreres
		List<Zone> zonesList = new ArrayList<Zone>();
		zonesList.addAll(zones);

		Set<Zone> resultatTrouverTousFreres = groupeEtalonService
				.trouverTousFreres(zoneA, zonesList);
		for (Zone zone : resultatTrouverTousFreres) {
			if (zone.getDepartementsImpactes().contains(dpt1)) {
				Assert
						.assertEquals(
								"On devrait avoir 3 zones dans le groupe étalon contenant la zone A",
								3, resultatTrouverTousFreres.size());
				Assert.assertTrue("On doit avoir la Zone A ",
						resultatTrouverTousFreres.contains(zoneA));
				Assert.assertFalse("On ne doit pas avoir la Zone B ",
						resultatTrouverTousFreres.contains(zoneB));
				Assert.assertTrue("On doit avoir la Zone C ",
						resultatTrouverTousFreres.contains(zoneC));
				Assert.assertTrue("On doit avoir la Zone D ",
						resultatTrouverTousFreres.contains(zoneD));
			}
		}
	}

	@Test
	public void insertOrUpdate() {
		// Init
		GroupeEtalon groupe = new GroupeEtalon();

		IGroupeEtalonDAO groupeDAOMock = EasyMock
				.createMock(IGroupeEtalonDAO.class);
		EasyMock.expect(groupeDAOMock.insertOrUpdate(groupe)).andReturn(groupe)
				.times(1);
		EasyMock.replay(groupeDAOMock);
		// Test de la méthode
		IGroupeEtalonService groupeService = new GroupeEtalonService(
				groupeDAOMock);
		GroupeEtalon result = groupeService.insertOrUpdate(groupe);
		// Assertions
		EasyMock.verify(groupeDAOMock);
		Assert.assertEquals(groupe, result);
	}

	@Test
	public void findByZonage() {
		// Init
		GroupeEtalon groupe = new GroupeEtalon();
		List<GroupeEtalon> groupes = new ArrayList<GroupeEtalon>();
		groupes.add(groupe);

		Zonage zonage = new Zonage();

		IGroupeEtalonDAO groupeDAOMock = EasyMock
				.createMock(IGroupeEtalonDAO.class);
		EasyMock.expect(groupeDAOMock.findByZonage(zonage)).andReturn(groupes)
				.times(1);
		EasyMock.replay(groupeDAOMock);
		// Test de la méthode
		IGroupeEtalonService groupeService = new GroupeEtalonService(
				groupeDAOMock);
		List<GroupeEtalon> result = groupeService.findByZonage(zonage);
		// Assertions
		EasyMock.verify(groupeDAOMock);
		Assert.assertEquals(groupes, result);
	}

	@Test
	public void findById() {
		// Init
		GroupeEtalon groupe = new GroupeEtalon();
		GroupeEtalonId id = new GroupeEtalonId();
		groupe.setId(id);

		IGroupeEtalonDAO groupeDAOMock = EasyMock
				.createMock(IGroupeEtalonDAO.class);
		EasyMock.expect(groupeDAOMock.findById(id)).andReturn(groupe).times(1);
		EasyMock.replay(groupeDAOMock);
		// Test de la méthode
		IGroupeEtalonService groupeService = new GroupeEtalonService(
				groupeDAOMock);
		GroupeEtalon result = groupeService.findById(id);
		// Assertions
		EasyMock.verify(groupeDAOMock);
		Assert.assertEquals(groupe, result);
	}

	@Test
	public void addZoneResiduelle() {
		// Init
		GroupeEtalon groupe = new GroupeEtalon();
		GroupeEtalonId id = new GroupeEtalonId();
		groupe.setId(id);
		String nomZone = "nomZone";

		Departement departement = new Departement();
		Set<Departement> departements = new HashSet<Departement>();
		departements.add(departement);
		groupe.setDepartements(departements);

		Commune comResiduelle1 = new Commune();
		Commune comResiduelle2 = new Commune();
		Set<Commune> residuelles = new HashSet<Commune>();
		residuelles.add(comResiduelle1);
		residuelles.add(comResiduelle2);
		groupe.setCommunesResiduelles(residuelles);

		Zonage zonage = new Zonage();
		zonage.setNom("nomZonage");
		groupe.setZonage(zonage);

		Set<Zone> zones = new HashSet<Zone>();
		zonage.setZones(zones);
		int nbZonesZonage = zonage.getZones().size();
		groupe.setZones(zones);
		int nbZonesGroupe = groupe.getZones().size();

		TypeZoneStandard type = new TypeZoneStandard();

		ITypeZoneStandardService tzsServiceMock = EasyMock
				.createMock(ITypeZoneStandardService.class);
		EasyMock.expect(tzsServiceMock.findById(0)).andReturn(type).times(1);

		IZoneService zoneServiceMock = EasyMock.createMock(IZoneService.class);
		EasyMock.expect(
				zoneServiceMock.insertOrUpdate((Zone) EasyMock.anyObject()))
				.andReturn(true);

		IGroupeEtalonService groupeServiceMock = EasyMock
				.createMock(IGroupeEtalonService.class);
		EasyMock.expect(groupeServiceMock.insertOrUpdate(groupe)).andReturn(
				groupe);

		IGroupeEtalonDAO groupeDAOMock = EasyMock
				.createMock(IGroupeEtalonDAO.class);

		EasyMock.replay(tzsServiceMock, zoneServiceMock, groupeServiceMock);
		// Test de la méthode
		IGroupeEtalonService groupeService = new GroupeEtalonService(
				groupeDAOMock);
		groupeService.addZoneResiduelle(nomZone,zonage, groupe, tzsServiceMock,
				zoneServiceMock, groupeServiceMock);
		// Assertions
		Assert.assertEquals("On a rajouté une zone au zonage",
				nbZonesZonage + 1, zonage.getZones().size());
		Assert.assertEquals("On a rajouté une zone au groupe",
				nbZonesGroupe + 1, groupe.getZones().size());
		Assert.assertNull("Il n'y a plus de communes résiduelles", groupe
				.getCommunesResiduelles());
	}

}
