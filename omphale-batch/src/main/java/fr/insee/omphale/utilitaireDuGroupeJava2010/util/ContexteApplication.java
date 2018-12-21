package fr.insee.omphale.utilitaireDuGroupeJava2010.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.MissingResourceException;

import fr.insee.config.InseeConfig;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.AbstractDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ConnectionManager;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.ContexteApplicationException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;

/**
 * Classe utilitaire responsable de la localisation d'un certain nombre d'objets
 * de l'application. Elle est implémentée sous forme de Singleton. <br>
 * <ul>
 * <li>Elle utilise le design pattern Service Locator pour localiser les
 * DataSources de l'application.</li>
 * <li>Elle joue le rôle de Factory pour les Services et les Daos utilisés par
 * l'application.</li>
 * </ul>
 * 
 */
public class ContexteApplication {

	private static final ThreadLocal<IConnectionManager> threadConnectionManager = new ThreadLocal<IConnectionManager>();

	/**
	 * L'unique instance de cette classe
	 */
	private static ContexteApplication context = null;

	/**
	 * Clé sous laquelle le nom du fichier donnant la définition des Services
	 * est définie.
	 */
	protected final static String SERVICE_DEFINITION_KEY = "service.";

	/**
	 * Clé sous laquelle le nom du fichier donnant la définition des Daos est
	 * définie.
	 */
	protected final static String DAO_DEFINITION_KEY = "dao.";

	/** Initialisation statique de l'instance. */
	private ContexteApplication() {
	}

	/**
	 * Récupère l'unique instance du singleton.
	 * 
	 * @return l'unique instance de cette classe.
	 */
	public static ContexteApplication getInstance() {
		if (context == null) {
			context = new ContexteApplication();
		}
		return context;
	}

	/**
	 * Accesseur statique à la factory des connexions aux bases de données.
	 * 
	 * @return la factory des connexions aux bases de données.
	 */
	public static IConnectionManager getConnectionManager() {
		IConnectionManager connexionManager = threadConnectionManager.get();
		if (connexionManager == null) {
			connexionManager = new ConnectionManager();
			threadConnectionManager.set(connexionManager);
		}
		return connexionManager;
	}

	/**
	 * Accesseur statique à la factory des Services de l'application.
	 * 
	 * @param interfaceService
	 *            nom de l'interface du Service souhaitée.
	 * @return une instanciation de l'interface souhaitée.
	 * @throws ContexteApplicationException
	 *             en cas de problème d'instanciation de l'interface du service
	 *             souhaitée
	 */
	public static Object getService(String interfaceService)
			throws ContexteApplicationException {
		return getServiceDefinition(interfaceService);
	}

	/**
	 * Factory des Daos de l'application. Recherche dans le fichier de
	 * configuration de l'application la propriété décrivant le dao et construit
	 * un object implémentant l'interface souhaitée. <br>
	 * Le Dao doit étendre la classe
	 * {@link fr.insee.omphale.utilitaireDuGroupeJava2010.dao.AbstractDao} et donc posséder un
	 * contructeur
	 * {@link fr.insee.omphale.utilitaireDuGroupeJava2010.dao.AbstractDao#AbstractDao(String, IConnectionManager)}
	 * .
	 * 
	 * @param interfaceDao
	 *            nom de l'interface du Dao souhaité
	 * @param nomDataSource
	 *            nom de la DataSource
	 * @return une instanciation de l'interface souhaitée.
	 * @see fr.insee.omphale.utilitaireDuGroupeJava2010.dao.AbstractDao
	 * @see fr.insee.omphale.utilitaireDuGroupeJava2010.util.IConnectionManager
	 * @throws GroupeJavaDaoException
	 *             en cas de pb lors de l'initialisation du dao souhaité.
	 * @throws ContexteApplicationException
	 *             en cas de pb d'instanciation de l'interface du dao souhaité.
	 */
	public static Object getDao(String interfaceDao, String nomDataSource)
			throws GroupeJavaDaoException, ContexteApplicationException {
		return getDaoDefinition(interfaceDao, nomDataSource);
	}

	/**
	 * Factory des Services de l'application. Recherche dans le fichier de
	 * configuration de l'application la propriété décrivant les services
	 * utilisés par l'application et construit un object implémentant
	 * l'interface souhaitée. <br>
	 * Le Service doit posséder un contructeur par défaut.
	 * 
	 * @param interfaceService
	 *            nom de l'interface du Service souhaitée.
	 * @return une instanciation de l'interface souhaitée.
	 * @throws ContexteApplicationException
	 *             en cas de pb d'instanciation de l'interface du Service
	 *             souhaitée
	 */
	private static Object getServiceDefinition(String interfaceService)
			throws ContexteApplicationException {
		String nomClasseService = null;
		try {
			nomClasseService = InseeConfig.getConfig().getString(
					SERVICE_DEFINITION_KEY + interfaceService);
			Class<?> cl = Class.forName(nomClasseService);
			return (Object) cl.newInstance();
		} catch (ClassNotFoundException e) {
			throw new ContexteApplicationException(
					"Impossible de trouver la classe java du service : "
							+ interfaceService, e);
		} catch (InstantiationException e) {
			throw new ContexteApplicationException(
					"Impossible d'instancier le service : " + interfaceService,
					e);
		} catch (IllegalAccessException e) {
			throw new ContexteApplicationException(
					"Accès illégal à la classe java du service : "
							+ interfaceService, e);
		}
	}

	/**
	 * Factory des Daos de l'application.
	 * 
	 * @param interfaceDao
	 *            nom de l'interface du Dao souhaité
	 * @param nomDataSource
	 *            nom de la DataSource
	 * @return une instanciation de l'interface souhaitée.
	 * @throws ContexteApplicationException
	 *             en cas de pb d'instanciation de l'interface du dao souhaitée
	 * @throws GroupeJavaDaoException
	 *             en cas de pb lors de l'initialisation du dao souhaité.
	 */
	private static AbstractDao getDaoDefinition(String interfaceDao,
			String nomDataSource) throws ContexteApplicationException,
			GroupeJavaDaoException {
		String nomClasseDao = null;
		AbstractDao daoObject;
		try {
			nomClasseDao = InseeConfig.getConfig().getString(
					DAO_DEFINITION_KEY + interfaceDao);
			Class<?> cl = Class.forName(nomClasseDao);
			// on récupère son constructeur en précisant le type des paramètres
			// qu'on va lui passer
			// (moyen de différencier les constructeurs)
			Constructor<?> constructeur = cl.getConstructor(new Class[] {
					String.class, IConnectionManager.class });
			// instanciation de la classe grâce au constructeur
			daoObject = (AbstractDao) constructeur.newInstance(new Object[] {
					nomDataSource, getConnectionManager() });
			daoObject.initDAO();

			return daoObject;
		} catch (MissingResourceException e) {
			throw new ContexteApplicationException(
					"Impossible de trouver le fichier " + nomClasseDao
							+ " donnant la définition des daos ", e);
		} catch (ClassNotFoundException e) {
			throw new ContexteApplicationException(
					"Impossible de trouver la classe java du dao : "
							+ interfaceDao, e);
		} catch (InstantiationException e) {
			throw new ContexteApplicationException(
					"Impossible d'instancier le dao : " + interfaceDao, e);
		} catch (IllegalAccessException e) {
			throw new ContexteApplicationException(
					"Accès illégal à la classe java du dao : " + interfaceDao,
					e);
		} catch (IllegalArgumentException e) {
			throw new ContexteApplicationException("Mauvais type de paramètre "
					+ nomDataSource + " pour la classe : " + interfaceDao, e);
		} catch (SecurityException e) {
			throw new ContexteApplicationException(
					"Impossible d'instancier la classe java du dao : "
							+ interfaceDao, e);
		} catch (NoSuchMethodException e) {
			throw new ContexteApplicationException(
					"Impossible d'instancier la classe java du dao : "
							+ interfaceDao, e);
		} catch (InvocationTargetException e) {
			throw new ContexteApplicationException(
					"Impossible d'instancier la classe java du dao : "
							+ interfaceDao, e);
		}
	}

}
