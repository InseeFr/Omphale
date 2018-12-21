package fr.insee.omphale.generationDuPDF.service.projection.impl;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.util.MiseEnForme;

/**
 * Ecriture des graphiques flux dans le Pdf pour une zone donnée.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class ITextIGraphiques2Service {
	
	
	/**
	 * @param beanParametresResultat {@link fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
	 * @param pdfWriter le writer associé au document
	 * @param compressionLevel le taux de compression
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
	 * @param zonesEtudeZoneEch pour chaque zone d'étude, les 5 zones d'échange les plus importantes
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		Ex. : 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		zone_etude 75 	
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		List liste = zonesEtudeZoneEch.get("75"); // 5 zones d'échange les plus importantes
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(0) --&gt. "92";
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(1) --&gt. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(2) --&gt. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(3) --&gt. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(4) --&gt. 
	 * 		<br>
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		zone_etude 92 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste = zonesEtudeZoneEch.get("92"); ; // 5 zones d'échange les plus importantes
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(0) --&gt. "75"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(1) --&gt. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(2) --&gt. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(3) --&gt. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste.get(4) --&gt. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		etc.
	 * @param zoneId identifiant de la zone
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
	 * @param donneesSoldeFlux données graphique Solde migratoire au RP
	 * @param donneesFluxSortants données graphiques flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		flux sortants
	 * @param donneesFluxEntr données graphiques flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		flux entrants
	 * @param donneesFluxRange données graphiques flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		range flux sortants, flux entrants
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		permet aux graphiques de flux qui sont en regard d'avoir le même range.
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		Ex. le graphique Flux de Ain vers Rhône est gradué de 0 à 750
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		et le graphique Flux de Rhône vers Ain est gradué aussi de 0 à 750
	 * @throws Exception
	 */
	public void setGraphiques2(
			BeanParametresResultat beanParametresResultat,
			PdfWriter pdfWriter, 
			int compressionLevel,
			Document document,			
			Map<String, Font>  font,
			Map<String, String> zoneLibelle,
			Map<String, List<String>> zonesEtudeZoneEch,
			String zoneId,
			// graphiques
			Map<String, String> hashMap,
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesSoldeFlux,
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxSortants,
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxEntr,
			Map<String, HashMap<String, Double>> donneesFluxRange
				) throws Exception {
		
		Font fontBold = font.get("fontBold");
		
		PdfPTable table = new PdfPTable(2); 
        table.setWidthPercentage(100);
        
        float myWidth = (document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / 2;
		
        // titre
        StringBuffer titreGraphique = new StringBuffer();
        titreGraphique.append("                       Solde migratoire entre ");
        MiseEnForme miseEnForme = new MiseEnForme();
        titreGraphique.append(miseEnForme.anneePrecedente(hashMap.get("anneeDebut")));
        titreGraphique.append(" et ");
        titreGraphique.append(hashMap.get("anneeDebut"));
        titreGraphique.append(" (hors échanges avec l'étranger)");
       
        com.lowagie.text.Image image20 = null;
        // graphique Solde migratoire au RP
		if (donneesSoldeFlux != null){
	        image20 = 
				fluxSoldeImage(
						beanParametresResultat,
						pdfWriter, 
						titreGraphique.toString(), 
						donneesSoldeFlux.get(zoneId), 
						compressionLevel);		

			image20.scalePercent(myWidth/ image20.getWidth() * 100);
			
			PdfPCell cell20 = new PdfPCell(image20);
			cell20.setBorder(PdfPCell.NO_BORDER); 
			cell20.setColspan(2);
			cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell20);
		}

		

		
		document.add(table);
		
		// Flux avec les principales zones d'échange
		StringBuffer titre = new StringBuffer();
		titre.append("Flux avec les principales zones d'échange (");
		titre.append(hashMap.get("auRP"));
		titre.append(" ");
		titre.append(hashMap.get("anneeDebut"));
		titre.append(")");
		Paragraph paragraph = new Paragraph();		
		paragraph.add(new Chunk(titre.toString(), fontBold)); 
		paragraph.add(new Chunk(" ", fontBold));
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		paragraph.setSpacingBefore(10);
		paragraph.setSpacingAfter(10);
		document.add(paragraph); 

		
		if (zonesEtudeZoneEch != null && !zonesEtudeZoneEch.isEmpty() 
				&& zonesEtudeZoneEch.get(zoneId) != null && !zonesEtudeZoneEch.isEmpty()){
			table = new PdfPTable(2); 
	        table.setWidthPercentage(100);
	        
	        
			// au moins 1 zone d'échange
			if (zonesEtudeZoneEch.get(zoneId).size() >= 1) {
				
				
	        // titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(0)));
				
			
			// graphique 
			com.lowagie.text.Image image5 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
				image5 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter,  
							titreGraphique.toString(),
							donneesFluxSortants.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(0)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(0)), 
							compressionLevel);
				
				image5.scalePercent(myWidth/ image5.getWidth() * 100);
				
				PdfPCell cell5 = new PdfPCell(image5);
				cell5.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell5);
			}
			
			
			
			// titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(0)));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			
			// graphique 
			com.lowagie.text.Image image6 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
				image6 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter, 
							titreGraphique.toString(),
							donneesFluxEntr.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(0)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(0)), 
							compressionLevel);
				 
				image6.scalePercent(myWidth/ image6.getWidth() * 100);
				
				PdfPCell cell6 = new PdfPCell(image6);
				cell6.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell6);
			}
			

					
			
			}
			// au moins 2 zones d'échange
			if (zonesEtudeZoneEch.get(zoneId).size() >= 2) {
				
				
			// titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(1)));
			
			// graphique 
			com.lowagie.text.Image image7 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
				image7 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter, 
							titreGraphique.toString(),
							donneesFluxSortants.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(1)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(1)), 
							compressionLevel);
				
				image7.scalePercent(myWidth/ image7.getWidth() * 100);
				
				PdfPCell cell7 = new PdfPCell(image7);
				cell7.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell7);
			}
			

			
			// titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(1)));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			
			// graphique 
			com.lowagie.text.Image image8 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
					image8 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter, 
							titreGraphique.toString(),
							donneesFluxEntr.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(1)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(1)), 
							compressionLevel);
				
				image8.scalePercent(myWidth/ image8.getWidth() * 100);
				
				PdfPCell cell8 = new PdfPCell(image8);
				cell8.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell8);
			}
			

					
			
			}
			// au moins 3 zones d'échange
			if (zonesEtudeZoneEch.get(zoneId).size() >= 3) {
				
				
			// titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(2)));
			
			// graphique 
			com.lowagie.text.Image image9 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
				image9 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter, 
							titreGraphique.toString(),
							donneesFluxSortants.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(2)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(2)), 
							compressionLevel);
				
				image9.scalePercent(myWidth/ image9.getWidth() * 100);
				
				PdfPCell cell9 = new PdfPCell(image9);
				cell9.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell9);
			}
			

			
			// titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(2)));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			
			// graphique 
			com.lowagie.text.Image image10 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
				image10 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter, 
							titreGraphique.toString(),
							donneesFluxEntr.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(2)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(2)), 
							compressionLevel);
				
				image10.scalePercent(myWidth/ image10.getWidth() * 100);
				
				PdfPCell cell10 = new PdfPCell(image10);
				cell10.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell10);
			}

			

					
			
			}
			// au moins 4 zones d'échange
			if (zonesEtudeZoneEch.get(zoneId).size() >= 4) {
						
						
			// titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(3)));
			
			// graphique 
			com.lowagie.text.Image image11 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
				 image11 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter, 
							titreGraphique.toString(),
							donneesFluxSortants.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(3)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(3)), 
							compressionLevel);
				
				image11.scalePercent(myWidth/ image11.getWidth() * 100);
				
				PdfPCell cell11 = new PdfPCell(image11);
				cell11.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell11);
			}
			

			
			// titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(3)));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			
			// graphique 
			com.lowagie.text.Image image12 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
				image12 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter, 
							titreGraphique.toString(),
							donneesFluxEntr.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(3)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(3)), 
							compressionLevel);
				
				image12.scalePercent(myWidth/ image12.getWidth() * 100);
				
				PdfPCell cell12 = new PdfPCell(image12);
				cell12.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell12);
			}
					
			
			}
			// au moins 5 zones d'échange
			if (zonesEtudeZoneEch.get(zoneId).size() >= 5) {
				
	
			// titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(4)));
			
			// graphique 
			com.lowagie.text.Image image13 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
				image13 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter, 
							titreGraphique.toString(),
							donneesFluxSortants.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(4)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(4)), 
							compressionLevel);
				
				image13.scalePercent(myWidth/ image13.getWidth() * 100);
				
				PdfPCell cell13 = new PdfPCell(image13);
				cell13.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell13);
			}


			

	
			// titre
			titreGraphique = new StringBuffer();
			titreGraphique.append("Flux de ");
			titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(4)));
			titreGraphique.append(" vers ");
			titreGraphique.append(zoneLibelle.get(zoneId));
			
			// graphique 
			com.lowagie.text.Image image14 =  null;
			if (donneesFluxSortants != null && donneesFluxEntr != null){
				image14 = 
					fluxImage(
							beanParametresResultat,
							pdfWriter, 
							titreGraphique.toString(),
							donneesFluxEntr.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(4)), 
							donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(4)), 
							compressionLevel);
				
				image14.scalePercent(myWidth/ image14.getWidth() * 100);
				
				PdfPCell cell14 = new PdfPCell(image14);
				cell14.setBorder(PdfPCell.NO_BORDER); 
				table.addCell(cell14);
			}

	
			
			}
		}else{
			Font fontSousTitre = font.get("fontSousTitre");
			float borderWidth2 = 0.1f;
			
			PdfPCell cell2 = new PdfPCell(new Phrase(new Chunk("Pas de zones d'échanges", fontSousTitre))); cell2.setPadding(7);
			cell2.setBackgroundColor(Color.WHITE); cell2.setBorderColor(Color.WHITE); cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell2.setColspan(13);
			cell2.setBorderWidthLeft(borderWidth2); cell2.setBorderWidthRight(0); cell2.setBorderWidthTop(0); cell2.setBorderWidthBottom(borderWidth2);
			table.addCell(cell2);
		}
		
		document.add(table);
		
	}


	public void setGraphiques2Bis(
			BeanParametresResultat beanParametresResultat,
			PdfWriter pdfWriter, 
			int compressionLevel,
			Document document,			
			Map<String, Font>  font,
			Map<String, String> zoneLibelle,
			Map<String, List<String>> zonesEtudeZoneEch,
			String zoneId,
			// graphiques
			Map<String, String> hashMap,
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesSoldeFlux,
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxSortants,
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxEntr,
			Map<String, HashMap<String, Double>> donneesFluxRange
				) throws Exception {
		
		Font fontBold = font.get("fontBold");
		
		PdfPTable table = new PdfPTable(2); 
        table.setWidthPercentage(100);
        
        float myWidth = (document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / 2;
		
        // titre
        StringBuffer titreGraphique = new StringBuffer();
        titreGraphique.append("                       Solde migratoire entre ");
        MiseEnForme miseEnForme = new MiseEnForme();
        titreGraphique.append(miseEnForme.anneePrecedente(hashMap.get("anneeDebut")));
        titreGraphique.append(" et ");
        titreGraphique.append(hashMap.get("anneeDebut"));
        titreGraphique.append(" (hors échanges avec l'étranger)");
       
        com.lowagie.text.Image image20 = null;
        // graphique Solde migratoire au RP
		if (donneesSoldeFlux != null){
	        image20 = 
				fluxSoldeImage(
						beanParametresResultat,
						pdfWriter, 
						titreGraphique.toString(), 
						donneesSoldeFlux.get(zoneId), 
						compressionLevel);		

			image20.scalePercent(myWidth/ image20.getWidth() * 100);
			
			PdfPCell cell20 = new PdfPCell(image20);
			cell20.setBorder(PdfPCell.NO_BORDER); 
			cell20.setColspan(2);
			cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell20);
		}

		

		
		document.add(table);
		
		// Flux avec les principales zones d'échange
		StringBuffer titre = new StringBuffer();
		titre.append("Flux avec les principales zones d'échange (");
		titre.append(hashMap.get("auRP"));
		titre.append(" ");
		titre.append(hashMap.get("anneeDebut"));
		titre.append(")");
		Paragraph paragraph = new Paragraph();		
		paragraph.add(new Chunk(titre.toString(), fontBold)); 
		paragraph.add(new Chunk(" ", fontBold));
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		paragraph.setSpacingBefore(10);
		paragraph.setSpacingAfter(10);
		document.add(paragraph); 

		
		if (zonesEtudeZoneEch != null && !zonesEtudeZoneEch.isEmpty() 
				&& zonesEtudeZoneEch.get(zoneId) != null && !zonesEtudeZoneEch.isEmpty()){
			table = new PdfPTable(2); 
	        table.setWidthPercentage(100);
	        
	        
	        if (donneesFluxSortants != null && donneesFluxEntr != null){
	        	for(int i = 0; i < Math.min(zonesEtudeZoneEch.get(zoneId).size(),5);i++ ){
		        // Flux Origine vers destination{
		        	// titre
					titreGraphique = new StringBuffer();
					titreGraphique.append("Flux de ");
					titreGraphique.append(zoneLibelle.get(zoneId));
					titreGraphique.append(" vers ");
					titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(i)));
					
					// graphique 
					com.lowagie.text.Image imageA = 
						fluxImage(
									beanParametresResultat,
									pdfWriter,  
									titreGraphique.toString(),
									donneesFluxSortants.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(i)), 
									donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(i)), 
									compressionLevel);
						
					imageA.scalePercent(myWidth/ imageA.getWidth() * 100);
					
					PdfPCell cell5 = new PdfPCell(imageA);
					cell5.setBorder(PdfPCell.NO_BORDER); 
					table.addCell(cell5);
					
					
					// Flux destination vers Origine
					// titre
					titreGraphique = new StringBuffer();
					titreGraphique.append("Flux de ");
					titreGraphique.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(i)));
					titreGraphique.append(" vers ");
					titreGraphique.append(zoneLibelle.get(zoneId));
					
					// graphique 
					com.lowagie.text.Image imageB =  
							fluxImage(
									beanParametresResultat,
									pdfWriter, 
									titreGraphique.toString(),
									donneesFluxEntr.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(i)), 
									donneesFluxRange.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(i)), 
									compressionLevel);
						 
						imageB.scalePercent(myWidth/ imageB.getWidth() * 100);
						
						PdfPCell cell6 = new PdfPCell(imageB);
						cell6.setBorder(PdfPCell.NO_BORDER); 
						table.addCell(cell6);
		        }
	        }
		}else{
			Font fontSousTitre = font.get("fontSousTitre");
			float borderWidth2 = 0.1f;
			
			PdfPCell cell2 = new PdfPCell(new Phrase(new Chunk("Pas de zones d'échanges", fontSousTitre))); cell2.setPadding(7);
			cell2.setBackgroundColor(Color.WHITE); cell2.setBorderColor(Color.WHITE); cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell2.setColspan(13);
			cell2.setBorderWidthLeft(borderWidth2); cell2.setBorderWidthRight(0); cell2.setBorderWidthTop(0); cell2.setBorderWidthBottom(borderWidth2);
			table.addCell(cell2);
		}
		
		document.add(table);
		
	}

	/**
	 * get le graphique Solde migratoire au RP
	 * <br>
	 * le graphique s'obtient de la façon suivante :
	 * <br>
	 * - création d'un org.jfree.chart.JFreeChart
	 * <br>
	 * - création d'un com.lowagie.text.Image à partir du org.jfree.chart.JFreeChart
	 * @param beanParametresResultat {@link fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
	 * @param pdfWriter le writer associé au document
	 * @param titre titre du graphique
	 * @param donnees données graphique Solde migratoire au RP
	 * @param compressionLevel le taux de compression
	 * @return com.lowagie.text.Image
	 */
	public Image fluxSoldeImage(
			BeanParametresResultat beanParametresResultat,
			PdfWriter pdfWriter, 
			String titre, 
			Map<Integer, ArrayList<Double>> donnees, 
			int compressionLevel) {
		
		Image image = null;
		try{
	        int width = 400;
	        int height = 370;
	        
	        PdfContentByte cb = pdfWriter.getDirectContent();
	        
	        PdfTemplate tp = cb.createTemplate(width, height);
	        
	        Graphics2D g2d = tp.createGraphics(width, height, new DefaultFontMapper());
	        
	        Rectangle2D rect2d = new Rectangle2D.Float(0, 0, width, height);
	        
	        ((new JFreeChartFluxSoldeService(
	        		titre, 
	        		beanParametresResultat.getAge100(),
	        		donnees)).getChart()).draw(g2d, rect2d);
	        
	        g2d.dispose();
	        
	        image = Image.getInstance(tp);
	        
	        image.setCompressionLevel(compressionLevel); 
		}
		catch(Exception e){
			
		}
		
		return image;
	}

	/**
	 * get le graphique Flux de .. vers ..
	 * <br>
	 * le graphique s'obtient de la façon suivante :
	 * <br>
	 * - création d'un org.jfree.chart.JFreeChart
	 * <br>
	 * - création d'un com.lowagie.text.Image à partir du org.jfree.chart.JFreeChart
	 * @param beanParametresResultat {@link fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
	 * @param pdfWriter le writer associé au document
	 * @param titre titre du graphique
	 * @param donnees données flux sortants des zones d'étude vers les zones d'échange,
	 * ou données flux entrants des zones d'étude en provenance des zones d'échange
	 * @param compressionLevel le taux de compression
	 * @return com.lowagie.text.Image
	 */
	public Image fluxImage(
			BeanParametresResultat beanParametresResultat,
			PdfWriter pdfWriter, 
			String titre, 
			Map<Integer, ArrayList<Double>> donnees, 
			Double fluxMax,
			int compressionLevel) {
		
		Image image = null;
		try{
	        int width = 400;
	        int height = 370;
	        
	        PdfContentByte cb = pdfWriter.getDirectContent();
	        
	        PdfTemplate tp = cb.createTemplate(width, height);
	        
	        Graphics2D g2d = tp.createGraphics(width, height, new DefaultFontMapper());
	        
	        Rectangle2D rect2d = new Rectangle2D.Float(0, 0, width, height);
	        
	        ((new JFreeChartFluxService(
	        		titre,
	        		beanParametresResultat.getAge100(),
	        		donnees, 
	        		fluxMax)).getChart()).draw(g2d, rect2d);
	        
	        g2d.dispose();
	        
	        image = Image.getInstance(tp);
	        
	        image.setCompressionLevel(compressionLevel); 
		}
		catch(Exception e){
			
		}
		
		return image;
	}
	
	
}
