package fr.insee.omphale.generationDuPDF.service.projection.impl;

import java.util.Map;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

/**
 * Ecriture du libellé "Expertise des flux" dans le Pdf.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class ITextFExpertiseFlux {

	/**
	 * Ecriture du libellé "Expertise générale".
	 * <br>
	 * @param document le document dans lequel écrire
	 * @param zoneId identifiant de la zone
	 * @param font HashMap qui contient les Font utilisées 
	 * @throws DocumentException
	 */
	public void setExpertiseFlux(
			Document document,
			String zoneId,
			Map<String, Font> font) throws DocumentException {
		
		Font fontTitre = font.get("fontTitre");
		
		Paragraph paragraph = new Paragraph();
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		StringBuffer lienExpertiseFlux = new StringBuffer();
		lienExpertiseFlux.append("ExpertiseFlux");
		lienExpertiseFlux.append(zoneId);
		paragraph.add(new Chunk("Expertise des migrations internes", fontTitre).setLocalDestination(lienExpertiseFlux.toString()));
		paragraph.setSpacingAfter(15); 

			document.add(paragraph);

	}
}
