package fr.insee.omphale.batch.transversal.exception;

/**
 * Exception grave entraînant l'arrêt du service.
 *
 */
public class OmphaleConfigException extends Exception {

    
    private static final long serialVersionUID = -6849852339509566298L;

        
    public OmphaleConfigException() {
        super();
    }

    public OmphaleConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public OmphaleConfigException(String message) {
        super(message);
    }

    public OmphaleConfigException(Throwable cause) {
        super(cause);
    }

}
