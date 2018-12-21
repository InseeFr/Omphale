package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5DeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5VersDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau2TousAgesPlus5Service;


public class Tableau2TousAgesPlus5Service implements ITableau2TousAgesPlus5Service {

	private ITableau2TousAgesPlus5VersDAO tousAgesPlus5VersDao = daoFactoryPDF.getTousAgesPlus5VersDAO();
	private ITableau2TousAgesPlus5DeDAO tousAgesPlus5DeDao = daoFactoryPDF.getTousAgesPlus5DeDAO();
	
	public List<Object[]> getVersAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return tousAgesPlus5VersDao.getVersAnneeDebut(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				zonesEtude);
	}
	
	public List<Object[]> getVersAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return tousAgesPlus5VersDao.getVersAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeDebut"),
				zonesEtude);
	}
	
	public List<Object[]> getVersAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return tousAgesPlus5VersDao.getVersAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeFinMoins1"),
				zonesEtude);
	}
	
	public List<Object[]> getVers(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return tousAgesPlus5VersDao.getVers(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				zonesEtude);
	}
	
	public List<Object[]> getDeAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return tousAgesPlus5DeDao.getDeAnneeDebut(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				zonesEtude);
	}
	
	public List<Object[]> getDeAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return tousAgesPlus5DeDao.getDeAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeDebut"),
				zonesEtude);
	}
	
	public List<Object[]> getDeAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return tousAgesPlus5DeDao.getDeAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeFinMoins1"),
				zonesEtude);
	}
	
	public List<Object[]> getDe(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude) {
		return tousAgesPlus5DeDao.getDe(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				zonesEtude);
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
			// object[1] --> identifiant de zone d'échange
			// object[2] --> flux
			
			
			Map<String, Double> hashMapZoneEtude = hashMap1.get(object[0]); 
			
			// hashMapZoneEtude.put(identifiant de zone d'échange, flux)
			hashMapZoneEtude.put((String) object[1], ((BigDecimal) object[2]).doubleValue()); 
		}
		
		return hashMap1;
	}
	
	
	// crée un Map<String, HashMap<String, Double>>
	// keys : identifiants des zones d'étude
	// keys des HashMap<String, Double> : identifiants des zones d'échange
	// values des HashMap<String, Double> : flux initialisés à 0
	private Map<String, HashMap<String, Double>> initHashMap(
			Map<String, List<String>> hashMapZonesEtudeZoneEch){
		
		Map<String, HashMap<String, Double>> hashMap1 = 
						new HashMap<String, HashMap<String, Double>>();	
		
		for(String zoneEtude: hashMapZonesEtudeZoneEch.keySet()){

			HashMap<String, Double> hashMapZoneEtude = new HashMap<String, Double>();
			hashMap1.put(zoneEtude, hashMapZoneEtude);
			
			for(String zoneEch: hashMapZonesEtudeZoneEch.get(zoneEtude)){
				
				hashMapZoneEtude.put(zoneEch, new Double(0));
			}
		}
		
		return hashMap1;
	}
}
