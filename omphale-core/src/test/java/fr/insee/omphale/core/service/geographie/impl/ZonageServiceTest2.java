package fr.insee.omphale.core.service.geographie.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.geographie.ICommuneDependanceService;
import fr.insee.omphale.core.service.geographie.IGroupeEtalonService;
import fr.insee.omphale.core.service.geographie.IZonageService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.core.service.impl.Service;
import fr.insee.omphale.dao.geographie.IZonageDAO;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;
import fr.insee.omphale.domaine.geographie.EEtatValidation;
import fr.insee.omphale.domaine.geographie.GroupeEtalon;
import fr.insee.omphale.domaine.geographie.GroupeEtalonId;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.geographie.ZoneClassique;

public class ZonageServiceTest2 {

	@SuppressWarnings("unchecked")
	@Test
	public void validerZonageZonesNonDisjointes() {
		

		// Init
		Zonage zonage = new Zonage();
		zonage.setId("id");

		List<GroupeEtalon> groupes = new ArrayList<GroupeEtalon>();
		GroupeEtalon g1 = new GroupeEtalon();
		GroupeEtalonId g1id = new GroupeEtalonId();
		g1id.setSignature("G_1");
		g1id.setZonage(zonage);
		g1.setId(g1id);
		GroupeEtalon g2 = new GroupeEtalon();
		GroupeEtalonId g2id = new GroupeEtalonId();
		g2id.setSignature("G_2");
		g2id.setZonage(zonage);
		g2.setId(g2id);

		groupes.add(g1);
		groupes.add(g2);

		Departement dept1 = new Departement();
		Departement dept2 = new Departement();
		Departement dept3 = new Departement();

		Set<Zone> zones = new HashSet<Zone>();
		Zone zone1 = new ZoneClassique();
		zone1.setId("z1");
		Zone zone2 = new ZoneClassique();
		zone2.setId("z2");
		zones.add(zone1);
		zones.add(zone2);

		zonage.setZones(zones);

		Set<Commune> communesZone1 = new HashSet<Commune>();
		Set<Commune> communesZone2 = new HashSet<Commune>();
		Commune com1 = new Commune("c1", "", dept1);
		Commune com2 = new Commune("c2", "", dept2);
		communesZone1.add(com1);
		communesZone2.add(com1);
		communesZone2.add(com2);

		zone1.setCommunes(communesZone1);
		zone2.setCommunes(communesZone2);

		Set<Commune> dependancesZone1 = new HashSet<Commune>();
		Commune com3 = new Commune("c3", "", dept3);
		dependancesZone1.add(com3);

		Map<String, String> communesNonDisjointes = new HashMap<String, String>();
		communesNonDisjointes.put("c1", "z1,z2");

		IGroupeEtalonService groupeServiceMock = EasyMock
				.createMock(IGroupeEtalonService.class);
		EasyMock.expect(groupeServiceMock.findByZonage(zonage)).andReturn(
				groupes);
		EasyMock.expect(
				groupeServiceMock.delete((GroupeEtalon) EasyMock.anyObject()))
				.andReturn(true).anyTimes();

		IZoneService zoneServiceMock = EasyMock.createMock(IZoneService.class);
		EasyMock.expect(
				zoneServiceMock.findCommunesDependantes(EasyMock.eq(zone1),
						(ICommuneDependanceService) EasyMock.anyObject()))
				.andReturn(dependancesZone1);
		EasyMock.expect(
				zoneServiceMock.findCommunesDependantes(EasyMock.eq(zone2),
						(ICommuneDependanceService) EasyMock.anyObject()))
				.andReturn(null);

		IZonageService zonageServiceMock = EasyMock
				.createMock(IZonageService.class);
		EasyMock.expect(
				zonageServiceMock.verifierZonesDisjointes(zonage,
						zoneServiceMock)).andReturn(communesNonDisjointes);

		IZonageDAO zonageDAOMock = EasyMock.createMock(IZonageDAO.class);
		EasyMock.expect(zonageDAOMock.insertOrUpdate(zonage)).andReturn(zonage);

		EasyMock.replay(groupeServiceMock, zoneServiceMock, zonageServiceMock,
				zonageDAOMock);
		// Test de la méthode
		
		IZonageService zonageService = new ZonageService(zonageDAOMock);

		
		Map<String, Object> result = zonageService.validerZonage(zonage,
				zoneServiceMock, groupeServiceMock, null, null,
				zonageServiceMock, new Service());
		// Assertions
		Assert.assertEquals("Le zonage n'est pas valide", zonage
				.getEtatValidation(), EEtatValidation.NON_VALIDE);
		Assert.assertTrue(
				"Les zones ne sont pas disjointe à cause de la commune c1",
				((Map<String, String>) result.get("intersectionZones"))
						.containsKey("c1"));
		Assert.assertEquals("La commune c1 appartient aux zones z1 et z2",
				"z1,z2",
				((Map<String, String>) result.get("intersectionZones"))
						.get("c1"));
	}

}
