package fr.insee.omphale.utilitaireDuGroupeJava2010.exception;

/**
 * Exception soulevée en cas de problème de requete SQL.
 *
 */
public class GroupeJavaSQLException extends GroupeJavaDaoException {

	/**
	 * Numéro de version de cette classe.
	 */

	private static final long serialVersionUID = 8522257856860289701L;

	/**
	 * Constructeur simple.
	 *
	 * @param arg0
	 *            message lié à  cette exception
	 */
	public GroupeJavaSQLException(String arg0) {
		super(arg0);
	}

	/**
	 * Constructeur permettant le chaînage des exceptions.
	 *
	 * @param arg0
	 *            message lié à  cette exception
	 * @param arg1
	 *            exception ayant déclenché cette exception
	 */
	public GroupeJavaSQLException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
