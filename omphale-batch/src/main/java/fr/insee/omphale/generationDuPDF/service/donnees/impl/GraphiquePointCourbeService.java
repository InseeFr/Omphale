package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiqueFeconditeDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointCourbeService;

public class GraphiquePointCourbeService implements IGraphiquePointCourbeService {

	private IGraphiqueFeconditeDAO pointCourbeDao = daoFactoryPDF.getPointCourbeDAO();

	private GraphiqueService graphiqueService = new GraphiqueService();
	
	// ex.
	// List<Object[]> list = findAll(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone
	// liste.get(0)[1] --> annee
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> qf
	private List<Object[]> findAll(
			BeanParametresResultat beanParametresResultat, 
			String anneeDebut) {	
		return pointCourbeDao.getListe(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				anneeDebut, 
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
														100, 
														listeZones);
		
		// requête
		List<Object[]> liste = findAll(
								beanParametresResultat,
								hashMap.get("anneeDebut"));
		// boucle liste
		for(Object[] object: liste) {
			
			// object[0] --> identifiant de zone d'étude
			// object[1] --> annee
			// object[2] --> age
			// object[3] --> qf
			
			String idZoneEtude = (String) object[0];
						
			hashMap1.get(idZoneEtude).set(((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).doubleValue());
		}
		
		return hashMap1;
	}
}
