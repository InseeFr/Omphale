package fr.insee.omphale.utilitaireDuGroupeJava2010.classpath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Objet représentant une ressource du classpath.
 *
 */
public class InputStreamFile implements IClasspathInputStream {

	/**
	 * Le nom du fichier de la ressource du classpath.
	 */
	private String fileName;

	/**
	 * Constructeur
	 *
	 * @param fileName
	 *            Le nom du fichier de la ressource du classpath.
	 */
	public InputStreamFile(String fileName) {
		super();
		this.fileName = fileName;
	}


	public InputStream getInputStream() throws IOException {
		File file = new File(fileName);
		return new FileInputStream(file);
	}

}
