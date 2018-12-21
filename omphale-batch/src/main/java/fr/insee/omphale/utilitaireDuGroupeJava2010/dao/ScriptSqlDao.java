package fr.insee.omphale.utilitaireDuGroupeJava2010.dao;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.filescriptsql.ScriptSql;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.IConnectionManager;

/**
 * Cette classe permet de gèrer des scripts sql et/ou plsql et exécutés dans des
 * programmes java. Ces scripts sont au format sql*plus et peuvent donc être mis
 * au point sous cet outil avant d'être portés en base pour utilisation sous
 * java. Lors de la demande d'exécution, les parametres formels sont substitués
 * par les valeurs de ces parametres.
 * 
 */
public class ScriptSqlDao extends AbstractDao {


	
	public ScriptSqlDao(String nomPool, IConnectionManager connectionManager) {
		super(nomPool, connectionManager);
	}

	/**
	 * Execute une liste d'instructions sql ou pl/sql de manière
	 * transactionnelle. Le commit de la transaction n'a lieu qu'a la fin de la
	 * liste des opérations. En cas d'erreur, un rollback est effectué. <br>
	 * ATTENTION : Pour que le fonctionnement transactionnel de l'exécution soit
	 * correct , il est impératif d'éviter des ordres DDL entraînant un commit
	 * implicite par ORACLE. De même si un ordre commit est présent dans un des
	 * scripts, il sera exécuté.
	 * 
	 * @param scriptSql
	 *            la liste des instructions sql ou pl/sql à exécuter.
	 * @param listeParametres
	 *            la map des parametres formels à substituer das le sql/plsql
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur d'exécution.
	 */
	public void execute(List<ScriptSql> scriptSql,
			Map<String, String> listeParametres) throws GroupeJavaDaoException {
			for (ScriptSql sql : scriptSql) {
				if (sql.isSql()) {
					executeInstructionSql(sql, listeParametres);
				} else {
					executeInstructionPlSql(sql, listeParametres);
				}
			}
	}

	/**
	 * Execution une instruction de type SQL. Si l'instruction est de type
	 * SELECT, une exception est levée. Si l'intruction est :<br>
	 * <ul>
	 * <li>commit, la transaction ORACLE est validée,</li>
	 * <li>rollback, la trasaction ORACLE est annulée,</li>
	 * <li>drop, la non existence de l'objet n'entraine pas une erreur
	 * d'exécution.</li>
	 * </ul>
	 * 
	 * @param ordreSql
	 *            l'ordre SQL à exécuter.
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
			connection();
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
			close();
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
			connection();
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

}
