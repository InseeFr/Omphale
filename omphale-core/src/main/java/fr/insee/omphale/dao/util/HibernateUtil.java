package fr.insee.omphale.dao.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

/**
 * Classe pour gèrer l'initialisation de la session Hibernate
 *
 */
public final class HibernateUtil {
	private static Configuration configuration;
	private static SessionFactory sessionFactory;
	private static String fichierConfig;

	private HibernateUtil() {
	}

	public static synchronized void init() {
		// Create the initial SessionFactory from the default configuration
		// files
		try {
			// Read hibernate.properties, if present
			configuration = new Configuration();

			if (fichierConfig != null) {
				configuration.configure(fichierConfig);
			} else {
				configuration.configure("hibernate/hibernateCoreEtWeb.cfg.xml");
			}

			rebuildSessionFactory(configuration);
		} catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static void setFichierConfigHibernate(String fichConf) {
		if (!fichConf.equals(fichierConfig)) {
			fichierConfig = fichConf;
			if (configuration != null) {
				configuration = null;
				init();
			}
		}
	}

	/**************** GESTION DE LA SESSION FACTORY *****************************/

	/**
	 * Rebuild the SessionFactory with the given Hibernate Configuration.
	 * <p>
	 * HibernateUtil does not configure() the given Configuration object, it
	 * directly calls buildSessionFactory(). This method also closes the old
	 * static variable SessionFactory before, if it is still open.
	 * 
	 * @param cfg
	 */
	private static void rebuildSessionFactory(Configuration cfg) {
		
		if (sessionFactory != null && !sessionFactory.isClosed()) {
			sessionFactory.close();
		}
		sessionFactory = cfg.buildSessionFactory();
		configuration = cfg;
	}

	/**
	 * Renvoie la SessionFactory d'Hibernate
	 * 
	 * @return SessionFactory
	 */
	public static synchronized SessionFactory getSessionFactory() {

		if (sessionFactory == null) {
			init();
		}
		return sessionFactory;
	}

	/**
	 * Returns the Hibernate configuration that was used to build the
	 * SessionFactory.
	 * 
	 * @return Configuration
	 */
	public static Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Run the schema creation script.
	 * 
	 * @param script
	 *            print the DDL to the console
	 * @param export
	 *            export the script to the database
	 */

	public static void exportSchema(boolean script, boolean export) {
		if (configuration == null) {
			init();
		}
		SchemaExport se = new SchemaExport(configuration);
		se.create(script, export);
	}

	/**
	 * Closes the current SessionFactory and releases all resources.
	 * <p>
	 * The only other method that can be called on HibernateUtil after this one
	 * is rebuildSessionFactory(Configuration).
	 */
	public static void shutdown() {

		// Close caches and connection pools
		getSessionFactory().close();
		// Clear static variables
		sessionFactory = null;
	}

}
