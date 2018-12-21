package fr.insee.omphale.generationDuPDF.service.donnees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * Recherche des données dans la base de données, pour le graphique Pyramide des âges
 * <br>
 * interface utilisée par {@link fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService}
 */
public interface IGraphiquePointPyramideService {


	
	/**
	 * Recherche des données dans la base de données, pour le graphique Pyramide des âges
	 * @param beanParametresResultat {@link fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat}
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		contient idUser, nomFichierPdf, etc.
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
	 * @param listeZones liste des identifiants des zones du zonage
	 * @return HashMap(zone, HashMap(série, ArrayList(nombre de personnes pour chaque âge)))
	 * <br>
	 * <br>
	 * série : 
	 * <br>
	 * série = 0 : Hommes, année de référence
	 * <br>
	 * série = 1 : Femmes, année de référence
	 * <br>
	 * série = 2 : Hommes, année de référence + 5
	 * <br>
	 * série = 3 : Hommes, année de fin
	 * <br>
	 * série = 4 : Femmes, année de référence + 5
	 * <br>
	 * série = 5 : Femmes, année de fin
	 */
	public Map<String, HashMap<Integer, ArrayList<Double>>> getDonnees(
						BeanParametresResultat beanParametresResultat,
						Map<String, String> hashMap,
						List<String> listeZones);
}
