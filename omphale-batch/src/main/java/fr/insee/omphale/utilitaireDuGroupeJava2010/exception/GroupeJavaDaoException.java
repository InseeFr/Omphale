package fr.insee.omphale.utilitaireDuGroupeJava2010.exception;


/**
 * Exception générique soulevée par la couche DAO. <br>
 * Cette exception doit être utilisée dans la couche service afin de filtrer les
 * erreurs en provenance de la couche DAO.
 *
 */
public class GroupeJavaDaoException extends GroupeJavaException{

	/**
	 * Numéro de version de cette classe.
	 */
	private static final long serialVersionUID = 3646817228479268179L;

	/**
	 * Constructeur simple.
	 *
	 * @param arg0
	 *            message lié à  cette exception
	 */
	public GroupeJavaDaoException(String arg0) {
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
	public GroupeJavaDaoException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
