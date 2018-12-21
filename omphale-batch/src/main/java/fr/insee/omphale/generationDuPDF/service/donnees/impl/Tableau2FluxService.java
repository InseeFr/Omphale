package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2DeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2VersDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau2FluxService;


public class Tableau2FluxService implements ITableau2FluxService {

	private ITableau2VersDAO versDao = daoFactoryPDF.getVersDAO();
	private ITableau2DeDAO deDao = daoFactoryPDF.getDeDAO();
	
	public List<Object[]> getVersAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return versDao.getVersAnneeDebut(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				zonesEtude);
	}
	
	public List<Object[]> getVersAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return versDao.getVersAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeDebut"),
				zonesEtude );
	}
	
	public List<Object[]> getVersAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return versDao.getVersAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeFinMoins1"),
				zonesEtude);
	}
	
	public List<Object[]> getVers(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return versDao.getVers(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				zonesEtude);
	}
	
	public List<Object[]> getDeAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return deDao.getDeAnneeDebut(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				zonesEtude);
	}
	
	public List<Object[]> getDeAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return deDao.getDeAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeDebut"),
				zonesEtude);
	}
	
	public List<Object[]> getDeAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return deDao.getDeAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeFinMoins1"),
				zonesEtude);
	}
	
	public List<Object[]> getDe(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return deDao.getDe(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				zonesEtude);
	}

	public Map<String, HashMap<String, HashMap<String, Double>>> getDonnees(
			List<Object[]> donnees,
			BeanParametresResultat beanParametresResultat,
			Map<String, List<String>> treeMapZonesEtudeZoneEch,
			Map<String, String> hashMap) {

		// crée une Map et l'initialise
		Map<String, HashMap<String, HashMap<String, Double>>> hashMap1 = initHashMap(treeMapZonesEtudeZoneEch); 

		// boucle
		for(Object[] object: donnees) {
		
			// object[0] --> identifiant de zone d'étude
			// object[1] --> identifiant de zone d'échange
			// object[2] --> tr age
			// object[3] --> flux
			
			
			HashMap<String, HashMap<String, Double>> hashMapZoneEtude = hashMap1.get(object[0]); 
			
			HashMap<String, Double> hashMapZoneEtudeTr_Age = hashMapZoneEtude.get(object[2]); 
			
			hashMapZoneEtudeTr_Age.put((String) object[1], ((BigDecimal) object[3]).doubleValue()); 
		}
		
		return hashMap1;
	}
	
	// crée une Map<String, HashMap<String, HashMap<String, Double>>>
	// keys : identifiants des zones d'étude
	// keys des HashMap<String, HashMap<String, Double>> : tr age
	// keys des HashMap<String, Double> : zones d'échange
	// values des HashMap<String, Double> : flux initialisés à 0
	private Map<String, HashMap<String, HashMap<String, Double>>> initHashMap(
			Map<String, List<String>> hashMapZonesEtudeZoneEch){
		
		HashMap<String, HashMap<String, HashMap<String, Double>>> hashMap1 = 
						new HashMap<String, HashMap<String, HashMap<String, Double>>>();	
		
		// boucle zone d'étude
		for(String zoneEtude: hashMapZonesEtudeZoneEch.keySet()){

			HashMap<String, HashMap<String, Double>> hashMapZoneEtude = new HashMap<String, HashMap<String, Double>>();
			hashMap1.put(zoneEtude, hashMapZoneEtude);
		
			hashMapZoneEtude.put("1-24", new HashMap<String, Double>());
			hashMapZoneEtude.put("25-59", new HashMap<String, Double>());
			hashMapZoneEtude.put("60", new HashMap<String, Double>());
			
			// boucle zone d'échange
			for(String zoneEch: hashMapZonesEtudeZoneEch.get(zoneEtude)){
				
				hashMapZoneEtude.get("1-24").put(zoneEch, new Double(0));
				hashMapZoneEtude.get("25-59").put(zoneEch, new Double(0));
				hashMapZoneEtude.get("60").put(zoneEch, new Double(0));	
			}
		}
		
		return hashMap1;
	}
}
