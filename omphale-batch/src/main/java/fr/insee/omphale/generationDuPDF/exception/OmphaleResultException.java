package fr.insee.omphale.generationDuPDF.exception;

public class OmphaleResultException extends Exception {

	
    private static final long serialVersionUID = 4789903114006787778L;

    public OmphaleResultException() {
        super();
    }

    public OmphaleResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public OmphaleResultException(String message) {
        super(message);
    }

    public OmphaleResultException(Throwable cause) {
        super(cause);
    }

    public OmphaleResultException(Exception exception) {
        super(exception);
    }
}
