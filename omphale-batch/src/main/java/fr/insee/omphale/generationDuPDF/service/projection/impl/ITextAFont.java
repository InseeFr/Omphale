package fr.insee.omphale.generationDuPDF.service.projection.impl;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.Font;

/**
 * Création des Font utilisées dans le Pdf.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService 
 */
public class ITextAFont {

	/**
	 * création des Font utilisées dans le Pdf
	 * @return HashMap qui contient les font créées
	 */
	public Map<String, Font> getFont() {
		
		HashMap<String, Font> font = new HashMap<String, Font>();
		
		String fontTimesNewRoman = "Times New Roman";
		
		Font fontTitre = new Font();
		fontTitre.setFamily(fontTimesNewRoman);
		fontTitre.setStyle(Font.BOLD);
		fontTitre.setSize(13); 
		font.put("fontTitre", fontTitre);
		
		Font fontSousTitre = new Font();
		fontSousTitre.setFamily(fontTimesNewRoman);
		fontSousTitre.setSize(13); 
		font.put("fontSousTitre", fontSousTitre);
		
		Font fontZone = new Font();
		fontZone.setFamily(fontTimesNewRoman);
		fontZone.setColor(Color.BLUE);
		fontZone.setStyle(Font.BOLD);
		fontZone.setSize(15); 
		font.put("fontZone", fontZone);
		
		Font fontTexte = new Font();
		fontTexte.setFamily(fontTimesNewRoman);
		fontTexte.setSize(9); 
		font.put("fontTexte", fontTexte);
		
		Font fontLink = new Font();
		fontLink.setFamily(fontTimesNewRoman);
		fontLink.setColor(Color.BLUE);
		fontLink.setStyle(Font.UNDERLINE);	
		fontLink.setSize(9); 
		font.put("fontLink", fontLink);
		
		Font fontEspace = new Font();
		fontEspace.setFamily(fontTimesNewRoman);
		fontEspace.setSize(2);
		
		Font fontBold = new Font();	
		fontBold.setFamily(fontTimesNewRoman); 
		fontBold.setStyle(Font.BOLD);
		fontBold.setSize(7); 
		font.put("fontBold", fontBold);
		
		Font fontTableauBold = new Font();	
		fontTableauBold.setFamily(fontTimesNewRoman); 
		fontTableauBold.setStyle(Font.BOLD);
		fontTableauBold.setSize(7);
		font.put("fontTableauBold", fontTableauBold);
		
		Font fontTableauBoldWhite = new Font();	
		fontTableauBoldWhite.setFamily(fontTimesNewRoman);
		fontTableauBoldWhite.setStyle(Font.BOLD);
		fontTableauBoldWhite.setSize(7);
		fontTableauBoldWhite.setColor(Color.white);
		font.put("fontTableauBoldWhite", fontTableauBoldWhite);
		
		Font fontTableau = new Font();	
		fontTableau.setFamily(fontTimesNewRoman); 
		fontTableau.setSize(7); 
		font.put("fontTableau", fontTableau);
		
		Font fontTableauSousTitre = new Font();	
		fontTableauSousTitre.setFamily(fontTimesNewRoman); 
		fontTableauSousTitre.setSize(6);
		font.put("fontTableauSousTitre", fontTableauSousTitre);
		
		Font fontTableauLink = new Font();
		fontTableauLink.setFamily(fontTimesNewRoman);
		fontTableauLink.setColor(Color.BLUE);
		fontTableauLink.setStyle("bold underline");	
		fontTableauLink.setSize(7); 
		font.put("fontTableauLink", fontTableauLink);
		
		Font fontTableauBoldItalique = new Font();
		fontTableauBoldItalique.setFamily(fontTimesNewRoman);
		fontTableauBoldItalique.setStyle(Font.BOLDITALIC);
		fontTableauBoldItalique.setSize(7);
		font.put("fontTableauBoldItalique", fontTableauBoldItalique);
		
		return font;
	}
}
