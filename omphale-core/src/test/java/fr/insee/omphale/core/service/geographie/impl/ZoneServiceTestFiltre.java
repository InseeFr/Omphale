package fr.insee.omphale.core.service.geographie.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.domaine.geographie.Departement;
import fr.insee.omphale.domaine.geographie.Region;
import fr.insee.omphale.domaine.geographie.TypeZoneStandard;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.geographie.ZoneClassique;

public class ZoneServiceTestFiltre {

	Region region1;
	Region region2;

	TypeZoneStandard type1;
	TypeZoneStandard type2;

	Zone zone1;
	Zone zone2;
	Zone zone3;

	List<Zone> zones;

	Departement dep1;
	Departement dep2;

	@Before
	public void initScenario() {
		region1 = new Region();
		region2 = new Region();

		type1 = new TypeZoneStandard();
		type2 = new TypeZoneStandard();

		zone1 = new ZoneClassique();
		zone1.setTypeZoneStandard(type1);
		zone2 = new ZoneClassique();
		zone2.setTypeZoneStandard(type2);
		zone3 = new ZoneClassique();
		zone3.setTypeZoneStandard(type2);

		dep1 = new Departement();
		dep1.setRegion(region1);
		dep2 = new Departement();
		dep2.setRegion(region2);

		Set<Departement> depZone1 = new HashSet<Departement>();
		depZone1.add(dep1);
		depZone1.add(dep2);
		zone1.setDepartementsImpactes(depZone1);
		Set<Departement> depZone2 = new HashSet<Departement>();
		depZone2.add(dep2);
		zone2.setDepartementsImpactes(depZone2);
		Set<Departement> depZone3 = new HashSet<Departement>();
		depZone3.add(dep1);
		zone3.setDepartementsImpactes(depZone3);

		zones = new ArrayList<Zone>();
		zones.add(zone1);
		zones.add(zone2);
		zones.add(zone3);
	}

	@Test
	public void filtrerParRegionEtTypePasDeFiltre() {
		// Test de la méthode
		IZoneService zoneService = new ZoneService(null);
		List<Zone> result = zoneService.filtrerParRegionEtType(null, null,
				zones);
		// Assertions
		Assert.assertEquals(zones, result);
	}

	@Test
	public void filtrerParRegion() {
		// Test de la méthode
		IZoneService zoneService = new ZoneService(null);
		List<Zone> result = zoneService.filtrerParRegionEtType(region1, null,
				zones);
		// Assertions
		Assert.assertTrue("le filtre doit ramener la zone1", result
				.contains(zone1));
		Assert.assertEquals(
				"le filtre doit ramener 2 éléments : zone1 et zone3", 2, result
						.size());
	}

	@Test
	public void filtrerParType() {
		// Test de la méthode
		IZoneService zoneService = new ZoneService(null);
		List<Zone> result = zoneService.filtrerParRegionEtType(null, type2,
				zones);
		// Assertions
		Assert.assertTrue("le filtre doit ramener la zone2", result
				.contains(zone2));
		Assert.assertEquals(
				"le filtre doit ramener 2 éléments : zone2 et zone3", 2, result
						.size());
	}

	@Test
	public void filtrerParRegionEtType() {
		// Test de la méthode
		IZoneService zoneService = new ZoneService(null);
		List<Zone> result = zoneService.filtrerParRegionEtType(region1, type2,
				zones);
		// Assertions
		Assert.assertTrue("le filtre doit ramener la zone3", result
				.contains(zone3));
		Assert.assertEquals("le filtre doit ramener 1 élément : zone3", 1,
				result.size());
	}
}
