package fr.insee.omphale.utilitaireDuGroupeJava2010.classpath;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Cette classe permet de dresser une liste de "ressources" et de leur
 * InputStream disponibles d'une application JAVA.
 * 
 * Ce module s'inspire d'un article d'André Sébastien sur
 * http://java.developpez.com/sources/?page=divers#plugins. Pour plus
 * d'informations, reportez vous sur ce site.
 * 
 * L'idée de cet inspecteur est de rechercher les ressources présentent dans le
 * classpath de l'application sous un "package parent". Les ressources sont
 * filtrées par un ou plusieurs patterns filtre {@link FiltreFichier}.
 * 
 * La propriété systeme "java.class.path" est utilisée afin de determiner toutes
 * les ressources. Il est ainsi possible de rajouter par la suite un fichier JAR
 * contenant de nouvelles ressources pour qu'elles soient automatiquement pris
 * en compte par l'application (pour peu que le fichier JAR soit rajouté
 * correctement au classpath). <br>
 * Pour identifier les ressources disponibles, le "ClasspathInspector" a besoin
 * :<br>
 * <ul>
 * <li>du package de recherche (ainsi que tous les sous-packages)</li>
 * <li>du ou des filtres à appliquer aux ressources</li>
 *</ul>
 * 
 * Il est possible d'avoir plusieurs "ClasspathInspector" pour gérer differentes
 * natures de ressources
 * 
 * Exemple : Si les ressources sont installées dans le package
 * fr.insee.monAppli.ressources et que le filtre *.sql
 * 
 * Récuperation des noms des ressources de l'application :<br>
 * <code>
 * Iterator ressources = new
 * ClasspathInspector("fr.insee.monAppli.ressources","*.sql").noms();<br>
 * while( ressources.hasNext() ) {<br>
 *    String ressource = (String)ressources.next();<br>
 *    System.out.println("ressource trouvée : " + ressource); <br>
 * }<code>
 * 
 * Récuperation des InputStream des ressources de l'application :<br>
 * <code>
 * Iterator ressources = new
 * ClasspathInspector("fr.insee.monAppli.ressources","*.sql").readers();<br>
 * while( ressources.hasNext() ) {<br>
 *    InputStream ressource = (InputStream)ressources.next();<br>
 *    ... <br>
 * }<code>
 * 
 * @see fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.FiltreFichier
 * 
 */
public class ClasspathInspector extends AbstractInspector {

	/** Liste des objets */
	private List<String> lesNoms = new ArrayList<String>();

	/** Map des InputStream */
	private Map<String, IClasspathInputStream> lesStreams = new TreeMap<String, IClasspathInputStream>();

	/**
	 * Constructeur.
	 * 
	 * @param packageParent
	 *            Nom du package parent dont doivent faire partie les
	 *            ressources. *
	 * @param filtreFichier
	 *            Filtre unique à appliquer aux ressources
	 *            {@link fr.insee.omphale.utilitaireDuGroupeJava2010.classpath}
	 * @throws ClasspathException
	 *             En cas d'erreur lors de la recherche.
	 */
	public ClasspathInspector(String packageParent, String filtreFichier)
			throws ClasspathException {

		super(packageParent, filtreFichier);
		/** Lance l'examen de l'application * */
		examineFiles();
	}

	/**
	 * Constructeur.
	 * 
	 * @param packageParent
	 *            Nom du package parent dont doivent faire partie les
	 *            ressources. *
	 * @param filtreFichiers
	 *            Liste de filtres à appliquer aux ressources
	 *            {@link fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.FiltreFichier}
	 * @throws ClasspathException
	 *             En cas d'erreur lors de la recherche.
	 */
	public ClasspathInspector(String packageParent, List<String> filtreFichiers)
			throws ClasspathException {
		super(packageParent, filtreFichiers);
		/** Lance l'examen de l'application * */
		examineFiles();
	}

	/**
	 * Retourne la liste des noms des ressources. Les noms des ressources sont
	 * relatifs au packageParent.<br>
	 * Exemple :<br> 
	 * Si le packageParent est : fr.insee.monAppli.ressources et que le filtre *.sql
	 * Si les ressources sont les suivantes :<br>
	 * fr.insee.monAppli.ressources.fichier1.sql
	 * fr.insee.monAppli.ressources.fichier2.sql
	 * fr.insee.monAppli.ressources.sous-dossier.fichier1.sql
	 * <br>alors, la liste de noms sera la suivante :<br>
	 * fichier1.sql<br>
	 * fichier2.sql<br>
	 * sous-dossier.fichier1.sql<br>
	 * 
	 * @return un iterator permettant de parcourir la liste des noms des
	 *         ressources.
	 */
	public Iterator<String> noms() {
		return this.lesStreams.keySet().iterator();
	}

	/**
	 * Retourne la liste des readers des ressources accessibles.
	 * 
	 * @return un iterator permettant de parcourir la liste des ressources sous
	 *         forme d'InputStream.
	 */
	public Iterator<Entry<String, IClasspathInputStream>> readers() {
		return this.lesStreams.entrySet().iterator();
	}

	/**
	 * Retourne l'objet demandé sous forme d'inputStream.
	 * 
	 * @throws ClasspathException
	 *             en cas de ressource non trouvée ou ne répondant pas au filtre
	 *             de recherche.
	 * 
	 */
	public InputStream getInputStream(String cle) throws ClasspathException {
		InputStream is = null;
		try {
			is = lesStreams.get(cle).getInputStream();
		} catch (IOException e) {
			throw new ClasspathException(
					"Impossible de récupérer un inputStream dans le classpath.",
					e);
		}

		return is;
	}


	@Override
	protected void chargeObjectFile(String cle, String fileName) {
		if (!this.lesNoms.contains(cle)) {
			this.lesNoms.add(cle);
			this.lesStreams.put(cle, new InputStreamFile(fileName));
		}
	}


	@Override
	protected void chargeObjectZip(String cle, String zipFileName,
			String zipEntryName) {
		if (!this.lesNoms.contains(cle)) {
			this.lesNoms.add(cle);
			this.lesStreams.put(cle, new InputStreamZip(zipFileName,
					zipEntryName));
		}
	}

}
