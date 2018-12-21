package fr.insee.omphale.generationDuPDF.service.projection.impl;


import java.awt.Color;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

import fr.insee.omphale.generationDuPDF.util.MiseEnForme;

/**
 * Ecriture du tableau Flux avec les principales zones d'échange dans le Pdf pour une zone donnée.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class ITextGTableau2Service { 
	
	private Document document = null;			
	@SuppressWarnings("unused")
	private Map<String, Font> font = null;
	private Map<String, String> zoneLibelle = null;
	private Map<String, List<String>> zonesEtudeZoneEch = null;
	private String zoneId = null;
	private Map<String, String> hashMap = null;
	
	private Font fontBold = null;
	private Font fontTableauBoldWhite = null;
	private Font fontTableau = null;
	private Font fontTableauSousTitre = null;		
	private Font fontTableauLink = null;
	private Font fontTableauBoldItalique = null;
	
	private NumberFormat numberFormatPopulation = null;
	private NumberFormat numberFormatPopulationEvol = null;
	private NumberFormat numberFormatAgeMoyen = null;
	
	private int nbColonnes = 13;
	
	private float padding = 2;
	private float borderWidth2 = 0.1f;
	private float borderWidthTop = 0.1f;
	private float borderWidth4 = 0.9f;
	
	private PdfPTable table = null;
	
	private String projectionUtilisateur = null;
	
	private StringBuffer fluxAuRPAnneeDebut = null;
	
	private StringBuffer fluxProjetesAnneeDebutPlus5 = null;
	
	private StringBuffer fluxProjetesAnneeFin = null;
	
	private StringBuffer fluxAnneeDebutAnneeFin = null;
	
	private float height = 7;
	
	
	
	public ITextGTableau2Service(
			Document document,			
			Map<String, Font> font,
			Map<String, String> zoneLibelle,
			Map<String, List<String>> zonesEtudeZoneEch,
			String zoneId,
			Map<String, String> hashMap
						   ) {
		
	    this.document = document;
		this.font = font;
		this.zoneLibelle = zoneLibelle;
		this.zonesEtudeZoneEch = zonesEtudeZoneEch;
		this.zoneId = zoneId;
		this.hashMap = hashMap;
				
		fontBold = font.get("fontBold");
		fontTableauBoldWhite = font.get("fontTableauBoldWhite");
		fontTableau = font.get("fontTableau");
		fontTableauSousTitre = font.get("fontTableauSousTitre");		
		fontTableauLink = font.get("fontTableauLink");
		fontTableauBoldItalique = font.get("fontTableauBoldItalique");
		
        numberFormatPopulation = NumberFormat.getInstance(java.util.Locale.FRENCH);
        numberFormatPopulation.setMaximumFractionDigits(0);
        
        numberFormatPopulationEvol = NumberFormat.getPercentInstance(java.util.Locale.FRENCH);
        numberFormatPopulationEvol.setMaximumFractionDigits(1);
        
        numberFormatAgeMoyen = NumberFormat.getInstance(java.util.Locale.FRENCH);
        numberFormatAgeMoyen.setMaximumFractionDigits(1);
        
		// projectionUtilisateur : "1" si idZonage == null || idZonage != 0
		// 						   "0" sinon
		projectionUtilisateur = hashMap.get("projectionUtilisateur");
		
		// ex. : "Flux observés au RP 2007"
		fluxAuRPAnneeDebut = new StringBuffer();
		fluxAuRPAnneeDebut.append(hashMap.get("fluxObserveEntre"));
		MiseEnForme misEnForme = new MiseEnForme();
		fluxAuRPAnneeDebut.append(misEnForme.anneePrecedente(hashMap.get("anneeDebut")));
		fluxAuRPAnneeDebut.append(" et ");
		fluxAuRPAnneeDebut.append(hashMap.get("anneeDebut"));
		
		// ex. : Flux projetés 2007-2008
		fluxProjetesAnneeDebutPlus5 =  new StringBuffer();
		fluxProjetesAnneeDebutPlus5.append("Flux projetés ");
		fluxProjetesAnneeDebutPlus5.append(hashMap.get("anneeDebut"));
		fluxProjetesAnneeDebutPlus5.append("-");
		fluxProjetesAnneeDebutPlus5.append(hashMap.get("anneeDebutPlus1"));
		
		// ex. : Flux projetés 2041-2042
		fluxProjetesAnneeFin =  new StringBuffer();
		fluxProjetesAnneeFin.append("Flux projetés ");
		fluxProjetesAnneeFin.append(hashMap.get("anneeFinMoins1"));
		fluxProjetesAnneeFin.append("-");
		fluxProjetesAnneeFin.append(hashMap.get("anneeFin"));
		
		// ex. : Flux cumulés (2007-2042)
		fluxAnneeDebutAnneeFin =  new StringBuffer();
		fluxAnneeDebutAnneeFin.append("Flux cumulés (");
		fluxAnneeDebutAnneeFin.append(hashMap.get("anneeDebut"));
		fluxAnneeDebutAnneeFin.append("-");
		fluxAnneeDebutAnneeFin.append(hashMap.get("anneeFin"));
		fluxAnneeDebutAnneeFin.append(")");
	}
	
	
	public void setTitreTableau2(Document document, Map<String, String> zoneLibelle,String zoneId	, Map<String, Font> font) throws Exception{
		Font fontBold = font.get("fontBold");
		Font fontSousTitre = font.get("fontSousTitre");
		PdfPTable table = new PdfPTable(13); 
		table.setWidthPercentage(100);
		int[] l = {16, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
		table.setWidths(l);	
		table.setSpacingBefore(0); 
		table.setSpacingAfter(20); 
		table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		
		float borderWidth2 = 0.1f;

		
		/* titre */ 
		StringBuffer titre =  new StringBuffer();
		titre.append("Flux de ");
		titre.append(zoneLibelle.get(zoneId));
		titre.append(" avec ses principales zones d'échange");
		PdfPCell cell = new PdfPCell(new Phrase(new Chunk(titre.toString(), fontBold))); cell.setPadding(7);
		cell.setBackgroundColor(Color.WHITE); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell.setColspan(13);
		cell.setBorderWidthLeft(borderWidth2); cell.setBorderWidthRight(0); cell.setBorderWidthTop(0); cell.setBorderWidthBottom(borderWidth2);
		table.addCell(cell);
		PdfPCell cell2 = new PdfPCell(new Phrase(new Chunk("Pas de zones d'échanges", fontSousTitre))); cell2.setPadding(7);
		cell2.setBackgroundColor(Color.WHITE); cell2.setBorderColor(Color.WHITE); cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		cell2.setColspan(13);
		cell2.setBorderWidthLeft(borderWidth2); cell2.setBorderWidthRight(0); cell2.setBorderWidthTop(0); cell2.setBorderWidthBottom(borderWidth2);
		table.addCell(cell2);
		document.add(table);
	}	
	/**
	 * 
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
	 * @param tousAgesPlus5VersAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne Vers
	 * @param tousAgesPlus5VersAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne Vers
	 * @param tousAgesPlus5VersAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne Vers
	 * @param tousAgesPlus5Vers tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne Vers (flux cumulés)
	 * @param tousAgesPlus5DeAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne De
	 * @param tousAgesPlus5DeAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne De
	 * @param tousAgesPlus5DeAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne De
	 * @param tousAgesPlus5De tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne De (flux cumulés)
	 * @param versAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne Vers
	 * @param versAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne Vers
	 * @param versAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne Vers
	 * @param vers tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne Vers (flux cumulés)
	 * @param deAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne De
	 * @param deAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne De
	 * @param deAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne De
	 * @param de tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, par zone (ex. lignes Ain, Alpes Maritimes, ..)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne De (flux cumulés)
	 * @param tousAgesPlus5ToutesZonesVersAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne Vers
	 * @param tousAgesPlus5ToutesZonesVersAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne Vers
	 * @param tousAgesPlus5ToutesZonesVersAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne Vers
	 * @param tousAgesPlus5ToutesZonesVers tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne Vers (flux cumulés)
	 * @param tousAgesPlus5ToutesZonesDeAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne De
	 * @param tousAgesPlus5ToutesZonesDeAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne De
	 * @param tousAgesPlus5ToutesZonesDeAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne De
	 * @param tousAgesPlus5ToutesZonesDe tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données "1 ans et plus", ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne De (flux cumulés)
	 * @param toutesZonesVersAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne Vers
	 * @param toutesZonesVersAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne Vers
	 * @param toutesZonesVersAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne Vers
	 * @param toutesZonesVers tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne Vers (flux cumulés)
	 * @param toutesZonesDeAnneeDebut tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		1ère colonne De
	 * @param toutesZonesDeAnneeDebutPlus5 tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		2ème colonne De
	 * @param toutesZonesDeAnneeFin tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		3ème colonne De
	 * @param toutesZonesDe tableau Flux avec les principales zones d'échange
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		données par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		4ème colonne De (flux cumulés)
	 * @throws Exception
	 */
	public void setTableau2(
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeDebut,
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeFin,
			Map<String, HashMap<String, Double>> tousAgesPlus5Vers,
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeDebut,
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeFin,
			Map<String, HashMap<String, Double>> tousAgesPlus5De,
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeDebut,
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeDebutPlus5,
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeFin,
			Map<String, HashMap<String, HashMap<String, Double>>> vers,
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeDebut,
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeDebutPlus5,
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeFin,
			Map<String, HashMap<String, HashMap<String, Double>>> de,
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeDebut,
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeDebutPlus5,
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeFin,
			Map<String, Double> tousAgesPlus5ToutesZonesVers,
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeDebut,
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeDebutPlus5,
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeFin,
			Map<String, Double> tousAgesPlus5ToutesZonesDe,
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeDebut,
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeFin,
			Map<String, HashMap<String, Double>> toutesZonesVers,
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeDebut,
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeFin,
			Map<String, HashMap<String, Double>> toutesZonesDe
						   ) throws Exception {
		
			// PdfPTable
			table = new PdfPTable(nbColonnes); 
			table.setWidthPercentage(100);
			int[] l = {16, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
			table.setWidths(l);	
			table.setSpacingBefore(0); 
			table.setSpacingAfter(20); 
			table.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
			table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			
			/* titre */ 
			// ex. "Flux de zone1 avec ses principales zones d'échange"
			StringBuffer titre =  new StringBuffer();
			titre.append("Flux de ");
			titre.append(zoneLibelle.get(zoneId));
			titre.append(" avec ses principales zones d'échange");
			PdfPCell cell = new PdfPCell(new Phrase(new Chunk(titre.toString(), fontBold))); cell.setPadding(7);
			cell.setBackgroundColor(Color.WHITE); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			cell.setColspan(13);
			cell.setBorderWidthLeft(borderWidth2); cell.setBorderWidthRight(0); cell.setBorderWidthTop(0); cell.setBorderWidthBottom(borderWidth2);
			table.addCell(cell);
			
			/* note de lecture */
			// ex. "Note de lecture : Entre 2007 et 2012, 3 501 individus migreraient de zone1 vers zone2 (Vers), et 5 448 de zone2 vers zone1 (De)."
			StringBuffer noteLecture = new StringBuffer();
			noteLecture.append("Note de lecture : Entre ");
			noteLecture.append(hashMap.get("anneeDebut"));
			noteLecture.append(" et ");
			noteLecture.append(hashMap.get("anneeDebutPlus1"));
			noteLecture.append(", ");
			noteLecture.append(numberFormatPopulation.format(tousAgesPlus5VersAnneeDebutPlus5.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(0))));
			noteLecture.append(" individus migreraient de ");
			noteLecture.append(zoneLibelle.get(zoneId));
			noteLecture.append(" vers ");
			noteLecture.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(0)));
			noteLecture.append(" (Vers), et ");
			noteLecture.append(numberFormatPopulation.format(tousAgesPlus5DeAnneeDebutPlus5.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(0))));
			noteLecture.append(" de ");
			noteLecture.append(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(0)));
			noteLecture.append(" vers ");
			noteLecture.append(zoneLibelle.get(zoneId));
			noteLecture.append(" (De).");
			cell = new PdfPCell(new Phrase(new Chunk(noteLecture.toString(), fontTableauSousTitre))); cell.setPadding(4);							
			cell.setBackgroundColor(Color.WHITE); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
			cell.setColspan(13);
			cell.setBorderWidthLeft(borderWidth2); cell.setBorderWidthRight(0); cell.setBorderWidthTop(borderWidth2); cell.setBorderWidthBottom(0);
			table.addCell(cell);

			/* tableau 1 ans et plus */
			addTableau5AnsEtPlus(
					"1 ans et plus",
					tousAgesPlus5VersAnneeDebut,
					tousAgesPlus5VersAnneeDebutPlus5,
					tousAgesPlus5VersAnneeFin,
					tousAgesPlus5Vers,
					tousAgesPlus5DeAnneeDebut,
					tousAgesPlus5DeAnneeDebutPlus5,
					tousAgesPlus5DeAnneeFin,
					tousAgesPlus5De,
					tousAgesPlus5ToutesZonesVersAnneeDebut,
					tousAgesPlus5ToutesZonesVersAnneeDebutPlus5,
					tousAgesPlus5ToutesZonesVersAnneeFin,
					tousAgesPlus5ToutesZonesVers,
					tousAgesPlus5ToutesZonesDeAnneeDebut,
					tousAgesPlus5ToutesZonesDeAnneeDebutPlus5,
					tousAgesPlus5ToutesZonesDeAnneeFin,
					tousAgesPlus5ToutesZonesDe
						  		);
			
			/* tableau 1-24 ans */
			addTableau(
					"1-24 ans", // titre
					"1-24", // tranche d'âge
					versAnneeDebut,
					versAnneeDebutPlus5,
					versAnneeFin,
					vers,
					deAnneeDebut,
					deAnneeDebutPlus5,
					deAnneeFin,
					de,
					toutesZonesVersAnneeDebut,
					toutesZonesVersAnneeDebutPlus5,
					toutesZonesVersAnneeFin,
					toutesZonesVers,
					toutesZonesDeAnneeDebut,
					toutesZonesDeAnneeDebutPlus5,
					toutesZonesDeAnneeFin,
					toutesZonesDe
					  );
			
			/* tableau 25-59 ans */
			addTableau(
					"25-59 ans", // titre
					"25-59", // tranche d'âge
					versAnneeDebut,
					versAnneeDebutPlus5,
					versAnneeFin,
					vers,
					deAnneeDebut,
					deAnneeDebutPlus5,
					deAnneeFin,
					de,
					toutesZonesVersAnneeDebut,
					toutesZonesVersAnneeDebutPlus5,
					toutesZonesVersAnneeFin,
					toutesZonesVers,
					toutesZonesDeAnneeDebut,
					toutesZonesDeAnneeDebutPlus5,
					toutesZonesDeAnneeFin,
					toutesZonesDe
			  		  );
			
			/* tableau 60 ans et plus */
			addTableau(
					"60 ans et plus", // titre
					"60", // tranche d'âge
					versAnneeDebut,
					versAnneeDebutPlus5,
					versAnneeFin,
					vers,
					deAnneeDebut,
					deAnneeDebutPlus5,
					deAnneeFin,
					de,
					toutesZonesVersAnneeDebut,
					toutesZonesVersAnneeDebutPlus5,
					toutesZonesVersAnneeFin,
					toutesZonesVers,
					toutesZonesDeAnneeDebut,
					toutesZonesDeAnneeDebutPlus5,
					toutesZonesDeAnneeFin,
					toutesZonesDe
			  		  );

			document.add(table);
	}
	
	
	/* tableau 1 ans et plus */
	public void addTableau5AnsEtPlus(
			String titre, // "1 ans et plus"
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeDebut,
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeFin,
			Map<String, HashMap<String, Double>> tousAgesPlus5Vers,
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeDebut,
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeFin,
			Map<String, HashMap<String, Double>> tousAgesPlus5De,
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeDebut,
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeDebutPlus5,
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeFin,
			Map<String, Double> tousAgesPlus5ToutesZonesVers,
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeDebut,
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeDebutPlus5,
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeFin,
			Map<String, Double> tousAgesPlus5ToutesZonesDe
						  ){ 
		
		/* en-tête */
		// 1 ans et plus Flux observés au RP 2007 Flux projetés 2007-2012 Flux projetés 2037-2042 Flux cumulés (2007-2042)
		//               Vers De Solde            Vers De Solde           Vers De Solde           Vers De Solde
		addEnTete(titre);
		
		/* lignes 5 zones d'échange */ 
		// boucle liste des principales zones d'échange
		// le nombre des zones d'échange est : zonesEtudeZoneEch.get(zoneId).size()
		
		for(int j = 0 ; j < Math.min(5, zonesEtudeZoneEch.get(zoneId).size()); j++) {
			
			if (j == 0) { // 1ère zone d'échange
				borderWidthTop = 0.9f;
			}
			else { // 2ème - 5ème zone d'échange
				borderWidthTop = 0.1f;
			}
			
			// 1ère cell : nom de la zone d'échange
			addCellNomZoneEchange(zoneId, j, borderWidthTop);
	
			/* autres cell */
			addCell(numberFormatPopulation.format(tousAgesPlus5VersAnneeDebut.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth4,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(tousAgesPlus5DeAnneeDebut.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(
					- tousAgesPlus5VersAnneeDebut.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j)).doubleValue()
					+ tousAgesPlus5DeAnneeDebut.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j)).doubleValue()
					),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(tousAgesPlus5VersAnneeDebutPlus5.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth4,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(tousAgesPlus5DeAnneeDebutPlus5.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(
					- tousAgesPlus5VersAnneeDebutPlus5.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))
					+ tousAgesPlus5DeAnneeDebutPlus5.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))
					),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(tousAgesPlus5VersAnneeFin.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth4,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(tousAgesPlus5DeAnneeFin.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(
					- tousAgesPlus5VersAnneeFin.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))
					+ tousAgesPlus5DeAnneeFin.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))
					),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(tousAgesPlus5Vers.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth4,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(tousAgesPlus5De.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(
					- tousAgesPlus5Vers.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))
					+ tousAgesPlus5De.get(zoneId).get(zonesEtudeZoneEch.get(zoneId).get(j))
					),
					borderWidth2,
					borderWidthTop);
		}
		
		/* ligne "Toutes zones" */ 
		
		// 1ère cell : "Toutes zones"
		addCellToutesZones();
	
		// autres cell
		addCell(numberFormatPopulation.format(tousAgesPlus5ToutesZonesVersAnneeDebut.get(zoneId)),
				borderWidth4,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(tousAgesPlus5ToutesZonesDeAnneeDebut.get(zoneId)),
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(
				- tousAgesPlus5ToutesZonesVersAnneeDebut.get(zoneId)
				+ tousAgesPlus5ToutesZonesDeAnneeDebut.get(zoneId)
				),
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(tousAgesPlus5ToutesZonesVersAnneeDebutPlus5.get(zoneId)),
				borderWidth4,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(tousAgesPlus5ToutesZonesDeAnneeDebutPlus5.get(zoneId)),
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(
				- tousAgesPlus5ToutesZonesVersAnneeDebutPlus5.get(zoneId)
				+ tousAgesPlus5ToutesZonesDeAnneeDebutPlus5.get(zoneId)
				),
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(tousAgesPlus5ToutesZonesVersAnneeFin.get(zoneId)),
				borderWidth4,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(tousAgesPlus5ToutesZonesDeAnneeFin.get(zoneId)),
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(
				- tousAgesPlus5ToutesZonesVersAnneeFin.get(zoneId)
				+ tousAgesPlus5ToutesZonesDeAnneeFin.get(zoneId)
				),
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(tousAgesPlus5ToutesZonesVers.get(zoneId)),
				borderWidth4,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(tousAgesPlus5ToutesZonesDe.get(zoneId)),
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(
				- tousAgesPlus5ToutesZonesVers.get(zoneId)
				+ tousAgesPlus5ToutesZonesDe.get(zoneId)
				),
				borderWidth2,
				borderWidth4);
		
		// espacement après le tableau
		addEspacement();
	}
	
	/* tableaux 1-24 ans, 25-59 ans, 60 ans et plus */
	public void addTableau(
			String titre, // ex. "1-24 ans"
			String trancheAge, // ex. "1-24"
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeDebut,
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeDebutPlus5,
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeFin,
			Map<String, HashMap<String, HashMap<String, Double>>> vers,
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeDebut,
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeDebutPlus5,
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeFin,
			Map<String, HashMap<String, HashMap<String, Double>>> de,
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeDebut,
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeFin,
			Map<String, HashMap<String, Double>> toutesZonesVers,
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeDebut,
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeDebutPlus5,
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeFin,
			Map<String, HashMap<String, Double>> toutesZonesDe
						  ) {
		
		/* en-tête */
		// ex. 1-24 ans  Flux observés au RP 2007 Flux projetés 2007-2012 Flux projetés 2037-2042 Flux cumulés (2007-2042)
		//               Vers De Solde            Vers De Solde           Vers De Solde           Vers De Solde
		addEnTete(titre);
		
		/* lignes 5 zones d'échange */ 
		// boucle liste des principales zones d'échange
		// le nombre des zones d'échange est : zonesEtudeZoneEch.get(zoneId).size()
		
		for(int j = 0 ; j < Math.min(5, zonesEtudeZoneEch.get(zoneId).size()); j++) {
			
			// 1ère zone d'échange
			if (j == 0) {
				borderWidthTop = 0.9f;
			}
			else { // 2ème - 5ème zone d'échange
				borderWidthTop = 0.1f;
			}
			
			// 1ère cell : nom de la zone d'échange
			addCellNomZoneEchange(zoneId, j, borderWidthTop);

			/* autres cell */
			addCell(numberFormatPopulation.format(versAnneeDebut.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth4,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(deAnneeDebut.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(
					- versAnneeDebut.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))
					+ deAnneeDebut.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))
					),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(versAnneeDebutPlus5.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth4,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(deAnneeDebutPlus5.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(
					- versAnneeDebutPlus5.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))
					+ deAnneeDebutPlus5.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))
					),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(versAnneeFin.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth4,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(deAnneeFin.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(
					- versAnneeFin.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))
					+ deAnneeFin.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))
					),
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(vers.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))), 
					borderWidth4,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(de.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))), 
					borderWidth2,
					borderWidthTop);
			
			addCell(numberFormatPopulation.format(
					- vers.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))
					+ de.get(zoneId).get(trancheAge).get(zonesEtudeZoneEch.get(zoneId).get(j))
					),
					borderWidth2,
					borderWidthTop);
		}
		
		/* ligne "Toutes zones" */ 
		
		// 1ère cell : "Toutes zones"
		addCellToutesZones();

		/* autres cell */
		addCell(numberFormatPopulation.format(toutesZonesVersAnneeDebut.get(zoneId).get(trancheAge)), 
				borderWidth4,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(toutesZonesDeAnneeDebut.get(zoneId).get(trancheAge)), 
				borderWidth2,
				borderWidth4);

		addCell(numberFormatPopulation.format(
				- toutesZonesVersAnneeDebut.get(zoneId).get(trancheAge)
				+ toutesZonesDeAnneeDebut.get(zoneId).get(trancheAge)
				),
				borderWidth2,
				borderWidth4);

		addCell(numberFormatPopulation.format(toutesZonesVersAnneeDebutPlus5.get(zoneId).get(trancheAge)), 
				borderWidth4,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(toutesZonesDeAnneeDebutPlus5.get(zoneId).get(trancheAge)), 
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(
				- toutesZonesVersAnneeDebutPlus5.get(zoneId).get(trancheAge)
				+ toutesZonesDeAnneeDebutPlus5.get(zoneId).get(trancheAge)
				),
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(toutesZonesVersAnneeFin.get(zoneId).get(trancheAge)), 
				borderWidth4,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(toutesZonesDeAnneeFin.get(zoneId).get(trancheAge)), 
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(
				- toutesZonesVersAnneeFin.get(zoneId).get(trancheAge)
				+ toutesZonesDeAnneeFin.get(zoneId).get(trancheAge)
				), 
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(toutesZonesVers.get(zoneId).get(trancheAge)), 
				borderWidth4,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(toutesZonesDe.get(zoneId).get(trancheAge)), 
				borderWidth2,
				borderWidth4);
		
		addCell(numberFormatPopulation.format(
				- toutesZonesVers.get(zoneId).get(trancheAge)
				+ toutesZonesDe.get(zoneId).get(trancheAge)
				), 
				borderWidth2,
				borderWidth4);	
		
		// espacement après le tableau
		addEspacement();
	}
	
	/* ajout de l'en-tête */
	// ex. :
	// 1 ans et plus Flux observés au RP 2007 Flux projetés 2007-2012 Flux projetés 2037-2042 Flux cumulés (2007-2042)
	//               Vers De Solde            Vers De Solde           Vers De Solde           Vers De Solde
	public void addEnTete(String titre){
		/* 1ère ligne entête */ 
		// ex. : 1 ans et plus Flux observés au RP 2007 Flux projetés 2007-2012 Flux projetés 2037-2042 Flux cumulés (2007-2042)
		
		// ex. : 1 ans et plus
		PdfPCell cell = new PdfPCell(new Phrase(new Chunk(titre, fontTableauBoldItalique).setTextRise(-5))); cell.setPadding(padding);
		cell.setBackgroundColor(Color.WHITE); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		table.addCell(cell);
	
		// ex. Flux observés au RP 2007
		addEnTeteCell1ereLigne(fluxAuRPAnneeDebut.toString());
		
		// ex. Flux projetés 2007-2012
		addEnTeteCell1ereLigne(fluxProjetesAnneeDebutPlus5.toString());
		
		// ex. Flux projetés 2037-2042
		addEnTeteCell1ereLigne(fluxProjetesAnneeFin.toString());
		
		// ex. Flux cumulés (2007-2042)
		addEnTeteCell1ereLigne(fluxAnneeDebutAnneeFin.toString());
		
		/* 2ème ligne en-tête */ 		
		//      Vers De Solde            Vers De Solde           Vers De Solde           Vers De Solde
		
		// cell 1
		cell = new PdfPCell(new Phrase(new Chunk("", fontBold))); cell.setPadding(padding);
		cell.setBackgroundColor(Color.WHITE); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
		cell.setBorderWidthLeft(borderWidth2); cell.setBorderWidthRight(borderWidth2); cell.setBorderWidthTop(borderWidthTop); cell.setBorderWidthBottom(borderWidth2);
		table.addCell(cell);
		
		// cell 2 - 13
		for (int i = 1; i <= 4; i++) {
			
			// Vers
			addEnTeteCell2emeLigne("Vers", borderWidth4);
			
			// De
			addEnTeteCell2emeLigne("De", borderWidth2);
			
			// Solde
			addEnTeteCell2emeLigne("Solde", borderWidth2);	
		}
	}
	
	// ajout d'une cellule
	// ligne 1 de l'entête
	// ex. : Flux observés au RP 2007
	public void addEnTeteCell1ereLigne(String enTete) {
		
		PdfPCell cell = new PdfPCell(new Phrase(new Chunk(enTete, fontTableauBoldWhite))); cell.setPadding(padding);
		cell.setColspan(3);
		cell.setBackgroundColor(Color.GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
		cell.setBorderWidthLeft(borderWidth4); cell.setBorderWidthRight(0); cell.setBorderWidthTop(0); cell.setBorderWidthBottom(0);
		table.addCell(cell);
	}

	// ajout d'une cellule
	// ligne 2 de l'entête
	// ex. : Vers
	public void addEnTeteCell2emeLigne(
			String texte, 
			float borderWidthLeft) {
		
		PdfPCell cell = new PdfPCell(new Phrase(new Chunk(texte, fontBold))); cell.setPadding(padding);
		cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
		cell.setBorderWidthLeft(borderWidthLeft); cell.setBorderWidthRight(0); cell.setBorderWidthTop(borderWidthTop); cell.setBorderWidthBottom(0);
		table.addCell(cell);
	}
	
	// ajout d'une cellule gris clair sauf en-tête, première colonne
	public void addCell(
			String texte, 
			float borderWidthLeft, 
			float borderWidthTop) {
		
		PdfPCell cell = new PdfPCell(new Phrase(new Chunk(texte, fontTableau))); cell.setPadding(padding);
		cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER); 
		cell.setBorderWidthLeft(borderWidthLeft); cell.setBorderWidthRight(0); cell.setBorderWidthTop(borderWidthTop); cell.setBorderWidthBottom(0);
		table.addCell(cell);
	}
	
	/* cell avec le nom d'une zone d'échange*/
	public void addCellNomZoneEchange(
			String zoneId, // identifiant de la zone d'échange
			int j, // j-ème zone d'échange
			float borderWidthTop
							 ){

		PdfPCell cell = null;
		// projection utilisateur et zone d'échange hors zonage
		if (projectionUtilisateur.equals("1") && zonesEtudeZoneEch.get(zoneId).get(j).substring(0, 1).equals("_")) {
			cell = new PdfPCell(new Phrase(new Chunk(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(j)), fontTableau)
											));
			cell.setPadding(4);
		}
		// else
		else {
			cell = new PdfPCell(new Phrase(new Chunk(zoneLibelle.get(zonesEtudeZoneEch.get(zoneId).get(j)), fontTableauLink)
											.setLocalGoto("lien" + zonesEtudeZoneEch.get(zoneId).get(j))));
			cell.setPadding(4);
		}
		cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT); 
		cell.setBorderWidthLeft(0); cell.setBorderWidthRight(0); cell.setBorderWidthTop(borderWidthTop); cell.setBorderWidthBottom(0);
		table.addCell(cell);
	}
		
	/* cell "Toutes zones" */ 
	public void addCellToutesZones(){
		
		PdfPCell cell = new PdfPCell(new Phrase(new Chunk("Toutes zones", fontBold))); cell.setPadding(4);
		cell.setBackgroundColor(Color.LIGHT_GRAY); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT); 
		cell.setBorderWidthLeft(0); cell.setBorderWidthRight(0); cell.setBorderWidthTop(borderWidth4); cell.setBorderWidthBottom(0);
		table.addCell(cell);
	}

	// ajout d'un espacement après un tableau
	public void addEspacement(){
		
		PdfPCell cell = new PdfPCell(new Phrase(new Chunk("", fontTableau)));
		cell.setFixedHeight(height);
		cell.setBackgroundColor(Color.WHITE); cell.setBorderColor(Color.WHITE); cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		
		for(int i = 1 ; i <= nbColonnes; i++){
			table.addCell(cell);
		}
	}
	
	
	
}
