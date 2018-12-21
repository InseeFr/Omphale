package fr.insee.omphale.generationDuPDF.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ServiceResultat {

	
	/**
	 * méthode pour renvoyer la stack trace dans la log 4 j
	 * @param throwable
	 * @return String
	 */
	public static String getStackTrace(Throwable throwable) {
		    Writer writer = new StringWriter();
		    PrintWriter printWriter = new PrintWriter(writer);
		    throwable.printStackTrace(printWriter);
		    return writer.toString();
		    }
}
