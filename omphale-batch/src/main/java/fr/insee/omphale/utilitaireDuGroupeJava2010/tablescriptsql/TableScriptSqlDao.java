package fr.insee.omphale.utilitaireDuGroupeJava2010.tablescriptsql;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.AbstractDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.IConnectionManager;

/**
 * Cette classe permet de gèrer des scripts sql et/ou plsql stockés dans une
 * table et exécutés dans des programmes java. Ces scripts sont au format
 * sql*plus et peuvent donc être mis au point sous cet outil avant d'être portés
 * en base pour utilisation sous java. Lors de la demande d'exécution, les
 * parametres formels sont substitués par les valeurs de ces parametres.
 * 
 */
public class TableScriptSqlDao extends AbstractDao implements ITableScriptSqlDao {

	/**
	 * Le nom de la table de type script_sql.
	 */
	private String nomTableScript;

	/**
	 * Constructeur par défaut. Dans ce cas, la table de script sql a pour nom
	 * SCRIPT_SQL.
	 * 
	 * @param nomPool
	 *            nom du pool de connexions ou de la dataSource.
	 * @param connectionManager
	 *            l'objet gérant les connexions à la base de données.
	 */
	public TableScriptSqlDao(String nomPool, IConnectionManager connectionManager) {
		super(nomPool, connectionManager);
		this.nomTableScript = "SCRIPT_SQL";
	}

	/**
	 * Constructeur permettant de spécifier un nom à la table de script sql.
	 * 
	 * @param nomTableScript
	 *            le nom de la table de script sql.
	 * @param nomPool
	 *            nom du pool de connexions ou de la dataSource.
	 * @param connectionManager
	 *            l'objet gérant les connexions à la base de données.
	 */
	public TableScriptSqlDao(String nomTableScript, String nomPool,
			IConnectionManager connectionManager) {
		super(nomPool, connectionManager);
		this.nomTableScript = nomTableScript.toUpperCase(Locale.FRANCE);
	}


	public void execute(String uneOperation, Map<String, String> listeParametres)
			throws GroupeJavaDaoException {
		List<String> lesOperations = new ArrayList<String>();
		lesOperations.add(uneOperation);
		execute(lesOperations, listeParametres);
	}

	/**
	 * Execute une liste d'opérations sql/plsql de manière transactionnelle. Le
	 * commit de la transaction n'a lieu qu'a la fin de la liste des opérations.
	 * En cas d'erreur, un rollback est effectué. <br>
	 * ATTENTION : Pour que le fonctionnement transactionnel de l'exécution soit
	 * correct , il est impératif d'éviter des ordres DDL entrainant un commit
	 * implicite par ORACLE. De même si un ordre commit est présent dans un des
	 * scripts, il sera exécuté.
	 * 
	 * @param lesOperations
	 *            le liste des opérations sql/pl/sql à exécuter.
	 * @param listeParametres
	 *            la map des parametres formels à substituer das le sql/plsql
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur d'exécution.
	 */
	public void execute(List<String> lesOperations,
			Map<String, String> listeParametres) throws GroupeJavaDaoException {
		try {
			for (String nomOperation : lesOperations) {
				List<ScriptSql> scriptSql;

				scriptSql = loadOperation(nomOperation);

				for (ScriptSql sql : scriptSql) {
					if (sql.isSql()) {
						executeInstructionSql(sql, listeParametres);
					} else {
						executeInstructionPlSql(sql, listeParametres);
					}

				}
			}
			commitTransaction();
		} catch (GroupeJavaDaoException e) {
			rollbackTransaction();
			throw e;
		} finally {
			close();
		}

	}

	/**
	 * Crée une opération dans la table des scripts à partir d'un fichier
	 * externe.
	 * 
	 * @param readerSql
	 *            le lecteur de scripts sql.
	 * @throws GroupeJavaDaoException
	 *             en cas d'errreur de lecture du fichier.
	 */
	public void creOperation(IReaderScriptSql readerSql)
			throws GroupeJavaDaoException {
		try {
			connection();
			s = c.createStatement();
			while (readerSql.hasNext()) {
				String sql = (String) readerSql.getInstructionSql();
				executeUpdate(sql);
			}
		} catch (IOException e) {
			final String message = "IOException dans creOperation.";
			throw new GroupeJavaDaoException(message, e);
		} catch (SQLException e) {
			final String message = "SQLException dans creOperation.";
			throw new GroupeJavaDaoException(message, e);
		} finally {
			close();
		}
	}

	/**
	 * Valide une transaction.
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur de validation de transaction.
	 */
	private void commitTransaction() throws GroupeJavaDaoException {
		try {
			c.commit();
		} catch (SQLException e) {
			final String message = "impossible de valider la transaction.";
			throw new GroupeJavaDaoException(message, e);
		} finally {
			try {
				if (s != null) {
					s.close();
					s = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Annule une transaction.
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur d'annulation de transaction.
	 */
	private void rollbackTransaction() throws GroupeJavaDaoException {
		try {
			c.rollback();
		} catch (SQLException e) {
			final String message = "impossible d'annuler la transaction.";
			throw new GroupeJavaDaoException(message, e);
		} finally {
			try {
				if (s != null) {
					s.close();
					s = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Execution une instruction de type SQL. Si l'instruction est de type
	 * SELECT, une exception est levée. Si l'intruction est :<br>
	 * <ul>
	 * <li>commit, la transaction ORACLE est validée,</li>
	 * <li>rollback, la transaction ORACLE est annulée,</li>
	 * <li>drop, la non existence de l'objet n'entraine pas une erreur
	 * d'exécution.</li>
	 * </ul>
	 * 
	 * @param ordreSql
	 *            l'ordre SQL a exécuter.
	 * @param listeParametres
	 *            la map des parametres formels à substituer avant exécution.
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur d'exécution.
	 */
	protected void executeInstructionSql(ScriptSql ordreSql,
			Map<String, String> listeParametres) throws GroupeJavaDaoException {
		// si select on émet une exception
		if (ordreSql.isSelect()) {
			final String message = "pas d'exécution de select";
			throw new GroupeJavaDaoException(message);
		}

		String instruction = ordreSql.getOrdreSql(listeParametres);
		if ("".equals(instruction)) {
			return;
		}
		try {
			// si commit ou rollback, appel direct sur l'objet connection
			if (ordreSql.isCommit()) {
				c.commit();
			} else if (ordreSql.isRollback()) {
				c.rollback();
			} else {
				s = c.createStatement();
				if (s.execute(instruction) == false) {
					// Get the update count
					@SuppressWarnings("unused")
					int num = s.getUpdateCount();
				}
			}
		} catch (SQLException e) {
			// si drop on ne traite pas les erreurs
			if (!ordreSql.isDrop()) {
				final String message = "erreur lors de l'exécution de l'instruction "
						+ instruction;
				throw new GroupeJavaDaoException(message, e);
			}
		} finally {
			try {
				if (s != null) {
					s.close();
					s = null;
				}
			} catch (SQLException e) {
					e.printStackTrace();
			}
		}
	}

	/**
	 * Execution une instruction de type PL-SQL.
	 * 
	 * @param ordreSql
	 *            l'ordre PL-SQL a exécuter.
	 * @param listeParametres
	 *            la map des parametres formels à substituer avant exécution.
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur d'exécution.
	 */
	protected void executeInstructionPlSql(ScriptSql ordreSql,
			Map<String, String> listeParametres) throws GroupeJavaDaoException {
		String instruction = ordreSql.getOrdreSql(listeParametres);
		try {
			CallableStatement call = c.prepareCall(instruction);
			call.execute();
		} catch (SQLException e) {
			final String message = "erreur lors de l'exécution de l'instruction "
					+ instruction;
			throw new GroupeJavaDaoException(message, e);
		} finally {
			close();
		}
	}

	/**
	 * Charge une opération de script depuis la table de scriptsSql.
	 * 
	 * @param nomOperation
	 *            le nom de l'opération à charger.
	 * @return la liste des ordres SQL ou PL-SQL de l'opération.
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur d'accès à la table ou d'absence d'ordres
	 *             dans l'opération demandée.
	 */
	private List<ScriptSql> loadOperation(String nomOperation)
			throws GroupeJavaDaoException {
		List<ScriptSql> scriptSql = new ArrayList<ScriptSql>();
		StringBuffer sb = new StringBuffer();
		boolean nonFin;

		// constitution de la requête sélection de toutes les instructions avec
		// leur numéro d'ordre sql et leur numéro de ligne correspondant à
		// l'opération
		sb.append("select NO_SQL,NO_LIGNE,TYPE_SQL,TEXTE_SQL from ");
		sb.append(getNomTableScript());
		sb.append(" where OPERATION='");
		sb.append(nomOperation);
		sb.append("' order by NO_SQL,NO_LIGNE");

		try {
			connection();

			Statement operationSql = c.createStatement();
			// exécution de la requête
			r = operationSql.executeQuery(sb.toString());
			// récupération de la liste des opérations
			if (r.next()) {
				nonFin = true;
			} else {
				final String message = "Pas de code sql à exécuter pour l'opération "
						+ nomOperation;
				throw new GroupeJavaDaoException(message);

			}
			while (nonFin) {
				StringBuffer ordreSql = new StringBuffer();
				String typeSql = r.getString("TYPE_SQL");
				long numeroOrdreSql = r.getLong("NO_SQL");
				while (nonFin && numeroOrdreSql == r.getLong("NO_SQL")) {
					// traitement de la partie d'instruction courante
					String str = r.getString("TEXTE_SQL");
					if (str != null) {

						String chaine = str.trim();
						// si la partie d'instruction n'est pas un commentaire,
						// on
						// la mémorise
						if (!chaine.toUpperCase().startsWith("REM")
								&& !chaine.startsWith("//")
								&& !chaine.startsWith("--")) {
							ordreSql.append(" ");
							ordreSql.append(str);
						}
					}
					nonFin = r.next();
				}
				ScriptSql dadsuScriptSql = null;
				if ("SQL".equalsIgnoreCase(typeSql)) {
					dadsuScriptSql = new OrdreSql();
				} else if ("PLSQL".equalsIgnoreCase(typeSql)) {
					dadsuScriptSql = new OrdrePlSql();
				}

				dadsuScriptSql.setOperation(nomOperation);
				dadsuScriptSql.setTypeSql(typeSql);
				dadsuScriptSql.setTexteSql(ordreSql.toString());
				scriptSql.add(dadsuScriptSql);
			}

		} catch (SQLException e) {
			final String message = "erreur dans la récupération des instructions associées à l'ordre "
					+ nomOperation;
			throw new GroupeJavaDaoException(message, e);
		}
		return scriptSql;
	}

	public String getNomTableScript() {
		return nomTableScript;
	}

	public void creTable() throws GroupeJavaDaoException {
		StringBuffer sql = new StringBuffer();
		sql.append("CREATE TABLE ");
		sql.append(nomTableScript);
		sql.append(" (OPERATION VARCHAR2(64 BYTE) NOT NULL ENABLE,");
		sql.append(" NO_SQL NUMBER NOT NULL ENABLE,");
		sql.append(" NO_LIGNE NUMBER NOT NULL ENABLE,");
		sql.append(" TYPE_SQL VARCHAR2(5 BYTE),");
		sql.append(" TEXTE_SQL VARCHAR2(1024 BYTE),");
		sql.append(" CONSTRAINT ");
		sql.append(nomTableScript);
		sql.append("1_PK PRIMARY KEY (OPERATION, NO_SQL, NO_LIGNE))");

		try {
			connection();
			executeUpdate(sql.toString());

		} catch (GroupeJavaDaoException e) {
			final String message = "erreur dans la création de la table "
					+ nomTableScript + ".";
			throw new GroupeJavaDaoException(message, e);
		} finally {
			close();
		}
	}

	public void dropTable() throws GroupeJavaDaoException {
		StringBuffer sql = new StringBuffer();
		sql.append("drop table  ");
		sql.append(nomTableScript);
		try {
			connection();
			executeUpdate(sql.toString());
		} catch (GroupeJavaDaoException e) {
			final String message = "erreur dans la destruction de la table "
					+ nomTableScript + ".";
			throw new GroupeJavaDaoException(message, e);
		} finally {
			close();
		}
	}

	public boolean existe() throws GroupeJavaDaoException {
		StringBuffer sql = new StringBuffer();
		sql.append("select 1 nb from user_tables where table_name='");
		sql.append(nomTableScript);
		sql.append("'");

		try {
			connection();
			s = c.createStatement();
			// exécution de la requête
			r = s.executeQuery(sql.toString());
			if (r.next()) {
				return true;
			}
		} catch (SQLException e) {
			final String message = "erreur dans la recherche de la table "
					+ nomTableScript + ".";
			throw new GroupeJavaDaoException(message, e);
		} finally {
			close();
		}
		return false;
	}

	public List<String> getListOperations(String clauseWhere)
			throws GroupeJavaDaoException {
		List<String> listeOperations = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct operation from ");
		sql.append(nomTableScript);
		sql.append(" ");
		sql.append(clauseWhere);

		try {
			connection();
			s = c.createStatement();
			// exécution de la requête
			r = s.executeQuery(sql.toString());
			// récupération de la liste des opérations

			while (r.next()) {
				String str = r.getString("OPERATION");
				str = str.trim();
				listeOperations.add(str);
			}

		} catch (SQLException e) {
			final String message = "erreur dans getListOperations.";
			throw new GroupeJavaDaoException(message, e);
		} finally {
			close();
		}

		return listeOperations;
	}

	public void reinit() throws GroupeJavaDaoException {
		StringBuffer sql = new StringBuffer();
		sql.append("delete ");
		sql.append(nomTableScript);

		try {
			connection();
			executeUpdate(sql.toString());
		} catch (GroupeJavaDaoException e) {
			final String message = "erreur dans la réinitialisation de la table "
					+ nomTableScript + ".";
			throw new GroupeJavaDaoException(message, e);

		} finally {
			close();
		}
	}

	public void setNomTableScript(String nomTableScript) {
		this.nomTableScript = nomTableScript;

	}
}
