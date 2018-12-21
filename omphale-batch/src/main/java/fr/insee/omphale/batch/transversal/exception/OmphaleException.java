package fr.insee.omphale.batch.transversal.exception;

public class OmphaleException extends Exception {

	
	/**
	 * Exception suite au lancement du batch par le Lanceur
	 *
	 */
	private static final long serialVersionUID = 1L;

	public OmphaleException() {
        super();
    }

    public OmphaleException(String message, Throwable cause) {
        super(message, cause);
    }

    public OmphaleException(String message) {
        super(message);
    }

    public OmphaleException(Throwable cause) {
        super(cause);
    }
}