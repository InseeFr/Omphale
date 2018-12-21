package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IComposanteService;
import fr.insee.omphale.dao.projection.IComposanteDAO;
import fr.insee.omphale.domaine.projection.Composante;

public class ComposanteServiceTest {
	
	private List<Composante> composantes;
	private List<Composante> composantesStandard;
	private List<Composante> composantesUtilisateur;
	private Composante composanteDECES;
	private Composante composanteFECON;
	private Composante composanteEMIGR;
	private IComposanteService composanteService;
	
	
	
	/**
	 * teste que la composante est présente dans la liste
	 * ici la composante fait partie de la liste
	 * @param liste
	 * @param composante
	 * @return boolean
	 */
	@Test
	public void containsATrue(){
		creationDesObjComposantes();
		composanteService = new ComposanteService();
		Boolean resultat = composanteService.contains(composantes, composanteDECES.getCode());
		Assert.assertTrue("composante pas présente dans la liste ", resultat);
	}
	
	/**
	 * teste que la composante est présente dans la liste
	 * ici la composante fait partie de la liste
	 * @param liste
	 * @param composante
	 * @return boolean
	 */
	@Test
	public void containsAFalse(){
		creationDesObjComposantes();
		composanteService = new ComposanteService();
		Boolean resultat = composanteService.contains(composantes, composanteEMIGR.getCode());
		Assert.assertFalse("composante présente dans la liste ", resultat);
	}
	
	/**
	 * teste la taille de la liste renvoyée est égale à la liste
	 * donnée au Mock
	 * @param liste
	 * @param composante
	 * @return boolean
	 */
	@Test
	public void findAll(){
		creationDesObjComposantes();
		IComposanteDAO composanteDAOMock = EasyMock.createMock(IComposanteDAO.class);
		EasyMock.expect(composanteDAOMock.findAll()).andReturn(composantes);
		EasyMock.replay(composanteDAOMock);
		
		IComposanteService composanteService = new ComposanteService(composanteDAOMock);
		List<Composante> resultat = composanteService.findAll();
		
		Assert.assertEquals("taille du tableau pas égale à 2 ", resultat.size(), composantes.size());
	}
	
	
	/**
	 * méthode doit renvoyer la bonne composante
	 * identique à celle du mock
	 * @param liste
	 * @param composante
	 * @return boolean
	 */
	@Test
	public void findById(){
		composanteDECES = new Composante();
		IComposanteDAO composanteDAOMock = EasyMock.createMock(IComposanteDAO.class);
		EasyMock.expect(composanteDAOMock.findById("4")).andReturn(composanteDECES);
		EasyMock.replay(composanteDAOMock);
 
		
		IComposanteService composanteService = new ComposanteService(composanteDAOMock);
		Composante resultat = composanteService.findById("4");
		
		Assert.assertEquals("renvoie mauvaise composante", resultat, composanteDECES);
	}
	
	/**
	 * teste que la liste de composante à renvoyer
	 * est bien composantesStandard quand standard à true
	 * ici la composante fait partie de la liste
	 * @param liste
	 * @param composante
	 * @return boolean
	 */
	@Test
	public void findAllReferenceesEvolutionNLRenvoieListeStandard(){
		creationDesObjComposantes();
		IComposanteDAO composanteDAOMock = EasyMock.createMock(IComposanteDAO.class);
		EasyMock.expect(composanteDAOMock.findAllReferenceesEvolutionNL(true)).andReturn(composantesStandard);
		EasyMock.replay(composanteDAOMock);
 
		
		IComposanteService composanteService = new ComposanteService(composanteDAOMock);
		List<Composante> resultat = composanteService.findAllReferenceesEvolutionNL("test", true);
		
		Assert.assertEquals("renvoie mauvaise liste composante ", resultat, composantesStandard);
	}

	/**
	 * teste que la liste de composante à renvoyer
	 * est bien composantesUtilisateur quand standard à false
	 * ici la composante fait partie de la liste
	 * @param liste
	 * @param composante
	 * @return boolean
	 */
	@Test
	public void findAllReferenceesEvolutionNLRenvoieListeUtilisateur(){
		creationDesObjComposantes();
		IComposanteDAO composanteDAOMock = EasyMock.createMock(IComposanteDAO.class);
		EasyMock.expect(composanteDAOMock.findAllReferenceesEvolutionNL("test", false)).andReturn(composantesUtilisateur);
		EasyMock.replay(composanteDAOMock);
 
		
		IComposanteService composanteService = new ComposanteService(composanteDAOMock);
		List<Composante> resultat = composanteService.findAllReferenceesEvolutionNL("test", false);
		
		Assert.assertEquals("renvoie mauvaise liste composante ", resultat, composantesUtilisateur);
	}
	
	private void creationDesObjComposantes(){
		composanteDECES = new Composante();
		composanteDECES.setCode("DECES");
		composanteDECES.setLibelle("Décès");
		composanteFECON = new Composante();
		composanteFECON.setCode("FECON");
		composanteFECON.setLibelle("Fécondité");	
		composanteEMIGR = new Composante();
		composanteEMIGR.setCode("EMIGR");
		composanteEMIGR.setLibelle("Emigration entre communes");
		composantes = new ArrayList<Composante>();
		composantes.add(composanteDECES);
		composantes.add(composanteFECON);
		composantesStandard = new ArrayList<Composante>();
		composantesStandard.containsAll(composantes);
		composantesStandard.add(composanteEMIGR);
		composantesUtilisateur = new ArrayList<Composante>();
		composantesUtilisateur.containsAll(composantes);
	}
}
