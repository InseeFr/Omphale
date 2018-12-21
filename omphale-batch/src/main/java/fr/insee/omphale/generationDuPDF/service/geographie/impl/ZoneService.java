package fr.insee.omphale.generationDuPDF.service.geographie.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fr.insee.omphale.generationDuPDF.domaine.geographie.Zonage;
import fr.insee.omphale.generationDuPDF.domaine.geographie.Zone;
import fr.insee.omphale.generationDuPDF.service.donnees.IZoneEtudeZoneEchangeService;
import fr.insee.omphale.generationDuPDF.service.donnees.impl.ZoneEtudeZoneEchangeService;
import fr.insee.omphale.generationDuPDF.service.geographie.IZonageService;
import fr.insee.omphale.generationDuPDF.service.geographie.IZoneService;
import fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService;

public class ZoneService implements IZoneService {
	
	private IZonageService zonageService = new ZonageService();
	private IZoneEtudeZoneEchangeService zoneEtudeZoneEchangeService = new ZoneEtudeZoneEchangeService();
	
	// identifiants, libellés des zones du zonage
	public Map<String, String> getZoneLibelle(Zonage zonage) {
		HashMap<String, String> hashMap = new HashMap<String, String>();
		
		for(Zone zone: zonage.getZones()) {
			
			
			
			
			// modifier
			if (LancementPdfService.identifiant) {
				if (zone.getLibelle() != null && !("").equals(zone.getLibelle().trim())) {
					hashMap.put(zone.getId(), zone.getLibelle());
				}
				else {
					hashMap.put(zone.getId(), zone.getId());
				}
			}
			else {
				// pas zonage utilisateur
				if (zonageService.isProjectionUtilisateur(zonage.getId()).equals("0")) {
					StringBuffer str = new StringBuffer();
					str.append("DET_");
					str.append(zone.getNom().substring(13));
					hashMap.put(str.toString(), zone.getLibelle());
				}
				// zonage utilisateur
				else {
					if (zone.getLibelle() != null && !("").equals(zone.getLibelle().trim())) {
						hashMap.put(zone.getNom(), zone.getLibelle());
					}
					else {
						hashMap.put(zone.getNom(), zone.getNom());
					}
				}
			}
			
			
			
			
		}
		return hashMap;
	}
	
	// identifiants des zones du zonage
	public List<String> getListeZones(Map<String, String> zoneLibelle) {
		@SuppressWarnings("unchecked")
		TreeMap<String, String> treeMap = new TreeMap<String, String>(new ZoneCompare(zoneLibelle));
		for(String key: zoneLibelle.keySet()) {
			treeMap.put(key, zoneLibelle.get(key));
		}
		
		ArrayList<String> listeZones = new ArrayList<String>(treeMap.size());
		for(String zone: treeMap.keySet()){
			listeZones.add(zone);
		}
		return listeZones;
	}
	
	// ajoute à zoneLibelle les libellés des zones d'échange hors zonage
	public void add(
			String idUser,
			String prefixe,
			Map<String, String> zoneLibelle) {
		
		// zones d'échange (ID_ZONE, libellé), qui apparaissent dans la liste pour chaque zone d'étude des 5 principales zones d'échange,
		// et qui sont hors zonage
		Map<String, String> hashMapZoneEchange = zoneEtudeZoneEchangeService.getHashMapZoneEchangeHorsZonage(idUser, prefixe);		
		
		for(String zoneId: hashMapZoneEchange.keySet()) {
				
			// ajout zoneLibelle
			
			
			
			// modifier
			if (LancementPdfService.identifiant) {
				zoneLibelle.put(zoneId, hashMapZoneEchange.get(zoneId));
			}
			else {
				zoneLibelle.put("_DEP_" + zoneId.substring(13), hashMapZoneEchange.get(zoneId));
			}
			
			
		}
	}
	
}
