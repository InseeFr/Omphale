package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePointFluxSoldeDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.IGraphiquePointSoldeFluxService;

public class GraphiquePointSoldeFluxService implements IGraphiquePointSoldeFluxService {
	
	private IGraphiquePointFluxSoldeDAO pointFluxDaoSolde = daoFactoryPDF.getPointFluxSoldeDAO();
	
	private GraphiqueService graphiqueService = new GraphiqueService();
	
	
	// ex.
	// List<Object[]> list = getPointFluxSolde(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone
	// liste.get(0)[1] --> sexe
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> flux
	private List<Object[]> getPointFluxSolde(
			BeanParametresResultat beanParametresResultat, 
			String anneeDebut,
			String zonesEtude) {	
		
		List<Object[]> liste = pointFluxDaoSolde.getPointFluxSolde(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(), 
				anneeDebut,
				beanParametresResultat.getAge100(),
				zonesEtude);

		return liste;
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
			List<String> listeZones, 
			Map<String, String> hashMap,
			String zonesEtude) {
		
		// crée un Map et l'initialise
		Map<String, HashMap<Integer, ArrayList<Double>>> hashMap1 = graphiqueService.initHashMap(
																			beanParametresResultat.getAge100().intValue() + 1, 
																			listeZones);		
		
		// requête
		List<Object[]> liste = getPointFluxSolde(
				beanParametresResultat, 
				hashMap.get("anneeDebut"), 
				zonesEtude);
		
		// boucle liste
		for(Object[] object: liste) {
			
			// object[0] --> zone
			// object[1] --> sexe
			// object[2] --> age
			// object[3] --> flux
			
			
			HashMap<Integer, ArrayList<Double>> hashMapZoneEtude = hashMap1.get(object[0]);
			
			// 
			if (((BigDecimal) object[1]).intValue() == 1) {
				
				hashMapZoneEtude.get(0).set(((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).doubleValue());
			}
			if (((BigDecimal) object[1]).intValue() == 2) {
				
				hashMapZoneEtude.get(1).set(((BigDecimal) object[2]).intValue(), ((BigDecimal) object[3]).doubleValue());
			}
		}
		
		return hashMap1;
	}
}
