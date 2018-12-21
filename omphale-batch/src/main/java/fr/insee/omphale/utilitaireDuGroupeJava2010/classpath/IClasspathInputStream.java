package fr.insee.omphale.utilitaireDuGroupeJava2010.classpath;

import java.io.IOException;
import java.io.InputStream;

/**
 * Interface donnant accès, sous forme d'InputStream, à une ressource présente
 * dans le classpath.
 *
 */
public interface IClasspathInputStream {

	/**
	 * Retourne la ressource présente dans le classpath sous forme
	 * d'InputStream.
	 *
	 * @return La ressource sous forme d'InputStream.
	 * @throws IOException
	 *             En cas de problème d'entrée-sortie sur la ressource
	 */
	InputStream getInputStream() throws IOException;
}
