package fr.insee.omphale.utilitaireDuGroupeJava2010.exception;

/**
 * Exception soulevée en cas de problème d'accès au pool de connexions.
 *
 */
public class GroupeJavaPoolException extends GroupeJavaDaoException {

	/**
	 * Numéro de version de cette classe.
	 */
	private static final long serialVersionUID = 377908775799911244L;

	/**
	 * Constructeur simple.
	 *
	 * @param arg0
	 *            message lié à  cette exception
	 */
	public GroupeJavaPoolException(String arg0) {
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
	public GroupeJavaPoolException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
