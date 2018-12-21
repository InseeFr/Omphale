package fr.insee.omphale.generationDuPDF.service.donnees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * Recherche des données dans la base de données, pour les graphiques Flux de &nbsp;&nbsp;&nbsp;&nbsp; vers &nbsp;&nbsp;&nbsp;&nbsp;
 * <br>
 * <br>
 * interface utilisée par {@link fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService}
 */
public interface IGraphiquePointFluxService {


	/**
	 * Recherche des flux sortants dans la base de données, pour les graphiques Flux de .. vers ..
	 * @param beanParametresResultat {@link fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
	 * @param String anneeDebut
	 * @param zonesEtude liste des zones d'étude. Ex. : "('464', '481')"
	 * @return
	 */
	public List<Object[]> getPointFluxSortant(
			BeanParametresResultat beanParametresResultat, 
			String anneeDebut,
			String zonesEtude);
	
	/**
	 * Recherche des flux entrants dans la base de données, pour les graphiques Flux de .. vers ..
	 * @param beanParametresResultat {@link fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
	 * @param String anneeDebut
	 * @param zonesEtude liste des zones d'étude. Ex. : "('464', '481')"
	 * @return
	 */
	public List<Object[]> getPointFluxEntr(
			BeanParametresResultat beanParametresResultat, 
			String anneeDebut,
			String zonesEtude);
	
	/**
	 * Recherche des données dans la base de données, pour les graphiques Flux de .. vers ..
	 * @param liste flux sortants ou flux entrants (getPointFluxSortant(), getPointFluxEntr())
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
	 * @return HashMap(zone d'étude, HashMap(zone d'échange, HashMap(sexe, ArrayList(flux sortant de la zone d'étude pour chaque âge))))
	 * <br>
	 * ou HashMap(zone d'étude, HashMap(zone d'échange, HashMap(sexe, ArrayList(flux entrants de la zone d'étude pour chaque âge))))
	 */
	public Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> getDonnees(
			List<Object[]> liste,
			BeanParametresResultat beanParametresResultat,
			Map<String, List<String>> zonesEtudeZoneEch);
}
