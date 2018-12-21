package fr.insee.omphale.core.service.projection.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ENLParamGAIN_ICFTest {

	private ENLParamGAIN_ICF enlParamGAIN_ICF;
	
	@Before
	public void init(){
		enlParamGAIN_ICF = new ENLParamGAIN_ICF();
	}
	
	@Test
	public void testvaloriseMessageChampFlagCasValAffichageChiffre(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamGAIN_ICF.valoriseMessageChampFlag(new ENLFieldErrorParam(), "\\-2.2");
		
		// THEN
		assertEquals("error.evolution.format.valParam.caract.gain_icf", resultat.getMessage());
	}
	
	@Test
	public void testControleCasValAffichageVide(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamGAIN_ICF.valoriseMessageChampFlag(new ENLFieldErrorParam(), null);
		
		// THEN
		assertEquals("error.evolution.format.valParam.vide", resultat.getMessage());
	}
	

	@Test
	public void testControleCasValAffichageEntreMinEtMax(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamGAIN_ICF.valoriseMessageChampFlag(new ENLFieldErrorParam(), "20");
		
		// THEN
		assertEquals("error.evolution.format.valParam.caract.gain_icf", resultat.getMessage());
	}
	
}
