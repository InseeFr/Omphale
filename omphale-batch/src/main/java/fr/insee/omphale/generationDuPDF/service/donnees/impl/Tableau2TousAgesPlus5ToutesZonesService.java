package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5ToutesZonesDeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5ToutesZonesVersDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau2TousAgesPlus5ToutesZonesService;


public class Tableau2TousAgesPlus5ToutesZonesService implements ITableau2TousAgesPlus5ToutesZonesService {

	private ITableau2TousAgesPlus5ToutesZonesVersDAO tousAgesPlus5ToutesZonesVersDao = daoFactoryPDF.getTousAgesPlus5ToutesZonesVersDAO();
	private ITableau2TousAgesPlus5ToutesZonesDeDAO tousAgesPlus5ToutesZonesDeDao = daoFactoryPDF.getTousAgesPlus5ToutesZonesDeDAO();
	
	public List<Object[]> getVersAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return tousAgesPlus5ToutesZonesVersDao.getVersAnneeDebut(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe());
	}
	
	public List<Object[]> getVersAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return tousAgesPlus5ToutesZonesVersDao.getVersAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeDebut"));
	}
	
	public List<Object[]> getVersAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return tousAgesPlus5ToutesZonesVersDao.getVersAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeFinMoins1"));
	}
	
	public List<Object[]> getVers(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return tousAgesPlus5ToutesZonesVersDao.getVers(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe());
	}
	
	public List<Object[]> getDeAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return tousAgesPlus5ToutesZonesDeDao.getFluxEntrAnneeDebut(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe());
	}
	
	public List<Object[]> getDeAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return tousAgesPlus5ToutesZonesDeDao.getFluxEntrAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeDebut"));
	}
	
	public List<Object[]> getDeAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return tousAgesPlus5ToutesZonesDeDao.getFluxEntrAnnee(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe(),
				hashMap.get("anneeFinMoins1"));
	}
	
	public List<Object[]> getDe(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap) {
		return tousAgesPlus5ToutesZonesDeDao.getFluxEntr(
				beanParametresResultat.getIdUser(), 
				beanParametresResultat.getPrefixe());
	}

	public Map<String, Double> getDonnees(
			List<Object[]> donnees,
			BeanParametresResultat beanParametresResultat,
			Map<String, List<String>> treeMapZonesEtudeZoneEch,
			Map<String, String> hashMap) {

		// crée un Map et l'initialise
		Map<String, Double> hashMap1 = initHashMap(treeMapZonesEtudeZoneEch); 

		// boucle
		for(Object[] object: donnees) {
		
			// object[0] --> identifiant de zone d'étude
			// object[1] --> flux
			
			
			// hashMap1.put(identifiant de zone d'étude, flux)
			hashMap1.put((String) object[0], ((BigDecimal) object[1]).doubleValue()); 
		}
		
		return hashMap1;
	}
	
	
	// crée un Map<String, Double>
	// keys : identifiants des zones d'étude
	// values : flux initialisés à 0
	private Map<String, Double> initHashMap(
			Map<String, List<String>> hashMapZonesEtudeZoneEch){
		
		HashMap<String, Double> hashMap1 = 
						new HashMap<String, Double>();	
		
		for(String zoneEtude: hashMapZonesEtudeZoneEch.keySet()){

			hashMap1.put(zoneEtude, new Double(0));
		}
		
		return hashMap1;
	}
}
