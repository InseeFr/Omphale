package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1AgeMoyenDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau1AgeMoyenService;


public class Tableau1AgeMoyenService implements ITableau1AgeMoyenService {

	private ITableau1AgeMoyenDAO ageMoyenDao = daoFactoryPDF.getAgeMoyenDAO();

	// ex.
	// List<Object[]> list = getZonesAgeMoyen(..
	// liste.get(0) tableau d'Object
	// liste.get(0)[0] --> identifiant de zone étude
	// liste.get(0)[1] --> age moyen
	private List<Object[]> getZonesAgeMoyen(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String annee) {	
		return ageMoyenDao.getAgeMoyen(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				hashMap.get("calage"),
				annee);
	}
	
	public Map<String, Double> getAgeMoyen(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String annee) {
		
		HashMap<String, Double> donnees = new HashMap<String, Double>();

		// boucle
		for(Object[] object: getZonesAgeMoyen(
								beanParametresResultat, 
								hashMap,
								annee)) {
			
			// object[0] --> identifiant de zone d'étude
			// object[1] --> âge moyen
			
			donnees.put((String) object[0], ((BigDecimal) object[1]).doubleValue());
		}
		
		return donnees;
	}
}
