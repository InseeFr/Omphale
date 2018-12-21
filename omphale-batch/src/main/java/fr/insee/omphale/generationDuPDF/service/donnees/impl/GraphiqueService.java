package fr.insee.omphale.generationDuPDF.service.donnees.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphiqueService {
	
	
	/**
	 * crée un ArrayList de longueur len, les valeurs sont initialisées à 0
	 * @param len size arrayList
	 */
	private ArrayList<Double> createArrayListAndInitValuesTo0(int len){
		
		ArrayList<Double> arrayList = new ArrayList<Double>(len);
		for(int i = 0; i < len; i++) {
			arrayList.add(new Double(0));
		}
		
		return arrayList;
	}
	
	
	// crée un Map<String, HashMap<Integer, ArrayList<Double>>>
	// keys : identifiants des zones d'étude
	// keys des HashMap<Integer, ArrayList<Double>>> : numero de la série : 0 hommes anneeDebut, 1 femmes anneeDebut, 2 hommes anneeDebut + 5,  
	// 			3 hommes anneeFin, 4 femmes anneeDebut + 5, 5 femmes anneeFin
	// les ArrayList sont de longueur len, leurs valeurs sont initialisées à 0
	protected Map<String, HashMap<Integer, ArrayList<Double>>> initHashMapPyramide(
			int len,
			List<String> listeZones){
		
		// constructeur
		HashMap<String, HashMap<Integer, ArrayList<Double>>> hashMap1 = new HashMap<String, HashMap<Integer, ArrayList<Double>>>();
		
		// boucle identifiants des zones d'étude
		for(String zoneEtude: listeZones){
			
			// crée un arraylist, size 100
			// et initialise les valeurs de l'arraylist à 0
			ArrayList<Double> arraylist0 = createArrayListAndInitValuesTo0(len); 
			
			// crée un arraylist, size 100
			// et initialise les valeurs de l'arraylist à 0
			ArrayList<Double> arraylist1 = createArrayListAndInitValuesTo0(len); 
			
			// crée un arraylist, size 100
			// et initialise les valeurs de l'arraylist à 0
			ArrayList<Double> arraylist2 = createArrayListAndInitValuesTo0(len); 
			
			// crée un arraylist, size 100
			// et initialise les valeurs de l'arraylist à 0
			ArrayList<Double> arraylist3 = createArrayListAndInitValuesTo0(len); 
			
			// crée un arraylist, size 100
			// et initialise les valeurs de l'arraylist à 0
			ArrayList<Double> arraylist4 = createArrayListAndInitValuesTo0(len); 
			
			// crée un arraylist, size 100
			// et initialise les valeurs de l'arraylist à 0
			ArrayList<Double> arraylist5 = createArrayListAndInitValuesTo0(len); 
					
			//
			HashMap<Integer, ArrayList<Double>> hashMapZoneEtude = new HashMap<Integer, ArrayList<Double>>();
			hashMapZoneEtude.put(Integer.valueOf(0), arraylist0);
			hashMapZoneEtude.put(Integer.valueOf(1), arraylist1);
			hashMapZoneEtude.put(Integer.valueOf(2), arraylist2);
			hashMapZoneEtude.put(Integer.valueOf(3), arraylist3);
			hashMapZoneEtude.put(Integer.valueOf(4), arraylist4);
			hashMapZoneEtude.put(Integer.valueOf(5), arraylist5);
			
			hashMap1.put(zoneEtude, hashMapZoneEtude);
		}

		return hashMap1;
	}
	
	
	// crée un Map<String, ArrayList<Double>>
	// keys : identifiants des zones d'étude
	// les ArrayList sont de longueur len, leurs valeurs sont initialisées à 0
	protected Map<String, ArrayList<Double>> initHashMap1(
												int len, 
												List<String> listeZones){
		
		// constructeur
		HashMap<String, ArrayList<Double>> hashMap1 = new HashMap<String, ArrayList<Double>>();
		
		// boucle identifiants des zones d'étude
		for(String zoneEtude: listeZones){
			
			// crée un arraylist, size len
			// et initialise les valeurs de l'arraylist à 0
			ArrayList<Double> arraylist = createArrayListAndInitValuesTo0(len); 
			
			hashMap1.put(zoneEtude, arraylist); 
		}

		return hashMap1;
	}
	
	
	// crée un Map<String, HashMap<Integer, ArrayList<Double>>>
	// keys : identifiants des zones d'étude
	// keys des HashMap<Integer, ArrayList<Double>>> : 0, 1 (hommes, femmes)
	// les ArrayList sont de longueur len, leurs valeurs sont initialisées à 0
	protected Map<String, HashMap<Integer, ArrayList<Double>>> initHashMap(
										int len,
										List<String> listeZones){
		
		// constructeur Map<String, HashMap<Integer, ArrayList<Double>>>
		HashMap<String, HashMap<Integer, ArrayList<Double>>> hashMap1 = new HashMap<String, HashMap<Integer, ArrayList<Double>>>();
		
		// boucle zone_etude
		for(String zoneEtude: listeZones){
					
			// crée arraylist0, size len
			// et initialise les valeurs à 0
			ArrayList<Double> arraylist0 = createArrayListAndInitValuesTo0(len); 
			
			// crée arraylist1, size len
			// et initialise les valeurs à 0
			ArrayList<Double> arraylist1 = createArrayListAndInitValuesTo0(len); 
			
			// crée hashMapZoneEtude
			// keys : 0, 1
			// values : arraylist0, arraylist1
			HashMap<Integer, ArrayList<Double>> hashMapZoneEtude = new HashMap<Integer, ArrayList<Double>>();
			hashMapZoneEtude.put(Integer.valueOf(0), arraylist0); // hommes
			hashMapZoneEtude.put(Integer.valueOf(1), arraylist1); // femmes
			
			// 
			hashMap1.put(zoneEtude, hashMapZoneEtude);
		}

		return hashMap1;
	}
	
	
	// crée un Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>>
	// keys : identifiants des zones d'étude
	// keys des HashMap<String, HashMap<Integer, ArrayList<Double>>> : identifiants des zones d'échange
	// keys des HashMap<Integer, ArrayList<Double>> : 0, 1 (hommes, femmes)
	// les ArrayList sont de longueur len, leurs valeurs sont initialisées à 0
	protected Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> initHashMapZonesEtudeZoneEchange(
														Integer len,
														Map<String, List<String>> zonesEtudeZoneEch){
		
		// constructeur Map<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>>
		HashMap<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>> hashMap1 = 
			new HashMap<String, HashMap<String, HashMap<Integer, ArrayList<Double>>>>();
		
		// boucle zone_etude
		for(String zoneEtude: zonesEtudeZoneEch.keySet()){

			// constructeur
			HashMap<String, HashMap<Integer, ArrayList<Double>>> hashMapZoneEtude = new HashMap<String, HashMap<Integer, ArrayList<Double>>>();
		
			// boucle zone d'échange
			for(String zoneEch: zonesEtudeZoneEch.get(zoneEtude)){

				// crée arraylist0, size beanParametresResultat.getAge100().intValue() + 1
				// et initialise les valeurs à 0
				ArrayList<Double> arraylist0 = createArrayListAndInitValuesTo0(len); 
				
				// crée arraylist1, size beanParametresResultat.getAge100().intValue() + 1
				// et initialise les valeurs à 0
				ArrayList<Double> arraylist1 = createArrayListAndInitValuesTo0(len); 
				
				// crée hashMapZoneEtudeZoneEch
				// keys : 0, 1
				// values : arraylist0, arraylist1
				HashMap<Integer, ArrayList<Double>> hashMapZoneEtudeZoneEch = new HashMap<Integer, ArrayList<Double>>();
				hashMapZoneEtudeZoneEch.put(Integer.valueOf(0), arraylist0); // hommes
				hashMapZoneEtudeZoneEch.put(Integer.valueOf(1), arraylist1); // femmes
				
				// 
				hashMapZoneEtude.put(zoneEch, hashMapZoneEtudeZoneEch);	
			}
			
			hashMap1.put(zoneEtude, hashMapZoneEtude);
		}
		
		return hashMap1;
	}
	
	
	// crée un Map<String, HashMap<String, Double>>
	// keys : identifiants des zones d'étude
	// keys des HashMap<String, Double>> : identifiants des zones d'échange
	// values des HashMap<String, Double>> : range initialisés à 0
	protected Map<String, HashMap<String, Double>> initRange(Map<String, List<String>> zonesEtudeZoneEch){
		
		// constructeur HashMap<String, HashMap<String, Double>>
		HashMap<String, HashMap<String, Double>> hashMap = new HashMap<String, HashMap<String, Double>>();
		
		// boucle zone d'etude
		for(String zoneEtude: zonesEtudeZoneEch.keySet()){
			
			// constructeur													  
			HashMap<String, Double> hashMapZoneEtude = new HashMap<String, Double>();
		
			// boucle zone d'échange
			for(String zoneEch: zonesEtudeZoneEch.get(zoneEtude)){
				
				// range initialisé à 0 
				hashMapZoneEtude.put(zoneEch, new Double(0));
			}
			
			hashMap.put(zoneEtude, hashMapZoneEtude);
		}
		
		return hashMap;
	}
	
	

}
