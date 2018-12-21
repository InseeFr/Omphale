package fr.insee.omphale.core.service.projection.impl;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ENLParamCOEF_TRANTest {

	private ENLParamCOEF_TRAN enlParamCOEF_TRAN;
	
	@Before
	public void init(){
		enlParamCOEF_TRAN = new ENLParamCOEF_TRAN();
	}
	
	@Test
	public void testvaloriseMessageChampFlagCasValAffichageChiffre(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamCOEF_TRAN.valoriseMessageChampFlag(new ENLFieldErrorParam(), "\\-2.2");
		
		// THEN
		assertEquals("error.evolution.format.valParam.caract.coef_tran", resultat.getMessage());
	}
	
	@Test
	public void testControleCasValAffichageVide(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamCOEF_TRAN.valoriseMessageChampFlag(new ENLFieldErrorParam(), null);
		
		// THEN
		assertEquals("error.evolution.format.valParam.vide", resultat.getMessage());
	}
	

	@Test
	public void testControleCasValAffichageEntreMinEtMax(){
		//WHEN
		ENLFieldErrorParam resultat = enlParamCOEF_TRAN.valoriseMessageChampFlag(new ENLFieldErrorParam(), "20");
		
		// THEN
		assertEquals("error.evolution.format.valParam.caract.coef_tran", resultat.getMessage());
	}
	
}
