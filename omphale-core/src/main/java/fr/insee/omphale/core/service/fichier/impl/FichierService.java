package fr.insee.omphale.core.service.fichier.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import fr.insee.omphale.core.service.fichier.IFichierService;
import fr.insee.omphale.core.service.geographie.ICommuneService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.domaine.Utilisateur;

/**
 * Cette classe gère toutes les fonctionnalités pour manipuler des fichiers csv
 * <BR>
 * utilisée lors des créations par import de Zonage ou de Zone
 *
 */
public class FichierService implements IFichierService {

	int compt = 0;
	public Map<String, List<String>> controleFichierCommune(
			File fichierATraiter, ICommuneService communeService)
			throws IOException
			 {
		boolean isValid = true;
		Map<String, List<String>> erreurs = new HashMap<String, List<String>>();
		List<String> erreursFormat = new ArrayList<String>();
		List<String> erreursBase = new ArrayList<String>();
		List<String> premiereLigne = lirePremiereLigne(fichierATraiter);
		if (!premiereLigne.contains("ng")) {
			erreurs.put("erreursPremiereLigne", new ArrayList<String>());
			return erreurs;
		} else if (premiereLigne.size() > 1) {
			List<String> variablesEnTrop = new ArrayList<String>(premiereLigne);
			variablesEnTrop.remove("ng");
			erreurs.put("erreursPremiereLigne", variablesEnTrop);
			return erreurs;
		}

		List<String> codesCommunes = lireDonneesFichier(fichierATraiter);
		List<String> idAuBonFormat = new ArrayList<String>();
		for (String code : codesCommunes) {
			if (!("").equals(code)) {
				if (code.length() != 5) {
					isValid = false;
					erreursFormat.add(code);
				} else {
					idAuBonFormat.add(code);
				}
			}
		}
		if (idAuBonFormat.size() > 0) {
			erreursBase = communeService.findIdNonPresents(idAuBonFormat);
			if (erreursBase.size() > 0) {
				isValid = false;
			}
		}

		if (isValid) {
			return null;
		} else {
			erreurs.put("erreursFormat", erreursFormat);
			erreurs.put("erreursBase", erreursBase);
			return erreurs;
		}
	}

	public Map<String, Object> controleFichierZone(File fichierATraiter,
			Utilisateur utilisateur, IZoneService zoneService)
			throws IOException
			 {
		boolean isValid = true;
		Map<String, List<String>> erreurs = new HashMap<String, List<String>>();
		List<String> erreursFormat = new ArrayList<String>();
		List<String> erreursBase = new ArrayList<String>();
		Map<String, Object> resultat = new HashMap<String, Object>();

		List<String> premiereLigne = lirePremiereLigne(fichierATraiter);
		
		List<String> variablesNonPresentes = new ArrayList<String>();
		if (!premiereLigne.contains("nom")) {
			variablesNonPresentes.add("nom");
		}
		if (!premiereLigne.contains("libelle")) {
			variablesNonPresentes.add("libelle");
		}
		if (variablesNonPresentes.size() > 0) {
			erreurs.put("erreursVariablesManquantes", variablesNonPresentes);
			resultat.put("fichierValide", false);
			resultat.put("erreursFichier", erreurs);
			return resultat;
		} else if (premiereLigne.size() > 2) {
			List<String> variablesEnTrop = new ArrayList<String>(premiereLigne);
			variablesEnTrop.remove("nom");
			variablesEnTrop.remove("libelle");
			erreurs.put("erreursVariablesEnTrop", variablesEnTrop);
			resultat.put("fichierValide", false);
			resultat.put("erreursFichier", erreurs);
			return resultat;
		}

		int indexNom = premiereLigne.indexOf("nom");
		int indexLibelle = premiereLigne.indexOf("libelle");

		List<String> lignes = lireDonneesFichier(fichierATraiter);
		Map<String, String> zonesOk = new HashMap<String, String>();
		for (String ligne : lignes) {
			String[] elements = ligne.split(",");
			if (elements.length < 2) {
				if(elements.length == 0){
					variablesNonPresentes.add("nom");
					variablesNonPresentes.add("libelle");
				}else{
					variablesNonPresentes.add("libelle");
				}
				erreurs.put("erreursVariablesManquantes", variablesNonPresentes);
				resultat.put("fichierValide", false);
				resultat.put("erreursFichier", erreurs);
				return resultat;
			}else if (elements.length > 2) {
				String[] ligneATraiter = ligne.split(",");
				List<String> variablesEnTrop = new ArrayList<String>();
				variablesEnTrop.remove("nom");
				variablesEnTrop.remove("libelle");
				for (int i = 2; i < ligneATraiter.length;i++){
					variablesEnTrop.add(ligneATraiter[i]);
				}
				erreurs.put("erreursVariablesEnTrop", variablesEnTrop);
				resultat.put("fichierValide", false);
				resultat.put("erreursFichier", erreurs);
				return resultat;
			}else{

				String nomZone = elements[indexNom];
				String libelleZone = elements[indexLibelle];

				if (libelleZone.length() > 48) {
					libelleZone = libelleZone.substring(0, 48);
				}


				if (!nomZoneCorrect(nomZone)) {
					isValid = false;
					erreursFormat.add(nomZone);
				} else {
					zonesOk.put(nomZone, libelleZone);
				}
			}

		}

		if (zonesOk.size() > 0) {
			for (String nomZone : zonesOk.keySet()) {
				if (!zoneService.testerNomZone(utilisateur, nomZone)) {
					erreursBase.add(nomZone);
				}
			}
			if (erreursBase.size() > 0) {
				isValid = false;
			}
		}

		if (isValid) {
			resultat.put("fichierValide", true);
			resultat.put("valeursFichier", zonesOk);
			return resultat;
		} else {
			erreurs.put("erreursFormat", erreursFormat);
			erreurs.put("erreursBase", erreursBase);

			resultat.put("fichierValide", false);
			resultat.put("erreursFichier", erreurs);
			return resultat;
		}
	}

	public Map<String, Object> controleFichierCommunesDeZones(
			File fichierATraiter, Map<String, String> zones,
			ICommuneService communeService) throws IOException  {

		boolean isValid = true;
		Map<String, List<String>> erreurs = new HashMap<String, List<String>>();
		List<String> erreursFormat = new ArrayList<String>();
		List<String> erreursBase = new ArrayList<String>();
		List<String> erreursNomZone = new ArrayList<String>();
		List<String> erreurNbreZoneEtCommuneDeZones = new ArrayList<String>();
		List<String> erreursZonesManquantes = new ArrayList<String>();
		List<String> erreursCommunesManquantes = new ArrayList<String>();
		List<String> erreursCommunesEtZonesManquantes = new ArrayList<String>();
		List<String> lignes = lireDonneesFichier(fichierATraiter);
		Map<String, List<String>> valeursFichierOk = new HashMap<String, List<String>>();
		Map<String, Object> resultat = new HashMap<String, Object>();

		List<String> premiereLigne = lirePremiereLigne(fichierATraiter);

		List<String> variablesNonPresentes = new ArrayList<String>();
		if (!premiereLigne.contains("zone")) {
			variablesNonPresentes.add("zone");
		}
		if (!premiereLigne.contains("ng")) {
			variablesNonPresentes.add("ng");
		}
		if (variablesNonPresentes.size() > 0) {
			erreurs.put("erreursVariablesManquantes", variablesNonPresentes);
			resultat.put("fichierValide", false);
			resultat.put("erreursFichier", erreurs);
			return resultat;
		} else if (premiereLigne.size() > 2) {
			List<String> variablesEnTrop = new ArrayList<String>(premiereLigne);
			variablesEnTrop.remove("zone");
			variablesEnTrop.remove("ng");
			erreurs.put("erreursVariablesEnTrop", variablesEnTrop);
			resultat.put("fichierValide", false);
			resultat.put("erreursFichier", erreurs);
			return resultat;
		}

		int indexZone = premiereLigne.indexOf("zone");
		int indexNg = premiereLigne.indexOf("ng");

		for (String ligne : lignes) {
			String[] elem = ligne.split(",");
			if(elem.length <= 1 || "".equals(elem[0])){
				if(elem.length == 0 ){
					erreursCommunesEtZonesManquantes.add("");
				}else{
					if (!"".equals(elem[0])){
						erreursZonesManquantes.add(elem[0]);
					}else {
						erreursCommunesManquantes.add(elem[1]);
					}
				}
				isValid = false;
			}else{
				String nomZone = elem[indexZone];
				String codeCommune = elem[indexNg];
	
				if (!zones.containsKey(nomZone)) {
					erreursNomZone.add(nomZone);
					isValid = false;
				}
				if (codeCommune == null || codeCommune.length() != 5) {
					erreursFormat.add(codeCommune);
					isValid = false;
				} else {
					List<String> codesCommunes = valeursFichierOk.get(nomZone);
					if (codesCommunes == null) {
						codesCommunes = new ArrayList<String>();
					}
					codesCommunes.add(codeCommune);
					valeursFichierOk.put(nomZone, codesCommunes);
				}
			}
		}
		if (valeursFichierOk.size() > 0) {
			List<String> allCodesCommunes = new ArrayList<String>();
			for (String nomZone : valeursFichierOk.keySet()) {
				allCodesCommunes.addAll(valeursFichierOk.get(nomZone));
			}
			erreursBase = communeService.findIdNonPresents(allCodesCommunes);
			if (erreursBase.size() > 0) {
				isValid = false;
			}
		}

		if (            zones.size() != valeursFichierOk.keySet().size()     &&
						erreursZonesManquantes.size() == 0 &&
						erreursCommunesManquantes.size() == 0 &&
						erreursCommunesEtZonesManquantes.size() == 0
			){
			for (String nomZoneEnCours : zones.keySet()){
				if(valeursFichierOk.get(nomZoneEnCours)== null){
					erreurNbreZoneEtCommuneDeZones.add(nomZoneEnCours);
				}
			}
			isValid = false;
		}
		
		if (isValid) {
			resultat.put("fichierValide", true);
			resultat.put("valeursFichier", valeursFichierOk);
			return resultat;
		} else {
			resultat.put("fichierValide", false);
			erreurs.put("erreursFormat", erreursFormat);
			erreurs.put("erreursBase", erreursBase);
			erreurs.put("erreursNomZone", erreursNomZone);
			resultat.put("erreursFichier", erreurs);
			erreurs.put("erreurNbreZoneEtCommuneDeZones", erreurNbreZoneEtCommuneDeZones);
			erreurs.put("erreursZonesManquantes", erreursZonesManquantes);
			erreurs.put("erreursCommunesManquantes", erreursCommunesManquantes);
			erreurs.put("erreursCommunesEtZonesManquantes", erreursCommunesEtZonesManquantes);
			return resultat;
		}
	}


	public List<String> lireDonneesFichier(File fichierATraiter) throws IOException {
		List<String> tab = new ArrayList<String>();
		InputStream ips = new FileInputStream(fichierATraiter);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String ligne;
		// On ne lit pas la premiere ligne qui contient les noms de variables
		br.readLine();
		while ((ligne = br.readLine()) != null) {
			if( !ligne.isEmpty()){
				tab.add(ligne);
			}
			
		}
		ips.close();
		ipsr.close();
		br.close();
		return tab;
	}
	
	/**
	 * Lire la première ligne d'un fichier
	 * 
	 * @param fichierATraiter
	 * @return {@link List}<{@link String}>
	 * @throws IOException
	 */
	public List<String> lirePremiereLigne(File fichierATraiter) throws IOException
	 {
		List<String> resultat = new ArrayList<String>();
		InputStream ips = new FileInputStream(fichierATraiter);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String premiereLigne = br.readLine();
		String[] elem = premiereLigne.split(",");
		for (String string : elem) {
			resultat.add(string);
		}
		ips.close();
		ipsr.close();
		br.close();
		return resultat;
	}

	/**
	 * Enlever les doublons d'une ArrayList
	 * 
	 * @param listeATraiter
	 * @return {@link List}<{@link String}>
	 */
	public List<String> enleverDoublonArrayList(List<String> listeATraiter) {
		Set<String> setSansDoublon = new LinkedHashSet<String>(listeATraiter);
		List<String> listeSansDoublon = new LinkedList<String>(setSansDoublon);
		return listeSansDoublon;
	}

	/**
	 * Vérifie que le nom de la zone est correct
	 * 
	 * @param nom
	 * @return {@link Boolean}
	 */
	private boolean nomZoneCorrect(String nom) {
		if (("").equals(nom)) {
			return false;
		} else if (nom.length() > 20) {
			return false;
		} else if (nom.charAt(0) == '_') {
			return false;
		} else if (!Pattern.matches("[a-zA-Z_0-9]*", nom)) {
			return false;
		}
		return true;
	}
}
