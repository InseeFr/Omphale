package fr.insee.omphale.core.service.geographie.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.geographie.IRegionService;
import fr.insee.omphale.dao.geographie.IRegionDAO;
import fr.insee.omphale.domaine.geographie.Region;

public class RegionServiceTest {
	@Test
	public void findById() {
		// Init
		Region region = new Region();
		IRegionDAO regionDAOMock = EasyMock.createMock(IRegionDAO.class);
		EasyMock.expect(regionDAOMock.findById("1")).andReturn(region);
		EasyMock.replay(regionDAOMock);
		// Test de la méthode
		IRegionService regionService = new RegionService(regionDAOMock);
		Region result = regionService.findById("1");
		// Assertions
		Assert.assertEquals(region, result);
	}

	@Test
	public void findAll() {
		// Init
		List<Region> regions = new ArrayList<Region>();
		IRegionDAO regionDAOMock = EasyMock.createMock(IRegionDAO.class);
		EasyMock.expect(regionDAOMock.findAll()).andReturn(regions);
		EasyMock.replay(regionDAOMock);
		// Test de la méthode
		IRegionService regionService = new RegionService(regionDAOMock);
		List<Region> result = regionService.findAll();
		// Assertions
		Assert.assertEquals(regions, result);
	}
}
