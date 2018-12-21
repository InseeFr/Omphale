package fr.insee.omphale.core.service.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.List;

import fr.insee.omphale.core.service.IUtilisateurService;
import fr.insee.omphale.core.service.administration.ISuppressionService;
import fr.insee.omphale.core.service.administration.utilisateurs.IAdministrationUtilisateursService;
import fr.insee.omphale.core.service.projection.IEvolutionLocaliseeService;
import fr.insee.omphale.dao.IUtilisateurDAO;
import fr.insee.omphale.domaine.Utilisateur;

public class UtilisateurService implements IUtilisateurService {

	private IUtilisateurDAO utilisateurDao;
	@SuppressWarnings("unused")
	private IEvolutionLocaliseeService evolutionLocaliseeService;
	private IAdministrationUtilisateursService administrationUtilisateurService;
	private ISuppressionService suppressionService;

	public UtilisateurService() {
		this.utilisateurDao = daoFactory.getUtilisateurDAO();
	}
	
	
	public UtilisateurService(
								IUtilisateurDAO utilisateurDAO, 
								IAdministrationUtilisateursService administrationUtilisateursService,
								ISuppressionService suppressionService) {
		this.utilisateurDao = utilisateurDAO;
		this.administrationUtilisateurService = administrationUtilisateursService;
		this.suppressionService = suppressionService;
	}

	public UtilisateurService(IUtilisateurDAO utilisateurDAO) {
		this.utilisateurDao = utilisateurDAO;
	}
	
	public UtilisateurService(IUtilisateurDAO utilisateurDAO, IEvolutionLocaliseeService evolutionLocaliseeService) {
		this.utilisateurDao = utilisateurDAO;
		this.evolutionLocaliseeService = evolutionLocaliseeService;
	}

	public Utilisateur findById(String id) {
		return utilisateurDao.findById(id);
	}

	public List<Utilisateur> findAll() {
		return utilisateurDao.findAll();
	}

	public void insertOrUpdate(Utilisateur utilisateur) {
		utilisateurDao.insertOrUpdate(utilisateur);
	}
	
	public Utilisateur findByIdep(String idep) {
		return utilisateurDao.findByIdep(idep);
	}
	
	public void supprimer(Utilisateur utilisateur) {
		utilisateurDao.delete(utilisateur);
	}
	
	public List<Utilisateur> findByRole(int role){
		return utilisateurDao.findByRole(role);
	}
	
	public ERetourSuppressionUtilisateur supprimerToutesReferences(Utilisateur utilisateur) throws Exception{
		
		String idep = utilisateur.getIdep();
		
		// supprimer l'utilisateur dans le groupe applicatif de l'annuaire LDAP
		administrationUtilisateurService.suppressionUtilisateurAuGroupeLDAP(idep);
		if(administrationUtilisateurService.isIdepExisteDansGroupeLDAP(idep)){
			return ERetourSuppressionUtilisateur.getEnum("utilisateurEncorePresentDansGroupeLDAP");
		}
		
		// supprimer les objets métiers de l'utilisateur en base de données
		suppressionService.rechercheObjetsMetierUtilisateur(utilisateur);
		suppressionService.supprimerLesObjetsUtilisateurs(utilisateur);
		if(suppressionService.rechercheObjetsMetierUtilisateur(utilisateur)!= 0){
			return ERetourSuppressionUtilisateur.getEnum("utilisateurObjetsMetiersEncorePresentsEnBDD");
		}
		
		// supprimer l'utilisateur en base de données
		supprimer(utilisateur);
		if(findByIdep(idep)!= null){
			return ERetourSuppressionUtilisateur.getEnum("utilisateurEncorePresentEnBDD");
		}
		
		return null;
	}
	
	


}
