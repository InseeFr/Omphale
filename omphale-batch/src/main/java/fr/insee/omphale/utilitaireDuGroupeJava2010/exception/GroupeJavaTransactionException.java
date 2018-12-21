package fr.insee.omphale.utilitaireDuGroupeJava2010.exception;

public class GroupeJavaTransactionException  extends Exception {

	private static final long serialVersionUID = -4721801266845832186L;

	public GroupeJavaTransactionException() {
		super();
	}

	public GroupeJavaTransactionException(String message) {
		super(message);
	}

	public GroupeJavaTransactionException(String message, Throwable cause) {
		super(message, cause);
	}

	public GroupeJavaTransactionException(Throwable cause) {
		super(cause);
	}
}
