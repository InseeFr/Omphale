package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2ToutesZonesDeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2ToutesZonesVersDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau2ToutesZonesService;


public class Tableau2ToutesZonesService implements ITableau2ToutesZonesService {

	private ITableau2ToutesZonesVersDAO toutesZonesVersDao = daoFactoryPDF.getToutesZonesVersDAO();
	private ITableau2ToutesZonesDeDAO toutesZonesDeDao = daoFactoryPDF.getToutesZonesDeDAO();
	
	public List<Object[]> getVersAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return toutesZonesVersDao.getVersAnneeDebut(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe());
	}
	
	public List<Object[]> getVersAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return toutesZonesVersDao.getVersAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeDebut"));
	}
	
	public List<Object[]> getVersAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return toutesZonesVersDao.getVersAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeFinMoins1"));
	}
	
	public List<Object[]> getVers(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return toutesZonesVersDao.getVers(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe());
	}
	
	public List<Object[]> getDeAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return toutesZonesDeDao.getDeAnneeDebut(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe());
	}
	
	public List<Object[]> getDeAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return toutesZonesDeDao.getDeAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeDebut"));
	}
	
	public List<Object[]> getDeAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return toutesZonesDeDao.getDeAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeFinMoins1"));
	}
	
	public List<Object[]> getDe(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return toutesZonesDeDao.getDe(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe());
	}

	public Map<String, HashMap<String, Double>> getDonnees(
			List<Object[]> donnees,
			BeanParametresResultat beanParametresResultat,
			Map<String, List<String>> treeMapZonesEtudeZoneEch,
			Map<String, String> hashMap) {

		// crée un Map et l'initialise
		Map<String, HashMap<String, Double>> hashMap1 = initHashMap(treeMapZonesEtudeZoneEch); 
		
		// boucle
		for(Object[] object: donnees) {
		
			// object[0] --> identifiant de zone d'étude
			// object[1] --> tr age
			// object[2] --> flux
			
			
			HashMap<String, Double> hashMapZoneEtude = hashMap1.get(object[0]); // za
			hashMapZoneEtude.put((String) object[1], ((BigDecimal) object[2]).doubleValue()); // tr_age, flu
		}
		
		return hashMap1;
	}
	
	
	// crée un Map<String, HashMap<String, Double>>
	// keys : identifiants des zones d'étude
	// keys des HashMap<String, Double> : tr age
	// values des HashMap<String, Double> : flux initialisés à 0
	private Map<String, HashMap<String, Double>> initHashMap(
			Map<String, List<String>> hashMapZonesEtudeZoneEch){
		
		HashMap<String, HashMap<String, Double>> hashMap1 = 
						new HashMap<String, HashMap<String, Double>>();	
		
		// boucle zone d'étude
		for(String zoneEtude: hashMapZonesEtudeZoneEch.keySet()){

			HashMap<String, Double> hashMapZoneEtude = new HashMap<String, Double>();
			hashMap1.put(zoneEtude, hashMapZoneEtude);
		
			hashMapZoneEtude.put("1-24", new Double(0));
			hashMapZoneEtude.put("25-59", new Double(0));
			hashMapZoneEtude.put("60", new Double(0));
		}
		
		return hashMap1;
	}
}
