package fr.insee.omphale.generationDuPDF.service.projection.impl;

import java.util.Map;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

/**
 * Ecriture du libellé de la zone et du libellé "Expertise générale" dans le Pdf.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class ITextCExpertiseGenerale {
	
	/**
	 * Ecriture du libellé de la zone et du libellé "Expertise générale".
	 * <br>
	 * @param document le document dans lequel écrire
	 * @param zoneId identifiant de la zone
	 * @param zoneLibelle libellés des zones
	 * 		<br>     
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		Ex. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		zoneLibelle.get("75") --&gt. "Paris"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		etc.
	 * @param font HashMap qui contient les Font utilisées 
	 * @throws DocumentException
	 */
	public void setNomZoneExpertiseGenerale(
			Document document,
			String zoneId,
			Map<String, String> zoneLibelle,
			Map<String, Font> font) throws DocumentException {
		
		Font fontZone = font.get("fontZone");
		Font fontTitre = font.get("fontTitre");
		Font fontTexte = font.get("fontTexte");
		
			
			// zone
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Paragraph.ALIGN_CENTER);    
			Phrase phrase = new Phrase();
			StringBuffer lieni = new StringBuffer();
			lieni.append("lien");
			lieni.append(zoneId);
			phrase.add(new Chunk((String) zoneLibelle.get(zoneId), fontZone).setLocalDestination(lieni.toString()));
			paragraph.add(phrase);
			document.add(paragraph);
				
			paragraph = new Paragraph();
			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
			paragraph.add(new Chunk("Expertise générale", fontTitre));
			document.add(paragraph);
			
			paragraph = new Paragraph();		
			paragraph.add(new Chunk("", fontTexte));
			document.add(paragraph);	

	}
}
