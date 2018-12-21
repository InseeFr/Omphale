package fr.insee.omphale.generationDuPDF.service.projection.impl;

import java.util.List;
import java.util.Map;

import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Ajout signets au Pdf.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class ITextJSignets {

	
	/**
	 * Ajout signets au Pdf
	 * @param pdfWriter le writer associé au document
	 * @param listeZonesidentifiants des zones du zonage  
	 * 		<br>    
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		liste par ordre alphabétique des libellé des zones
	 * 		<br>    
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		Ex. 
	 * 		<br>    
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		listeZones.get(0) --&gt. "92"
	 * 		<br>    
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		listeZones.get(1) --&gt. "75"
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
	 */
	public void setSignets(
			PdfWriter pdfWriter,
			List<String> listeZones,
			Map<String, String> zoneLibelle) {
		
		
		// Code 2
		PdfContentByte cb = pdfWriter.getDirectContent();
		PdfOutline root = cb.getRootOutline();
		
		// 
		pdfWriter.setViewerPreferences(PdfWriter.PageModeUseOutlines);
		
		// Code 3
		for(String zoneId: listeZones){
		
			// zone
			StringBuffer lieni = new StringBuffer();
			lieni.append("lien");
			lieni.append(zoneId);
			PdfOutline oline = new PdfOutline(
				root, 
				PdfAction.gotoLocalPage(lieni.toString(), false),
				zoneLibelle.get(zoneId));
			oline.setOpen(false);
			
			// Expertise générale
			new PdfOutline(
					oline, 
					PdfAction.gotoLocalPage(lieni.toString(), false),
					"Expertise générale");
			
			// Expertise des flux
			StringBuffer expertiseFluxI = new StringBuffer();
			expertiseFluxI.append("ExpertiseFlux");
			expertiseFluxI.append(zoneId);
			new PdfOutline(
					oline, 
					PdfAction.gotoLocalPage(expertiseFluxI.toString(), false),
					"Expertise des flux");
			
			// Flux au RP
			StringBuffer fluxAuRPI = new StringBuffer();
			fluxAuRPI.append("FluxAuRP");
			fluxAuRPI.append(zoneId);
			new PdfOutline(
					oline, 
					PdfAction.gotoLocalPage(fluxAuRPI.toString(), false),
					"Flux au RP");
		}
		
	}
}
