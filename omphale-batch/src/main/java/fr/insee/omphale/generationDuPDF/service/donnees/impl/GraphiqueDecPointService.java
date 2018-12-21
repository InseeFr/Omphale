package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiqueDecDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiqueDecPointService;

public class GraphiqueDecPointService implements IGraphiqueDecPointService {

	private IGraphiqueDecDAO decDao = daoFactoryPDF.getDecDAO();

	private GraphiqueService graphiqueService = new GraphiqueService();
	
	// ex.
	// List<Object[]> list = findAll(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone
	// liste.get(0)[1] --> annee
	// liste.get(0)[2] --> sexe
	// liste.get(0)[3] --> age
	// liste.get(0)[4] --> qd
	private List<Object[]> findAll(
			BeanParametresResultat beanParametresResultat, 
			String anneeDebut) {	
		return decDao.getListe(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				anneeDebut, 
				beanParametresResultat.getAge100());
	}
	
	// crée un Map<String, HashMap<Integer, ArrayList<Double>>>
	// ce Map contient :
	// keys : identifiants des zones d'étude
	// values : HashMap<Integer, ArrayList<Double>>>
	// ces HashMap<Integer, ArrayList<Double>>> contiennent :
	// keys : 0, 1 (hommes, femmes)
	// values : ArrayList, size age100 + 1
	public Map<String, HashMap<Integer, ArrayList<Double>>> getDonnees(
					BeanParametresResultat beanParametresResultat,
					Map<String, String> hashMap, 
					List<String> listeZones) {
		
		// crée un Map et l'initialise
		Map<String, HashMap<Integer, ArrayList<Double>>> hashMap1 = graphiqueService.initHashMap(
																				beanParametresResultat.getAge100().intValue() + 1, 
																				listeZones); 
		// requête
		List<Object[]> liste = findAll(beanParametresResultat, hashMap.get("anneeDebut"));
		
		// boucle liste
		for(Object[] object: liste) {
			
			// object[0] --> identifiant de zone d'étude
			// object[1] --> annee
			// object[2] --> sexe
			// object[3] --> age
			// object[4] --> qd
			
			String idZoneEtude = (String) object[0];
			
			HashMap<Integer, ArrayList<Double>> hashMapZoneEtude = hashMap1.get(idZoneEtude);
			
			// 
			if (	((BigDecimal) object[2]).intValue() == 1 
					// 0 <= age <= 99
					&& ((BigDecimal) object[3]).intValue() >= 0 && ((BigDecimal) object[3]).intValue() <= 99) {
				

				hashMapZoneEtude.get(0).set(((BigDecimal) object[3]).intValue(), ((BigDecimal) object[4]).doubleValue());
			}
			// 
			if (	((BigDecimal) object[2]).intValue() == 2
					// 0 <= age <= 99
					&& ((BigDecimal) object[3]).intValue() >= 0 && ((BigDecimal) object[3]).intValue() <= 99) {
				
	
				hashMapZoneEtude.get(1).set(((BigDecimal) object[3]).intValue(), ((BigDecimal) object[4]).doubleValue());
			}
		}
		
		return hashMap1;
	}
}
