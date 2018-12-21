package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePointFluxDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointFluxService;

public class GraphiquePointFluxService implements IGraphiquePointFluxService {

	
	private IGraphiquePointFluxDAO pointFluxDao = daoFactoryPDF.getPointFluxDAO();

	private GraphiqueService graphiqueService = new GraphiqueService();
	
	
	// zonesEtude :  ex. "('400', '410')"
	// ex.
	// List<Object[]> list = getPointFluxSortant(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> zone d'échange
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> sexe
	// liste.get(0)[4] --> flux sortant
	public List<Object[]> getPointFluxSortant(
			BeanParametresResultat beanParametresResultat, 
			String anneeDebut,
			String zonesEtude) {	
		
		List<Object[]> liste = pointFluxDao.getPointFluxSortant(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				anneeDebut, 
				beanParametresResultat.getAge100(),
				zonesEtude);

		return liste;
	}
	
	// zonesEtude :  ex. "('400', '410')"
	// ex.
	// List<Object[]> list = getPointFluxEntr(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> zone d'échange
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> sexe
	// liste.get(0)[4] --> flux entr
	public List<Object[]> getPointFluxEntr(
			BeanParametresResultat beanParametresResultat, 
			String anneeDebut,
			String zonesEtude) {	
		
		List<Object[]> liste = pointFluxDao.getPointFluxEntr(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				anneeDebut, 
				beanParametresResultat.getAge100(),
				zonesEtude);

		return liste;
	}

	// liste : résultat de getPointFluxSortant(.. ou résultat de getPointFluxEntr(..
	// 
	// crée un Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>>
	// ce Map contient :
	// keys : identifiants des zones d'étude
	// values : HashMap<String, HashMap<Integer, ArrayList<Double>>>
	// ces HashMap<String, HashMap<Integer, ArrayList<Double>>> contiennent :
	// keys : identifiants des zones d'échange
	// values : HashMap<Integer, ArrayList<Double>>
	// ces HashMap<Integer, ArrayList<Double>> contiennent :
	// keys : 0, 1 (hommes, femmes)
	// values : ArrayList, size age100 + 1
	public Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> getDonnees(
								List<Object[]> liste,
								BeanParametresResultat beanParametresResultat,
								Map<String, List<String>> zonesEtudeZoneEch) {
		
		// crée un Map et l'initialise
		Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> hashMap1 = graphiqueService.initHashMapZonesEtudeZoneEchange(
																							beanParametresResultat.getAge100().intValue() + 1,
																							zonesEtudeZoneEch); 
		
		// boucle liste
		for(Object[] object: liste) {
			
			// object[0] --> zone d'étude
			// object[1] --> zone d'échange
			// object[2] --> age
			// object[3] --> sexe
			// object[4] --> flux
			
			
			HashMap<String, HashMap<Integer, ArrayList<Double>>> hashMapZoneEtude = hashMap1.get(object[0]); 
			
			HashMap<Integer, ArrayList<Double>> hashMapZoneEtudeZoneEch = hashMapZoneEtude.get(object[1]); 
			
			// 
			if (((BigDecimal) object[3]).intValue() == 1) {
				
				// hashMapZoneEtudeZoneEch.get(0).set(age, flux)
				hashMapZoneEtudeZoneEch.get(0).set(((BigDecimal) object[2]).intValue(), ((BigDecimal) object[4]).doubleValue());
			}
			if (((BigDecimal) object[3]).intValue() == 2) {
				
				// hashMapZoneEtudeZoneEch.get(1).set(age, flux)
				hashMapZoneEtudeZoneEch.get(1).set(((BigDecimal) object[2]).intValue(), ((BigDecimal) object[4]).doubleValue());
			}
		}
		
		return hashMap1;
	}
}
