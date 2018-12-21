package fr.insee.omphale.batch.traitementProjection.service.impl;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.IServiceBatch;
import fr.insee.omphale.batch.transversal.bean.BeanRapport;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.batch.transversal.webServices.impl.SpocClientWS;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

public class MailService implements IServiceBatch {

   
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
     * Appels au CacheDAOManager d'Omphale Batch
     */


    /**
     * Méthode de service.
     * @param beanRapport paramètres pour construction du message de compte
     *        rendu.
     * @throws OmphaleMetierException en cas d'erreur.
     */
    public void executeService() throws OmphaleMetierException{
 		//La condition if est mise en commentaire car elle ne fonctionne pas,
 		//l'idep de l'administrateur etant caché. Pour rendre ces lignes fonctionnelles, il 
 		//faut remplacer idep_administrateur par le vrai idep de l'administrateur.
//    	if(!"idep_administrateur".equals(CacheDaoManager.tableUser_omphale.get(0).IDEP)){
	
	    	String objet = "Projection Omphale terminee";
	    	String cheminPJ = OmphaleBatchConfig.APPLISHARE_RacineDepotResultatProjection + OmphaleBatchConfig.dirSeparateur+CacheDaoManager.beanRapport.results.getNomFichierPdf();
	    	BeanRapport informationsProjectionEnCoursTraitement = CacheDaoManager.beanRapport;
	        BeanParametresResultat informationsProjectionsGraphiqueEtCSV = CacheDaoManager.beanRapport.results;
	    	String result = null;
	        
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
		    	String mailDestinataire = null;
		    	if(OmphaleBatchConfig.mailDestinataire == null){
		        	mailDestinataire = spocClientWS.recuperationDeAdresseMail(CacheDaoManager.tableUser_omphale.get(0).IDEP);
		    	} else{
		        	mailDestinataire = spocClientWS.recuperationDeAdresseMail(OmphaleBatchConfig.mailDestinataire);
		    	}
		    	
		    	// recuperation mailCopie
		    	String mailCopie = spocClientWS.recuperationDeAdresseMail(OmphaleBatchConfig.mailCopie);
		    	
				if ("true".equalsIgnoreCase(OmphaleBatchConfig.mailPieceJointe)&&
						Integer.parseInt(CacheDaoManager.getFirstProjectionLancee().NBR_ZONE) 
		                < Integer.parseInt(OmphaleBatchConfig.nbrZoneGrosZonage)){
					result = spocClientWS.envoyerUnMailAvecPJ(mailExpediteur, mailDestinataire, objet, message, cheminPJ);
		    		spocClientWS.onVerifieQueLeMailABienEteTransmis(result);
				}else{
					result = spocClientWS.envoyerUnMail(mailExpediteur, mailDestinataire, objet, message);
		    		spocClientWS.onVerifieQueLeMailABienEteTransmis(result);
				}
				
		        if (mailCopie != null) {
		        	result = spocClientWS.envoyerUnMail(mailExpediteur, mailCopie, objet, message);
		    		spocClientWS.onVerifieQueLeMailABienEteTransmis(result);
		        } 
	        
		    } catch (Exception e) {
		    	e.printStackTrace();
		        throw new OmphaleMetierException("Probleme mail service SPOC", e);
		    }
//    	}
    }
 
	
	public String constructionDuMessageDuMail(BeanRapport beanRapport, BeanParametresResultat beanParametresResultat){
		 StringBuffer contenuMessage = new StringBuffer();
	        
	     // première partie du message avec notamment les éléments d'une projection
		 contenuMessage
		 	.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\"> <html>")
		 	.append("<HTML>")
		 		.append("<head>")
		 			.append("<style>")
		 				.append("p{font-size: small;font-family: arial;}")
		 			.append("</style>")
		 		.append("</head>")
		 		.append("<body>")
		 			.append("<p> Message du service asynchrone des projections omphale </p>")
		 			.append("<p>")
		 				.append("Code erreur : ").append(beanRapport.code)
		 			.append("</p>")
		            .append("<p>")
		            	.append("Etape : ").append(beanRapport.etape)
		            .append("</p>")
		            .append("<p>")
		            	.append("Message : ").append(beanRapport.message)
		            .append("</p>")
		            .append("<p>")
		            	.append(beanRapport.ficheProjectionHTML)
		            .append("</p>")
		            .append("<p>")
		            	.append(beanRapport.erreurProjection)
		            .append("</p>");

	        
	       // si toutes les étapes ont été exécutées avec succès
	       // alors ajout du lien hypertexte indiquant le dépôt du zip contenant CSV et PDF
	       if (OrdonnanceurService.lastTraitementOk == OrdonnanceurService.lastTraitement) {
	
	           contenuMessage
	              	.append("<p>")
	              		.append("url des fichier CSV:")
	              	.append("</p>")
	              	.append("<p>")
	              			.append("<a href=\"")
	              				.append(OmphaleBatchConfig.APPLISHARE_RacineResultatProjectionAffichageMail).append(OmphaleBatchConfig.dirSeparateur).append(beanParametresResultat.getNomDossier()).append(".zip")
	              			.append(("\">"))
	              				.append(OmphaleBatchConfig.APPLISHARE_RacineResultatProjectionAffichageMail).append(OmphaleBatchConfig.dirSeparateur).append(beanParametresResultat.getNomDossier()).append(".zip")
	              			.append("</a>")
	              	.append("</p>")
	                       ;
	               
	
	           }
	       
	        contenuMessage
	        		.append("</body>")
	        	.append("</html>");
		
		return contenuMessage.toString();
	}
 }
