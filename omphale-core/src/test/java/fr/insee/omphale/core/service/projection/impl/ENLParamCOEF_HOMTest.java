package fr.insee.omphale.core.service.projection.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ENLParamCOEF_HOMTest {

	private ENLParamCOEF_HOM enlParamCOEF_HOM;
	
	@Before
	public void init(){
		enlParamCOEF_HOM = new ENLParamCOEF_HOM();
	}
	
	@Test
	public void testvaloriseMessageChampFlagCasValAffichageChiffre(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamCOEF_HOM.valoriseMessageChampFlag(new ENLFieldErrorParam(), "\\-2.2");
		
		// THEN
		assertEquals("error.evolution.format.valParam.caract.coef_hom", resultat.getMessage());
	}
	
	@Test
	public void testControleCasValAffichageVide(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamCOEF_HOM.valoriseMessageChampFlag(new ENLFieldErrorParam(), null);
		
		// THEN
		assertEquals("error.evolution.format.valParam.vide", resultat.getMessage());
	}
	

	@Test
	public void testControleCasValAffichageEntreMinEtMax(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamCOEF_HOM.valoriseMessageChampFlag(new ENLFieldErrorParam(), "20");
		
		// THEN
		assertEquals("error.evolution.format.valParam.caract.coef_hom", resultat.getMessage());
	}
	
}
