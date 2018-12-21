package fr.insee.omphale.batch.transversal.exception;

/**
 * Exception métier, n'interrompant pas le service.
 *
 */
public class OmphaleMetierException extends Exception{
    
    
 
        private static final long serialVersionUID = -7283763065512437037L;

        public OmphaleMetierException() {
            super();
        }

        public OmphaleMetierException(String message, Throwable cause) {
            super(message, cause);
        }

        public OmphaleMetierException(String message) {
            super(message);
        }

        public OmphaleMetierException(Throwable cause) {
            super(cause);
        }

    }
