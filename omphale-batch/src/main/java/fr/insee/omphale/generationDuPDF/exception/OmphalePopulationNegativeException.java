package fr.insee.omphale.generationDuPDF.exception;

public class OmphalePopulationNegativeException extends Exception{


	private static final long serialVersionUID = -2197930807470031135L;
	
	public OmphalePopulationNegativeException() {
		super();
	}
	
	public OmphalePopulationNegativeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OmphalePopulationNegativeException(String message) {
        super(message);
    }

    public OmphalePopulationNegativeException(Throwable cause) {
        super(cause);
    }

    public OmphalePopulationNegativeException(Exception exception) {
        super(exception);
    }

	
}
