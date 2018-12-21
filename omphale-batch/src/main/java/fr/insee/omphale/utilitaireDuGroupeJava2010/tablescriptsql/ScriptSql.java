package fr.insee.omphale.utilitaireDuGroupeJava2010.tablescriptsql;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * Bean associé à un ordre SQL ou PL/SQL stocké sous forme d’opération de
 * script.
 *
 */
public abstract class ScriptSql {
	/**
	 * le nom de l'opération de script sql.
	 */
	private String operation;

	/**
	 * le type d'instruction SQL ou PLSQL.
	 */
	private String typeSql;

	/**
	 * le texte de l'instruction.
	 */
	protected String texteSql;

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getTexteSql() {
		return texteSql;
	}

	/**
	 * Setteur du texteSql. Cette methode est destinée à être surchargée par les
	 * classes dérivées pour notamment supprimer le ou les caractères de fin
	 * d'instruction :<br>
	 * <ul>
	 * <li>si typeSql = SQL, alors supprimer le caractère ";"</li>
	 * <li>si typeSql = PLSQL, alors supprimer le caractère "/"</li>
	 * </ul>
	 *
	 * @param texteSql
	 *            le texte sql
	 */
	protected abstract void setTexteSql(String texteSql);

	public String getTypeSql() {
		return typeSql;
	}

	public void setTypeSql(String typeSql) {
		this.typeSql = typeSql;
	}

	/**
	 * Substitue les parametres formels et renvoie l'instruction SQL (ou
	 * PL/SQL).
	 *
	 * @param listeParametres
	 *            la map des parametres formels à substituer.
	 * @return l'instruction après substitution des parametres formels.
	 */
	public String getOrdreSql(Map<String, String> listeParametres) {
		String instruction = getTexteSql();
		if (listeParametres != null) {
			// récupération de la liste des paramètres
			Set<String> listeCle = listeParametres.keySet();
			// pour chaque paramètre
			// remplacement du paramètre par sa valeur
			for (String cle : listeCle) {
				instruction = StringUtils.replace(instruction, cle,
						listeParametres.get(cle));
			}
		}
		return instruction;
	}

	/**
	 * Teste si l'instruction est de type SELECT.
	 *
	 * @return true si oui, false sinon.
	 */
	public boolean isSelect() {
		return getTexteSql().toUpperCase().startsWith("SELECT");
	}

	/**
	 * Teste si l'instruction est de type DROP.
	 *
	 * @return true si oui, false sinon.
	 */
	public boolean isDrop() {
		return getTexteSql().toUpperCase().startsWith("DROP");
	}

	/**
	 * Teste si l'instruction est de type COMMIT.
	 *
	 * @return true si oui, false sinon.
	 */
	public boolean isCommit() {
		return getTexteSql().toUpperCase().startsWith("COMMIT");
	}

	/**
	 * Teste si l'instruction est de type ROLLBACK.
	 *
	 * @return true si oui, false sinon.
	 */
	public boolean isRollback() {
		return getTexteSql().toUpperCase().startsWith("ROLLBACK");
	}

	/**
	 * Teste si l'instruction est de type SQL.
	 *
	 * @return true si oui, false sinon.
	 */
	public abstract boolean isSql();

	/**
	 * Vérifie si un String se termine par un caractère donné.
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
}
