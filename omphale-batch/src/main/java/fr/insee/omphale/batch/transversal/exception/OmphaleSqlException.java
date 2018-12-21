package fr.insee.omphale.batch.transversal.exception;

/**
 * Exception métier soulevée par un script n'entraînant pas l'interruption
 * du service. 
 *
 */
public class OmphaleSqlException extends Exception {

    private static final long serialVersionUID = 4789903114006787778L;

    public OmphaleSqlException() {
        super();
    }

    public OmphaleSqlException(String message, Throwable cause) {
        super(message, cause);
    }

    public OmphaleSqlException(String message) {
        super(message);
    }

    public OmphaleSqlException(Throwable cause) {
        super(cause);
    }

}
