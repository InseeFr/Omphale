
package fr.insee.omphale.core.service.projection.impl;

import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.ITypeService;


public class TypeServiceTest {
	
	@Test
	public void getTreeMap(){
		TreeMap<Boolean, String> treeMap = new TreeMap<Boolean, String>();
		treeMap.put(new Boolean(false), "Personnel");
		treeMap.put(new Boolean(true),"Standard");
		
		ITypeService typeService = new TypeService();
		TreeMap<Boolean, String> resultat = typeService.getTreeMap();
		
		Assert.assertEquals("renvoie le treeMap", resultat, treeMap);
	}

}
