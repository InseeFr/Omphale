package fr.insee.omphale.generationDuPDF.service.projection.impl;

import java.util.Map;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

/**
 * Ecriture du libellé "Flux au RP" dans le Pdf.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class ITextHFluxAuRP {

	/**
	 * Ecriture du libellé "Flux au RP".
	 * <br>
	 * @param document le document dans lequel écrire
	 * @param zoneId identifiant de la zone
	 * @param font HashMap qui contient les Font utilisées 
	 * @throws DocumentException
	 */
	public void setFluxAuRP(
			Document document,
			String zoneId,
			Map<String, Font> font) throws DocumentException {

		Font fontTitre = font.get("fontTitre");
		
		Paragraph paragraph = new Paragraph();
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		StringBuffer lienFluxAuRP = new StringBuffer();
		lienFluxAuRP.append("FluxAuRP");
		lienFluxAuRP.append(zoneId);
		paragraph.add(new Chunk("Flux au RP", fontTitre).setLocalDestination(lienFluxAuRP.toString()));
		paragraph.setSpacingAfter(30); 
		

			document.add(paragraph);

	}
}
