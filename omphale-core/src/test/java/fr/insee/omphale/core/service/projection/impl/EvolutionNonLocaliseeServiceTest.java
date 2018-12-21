package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.insee.omphale.core.service.impl.Service;
import fr.insee.omphale.core.service.projection.IEvolutionNonLocaliseeService;
import fr.insee.omphale.dao.projection.IEvolutionNonLocaliseeDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.EvolDeScenario;
import fr.insee.omphale.domaine.projection.EvolDeScenarioId;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Hypothese;

public class EvolutionNonLocaliseeServiceTest {

	private static IEvolutionNonLocaliseeService evolutionNonLocaliseeService;
	
	

	@Test
	public void compterEvolutionsNonLocalisees(){
		Utilisateur utilisateur = new Utilisateur();
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDaoMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDaoMock.compterEvolutionsNonLocalisees(utilisateur)).andReturn(1L);
		EasyMock.replay(evolutionNonLocaliseeDaoMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDaoMock);
		Long resultat = enlService.compterEvolutionsNonLocalisees(utilisateur);
		Assert.assertEquals("1L doit être renvoyée", resultat, Long.valueOf(1));
	}
		
	
	@Test
	public void findAll(){
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDaoMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDaoMock.findAll(true, "composante")).andReturn(listeENL);
		EasyMock.replay(evolutionNonLocaliseeDaoMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDaoMock);
		List<EvolutionNonLocalisee> resultat = enlService.findAll(true, "composante");
		Assert.assertEquals("ListeENL doit être renvoyée", resultat, listeENL);
		
	}

	
	@Test
	public void findAllInteger(){
		EvolutionNonLocalisee enl = new EvolutionNonLocalisee();
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		listeENL.add(enl);
		List<Integer> listeIdentifiantENL = new ArrayList<Integer>(1);
		listeIdentifiantENL.add(1);
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDaoMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDaoMock.getById(1)).andReturn(enl);
		EasyMock.replay(evolutionNonLocaliseeDaoMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDaoMock);
		List<EvolutionNonLocalisee> resultat = enlService.findAll(listeIdentifiantENL);
		Assert.assertEquals("listeENL doit être renvoyée", resultat, listeENL);
	}

	
	@Test
	public void findAllSTringIdUserStandardComposante(){
		EvolutionNonLocalisee enl1 = new EvolutionNonLocalisee();
		EvolutionNonLocalisee enl2 = new EvolutionNonLocalisee();
		List<EvolutionNonLocalisee> listeENL1 = new ArrayList<EvolutionNonLocalisee>();
		listeENL1.add(enl1);
		List<EvolutionNonLocalisee> listeENL2 = new ArrayList<EvolutionNonLocalisee>();
		listeENL2.add(enl2);
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDaoMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDaoMock.findAll(true, "composante")).andReturn(listeENL1);
		EasyMock.expect(evolutionNonLocaliseeDaoMock.findAll("idUser", false, "composante")).andReturn(listeENL2);
		EasyMock.replay(evolutionNonLocaliseeDaoMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDaoMock);
		List<EvolutionNonLocalisee> resultat1 = enlService.findAll("idUser",true,"composante");

		Assert.assertEquals("listeENL1 doit être renvoyée", resultat1, listeENL1);
		
		List<EvolutionNonLocalisee> resultat2 = enlService.findAll("idUser",false,"composante");
		
		Assert.assertEquals("listeENL2 doit être renvoyée", resultat2, listeENL2);
	}

	
	@Test
	public void findAllIdUserStandardComposanteListeEvolutionNonLocalisee(){
		EvolutionNonLocalisee enl1 = new EvolutionNonLocalisee();
		EvolutionNonLocalisee enl2 = new EvolutionNonLocalisee();
		List<EvolutionNonLocalisee> listeENL1 = new ArrayList<EvolutionNonLocalisee>();
		listeENL1.add(enl1);
		List<EvolutionNonLocalisee> listeENL2 = new ArrayList<EvolutionNonLocalisee>();
		listeENL2.add(enl2);
		
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDaoMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDaoMock.findAll(true, "composante")).andStubReturn(listeENL1);
		EasyMock.expect(evolutionNonLocaliseeDaoMock.findAll("idUser", false, "composante")).andStubReturn(listeENL2);
		EasyMock.replay(evolutionNonLocaliseeDaoMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDaoMock);
		List<EvolutionNonLocalisee> resultat1 = enlService.findAll("idUser",true,"composante", null);

		Assert.assertEquals("listeENL1 doit être renvoyée", resultat1, listeENL1);
		
		List<EvolutionNonLocalisee> resultat2 = enlService.findAll("idUser",false,"composante", null);
		
		Assert.assertEquals("listeENL2 doit être renvoyée", resultat2, listeENL2);
		
		List<EvolutionNonLocalisee> resultat3 = enlService.findAll("idUser",false,"composante", listeENL2);
		
		Assert.assertEquals("0 doit être renvoyé", resultat3.size(), 0);
	}

	
	@Test
	public void findAllOrdreDefaut(){
		EvolutionNonLocalisee enl = new EvolutionNonLocalisee();
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		listeENL.add(enl);
		List<Integer> listeIdentifiantENL = new ArrayList<Integer>(1);
		listeIdentifiantENL.add(1);
		
		
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDaoMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDaoMock.findAllOrdreDefaut(Service.tableauToString(listeIdentifiantENL))).andReturn(listeENL);
		EasyMock.replay(evolutionNonLocaliseeDaoMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDaoMock);
		List<EvolutionNonLocalisee> resultat = enlService.findAllOrdreDefaut(listeIdentifiantENL);
		Assert.assertEquals("listeENL doit être renvoyée", resultat, listeENL);
		
		List<EvolutionNonLocalisee> resultat2 = enlService.findAllOrdreDefaut(null);
		Assert.assertEquals("liste vide doit être renvoyée", resultat2.size(), 0);
	}
	
	@Test
	public void findByHypothese(){
		Hypothese hypothese = new Hypothese();
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDaoMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDaoMock.findByHypothese(hypothese)).andReturn(listeENL);
		EasyMock.replay(evolutionNonLocaliseeDaoMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDaoMock);
		List<EvolutionNonLocalisee> resultat = enlService.findByHypothese(hypothese);
		Assert.assertEquals("listeENL doit être renvoyée", resultat, listeENL);
	}

	
	@Test
	public void findById(){
		EvolutionNonLocalisee enl = new EvolutionNonLocalisee();
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDAOMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDAOMock.findById(1)).andReturn(enl);
		EasyMock.replay(evolutionNonLocaliseeDAOMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDAOMock);
		EvolutionNonLocalisee resultat = enlService.findById(1);
		
		Assert.assertEquals("enl doit être renvoyé", resultat, enl);
	}
	
	@Test
	public void findByUtilisateur(){
		Utilisateur util = new Utilisateur();
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDAOMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDAOMock.findByUtilisateur(util)).andReturn(listeENL);
		EasyMock.replay(evolutionNonLocaliseeDAOMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDAOMock);
		List<EvolutionNonLocalisee> resultat = enlService.findByUtilisateur(util);
		
		Assert.assertEquals("ListeENL doit être renvoyée", resultat, listeENL);
	}
	
	@Test
	public void findStandard(){
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDAOMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDAOMock.findStandard()).andReturn(listeENL);
		EasyMock.replay(evolutionNonLocaliseeDAOMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDAOMock);
		List<EvolutionNonLocalisee> resultat = enlService.findStandard();
		
		Assert.assertEquals("ListeENL doit être renvoyée", resultat, listeENL);
	}
	
	@Test
	public void get(){
		EvolutionNonLocalisee enl = new EvolutionNonLocalisee();
		enl.setId(1);
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		listeENL.add(enl);
			
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService();
		EvolutionNonLocalisee resultat = enlService.get(listeENL, 1);
		
		Assert.assertEquals("enl doit être renvoyé", resultat, enl);
	}
	

	
	@Test
	public void getListe(){
		EvolDeScenario eds = new EvolDeScenario();
		eds.setRang(1);
		EvolDeScenarioId edsid = new EvolDeScenarioId();
		EvolutionNonLocalisee enl = new EvolutionNonLocalisee();
		edsid.setEvolutionNonLocalisee(enl);
		eds.setId(edsid);
		Set<EvolDeScenario> evolutionsDeScenario = new HashSet<EvolDeScenario>();
		evolutionsDeScenario.add(eds);
		
		List<EvolutionNonLocalisee> evolutionsNL = new ArrayList<EvolutionNonLocalisee>();
		evolutionsNL.add(enl);

		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService();
		List<EvolutionNonLocalisee> resultat = enlService.getListe(evolutionsDeScenario);
		
		Assert.assertEquals("evolutionsNL doit être renvoyée", resultat, evolutionsNL);
	}
	
	public Integer[] getTableauId(List<EvolutionNonLocalisee> liste) {

		if (liste != null && liste.size() > 0) {
			Integer[] tableau = new Integer[liste.size()];
			for (int i = 0; i < tableau.length; i++) {
				tableau[i] = new Integer(liste.get(i).getId());
			}
			return tableau;
		}
		return new Integer[0];
	}
	
	@Test
	public void getTableauId(){
		EvolutionNonLocalisee enl = new EvolutionNonLocalisee();
		List<EvolutionNonLocalisee> evolutionsNL = new ArrayList<EvolutionNonLocalisee>();
		evolutionsNL.add(enl);
		enl.setId(1);
		
		List<Integer> tableau = new ArrayList<Integer>();
		tableau.add(1);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService();
		List<Integer> resultat = enlService.getTableauId(evolutionsNL);
		int resultat1 = resultat.get(0);
		
		Assert.assertEquals("tableau d'Integer doit être renvoyé", resultat1, 1);
	}
	

	@Test
	public void insertOrUpdate(){
		EvolutionNonLocalisee enl = new EvolutionNonLocalisee();
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDAOMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDAOMock.insertOrUpdate(enl)).andReturn(enl);
		EasyMock.replay(evolutionNonLocaliseeDAOMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDAOMock);
		EvolutionNonLocalisee resultat = enlService.insertOrUpdate(enl);
		
		Assert.assertEquals("enl doit être renvoyé", resultat, enl);
	}
	
	
	@Test
	public void testerNomEvolutionNonLocalisee(){
		
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		EvolutionNonLocalisee enl = new EvolutionNonLocalisee();
		enl.setNom("toto");
		listeENL.add(enl);
		Utilisateur utilisateur = new Utilisateur();
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDAOMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDAOMock.findByUtilisateur(utilisateur)).andReturn(listeENL);
		EasyMock.replay(evolutionNonLocaliseeDAOMock);
		
		IEvolutionNonLocaliseeService enlService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDAOMock);
		Boolean resultat = enlService.testerNomEvolutionNonLocalisee(utilisateur, "toto");
		
		Assert.assertFalse("True doit être renvoyé", resultat);
		
	}
	
	@Test
	public void filtrerParStandardEtComposante(){
		List<EvolutionNonLocalisee> listeENLFalse = new ArrayList<EvolutionNonLocalisee>();
		List<EvolutionNonLocalisee> listeENLTrue = new ArrayList<EvolutionNonLocalisee>();
		EvolutionNonLocalisee enlFalse = new EvolutionNonLocalisee();
		enlFalse.setNom("enlFalse");
		enlFalse.setStandard(false);
		Composante composanteFalse = new Composante();
		composanteFalse.setCode("composanteFalse");
		enlFalse.setComposante(composanteFalse);
		listeENLFalse.add(enlFalse);
		EvolutionNonLocalisee enlTrue = new EvolutionNonLocalisee();
		enlTrue.setNom("enlTrue");
		enlTrue.setStandard(true);
		Composante composanteTrue = new Composante();
		composanteTrue.setCode("composanteTrue");
		enlTrue.setComposante(composanteTrue);
		listeENLTrue.add(enlTrue);
		
		IEvolutionNonLocaliseeService evolutionNonLocaliseeService = new EvolutionNonLocaliseeService();
		
		List<EvolutionNonLocalisee> resultat = evolutionNonLocaliseeService.filtrerParStandardEtComposante(-1, "composanteFalse", listeENLFalse);
		Assert.assertEquals("listeENLFalse doit être renvoyée", resultat.get(0).getNom(), listeENLFalse.get(0).getNom());
		
		List<EvolutionNonLocalisee> resultat2 = evolutionNonLocaliseeService.filtrerParStandardEtComposante(1, "composanteTrue", listeENLTrue);
		Assert.assertEquals("listeENLFalse doit être renvoyée", resultat2.get(0).getNom(), listeENLTrue.get(0).getNom());
		
		List<EvolutionNonLocalisee> resultat3 = evolutionNonLocaliseeService.filtrerParStandardEtComposante(0, "composanteFalse", listeENLFalse);
		Assert.assertEquals("listeENLFalse doit être renvoyée", resultat3.get(0).getNom(), listeENLFalse.get(0).getNom());
		
	}

	@Test
	public void findENLStandardByUtilisateur(){
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		EvolutionNonLocalisee enlTrue = new EvolutionNonLocalisee();
		Utilisateur utilisateur = new Utilisateur();
		enlTrue.setUtilisateur(utilisateur);
		enlTrue.setNom("enlTrue");
		enlTrue.setStandard(true);

		
		listeENL.add(enlTrue);
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDAOMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDAOMock.findAll()).andReturn(listeENL);
		EasyMock.replay(evolutionNonLocaliseeDAOMock);
		
		IEvolutionNonLocaliseeService evolutionNonLocaliseeService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDAOMock);
		
		List<EvolutionNonLocalisee> resultat = evolutionNonLocaliseeService.findENLStandardsByUtilisateur(utilisateur);
		Assert.assertEquals("nom des enl doit être identique", resultat.get(0).getNom(), listeENL.get(0).getNom());
		
	}
	
	
	@Test
	public void findENLNonStandardByUtilisateur(){
		List<EvolutionNonLocalisee> listeENL = new ArrayList<EvolutionNonLocalisee>();
		EvolutionNonLocalisee enlFalse = new EvolutionNonLocalisee();
		Utilisateur utilisateur = new Utilisateur();
		enlFalse.setUtilisateur(utilisateur);
		enlFalse.setNom("enlFalse");
		enlFalse.setStandard(false);

		
		listeENL.add(enlFalse);
		
		IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDAOMock = EasyMock.createMock(IEvolutionNonLocaliseeDAO.class);
		EasyMock.expect(evolutionNonLocaliseeDAOMock.findAll()).andReturn(listeENL);
		EasyMock.replay(evolutionNonLocaliseeDAOMock);
		
		IEvolutionNonLocaliseeService evolutionNonLocaliseeService = new EvolutionNonLocaliseeService(evolutionNonLocaliseeDAOMock);
		
		List<EvolutionNonLocalisee> resultat = evolutionNonLocaliseeService.findENLNonStandardsByUtilisateur(utilisateur);
		Assert.assertEquals("nom des enl doit être identique", resultat.get(0).getNom(), listeENL.get(0).getNom());
		
	}
	
	
	@BeforeClass
	public static void initGeneral() {
		evolutionNonLocaliseeService = new EvolutionNonLocaliseeService();
	}
	
	/**
	 * On teste que la méthode advance fonctionne comme attendu, c-à-d :
	 * <BR>
	 * 	s'agit de monter ou descendre des évolutions non localisées
	 * <BR>
	 * 	dans une liste déroulante d'évolution non localisée (ENL)
	 * <BR>
	 * 		- quand arg est à "avancer" l'ENL descend
	 * <BR>
	 * 		- quand ar est à "bottom" l'ENL monte
	 * 
	 */
	@Test
	public void advanceTest() {
		
		EvolutionNonLocalisee evolutionsNL0 = new EvolutionNonLocalisee();
		evolutionsNL0.setId(0);
		EvolutionNonLocalisee evolutionsNL1 = new EvolutionNonLocalisee();
		evolutionsNL1.setId(1);
		EvolutionNonLocalisee evolutionsNL2 = new EvolutionNonLocalisee();
		evolutionsNL2.setId(2);
		EvolutionNonLocalisee evolutionsNL3 = new EvolutionNonLocalisee();
		evolutionsNL3.setId(3);
		EvolutionNonLocalisee evolutionsNL4 = new EvolutionNonLocalisee();
		evolutionsNL4.setId(4);
		EvolutionNonLocalisee evolutionsNL5 = new EvolutionNonLocalisee();
		evolutionsNL5.setId(5);
		EvolutionNonLocalisee evolutionsNL6 = new EvolutionNonLocalisee();
		evolutionsNL6.setId(6);
		
		List<EvolutionNonLocalisee> liste = new ArrayList<EvolutionNonLocalisee>(7);
		liste.add(evolutionsNL0);
		liste.add(evolutionsNL1);
		liste.add(evolutionsNL2);
		liste.add(evolutionsNL3);
		liste.add(evolutionsNL4);
		liste.add(evolutionsNL5);
		liste.add(evolutionsNL6);
		
		List<Integer> tableauId ;
		{
			tableauId = new ArrayList<Integer>();
			tableauId.add(1);
		}// evolutionsNL1
		evolutionNonLocaliseeService.advance(liste, tableauId, "avancer");
		
		Assert.assertEquals(liste.get(0), evolutionsNL1);
		Assert.assertEquals(liste.get(1), evolutionsNL0);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL3);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL5);
		Assert.assertEquals(liste.get(6), evolutionsNL6);
		
		List<Integer> tableauId2 ;
		{
			tableauId2 = new ArrayList<Integer>();
			tableauId2.add(1);
		} // evolutionsNL1
		evolutionNonLocaliseeService.advance(liste, tableauId2, "avancer");
		
		Assert.assertEquals(liste.get(0), evolutionsNL1);
		Assert.assertEquals(liste.get(1), evolutionsNL0);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL3);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL5);
		Assert.assertEquals(liste.get(6), evolutionsNL6);
		
		List<Integer> tableauId3 ;
		{
			tableauId3 = new ArrayList<Integer>();
			tableauId3.add(1);
		} // evolutionsNL1
		evolutionNonLocaliseeService.advance(liste, tableauId3, "bottom");
		
		Assert.assertEquals(liste.get(0), evolutionsNL0);
		Assert.assertEquals(liste.get(1), evolutionsNL1);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL3);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL5);
		Assert.assertEquals(liste.get(6), evolutionsNL6);
		
		List<Integer> tableauId4 ;
		{
			tableauId4 = new ArrayList<Integer>();
			tableauId4.add(1);
			tableauId4.add(3);
		} // evolutionsNL1, evolutionsNL3
		evolutionNonLocaliseeService.advance(liste, tableauId4, "avancer");
		
		Assert.assertEquals(liste.get(0), evolutionsNL1);
		Assert.assertEquals(liste.get(1), evolutionsNL0);
		Assert.assertEquals(liste.get(2), evolutionsNL3);
		Assert.assertEquals(liste.get(3), evolutionsNL2);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL5);
		Assert.assertEquals(liste.get(6), evolutionsNL6);
		
		List<Integer> tableauId5 ;
		{
			tableauId5 = new ArrayList<Integer>();
			tableauId5.add(1);
			tableauId5.add(3);
		} // evolutionsNL1, evolutionsNL3
		evolutionNonLocaliseeService.advance(liste, tableauId5, "avancer");
		
		Assert.assertEquals(liste.get(0), evolutionsNL1);
		Assert.assertEquals(liste.get(1), evolutionsNL3);
		Assert.assertEquals(liste.get(2), evolutionsNL0);
		Assert.assertEquals(liste.get(3), evolutionsNL2);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL5);
		Assert.assertEquals(liste.get(6), evolutionsNL6);
		
		List<Integer> tableauId6 ;
		{
			tableauId6 = new ArrayList<Integer>();
			tableauId6.add(1);
			tableauId6.add(3);
		}  // evolutionsNL1, evolutionsNL3
		evolutionNonLocaliseeService.advance(liste, tableauId6, "avancer");
		
		Assert.assertEquals(liste.get(0), evolutionsNL1);
		Assert.assertEquals(liste.get(1), evolutionsNL3);
		Assert.assertEquals(liste.get(2), evolutionsNL0);
		Assert.assertEquals(liste.get(3), evolutionsNL2);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL5);
		Assert.assertEquals(liste.get(6), evolutionsNL6);
		
		List<Integer> tableauId7 ;
		{
			tableauId7 = new ArrayList<Integer>();
			tableauId7.add(3);
		} // evolutionsNL3
		evolutionNonLocaliseeService.advance(liste, tableauId7, "bottom");
		
		Assert.assertEquals(liste.get(0), evolutionsNL1);
		Assert.assertEquals(liste.get(1), evolutionsNL0);
		Assert.assertEquals(liste.get(2), evolutionsNL3);
		Assert.assertEquals(liste.get(3), evolutionsNL2);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL5);
		Assert.assertEquals(liste.get(6), evolutionsNL6);
		
		List<Integer> tableauId8 ;
		{
			tableauId8 = new ArrayList<Integer>();
			tableauId8.add(1);
			tableauId8.add(3);
		} // evolutionsNL1, evolutionsNL3
		evolutionNonLocaliseeService.advance(liste, tableauId8, "bottom");
		
		Assert.assertEquals(liste.get(0), evolutionsNL0);
		Assert.assertEquals(liste.get(1), evolutionsNL1);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL3);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL5);
		Assert.assertEquals(liste.get(6), evolutionsNL6);
		
		List<Integer> tableauId9 ;
		{
			tableauId9 = new ArrayList<Integer>();
			tableauId9.add(5);
		} // evolutionsNL5
		evolutionNonLocaliseeService.advance(liste, tableauId9, "bottom");
		
		Assert.assertEquals(liste.get(0), evolutionsNL0);
		Assert.assertEquals(liste.get(1), evolutionsNL1);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL3);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL6);
		Assert.assertEquals(liste.get(6), evolutionsNL5);
		
		List<Integer> tableauId10 ;
		{
			tableauId10 = new ArrayList<Integer>();
			tableauId10.add(5);
		}  // evolutionsNL5
		evolutionNonLocaliseeService.advance(liste, tableauId10, "bottom");
		
		Assert.assertEquals(liste.get(0), evolutionsNL0);
		Assert.assertEquals(liste.get(1), evolutionsNL1);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL3);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL6);
		Assert.assertEquals(liste.get(6), evolutionsNL5);
		
		List<Integer> tableauId11 ;
		{
			tableauId11 = new ArrayList<Integer>();
			tableauId11.add(5);
		}  // evolutionsNL5
		evolutionNonLocaliseeService.advance(liste, tableauId11, "avancer");
		
		Assert.assertEquals(liste.get(0), evolutionsNL0);
		Assert.assertEquals(liste.get(1), evolutionsNL1);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL3);
		Assert.assertEquals(liste.get(4), evolutionsNL4);
		Assert.assertEquals(liste.get(5), evolutionsNL5);
		Assert.assertEquals(liste.get(6), evolutionsNL6);
		
		List<Integer> tableauId12 ;
		{
			tableauId12 = new ArrayList<Integer>();
			tableauId12.add(3);
			tableauId12.add(5);
		} // evolutionsNL3, evolutionsNL5
		evolutionNonLocaliseeService.advance(liste, tableauId12, "bottom");
		
		Assert.assertEquals(liste.get(0), evolutionsNL0);
		Assert.assertEquals(liste.get(1), evolutionsNL1);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL4);
		Assert.assertEquals(liste.get(4), evolutionsNL3);
		Assert.assertEquals(liste.get(5), evolutionsNL6);
		Assert.assertEquals(liste.get(6), evolutionsNL5);
		
		List<Integer> tableauId13 ;
		{
			tableauId13 = new ArrayList<Integer>();
			tableauId13.add(3);
			tableauId13.add(5);
		} // evolutionsNL3, evolutionsNL5
		evolutionNonLocaliseeService.advance(liste, tableauId13, "bottom");
		
		Assert.assertEquals(liste.get(0), evolutionsNL0);
		Assert.assertEquals(liste.get(1), evolutionsNL1);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL4);
		Assert.assertEquals(liste.get(4), evolutionsNL6);
		Assert.assertEquals(liste.get(5), evolutionsNL3);
		Assert.assertEquals(liste.get(6), evolutionsNL5);
		
		List<Integer> tableauId14 ;
		{
			tableauId14 = new ArrayList<Integer>();
			tableauId14.add(3);
			tableauId14.add(5);
		} // evolutionsNL3, evolutionsNL5
		evolutionNonLocaliseeService.advance(liste, tableauId14, "bottom");
		
		Assert.assertEquals(liste.get(0), evolutionsNL0);
		Assert.assertEquals(liste.get(1), evolutionsNL1);
		Assert.assertEquals(liste.get(2), evolutionsNL2);
		Assert.assertEquals(liste.get(3), evolutionsNL4);
		Assert.assertEquals(liste.get(4), evolutionsNL6);
		Assert.assertEquals(liste.get(5), evolutionsNL3);
		Assert.assertEquals(liste.get(6), evolutionsNL5);
	}
	
}