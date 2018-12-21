package fr.insee.omphale.core.service.impl;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import fr.insee.omphale.core.service.IUtilisateurService;
import fr.insee.omphale.dao.IUtilisateurDAO;
import fr.insee.omphale.domaine.Utilisateur;

public class UtilisateurServiceTest {
	
	private List<Utilisateur> utilisateurs; 
	@SuppressWarnings("unused")
	private IUtilisateurService utilisateurService;
	private IUtilisateurDAO utilisateurDAO;
	
	@Before
	public void creationObjets(){
		Utilisateur user1 = new Utilisateur();
		user1.setIdep("user1");

		Utilisateur user2 = new Utilisateur();
		user2.setIdep("user2");
		utilisateurs = new ArrayList<Utilisateur>();
		utilisateurs.add(user1);
		utilisateurs.add(user2);
		
		utilisateurDAO = mock(IUtilisateurDAO.class);
		utilisateurService = new UtilisateurService(utilisateurDAO);
	}
}
