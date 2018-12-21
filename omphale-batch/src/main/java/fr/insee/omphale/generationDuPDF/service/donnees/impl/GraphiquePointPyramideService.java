package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePyramideDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointPyramideService;

public class GraphiquePointPyramideService implements IGraphiquePointPyramideService {
	
	private IGraphiquePyramideDAO pointPyramideDao = daoFactoryPDF.getPointPyramideDAO();

	private GraphiqueService graphiqueService = new GraphiqueService();
	
	
	// ex.
	// List<Object[]> list = findAll(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> annee
	// liste.get(0)[2] --> sexe
	// liste.get(0)[3] --> age
	// liste.get(0)[4] --> population
	private List<Object[]> findAll(
						BeanParametresResultat beanParametresResultat,
						String calageSuffixe,
						String anneeDebut, 
						String anneeDebutPlus5, 
						String anneeFin)  {	
		
		List<Object[]> liste = pointPyramideDao.findAll(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				calageSuffixe, 
				anneeDebut, 
				anneeDebutPlus5, 
				anneeFin, 
				beanParametresResultat.getAge100());
		
		return liste;
	}
	
	// crée un Map<String, HashMap<Integer, ArrayList<Double>>>
	// ce Map contient :
	// keys : identifiants des zones d'étude
	// values : HashMap<Integer, ArrayList<Double>>
	// ces HashMap<Integer, ArrayList<Double>> contiennent :
	// keys : numero de la série : 0 hommes anneeDebut, 1 femmes anneeDebut, 2 hommes anneeDebut + 5,  
	// 			3 hommes anneeFin, 4 femmes anneeDebut + 5, 5 femmes anneeFin
	// values : ArrayList<Double>
	public Map<String, HashMap<Integer, ArrayList<Double>>> getDonnees(
					BeanParametresResultat beanParametresResultat,
					Map<String, String> hashMap, 
					List<String> listeZones) {
		
		// crée un Map et l'initialise
		Map<String, HashMap<Integer, ArrayList<Double>>> hashMap1 = graphiqueService.initHashMapPyramide(
																				beanParametresResultat.getAge100().intValue() + 1, 
																				listeZones); 
		
		// requète
		List<Object[]> liste = findAll(
				beanParametresResultat,
				hashMap.get("calage"),
				hashMap.get("anneeDebut"),
				hashMap.get("anneeDebutPlus5"),
				hashMap.get("anneeFin"));
		
		// boucle liste
		for(Object[] object: liste) {
			
			// object[0] --> zone d'étude
			// object[1] --> annee
			// object[2] --> sexe
			// object[3] --> age
			// object[4] --> population			
			
			
			String idZoneEtude = (String) object[0];
			
			HashMap<Integer, ArrayList<Double>> hashMapZoneEtude = hashMap1.get(idZoneEtude);
			
			if (	((BigDecimal) object[1]).intValue() == Integer.parseInt(hashMap.get("anneeDebut"))
				 && ((BigDecimal) object[2]).intValue() == 1) {
				
				hashMapZoneEtude.get(0).set(((BigDecimal) object[3]).intValue(), -((BigDecimal) object[4]).doubleValue());
			}

			if (	((BigDecimal) object[1]).intValue() == Integer.parseInt(hashMap.get("anneeDebut"))
				 && ((BigDecimal) object[2]).intValue() == 2) {
				
				hashMapZoneEtude.get(1).set(((BigDecimal) object[3]).intValue(), ((BigDecimal) object[4]).doubleValue());
			}

			if (	((BigDecimal) object[1]).intValue() == Integer.parseInt(hashMap.get("anneeDebutPlus5"))
				 && ((BigDecimal) object[2]).intValue() == 1) {
				
				hashMapZoneEtude.get(2).set(((BigDecimal) object[3]).intValue(), -((BigDecimal) object[4]).doubleValue());
			}

			if (	((BigDecimal) object[1]).intValue() == Integer.parseInt(hashMap.get("anneeDebutPlus5"))
				 && ((BigDecimal) object[2]).intValue() == 2) {
				
				hashMapZoneEtude.get(4).set(((BigDecimal) object[3]).intValue(), ((BigDecimal) object[4]).doubleValue());
			}

			if (	((BigDecimal) object[1]).intValue() == Integer.parseInt(hashMap.get("anneeFin"))
				 && ((BigDecimal) object[2]).intValue() == 1) {
				
				hashMapZoneEtude.get(3).set(((BigDecimal) object[3]).intValue(), -((BigDecimal) object[4]).doubleValue());
			}

			if (	((BigDecimal) object[1]).intValue() == Integer.parseInt(hashMap.get("anneeFin"))
				 && ((BigDecimal) object[2]).intValue() == 2) {
				
				hashMapZoneEtude.get(5).set(((BigDecimal) object[3]).intValue(), ((BigDecimal) object[4]).doubleValue());
			}
		}
		
		return hashMap1;
	}
}
