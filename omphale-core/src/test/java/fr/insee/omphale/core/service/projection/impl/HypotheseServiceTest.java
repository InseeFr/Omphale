package fr.insee.omphale.core.service.projection.impl;

import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;

import fr.insee.omphale.core.service.projection.IHypotheseService;
import fr.insee.omphale.dao.projection.IHypotheseDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.TypeEntite;

public class HypotheseServiceTest {

	
	@Test
	public void compterhypotheses(){
		Utilisateur utilisateur = new Utilisateur();
		
		IHypotheseDAO hypotheseDAOMock = EasyMock.createMock(IHypotheseDAO.class);
		EasyMock.expect(hypotheseDAOMock.compterHypotheses(utilisateur)).andReturn(1L);
		EasyMock.replay(hypotheseDAOMock);
		
		IHypotheseService hypotheseService = new HypotheseService(hypotheseDAOMock);
		Long resultat = hypotheseService.compterHypotheses(utilisateur);
		
		Assert.assertEquals("1L doit être renvoyé", resultat, Long.valueOf(1));
	}

	@Test
	public void testerNomHypothese(){
		List<Hypothese> hypothesesUtilisateur = new ArrayList<Hypothese>();
		Hypothese hypothese = new Hypothese();
		hypothese.setNom("hypothese");
		hypothesesUtilisateur.add(hypothese);
		Utilisateur utilisateur = new Utilisateur();
		
		IHypotheseDAO hypotheseDAOMock = EasyMock.createMock(IHypotheseDAO.class);
		EasyMock.expect(hypotheseDAOMock.findByUtilisateur(utilisateur)).andReturn(hypothesesUtilisateur);
		EasyMock.replay(hypotheseDAOMock);
		
		IHypotheseService hypotheseService = new HypotheseService(hypotheseDAOMock);
		boolean resultat = hypotheseService.testerNomHypothese(utilisateur,"hypothese");
		
		Assert.assertFalse("1L doit être renvoyé", resultat);
	}
	
	@Test
	public void findByTypeEntite(){
		List<Hypothese> hypothesesUtilisateur = new ArrayList<Hypothese>();
		TypeEntite entite = new TypeEntite();
		
		IHypotheseDAO hypotheseDAOMock = EasyMock.createMock(IHypotheseDAO.class);
		EasyMock.expect(hypotheseDAOMock.findByTypeEntite(entite)).andReturn(hypothesesUtilisateur);
		EasyMock.replay(hypotheseDAOMock);
		
		IHypotheseService hypotheseService = new HypotheseService(hypotheseDAOMock);
		List<Hypothese> resultat = hypotheseService.findByTypeEntite(entite);
		
		Assert.assertEquals("liste hypothese doit être identique", resultat, hypothesesUtilisateur);
	}

	@Test
	public void findById(){
		Hypothese hypothese = new Hypothese();
		
		IHypotheseDAO hypotheseDAOMock = EasyMock.createMock(IHypotheseDAO.class);
		EasyMock.expect(hypotheseDAOMock.findById(1)).andReturn(hypothese);
		EasyMock.replay(hypotheseDAOMock);
		
		IHypotheseService hypotheseService = new HypotheseService(hypotheseDAOMock);
		Hypothese resultat = hypotheseService.findById(1);
		
		Assert.assertEquals("liste hypothese doit être identique", resultat, hypothese);
	}

	@Test
	public void insertOrUpdate(){
		Hypothese hypothese = new Hypothese();
		
		IHypotheseDAO hypotheseDAOMock = EasyMock.createMock(IHypotheseDAO.class);
		EasyMock.expect(hypotheseDAOMock.insertOrUpdate(hypothese)).andReturn(hypothese);
		EasyMock.replay(hypotheseDAOMock);
		
		IHypotheseService hypotheseService = new HypotheseService(hypotheseDAOMock);
		Hypothese resultat = hypotheseService.insertOrUpdate(hypothese);
		
		Assert.assertEquals("liste hypothese doit être identique", resultat, hypothese);
	}
	
	@Test
	public void findByUtilisateur(){
		List<Hypothese> hypothesesUtilisateur = new ArrayList<Hypothese>();
		Utilisateur utilisateur = new Utilisateur();
		
		IHypotheseDAO hypotheseDAOMock = EasyMock.createMock(IHypotheseDAO.class);
		EasyMock.expect(hypotheseDAOMock.findByUtilisateur(utilisateur)).andReturn(hypothesesUtilisateur);
		EasyMock.replay(hypotheseDAOMock);
		
		IHypotheseService hypotheseService = new HypotheseService(hypotheseDAOMock);
		List<Hypothese> resultat = hypotheseService.findByUtilisateur(utilisateur);
		
		Assert.assertEquals("liste hypothese doit être identique", resultat, hypothesesUtilisateur);
	}
	
	@Test
	public void findHypothesesStandards(){
		List<Hypothese> hypothesesUtilisateur = new ArrayList<Hypothese>();
		Hypothese hypothese = new Hypothese();
		hypothese.setNom("test");
		hypothese.setStandard(true);
		hypothesesUtilisateur.add(hypothese);
		
		IHypotheseDAO hypotheseDAOMock = EasyMock.createMock(IHypotheseDAO.class);
		EasyMock.expect(hypotheseDAOMock.findAll()).andReturn(hypothesesUtilisateur);
		EasyMock.replay(hypotheseDAOMock);
		
		IHypotheseService hypotheseService = new HypotheseService(hypotheseDAOMock);
		List<Hypothese> resultat = hypotheseService.findHypothesesStandards();
		
		Assert.assertEquals("liste hypothese doit être identique", resultat.get(0).getNom(), hypothesesUtilisateur.get(0).getNom());
	}

	@Test
	public void findHypothesesStandardsByUtilisateur(){
		List<Hypothese> hypothesesUtilisateur = new ArrayList<Hypothese>();
		Utilisateur utilisateur = new Utilisateur();
		Hypothese hypothese = new Hypothese();
		hypothese.setNom("test");
		hypothese.setStandard(true);
		hypothese.setUtilisateur(utilisateur);
		hypothesesUtilisateur.add(hypothese);

		
		
		IHypotheseDAO hypotheseDAOMock = EasyMock.createMock(IHypotheseDAO.class);
		EasyMock.expect(hypotheseDAOMock.findAll()).andReturn(hypothesesUtilisateur);
		EasyMock.replay(hypotheseDAOMock);
		
		IHypotheseService hypotheseService = new HypotheseService(hypotheseDAOMock);
		List<Hypothese> resultat = hypotheseService.findHypothesesStandardsByUtilisateur(utilisateur);
		
		Assert.assertEquals("nom hypothese dans la liste doit être identique", resultat.get(0).getNom(), hypothesesUtilisateur.get(0).getNom());
	}
	
	@Test
	public void findHypothesesNonStandardsByUtilisateur(){
		List<Hypothese> hypothesesUtilisateur = new ArrayList<Hypothese>();
		Utilisateur utilisateur = new Utilisateur();
		Hypothese hypothese = new Hypothese();
		hypothese.setNom("test");
		hypothese.setStandard(false);
		hypothese.setUtilisateur(utilisateur);
		hypothesesUtilisateur.add(hypothese);

		
		
		IHypotheseDAO hypotheseDAOMock = EasyMock.createMock(IHypotheseDAO.class);
		EasyMock.expect(hypotheseDAOMock.findAll()).andReturn(hypothesesUtilisateur);
		EasyMock.replay(hypotheseDAOMock);
		
		IHypotheseService hypotheseService = new HypotheseService(hypotheseDAOMock);
		List<Hypothese> resultat = hypotheseService.findHypothesesNonStandardsByUtilisateur(utilisateur);
		
		Assert.assertEquals("nom hypothese dans la liste doit être identique", resultat.get(0).getNom(), hypothesesUtilisateur.get(0).getNom());
	}
	

	@Test
	public void filtrerParStandardEtTypeEntite(){
		List<Hypothese> hypothesesUtilisateur = new ArrayList<Hypothese>();
		Hypothese hypothese = new Hypothese();
		hypothese.setNom("test");
		hypothesesUtilisateur.add(hypothese);

		
		
		IHypotheseService hypotheseServiceMock = EasyMock.createMock(IHypotheseService.class);
		EasyMock.expect(hypotheseServiceMock.filtrerHypothese(1,"-1",hypothese)).andReturn(true);
		EasyMock.replay(hypotheseServiceMock);
		
		IHypotheseService hypotheseService = new HypotheseService();
		List<Hypothese> resultat = hypotheseService.filtrerParStandardEtTypeEntite(1, "-1", hypothesesUtilisateur, hypotheseServiceMock);
		
		Assert.assertEquals("nom hypothese dans la liste doit être identique", resultat.get(0).getNom(), hypothesesUtilisateur.get(0).getNom());
	}
	
	
	@Test
	public void filtrerHypothese(){
		List<Hypothese> hypothesesUtilisateur = new ArrayList<Hypothese>();
		Utilisateur utilisateur = new Utilisateur();
		TypeEntite entite = new TypeEntite();
		entite.setCode("codeEntite");
		Hypothese hypothese = new Hypothese();
		hypothese.setNom("test");
		hypothese.setStandard(true);
		hypothese.setTypeEntite(entite);
		hypothese.setUtilisateur(utilisateur);
		hypothesesUtilisateur.add(hypothese);

		IHypotheseService hypotheseService = new HypotheseService();
		
		boolean resultat = hypotheseService.filtrerHypothese(-1, "-1", hypothese);
		Assert.assertTrue("True doit être retourné", resultat);
		
		boolean resultat2 = hypotheseService.filtrerHypothese(1, "-1", hypothese);
		Assert.assertTrue("True doit être retourné", resultat2);
		
		boolean resultat3 = hypotheseService.filtrerHypothese(0, "-1", hypothese);
		Assert.assertFalse("False doit être retourné", resultat3);
	}
	
	public List<Hypothese> findByTypeEntitePourENLGestionParams(
			TypeEntite entite, String idep) {
		List<Hypothese> all = new HypotheseService().findByTypeEntite(entite);
		List<Hypothese> hypotheses = new ArrayList<Hypothese>();
		for (Hypothese hypothese : all) {
			if (hypothese.isStandard()) {
				hypotheses.add(hypothese);
			} else if (hypothese.getUtilisateur().getIdep().equals(idep)) {
				hypotheses.add(hypothese);
			}
		}

		return hypotheses;
	}


		public List<Hypothese> findByTypeEntitePourENLGestionParams(
				TypeEntite entite, String idep, IHypotheseService hypotheseService) {
			List<Hypothese> all = hypotheseService.findByTypeEntite(entite);
			List<Hypothese> hypotheses = new ArrayList<Hypothese>();
			for (Hypothese hypothese : all) {
				if (hypothese.isStandard()) {
					hypotheses.add(hypothese);
				} else if (hypothese.getUtilisateur().getIdep().equals(idep)) {
					hypotheses.add(hypothese);
				}
			}

			return hypotheses;
		}

	
	@Test
	public void findByTypeEntitePourENLGestionParams(){
		List<Hypothese> hypothesesUtilisateur = new ArrayList<Hypothese>();
		Utilisateur utilisateur = new Utilisateur();
		utilisateur.setIdep("test");
		
		TypeEntite entite = new TypeEntite();
	
		Hypothese hypothese = new Hypothese();
		hypothese.setStandard(true);
		hypothese.setNom("hypothese");
		hypothesesUtilisateur.add(hypothese);	
		
		Hypothese hypothese2 = new Hypothese();
		hypothese2.setNom("hypothese");
		hypothese2.setStandard(false);
		hypothese2.setUtilisateur(utilisateur);
		hypothesesUtilisateur.add(hypothese2);
		
		
		IHypotheseService hypotheseServiceMock = EasyMock.createMock(IHypotheseService.class);
		EasyMock.expect(hypotheseServiceMock.findByTypeEntite(entite)).andReturn(hypothesesUtilisateur);
		EasyMock.replay(hypotheseServiceMock);
		
		IHypotheseService hypotheseService = new HypotheseService();
		List<Hypothese> resultat = hypotheseService.findByTypeEntitePourENLGestionParams(entite, "test", hypotheseServiceMock);
		
		Assert.assertEquals("nom hypothese dans la liste doit être identique", resultat.get(0).getNom(), hypothesesUtilisateur.get(0).getNom());
		Assert.assertEquals("nom hypothese dans la liste doit être identique", resultat.get(1).getNom(), hypothesesUtilisateur.get(1).getNom());
	}
	
	
}
