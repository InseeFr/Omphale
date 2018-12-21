package fr.insee.omphale.generationDuPDF.service.donnees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * Recherche des données dans la base de données, pour le graphique Solde migratoire au RP
 * <br>
 * interface utilisée par {@link fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService}
 */
public interface IGraphiquePointSoldeFluxService {

	
	/**
	 * Recherche des données dans la base de données, pour le graphique Solde migratoire au RP
	 * @param beanParametresResultat {@link fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
	 * @param listeZones liste des identifiants des zones du zonage
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
	 * @return HashMap(zone, HashMap(sexe, ArrayList(solde migratoire au RP pour chaque âge)))
	 */
	public Map<String, HashMap<Integer, ArrayList<Double>>> getDonnees(
			BeanParametresResultat beanParametresResultat,
			List<String> listeZones, 
			Map<String, String> hashMap,
			String zonesEtude);
}
