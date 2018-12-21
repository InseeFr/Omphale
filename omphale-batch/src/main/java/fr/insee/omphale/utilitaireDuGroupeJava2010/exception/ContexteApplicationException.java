package fr.insee.omphale.utilitaireDuGroupeJava2010.exception;

public class ContexteApplicationException extends Exception {


	private static final long serialVersionUID = -5427776585302307992L;

	public ContexteApplicationException() {
		super();
	}

	public ContexteApplicationException(String message) {
		super(message);
	}

	public ContexteApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContexteApplicationException(Throwable cause) {
		super(cause);
	}
}