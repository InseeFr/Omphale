package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IEvolutionLocaliseeService;
import fr.insee.omphale.dao.projection.IEvolutionLocaliseeDAO;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.EvolutionLocalisee;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Projection;

public class EvolutionLocaliseeServiceTest {

	private Integer[] listeIdentifiantEL;
	private EvolutionLocalisee el;
	
	/**
	 * Teste que la liste renvoyée contient est vide
	 */
	@Test
	public void findAllRenvoieListeVide(){
		
		listeIdentifiantEL =null;

		el = new EvolutionLocalisee();
		el.setId(0);
		el.setRang(4);
		
		IEvolutionLocaliseeDAO elDAOMock = EasyMock.createMock(IEvolutionLocaliseeDAO.class);
		
		IEvolutionLocaliseeService elService = new EvolutionLocaliseeService(elDAOMock);
		Set<EvolutionLocalisee> resultat = elService.findAll(listeIdentifiantEL);
		
		
		Assert.assertTrue("La liste doit être vide", resultat.isEmpty());
	}
	
	/**
	 * Teste que la liste renvoyée contient un objet d'évolution localisée
	 */
	@Test
	public void findAllRenvoieListeRemplie(){
		
		listeIdentifiantEL = new Integer[1];
		
		listeIdentifiantEL[0] = 1;
		el = new EvolutionLocalisee();
		el.setId(0);
		el.setRang(4);
		
		IEvolutionLocaliseeDAO elDAOMock = EasyMock.createMock(IEvolutionLocaliseeDAO.class);
		EasyMock.expect(elDAOMock.getById(listeIdentifiantEL[0])).andReturn(el);
		EasyMock.replay(elDAOMock);
		
		IEvolutionLocaliseeService elService = new EvolutionLocaliseeService(elDAOMock);
		Set<EvolutionLocalisee> resultat = elService.findAll(listeIdentifiantEL);
		
		
		Assert.assertTrue("La liste renvoyée doit contenir l'obj d'évolution localisée", resultat.contains(el));
	}
	
	/**
	 * Teste d'advance avec argument "monter"
	 */
	@Test
	public void advanceMonter(){
		EvolutionLocalisee elProche = new EvolutionLocalisee();
		elProche.setId(1);
		elProche.setRang(1);
		EvolutionLocalisee el = new EvolutionLocalisee();
		el.setId(2);
		el.setRang(2);
		Set<EvolutionLocalisee> evolutionsLocalisees = new HashSet<EvolutionLocalisee>();
		evolutionsLocalisees.add(elProche);
		evolutionsLocalisees.add(el);
		List<Integer> evolutionsLocaliseesSelectionnees = new ArrayList<Integer>();
		evolutionsLocaliseesSelectionnees.add(2);
		
		IEvolutionLocaliseeDAO evolutionLocaliseeDaoMock = EasyMock.createMock(IEvolutionLocaliseeDAO.class);
		EasyMock.expect(evolutionLocaliseeDaoMock.findById(2)).andReturn(el);
		EasyMock.expect(evolutionLocaliseeDaoMock.insertOrUpdate(EasyMock.eq(el))).andStubReturn(el);
		EasyMock.expect(evolutionLocaliseeDaoMock.insertOrUpdate(EasyMock.eq(elProche))).andStubReturn(elProche);
		EasyMock.expect(evolutionLocaliseeDaoMock.nettoyage()).andStubReturn(true);
	
		EasyMock.replay(evolutionLocaliseeDaoMock);
		IEvolutionLocaliseeService evolutionLocaliseeService = new EvolutionLocaliseeService(evolutionLocaliseeDaoMock);
		evolutionLocaliseeService.advance(evolutionsLocalisees, evolutionsLocaliseesSelectionnees, "monter");

		Assert.assertEquals("Rang elListe2 doit être à 1", el.getRang(), 1);
		
	}
	
	/**
	 * Teste d'advance avec argument "descendre"
	 */
	@Test
	public void advanceDescencre(){
		EvolutionLocalisee elProche = new EvolutionLocalisee();
		elProche.setId(1);
		elProche.setRang(2);
		EvolutionLocalisee el = new EvolutionLocalisee();
		el.setId(2);
		el.setRang(1);
		Set<EvolutionLocalisee> evolutionsLocalisees = new HashSet<EvolutionLocalisee>();
		evolutionsLocalisees.add(elProche);
		evolutionsLocalisees.add(el);
		List<Integer> evolutionsLocaliseesSelectionnees = new ArrayList<Integer>();
		evolutionsLocaliseesSelectionnees.add(2);
		
		IEvolutionLocaliseeDAO evolutionLocaliseeDaoMock = EasyMock.createMock(IEvolutionLocaliseeDAO.class);
		EasyMock.expect(evolutionLocaliseeDaoMock.findById(2)).andReturn(el);
		EasyMock.expect(evolutionLocaliseeDaoMock.insertOrUpdate(EasyMock.eq(el))).andStubReturn(el);
		EasyMock.expect(evolutionLocaliseeDaoMock.insertOrUpdate(EasyMock.eq(elProche))).andStubReturn(elProche);
		EasyMock.expect(evolutionLocaliseeDaoMock.nettoyage()).andStubReturn(true);
	
		EasyMock.replay(evolutionLocaliseeDaoMock);
		IEvolutionLocaliseeService evolutionLocaliseeService = new EvolutionLocaliseeService(evolutionLocaliseeDaoMock);
		evolutionLocaliseeService.advance(evolutionsLocalisees, evolutionsLocaliseesSelectionnees, "descendre");


		Assert.assertEquals("Rang elListe2 doit être à 1", el.getRang(), 2);
	}
	
	/**
	 * teste que méthode renvoie bien le bon objet
	 */
	@Test
	public void findByIdDontIdELA1(){
		EvolutionLocalisee elARenvoyer = new EvolutionLocalisee();
		elARenvoyer.setId(1);
		
		IEvolutionLocaliseeDAO evolutionLocaliseeDaoMock = EasyMock.createMock(IEvolutionLocaliseeDAO.class);
		EasyMock.expect(evolutionLocaliseeDaoMock.findById(1)).andReturn(elARenvoyer);
		EasyMock.replay(evolutionLocaliseeDaoMock);
		
		IEvolutionLocaliseeService evolutionLocaliseeService = new EvolutionLocaliseeService(evolutionLocaliseeDaoMock);
		EvolutionLocalisee resultat = evolutionLocaliseeService.findById(1);
		
		Assert.assertEquals("Renvoie l'el dont l'id est à 1 ", resultat.getId(), 1);
		
		
	}

	/**
	 * teste que méthode renvoie bien le bon objet
	 */
	@Test
	public void findByRangDontIdELA1(){
		EvolutionLocalisee elARenvoyer = new EvolutionLocalisee();
		elARenvoyer.setRang(1);

		
		Projection projection = new Projection();
		Composante composante = new Composante();
		int rang = 1;
		
		IEvolutionLocaliseeDAO evolutionLocaliseeDaoMock = EasyMock.createMock(IEvolutionLocaliseeDAO.class);
		EasyMock.expect(evolutionLocaliseeDaoMock.findByRang(projection, composante, rang)).andReturn(elARenvoyer);
		EasyMock.replay(evolutionLocaliseeDaoMock);
		
		IEvolutionLocaliseeService evolutionLocaliseeService = new EvolutionLocaliseeService(evolutionLocaliseeDaoMock);
		EvolutionLocalisee resultat = evolutionLocaliseeService.findByRang(projection, composante, rang);
		
		Assert.assertEquals("Renvoie l'el dont Rang est à 1 ", resultat.getRang(), 1);
		
	}
	
	
	/**
	 * teste que méthode renvoie bien le bon objet
	 */
	@Test
	public void findByENL(){
		EvolutionNonLocalisee enl = new EvolutionNonLocalisee();
		List<EvolutionLocalisee> evolsloc = new ArrayList<EvolutionLocalisee>();
		
		EvolutionLocalisee el = new EvolutionLocalisee();
		el.setId(1);
		evolsloc.add(el);
		
		IEvolutionLocaliseeDAO evolutionLocaliseeDaoMock = EasyMock.createMock(IEvolutionLocaliseeDAO.class);
		EasyMock.expect(evolutionLocaliseeDaoMock.findByENL(enl)).andReturn(evolsloc);
		EasyMock.replay(evolutionLocaliseeDaoMock);
		
		IEvolutionLocaliseeService evolutionLocaliseeService = new EvolutionLocaliseeService(evolutionLocaliseeDaoMock);
		List<EvolutionLocalisee> resultat = evolutionLocaliseeService.findByENL(enl);
		
		Assert.assertEquals("Renvoie l'el dont l'id est à 1 ", resultat.get(0).getId(), 1);
	}

}
