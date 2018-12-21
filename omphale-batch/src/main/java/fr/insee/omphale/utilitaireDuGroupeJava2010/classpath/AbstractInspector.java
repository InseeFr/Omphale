package fr.insee.omphale.utilitaireDuGroupeJava2010.classpath;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.lang.StringUtils;

/**
 * Classe abstraite utilitaire pour l'inspection du classpath.
 * 
 */
public abstract class AbstractInspector {


    /** Nom du package de recherche */
    private String packageParent = null;

    /** Repertoire de recherche (en terme de resource) */
    private String packageResourceName = null;

    /** Repertoire de recherche (en terme de fileSystem) */
    private String packageFileName = null;

    private List<URL> classpath = new ArrayList<URL>();

    /** Filtre utilisé lors d'un examen de repertoire physique * */
    private FiltreFichier filter = new FiltreFichier();;

    /**
     * Constructeur.
     * 
     * @param packageParent
     *            Nom du package parent dont doivent faire partie les
     *            ressources.
     * @param filtreFichier
     *            Filtre unique à appliquer aux ressources
     *            {@link fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.FiltreFichier}
     * @throws ClasspathException
     *             En cas d'erreur lors de la recherche.
     */
    public AbstractInspector(String packageParent, String filtreFichier)
            throws ClasspathException {

        filter.addPattern(filtreFichier);
        init(packageParent);
    }

    /**
     * Constructeur.
     * 
     * @param packageParent
     *            Nom du package parent dont doivent faire partie les
     *            ressources.
     * @param filtreFichiers
     *            Liste de filtres à appliquer aux ressources
     *            {@link fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.FiltreFichier}
     * @throws ClasspathException
     *             En cas d'erreur lors de la recherche.
     */
    public AbstractInspector(String packageParent, List<String> filtreFichiers)
            throws ClasspathException {
        filter = new FiltreFichier();
        for (String pattern : filtreFichiers) {
            filter.addPattern(pattern);
        }
        init(packageParent);
    }

    /**
     * Initialisation des variables.
     * 
     * @param packageParent
     *            Nom du package parent dont doivent faire partie les
     *            ressources.
     * @throws ClasspathException
     *             En cas d'erreur lors de la recherche.
     */
    protected void init(String packageParent) throws ClasspathException {

        loadClasspath();

        /** Initialisation * */
        this.packageParent = packageParent;

        /** On calcul le repertoire d'acces aux repertoires * */
        this.packageResourceName = this.packageParent.replace('.', '/');
        this.packageFileName = this.packageParent.replace('.',
                File.separatorChar);
    }

    /**
     * Exploitation de la propriété système java.class.path.
     * 
     * @throws ClasspathException
     *             en cas d'erreur dans la définition de java.class.path
     */
    private void loadClasspath() throws ClasspathException {
        String classpath = System.getProperty("java.class.path");
        String[] tabs = StringUtils.split(classpath, System.getProperty("path.separator"));
        for (int i = 0; i < tabs.length; i++) {
            File f = new File(tabs[i]);
            if (f.exists()) {
                try {
                	URI uri = f.toURI();
                	URL url = uri.toURL();
                    this.classpath.add(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    throw new ClasspathException(
                            "Impossible d'exploiter le classpath de l'application.",
                            e);
                }
            }
        }
    }

    /**
     * Examen du classpath en vue de la recherche des ressources.
     * 
     * @throws ClasspathException
     *             en cas d'erreur dans la recherche.
     */
    protected void examineFiles() throws ClasspathException {
        try {
        	
            Iterator<URL> iter = this.classpath.iterator();
            while (iter.hasNext()) {
                URL url = iter.next();
                String courant = url.toExternalForm();
                if (courant.endsWith(".jar") || courant.endsWith(".zip")) {
                    this.examineFilesFromJar(url);
                } else {
                    String s = URLDecoder.decode(url.getFile(), "UTF-8");
                    this.examineFilesFromDirectory(new File(s));
                }
            }

        } catch (IOException e) {
            throw new ClasspathException(
                    "Erreur rencontrée durant la recherche des fichiers", e);
        }
    }

    /**
     * Examen d'un directory présent dans le classpath.
     * 
     * @param directory
     *            repertoire présent dans le classpath.
     */
    private void examineFilesFromDirectory(File directory) {
        if (!directory.isDirectory())
            return;
        String className = null;
        String fileName = null;

        File[] files = directory.listFiles(this.filter);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                this.examineFilesFromDirectory(file);
                continue;
            }

            /** Transformer le chemin en package * */
            className = file.getAbsolutePath();
            int pos = className.indexOf(this.packageFileName);
            if (pos == -1)
                continue;
            className = className.substring(pos, className.length()).replace(
                    File.separatorChar, '.');

            fileName = className.substring(this.packageParent.length() + 1,
                    className.length());
            /** Charger le fichier */
            chargeObjectFile(fileName, file.getAbsolutePath());
        }
    }

    /**
     * Examen d'un fichier jar(ou zip) présent dans le classpath.
     * 
     * @param url
     *            Url du fichier jar présent dans le classpath.
     * @throws ClasspathException
     *             en cas de problème d'ouverture du fichier jar.
     */
    private void examineFilesFromJar(URL url) throws ClasspathException {        
    	String jarName = url.getFile();
    	jarName = jarName.replace("%20", " ");
        // Examen du nom du jar à traiter :
        // si le nom commence par //, il s'agit d'un nom de serveur Windows,
        // donc ne rien faire
        // si le nom commence par un seul /, il faut l'enlever pour retrouver un
        // nom de fichier conforme à la syntaxe Windows
        String os = System.getProperty("os.name").trim();
        String osDetermine = determineSystemeExploitation(os, "^(((W|w)indows)(.*)|((L|l)inux)(.*))$");
        if("windows".equals(osDetermine)) {
          if (jarName.startsWith("//")) {
        	  
	       } else if (jarName.startsWith("/")) {
	            jarName = jarName.substring(1);
	       }
	       jarName = jarName.replace('/', File.separatorChar);
        }


        String className = null;
        try {

            ZipFile zipFile = new ZipFile(jarName);

            Enumeration<?> e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();

                // Appliquer le filtre de fichier sur les noms
                String entryName = entry.getName();        
                if (!entryName.startsWith(this.packageResourceName))
                    continue;
                if (entryName.indexOf('$') >= 0)
                    continue;
                /* suppression de l'extension et remise sous forme de package */
                String fileName = StringUtils.substringBeforeLast(entryName,
                        ".");
                String extension = StringUtils.substringAfterLast(entryName,
                        ".");
                fileName = fileName.replace('/', '.');
                // conservation du dernier nom
                className = StringUtils.substringAfterLast(fileName, ".");
                if (!acceptFileInJar(className + "." + extension))
                    continue;

                /** Charger le plugin * */
                fileName = fileName.substring(this.packageParent.length() + 1,
                        fileName.length());
               chargeObjectZip(fileName + "." + extension, jarName, entry
                        .getName());

            }
            zipFile.close();
        } catch (Exception e) {
            throw new ClasspathException(
                    "Impossible de lire le jar " + jarName, e);

        }
    }
    
    
    /**
     * détermine le systeme d'exploitation sur lequel est execute le batch
     * recherche.
     * 
     * @param chaineAAnalyser (chaine identifiant l'OS)
     * @param modeleAAppliquer (modele regexp a appliquer)
     * @return String nom os trouve windows ou linux si l'entrée coreespond aux filtres de recherche, false sinon.
     * @return null si pas trouve
     * @exception java.lang.IllegalStateException si la methode group n'est pas valide
     */
	public static String determineSystemeExploitation(String chaineAAnalyser, String modeleAAppliquer) {
	    String result = null;
	    try {
		   Pattern p = Pattern.compile(modeleAAppliquer);

		   Matcher m = p.matcher(chaineAAnalyser);
		   @SuppressWarnings("unused")
		boolean match = m.matches();
		   result = m.group(1);

		} catch (java.lang.IllegalStateException e) {
			   e.printStackTrace();
			   return result;
		} 
	    return result;
	}

    /**
     * teste si une entrée de fichier jar (ou zip) est conforme aux filtres de
     * recherche.
     * 
     * @param entryName
     *            nom de l'entrée dans le fichier jar (ou zip)
     * @return true si l'entrée coreespond aux filtres de recherche, false sinon.
     */
    private boolean acceptFileInJar(String entryName) {
        List<String> patterns = filter.getPatterns();
        if (patterns.size() == 0) {
            return true;
        }
        boolean retour = false;
        String nomFichier = entryName.toLowerCase();
        for (String p : patterns) {
            String pattern = p.toLowerCase();
            retour = retour || FiltreFichier.testPattern(nomFichier, pattern);
        }
        return retour;

    }

    protected abstract void chargeObjectFile(String cle, String fileName);

    protected abstract void chargeObjectZip(String cle, String zipFileName,
            String zipEntryName);

    public String getPackageParent() {
        return packageParent;
    }

    public void setPackageParent(String packageParent) {
        this.packageParent = packageParent;
    }
}
