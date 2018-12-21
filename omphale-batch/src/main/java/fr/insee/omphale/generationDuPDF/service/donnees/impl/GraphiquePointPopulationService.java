package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePopulationDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointPopulationService;

public class GraphiquePointPopulationService implements IGraphiquePointPopulationService {

	private IGraphiquePopulationDAO populationDao = daoFactoryPDF.getPopulationDAO();

	private GraphiqueService graphiqueService = new GraphiqueService();
	
	
	// ex.
	// List<Object[]> list = getPopulation(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> annee
	// liste.get(0)[2] --> population
	private List<Object[]> getPopulation(
			BeanParametresResultat beanParametresResultat,
			String calageSuffixe) {	
		return populationDao.getPopulation(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				calageSuffixe, 
				beanParametresResultat.getAge100());
	}
	
	// crée un Map<String, ArrayList<Double>>
	// ce Map contient :
	// keys : identifiants des zones d'étude
	// values : ArrayList<Double>
	public Map<String, ArrayList<Double>> getDonnees(
			BeanParametresResultat beanParametresResultat,
			Map<String, String> hashMap, 
			List<String> listeZones) {
		
		// crée un Map et l'initialise
		Map<String, ArrayList<Double>> hashMap1 = graphiqueService.initHashMap1(
													Integer.parseInt(hashMap.get("anneeFin")) - Integer.parseInt(hashMap.get("anneeDebut")) + 1,
													listeZones); 		
		
		// requête
		List<Object[]> liste = getPopulation(
				beanParametresResultat,
				hashMap.get("calage"));
		
		// boucle liste
		for(Object[] object: liste) {
			
			// object[0] --> identifiant zone d'étude
			// object[1] --> annee
			// object[2] --> population
			
			String idZoneEtude = (String) object[0];

			hashMap1.get(idZoneEtude).set(((BigDecimal) object[1]).intValue() - Integer.parseInt(hashMap.get("anneeDebut")),
						  				 ((BigDecimal) object[2]).divide(new BigDecimal(1000)).doubleValue());
		}
		
		return hashMap1;
	}
}
