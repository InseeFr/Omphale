package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePointFluxRangeDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointFluxRangeService;

public class GraphiquePointFluxRangeService implements IGraphiquePointFluxRangeService {


	
	private IGraphiquePointFluxRangeDAO pointFluxRangeDao = daoFactoryPDF.getPointFluxRangeDAO();

	private GraphiqueService graphiqueService = new GraphiqueService();
	
	
	// zonesEtude :  ex. "('400', '410')"
	// ex.
	// List<Object[]> list = getPointFluxSortantRange(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> zone d'échange
	// liste.get(0)[2] --> flux_max sortant
	private List<Object[]> getPointFluxSortantRange(
			BeanParametresResultat beanParametresResultat, 
			String anneeDebut,
			String zonesEtude) {	
		
		List<Object[]> liste = pointFluxRangeDao.getPointFluxSortantRange(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				anneeDebut, 
				beanParametresResultat.getAge100(),
				zonesEtude);
		
		return liste;
	}
	
	// zonesEtude :  ex. "('400', '410')"
	// ex.
	// List<Object[]> list = getPointFluxSortantRange(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> zone d'échange
	// liste.get(0)[2] --> flux_max entr
	private List<Object[]> getPointFluxEntrRange(
			BeanParametresResultat beanParametresResultat, 
			String anneeDebut,
			String zonesEtude) {	
		
		List<Object[]> liste = pointFluxRangeDao.getPointFluxEntrRange(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				anneeDebut, 
				beanParametresResultat.getAge100(),
				zonesEtude);

		return liste;
	}
	
	// crée un Map<String, HashMap<String, Double>>
	// ce Map contient :
	// keys : identifiants des zones d'étude
	// values : HashMap<String, Double>
	// ces HashMap contiennent :
	// keys des HashMap : identifiants des zones d'échange
	// values des HashMap : range
	public Map<String, HashMap<String, Double>> getRange(
								BeanParametresResultat beanParametresResultat,
								Map<String, List<String>> zonesEtudeZoneEch,
								Map<String, String> hashMap,
								String zonesEtude) {
		
		// crée un Map et l'initialise
		// les range sont égal à 0
		Map<String, HashMap<String, Double>> hashMap1 = graphiqueService.initRange(zonesEtudeZoneEch); 
		
		// requête flux sortant
		List<Object[]> liste = getPointFluxSortantRange(
				beanParametresResultat, 
				hashMap.get("anneeDebut"),
				zonesEtude);
		
		// boucle liste
		for(Object[] object: liste) {
			
			// object[0] --> identifiant de zone d'étude
			// object[1] --> identifiant de zone d'échange
			// object[2] --> flux_max sortant
			
			HashMap<String, Double> hashMapZoneEtude = hashMap1.get(object[0]);
			
			Double range = hashMapZoneEtude.get(object[1]); // range initialisé à = 0

			// si flux_max sortant > range initialisé à = 0
			if (object[2] != null && ((BigDecimal) object[2]).doubleValue() > range) {
				
				// hashMapZoneEtude.put(identifiant de zone d'échange, flux_max sortant)
				hashMapZoneEtude.put((String) object[1], new Double(((BigDecimal) object[2]).doubleValue()));
			}
		}
		
		// requête flux entr
		liste = getPointFluxEntrRange(
				beanParametresResultat, 
				hashMap.get("anneeDebut"),
				zonesEtude);
		
		// boucle liste
		for(Object[] object: liste) {
			
			// object[0] --> identifiant de zone d'étude
			// object[1] --> identifiant de zone d'échange
			// object[2] --> flux_max entr
			
			
			HashMap<String, Double> hashMapZoneEtude = hashMap1.get(object[0]);
			
			Double range = hashMapZoneEtude.get(object[1]); // étape précédente --> range = flux_max sortant

			// si flux_max entr > range
			if (object[2] != null && ((BigDecimal) object[2]).doubleValue() > range) {
				
				hashMapZoneEtude.put((String) object[1], new Double(((BigDecimal) object[2]).doubleValue()));
			}
		}
		
		return hashMap1;
	}
}
