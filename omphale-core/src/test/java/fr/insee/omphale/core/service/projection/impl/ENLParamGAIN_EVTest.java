package fr.insee.omphale.core.service.projection.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ENLParamGAIN_EVTest {

	private ENLParamGAIN_EV enlParamGAIN_EV = new ENLParamGAIN_EV();
	
	@Before
	public void init(){
		enlParamGAIN_EV = new ENLParamGAIN_EV();
	}
	
	@Test
	public void testvaloriseMessageChampFlagCasValAffichageChiffre(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamGAIN_EV.valoriseMessageChampFlag(new ENLFieldErrorParam(), "\\-12");
		
		// THEN
		assertEquals("error.evolution.format.valParam.caract.gain_ev", resultat.getMessage());
	}
	
	@Test
	public void testControleCasValAffichageVide(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamGAIN_EV.valoriseMessageChampFlag(new ENLFieldErrorParam(), null);
		
		// THEN
		assertEquals("error.evolution.format.valParam.vide", resultat.getMessage());
	}
	

	@Test
	public void testControleCasValAffichageEntreMinEtMax(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamGAIN_EV.valoriseMessageChampFlag(new ENLFieldErrorParam(), "0");
		
		// THEN
		assertEquals("error.evolution.format.valParam.caract.gain_ev", resultat.getMessage());
	}
	
}
