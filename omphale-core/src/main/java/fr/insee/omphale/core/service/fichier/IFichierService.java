package fr.insee.omphale.core.service.fichier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.core.service.geographie.ICommuneService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.domaine.Utilisateur;

/**
 * Cette interface gère toutes les fonctionnalités pour manipuler des fichiers csv
 * <BR>
 * utilisée lors des créations par import de Zonage ou de Zone
 *
 */
public interface IFichierService {
	/**
	 * Méthode qui contrôle un fichier contenant des codes commune.
	 * 
	 * @param fichierATraiter
	 * @return Map : la liste des valeurs non valides du fichier, vaut 'null' si
	 *         le fichier est correct. Sinon, les erreurs sont renvoyées dans
	 *         une map sous les clés suivantes :
	 *         <ul>
	 *         <li>erreursFormat : Liste de string contenant les erreurs de
	 *         format</li>
	 *         <li>erreursBase : Liste de string contenant les communes non
	 *         présentes en base</li>
	 *         <li>erreursPremiereLigne : Liste de string, vide s'il manque
	 *         l'en-tête "ng", remplie par les variables en trop si "ng" est
	 *         présent et qu'il y a d'autres en-têtes en trop</li>
	 *         </ul>
	 * @throws Exception 
	 */
	public Map<String, List<String>> controleFichierCommune(
			File fichierATraiter, ICommuneService communeService)
			throws IOException;

	/**
	 * Méthode qui contrôle un fichier contenant des noms et des libellés de
	 * zones (utilisé à la création d'un zonage)
	 * 
	 * @param fichierATraiter
	 * @param utilisateur
	 * @param zoneService
	 * @return Map : contenant un booléen indiquant la validité ou non du
	 *         fichier, ainsi que la liste des erreurs pour les tracer, sous les
	 *         clés suivantes :
	 *         <ul>
	 *         <li>fichierValide : booléen positionné à true si le fichier est
	 *         valide, false sinon</li>
	 *         <li>erreursFichier :</li> Map (clés = string) listant les
	 *         différentes erreurs du fichier sous les clés suivantes :
	 *         <ul>
	 *         <li>erreursVariablesManquantes : liste de string, chaque string
	 *         correspondant à une variable attendue et non présente dans le
	 *         fichier</li>
	 *         <li>erreursVariablesEnTrop : liste de string, chaque string
	 *         correspondant à une variable non attendue mais présente dans le
	 *         fichier</li>
	 *         <li>erreursFormat : liste de string, chaque string correspondant
	 *         à un nom de zone mal formaté</li>
	 *         <li>erreursBase : liste de string, chaque string correspondant à
	 *         un nom de zone déjà existant en base pour cet utilisateur</li>
	 *         </ul>
	 *         </ul>
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception 
	 */
	public Map<String, Object> controleFichierZone(File fichierATraiter,
			Utilisateur utilisateur, IZoneService zoneService)
			throws IOException;

	/**
	 * Méthode qui contrôle un fichier contenant des noms et des libellés de
	 * zones (utilisée à la création d'un zonage)
	 * 
	 * @param fichierATraiter
	 * @param zones
	 *            une map (clé = nomZone, value = libelleZone) stockant les
	 *            valeurs du fichier de zones analysé juste avant
	 * @param communeService
	 * @return Map : contenant un booléen indiquant la validité ou non du
	 *         fichier, ainsi que la liste des erreurs pour les tracer, sous les
	 *         clés suivantes :
	 *         <ul>
	 *         <li>fichierValide : booléen positionné à true si le fichier est
	 *         valide, false sinon</li>
	 *         <li>erreursFichier :</li> Map (clés = string) listant les
	 *         différentes erreurs du fichier sous les clés suivantes :
	 *         <ul>
	 *         <li>erreursVariablesManquantes : liste de string, chaque string
	 *         correspondant à une variable attendue et non présente dans le
	 *         fichier</li>
	 *         <li>erreursVariablesEnTrop : liste de string, chaque string
	 *         correspondant à une variable non attendue mais présente dans le
	 *         fichier</li>
	 *         <li>erreursFormat : liste de string, chaque string correspondant
	 *         à un code commune mal formaté</li>
	 *         <li>erreursBase : liste de string, chaque string correspondant à
	 *         un code commune non présent en base</li>
	 *         <li>erreursNomZone : liste de string, chaque string correspondant
	 *         à un nom de zone non présent dans le premier fichier (fichier de
	 *         zones)</li>
	 *         </ul>
	 *         </ul>
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception 
	 */
	public Map<String, Object> controleFichierCommunesDeZones(
			File fichierATraiter, Map<String, String> zones,
			ICommuneService communeService) throws 
			IOException;

	/**
	 * Méthode qui lit le fichier contenant les codes communes et stocke dans
	 * une collection.
	 * 
	 * @param fichierATraiter
	 * @return List(String)
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception 
	 */
	public List<String> lireDonneesFichier(File fichierATraiter)
			throws IOException;

}
