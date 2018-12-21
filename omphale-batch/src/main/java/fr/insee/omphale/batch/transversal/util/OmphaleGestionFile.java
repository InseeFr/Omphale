package fr.insee.omphale.batch.transversal.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;



/**
 * Classe utilitaire de gestion de fichiers. Omphale n'utilise pour l'instant
 * que la méthode deleteDirectory de suppression récursive d'un répertoire et de
 * son contenu. Mais on pourra ultérieurement utiliser les autres ...
 */
public class OmphaleGestionFile {
    
    /**
     * Méthodes utilitaires primitives (non symbolique) de gestion de fichiers
     * et de répertoires.
     */
 

    public static boolean deplacerForce(File source, File destination) {
        if (source == null)
            return false;
        if (destination == null)
            return false;
        if (destination.exists()) {
            supprimer(destination);
        }
        boolean  result = false;
        if (!result) {
            // On essaye de copier
            result = true;
            result = copier(source, destination);
        }
        return (result);

    }

    public static boolean deplacer(File source, File destination) {
        if (source == null)
            return false;
        if (destination == null)
            return false;
        if (!destination.exists()) {
            return (deplacerForce(source, destination));
        } else {
            // Si le fichier destination existe, on annule ...
            return (false);
        }
    }

    public static File[] listeRepertoire(File repertoire) {
        if (repertoire == null)
            return new File[0];
        if (repertoire.isDirectory())
            return repertoire.listFiles();
        return new File[0];
    }

    public static File[] listeRepertoireDirs(File repertoire) {
        File[] liste = new File[0];
        if (repertoire == null)
            return liste;
        ArrayList<File> lesDirs = new ArrayList<File>();
        if (repertoire.isDirectory())
            liste = repertoire.listFiles();

        for (int i = 0; i < liste.length; i++) {
            if (isRepertoire(liste[i]))
                lesDirs.add(liste[i]);
        }
        liste = new File[lesDirs.size()];
        for (int i = 0; i < liste.length; i++) {
            liste[i] = lesDirs.get(i);
        }
        return liste;
    }

    @SuppressWarnings("rawtypes")
	private static class FichierDate implements java.lang.Comparable {
        File fichier;

        public FichierDate(File fichier) {
            this.fichier = fichier;
        }

        public int compareTo(Object other) {
            long d1 = ((FichierDate) other).fichier.lastModified();
            long d2 = this.fichier.lastModified();
            if (d1 > d2)
                return -1;
            else if (d1 == d2)
                return 0;
            else
                return 1;
        }
    }

    @SuppressWarnings("unchecked")
    public static File[] listeRepertoireFicsDate(File repertoire) {

        File[] liste = new File[0];
        if (repertoire == null)
            return liste;
        List<FichierDate> lesFics = new ArrayList<FichierDate>();

        if (repertoire.isDirectory())
            liste = repertoire.listFiles();
        for (int i = 0; i < liste.length; i++) {
            if (!(isRepertoire(liste[i]))) {
                lesFics.add(new FichierDate(liste[i]));
            }
        }
        Collections.sort(lesFics);
        liste = new File[lesFics.size()];
        for (int i = 0; i < liste.length; i++) {
            liste[i] = lesFics.get(i).fichier;
        }
        return liste;
    }
    
    @SuppressWarnings("unchecked")
    /**
     * pour mettre a la fin le fichier cycle
     * de maniere a creer les cycles apres les chargements.
     */
    public static File[] listeRepertoireFicsDateFins(File repertoire, String[] fins) {
        if (fins==null) return listeRepertoireFicsDate(repertoire);
        File[] liste = new File[0];
        if (repertoire == null)
            return liste;
        List<FichierDate> lesFics = new ArrayList<FichierDate>();
        List<FichierDate> cycles = new ArrayList<FichierDate>();
        
        if (repertoire.isDirectory())
            liste = repertoire.listFiles();
        
        for (int i = 0; i < liste.length; i++) {
            if (!(isRepertoire(liste[i]))) {
                boolean ok=false;
                for (int j = 0; j < fins.length; j++) {
                    if (fins[j].equalsIgnoreCase(getPetitNom(liste[i]))) {
                        cycles.add(new FichierDate(liste[i]));
                        ok=true;
                        break;
                    }
                }
                if (!ok) {
                    lesFics.add(new FichierDate(liste[i]));
                }
            }
        }
        Collections.sort(lesFics);
        Collections.sort(cycles);
        lesFics.addAll(cycles);
        
        liste = new File[lesFics.size()];
        for (int i = 0; i < liste.length; i++) {
            liste[i] = lesFics.get(i).fichier;
        }
        return liste;
    }

    @SuppressWarnings("unchecked")
    /**
     * pour mettre a la fin le fichier cycle et le fichier commune_dependance
     * et pour mettre en premier le fichier commune
     * de maniere a creer les cycles apres les chargements.
     */
    public static File[] listeRepertoireFicsDateDebutsFins(File repertoire, String[] traitementEnPremier, String[] traitementDeFin) {
        if (traitementEnPremier==null && traitementDeFin==null) return listeRepertoireFicsDate(repertoire);
        if (traitementEnPremier==null) return listeRepertoireFicsDateFins(repertoire, traitementDeFin);

        File[] liste = new File[0];
        if (repertoire == null)
            return liste;
        List<FichierDate> normaux = new ArrayList<FichierDate>();
        List<FichierDate> EnPremiers= new ArrayList<FichierDate>();
        List<FichierDate> AlaFins = new ArrayList<FichierDate>();
        List<FichierDate> tous = new ArrayList<FichierDate>();
        
        if (repertoire.isDirectory())
            liste = repertoire.listFiles();
        
        for (int i = 0; i < liste.length; i++) {
            if (!(isRepertoire(liste[i]))) {
                boolean ok=false;
                for (int j = 0; j < traitementEnPremier.length; j++) {
                    if (traitementEnPremier[j].equalsIgnoreCase(getPetitNom(liste[i]))) {
                        EnPremiers.add(new FichierDate(liste[i]));
                        ok=true;
                        break;
                    }
                }
                for (int j = 0; j < traitementDeFin.length; j++) {
                    if (traitementDeFin[j].equalsIgnoreCase(getPetitNom(liste[i]))) {
                        AlaFins.add(new FichierDate(liste[i]));
                        ok=true;
                        break;
                    }
                }
                if (!ok) {
                    normaux.add(new FichierDate(liste[i]));
                }
            }
        }

        Collections.sort(normaux);
        Collections.sort(EnPremiers);
        Collections.sort(AlaFins);
        tous.addAll(EnPremiers);
        tous.addAll(normaux);
        tous.addAll(AlaFins);

        
        liste = new File[tous.size()];
        for (int i = 0; i < liste.length; i++) {
            liste[i] = tous.get(i).fichier;
        }
        return liste;
    }


    
    public static File[] listeRepertoireFics(File repertoire) {
        File[] liste = new File[0];
        if (repertoire == null)
            return liste;
        ArrayList<File> lesFics = new ArrayList<File>();

        if (repertoire.isDirectory())
            liste = repertoire.listFiles();

        for (int i = 0; i < liste.length; i++) {
            if (!(isRepertoire(liste[i])))
                lesFics.add(liste[i]);
        }
        liste = new File[lesFics.size()];
        for (int i = 0; i < liste.length; i++) {
            liste[i] = lesFics.get(i);
        }
        return liste;
    }

    public static boolean isRepertoire(File fichier) {
        if (fichier == null)
            return false;
        boolean result = true;
        result &= fichier.isDirectory();
        return result;
    }

    public static File getParent(File fichier) {
        if (fichier == null)
            return null;
        return fichier.getParentFile();
    }

    public static String getPetitNom(File fichier) {
        if (fichier == null)
            return null;
        return StringUtils.substringBeforeLast(fichier.getName(), ".");
    }
    
    public static String getExtension(File fichier) {
        if (fichier == null)
            return null;
        
        return StringUtils.substringAfterLast(fichier.getName(), ".");
    }

    public static boolean supprimer(File fichier) {
        if (fichier == null)
            return false;
        boolean result = true;
        result &= fichier.delete();
        return result;
    }

    public static boolean exist(File fichier) {
        if (fichier == null)
            return false;
        boolean result = true;
        result &= fichier.exists();
        return result;
    }

    public static boolean copier(File fichierDepart, File fichierArrive) {
        if (fichierDepart == null)
            return false;
        if (fichierArrive == null)
            return false;

        Path source = Paths.get(fichierDepart.getPath());
        Path destination = Paths.get(fichierArrive.getPath());
        try {
			Files.move(source, destination);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }

  
    /**
     * suppression récursive d'un répertoire et de son contenu.
     * @param path du répertoire à supprimer.
     * @return vrai si la suppression a réussi.
     */
    static public boolean deleteDirectory(File path) {
        boolean resultat = true;
       
        if( path.exists() ) {
                File[] files = path.listFiles();
                for(int i=0; i<files.length; i++) {
                        if(files[i].isDirectory()) {
                                resultat &= deleteDirectory(files[i]);
                        }
                        else {
                        resultat &= files[i].delete();
                        }
                }
        }
        resultat &= path.delete();
        return( resultat );
}

}
