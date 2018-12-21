package fr.insee.omphale.generationDuPDF.service.projection.impl;


import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * Ecriture des graphiques Pyramide des âges, Evolution de la population, Quotients de fécondité, Quotients de décès dans le Pdf
 * pour une zone donnée.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class ITextEGraphiques1Service {

	
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
	 * @param donneesPyramide données graphique Pyramide des âges
	 * @param donneesPopulation données graphique Evolution de la population
	 * @param donneesCourbe données graphique Quotients de fécondité
	 * @param donneesDec données graphique Quotients de décès
	 * @throws Exception
	 */
	public void setGraphiques1(
			BeanParametresResultat beanParametresResultat,
			PdfWriter pdfWriter, 
			int compressionLevel,
			Document document,			
			Map<String, Font>  font,
			Map<String, String> zoneLibelle,
			String zoneId,
			// graphiques
			Map<String, String> hashMap,
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesPyramide, 
			Map<String, ArrayList<Double>> donneesPopulation,
			Map<String, ArrayList<Double>> donneesCourbe,
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesDec
				) throws Exception {
		

		PdfPTable table = new PdfPTable(2); 
        table.setWidthPercentage(100);
        
        float myWidth = (document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin()) / 2;
			        
        StringBuffer titre = new StringBuffer();
    	titre.append("           Pyramides des âges ");
    	titre.append(hashMap.get("anneeDebut"));
    	titre.append(", ");
    	titre.append(hashMap.get("anneeDebutPlus5"));
    	titre.append(" et ");
    	titre.append(hashMap.get("anneeFin"));
    	
    	// graphique Pyramide des âges
		com.lowagie.text.Image image1 = pyramideImage(
				beanParametresResultat,
				pdfWriter, 
				titre.toString(), 
				hashMap,
				donneesPyramide.get(zoneId),
				compressionLevel);
		
		// réduction 
		image1.scalePercent(myWidth/ image1.getWidth() * 100);
		
        PdfPCell cell1 = new PdfPCell(image1);
		cell1.setPaddingBottom(40); // padding
		cell1.setBorder(PdfPCell.NO_BORDER); 
		table.addCell(cell1);

		// graphique Evolution de la population
		com.lowagie.text.Image image2 = populationImage(
				pdfWriter, 
				"Evolution de la population (en milliers)", 
				hashMap,
				donneesPopulation.get(zoneId), 
				compressionLevel);
		
		image2.scalePercent(myWidth/ image2.getWidth() * 100);
		
		PdfPCell cell2 = new PdfPCell(image2);
		cell2.setBorder(PdfPCell.NO_BORDER); 
		table.addCell(cell2);
		
		// graphique Quotients de fécondité
		com.lowagie.text.Image image3 = courbeImage(
				beanParametresResultat,
				pdfWriter, 
				"Quotients de fécondité", 
				hashMap, 
				donneesCourbe.get(zoneId), 
				compressionLevel);
		
		image3.scalePercent(myWidth/ image3.getWidth() * 100);
		
		PdfPCell cell3 = new PdfPCell(image3);
		cell3.setBorder(PdfPCell.NO_BORDER); 
		table.addCell(cell3);
		
		// graphique Quotients de décès
		com.lowagie.text.Image image4 = decImage(
				beanParametresResultat,
				pdfWriter, 
				"Quotients de décès", 
				hashMap, 
				donneesDec.get(zoneId), 
				compressionLevel);
		
		image4.scalePercent(myWidth/ image4.getWidth() * 100);
		
		PdfPCell cell4 = new PdfPCell(image4);
		cell4.setBorder(PdfPCell.NO_BORDER); 
		table.addCell(cell4);
		
		document.add(table);
	}
	
	
	
	
	/**
	 * get le graphique Pyramide des âges
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
	 * @param donnees données graphique Pyramide des âges
	 * @param compressionLevel le taux de compression
	 * @return com.lowagie.text.Image
	 */
	public Image pyramideImage(
			BeanParametresResultat beanParametresResultat,
			PdfWriter pdfWriter, 
			String titre, 
			Map<String, String> hashMap,
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
	        JFreeChartPyramideAgeService pyramideAgeService = 	        new JFreeChartPyramideAgeService(
	        		titre, 
	        		beanParametresResultat.getAge100(),
	        		hashMap,
	        		donnees);
	        pyramideAgeService.getChart().draw(g2d, rect2d);
	        
	        g2d.dispose();
	        
	        image = Image.getInstance(tp);
	        
	        image.setCompressionLevel(compressionLevel); 
		}
		catch(Exception e){
			
		}
		
		return image;
	}
	
	/**
	 * get le graphique Evolution de la population
	 * <br>
	 * le graphique s'obtient de la façon suivante :
	 * <br>
	 * - création d'un org.jfree.chart.JFreeChart
	 * <br>
	 * - création d'un com.lowagie.text.Image à partir du org.jfree.chart.JFreeChart
	 * @param pdfWriter le writer associé au document
	 * @param titre titre du graphique
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
	 * @param donnees données graphique Evolution de la population
	 * @param compressionLevel le taux de compression
	 * @return com.lowagie.text.Image
	 */
	public Image populationImage(
			PdfWriter pdfWriter, 
			String titre, 
			Map<String, String> hashMap,
			List<Double> donnees, 
			int compressionLevel) {
		
		Image image = null;
		try{
	        int width = 400;
	        int height = 370;
	        
	        PdfContentByte cb = pdfWriter.getDirectContent();
	        
	        PdfTemplate tp = cb.createTemplate(width, height);
	        
	        Graphics2D g2d = tp.createGraphics(width, height, new DefaultFontMapper());
	        
	        Rectangle2D rect2d = new Rectangle2D.Float(0, 0, width, height);
	        
	        ((new JFreeChartPopulationService(titre, hashMap, donnees)).getChart()).draw(g2d, rect2d);
	        
	        g2d.dispose();
	        
	        image = Image.getInstance(tp);
	        
	        image.setCompressionLevel(compressionLevel); 
		}
		catch(Exception e){
			
		}
		
		return image;
	}
	
	/**
	 * get le graphique Quotients de fécondité
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
	 * @param donnees données graphique Quotients de fécondité
	 * @param compressionLevel le taux de compression
	 * @return com.lowagie.text.Image
	 */
	public Image courbeImage(
			BeanParametresResultat beanParametresResultat,
			PdfWriter pdfWriter,
			String titre,
			Map<String, String> hashMap,
			ArrayList<Double> donnees,
			int compressionLevel) {
		
		Image image = null;
		try{
	        int width = 400;
	        int height = 370;
	        
	        PdfContentByte cb = pdfWriter.getDirectContent();
	        
	        PdfTemplate tp = cb.createTemplate(width, height);
	        
	        Graphics2D g2d = tp.createGraphics(width, height, new DefaultFontMapper());
	        
	        Rectangle2D rect2d = new Rectangle2D.Float(0, 0, width, height);
	        
	        ((new JFreeChartCourbeService(
	        		titre, 
	        		beanParametresResultat.getAgeDebutMere(),
	        		beanParametresResultat.getAgeFinMere(),
	        		hashMap, 
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
	 * get le graphique Quotients de décès
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
	 * @param donnees données graphique Quotients de décès
	 * @param compressionLevel le taux de compression
	 * @return com.lowagie.text.Image
	 */
	public Image decImage(
			BeanParametresResultat beanParametresResultat,
			PdfWriter pdfWriter, 
			String titre, 
			Map<String, String> hashMap,
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
	        
	        ((new JFreeChartDecService(
	        		titre, 
	        		beanParametresResultat.getAge100(),
	        		hashMap, donnees)).getChart()).draw(g2d, rect2d);
	        
	        g2d.dispose();
	        
	        image = Image.getInstance(tp);
	        
	        image.setCompressionLevel(compressionLevel); 
		}
		catch(Exception e){
			
		}
		
		return image;
	}
}
