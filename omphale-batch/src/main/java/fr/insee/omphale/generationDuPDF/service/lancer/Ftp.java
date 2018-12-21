package fr.insee.omphale.generationDuPDF.service.lancer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.exception.OmphaleConfigResultException;
import fr.insee.omphale.generationDuPDF.exception.OmphaleResultException;


public class Ftp {

	
	public void storeFile(String filename, String remoteFilename, BeanParametresResultat beanParametresResultat) throws OmphaleConfigResultException, OmphaleResultException {
		
	    FTPClient client = null;
		FileInputStream fileInputStream = null;
	    try {
	    	
	        String server=beanParametresResultat.getFtpServer();
	      
	      /* connect */
		  client = new FTPClient();
		  try {
			  client.connect(server); 
		  }
		  catch (IOException iOexception) {
			  throw new OmphaleConfigResultException("FTP server refused connection.", iOexception);
		  }

	      // After connection attempt, you should check the reply code to verify
	      // success.
	      int reply = client.getReplyCode();
	      if(!FTPReply.isPositiveCompletion(reply)) {
			try {
	        	client.disconnect();
  			} 
	  		catch(IOException iOException) {
	  			throw new OmphaleConfigResultException("IOException", iOException);
	  		}
			throw new OmphaleConfigResultException("FTP server refused connection.");
	      }
	      
	      /* login */
		   
		  client.login(beanParametresResultat.getFtpUser(), beanParametresResultat.getFtpPwd()); // "user", "password"
		   
	      client.setFileType(FTP.BINARY_FILE_TYPE);
	      client.setBufferSize(8192);
	      client.enterLocalPassiveMode();
	      /* transfer files */
		  fileInputStream = new FileInputStream(filename);
		  boolean successfullyCompleted = client.storeFile(remoteFilename, fileInputStream);
		  if (!successfullyCompleted) {
		    	throw new OmphaleResultException("transfer files not successfully completed");
		  }

	      
		  /* logout */
		  try {
			  client.logout();
		  }
		  catch (IOException iOexception) {
			  throw new OmphaleResultException("erreur logout", iOexception);
		  }
	    } 
	    catch (FileNotFoundException fileNotFoundException) {
	    	throw new OmphaleResultException("FileNotFoundException", fileNotFoundException);
		}
		catch(IOException iOException) {	
			throw new OmphaleResultException("IOException", iOException);
		}
	    finally {
	    	try {
	    		if (fileInputStream != null) {
	    			fileInputStream.close();
	    		}
	    	}
	    	catch(IOException iOException) {
	    		throw new OmphaleResultException("IOException", iOException);
	    	}
	    	if(client.isConnected()) {
	    		try {
	    			/* disconnect */
	    			client.disconnect();
	    		} 
	    		catch(IOException iOException) {
	    			throw new OmphaleConfigResultException("IOException", iOException);
	    		}
	    	}
	    }

	}	
	
	
   
  
}


