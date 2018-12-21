package fr.insee.omphale.utilitaireDuGroupeJava2010.tablescriptsql;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.configuration.Configuration;

import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.ClasspathException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.ClasspathInspector;
import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.IClasspathInputStream;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.ContexteApplicationException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.ContexteApplication;

/**
 * Classe de gestion de la table de scripts sql.
 * 
 */
public class TableScriptSqlManager {

	public static final String PREFIX_CRE_TABLE = "CRE_TABLE_";

	public static final String PREFIX_CRE_VUE = "CRE_VUE_";

	public static final String PREFIX_CRE_INDEX = "CRE_INDEX_";

	/**
	 * L'interface d'accès à la table de type ScriptSql.
	 */
	protected ITableScriptSqlDao scriptSql;

	/**
	 * Le nom de la dataSource d'accès à la table de type ScriptSql.
	 */
	protected String nomDataSource;

	/**
	 * Constructeur, Vérifie que la table de type ScriptSql existe, sinon la
	 * table est créée automatiquement.
	 * 
	 * @param nomTableScript
	 *            le nom de la table de type ScriptSql.
	 * @param nomDataSource
	 *            le nom de la dataSource d'accès à la table de type ScriptSql.
	 * @throws GroupeJavaException
	 */
	public TableScriptSqlManager(String nomTableScript, String nomDataSource)
			throws GroupeJavaException {
		// Vérification de l'existence de la table SCRIPT_SQL

		try {
			this.nomDataSource = nomDataSource;
			scriptSql = (ITableScriptSqlDao) ContexteApplication.getDao("scriptSql",
					this.nomDataSource);
			scriptSql.setNomTableScript(nomTableScript);
			if (!scriptSql.existe()) {
				scriptSql.creTable();
			}
		} catch (ContexteApplicationException e) {
			throw new GroupeJavaException(
					"Impossible d'instancier le dao d'accès à la table de type scriptSql.",
					e);
		}
	}

	/**
	 * Supprime la table de type ScriptSql.
	 * 
	 * @throws GroupeJavaException
	 *             en cas d'impossibilité de supprimer la table scriptSql.
	 */
	public void close() throws GroupeJavaException {
		try {
			scriptSql.dropTable();
		} catch (GroupeJavaDaoException e) {
			throw new GroupeJavaException(
					"Impossible de fermer le dao d'accès à la table de type scriptSql.",
					e);
		}
	}

	/**
	 * Vérifie que la liste d'operations passée en paramètre est présente dans
	 * la table de type ScriptSql. Si une opération de la liste, n'existe pas
	 * dans la table, elle est recherchée dans les ressources du projet sous le
	 * package nomPackageResourceSql, puis elle est chargée automatiquement.
	 * 
	 * @param nomPackageResourceSql
	 *            le nom du package de ressources où se trouvent les opérations.
	 * @param listOperations
	 *            La liste d'opérations à vérifier.
	 * @throws GroupeJavaException
	 *             en cas d'erreur
	 */
	public void verifExistenceOperationsSql(String nomPackageResourceSql,
			List<String> listOperations) throws GroupeJavaException {
		List<String> listOperationsSql;
		// Recherche des opérations présentes dans la table ScriptSql
		listOperationsSql = scriptSql.getListOperations("");

		for (String operationSql : listOperations) {
			// Recherche du fichier de ressources contenant les ordres sql de
			// l'opération manquante
			if (!listOperationsSql.contains(operationSql)) {
				String nomScript = nomPackageResourceSql + "/" + operationSql
						+ ".sql";
				InputStream is = getClass().getResourceAsStream(nomScript);
				OperationSqlReader osr = new OperationSqlReader(scriptSql
						.getNomTableScript(), is);
				scriptSql.creOperation(osr);
			} 
		}

	}

	/**
	 * Vérifie que la liste d'operations trouvée dans les ressources du projet
	 * sous le package nomPackageResourceSql est présente dans la table de type
	 * ScriptSql. Si une opération de la liste, n'existe pas dans la table, elle
	 * est chargée automatiquement.
	 * 
	 * @param nomPackageResourceSql
	 *            le nom du package de ressources où se trouvent les opérations.
	 * @throws GroupeJavaException
	 *             en cas d'erreur
	 */
	public void verifExistenceOperationsSql(String nomPackageResourceSql)
			throws GroupeJavaException {
		Iterator<Entry<String, IClasspathInputStream>> s;
		try {
			s = new ClasspathInspector(nomPackageResourceSql, "*.sql")
					.readers();
			// Recherche des opérations présentes dans la table ScriptSql
			List<String> listOperationsSql = scriptSql.getListOperations("");
			while (s.hasNext()) {
				Entry<String, IClasspathInputStream> plugin = (Entry<String, IClasspathInputStream>) s
						.next();
				String operationSql = plugin.getKey();
				// Recherche du fichier de ressources contenant les ordres sql de
				// l'opération manquante
				if (!listOperationsSql.contains(operationSql)) {
					InputStream is = (InputStream) plugin.getValue();
					OperationSqlReader osr = new OperationSqlReader(scriptSql
							.getNomTableScript(), is);
					scriptSql.creOperation(osr);
				} 
			}
		} catch (ClasspathException e) {
			throw new GroupeJavaException(
					"Erreur rencontrée lors d'une recherche dans le classpath.",
					e);
		}
	}

	/**
	 * Vérifie que l'operation passée en paramètre est présente dans la table
	 * de type ScriptSql. Si l'opération, n'existe pas dans la table, elle est
	 * recherchée dans les ressources du projet sous le package
	 * nomPackageResourceSql, puis elle est chargée automatiquement.
	 * 
	 * @param nomPackageResourceSql
	 *            le nom du package de ressources où se trouvent les opérations.
	 * @param operationSql
	 *            L'opération à vérifier.
	 * @throws GroupeJavaException
	 *             en cas d'erreur
	 */
	public void verifUneOperationSql(String nomPackageResourceSql,
			String operationSql) throws GroupeJavaException {
		
		List<String> listOperationsSql;

		try {
			// Recherche des opérations présentes dans la table ScriptSql
			listOperationsSql = scriptSql.getListOperations("");

			// Recherche du fichier de ressources contenant les ordres sql de
			// l'opération manquante
			ClasspathInspector clInspector = new ClasspathInspector(
					nomPackageResourceSql, operationSql + ".sql");

			if (!listOperationsSql.contains(operationSql)) {
				InputStream is = (InputStream) clInspector
						.getInputStream(operationSql + ".sql");
				if (is == null) {
					throw new GroupeJavaException(
							"Impossible de trouver le fichier sql suivant : "
									+ operationSql);
				}
				OperationSqlReader osr = new OperationSqlReader(scriptSql
						.getNomTableScript(), is);
				scriptSql.creOperation(osr);
			} 
		} catch (GroupeJavaDaoException e) {
			throw new GroupeJavaException(
					"Une erreur s'est produite lors de l'accès à la base de données.");
		} catch (ClasspathException e) {
			throw new GroupeJavaException(
					"Erreur rencontrée lors d'une recherche dans le classpath.");
		}
	}

	/**
	 * Execute l'opération sql passée en paramètre, toutes les valeurs du
	 * fichier de configuration sont utilisées comme paramètres symboliques de
	 * l'opération. La syntaxe du parametre symbolique dans l'operation sql est
	 * conforme à la syntaxe plSql, c'est à dire <code>&&parametre.</code>
	 * 
	 * @param operationSql
	 *            l'opération sql à exécuter.
	 * @param cfg
	 *            le fichier de configuration servant à la substitution des
	 *            parametres symboliques.
	 * @throws GroupeJavaException
	 *             en cas d'erreur.
	 */
	@SuppressWarnings("unchecked")
	public void executeOperationSql(String operationSql, Configuration cfg)
			throws GroupeJavaException {
		Map<String, String> listeParametres = new HashMap<String, String>();
		if (cfg != null) {
			Iterator<String> iter = cfg.getKeys();
			while (iter.hasNext()) {
				String cle = iter.next();
				String param = "&&" + cle + ".";
				listeParametres.put(param, cfg.getString(cle));
			}
		}
		executeOperationSql(operationSql, listeParametres);
	}

	/**
	 * Execute l'opération sql passée en paramètre, toutes les valeurs de la
	 * mapParametres sont utilisées comme paramètres symboliques de l'opération.
	 * La syntaxe du parametre symbolique dans l'operation sql est conforme à la
	 * syntaxe plSql, c'est à dire <code>&&parametre.</code> *
	 * 
	 * @param operationSql
	 *            l'opération sql à exécuter.
	 * @param mapParametres
	 *            la map servant à la substitution des parametres symboliques.
	 * @throws GroupeJavaException
	 *             en cas d'erreur.
	 */
	public void executeOperationSql(String operationSql,
			Map<String, String> mapParametres) throws GroupeJavaException {
		scriptSql.execute(operationSql, mapParametres);
	}

	/**
	 * Création d'une séquence oracle si elle n'existe pas.
	 * 
	 * @param nomSequence
	 *            le nom de la séquence Oracle
	 * @throws GroupeJavaException
	 *             en cas d'erreur.
	 */
	public void creSequenceOracleSiNonExiste(String nomSequence)
			throws GroupeJavaException {
		ISimpleSelectDao simpleSelectDao = null;
		try {
			simpleSelectDao = (ISimpleSelectDao) ContexteApplication.getDao(
					"simpleSelectDao", this.nomDataSource);
			String sql = "select * from  user_sequences where sequence_name = '"
					+ nomSequence + "'";
			simpleSelectDao.execute(sql);
			if (!simpleSelectDao.nextRow()) {
				// création de la sequence
				sql = "CREATE SEQUENCE " + nomSequence
						+ " MINVALUE 1 MAXVALUE 999999 "
						+ "INCREMENT BY 1 START WITH 1 NOORDER NOCYCLE";
				ISimpleDao simpleDao = (ISimpleDao) ContexteApplication.getDao(
						"simpleDao", this.nomDataSource);
				simpleDao.executeUpdate(sql);
			}
		} catch (ContexteApplicationException e) {
			throw new GroupeJavaException(
					"Impossible d'instancier le dao ISimpleDao.", e);
		} finally {
			if (simpleSelectDao != null) {
				simpleSelectDao.close();
			}
		}
	}

	/**
	 * Création d'une table oracle si elle n'existe pas. Si elle n'existe pas,
	 * elle est créée à l'aide d'une opération sql préalablement chargée dans la
	 * table ScriptSql. Le nom de cette opération est, par convention,
	 * <code>CRE_TABLE_nomTable</code>
	 * 
	 * @param nomTable
	 *            le nom de la table Oracle
	 * @throws GroupeJavaException
	 *             en cas d'erreur.
	 */
	public void creTableOracleSiNonExiste(String nomTable)
			throws GroupeJavaException {
		ISimpleSelectDao simpleSelectDao = null;
		try {
			simpleSelectDao = (ISimpleSelectDao) ContexteApplication.getDao(
					"simpleDao", this.nomDataSource);

			String sql = "select * from  user_tables where table_name = '"
					+ nomTable + "'";

			simpleSelectDao.execute(sql);

			if (!simpleSelectDao.nextRow()) {
				scriptSql.execute(PREFIX_CRE_TABLE + nomTable, null);
			}
		} catch (ContexteApplicationException e) {
			throw new GroupeJavaException(
					"Impossible d'instancier le dao ISimpleDao.", e);
		} finally {
			if (simpleSelectDao != null) {
				simpleSelectDao.close();
			}
		}
	}

	/**
	 * Création d'une vue oracle si elle n'existe pas. Si elle n'existe pas,
	 * elle est créée à l'aide d'une opération sql préalablement chargée dans la
	 * table ScriptSql. Le nom de cette opération est, par convention,
	 * <code>CRE_VUE_nomVue</code>
	 * 
	 * @param nomVue
	 *            le nom de la vue Oracle
	 * @throws GroupeJavaException
	 *             en cas d'erreur.
	 */
	public void creVueOracleSiNonExiste(String nomVue)
			throws GroupeJavaException {
		ISimpleSelectDao simpleSelectDao = null;
		try {
			simpleSelectDao = (ISimpleSelectDao) ContexteApplication.getDao(
					"simpleDao", this.nomDataSource);
			String sql = "select * from  user_views where VIEW_NAME = '"
					+ nomVue + "'";

			simpleSelectDao.execute(sql);
			if (!simpleSelectDao.nextRow()) {
				scriptSql.execute(PREFIX_CRE_VUE + nomVue, null);
			} 
		} catch (ContexteApplicationException e) {
			throw new GroupeJavaException(
					"Impossible d'instancier le dao ISimpleDao.", e);
		} finally {
			if (simpleSelectDao != null) {
				simpleSelectDao.close();
			}
		}
	}

	/**
	 * Création d'un index oracle s'il n'existe pas. S'il n'existe pas, il est
	 * créé à l'aide d'une opération sql préalablement chargée dans la table
	 * ScriptSql. Le nom de cette opération est, par convention,
	 * <code>CRE_INDEX_nomIndex</code>
	 * 
	 * @param nomIndex
	 *            le nom de l'index Oracle
	 * @throws GroupeJavaException
	 *             en cas d'erreur.
	 */
	public void creIndexOracleSiNonExiste(String nomIndex)
			throws GroupeJavaException {
		ISimpleSelectDao simpleSelectDao = null;
		try {
			simpleSelectDao = (ISimpleSelectDao) ContexteApplication.getDao(
					"simpleDao", this.nomDataSource);
			String sql = "select * from  user_indexes where INDEX_NAME = '"
					+ nomIndex + "'";

			simpleSelectDao.execute(sql);
			if (!simpleSelectDao.nextRow()) {
				scriptSql.execute(PREFIX_CRE_INDEX + nomIndex, null);
			} 
		} catch (ContexteApplicationException e) {
			throw new GroupeJavaException(
					"Impossible d'instancier le dao ISimpleDao.", e);
		} finally {
			if (simpleSelectDao != null) {
				simpleSelectDao.close();
			}
		}
	}
}
