package fr.insee.omphale.batch.cps;

public class CpsException extends Exception {


	private static final long serialVersionUID = -2858677122024487652L;

	public CpsException() {
		super();
	}

	public CpsException(String message, Throwable cause) {
		super(message, cause);
	}

	public CpsException(String message) {
		super(message);
	}

	public CpsException(Throwable cause) {
		super(cause);
	}

}
