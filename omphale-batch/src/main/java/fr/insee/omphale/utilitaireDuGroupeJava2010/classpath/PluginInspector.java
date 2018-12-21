package fr.insee.omphale.utilitaireDuGroupeJava2010.classpath;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Cette classe permet de dresser une liste des "plugins" disponibles au sein
 * d'une application JAVA.
 *
 * Ce module s'inspire d'un article d'André Sébastien sur
 * http://java.developpez.com/sources/?page=divers#plugins. Pour plus
 * d'informations, reportez vous sur ce site.
 *
 * L'idée de cet inspecteur est d'imposer un package pour les plugins et
 * d'utiliser la propriété systeme "java.class.path" est utilisée afin de
 * determiner toutes les classes. Il est ainsi possible de rajouter par la suite
 * un fichier JAR contenant de nouveaux plugins et qu'ils soient automatiquement
 * pris en compte par l'application (pour peu que le fichier JAR soit rajouté
 * correctement au classpath). <br>
 * Pour identifier les plugins disponibles, le "PluginInspector" a besoin :<br>
 * <ul>
 * <li> du package de recherche (ainsi que tous les sous-packages)</li>
 * <li> de la classe mère des plugins afin de s'assurer qu'une classe est
 * effectivement un plugin</li>
 * </ul>
 *
 * Il est possible d'avoir plusieurs "PluginInspector" pour gérer differentes
 * natures de plugins
 *
 * Exemple : Si les plugins sont installés dans le package
 * fr.insee.monAppli.plugins et que la classe mère des plugins est
 * fr.insee.monAppli.util.Plugin
 *
 * Récuperation des plugins de l'application :<br>
 * <code>
 * Iterator plugins = new
 * PluginInspector("fr.insee.monAppli.plugins",fr.insee.monAppli.util.Plugin.class).plugins();<br>
 * while( plugins.hasNext() ) {<br>
 *    Class plugin = (Class)plugins.next();<br>
 *    System.out.println("Plugin chargé : " + plugin); <br>
 * }<code>
 *
 * Remarque : le PluginInspector charge les classes lors de l'examen, ce qui
 * n'est pas un mauvais choix pour un mécanisme de plugins, mais ce mécanisme
 * pourait être assez pénalisant pour un usage detourné
 *
 */
public class PluginInspector extends AbstractInspector {

	/** Classe mere de tous les plugins * */
	private Class<?> classMerePlugin = null;

	/** Liste des plugins * */
	private List<Class<?>> plugins = new ArrayList<Class<?>>();

	/**
	 * Constructeur.
	 *
	 * @param packageName
	 *            Nom du package parent dont doivent faire partie les plugins.
	 * @param classMerePlugin
	 *            Classe mère de tous les plugins, si null alors, il sera
	 *            considéré Object.class comme classe mère de tous les plugins.
	 * @throws ClasspathException
	 *             En cas d'erreur lors de la recherche.
	 */
	public PluginInspector(String packageName, Class<?> classMerePlugin)
			throws ClasspathException {
		super(packageName, "*.class");
		/** Initialisation * */
		this.classMerePlugin = classMerePlugin;
		if (this.classMerePlugin == null)
			this.classMerePlugin = Object.class;

		/** Lance l'examen de l'application * */
		examineFiles();
	}

	/**
	 * Retourne la liste des plugins accessibles (objets "Class").
	 *
	 * @return un iterator permettant de parcourir la liste des plugins.
	 */
	public Iterator<?> plugins() {
		return this.plugins.iterator();
	}

	/**
	 * Retourne la liste des plugins accessibles (objets "Class") et dont le
	 * nom simple de la classe ou de l'interface souhaité est donné en
	 * parametre.
	 *
	 * @param simpleClassName
	 *            nom simple de l'interface ou de la classe.
	 * @return un iterator permettant de parcourir la liste des plugins.
	 */
	public Iterator<?> plugins(String simpleClassName) {
		if (simpleClassName == null) {
			return plugins();
		}
		List<Class<?>> lesPlugins = new ArrayList<Class<?>>();
		for (Class<?> unPlugin : this.plugins) {
			boolean execPlugin = false;
			Class<?>[] lesInterfaces = unPlugin.getInterfaces();
			for (int i = 0; i < lesInterfaces.length; i++) {
				if (lesInterfaces[i].getSimpleName().equals(simpleClassName)) {
					execPlugin = true;
					break;
				}
			}
			if (!execPlugin) {
				if (unPlugin.getSimpleName().equals(simpleClassName)) {
					execPlugin = true;
				}
			}
			if (execPlugin) {
				lesPlugins.add(unPlugin);
			}
		}

		return lesPlugins.iterator();
	}

	/**
	 * Charge la classe trouvée. Vérifie si la classe n'est pas une interface et
	 * si elle a comme ancêtre la classe classMerePlugin.
	 *
	 * @param className
	 *            Le nom de la classe à charger.
	 */
	@SuppressWarnings( { "static-access", "rawtypes" })
	private boolean chargePlugin(String className) {
		Class plugin = null;

		try {
			plugin = this.getClass().forName(className, false,
					this.getClass().getClassLoader());
		} catch (ClassNotFoundException e) {
			// Impossible par construction : la classe vient d'etre trouvée.
			throw new RuntimeException("Classe " + className + " non trouvée.",
					e);
		}

		// Dans certains cas particuliers, plugin est null
		if (plugin == null) {
			return false;
		}

		// Le plugin n'est pas une implementation
		if (plugin.isInterface()) {
			return false;
		}

		// Le plugin n'a pas classMerePlugin comme ancêtre
		if (!this.classMerePlugin.isAssignableFrom(plugin))
			return false;

		// Le plugin est une classe abstraite
		int i = plugin.getModifiers();
		if (Modifier.isAbstract(i)) {
			return false;
		}

		// Plugin en double
		if (this.plugins.contains(plugin)) {
			throw new RuntimeException("Classe " + className + " déjà chargée.");
		}
		this.plugins.add(plugin);
		return true;
	}

	
	@Override
	protected void chargeObjectFile(String cle, String fileName) {
		/** suppression du .class et remise sous forme de package * */
		String className = getPackageParent() + "." + cle;
		if (className.endsWith(".class")) {
			className = className.substring(0, className.length() - 6);
		}
		chargePlugin(className);

	}


	@Override
	protected void chargeObjectZip(String cle, String jarFileName,
			String jarEntryName) {
		/** suppression du .class et remise sous forme de package * */
		String className = jarEntryName.substring(0, jarEntryName.length() - 6);
		className = className.replace('/', '.');
		chargePlugin(className);
	}
	

}
