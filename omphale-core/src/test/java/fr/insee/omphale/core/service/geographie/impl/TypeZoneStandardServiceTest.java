package fr.insee.omphale.core.service.geographie.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.geographie.ITypeZoneStandardService;
import fr.insee.omphale.dao.geographie.ITypeZoneStandardDAO;
import fr.insee.omphale.domaine.geographie.TypeZoneStandard;

public class TypeZoneStandardServiceTest {
	@Test
	public void findById() {
		// Init
		TypeZoneStandard type = new TypeZoneStandard();
		ITypeZoneStandardDAO typeDAOMock = EasyMock
				.createMock(ITypeZoneStandardDAO.class);
		EasyMock.expect(typeDAOMock.findById(1)).andReturn(type);
		EasyMock.replay(typeDAOMock);
		// Test de la méthode
		ITypeZoneStandardService typeService = new TypeZoneStandardService(
				typeDAOMock);
		TypeZoneStandard result = typeService.findById(1);
		// Assertions
		Assert.assertEquals(type, result);
	}

	@Test
	public void findAll() {
		// Init
		List<TypeZoneStandard> types = new ArrayList<TypeZoneStandard>();
		ITypeZoneStandardDAO typeDAOMock = EasyMock
				.createMock(ITypeZoneStandardDAO.class);
		EasyMock.expect(typeDAOMock.findAll()).andReturn(types);
		EasyMock.replay(typeDAOMock);
		// Test de la méthode
		ITypeZoneStandardService typeService = new TypeZoneStandardService(
				typeDAOMock);
		List<TypeZoneStandard> result = typeService.findAll();
		// Assertions
		Assert.assertEquals(types, result);
	}
}
