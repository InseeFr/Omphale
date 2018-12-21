package fr.insee.omphale.utilitaireDuGroupeJava2010.classpath;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Objet représentant une ressource trouvée dans un fichier jar (ou zip) du
 * classpath.
 *
 */
public class InputStreamZip implements IClasspathInputStream {

	/**
	 * Le nom du fichier jar (ou zip) contenant la ressource du classpath.
	 */
	private String zipFileName;

	/**
	 * Le nom de l'entrée dans le fichier jar (ou zip)
	 */
	private String zipEntryName;

	/**
	 * Constructeur.
	 *
	 * @param zipFileName
	 *            Le nom du fichier jar (ou zip) contenant la ressource du
	 *            classpath.
	 * @param zipEntryName
	 *            Le nom de l'entrée dans le fichier jar (ou zip)
	 */
	public InputStreamZip(String zipFileName, String zipEntryName) {
		super();
		this.zipFileName = zipFileName;
		this.zipEntryName = zipEntryName;
	}


	public InputStream getInputStream() throws IOException {
		@SuppressWarnings("resource")
		ZipFile zipFile = new ZipFile(zipFileName);
		ZipEntry entry = zipFile.getEntry(zipEntryName);
		return zipFile.getInputStream(entry);
	}

}
