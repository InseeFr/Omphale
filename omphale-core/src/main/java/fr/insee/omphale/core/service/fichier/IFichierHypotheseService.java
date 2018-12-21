package fr.insee.omphale.core.service.fichier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.domaine.projection.Hypothese;
/**
 * Interface qui gère toutes les fonctionnalités pour manipuler un fichier Hypothese
 *
 */
public interface IFichierHypotheseService {

	/**
	 * Méthode qui contrôle un fichier contenant des informations pour une
	 * hypothese.
	 * 
	 * @param fichierATraiter
	 * @return Map : contenant un booléen indiquant la validité ou non du
	 *         fichier, la liste des valeurs du fichier s'il ne contient aucune
	 *         erreur de format, ainsi que la liste des erreurs sinon pour les
	 *         tracer, sous les clés suivantes :
	 *         <ul>
	 *         <li>fichierValide : booléen positionné à true si le fichier est
	 *         valide, false sinon</li>
	 *         <li>valeursFichier : non-null uniquement lorsque le fichier ne
	 *         contient pas d'erreur de format, Map contenant toutes les valeurs
	 *         du fichier sous la forme suivante :
	 *         Map(annee,Map(age,Map(sexe,valeur))) - age et sexe sont
	 *         positionnés à 0 lorsque non requis par le type d'entité</li>
	 *         <li>erreursVariablesManquantes : liste de string, chaque string
	 *         correspondant à une variable attendue et non présente dans le
	 *         fichier</li>
	 *         <li>erreursVariablesEnTrop : liste de string, chaque string
	 *         correspondant à une variable non attendue mais présente dans le
	 *         fichier</li>
	 *         <li>erreursFormat :</li> Map (clés = string) listant les
	 *         différentes erreurs de format du fichier sous les clés suivantes
	 *         :
	 *         <ul>
	 *         <li>erreursFormatAnnee : erreurs de format sur les années :
	 *         numéro de ligne + valeur</li>
	 *         <li>erreursFormatAge : erreurs de format sur les ages : numéro de
	 *         ligne + valeur</li>
	 *         <li>erreursFormatSexe : erreurs de format sur les sexes : numéro
	 *         de ligne + valeur</li>
	 *         <li>erreursFormatValeur : erreurs de format sur les valeurs :
	 *         numéro de ligne + valeur</li>
	 *         </ul>
	 *         <li>erreursCoherence : Map (clés = string) listant les erreurs de
	 *         cohérence de la base sous les clés suivantes :
	 *         <ul>
	 *         <li>erreursCoherenceDoublon : donne les doublons (numéro de ligne
	 *         + valeur)</li>
	 *         <li>erreursCoherenceExhaustivite : indique les valeurs ou couples
	 *         de valeurs manquantes lorsque le type d'entite requiers
	 *         l'exhaustivite</li>
	 *         </ul>
	 *         </li> </ul>
	 * @throws Exception 
	 */
	public Map<String, Object> controleFichierHypothese(File fichierATraiter,
			Hypothese hypothese) throws FileNotFoundException, IOException, Exception;

	/**
	 * Méthode qui teste que l'intégralité des croisements est présente dans les
	 * valeurs d'un fichier. Les plages d'exhaustivité sont déterminées gràce à
	 * l'argument hypothese
	 * 
	 * @param valeurs
	 *            valeurs du fichier sous la forme
	 *            Map(annee,Map(age,Map(sexe,valeur))) - age et sexe peuvent
	 *            valoir 0 si le type d'entité n'en a pas besoin
	 * @param hypothese
	 *            l'hypothese qui permet d'indiquer via anneeDebut, anneeFin,
	 *            ageDebut, ageFin, sexeDebut et sexeFin les plages sur
	 *            lesquelles l'exhaustivité est testée, si ces attributs sont
	 *            null, on teste l'exhaustivité de la valeur minimale à la
	 *            valeur maxmimale contenues dans le fichier
	 * @return la liste des croisements manquants dans le fichier, sous la forme
	 *         d'une liste de string
	 */
	public List<String> testExhaustivite(
			Map<Integer, Map<Integer, Map<Integer, Double>>> valeurs,
			Hypothese hypothese);
}
