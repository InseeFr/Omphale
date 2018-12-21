package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1ICFDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau1ICFService;


public class Tableau1ICFService implements ITableau1ICFService {

	private ITableau1ICFDAO iCFDao = daoFactoryPDF.getICFDAO();

	// ex.
	// List<Object[]> list = getListe(..
	// liste.get(0) tableau d'Object
	// liste.get(0)[0] --> identifiant de zone étude
	// liste.get(0)[1] --> ICF
	private List<Object[]> getListe(
			BeanParametresResultat beanParametresResultat, 
			String annee) {	
		return iCFDao.getListe(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				annee);
	}
	
	public Map<String, Double> getICF(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String annee) {
		
		// 
		HashMap<String, Double> hashMap1 = new HashMap<String, Double>();

		// boucle
		for(Object[] object: getListe(
								beanParametresResultat, 
								annee)) {
			
			// object[0] --> identifiant de zone d'étude
			// object[1] --> ICF
			
			hashMap1.put((String) object[0], ((BigDecimal) object[1]).doubleValue());
		}
		
		return hashMap1;
	}
}
