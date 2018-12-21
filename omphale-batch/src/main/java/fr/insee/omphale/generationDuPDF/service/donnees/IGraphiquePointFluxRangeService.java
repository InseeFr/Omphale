package fr.insee.omphale.generationDuPDF.service.donnees;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * Recherche des range dans la base de données, pour les graphiques Flux de &nbsp;&nbsp;&nbsp;&nbsp; vers &nbsp;&nbsp;&nbsp;&nbsp;
 * <br>
 * ces données permettent aux graphiques de flux qui sont en regard d'avoir le même range
 * <br>
 * Ex. le graphique Flux de Ain vers Rhône est gradué de 0 à 750
 * et le graphique Flux de Rhône vers Ain est gradué aussi de 0 à 750
 * <br>
 * interface utilisée par {@link fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService}
 */
public interface IGraphiquePointFluxRangeService {

	/**
	 * Recherche des range dans la base de données, pour les graphiques Flux de .. vers ..
	 * @param beanParametresResultat {@link fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
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
	 * @param zonesEtude liste des zones d'étude. Ex. : "('464', '481')"
	 * @return HashMap(zone d'étude, HashMap(zone d'échange, maximum(flux sortants et flux entrants)))
	 */
	public Map<String, HashMap<String, Double>> getRange(
			BeanParametresResultat beanParametresResultat,
			Map<String, List<String>> zonesEtudeZoneEch,
			Map<String, String> hashMap,
			String zonesEtude);
		
}
