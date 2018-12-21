package fr.insee.omphale.generationDuPDF.service.lancer;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

import fr.insee.omphale.generationDuPDF.exception.OmphaleResultException;


public class Mail {
	

	public void mail(
			String hostName, 			
			String[][] destinataires, 	
			String[][] copie, 			
			String[] emailFrom, 		
			String subject, 			
			String message
					) throws OmphaleResultException {
		try {
		  SimpleEmail email = new SimpleEmail();
		  email.setHostName(hostName); 
		  // destinataires
		  if (destinataires != null) {
			  for(int i = 0 ; i < destinataires.length ; i++) {
				  email.addTo(destinataires[i][0], destinataires[i][1]); 
			  } 
		  }
		  // copie
		  if (copie != null) {
			  for(int i = 0 ; i < copie.length ; i++) {
				  email.addCc(copie[i][0], copie[i][1]); 
			  } 
		  }
		  // from
		  email.setFrom(emailFrom[0], emailFrom[1]);
		  // sujet
		  email.setSubject(subject);
		  // message
		  email.setMsg(message);
		  //
		  email.send();
		}
		catch(EmailException emailException){
			throw new OmphaleResultException(emailException);
		}
	}
	
	public void mailPieceJointe(
			String hostName, 				
			String[][] destinataires, 		
			String[][] copie, 				
			String[] emailFrom,				
			String subject, 				
			String message,
			String[] attachmentPath, 		
			String[] attachmentNomOutlook 	
								) throws OmphaleResultException {
		try {
			  		  
		  // Create the email message
		  MultiPartEmail email = new MultiPartEmail();
		  // 
		  email.setHostName(hostName); 
		  // destinataires
		  if (destinataires != null) {
			  for(int i = 0 ; i < destinataires.length ; i++) {
				  email.addTo(destinataires[i][0], destinataires[i][1]); 
			  } 
		  }
		  // copie
		  if (copie != null) {
			  for(int i = 0 ; i < copie.length ; i++) {
				  email.addCc(copie[i][0], copie[i][1]); 
			  } 
		  }
		  // from
		  email.setFrom(emailFrom[0], emailFrom[1]);
		  // sujet
		  email.setSubject(subject);
		  // message
		  email.setMsg(message);
		  // attachment
		  if (attachmentPath != null) {
			  for(int i = 0 ; i < attachmentPath.length ; i++) {
				  EmailAttachment attachment = new EmailAttachment();
				  attachment.setPath(attachmentPath[i]);
				  attachment.setDisposition(EmailAttachment.ATTACHMENT);
				  attachment.setDescription("description pièce jointe");
				  if (attachmentNomOutlook != null && attachmentNomOutlook.length >= i + 1) {
					  attachment.setName(attachmentNomOutlook[i]); 
				  }
				  // add the attachment
				  email.attach(attachment);
			  } 
		  }
		  // send the email
		  email.send();
		}
		catch(EmailException emailException){
			throw new OmphaleResultException(emailException); 
		}
	}	
	
}