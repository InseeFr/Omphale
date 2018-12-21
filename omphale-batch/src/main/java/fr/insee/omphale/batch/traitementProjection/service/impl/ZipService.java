package fr.insee.omphale.batch.traitementProjection.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import fr.insee.omphale.batch.traitementProjection.dao.CacheDaoManager;
import fr.insee.omphale.batch.traitementProjection.service.IServiceBatch;
import fr.insee.omphale.batch.transversal.bean.BeanRapport;
import fr.insee.omphale.batch.transversal.exception.OmphaleConfigException;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.batch.transversal.util.OmphaleBatchConfig;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.exception.OmphaleResultException;

/**
 * Classe de service responsable de la compression en fichier zip des fichiers
 * CSV resultat de la projection, sous-traitée au module résultat.
 */
public class ZipService implements IServiceBatch {
	
    /**
     * Méthode de service.
     * @param beanParametresResultat paramètres pour le module résultat.
     * @throws OmphaleMetierException en cas d'erreur.
     */
    public void executeService() throws OmphaleConfigException,
            OmphaleMetierException {
        BeanRapport beanRapport = CacheDaoManager.beanRapport;
        BeanParametresResultat beanParametresResultat = beanRapport.results;

        String repertoire = beanParametresResultat.getNomBaseFichiers();
        try {
            directoryZip(repertoire, repertoire + ".zip");
        } catch (OmphaleResultException e) {
            e.printStackTrace();
            throw new OmphaleMetierException("Probleme mail service", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new OmphaleConfigException("Probleme mail service", e);
        }
    }
    
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
 	             FileInputStream fi = new FileInputStream(repertoire + OmphaleBatchConfig.dirSeparateur + files[i]);
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
 	          throw new OmphaleResultException("erreur au cours de la compression fichier", e);
 	       }
    }
    
}
