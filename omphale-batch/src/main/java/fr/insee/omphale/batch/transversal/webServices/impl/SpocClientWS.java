package fr.insee.omphale.batch.transversal.webServices.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.xml.sax.InputSource;

import fr.insee.archi.spoc.content.MailingReport;
import fr.insee.archi.spoc.content.MessageTemplate;
import fr.insee.archi.spoc.content.NameValuePairType;
import fr.insee.archi.spoc.content.Recipient;
import fr.insee.archi.spoc.content.ReportItem;
import fr.insee.archi.spoc.content.SendRequest;
import fr.insee.archi.spoc.content.SendRequest.Recipients;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;

public class SpocClientWS {

    
	// appels aux couches de service
	private IgesaClientWS igesaClientWS ;
	
    // informations utiles au Web Service SPOC
	private String SEND_SERVICE_URL;
	private String IDENT;
	private String PASSW;
	@SuppressWarnings("unused")
	private String TRUST_STORE;
	@SuppressWarnings("unused")
	private String TRUST_STORE_PASSWORD;

	@SuppressWarnings("static-access")
	public String recuperationDeAdresseMail(String sujet) throws OmphaleMetierException{
    	String resultat = null;
    	
    	if(sujet != null && sujet.length()== 6){
    		resultat = igesaClientWS.rechercherMailFonctionIdep(sujet);
    	}else if(sujet != null){
    		resultat = sujet;
    	}else{
			throw new OmphaleMetierException("Probleme mail service SPOC : adresse mail est à vide");
    	}
    	
    	return resultat;
    }

    
	public String envoyerUnMailAvecPJ(String expediteur, String destinataire,
			String objet, String message, String cheminPJ) {
		
		MessageTemplate messageTemplate = new MessageTemplate();
		NameValuePairType nameValuePairType = new NameValuePairType();
		SendRequest request = new SendRequest();
		
		// initialisation du contenu du message
		nameValuePairType.setName("Content-Type");
		nameValuePairType.setValue("text/html; charset=UTF-8");
		messageTemplate.getHeader().add(nameValuePairType);

		// initialisation de l'expéditeur et de l'objet
		messageTemplate.setSender(expediteur);
		messageTemplate.setSubject(objet);
		messageTemplate.setContent(message);
		
		// initialisation de la liste des destinataires
		Recipients destinataires = new Recipients();
		
		// initialisation du destinataire utilisateur Omphale et ajout à la liste des destinataires
		Recipient recipient = new Recipient();
		recipient.setAddress(destinataire);
		
		File filePJ = new File(cheminPJ);

		List<String> attachments = new ArrayList<String>();
		attachments.add(filePJ.getName());
		recipient.setAttachments(attachments);
		destinataires.getRecipients().add(recipient);

		
		// ajout à la requête du contenu du mail
		request.setMessageTemplate(messageTemplate);
		
		// ajout à la requête des destinataires
		request.setRecipients(destinataires);	
		
		// création d'un client  pour SPOC
		ClientBuilder builder = ClientBuilder.newBuilder();

		// initialisation du key store certificat pour SSL

		ignorerEtapeVerificationSSL(true, builder);
		
		Client client = builder.build();

		// authentification 
		HttpAuthenticationFeature authentificationFeature = HttpAuthenticationFeature.basic(IDENT, PASSW);
		client.register(authentificationFeature);
		
		// initialiation du multi part
		client.register(MultiPartFeature.class);
			
		FormDataMultiPart mp = new FormDataMultiPart();
		
		mp.bodyPart(new FormDataBodyPart(FormDataContentDisposition.name("request").build(),
                request,
    		   MediaType.APPLICATION_XML_TYPE));
		
		final FileDataBodyPart filePart = new FileDataBodyPart("attachments", filePJ);
		mp.bodyPart(filePart);
			
		// envoie de la requête à SPOC
		String result = client.target(SEND_SERVICE_URL)
					   .request(MediaType.APPLICATION_XML_TYPE)
					   .post(Entity.entity(mp, MediaType.MULTIPART_FORM_DATA_TYPE),
								String.class); 	
		
		return result;
	}
	
	public String envoyerUnMail(String expediteur, String destinataire,
			String objet, String message){
		

		MessageTemplate messageTemplate = new MessageTemplate();
		NameValuePairType nameValuePairType = new NameValuePairType();
		SendRequest request = new SendRequest();
		
		// initialisation du contenu du message
		nameValuePairType.setName("Content-Type");
		nameValuePairType.setValue("text/html; charset=UTF-8");
		messageTemplate.getHeader().add(nameValuePairType);

		// initialisation de l'expéditeur et de l'objet
		messageTemplate.setSender(expediteur);
		messageTemplate.setSubject(objet);
		messageTemplate.setContent(message);
		
		// initialisation de la liste des destinataires
		Recipients destinataires = new Recipients();
		
		// initialisation du destinataire utilisateur Omphale et ajout à la liste des destinataires
		Recipient recipient = new Recipient();
		recipient.setAddress(destinataire);
		destinataires.getRecipients().add(recipient);
		
		// ajout à la requête du contenu du mail
		request.setMessageTemplate(messageTemplate);

		
		// ajout à la requête des destinataires
		request.setRecipients(destinataires);	
		
		// création d'un client  pour SPOC
		ClientBuilder builder = ClientBuilder.newBuilder();

		// initialisation du key store certificat pour SSL
		ignorerEtapeVerificationSSL(true, builder);
		
		Client client = builder.build();

		// authentification 
		HttpAuthenticationFeature authentificationFeature = HttpAuthenticationFeature.basic(IDENT, PASSW);
		client.register(authentificationFeature);
			
		// envoie de la requête à SPOC
		String result = client.target(SEND_SERVICE_URL)
					   .request(MediaType.APPLICATION_XML_TYPE)
					   .post(Entity.entity(request, MediaType.APPLICATION_XML),
							   String.class);
		
		return result;
	}
	

	/**
	 * 
	 * on vérifie à partir du message XML de retour de SPOC suite à l'envoi de mail
	 * 	que pour chaque mail envoyé le message est égale à 0
	 * 
	 * ce qui signifie que SPOC a bien envoyé le message
	 * 
	 * Attention aucune vérification par SPOC n'est faite pour les mails destinataires comme expéditeur
	 * 
	 * @param result
	 * @return
	 * @throws Exception 
	 */
	public void onVerifieQueLeMailABienEteTransmis(String result) throws Exception{
    	
    	boolean resultat = true;
    	
    	JAXBContext context = JAXBContext.newInstance(MailingReport.class);
    	InputSource inputSource = new InputSource(new ByteArrayInputStream(result.getBytes("utf-8")));
    	
    	Unmarshaller unmarshaller = context.createUnmarshaller();
    	
    	MailingReport mailingReport = (MailingReport)unmarshaller.unmarshal(inputSource);
    	for (ReportItem reportItem : mailingReport.getReportItem()) {
    		if(!"OK".toUpperCase().equals(reportItem.getMessage())){
    			resultat = false;
    		}
		}
    	if(!resultat){
			throw new Exception();
    	}

    }

	/**
	 * 
	 * Cette méthode s'occupe de la gestion des cerficats selon l'environnement de déploiement
	 * 		si en production les properties seront à vides donc la méthode ne fait rien
	 * 		si en dév/recette les properties seront renseignés
	 * 			le keystore est disponible sous fr/insee/config/spoc
	 *  
	 * @param result
	 * @return
	 * @throws Exception 
	 */
	public void gestionKeyStore(String TRUST_STORE, String TRUST_STORE_PASSWORD){
		// gestion du HTTPS

		if (TRUST_STORE != null && !TRUST_STORE.isEmpty()) {
			
			InputStream keystoreInput = this.getClass().getClassLoader().getResourceAsStream(TRUST_STORE);

		try {
			File keystore = new File(System.getProperty("java.io.tmpdir"),"keystore");
			FileOutputStream outputStream = new FileOutputStream(keystore);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = keystoreInput.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			keystoreInput.close();
			outputStream.flush();
			outputStream.close();

			System.setProperty("javax.net.ssl.trustStore",	keystore.getAbsolutePath());
			System.setProperty("javax.net.ssl.trustStorePassword",TRUST_STORE_PASSWORD);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

		
		
		
	}


	/**
	 * 
	 * cette méthode avec le paramètre ignoreHTTPS à true permet de désactiver l'étape de vérificiation du certificat Serveur
	 * En effet, pour bien faire le programme devra avoir connaissance dans une key store de l'AC qui a signé ce certificat
	 * 
	 * @param ignoreHTTPS
	 * @param builder
	 * @return Client
	 */
	private Client ignorerEtapeVerificationSSL(	boolean ignoreHTTPS, 
												ClientBuilder builder){
		if (ignoreHTTPS) {
			TrustManager[] certs = new TrustManager[] { 
						new X509TrustManager() 
						{
								@Override
								public X509Certificate[] getAcceptedIssuers() {
										return null;
								}
					
								@Override
								public void checkServerTrusted(X509Certificate[] chain,	String authType) throws CertificateException {
								}
					
								@Override
					
								public void checkClientTrusted(X509Certificate[] chain,	String authType) throws CertificateException {
								}
					
						} 
			};

			SSLContext ctx = null;

			try {
				ctx = SSLContext.getInstance("TLS");
				ctx.init(null, certs, new SecureRandom());
			} 
			catch (java.security.GeneralSecurityException ex) {
				ex.printStackTrace();
			}


			builder.sslContext(ctx).hostnameVerifier(
						new HostnameVerifier() {
							@Override
							public boolean verify(String hostname, SSLSession session) {
								return true;
							}
						}
						);

		}
		return builder.build();
	}

    
    public void setIgesaClientWS(IgesaClientWS igesaClientWS) {
		this.igesaClientWS = igesaClientWS;
	}
    
	public void setSEND_SERVICE_URL(String sEND_SERVICE_URL) {
		SEND_SERVICE_URL = sEND_SERVICE_URL;
	}

	public void setIDENT(String iDENT) {
		IDENT = iDENT;
	}

	public void setPASSW(String pASSW) {
		PASSW = pASSW;
	}

	public void setTRUST_STORE(String tRUST_STORE) {
		TRUST_STORE = tRUST_STORE;
	}

	public void setTRUST_STORE_PASSWORD(String tRUST_STORE_PASSWORD) {
		TRUST_STORE_PASSWORD = tRUST_STORE_PASSWORD;
	}



}
