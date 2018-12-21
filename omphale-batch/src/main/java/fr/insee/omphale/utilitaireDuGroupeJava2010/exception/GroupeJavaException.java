package fr.insee.omphale.utilitaireDuGroupeJava2010.exception;

/**
 * Exception générique soulevée par l'application WebRp. <br>
 * Toutes les exceptions spécifiques utilisées par l'application WebRp doivent
 * hériter de cette exception.
 *
 */
public class GroupeJavaException extends Exception {

	/**
	 * Numéro de version de cette classe.
	 */
	private static final long serialVersionUID = 4298596580314238844L;

	/**
	 * Constructeur simple.
	 *
	 * @param arg0
	 *            message lié à cette exception
	 */
	public GroupeJavaException(String arg0) {
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
	public GroupeJavaException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}
}
