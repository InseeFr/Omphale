package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.dao.donnees.IZoneEtudeZoneEchangeDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.IZoneEtudeZoneEchangeService;
import fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService;

public class ZoneEtudeZoneEchangeService implements IZoneEtudeZoneEchangeService {

	
	private IZoneEtudeZoneEchangeDAO zoneEtudeZoneEchangeDao = daoFactoryPDF.getZoneEtudeZoneEchangeDAO();

	private List<Object[]> getlisteZoneEtudeZoneEch(
			BeanParametresResultat beanParametresResultat) {
		
		List<Object[]> liste = zoneEtudeZoneEchangeDao.getliste(
															beanParametresResultat.getIdUser(), 
															beanParametresResultat.getPrefixe());

		return liste;
	}
		
	public Map<String, List<String>> getHashMapZoneEtudeZoneEchange(
			BeanParametresResultat beanParametresResultat,
			Map<String, String> hashMap){
				
		// init
		HashMap<String, List<String>> hashMapZoneEtudeZoneEchange = new HashMap<String, List<String>>();
		for(Object[] str: getlisteZoneEtudeZoneEch(beanParametresResultat)){

			if (hashMapZoneEtudeZoneEchange.get((String) str[0]) == null) {
				hashMapZoneEtudeZoneEchange.put((String) str[0], new ArrayList<String>(5));
			}
				
			
			if (LancementPdfService.identifiant) {
				hashMapZoneEtudeZoneEchange.get((String) str[0]).add((String) str[1]);
			}
			else {
				// projection utilisateur
				if (hashMap.get("projectionUtilisateur").equals("0")) {
					hashMapZoneEtudeZoneEchange.get((String) str[0]).add((String) str[1]);
				}
				else { // else
					hashMapZoneEtudeZoneEchange.get((String) str[0]).add((String) str[1]);
				}
			}
				
				
		}
		
		return hashMapZoneEtudeZoneEchange;
	}
	
	private List<Object[]> getListeZoneEchangeHorsZonage(
			String idUser, 
			String prefixe)  {
		List<Object[]> liste = zoneEtudeZoneEchangeDao.getListeZoneEchangeHorsZonage(
																		idUser, 
																		prefixe);
		if (liste != null) {
			return liste;
		}
		// else
		return new ArrayList<Object[]>(0);
	}
	
	public Map<String, String> getHashMapZoneEchangeHorsZonage(
			String idUser, 
			String prefixe)  {
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		
		for (Object[] object: getListeZoneEchangeHorsZonage(
				idUser, 
				prefixe)) {
			hashMap.put((String) object[0], (String) object[1]);
		}
		
		return hashMap;
	}
}
