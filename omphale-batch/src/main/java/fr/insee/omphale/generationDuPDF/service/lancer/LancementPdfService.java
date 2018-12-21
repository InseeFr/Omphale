package fr.insee.omphale.generationDuPDF.service.lancer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.util.HibernateUtil;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.domaine.geographie.Zonage;
import fr.insee.omphale.generationDuPDF.domaine.projection.Projection;
import fr.insee.omphale.generationDuPDF.exception.OmphaleConfigResultException;
import fr.insee.omphale.generationDuPDF.exception.OmphalePopulationNegativeException;
import fr.insee.omphale.generationDuPDF.exception.OmphaleResultException;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiqueDecPointService;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointCourbeService;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointFluxRangeService;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointFluxService;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointPopulationService;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointPyramideService;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointSoldeFluxService;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau1AgeMoyenService;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau1EDVService;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau1ICFService;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau1PopulationService;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau2FluxService;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau2TousAgesPlus5Service;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau2TousAgesPlus5ToutesZonesService;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau2ToutesZonesService;
import fr.insee.omphale.generationDuPDF.service.donnees.IZoneEtudeZoneEchangeService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.GraphiqueDecPointService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.GraphiquePointCourbeService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.GraphiquePointFluxRangeService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.GraphiquePointFluxService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.GraphiquePointPopulationService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.GraphiquePointPyramideService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.GraphiquePointSoldeFluxService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.Tableau1AgeMoyenService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.Tableau1EDVService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.Tableau1ICFService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.Tableau1PopulationService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.Tableau2FluxService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.Tableau2TousAgesPlus5Service;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.Tableau2TousAgesPlus5ToutesZonesService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.Tableau2ToutesZonesService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.ZoneEtudeZoneEchangeService;
import fr.insee.omphale.generationDuPDF.service.geographie.IZonageService;
import fr.insee.omphale.generationDuPDF.service.geographie.IZoneService;
import fr.insee.omphale.generationDuPDF.service.geographie.impl.ZonageService;
import fr.insee.omphale.generationDuPDF.service.geographie.impl.ZoneService;
import fr.insee.omphale.generationDuPDF.service.impl.Service;
import fr.insee.omphale.generationDuPDF.service.projection.IProjectionService;
import fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService;
import fr.insee.omphale.generationDuPDF.service.projection.impl.ProjectionService;

/**
 * Classe java qui permet de lancer le pdf.
 * <br>
 * classe testée dans {@link fr.insee.omphale.generationDuPDF.service.test.lancer.LancementPdfTest}
 * <br>
 * Etapes : 
 * <br>
 * 1. recherche des données utilisées pour faire le pdf
 * <br>
 * - des services cherchent les données essentiellement dans tables Oracle
 * <br>
 * - les données sont surtout des HashMap
 * <br>
 * 2. écriture dans le pdf
 * <br>
 * - on utilise la classe {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService}, méthode write,
 * qui crée le pdf à partir des données ci-dessus
 * @see fr.insee.omphale.core.result.service.test.lancer.LancementPdfServiceTest
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService
 */
public class LancementPdfService {


	

	// switch
	// public static boolean identifiant = true		: dans tables ZP_idep_CSV_.., identifiants des zones 
	// public static boolean identifiant = false 	: dans tables ZP_idep_CSV_.., noms des zones 
	public static boolean identifiant = false; 
	
	/**
	 * @param args
	 * @throws OmphalePopulationNegativeException 
	 */
	public void lancement(BeanParametresResultat beanParametresResultat) throws OmphaleConfigResultException, OmphaleResultException, OmphalePopulationNegativeException{
		
		/* 1. données utilisées pour faire le pdf */
		
		// services
		IZonageService zonageService = new ZonageService();
		IZoneService zonesService = new ZoneService();
		IProjectionService projectionService = new ProjectionService();
		IZoneEtudeZoneEchangeService zoneEtudeZoneEchangeService = new ZoneEtudeZoneEchangeService();
		// tableau Population, Indicateurs, Age moyen
		ITableau1PopulationService populationService = new Tableau1PopulationService();
		ITableau1EDVService eDVService = new Tableau1EDVService();
		ITableau1ICFService iCFService = new Tableau1ICFService();
		ITableau1AgeMoyenService ageMoyenService = new Tableau1AgeMoyenService();
		// tableau Flux avec les principales zones d'échange
		ITableau2TousAgesPlus5Service tousAgesPlus5Service = new Tableau2TousAgesPlus5Service();
		ITableau2FluxService fluxService = new Tableau2FluxService();
		ITableau2TousAgesPlus5ToutesZonesService tousAgesPlus5ToutesZonesService = new Tableau2TousAgesPlus5ToutesZonesService();
		ITableau2ToutesZonesService toutesZonesService = new Tableau2ToutesZonesService();
		// graphiques Pyramide des âges, Evolution de la population, Quotients de fécondité, Quotients de décès
		IGraphiquePointPyramideService pointPyramideService = new GraphiquePointPyramideService();
		IGraphiquePointPopulationService pointPopulationService = new GraphiquePointPopulationService();
		IGraphiquePointCourbeService pointCourbeService = new GraphiquePointCourbeService();
		IGraphiqueDecPointService decPointService = new GraphiqueDecPointService();
		// graphiques flux
		IGraphiquePointFluxService pointFluxService = new GraphiquePointFluxService();
		IGraphiquePointFluxRangeService pointFluxRangeService = new GraphiquePointFluxRangeService();
		IGraphiquePointSoldeFluxService pointSoldeFluxService = new GraphiquePointSoldeFluxService();
		ITextA1ItextService itextService = new ITextA1ItextService();
		
		// hashMap : va contenir :
		// - les années de début (ex. 2006), de début + 5 (ex. 2011), de fin -5 (ex. 2026), de fin (ex. 2031)
		// - des libellés "Flux observés au RP", "au RP"
		// - le libellé du zonage
		// - un indicateur si le zonage est un zonage utilisateur 
		// - le libellé de la projection
		// - la date d'exécution de la projection
		// - un indicateur si la projection est calée ou non
		Map<String, String> hashMap = new HashMap<String, String>();
		
		/* anneeDebut, anneeFin, anneeDebutPlus5, anneeFinMoins5 */
		String anneeDebut = beanParametresResultat.getAnneeReference();
		String anneeFin = beanParametresResultat.getAnneeHorizon();
		//variables utilisées pour le quinquenal
		String anneeDebutPlus5 = (Integer.valueOf(Integer.parseInt(anneeDebut) + 5)).toString();
		String anneeFinMoins5 = (Integer.valueOf(Integer.parseInt(anneeFin) - 5)).toString();
		//variables utilisées pour l'annuel
		String anneeDebutPlus1 = (Integer.valueOf(Integer.parseInt(anneeDebut) + 1)).toString();
		String anneeFinMoins1 = (Integer.valueOf(Integer.parseInt(anneeFin) - 1)).toString();
		
		hashMap.put("anneeDebut", anneeDebut); // ex. 2006
		hashMap.put("anneeDebutPlus5", anneeDebutPlus5); // ex. 2011
		hashMap.put("anneeFinMoins5", anneeFinMoins5); // ex. 2026
		hashMap.put("anneeFin", anneeFin); // ex. 2031
		hashMap.put("anneeDebutPlus1", anneeDebutPlus1); // ex. 2017
		hashMap.put("anneeFinMoins1", anneeFinMoins1); // ex. 2030
		
		/*********************************************************/
		
		// libellés "Flux observés au RP", "au RP"
		hashMap.put("fluxObserveEntre", "Flux observés entre ");
		hashMap.put("auRP", "au RP");
		
		try {
			Session hibernateSession = HibernateUtil.getSessionFactory().getCurrentSession();			
			hibernateSession.beginTransaction();			
			
			
			// init
			List<String> listeZones = null;
			Map<String, String> zoneLibelle = null;
			Map<String, List<String>> zonesEtudeZoneEch = null;
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesPyramide = null;
			Map<String, ArrayList<Double>> donneesPopulation = null;
			Map<String, ArrayList<Double>> donneesCourbe = null;
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesDec = null;
			Map<String, HashMap<Integer, ArrayList<Double>>> donneesSoldeFlux = null;
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxSortants = null;
			Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> donneesFluxEntr = null;
			Map<String, HashMap<String, Double>> donneesFluxRange = null;
			
			Map<String, Integer> populationAnneeDebut = null;
			Map<String, Integer> populationAnneeFin = null;
			Map<String, Double> populationEvol = null;
			Map<String, HashMap<Integer, Double>> eDV = null;
			Map<String, Double> iCF = null;
			Map<String, Double> ageMoyenAnnee1 = null;
			Map<String, Double> ageMoyenAnneeFin = null;
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeDebut = null;
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeDebutPlus5 = null;
			Map<String, HashMap<String, Double>> tousAgesPlus5VersAnneeFin = null;
			Map<String, HashMap<String, Double>> tousAgesPlus5Vers = null;
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeDebut = null;
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeDebutPlus5 = null;
			Map<String, HashMap<String, Double>> tousAgesPlus5DeAnneeFin = null;
			Map<String, HashMap<String, Double>> tousAgesPlus5De = null;
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeDebut = null;
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeDebutPlus5 = null;
			Map<String, HashMap<String, HashMap<String, Double>>> versAnneeFin = null;
			Map<String, HashMap<String, HashMap<String, Double>>> vers = null;
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeDebut = null;
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeDebutPlus5 = null;
			Map<String, HashMap<String, HashMap<String, Double>>> deAnneeFin = null;
			Map<String, HashMap<String, HashMap<String, Double>>> de = null;
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeDebut = null;
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeDebutPlus5 = null;
			Map<String, Double> tousAgesPlus5ToutesZonesVersAnneeFin = null;
			Map<String, Double> tousAgesPlus5ToutesZonesVers = null;
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeDebut = null;
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeDebutPlus5 = null;
			Map<String, Double> tousAgesPlus5ToutesZonesDeAnneeFin = null;
			Map<String, Double> tousAgesPlus5ToutesZonesDe = null;
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeDebut = null;
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeDebutPlus5 = null;
			Map<String, HashMap<String, Double>> toutesZonesVersAnneeFin = null;
			Map<String, HashMap<String, Double>> toutesZonesVers = null;
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeDebut = null;
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeDebutPlus5 = null;
			Map<String, HashMap<String, Double>> toutesZonesDeAnneeFin = null;
			Map<String, HashMap<String, Double>> toutesZonesDe = null;
			
			
			try {
				
				// libellé zonage		
				Zonage zonage = zonageService.findById(beanParametresResultat.getIdZonage());
				hashMap.put("zonageLibelle", zonage.getLibelle());	
				
				// projectionUtilisateur : "1" si idZonage == null || idZonage != 0
				// 						   "0" sinon
				hashMap.put("projectionUtilisateur", zonageService.isProjectionUtilisateur(beanParametresResultat.getIdZonage()));

				// libellé de la projection
				Projection projection = projectionService.findById(Integer.valueOf(beanParametresResultat.getIdProjection()));
				hashMap.put("projectionLibelle", projection.getLibelle());

				// date exécution projection
				DateFormat simpleDateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRENCH);
				hashMap.put("dateExec", simpleDateFormat.format(new Date()));
				
				// calage
				// projection calée : hashMap.get("calage") --> "cal"
				// projection non calée : hashMap.get("calage") --> "ncal"
				if (beanParametresResultat.getCalage() != null && beanParametresResultat.getCalage().equals("1")) {
					hashMap.put("calage", "cal");
				}
				else {
					hashMap.put("calage", "ncal");
				}

				/* pour chaque zone d'étude, les 5 zones d'échange les plus importantes
				 * <br>
				 * Ex. : 
				 * <br>
				 * zone_etude 464 (Paris)	
				 * <br>
				 * List liste = zonesEtudeZoneEch.get("464"); // 5 zones d'échange les plus importantes
				 * <br>
				 * liste.get(0) --> "481"; (Hauts de Seine)
				 * <br>
				 * liste.get(1) --> 
				 * <br>
				 * liste.get(2) --> 
				 * <br>
				 * liste.get(3) --> 
				 * <br>
				 * liste.get(4) --> 
				 * <br>
				 * <br>
				 * zone_etude 481 
				 * <br>
				 * liste = zonesEtudeZoneEch.get('481'); ; // 5 zones d'échange les plus importantes
				 * <br>
				 * liste.get(0) --> "464"
				 * <br>
				 * liste.get(1) --> 
				 * <br>
				 * liste.get(2) --> 
				 * <br>
				 * liste.get(3) --> 
				 * <br>
				 * liste.get(4) --> 
				 * <br>
				 * etc.
				 */
				// HashMap(identifiant de zone d'étude, identifiant de zone d'échange)
				zonesEtudeZoneEch = zoneEtudeZoneEchangeService.getHashMapZoneEtudeZoneEchange(
						beanParametresResultat, 
						hashMap);

				/* zoneLibelle : identifiants, libellés 
				   - des zones du zonage     
				   - et des zones d'échange hors zonage
				*/
				// ajout des identifiants, libellés des zones du zonage  
				// Ex. 
				// zoneLibelle.get("464") --> "Paris"
				// zoneLibelle.get("481") --> "Hauts de Seine"
				// etc. 
				//
				// HashMap(identifiant de zone, libellé de zone)
				zoneLibelle = zonesService.getZoneLibelle(zonage);
				
				// identifiants des zones du zonage      
				// par ordre alphabétique des libellé des zones
				// Ex. 
				// listeZones.get(0) --> "481"
				// listeZones.get(1) --> "464"
				listeZones = zonesService.getListeZones(zoneLibelle);

				// si zonage utilisateur
				if (hashMap.get("projectionUtilisateur").equals("1")) {
					// ajout à zoneLibelle des zones d'échange, qui apparaissent dans la liste pour chaque zone d'étude des 5 principales zones d'échange,
					// et qui sont hors zonage
					zonesService.add(
							beanParametresResultat.getIdUser(), 
							beanParametresResultat.getPrefixe(), 
							zoneLibelle);
				}
				
				// liste des zones d'étude   
				// utilisé dans requètes
				// Ex. : "('464', '481')"
				String zonesEtude = Service.tableauToString(zonesEtudeZoneEch.keySet());

				/* données graphique Pyramide des âges */
				// HashMap(zone, HashMap(série, ArrayList(nombre de personnes pour chaque âge)))
				donneesPyramide =
					pointPyramideService.getDonnees(
							beanParametresResultat, 
							hashMap, 
							listeZones);
				/* données graphique Evolution de la population */
				// HashMap(zone, ArrayList(population pour chaque année))
				donneesPopulation =
					pointPopulationService.getDonnees(
							beanParametresResultat, 
							hashMap, 
							listeZones);
				/* données graphique Quotients de fécondité */
				donneesCourbe =
					pointCourbeService.getDonnees(
							beanParametresResultat, 
							hashMap, 
							listeZones);
				/* données graphique Quotients de décès */
				donneesDec = 
					decPointService.getDonnees(
							beanParametresResultat, 
							hashMap, 
							listeZones);
				
				if (zonesEtude != null && !"()".equals(zonesEtude)){
					/* données graphiques flux */
					// Solde migratoire au RP
					// HashMap(zone, HashMap(sexe, ArrayList(solde migratoire au RP pour chaque âge)))
					donneesSoldeFlux =
						pointSoldeFluxService.getDonnees(
								beanParametresResultat, 
								listeZones, 
								hashMap, 
								zonesEtude);
					// flux sortants des zones d'étude vers les zones d'échange
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(sexe, ArrayList(flux sortant de la zone d'étude pour chaque âge))))
					donneesFluxSortants =
						pointFluxService.getDonnees(
								pointFluxService.getPointFluxSortant(
										beanParametresResultat, 
										hashMap.get("anneeDebut"), 
										zonesEtude),
								beanParametresResultat, 
								zonesEtudeZoneEch);
					// flux entrants des zones d'étude en provenance des zones d'échange
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(sexe, ArrayList(flux entrant de la zone d'étude pour chaque âge))))
					donneesFluxEntr =
						pointFluxService.getDonnees(
								pointFluxService.getPointFluxEntr(
										beanParametresResultat, 
										hashMap.get("anneeDebut"), 
										zonesEtude),
								beanParametresResultat, 
								zonesEtudeZoneEch);
					// range flux sortants, flux entrants
					// ces données permettent aux graphiques de flux qui sont en regard d'avoir le même range.
					// Ex. le graphique Flux de Ain vers Rhône est gradué de 0 à 750
					// et le graphique Flux de Rhône vers Ain est gradué aussi de 0 à 750
					// HashMap(zone d'étude, HashMap(zone d'échange, maximum(flux sortants et flux entrants))))
					donneesFluxRange = pointFluxRangeService.getRange(
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap, 
							zonesEtude);
				}

				/**********************/
				
				/* données tableau Population, Indicateurs, Age moyen */
				// HashMap(zone, population année de référence)
				
				populationAnneeDebut = populationService.getPopulation(
						beanParametresResultat, 
						hashMap,
						hashMap.get("anneeDebut"));
				// HashMap(zone, population année de fin)
				populationAnneeFin = populationService.getPopulation(
						beanParametresResultat, 
						hashMap,
						hashMap.get("anneeFin"));
				// HashMap(zone, évolution de la population)
				populationEvol = populationService.getPopulationEvol(
						populationAnneeDebut, 
						populationAnneeFin);
				// HashMap(zone, HashMap(sexe, eDV))
				eDV = eDVService.getEDV(
						beanParametresResultat, 
						hashMap, 
						listeZones);
				// HashMap(zone, iCF)
				iCF = iCFService.getICF(beanParametresResultat, hashMap, hashMap.get("anneeDebut"));
				// HashMap(zone, âge moyen année de référence)
				ageMoyenAnnee1 = ageMoyenService.getAgeMoyen(
						beanParametresResultat, 
						hashMap,
						hashMap.get("anneeDebut"));
				// HashMap(zone, âge moyen année de fin)
				ageMoyenAnneeFin = ageMoyenService.getAgeMoyen(
						beanParametresResultat, 
						hashMap,
						hashMap.get("anneeFin"));
				
				
				if (zonesEtude != null && !"()".equals(zonesEtude)){
					/* données tableau flux avec les principales zones d'échange */
					/* I. "1 ans et plus", par zone (ex. lignes Ain, Alpes Maritimes, ..) */
					// 1ère colonne Vers
					// HashMap(zone d'étude, HashMap(zone d'échange, flux))
																	// getDonnees : --> 	HashMap( HashMap(  ))
					tousAgesPlus5VersAnneeDebut = tousAgesPlus5Service.getDonnees(
											  // getVersAnneeDebut() : requete en base de données
							tousAgesPlus5Service.getVersAnneeDebut(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 2ème colonne Vers
					// HashMap(zone d'étude, HashMap(zone d'échange, flux))
					tousAgesPlus5VersAnneeDebutPlus5 = tousAgesPlus5Service.getDonnees(
							tousAgesPlus5Service.getVersAnneeDebutPlus5(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 3ème colonne Vers
					// HashMap(zone d'étude, HashMap(zone d'échange, flux))
					tousAgesPlus5VersAnneeFin = tousAgesPlus5Service.getDonnees(
							tousAgesPlus5Service.getVersAnneeFin(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 4ème colonne Vers (flux cumulés)
					// HashMap(zone d'étude, HashMap(zone d'échange, flux))
					tousAgesPlus5Vers = tousAgesPlus5Service.getDonnees(
							tousAgesPlus5Service.getVers(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 1ère colonne De
					// HashMap(zone d'étude, HashMap(zone d'échange, flux))
					tousAgesPlus5DeAnneeDebut = tousAgesPlus5Service.getDonnees(
							tousAgesPlus5Service.getDeAnneeDebut(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 2ème colonne De
					// HashMap(zone d'étude, HashMap(zone d'échange, flux))
					tousAgesPlus5DeAnneeDebutPlus5 = tousAgesPlus5Service.getDonnees(
							tousAgesPlus5Service.getDeAnneeDebutPlus5(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 3ème colonne De
					// HashMap(zone d'étude, HashMap(zone d'échange, flux))
					tousAgesPlus5DeAnneeFin = tousAgesPlus5Service.getDonnees(
							tousAgesPlus5Service.getDeAnneeFin(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 4ème colonne De (flux cumulés)
					// HashMap(zone d'étude, HashMap(zone d'échange, flux))
					tousAgesPlus5De = tousAgesPlus5Service.getDonnees(
							tousAgesPlus5Service.getDe(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);

				}

				/* II. "1 ans et plus", ligne "Toutes zones" */
				// 1ère colonne Vers
				// HashMap(zone d'étude, flux)
				tousAgesPlus5ToutesZonesVersAnneeDebut = tousAgesPlus5ToutesZonesService.getDonnees(
						tousAgesPlus5ToutesZonesService.getVersAnneeDebut(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 2ème colonne Vers
				// HashMap(zone d'étude, flux)
				tousAgesPlus5ToutesZonesVersAnneeDebutPlus5 = tousAgesPlus5ToutesZonesService.getDonnees(
						tousAgesPlus5ToutesZonesService.getVersAnneeDebutPlus5(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 3ème colonne Vers
				// HashMap(zone d'étude, flux)
				tousAgesPlus5ToutesZonesVersAnneeFin = tousAgesPlus5ToutesZonesService.getDonnees(
						tousAgesPlus5ToutesZonesService.getVersAnneeFin(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 4ème colonne Vers (flux cumulés)
				// HashMap(zone d'étude, flux)
				tousAgesPlus5ToutesZonesVers = tousAgesPlus5ToutesZonesService.getDonnees(
						tousAgesPlus5ToutesZonesService.getVers(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 1ère colonne De
				// HashMap(zone d'étude, flux)
				tousAgesPlus5ToutesZonesDeAnneeDebut = tousAgesPlus5ToutesZonesService.getDonnees(
						tousAgesPlus5ToutesZonesService.getDeAnneeDebut(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 2ème colonne De
				// HashMap(zone d'étude, flux)
				tousAgesPlus5ToutesZonesDeAnneeDebutPlus5 = tousAgesPlus5ToutesZonesService.getDonnees(
						tousAgesPlus5ToutesZonesService.getDeAnneeDebutPlus5(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 3ème colonne De
				// HashMap(zone d'étude, flux)
				tousAgesPlus5ToutesZonesDeAnneeFin = tousAgesPlus5ToutesZonesService.getDonnees(
						tousAgesPlus5ToutesZonesService.getDeAnneeFin(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 4ème colonne De (flux cumulés)
				// HashMap(zone d'étude, flux)
				tousAgesPlus5ToutesZonesDe = tousAgesPlus5ToutesZonesService.getDonnees(
						tousAgesPlus5ToutesZonesService.getDe(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				
				
				if (zonesEtude != null && !"()".equals(zonesEtude)){
					/* III. par tranches d'âge 1-24, 25-59, 60 et plus, par zone */
					// 1ère colonne Vers
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(tranche d'âge, flux)))
					versAnneeDebut = fluxService.getDonnees(
							fluxService.getVersAnneeDebut(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 2ème colonne Vers
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(tranche d'âge, flux)))
					versAnneeDebutPlus5 = fluxService.getDonnees(
							fluxService.getVersAnneeDebutPlus5(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 3ème colonne Vers
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(tranche d'âge, flux)))
					versAnneeFin = fluxService.getDonnees(
							fluxService.getVersAnneeFin(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 4ème colonne Vers (flux cumulés)
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(tranche d'âge, flux)))
					vers = fluxService.getDonnees(
							fluxService.getVers(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 1ère colonne De
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(tranche d'âge, flux)))
					deAnneeDebut = fluxService.getDonnees(
							fluxService.getDeAnneeDebut(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 2ème colonne De
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(tranche d'âge, flux)))
					deAnneeDebutPlus5 = fluxService.getDonnees(
							fluxService.getDeAnneeDebutPlus5(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 3ème colonne De
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(tranche d'âge, flux)))
					deAnneeFin = fluxService.getDonnees(
							fluxService.getDeAnneeFin(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);
					// 4ème colonne De (flux cumulés)
					// HashMap(zone d'étude, HashMap(zone d'échange, HashMap(tranche d'âge, flux)))
					de = fluxService.getDonnees(
							fluxService.getDe(beanParametresResultat, hashMap, zonesEtude),
							beanParametresResultat, 
							zonesEtudeZoneEch, 
							hashMap);

				}

				
				/* IV. par tranches d'âge 1-24, 25-59, 60 et plus, ligne "Toutes zones" */
				// 1ère colonne Vers
				// HashMap(zone d'étude, HashMap(tranche d'âge, flux))
				toutesZonesVersAnneeDebut = toutesZonesService.getDonnees(
						toutesZonesService.getVersAnneeDebut(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 2ème colonne Vers
				// HashMap(zone d'étude, HashMap(tranche d'âge, flux))
				toutesZonesVersAnneeDebutPlus5 = toutesZonesService.getDonnees(
						toutesZonesService.getVersAnneeDebutPlus5(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 3ème colonne Vers
				// HashMap(zone d'étude, HashMap(tranche d'âge, flux))
				toutesZonesVersAnneeFin = toutesZonesService.getDonnees(
						toutesZonesService.getVersAnneeFin(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 4ème colonne Vers (flux cumulés)
				// HashMap(zone d'étude, HashMap(tranche d'âge, flux))
				toutesZonesVers = toutesZonesService.getDonnees(
						toutesZonesService.getVers(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 1ère colonne De
				// HashMap(zone d'étude, HashMap(tranche d'âge, flux))
				toutesZonesDeAnneeDebut = toutesZonesService.getDonnees(
						toutesZonesService.getDeAnneeDebut(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 2ème colonne De
				// HashMap(zone d'étude, HashMap(tranche d'âge, flux))
				toutesZonesDeAnneeDebutPlus5 = toutesZonesService.getDonnees(
						toutesZonesService.getDeAnneeDebutPlus5(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 3ème colonne De
				// HashMap(zone d'étude, HashMap(tranche d'âge, flux))
				toutesZonesDeAnneeFin = toutesZonesService.getDonnees(
						toutesZonesService.getDeAnneeFin(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				// 4ème colonne De (flux cumulés)
				// HashMap(zone d'étude, HashMap(tranche d'âge, flux))
				toutesZonesDe = toutesZonesService.getDonnees(
						toutesZonesService.getDe(beanParametresResultat, hashMap),
						beanParametresResultat, 
						zonesEtudeZoneEch, 
						hashMap);
				/*******************/
			}
			catch(Exception exception) {
				throw new OmphaleResultException(exception);
			}
			//initialisation de messageAvertissement
			String messageAvertissement = "";
			//Rercherche s'il y a des populations négatives dans les données et envoie une exception si c'est le cas
            populationService.isPopulationNegativeException(
            		beanParametresResultat.getIdUser(), beanParametresResultat.getPrefixe(), hibernateSession,
            		itextService, beanParametresResultat,
            		listeZones, // zones du zonage  ex. listeZones.get(0) --> "92", listeZones.get(0) --> "75", etc
					zoneLibelle, // ex. zoneLibelle.get("75") --> "Paris"
					zonesEtudeZoneEch, // pour chaque zone d'étude, les 5 zones d'échange les plus importantes	
					hashMap, // contient les années de début (ex. 2006), de début + 5 (ex. 2011), de fin -5 (ex. 2026), de fin (ex. 2031), etc.
					// données graphique Pyramide des âges
					donneesPyramide, 
					// données graphique Evolution de la population
					donneesPopulation,
					// données graphique Quotients de fécondité
					donneesCourbe,
					// données graphique Quotients de décès
					donneesDec,
					// données graphique Solde migratoire au RP
					donneesSoldeFlux,
					// données flux sortants des zones d'étude vers les zones d'échange
					donneesFluxSortants,
					// données flux entrants des zones d'étude en provenance des zones d'échange
					donneesFluxEntr,
					// données flux range
					donneesFluxRange,
					// tableau Population, Indicateurs, Age moyen
					populationAnneeDebut,
					populationAnneeFin, 
					populationEvol,
					eDV,
					iCF,
					ageMoyenAnnee1,
					ageMoyenAnneeFin,
					// tableau Flux avec les principales zones d'échange
					tousAgesPlus5VersAnneeDebut,
					tousAgesPlus5VersAnneeDebutPlus5,
					tousAgesPlus5VersAnneeFin,
					tousAgesPlus5Vers,
					tousAgesPlus5DeAnneeDebut,
					tousAgesPlus5DeAnneeDebutPlus5,
					tousAgesPlus5DeAnneeFin,
					tousAgesPlus5De,
					versAnneeDebut,
					versAnneeDebutPlus5,
					versAnneeFin,
					vers,
					deAnneeDebut,
					deAnneeDebutPlus5,
					deAnneeFin,
					de,
					tousAgesPlus5ToutesZonesVersAnneeDebut,
					tousAgesPlus5ToutesZonesVersAnneeDebutPlus5,
					tousAgesPlus5ToutesZonesVersAnneeFin,
					tousAgesPlus5ToutesZonesVers,
					tousAgesPlus5ToutesZonesDeAnneeDebut,
					tousAgesPlus5ToutesZonesDeAnneeDebutPlus5,
					tousAgesPlus5ToutesZonesDeAnneeFin,
					tousAgesPlus5ToutesZonesDe,
					toutesZonesVersAnneeDebut,
					toutesZonesVersAnneeDebutPlus5,
					toutesZonesVersAnneeFin,
					toutesZonesVers,
					toutesZonesDeAnneeDebut,
					toutesZonesDeAnneeDebutPlus5,
					toutesZonesDeAnneeFin,
					toutesZonesDe,
					messageAvertissement
            		);
			
			hibernateSession.getTransaction().commit();
			
			/* 2. écriture dans le pdf */
			try{
				itextService.write(
						beanParametresResultat, // contient idUser, nomFichierPdf, etc.
						listeZones, // zones du zonage  ex. listeZones.get(0) --> "92", listeZones.get(0) --> "75", etc
						zoneLibelle, // ex. zoneLibelle.get("75") --> "Paris"
						zonesEtudeZoneEch, // pour chaque zone d'étude, les 5 zones d'échange les plus importantes	
						hashMap, // contient les années de début (ex. 2006), de début + 5 (ex. 2011), de fin -5 (ex. 2026), de fin (ex. 2031), etc.
						// données graphique Pyramide des âges
						donneesPyramide, 
						// données graphique Evolution de la population
						donneesPopulation,
						// données graphique Quotients de fécondité
						donneesCourbe,
						// données graphique Quotients de décès
						donneesDec,
						// données graphique Solde migratoire au RP
						donneesSoldeFlux,
						// données flux sortants des zones d'étude vers les zones d'échange
						donneesFluxSortants,
						// données flux entrants des zones d'étude en provenance des zones d'échange
						donneesFluxEntr,
						// données flux range
						donneesFluxRange,
						// tableau Population, Indicateurs, Age moyen
						populationAnneeDebut,
						populationAnneeFin, 
						populationEvol,
						eDV,
						iCF,
						ageMoyenAnnee1,
						ageMoyenAnneeFin,
						// tableau Flux avec les principales zones d'échange
						tousAgesPlus5VersAnneeDebut,
						tousAgesPlus5VersAnneeDebutPlus5,
						tousAgesPlus5VersAnneeFin,
						tousAgesPlus5Vers,
						tousAgesPlus5DeAnneeDebut,
						tousAgesPlus5DeAnneeDebutPlus5,
						tousAgesPlus5DeAnneeFin,
						tousAgesPlus5De,
						versAnneeDebut,
						versAnneeDebutPlus5,
						versAnneeFin,
						vers,
						deAnneeDebut,
						deAnneeDebutPlus5,
						deAnneeFin,
						de,
						tousAgesPlus5ToutesZonesVersAnneeDebut,
						tousAgesPlus5ToutesZonesVersAnneeDebutPlus5,
						tousAgesPlus5ToutesZonesVersAnneeFin,
						tousAgesPlus5ToutesZonesVers,
						tousAgesPlus5ToutesZonesDeAnneeDebut,
						tousAgesPlus5ToutesZonesDeAnneeDebutPlus5,
						tousAgesPlus5ToutesZonesDeAnneeFin,
						tousAgesPlus5ToutesZonesDe,
						toutesZonesVersAnneeDebut,
						toutesZonesVersAnneeDebutPlus5,
						toutesZonesVersAnneeFin,
						toutesZonesVers,
						toutesZonesDeAnneeDebut,
						toutesZonesDeAnneeDebutPlus5,
						toutesZonesDeAnneeFin,
						toutesZonesDe,
						messageAvertissement
								  );
			} catch(Exception exception){
				throw new OmphaleResultException(exception);
			}
			
			
		}
		catch(OmphaleResultException omphaleResultException) {
			throw omphaleResultException;
		} catch (OmphalePopulationNegativeException exception) {
			throw new OmphalePopulationNegativeException();   
		} catch(Exception exception) {
			throw new OmphaleConfigResultException();
		}
	}
	

}


