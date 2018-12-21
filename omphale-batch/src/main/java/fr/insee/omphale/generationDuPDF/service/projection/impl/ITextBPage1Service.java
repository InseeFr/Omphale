package fr.insee.omphale.generationDuPDF.service.projection.impl;


import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * Ecriture de la première page du Pdf.
 * <br>
 * classe utilisée dans {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}
 * au cours de l'écriture du Pdf
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class ITextBPage1Service {
	

	/**
	 * Ecriture de la première page du Pdf
	 * @param document le document dans lequel écrire
	 * @param font HashMap qui contient les Font utilisées 
	 * @param hashMap contient :
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * - les années de début (ex. 2006), de début + 5 (ex. 2011), de fin -5 (ex. 2026), de fin (ex. 2031)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * - des libellés "Flux observés au RP", "au RP"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * - le libellé du zonage
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * - un indicateur si le zonage est un zonage utilisateur 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * - le libellé de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * - la date d'exécution de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * - un indicateur si la projection est calée ou non
	 * @throws Exception
	 */
	public void setPage1(
			Document document,			
			Map<String, Font> font,
			Map<String, String> hashMap,
			BeanParametresResultat beanParametresResultat,
			String messageAvertissement
				) throws Exception {
		
		Font fontTitre = font.get("fontTitre");
		Font fontSousTitre = font.get("fontSousTitre");
		
		//titre :
		Paragraph paragraph = new Paragraph();
		if(StringUtils.isNotBlank(messageAvertissement)) {
			//avertissement
			
			paragraph.add(new Chunk(messageAvertissement, fontTitre).setLocalDestination("liste"));
			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
			paragraph.setSpacingAfter(20); 
			document.add(paragraph);
			//titre
			paragraph = new Paragraph();
			paragraph.add(new Chunk("Omphale 2017", fontTitre));
			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
			paragraph.setSpacingAfter(20); 
			document.add(paragraph); 
		}else {
			//titre
			paragraph.add(new Chunk("Omphale 2017", fontTitre).setLocalDestination("liste"));
			paragraph.setAlignment(Paragraph.ALIGN_CENTER);
			paragraph.setSpacingAfter(20); 
			document.add(paragraph); 
		}
		
		
		paragraph = new Paragraph();	
		if (hashMap.get("zonageLibelle") != null) {
			paragraph.add(new Chunk(hashMap.get("zonageLibelle"), fontSousTitre));
		}
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		paragraph.setSpacingAfter(20); 
		document.add(paragraph); 
		
		StringBuffer str = new StringBuffer();
		str.append("Projection ");
		str.append(hashMap.get("anneeDebut"));
		str.append(" - ");
		str.append(hashMap.get("anneeFin"));
		paragraph = new Paragraph();		
		paragraph.add(new Chunk(str.toString(), fontSousTitre));
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		paragraph.setSpacingAfter(20); 
		document.add(paragraph); 
		
		paragraph = new Paragraph();		
		if (hashMap.get("projectionLibelle") != null) {
			paragraph.add(new Chunk(hashMap.get("projectionLibelle"), fontSousTitre));
		}
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		paragraph.setSpacingAfter(20); 
		document.add(paragraph); 
		
		paragraph = new Paragraph();
		if (hashMap.get("dateExec") != null){
			str = new StringBuffer();
			str.append("Réalisée le ");
			str.append(hashMap.get("dateExec"));		
			paragraph.add(new Chunk(str.toString(), fontSousTitre));
		}
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph); 
		
		paragraph = new Paragraph();
		if (beanParametresResultat.getFicheIdentite() != null){		
			paragraph.add(new Chunk(" ", fontSousTitre));
		}
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph); 
		
		paragraph = new Paragraph();
		if (beanParametresResultat.getFicheIdentite() != null){		
			paragraph.add(new Chunk(beanParametresResultat.getFicheIdentite(), fontSousTitre));
		}
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph); 

	}
}
