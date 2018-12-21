package fr.insee.omphale.core.service.projection.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ENLParamAN_CIBLETest {

	private ENLParamAN_CIBLE enlParamAN_CIBLE;
	
	@Before
	public void init(){
		enlParamAN_CIBLE = new ENLParamAN_CIBLE();
	}
		
	@Test
	public void testControleCasValAffichageVide(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamAN_CIBLE.valoriseMessageChampFlag(new ENLFieldErrorParam(), null);
		
		// THEN
		assertEquals("error.evolution.format.valParam.vide", resultat.getMessage());
	}
	
}
