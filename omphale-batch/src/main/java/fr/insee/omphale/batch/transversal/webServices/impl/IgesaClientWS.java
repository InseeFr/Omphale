package fr.insee.omphale.batch.transversal.webServices.impl;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientProperties;

import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;

public abstract class  IgesaClientWS {

	private static WebTarget service;
	private static SPOCAnnuairePersonne inseePersonne = new SPOCAnnuairePersonne();
	private static String mail;
	

	public static String rechercherMailFonctionIdep(String idep){

		service = ClientBuilder.newClient()
				.property(ClientProperties.CONNECT_TIMEOUT, 5000)
				.property(ClientProperties.READ_TIMEOUT, 5000)
				.target(OmphaleBatchConfig.IGESA_SEND_SERVICE_URL );		
		
		WebTarget target = service.path("recherche/utilisateur/idep/"
				+ idep);
		
		inseePersonne.setMails(target.request(MediaType.APPLICATION_XML_TYPE)
				.get(SPOCAnnuairePersonnes.class).getPersonne().mails);
		
		mail = target.request(MediaType.APPLICATION_XML_TYPE)
				.get(SPOCAnnuairePersonnes.class).getPersonne().getMail();
		return mail;
	}

}
