package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.ITypeEntiteService;
import fr.insee.omphale.dao.projection.ITypeEntiteDAO;
import fr.insee.omphale.domaine.projection.TypeEntite;

public class TypeEntiteTest {

	@Test
	public void findAll(){
		List<TypeEntite> typeEntites = new ArrayList<TypeEntite>();
		
		ITypeEntiteDAO typeEntiteDAOMock = EasyMock.createMock(ITypeEntiteDAO.class);
		EasyMock.expect(typeEntiteDAOMock.findAll()).andReturn(typeEntites);
		EasyMock.replay(typeEntiteDAOMock);
		
		ITypeEntiteService typeEntiteService = new TypeEntiteService(typeEntiteDAOMock);
		List<TypeEntite> resultat = typeEntiteService.findAll();
		
		Assert.assertEquals("renvoie la liste des typesEntites", resultat, typeEntites);
	}
	
	@Test
	public void findById(){
		TypeEntite typeEntite = new TypeEntite();
		
		ITypeEntiteDAO typeEntiteDAOMock = EasyMock.createMock(ITypeEntiteDAO.class);
		EasyMock.expect(typeEntiteDAOMock.findById("1")).andReturn(typeEntite);
		EasyMock.replay(typeEntiteDAOMock);
		
		ITypeEntiteService typeEntiteService = new TypeEntiteService(typeEntiteDAOMock);
		TypeEntite resultat = typeEntiteService.findById("1");
		
		Assert.assertEquals("renvoie la liste des typesEntites", resultat, typeEntite);
	}

	
}
