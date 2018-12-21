package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1PopulationTableauDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.exception.OmphalePopulationNegativeException;
import fr.insee.omphale.generationDuPDF.exception.OmphaleResultException;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau1PopulationService;
import fr.insee.omphale.generationDuPDF.service.projection.impl.ITextA1ItextService;


public class Tableau1PopulationService implements ITableau1PopulationService {

	private ITableau1PopulationTableauDAO populationTableauDao = daoFactoryPDF.getPopulationTableauDAO();

	// ex.
	// List<Object[]> list = getZonesPopulation(..
	// liste.get(0) tableau d'Object
	// liste.get(0)[0] --> identifiant de zone d'étude
	// liste.get(0)[1] --> population
	private List<Object[]> getZonesPopulation(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String annee) {	
		return populationTableauDao.getPopulation(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				hashMap.get("calage"),
				annee);
	}
	
	// crée un Map<String, Double>
	// keys : identifiants des zones d'étude
	// values : population
	public Map<String, Integer> getPopulation(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String annee) {
		
		HashMap<String, Integer> hashMap1 = new HashMap<String, Integer>();

		for(Object[] object: getZonesPopulation(
									beanParametresResultat, 
									hashMap,
									annee)) {
			
			// object[0] --> identifiant de zone d'étude
			// object[1] --> population
			

			hashMap1.put((String) object[0], ((BigDecimal) object[1]).intValue());
		}
		
		return hashMap1;
	}
	
	// crée un Map<String, Double>
	// keys : identifiants des zones d'étude
	// values : évolution de la population
	public Map<String, Double> getPopulationEvol(
			Map<String, Integer> populationAnneeDebut,
			Map<String, Integer> populationAnneeFin
			) {
		
		HashMap<String, Double> hashMap1 = new HashMap<String, Double>();

		// boucle
		for(String zoneEtude: populationAnneeFin.keySet()) {
			
			hashMap1.put(zoneEtude, (new Double(populationAnneeFin.get(zoneEtude)))/(new Double(populationAnneeDebut.get(zoneEtude))) - 1);
		}
		
		return hashMap1;
	}
	
	@Override
	public int compterPopulationNegativeCal(String idUser, String prefixe) {
		
		return populationTableauDao.compterPopulationNegativeCal(idUser, prefixe);
	}

	@Override
	public int compterPopulationNegativeNcal(String idUser, String prefixe) {
		return populationTableauDao.compterPopulationNegativeNcal(idUser, prefixe);
	}

	@Override
	public int compterPopulationNegativeMen(String idUser, String prefixe) {
		return populationTableauDao.compterPopulationNegativeMen(idUser, prefixe);
	}

	@Override
	public int compterPopulationNegativeAct(String idUser, String prefixe) {
		return populationTableauDao.compterPopulationNegativeAct(idUser, prefixe);
	}
	
	@Override
	public int isPopulationAct(String idUser, String prefixe) {
		return populationTableauDao.isPopulationAct(idUser, prefixe);
	}
	
	@Override
	public int isPopulationMen(String idUser, String prefixe) {
		return populationTableauDao.isPopulationMen(idUser, prefixe);
	}
	
	@Override
	public int isPopulationCal(String idUser, String prefixe) {
		return populationTableauDao.isPopulationCal(idUser, prefixe);
	}
	
	@Override
	public int isPopulationNcal(String idUser, String prefixe) {
		return populationTableauDao.isPopulationNcal(idUser, prefixe);
	}
	
	

	@Override
	public int compterPopulationNegative(String idUser, String prefixe, BeanParametresResultat beanParametresResultat) {

		int popNegNcal = 0;
		int popNegMen = 0;
		int popNegAct = 0;
		int popNegCal = 0;
		
		
		if(isPopulationCal(idUser, prefixe)>0) {
			popNegNcal = compterPopulationNegativeNcal(idUser, prefixe);
		}
		
		if(isPopulationMen(idUser, prefixe)>0) {
			popNegMen = compterPopulationNegativeMen(idUser, prefixe);
		}
		
		if(isPopulationAct(idUser, prefixe)>0) {
			popNegAct = compterPopulationNegativeAct(idUser, prefixe);
		}
		

		//La table cal n'existe que s'il y a un calage :
		if (beanParametresResultat.getCalage().equals("1") && isPopulationCal(idUser, prefixe)>0) {
			popNegCal = compterPopulationNegativeCal(idUser, prefixe);
		}
		
		int popNegTotal = popNegAct + popNegCal + popNegNcal + popNegMen;
		
		return popNegTotal;
	}

	@Override
	public boolean isPopulationNegative(String idUser, String prefixe, BeanParametresResultat beanParametresResultat) {

		if ( compterPopulationNegative(idUser, prefixe, beanParametresResultat) > 0 ) {
			return true;
		} else {
			return false;
		}
		
	}

	@Override
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
			) throws OmphalePopulationNegativeException, Exception {

		if (isPopulationNegative(idUser, prefixe, beanParametresResultat)) {
			
			//Modification du titre du pdf pour signaler une population négative (on enlève .pdf pour remplacer par _erreur.pdf)
			beanParametresResultat.setNomFichierPdf(beanParametresResultat.getNomFichierPdf().substring(0, beanParametresResultat.getNomFichierPdf().length()-4)+"_erreur.pdf");
			//message d'avertissement du pdf pour alerter des populations négatives
			messageAvertissement = "Avertissement : présence de populations négatives dans les données";
			try {
				textService.write(
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
					ageMoyenAnneeDebut,
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
			hibernateSession.getTransaction().commit();
			throw new OmphalePopulationNegativeException();
		}
		
	}
}
