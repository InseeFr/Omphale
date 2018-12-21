package fr.insee.omphale.core.service.administration.utilisateurs.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientProperties;

import fr.insee.omphale.core.service.IUtilisateurService;
import fr.insee.omphale.core.service.administration.utilisateurs.IAdministrationUtilisateursService;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.ihm.util.WebOmphaleConfig;

public class AdministrationUtilisateursService implements IAdministrationUtilisateursService {
	
	
	private static Form credentials;
	private  AnnuairePersonne inseePersonne = new AnnuairePersonne();
	private String nomApplication = WebOmphaleConfig.getConfig().getNomApplicationIgesa();
	private String nomGroupeLDAP = WebOmphaleConfig.getConfig().getNomGroupeLDAPIgesa();
	private String urlIgesa = WebOmphaleConfig.getConfig().getUrlIgesa();
	private String compteApplicatif = WebOmphaleConfig.getConfig().getCompteApplicatifIgesa();
	private String motDePasseApplicatif = WebOmphaleConfig.getConfig().getMotDePasseApplicatifIgesa();
	
	public WebTarget service;
	
	public AnnuaireGroupe groupeApplicatif;
	public List<String> uidsGroupeApplicatif;

	public String afficheAdresseServeurLDAP(){
		return "" +service.getUri();
	}

	public AdministrationUtilisateursService() {
		service = ClientBuilder.newClient()
				.property(ClientProperties.CONNECT_TIMEOUT, 30000)
				.property(ClientProperties.READ_TIMEOUT, 30000)
				.target(urlIgesa);
	
			credentials = new Form();
			credentials
					.param("j_username",
							compteApplicatif
									);
			credentials.param("j_password",
					motDePasseApplicatif);
			
			groupeApplicatif = recupereGroupeApplicatif(nomGroupeLDAP, nomApplication);
			uidsGroupeApplicatif = recupererUidsGroupeApplicatif(new ArrayList<String>(), groupeApplicatif);
	}
	
	public AdministrationUtilisateursService(String nomGroupeLDAP, String nomApplication) {
		service = ClientBuilder.newClient()
				.property(ClientProperties.CONNECT_TIMEOUT, 30000)
				.property(ClientProperties.READ_TIMEOUT, 30000)
				.target(WebOmphaleConfig.getConfig().getUrlIgesa());
	
			credentials = new Form();
			credentials
					.param("j_username",
							compteApplicatif
									);
			credentials.param("j_password",
					motDePasseApplicatif);
			
			groupeApplicatif = recupereGroupeApplicatif(nomGroupeLDAP, nomApplication);
			uidsGroupeApplicatif = recupererUidsGroupeApplicatif(new ArrayList<String>(), groupeApplicatif);
	}
	




	@Override
	public AnnuaireGroupe recupereGroupeApplicatif(String nomGroupeLDAP,
			String nomApplication) {

		AnnuaireGroupe groupe = new AnnuaireGroupe();

		WebTarget target = service.path("recherche/application/"+nomApplication+"/groupe/"
				+ nomGroupeLDAP);

		groupe.setCn(
						(
								target.request(MediaType.APPLICATION_XML_TYPE)
										.get(AnnuaireGroupes.class)
											.getGroupe()
												.getCn()
						)
					);
		groupe.setPersonnes(target.request(MediaType.APPLICATION_XML_TYPE)
				.get(AnnuaireGroupes.class).getGroupe().getPersonnes());
		
		// met à jour la variable groupeApplicatif
		this.groupeApplicatif = groupe;
		
		return groupe;
	}
	
	public AnnuaireGroupe getGroupeApplicatif(){
		return groupeApplicatif;
	}
	
	public List<String> recupererUidsGroupeApplicatif(List<String> uidsGroupeApplicatif, AnnuaireGroupe groupeApplicatif){
		
		for (AnnuairePersonne personne : groupeApplicatif.getPersonnes()){
			uidsGroupeApplicatif.add(personne.getUid());
		}
		
		this.uidsGroupeApplicatif = uidsGroupeApplicatif;
		
		return uidsGroupeApplicatif;
	}
	
	public List<String> getUidsGroupeApplicatif(){
		return uidsGroupeApplicatif;
	}
	

	
	/**
	 * Ajout d'une liste d'idep autorisés à accéder à l'application
	 */

	public boolean ajoutUtilisateurAuGroupeLDAP(String IdepAAjouter) throws Exception 
	{
		
		boolean result = false;

		if(isIdepExiste(IdepAAjouter)){
			if(!uidsGroupeApplicatif.contains(IdepAAjouter)){
				WebTarget target = service
						.path("gestion/ajout/personne/application/")
						.path(nomApplication)
						.path("/groupe/")
						.path(nomGroupeLDAP)
						.path("utilisateur")
						.path(IdepAAjouter);
				@SuppressWarnings("unused")
				Response response = target.request().post(Entity.form(credentials));
				
				// mise à jour des objets groupeApplicatif et uidGroupeApplicatif
				recupereGroupeApplicatif(nomGroupeLDAP, nomApplication);
				recupererUidsGroupeApplicatif(new ArrayList<String>(), groupeApplicatif);

				result = true;
			}
		}
		return result;
	}
	
	/**
	 * Ajout d'une liste d'idep autorisés à accéder à l'application
	 */

	public boolean suppressionUtilisateurAuGroupeLDAP(String idepASupprimer) throws Exception 
	{
		boolean result = false;

		if(isIdepExiste(idepASupprimer)){
			if(uidsGroupeApplicatif.contains(idepASupprimer)){
				WebTarget target = service
						.path("gestion/suppression/personne/application/")
						.path(nomApplication)
						.path("/groupe/")
						.path(nomGroupeLDAP)
						.path("utilisateur")
						.path(idepASupprimer);
				@SuppressWarnings("unused")
				Response response = target.request().post(Entity.form(credentials));
				
				// mise à jour des objets groupeApplicatif et uidGroupeApplicatif
				recupereGroupeApplicatif(nomGroupeLDAP, nomApplication);
				recupererUidsGroupeApplicatif(new ArrayList<String>(), groupeApplicatif);

				result = true;
			}
		}
		return result;
	}
	
	public boolean isIdepExiste(String idep){
		boolean result = false;
		
		if(("").equals(idep) || idep ==null){
			return result;
		}
		
		WebTarget target = service
							.path("recherche/utilisateur/idep/")
							.path(idep.toUpperCase());
		
		if(target != null){
			AnnuairePersonne personneTrouvee = target.request(MediaType.APPLICATION_XML_TYPE).get(AnnuairePersonnes.class).getPersonne();
			if( personneTrouvee !=  null){
				inseePersonne.setUid(personneTrouvee.getUid());
			}
		}
		
		if(inseePersonne.getUid() != null){
			result = true;
		}
		return result;
	}
	
	
	
	public boolean isIdepExisteDansGroupeLDAP(String idepAVerifier){
		boolean result = false;
		
		List<AnnuairePersonne> annuairePersonnes  = recupereGroupeApplicatif(nomGroupeLDAP, nomApplication).getPersonnes();

		for (AnnuairePersonne annuairePersonne : annuairePersonnes) {
			if(idepAVerifier.toUpperCase().equals(annuairePersonne.getUid())){
				result = true;
			}
		}

		return result;
	}

	

	public EIdepLdap verificationIdep(String idep, IUtilisateurService utilisateurService) throws Exception{
		EIdepLdap resultat = null;
		
		if(!isIdepExiste(idep)){
			resultat = EIdepLdap.getEnum("idepNonPresentDansLdap");
		}
		else if(isDejaEnBase(idep, utilisateurService)){
			resultat = EIdepLdap.getEnum("idepDejaPresentEnBase");
		}
		
		return resultat;
		

		
	}
	


	
	public Boolean isDejaEnBase(String idep, IUtilisateurService utilisateurService) {
		Boolean result = false;

		List<Utilisateur> utilisateurs = utilisateurService.findAll();
		for (Utilisateur u : utilisateurs) {
			if (idep.equalsIgnoreCase(u.getIdep())) {
				result = true;
			}
		}
		return result;
	}
	

	public String rechercherParWebServiceIgesa(String idep){

		service = ClientBuilder.newClient()
				.property(ClientProperties.CONNECT_TIMEOUT, 5000)
				.property(ClientProperties.READ_TIMEOUT, 5000)
				.target(WebOmphaleConfig.getConfig().getUrlIgesa() );		
		
		WebTarget target = service.path("recherche/utilisateur/idep/"
				+ idep);
		
		if(target != null){
			AnnuairePersonne personneTrouvee = target.request(MediaType.APPLICATION_XML_TYPE).get(Personnes.class).getPersonne();
			if( personneTrouvee !=  null){
				inseePersonne.setUid(personneTrouvee.getUid());
			}
		}
		return inseePersonne.getUid();

	}
	

}
