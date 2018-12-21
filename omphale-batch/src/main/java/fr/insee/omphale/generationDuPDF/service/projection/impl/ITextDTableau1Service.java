package fr.insee.omphale.generationDuPDF.service.projection.impl;


import java.awt.Color;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * Ecriture du tableau Population, Indicateurs, Age moyen dans le Pdf pour une zone donnée.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class ITextDTableau1Service {
	
	
	/**
	 * Ecriture du tableau Population, Indicateurs, Age moyen dans le Pdf.
	 * @param document le document dans lequel écrire
	 * @param font HashMap qui contient les Font utilisées 
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
	 * @param zoneId identifiant de la zone donnée
	 * @param hashMap contient :
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- les années de début (ex. 2006), de début + 5 (ex. 2011), de fin -5 (ex. 2026), de fin (ex. 2031)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- des libellés "Flux observés au RP", "au RP"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- le libellé du zonage
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- un indicateur si le zonage est un zonage utilisateur 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- le libellé de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- la date d'exécution de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- un indicateur si la projection est calée ou non
	 * @param populationAnneeDebut données tableau Population, Indicateurs, Age moyen
	 * @param populationAnneeFin données tableau Population, Indicateurs, Age moyen
	 * @param populationEvol données tableau Population, Indicateurs, Age moyen
	 * @param eDV données tableau Population, Indicateurs, Age moyen
	 * @param iCF données tableau Population, Indicateurs, Age moyen
	 * @param ageMoyenAnneeDebut données tableau Population, Indicateurs, Age moyen
	 * @param ageMoyenAnneeFin données tableau Population, Indicateurs, Age moyen
	 * @throws Exception
	 */
	public void setTableau1(
			Document document,			
			Map<String, Font> font,
			Map<String, String> zoneLibelle,
			String zoneId,
			Map<String, String> hashMap,
			Map<String, Integer> populationAnneeDebut,
			Map<String, Integer> populationAnneeFin,
			Map<String, Double> populationEvol,
			Map<String, HashMap<Integer, Double>> eDV,
			Map<String, Double> iCF,
			Map<String, Double> ageMoyenAnneeDebut,
			Map<String, Double> ageMoyenAnneeFin
				) throws Exception {
		

			Font fontTableauBold = font.get("fontTableauBold");
			Font fontTableauBoldWhite = font.get("fontTableauBoldWhite");
			Font fontTableau = font.get("fontTableau");
            
            NumberFormat numberFormatPopulation = NumberFormat.getInstance(java.util.Locale.FRENCH);
            
            NumberFormat numberFormatPopulationEvol = NumberFormat.getPercentInstance(java.util.Locale.FRENCH);
            numberFormatPopulationEvol.setMaximumFractionDigits(1);
            numberFormatPopulationEvol.setMinimumFractionDigits(1);
            
            NumberFormat numberFormatEDV = NumberFormat.getInstance(java.util.Locale.FRENCH);
            numberFormatEDV.setMaximumFractionDigits(1);
            numberFormatEDV.setMinimumFractionDigits(1);
            
            NumberFormat numberFormatICF = NumberFormat.getInstance(java.util.Locale.FRENCH);
            numberFormatICF.setMaximumFractionDigits(1);
            numberFormatICF.setMinimumFractionDigits(1);
            
            NumberFormat numberFormatAgeMoyen = NumberFormat.getInstance(java.util.Locale.FRENCH);
            numberFormatAgeMoyen.setMaximumFractionDigits(1);
            numberFormatAgeMoyen.setMinimumFractionDigits(1);

			
			/* tableau Population, Indicateurs, Age moyen */ 
			PdfPTable table1 = new PdfPTable(8); 
			table1.setWidthPercentage(50);
			int[] l = {16, 16, 13, 11, 11, 11, 11, 11};
			table1.setWidths(l);	
			table1.setSpacingBefore(20);
			table1.setSpacingAfter(20); 
			table1.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
			
			float padding = 2;
			
			/* ligne 1 */ 			
			PdfPCell cell = new PdfPCell(new Phrase(new Chunk("Population", fontTableauBoldWhite))); cell.setPadding(padding);
			cell.setColspan(3);
			cell.setBackgroundColor(Color.GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table1.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("Indicateurs", fontTableauBoldWhite))); cell.setPadding(padding);
			cell.setColspan(3);
			cell.setBackgroundColor(Color.GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table1.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("Age moyen", fontTableauBoldWhite))); cell.setPadding(padding);
			cell.setColspan(2);
			cell.setBackgroundColor(Color.GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table1.addCell(cell);
			
			/* ligne 2 */ 
			cell = new PdfPCell(new Phrase(new Chunk(hashMap.get("anneeDebut"), fontTableauBold))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table1.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk(hashMap.get("anneeFin"), fontTableauBold))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE);  cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table1.addCell(cell);
	
			cell = new PdfPCell(new Phrase(new Chunk("Evol", fontTableauBold))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			table1.addCell(cell);
			
			PdfPTable table2 = new PdfPTable(2);
			cell = new PdfPCell(new Phrase(new Chunk("EDV", fontTableauBold))); cell.setPadding(padding);
			cell.setColspan(2);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
			table2.addCell(cell);
			cell = new PdfPCell(new Phrase(new Chunk("H", fontTableauBold))); cell.setPadding(padding);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
			table2.addCell(cell);
			cell = new PdfPCell(new Phrase(new Chunk("F", fontTableauBold))); cell.setPadding(padding);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
			table2.addCell(cell);
			
			cell = new PdfPCell(table2);
			cell.setColspan(2);
			table1.addCell(cell);
			
			cell = new PdfPCell(new Phrase(new Chunk("ICF", fontTableauBold))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
			table1.addCell(cell);
			
			table2 = new PdfPTable(2);
			cell = new PdfPCell(new Phrase(new Chunk("Age moyen", fontTableauBold))); cell.setPadding(padding);
			cell.setColspan(2);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
			table2.addCell(cell);
			cell = new PdfPCell(new Phrase(new Chunk(hashMap.get("anneeDebut"), fontTableauBold))); cell.setPadding(padding);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
			table2.addCell(cell);
			cell = new PdfPCell(new Phrase(new Chunk(hashMap.get("anneeFin"), fontTableauBold))); cell.setPadding(padding);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
			table2.addCell(cell);
			
			cell = new PdfPCell(table2);
			cell.setColspan(2);
			table1.addCell(cell);
			
			/* ligne 3 */
			// population annee début
						
			cell = new PdfPCell(new Phrase(new Chunk(numberFormatPopulation.format(populationAnneeDebut.get(zoneId)), fontTableau))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT); 
			table1.addCell(cell);
			
			// population annee fin
					
			cell = new PdfPCell(new Phrase(new Chunk(numberFormatPopulation.format(populationAnneeFin.get(zoneId)).toString(), fontTableau))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT); 
			table1.addCell(cell);
			
			// population évol
			cell = new PdfPCell(new Phrase(new Chunk(numberFormatPopulationEvol.format(populationEvol.get(zoneId)).toString(), fontTableau))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT); 
			table1.addCell(cell);
			
			// eDV
			cell = new PdfPCell(new Phrase(new Chunk(numberFormatEDV.format(eDV.get(zoneId).get(0)).toString(), fontTableau))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT); 
			table1.addCell(cell);
			
			// eDV
			cell = new PdfPCell(new Phrase(new Chunk(numberFormatEDV.format(eDV.get(zoneId).get(1)).toString(), fontTableau))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT); 
			table1.addCell(cell);
			
			// ICF
			cell = new PdfPCell(new Phrase(new Chunk(numberFormatICF.format(iCF.get(zoneId)).toString(), fontTableau))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT); 
			table1.addCell(cell);
			
			// âge moyen année 1
			cell = new PdfPCell(new Phrase(new Chunk(numberFormatAgeMoyen.format(ageMoyenAnneeDebut.get(zoneId)).toString(), fontTableau))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT); 
			table1.addCell(cell);
			
			// âge moyen année fin
			cell = new PdfPCell(new Phrase(new Chunk(numberFormatAgeMoyen.format(ageMoyenAnneeFin.get(zoneId)).toString(), fontTableau))); cell.setPadding(padding);
			cell.setVerticalAlignment(PdfPCell.ALIGN_RIGHT);
			cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT); 
			table1.addCell(cell);

			document.add(table1);
			/**********************************************/ 
	}
}
