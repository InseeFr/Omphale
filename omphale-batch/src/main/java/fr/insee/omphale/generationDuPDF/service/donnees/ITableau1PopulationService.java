package fr.insee.omphale.generationDuPDF.service.donnees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.exception.OmphalePopulationNegativeException;
import fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService;

/**
 * Recherche des données dans la base de données, pour le tableau Population, Indicateurs, Age moyen
 * <br>
 * données population
 * <br>
 * interface utilisée par {@link fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService}
 */
public interface ITableau1PopulationService {

	/**
	 * Recherche des données dans la base de données, pour le tableau Population, Indicateurs, Age moyen
	 * <br>
	 * données population
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
	 * @param zoneLibelle libellés des zones du zonage et des zones d'échange hors zonage
	 * 		<br>     
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		Ex. 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		zoneLibelle.get("75") --&gt. "Paris"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		etc.
	 * @return HashMap(zone, population)
	 */
	public Map<String, Integer> getPopulation(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String annee);
	
	/**
	 * Recherche des données dans la base de données, pour le tableau Population, Indicateurs, Age moyen
	 * <br>
	 * données évolution population
	 * @param populationAnneeDebut 
	 * @param populationAnneeFin 
	 * @return HashMap(zone, évolution de la population)
	 */
	public Map<String, Double> getPopulationEvol(
			Map<String, Integer> populationAnneeDebut,
			Map<String, Integer> populationAnneeFin);
	
	/**
	 * Compte le nombre de populations négatives dans les données csv sur les populations cal, ncal, act et men
	 * 
	 * @param idUser, prefixe et beanParametresResultat
	 * @return nombre de populations négatives
	 */
	public int compterPopulationNegative(String idUser, String prefixe, BeanParametresResultat beanParametresResultat);
	
	/**
	 * Compte le nombre de populations négatives dans les données csv sur les populations cal
	 * 
	 * @param idUser et prefixe
	 * @return nombre de populations négatives
	 */
	public int compterPopulationNegativeCal(String idUser, String prefixe);
	
	/**
	 * Compte le nombre de populations négatives dans les données csv sur les populations ncal
	 * 
	 * @param idUser et prefixe
	 * @return nombre de populations négatives
	 */
	public int compterPopulationNegativeNcal(String idUser, String prefixe);
	
	/**
	 * Compte le nombre de populations négatives dans les données csv sur les populations men
	 * 
	 * @param idUser et prefixe
	 * @return nombre de populations négatives
	 */
	public int compterPopulationNegativeMen(String idUser, String prefixe);
	
	/**
	 * Compte le nombre de populations négatives dans les données csv sur les populations act
	 * 
	 * @param idUser et prefixe
	 * @return nombre de populations négatives
	 */
	public int compterPopulationNegativeAct(String idUser, String prefixe);
	
	/**
	 * Détermine s'il existe ou non des populations négatives dans les données
	 * 
	 * @param idUser, prefixe et beanParametresResultat
	 * @return vrai s'il existe des populations négatives dans les données, false sinon
	 */
	public boolean isPopulationNegative(String idUser, String prefixe, BeanParametresResultat beanParametresResultat);
	
	/**
	 * renvoie si nécessaire une exception PopulationNegativeException en cas d'existence de population négative
	 * 
	 * @param idUser et prefixe
	 * @param hibernateSession 
	 * 
	 */
	public void isPopulationNegativeException(String idUser, String prefixe, Session hibernateSession,
			ITextA1ItextService textService, // zones du zonage  ex. listeZones.get(0) --> "92", listeZones.get(0) --> "75", etc
			BeanParametresResultat beanParametresResultat,
			List<String> listeZones,
			Map<String, String> zoneLibelle,
			Map<String, List<String>> zonesEtudeZoneEch,
			Map<String, String> hashMap,
			// graphiques
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesPyramide,
			Map<String, ArrayList<Double>> donneesPopulation,
			Map<String, ArrayList<Double>> donneesCourbe,
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesDec,
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesSoldeFlux,
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxSortants,
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxEntr,
			Map<String, HashMap<String, Double>> donneesFluxRange,
			// tableaux
			Map<String, Integer> populationAnneeDebut, 
			Map<String, Integer> populationAnneeFin, 
			Map<String, Double> populationEvol,
			Map<String, HashMap<Integer, Double>> eDV,
			Map<String, Double> iCF,
			Map<String, Double> ageMoyenAnneeDebut, 
			Map<String, Double> ageMoyenAnneeFin, 
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
			Map<String, HashMap<String, Double>> toutesZonesDe,
			String messageAvertissement
			) throws OmphalePopulationNegativeException, Exception;

	/**
	 * Renvoie 0 si la table prefixe_iduser_csv_population_act n'existe pas ou un 1 si elle existe
	 * 
	 * @param idUser et prefixe
	 * @return nombre de tables existantes se nommant prefixe_iduser_csv_population_act
	 */
	int isPopulationAct(String idUser, String prefixe);

	/**
	 * Renvoie 0 si la table prefixe_iduser_csv_population_men n'existe pas ou un 1 si elle existe
	 * 
	 * @param idUser et prefixe
	 * @return nombre de tables existantes se nommant prefixe_iduser_csv_population_men
	 */
	int isPopulationMen(String idUser, String prefixe);

	/**
	 * Renvoie 0 si la table prefixe_iduser_csv_population_cal n'existe pas ou un 1 si elle existe
	 * 
	 * @param idUser et prefixe
	 * @return nombre de tables existantes se nommant prefixe_iduser_csv_population_cal
	 */
	int isPopulationCal(String idUser, String prefixe);

	/**
	 * Renvoie 0 si la table prefixe_iduser_csv_population_ncal n'existe pas ou un 1 si elle existe
	 * 
	 * @param idUser et prefixe
	 * @return nombre de tables existantes se nommant prefixe_iduser_csv_population_ncal
	 */
	int isPopulationNcal(String idUser, String prefixe);
	
}
