package fr.insee.omphale.generationDuPDF.exception;


public class OmphaleConfigResultException extends Exception {


    private static final long serialVersionUID = -6849852339509566298L;

        
    public OmphaleConfigResultException() {
        super();
    }

    public OmphaleConfigResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public OmphaleConfigResultException(String message) {
        super(message);
    }

    public OmphaleConfigResultException(Throwable cause) {
        super(cause);
    }

    public OmphaleConfigResultException(String message, Exception e) {
        super(message, e);
    }
}
