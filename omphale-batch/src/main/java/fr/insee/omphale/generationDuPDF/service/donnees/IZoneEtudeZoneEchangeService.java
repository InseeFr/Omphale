package fr.insee.omphale.generationDuPDF.service.donnees;

import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * liste pour chaque zone d'étude des zones d'échange principales
 * <br>
 * <br>
 * interface utilisée par {@link fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService}
 */
public interface IZoneEtudeZoneEchangeService {


	
	
	/**
	 * Recherche dans la base de donnée pour chaque zone d'étude les 5 zones d'échange les plus importantes
	 * @param beanParametresResultat {@link fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
	 * @return 
	 * HashMap&lt.identifiant de zone d'étude, List&lt.identifiant de zone d'échange&gt.&gt.
	 */
	public Map<String, List<String>> getHashMapZoneEtudeZoneEchange(
			BeanParametresResultat beanParametresResultat,
			Map<String, String> hashMap);
	
	/**
	 * Zones d'échange (ID_ZONE, libellé), qui apparaissent dans la liste pour chaque zone d'étude des 5 principales zones d'échange,
	 * et qui sont hors zonage
	 * @param idUser
	 * @param prefixe
	 * @return HashMap
	 * &lt.identifiant de zone d'échange, libellé de zone d'échange&gt.
	 */
	public Map<String, String> getHashMapZoneEchangeHorsZonage(
			String idUser, 
			String prefixe) ;
}
