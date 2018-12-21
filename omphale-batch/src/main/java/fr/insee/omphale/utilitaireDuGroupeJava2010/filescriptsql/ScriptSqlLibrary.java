package fr.insee.omphale.utilitaireDuGroupeJava2010.filescriptsql;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.ClasspathException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.ClasspathInspector;
import fr.insee.omphale.utilitaireDuGroupeJava2010.classpath.IClasspathInputStream;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ISimpleSelectDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.ScriptSqlDao;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.ContexteApplicationException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.ContexteApplication;

public class ScriptSqlLibrary {

	private String nomPackageResourceSql;
	private ScriptSqlDao scriptSqlDao;
	private INomsScriptsDecorator nomsScriptsDecorator;

	private Map<String, List<ScriptSql>> lesScriptsSql = new HashMap<String, List<ScriptSql>>();

	public ScriptSqlLibrary(String nomPackageResourceSql,
			ScriptSqlDao scriptSqlDao) throws ClasspathException, GroupeJavaException {
		super();
		this.nomsScriptsDecorator = new StandardDecorator();
		this.nomPackageResourceSql = nomPackageResourceSql;
		this.scriptSqlDao = scriptSqlDao;
		loadPackageSql();
	}

	public void setNomsScriptsDecorator(
			INomsScriptsDecorator nomsScriptsDecorator) {
		this.nomsScriptsDecorator = nomsScriptsDecorator;
	}

	public void execute(String unScript, Map<String, String> listeParametres)
			throws GroupeJavaDaoException {
		List<String> lesScripts = new ArrayList<String>();
		lesScripts.add(unScript);
		execute(lesScripts, listeParametres);
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
	 * @param lesScripts
	 *            le liste des scripts sql/pl/sql à exécuter.
	 * @param listeParametres
	 *            la map des parametres formels à substituer dans le sql/plsql
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur d'exécution.
	 */
	public void execute(List<String> lesScripts,
			Map<String, String> listeParametres) throws GroupeJavaDaoException {
		// pour chaque element de la liste lesScripts
		// constituer une liste d'objets ScriptSql
		// passer cette liste et la liste des parametres au ScriptSqlDao
		List<ScriptSql> lesInstructionsSql = new ArrayList<ScriptSql>();
		for (String nomScript : lesScripts) {
			List<ScriptSql> op = lesScriptsSql.get(nomScript);
			if (op != null) {
				lesInstructionsSql.addAll(lesScriptsSql.get(nomScript));
			}
		}
		scriptSqlDao.execute(lesInstructionsSql, listeParametres);

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
					"simpleSelectDao", scriptSqlDao.getNomPool());
			String sql = "select * from  user_sequences where sequence_name = '"
					+ nomSequence + "'";
			simpleSelectDao.execute(sql);
			if (!simpleSelectDao.nextRow()) {
				execute(nomsScriptsDecorator.getNomScriptSequence(nomSequence),
						null);
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
					"simpleDao", scriptSqlDao.getNomPool());
			String sql = "select * from  user_indexes where INDEX_NAME = '"
					+ nomIndex + "'";

			simpleSelectDao.execute(sql);
			if (!simpleSelectDao.nextRow()) {
				execute(nomsScriptsDecorator.getNomScriptIndex(nomIndex), null);
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
					"simpleDao", scriptSqlDao.getNomPool());

			String sql = "select * from  user_tables where table_name = '"
					+ nomTable + "'";

			simpleSelectDao.execute(sql);

			if (!simpleSelectDao.nextRow()) {
				execute(nomsScriptsDecorator.getNomScriptIndex(nomTable), null);
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
					"simpleDao", scriptSqlDao.getNomPool());
			String sql = "select * from  user_views where VIEW_NAME = '"
					+ nomVue + "'";

			simpleSelectDao.execute(sql);
			if (!simpleSelectDao.nextRow()) {
				execute(nomsScriptsDecorator.getNomScriptIndex(nomVue), null);
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

	public Set<String> getScriptNames() {
		return lesScriptsSql.keySet();
	}


	public boolean existsUnScript(String script) {
	    return lesScriptsSql.containsKey(script);
	}

	private void loadPackageSql() throws ClasspathException,
			GroupeJavaException {
		ClasspathInspector clInspector;
		clInspector = new ClasspathInspector(nomPackageResourceSql, "*.sql");
		Iterator<Entry<String, IClasspathInputStream>> lesStreamSql = clInspector
				.readers();
		while (lesStreamSql.hasNext()) {
			Map.Entry<String, IClasspathInputStream> entry = (Map.Entry<String, IClasspathInputStream>) lesStreamSql
					.next();
			try {
				String nomScript = entry.getKey();
				InputStream is = entry.getValue().getInputStream();
				List<ScriptSql> script = loadUnStreamSql(is);
				lesScriptsSql.put(nomScript, script);
			} catch (IOException e) {
				throw new GroupeJavaException(
						"Erreur lors de la lecture du script " + entry.getKey(), e);
			}
		}

	}

	private List<ScriptSql> loadUnStreamSql(InputStream is) throws IOException {
		List<ScriptSql> lesScripts = new ArrayList<ScriptSql>();
		LineNumberReader lr = new LineNumberReader(new InputStreamReader(is));
		String typeSql = "SQL";
		String ligneLue = lr.readLine();
		StringBuffer ordreSql = new StringBuffer();
		while (ligneLue != null) {
			String sql = ligneLue;

			// s'il s'agit d'une annotation de type TYPE_SQL...
			if (sql.startsWith("--TYPE_SQL")) {
				// ...traitement de l'annotation
				String[] str = StringUtils.split(sql, "-=");
				typeSql = str[1];
			}
			// suppression des commentaires
			ordreSql.append(gereCommentaires(sql));
			// recherche de la fin d'instruction selon le typeSql
			boolean finInstruction = isSeTerminePar(sql, typeSql);
			if (finInstruction) {
				ScriptSql scriptSql;
				if ("SQL".equals(typeSql)) {
					scriptSql = new OrdreSql();
				} else {
					scriptSql = new OrdrePlSql();
				}
				scriptSql.setTexteSql(ordreSql.toString());
				ordreSql = new StringBuffer();
				lesScripts.add(scriptSql);
			}
			ligneLue = lr.readLine();
		}
		return lesScripts;
	}

	private String gereCommentaires(String str) {
		if (str != null) {
			String chaine = str.trim();
			if (!chaine.toUpperCase().startsWith("REM")
					&& !chaine.startsWith("//") && !chaine.startsWith("--")) {
				return " " + str;
			}
		}
		return "";
	}

	/**
	 * Vérifie si une String se termine par un caractère donné.
	 * 
	 * @param str
	 *            la chaine de caractères.
	 * @param typeSql
	 *            le type d'ordre sql (SQL ou PLSQL).
	 * @return true si le dernier caractère de str est égal à c, false sinon.
	 */
	private boolean isSeTerminePar(String str, String typeSql) {
		char charFin;
		if ("SQL".equals(typeSql)) {
			charFin = ';';
		} else {
			charFin = '/';
		}
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return false;
		for (int i = strLen - 1; i >= 0; i--) {
			if (!Character.isWhitespace(str.charAt(i))) {
				if (str.charAt(i) == charFin) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;

	}
}
