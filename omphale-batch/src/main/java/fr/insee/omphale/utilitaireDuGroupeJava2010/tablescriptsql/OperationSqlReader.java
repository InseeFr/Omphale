package fr.insee.omphale.utilitaireDuGroupeJava2010.tablescriptsql;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

import org.apache.commons.lang.StringUtils;

/**
 * Classe de lecture d'un fichier de scripts sql annoté pour la prise en compte
 * du chargement dans une table de scripts. Le fichier sql est un fichier
 * compatible avec sql*plus avec les restrictions suivantes :<br>
 * <ul>
 * <li>les seuls commentaires autorisés sont ceux commençant par --</li>
 * <li>les instructions spécifiques sql*plus (SET, DEFINE, etc...) sont
 * interdites</li>
 * </ul>
 * Le fichier doit contenir les annotations suivantes :<br>
 * <ul>
 * <li>--OPERATION=<b><i>nomOperation</i></b>--</li>
 * <li>--TYPE_SQL=<b><i>typeSql</i></b>--</li>
 * </ul>
 * L'annotation TYPE_SQL est facultative, et dans ce cas, elle considérée être
 * égale à SQL.<br>
 * Il peut y avoir plusieurs annotations OPERATION et/ou TYPE_SQL dans un même
 * fichier de script sql.<br>
 * Le lecteur sql doit renvoyer une instruction de la forme suivante :<br>
 * <code>insert into <i>nomTable</i> values (<i>nomOperation</i>,<i>no_sql</i>,<i>no_ligne</i>,<i>type_sql</i>,<i>texte_sql</i>);</code>
 *
 */
public class OperationSqlReader implements IReaderScriptSql {

	/**
	 * le lecteur de fichiers texte
	 */
	private LineNumberReader lr;

	/**
	 * la ligne brute lue depuis le fichier.
	 */
	protected String ligneLue;

	/**
	 * le nom de la table de scripts SQL contenant les opérations;
	 */
	private String tableName;

	/**
	 * le nom de l'opération SQL courante;
	 */
	protected String operation;

	/**
	 * le type d'instruction SQL ou PLSQL courant.
	 */
	protected String typeSql;

	/**
	 * Le numéro d'instruction sql en cours de chargement.
	 */
	private int noSql;

	/**
	 * le numéro de ligne de l'instruction sql en cours de chargement.
	 */
	private int noLigne;

	/**
	 * Indication de l'exsitance d'une annotation OPERATION.
	 */
	protected boolean isOperationAnnotation;

	/**
	 * Cosntructeur.
	 *
	 * @param tableName
	 *            le nom de la table de scripts SQL.
	 * @param is
	 *            l'inputStream sur le fichier sql.
	 */
	public OperationSqlReader(String tableName, InputStream is) {
		super();

		this.tableName = tableName;
		isOperationAnnotation = false;
		lr = new LineNumberReader(new InputStreamReader(is));
	}

	/**
	 * Cosntructeur.
	 *
	 * @param tableName
	 *            le nom de la table de scripts SQL.
	 * @param lr
	 *            le lecteur de fichiers texte.
	 */
	public OperationSqlReader(String tableName, LineNumberReader lr) {
		super();

		this.tableName = tableName;
		isOperationAnnotation = false;
		this.lr = lr;
	}


	public String getInstructionSql() throws IOException {
		String sql = ligneLue;

		// s'il s'agit d'une annotation de type OPERATION...
		if (sql.startsWith("--OPERATION")) {
			// ...et qu'une annotation n'a pas été lue précédemment
			if (!isOperationAnnotation) {
				// ...alors traitement de l'annotation
				isOperationAnnotation = true;
				return traiteOperation(sql);
			} else {
				// ...si une annotation a été lue précédemment,
				// on réinitialise l'indicateur et on poursuit pour écrire
				// l'annotation brute dans le script
				isOperationAnnotation = false;
			}

		}
		// s'il s'agit d'une annotation de type TYPE_SQL...
		if (sql.startsWith("--TYPE_SQL")) {
			// ...traitement de l'annotation
			String[] str = StringUtils.split(sql, "-=");
			typeSql = str[1];
		}

		// suppression des espaces en début et fin de ligne
		sql = sql.trim();

		// recherche de la fin d'instruction selon le typeSql
		char finInstruction;
		if ("SQL".equals(typeSql)) {
			finInstruction = ';';
		} else {
			finInstruction = '/';
		}
		int pos = isSeTerminePar(sql, finInstruction);

		// incrémentation du numéro de ligne et construction de l'ordre INSERT
		// dans la table de script
		noLigne++;
		String instruction = "insert into  " + tableName + " values ('"
				+ operation + "'," + noSql + "," + noLigne + ",'" + typeSql
				+ "','" + normaliseQuotes(ligneLue) + "');";

		// Si une fin d'instruction a été trouvée, incrémentation du numéro
		// d'instruction
		if (pos != -1) {
			noSql++;
			noLigne = 0;
		}
		return instruction;
	}


	public boolean hasNext() {
		// Si on vient de trouver une annotation OPERATION,
		// alors, il reste des lignes à lire dans le fichier.
		if (isOperationAnnotation) {
			return true;
		}
		try {
			ligneLue = lr.readLine();
		} catch (IOException e) {
			ligneLue = null;
		}
		return ligneLue != null;
	}

	/**
	 * Remplace une simple quote par deux quotes pour pouvoir être utilisé dans
	 * une instruction insert.
	 *
	 * @param instruction
	 *            l'instruction sql.
	 * @return L'instruction modifiée.
	 */
	protected String normaliseQuotes(String instruction) {
		return StringUtils.replace(instruction, "'", "''");
	}

	/**
	 * Vérifie si une String se termine par un caractère donné.
	 *
	 * @param str
	 *            la chaine de caractères.
	 * @param c
	 *            le caractère à tester.
	 * @return true si le dernier caractère de str est égal à c, false sinon.
	 */
	protected int isSeTerminePar(String str, char c) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0)
			return -1;
		for (int i = strLen - 1; i >= 0; i--) {
			if (!Character.isWhitespace(str.charAt(i))) {
				if (str.charAt(i) == c) {
					return i;
				} else {
					return -1;
				}
			}
		}
		return -1;

	}

	/**
	 * Traite une annotation de type OPERATION. Stocke diverses informations
	 * utiles pour les traitements et génére un <br>
	 * <code>delete <i>nomTable</i> where operation='<i>nomOperation</i>';</code>
	 *
	 * @param ligne
	 *            la ligne à traiter
	 * @return le sql à stocker dans la table des scripts
	 */
	private String traiteOperation(String ligne) {
		String[] str = StringUtils.split(ligne, "-=");
		operation = str[1];
		// par défaut, on considère que le typeSql=SQL
		typeSql = "SQL";
		// Initialisation du numéro d'instruction
		noSql = 1;
		noLigne = 0;
		return "delete " + tableName + " where operation='" + operation + "';";
	}
}
