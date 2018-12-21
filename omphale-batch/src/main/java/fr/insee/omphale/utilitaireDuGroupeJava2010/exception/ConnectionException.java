package fr.insee.omphale.utilitaireDuGroupeJava2010.exception;

public class ConnectionException extends Exception {

	private static final long serialVersionUID = -8129340748490992338L;

	public ConnectionException() {
		super();
	}

	public ConnectionException(String message) {
		super(message);
	}

	public ConnectionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConnectionException(Throwable cause) {
		super(cause);
	}

}
