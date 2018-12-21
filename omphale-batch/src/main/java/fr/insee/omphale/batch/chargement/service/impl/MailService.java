package fr.insee.omphale.batch.chargement.service.impl;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.transversal.bean.BeanRapport;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.batch.transversal.webServices.impl.SpocClientWS;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * Classe de service responsable de l'envoi d'un message à la boite fonctionnelle avec
 * compte rendu d'exécution.
 */
public class MailService {
	

    /**
     * Informations utiles au Web Service SPOC
     */
    private String SEND_SERVICE_URL = OmphaleBatchConfig.SPOC_SEND_SERVICE_URL;
	private String IDENT = OmphaleBatchConfig.SPOC_IDENT;
	private String PASSW = OmphaleBatchConfig.SPOC_PASSW;
	private String TRUST_STORE = OmphaleBatchConfig.SPOC_TRUST_STORE;
	private String TRUST_STORE_PASSWORD = OmphaleBatchConfig.SPOC_TRUST_STORE_PASSWORD;

    /**
     * Appels aux couches de service
     */
	private SpocClientWS spocClientWS ;
	
    /**
     * Méthode de service.
     * @param beanRapport paramètres pour construction du message de compte
     *        rendu.
     * @throws OmphaleMetierException en cas d'erreur.
     */
    public void executeService() throws OmphaleMetierException{
    	
    	BeanRapport informationsProjectionEnCoursTraitement = CacheDaoManager.beanRapport;
        BeanParametresResultat informationsProjectionsGraphiqueEtCSV = CacheDaoManager.beanRapport.results;
    	
    	// initialisation du client WebService pour SPOC et de ses différents attributs
        spocClientWS = new SpocClientWS();
        spocClientWS.setIDENT(IDENT);
        spocClientWS.setPASSW(PASSW);
        spocClientWS.setSEND_SERVICE_URL(SEND_SERVICE_URL);
        spocClientWS.setTRUST_STORE(TRUST_STORE);
        spocClientWS.setTRUST_STORE_PASSWORD(TRUST_STORE_PASSWORD);
        
        try{
	        // recuperation du message du mail
	    	String message = constructionDuMessageDuMail(informationsProjectionEnCoursTraitement, informationsProjectionsGraphiqueEtCSV);
	    
	    	// recuperation mailExpediteur
	    	String mailExpediteur = spocClientWS.recuperationDeAdresseMail(OmphaleBatchConfig.mailFrom);
	    	
	    	// recuperation mailDestinataire
	        String mailDestinataireChargementRP = spocClientWS.recuperationDeAdresseMail(OmphaleBatchConfig.mailDestinataireChargementRP);
	    	
	    	// recuperation mailCopie
	    	String mailCopie = spocClientWS.recuperationDeAdresseMail(OmphaleBatchConfig.mailCopie);
	
	    	// envoie du mail au destinataire principal
			String result = spocClientWS.envoyerUnMail(mailExpediteur, mailDestinataireChargementRP, "Service Omphale de chargement", message);
			spocClientWS.onVerifieQueLeMailABienEteTransmis(result);
			
			// envoie du mail du destinataire en copie
	    	result = spocClientWS.envoyerUnMail(mailExpediteur, mailCopie, "Service Omphale de chargement", message);
			spocClientWS.onVerifieQueLeMailABienEteTransmis(result);
        
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new OmphaleMetierException("Probleme mail service SPOC", e);
	    }
    }
    
    
	public String constructionDuMessageDuMail(BeanRapport beanRapport, BeanParametresResultat beanParametresResultat){
		StringBuffer contenuMessage = new StringBuffer();

		String ficheProjectionHTML = "";
		if(beanRapport.ficheProjectionHTML == null){
			ficheProjectionHTML = "";
		}
		contenuMessage
		 .append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">")
		 	.append("<html>")
				.append("<head>")
					.append("<style>")
						.append("p{font-size: small;font-family: arial;}")
					.append("</style>")
				.append("</head>")
				.append("<body>")
					.append("<p> message du service des chargements omphale </p>")
					.append("<p>")
	            		.append("Code erreur : ").append(beanRapport.code)
	            	.append("</p>")
	            	.append("<p>")
	              		.append("Message : ").append(beanRapport.erreurProjection)
	              	.append("</p>")
	              	.append("<p>")
	              		.append(ficheProjectionHTML)
	              	.append("</p>")
	            .append("</body>")
            .append("<html>");
		
		return contenuMessage.toString();
	}

}
