package fr.insee.omphale.generationDuPDF.service.lancer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.insee.omphale.generationDuPDF.exception.OmphaleResultException;




/**
 * Attention cette classe est désormais disponible dans le projet omphale-batch2
 * package /omphale-batch2/src/main/java/fr/insee/omphale/processus/service
 * 
 * la classe ci-dessous est obsolète
 * 
 * ce changement a été opéré pour accèder à OmphaleBatchConfig
 */
public class Zip {
	
   final static int BUFFER = 2048;
   
   public void directoryZip(String repertoire, String fichier2) throws OmphaleResultException {
	      try {
	          BufferedInputStream origin = null;
	          FileOutputStream dest = new FileOutputStream(fichier2); // fichier zippé
	          ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
	          byte data[] = new byte[BUFFER];
	          // get a list of files from current directory
	          File f = new File(repertoire); 
	          String files[] = f.list();

	          for (int i=0; i<files.length; i++) {
	             FileInputStream fi = new FileInputStream(repertoire + "\\" + files[i]);
	             origin = new BufferedInputStream(fi, BUFFER);
	             ZipEntry entry = new ZipEntry(files[i]);
	             out.putNextEntry(entry);
	             int count;
	             while((count = origin.read(data, 0, BUFFER)) != -1) {
	                out.write(data, 0, count);
	             }
	             origin.close();
	          }
	          out.close();
	       } catch(Exception e) {
	          throw new OmphaleResultException("erreur en cours de compression fichier", e);
	       }
   }
   

    public void fichierZip(String fichier1, String fichier2) throws OmphaleResultException {
	    try {
	        BufferedInputStream origin = null;
	        FileOutputStream dest = new FileOutputStream(fichier2);
	        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
	        byte data[] = new byte[BUFFER];
	
		    FileInputStream fi = new FileInputStream(fichier1);
		    origin = new BufferedInputStream(fi, BUFFER);
		    ZipEntry entry = new ZipEntry(fichier1);
		    out.putNextEntry(entry);
		    int count;
		    while((count = origin.read(data, 0, BUFFER)) != -1) {
		    	out.write(data, 0, count);
		    }
		    origin.close();
		    out.close();
    	} 
	    catch(Exception e) {
	          throw new OmphaleResultException("erreur en cours de compression fichier", e);
	    }
	}
    

}


