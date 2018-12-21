package fr.insee.omphale.generationDuPDF.service.donnees.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1EDVDAO;
import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;
import fr.insee.omphale.generationDuPDF.service.donnees.ITableau1EDVService;

public class Tableau1EDVService implements ITableau1EDVService {

	private ITableau1EDVDAO eDVDao = daoFactoryPDF.getEDVDAO();
	
	
	/**
	 * méthode qui calcule l'espérance de vie par zone et sexe
	 * retourne une map qui a :
	 * pour clé : l'identifiant de la zone d'étude
	 * pour valeur : une map qui a :
	 * 				 pour clé : le sexe
	 * 				 pour valeur : esperance de vie
	 */
	public Map<String, HashMap<Integer, Double>> getEDV(BeanParametresResultat beanParametresResultat, Map<String, String> hashMap, List<String> listeZones) {
		
		//age100=98 ans
		int age100 = beanParametresResultat.getAge100().intValue() - 1;
		
		/* crée une Map qui a:
		 * 	pour clé : l'identifiants de la zone d'étude
		 * 	pour valeur : une Map qui a : 
		 * 					pour clé : le sexe (0 ou 1)
		 * 					pour valeur : 0.5
		 */
		Map<String, HashMap<Integer, Double>> eDV = initTreeMap2(listeZones);		

		/* crée une map qui a:
		 * 	pour clé : l'identifiant de la zone d'étude
		 * 	pour valeur : une map qui a :
		 * 				pour cle : le sexe (0 ou 1)
		 * 				pour valeur : une liste de taille 99 qui ne contient que des 1 à chaque fois
		 */ 
		Map<String, HashMap<Integer, ArrayList<Double>>> hashMap1 = initTreeMap1(age100 + 1, listeZones);         
		
		/* la Map des données a :
		 * 	pour clé : l'identifiant de la zone d'étude
		 * 	pour valeur : une map qui a :
		 * 					pour cle : le sexe (0 ou 1)
		 *					pour valeur : une liste de taille 98 qui contient les quotients de décès
		 *
		 * C'est ici que l'on récupère les quotients de déces qd selon la zone, le sexe et l'age.
		 */
		Map<String, HashMap<Integer, ArrayList<Double>>> mapZoneSexeAgesQuotients = getListe(beanParametresResultat, hashMap, listeZones);      
		
		//On boucle pour chaque zone de la liste de zone :
		Set<String> listeZonesEtude = mapZoneSexeAgesQuotients.keySet();
		for(String idZoneEtude:listeZonesEtude) {

			//On reprends les données précédentes pour les mettre dans deux nouvelles variables plus claires pour comprendre
			// et pour obtenir les quotients pour une zone donnée et pour les hommes (ou les femmes) dans un tableau d'une seule colonne
			ArrayList<Double> listeQuotientsDecesDesHommesDeLaZone = mapZoneSexeAgesQuotients.get(idZoneEtude).get(0);
			ArrayList<Double> listeQuotientsDecesDesFemmesDeLaZone = mapZoneSexeAgesQuotients.get(idZoneEtude).get(1);
			
			/* mise à jour de hashMap1 */
			//On reprends les données de hasMap1 qui ne sont que des 1 pour l'instant pour chaque zone et chaque sexe pour les mettre dans un tableau d'une seule colonne
			ArrayList<Double> calculIntermediaireHommes  = hashMap1.get(idZoneEtude).get(0);
			ArrayList<Double> calculIntermediaireFemmes  = hashMap1.get(idZoneEtude).get(1);
			
			//calcul intermédiaire pour 0 an : 1-qd(0)       
			calculIntermediaireHommes.set(0, (1 - listeQuotientsDecesDesHommesDeLaZone.get(0)));
			calculIntermediaireFemmes.set(0, (1 - listeQuotientsDecesDesFemmesDeLaZone.get(0)));

			//calcul intermediaire pour les autres ages : Produits de 0 à 98 de (1-qd(age))
			for(int age = 1; age <= age100; age++) {				
				calculIntermediaireHommes.set(age, calculIntermediaireHommes.get(age - 1) * (1 - listeQuotientsDecesDesHommesDeLaZone.get(age)));
				calculIntermediaireFemmes.set(age, calculIntermediaireFemmes.get(age - 1) * (1 - listeQuotientsDecesDesFemmesDeLaZone.get(age)));
			}
			
			//On prend la valeur de eDV pour la zone (pour l'instant 0 pour chaque sexe)
			HashMap<Integer, Double> eDVZoneEtude = eDV.get(idZoneEtude);
			
			//On boucle sur les ages pour avoir un calcul de récurence pour obtenir l'espérance de vie en ajoutant le résultat intermédiaire pour chaque age
			//edv = 0.5 + Sommes de 0 à 99 de ( Produits de 0 à 99 de (1-qd(age) )
			for(int age = 0; age <= age100; age++) {
				Double edvDesHommes = eDVZoneEtude.get(0);
				Double edvDesFemmes = eDVZoneEtude.get(1);
				eDVZoneEtude.put(0, edvDesHommes + calculIntermediaireHommes.get(age));
				eDVZoneEtude.put(1, edvDesFemmes + calculIntermediaireFemmes.get(age));
			}
		}
		return eDV;
	}
	
	
	/** 
	 * crée une Map qui a:
	 * 	pour clé : l'identifiants de la zone d'étude
	 * 	pour valeur : une Map qui a : 
	 * 					pour clé : le sexe (0 ou 1)
	 * 					pour valeur : 0
	 */
	private Map<String, HashMap<Integer, Double>> initTreeMap2(List<String> listeZones) {
		
		HashMap<String, HashMap<Integer, Double>> hashMap = new HashMap<String, HashMap<Integer, Double>>();
		
		// Pour chaque zone, on associe aux hommes (0) puis aux femmes(1) la valeur 0
		for(String zone: listeZones) {			
			hashMap.put(zone, new HashMap<Integer, Double>());
			HashMap<Integer, Double> hashMapZoneEtude = hashMap.get(zone);
			
			hashMapZoneEtude.put(Integer.valueOf(0), new Double(0));
			hashMapZoneEtude.put(Integer.valueOf(1), new Double(0));
		}
		return hashMap;		
	}
	
	
	/**
	 * initialise une map qui a:
	 * pour clé : identifiant de la zone d'étude
	 * pour valeur : une map qui a :
	 * 				pour cle : le sexe (0 ou 1)
	 * 				pour valeur : une liste de taille len qui ne contient que des 1 à chaque fois 
	 * @param len
	 * @param listeZones
	 * @return
	 */
	private Map<String, HashMap<Integer, ArrayList<Double>>> initTreeMap1(int len, List<String> listeZones){
		
		HashMap<String, HashMap<Integer, ArrayList<Double>>> hashMap = new HashMap<String, HashMap<Integer, ArrayList<Double>>>();
		
		//Pour chaque zone, on associe une hashMap qui contient le sexe en clé et une liste de taille len et de valeurs toutes égales à 1
		for(String zone: listeZones){
			
			// arraylist0 size len, valeurs initialisés à 1
			ArrayList<Double> arraylist0 = createArrayListAndInitValuesToValeur(len, 1);
			
			// arraylist1 size len, valeurs initialisés à 1
			ArrayList<Double> arraylist1 = createArrayListAndInitValuesToValeur(len, 1);
					
			//
			HashMap<Integer, ArrayList<Double>> hashMapZoneEtude = new HashMap<Integer, ArrayList<Double>>();
			hashMapZoneEtude.put(Integer.valueOf(0), arraylist0);
			hashMapZoneEtude.put(Integer.valueOf(1), arraylist1);			
			
			hashMap.put(zone, hashMapZoneEtude);
		}
		return hashMap;
	}	


	/**
	 * crée une Map qui a:
	 * 	pour clé : l'identifiant de la zone d'étude
	 * 	pour valeur : une map qui a :
	 * 					pour cle : le sexe (0 ou 1)
	 *					pour valeur : une liste de taille 99 qui contient les quotients de décès
	 *On récupère ici les quotients de déces
	 */
	private Map<String, HashMap<Integer, ArrayList<Double>>> getListe(BeanParametresResultat beanParametresResultat,
																	Map<String, String> hashMap, 
																	List<String> listeZones) {
		/* intitialise une Map qui a :
		 * 	pour clé : l'identifiant de la zone d'étude
		 * 	pour valeur : une map qui a :
		 * 				pour cle : le sexe (0 ou 1)
		 *				pour valeur : une liste de taille 99 qui ne contient que des 0 à chaque fois
		 */
		Map<String, HashMap<Integer, ArrayList<Double>>> hashMap1 = initTreeMap(beanParametresResultat.getAge100().intValue(), listeZones);         
		
		/* on affecte les quotients de décès par zone, par âge et par sexe à cet hashMap*/
		for(Object[] object: findAll(beanParametresResultat, hashMap)) {		
		
			String idZoneEtude = (String) object[0];
			int sexe = ((BigDecimal) object[1]).intValue();
			int age = ((BigDecimal) object[2]).intValue();
			double quotientDeces = ((BigDecimal) object[3]).doubleValue();
			
			HashMap<Integer, ArrayList<Double>> hashMapZoneEtude = hashMap1.get(idZoneEtude);			
			
			if (sexe == 1 && age >= 0 && age <= 98) {				
				hashMapZoneEtude.get(0).set(age, quotientDeces);
			}

			if (sexe == 2 && age >= 0 && age <= 98) {				
				hashMapZoneEtude.get(1).set(age, quotientDeces);
			}
		}
		return hashMap1;
	}
	
	
	/**
	 * initialise une map qui a:
	 * pour clé : identifiant de la zone d'étude
	 * pour valeur : une map qui a :
	 * 				pour cle : le sexe (0 ou 1)
	 * 				pour valeur : une liste de taille len qui ne contient que des 0 à chaque fois 
	 * @param len
	 * @param listeZones
	 * @return
	 */
	private Map<String, HashMap<Integer, ArrayList<Double>>> initTreeMap(int len, List<String> listeZones){
		
		HashMap<String, HashMap<Integer, ArrayList<Double>>> hashMap = new HashMap<String, HashMap<Integer, ArrayList<Double>>>();		
		for(String idZoneEtude: listeZones){			
			// arraylist0 est de taille len et toutes ses valeurs sont 0
			ArrayList<Double> arraylist0 = createArrayListAndInitValuesToValeur(len, 0);            
			
			// arraylist1 est de taille len et toutes ses valeurs sont 0
			ArrayList<Double> arraylist1 = createArrayListAndInitValuesToValeur(len, 0);
			
			// hashMapZoneEtude a pour clé : le sexe (0 ou 1)
			// pour valeur arraylist0 pour l'homme et arraylist1 pour la femme
			HashMap<Integer, ArrayList<Double>> hashMapZoneEtude = new HashMap<Integer, ArrayList<Double>>();         
			hashMapZoneEtude.put(Integer.valueOf(0), arraylist0);
			hashMapZoneEtude.put(Integer.valueOf(1), arraylist1);
			
			hashMap.put(idZoneEtude, hashMapZoneEtude);
		}
		return hashMap;
	}
	
	

	// ex.
	// List<Object[]> list = findAll(..
	// liste.get(0) tableau d'Object
	// liste.get(0)[0] --> identifiant de zone étude
	// liste.get(0)[1] --> sexe
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> quotient de décès
	//C'est la requête permettant d'aller chercher les quotients de décès dans le csv correspondant de l'utilisateur
	private List<Object[]> findAll(BeanParametresResultat beanParametresResultat, Map<String, String> hashMap) {	
		return eDVDao.getListe(beanParametresResultat.getIdUser(), beanParametresResultat.getPrefixe(), hashMap.get("anneeDebut"));         
	}	
	


	/**
	 * crée un ArrayList de longueur len, les valeurs initialisées à valeur
	 * @param len size arrayList
	 */
	private ArrayList<Double> createArrayListAndInitValuesToValeur(int len, double valeur){		
		ArrayList<Double> arrayList = new ArrayList<Double>(len);
		for(int i = 0; i < len; i++) {
			arrayList.add(new Double(valeur));
		}		
		return arrayList;
	}
}


