package fr.insee.omphale.core.service.fichier.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.insee.omphale.core.service.fichier.IFichierHypotheseService;
import fr.insee.omphale.core.service.impl.Service;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.TypeEntite;

/**
 * Classe qui gère toutes les fonctionnalités pour manipuler un fichier Hypothese
 *
 */
public class FichierHypotheseService implements IFichierHypotheseService {

	// titre des colonnes dans le fichier
	private final String libAnnee = "annee";
	private final String libAge = "age";
	private final String libSexe = "sexe";
	private final String libValeur = "valeur";
	// caractère qui sépare les variables dans le fichier
	private final String separateur = ",";

	public Map<String, Object> controleFichierHypothese(File fichierATraiter,
			Hypothese hypothese) throws Exception {

		boolean fichierValide = true;

		// valeurs du fichier : Map<annee,Map<age,Map<sexe,valeur>>> , age et
		// sexe peuvent valoir 0 dans le cas ou ils ne sont pas requis par le
		// type d'entité
		Map<Integer, Map<Integer, Map<Integer, Double>>> valeurs = new HashMap<Integer, Map<Integer, Map<Integer, Double>>>();

		Map<String, Object> erreursFormat = new HashMap<String, Object>();

		Map<Integer, String> erreursFormatAnnee = new HashMap<Integer, String>();
		Map<Integer, String> erreursFormatAge = new HashMap<Integer, String>();
		Map<Integer, String> erreursFormatSexe = new HashMap<Integer, String>();
		Map<Integer, String> erreursFormatValeur = new HashMap<Integer, String>();
		List<Integer> erreursFormatManquante = new ArrayList<Integer>();

		Map<String, Object> erreursCoherence = new HashMap<String, Object>();

		Map<Integer, String> erreursCoherenceDoublon = new HashMap<Integer, String>();
		List<String> erreursCoherenceExhaustivite = new ArrayList<String>();

		Map<String, Object> resultat = new HashMap<String, Object>();

		// Controle première ligne

		List<String> premiereLigne = lirePremiereLigne(fichierATraiter);

		int rangAnnee = -1;
		int rangAge = -1;
		int rangSexe = -1;
		int rangValeur = -1;

		List<String> variablesNonPresentes = new ArrayList<String>();
		int nbVariables = 2;
		if (!premiereLigne.contains(libAnnee)) {
			variablesNonPresentes.add(libAnnee);
		} else {
			rangAnnee = premiereLigne.indexOf(libAnnee);
		}
		if (!premiereLigne.contains(libValeur)) {
			variablesNonPresentes.add(libValeur);
		} else {
			rangValeur = premiereLigne.indexOf(libValeur);
		}
		if (hypothese.getTypeEntite().getAge()) {
			nbVariables++;
			if (!premiereLigne.contains(libAge)) {
				variablesNonPresentes.add(libAge);
			} else {
				rangAge = premiereLigne.indexOf(libAge);
			}
		}
		if (hypothese.getTypeEntite().getSexe()) {
			nbVariables++;
			if (!premiereLigne.contains(libSexe)) {
				variablesNonPresentes.add(libSexe);
			} else {
				rangSexe = premiereLigne.indexOf(libSexe);
			}
		}

		if (variablesNonPresentes.size() > 0) {
			resultat.put("erreursVariablesManquantes", variablesNonPresentes);
			resultat.put("fichierValide", false);
			return resultat;
		} else if (premiereLigne.size() > nbVariables) {
			List<String> variablesEnTrop = new ArrayList<String>(premiereLigne);
			variablesEnTrop.remove(libAnnee);
			variablesEnTrop.remove(libValeur);
			if (hypothese.getTypeEntite().getAge()) {
				variablesEnTrop.remove(libAge);
			}
			if (hypothese.getTypeEntite().getSexe()) {
				variablesEnTrop.remove(libSexe);
			}
			resultat.put("erreursVariablesEnTrop", variablesEnTrop);
			resultat.put("fichierValide", false);
			return resultat;
		}

		// Controles du format des données et remplissage de la map valeurs si
		// le format est ok
		List<String> lignes = lireDonneesFichier(fichierATraiter);
		int numeroLigne = 1;

		int nbColonnes = 2;
		if (hypothese.getTypeEntite().getAge()) {
			nbColonnes++;
		}
		if (hypothese.getTypeEntite().getSexe()) {
			nbColonnes++;
		}

		for (String ligne : lignes) {
			numeroLigne++;
			String[] elem = ligne.split(separateur);

			String stringAnnee = "";
			String stringAge = "-10";
			String stringSexe = "0";
			String stringValeur = "";

			if (elem.length != nbColonnes) {
				erreursFormatManquante.add(numeroLigne);
				fichierValide = false;
			} else {

				stringAnnee = elem[rangAnnee];
				if (Service.testFormatAnnee(stringAnnee) == false) {
					fichierValide = false;
					erreursFormatAnnee.put(numeroLigne, stringAnnee);
				}

				stringValeur = elem[rangValeur];
				if (testFormatValeur(stringValeur, hypothese.getTypeEntite()) == false) {
					fichierValide = false;
					erreursFormatValeur.put(numeroLigne, stringValeur);
				}

				if (hypothese.getTypeEntite().getAge()) {
					stringAge = elem[rangAge];
					if (Service.testFormatAge(stringAge) == false) {
						fichierValide = false;
						erreursFormatAge.put(numeroLigne, stringAge);
					}
				}

				if (hypothese.getTypeEntite().getSexe()) {
					stringSexe = elem[rangSexe];
					if (testFormatSexe(stringSexe) == false) {
						fichierValide = false;
						erreursFormatSexe.put(numeroLigne, stringSexe);
					}
				}
			}

			// remplissage des valeurs (on ne remplit plus si une erreur de
			// format a déja été détectée)
			if (fichierValide) {
				int annee = Integer.valueOf(stringAnnee);
				int age = Integer.valueOf(stringAge);
				int sexe = Integer.valueOf(stringSexe);
				double valeur = Double.valueOf(stringValeur);

				if (valeurs.get(annee) == null) {
					valeurs.put(annee,
							new HashMap<Integer, Map<Integer, Double>>());
				}
				if (valeurs.get(annee).get(age) == null) {
					valeurs.get(annee).put(age, new HashMap<Integer, Double>());
				}
				if (valeurs.get(annee).get(age).get(sexe) == null) {
					valeurs.get(annee).get(age).put(sexe, valeur);
				} else {
					String erreurDoublon = "" + annee;
					if (hypothese.getTypeEntite().getAge()) {
						erreurDoublon = erreurDoublon + "|" + age;
					}
					if (hypothese.getTypeEntite().getSexe()) {
						erreurDoublon = erreurDoublon + "|" + sexe;
					}
					erreursCoherenceDoublon.put(numeroLigne, erreurDoublon);
				}
			}
		} // fin lecture fichier ligne par ligne

		// Si le fichier n'est pas valide, on renvoie les erreurs de format
		// et on arrête le contrôle ici

		if (fichierValide == false) {
			resultat.put("fichierValide", false);

			erreursFormat.put("erreursFormatAnnee", erreursFormatAnnee);
			erreursFormat.put("erreursFormatAge", erreursFormatAge);
			erreursFormat.put("erreursFormatSexe", erreursFormatSexe);
			erreursFormat.put("erreursFormatValeur", erreursFormatValeur);
			erreursFormat.put("erreursFormatManquante", erreursFormatManquante);

			resultat.put("erreursFormat", erreursFormat);
			return resultat;
		}

		// Controles de cohérence (dont le contrôle
		// d'exhaustivité)
		erreursCoherenceExhaustivite = testExhaustivite(valeurs, hypothese);
		if (!erreursCoherenceExhaustivite.isEmpty()) {
			fichierValide = false;
		}

		if (!erreursCoherenceDoublon.isEmpty()) {
			fichierValide = false;
		}

		// Si le fichier n'est pas valide, on renvoie les erreurs de
		// cohérence et on arrête le controle ici
		if (fichierValide == false) {
			resultat.put("fichierValide", false);

			erreursCoherence.put("erreursCoherenceDoublon",
					erreursCoherenceDoublon);
			erreursCoherence.put("erreursCoherenceExhaustivite",
					erreursCoherenceExhaustivite);

			resultat.put("erreursCoherence", erreursCoherence);
			resultat.put("valeursFichier", valeurs);
			return resultat;
		}

		resultat.put("fichierValide", true);
		resultat.put("valeursFichier", valeurs);
		return resultat;
	}

	public List<String> testExhaustivite(
			Map<Integer, Map<Integer, Map<Integer, Double>>> valeurs,
			Hypothese hypothese) {
		List<String> erreursCoherenceExhaustivite = new ArrayList<String>();
		int anneeMin = hypothese.getAnneeDebut() == null ? Service
				.getMin(valeurs.keySet()) : hypothese.getAnneeDebut();
		int anneeMax = hypothese.getAnneeFin() == null ? Service.getMax(valeurs
				.keySet()) : hypothese.getAnneeFin();

		Integer ageMin = hypothese.getAgeDebut();
		Integer ageMax = hypothese.getAgeFin();
		Integer sexeMin = hypothese.getSexeDebut();
		Integer sexeMax = hypothese.getSexeFin();

		if (hypothese.getTypeEntite().getAge()) {
			Set<Integer> agesPresents = new HashSet<Integer>();
			for (Integer annee : valeurs.keySet()) {
				agesPresents.addAll(valeurs.get(annee).keySet());
			}
			ageMin = ageMin == null ? Service.getMin(agesPresents) : ageMin;
			ageMax = ageMax == null ? Service.getMax(agesPresents) : ageMax;
		}
		if (hypothese.getTypeEntite().getSexe()) {
			Set<Integer> sexesPresents = new HashSet<Integer>();
			for (Integer annee : valeurs.keySet()) {
				for (Integer age : valeurs.get(annee).keySet()) {
					sexesPresents.addAll(valeurs.get(annee).get(age).keySet());
				}
			}
			sexeMin = sexeMin == null ? Service.getMin(sexesPresents) : sexeMin;
			sexeMax = sexeMax == null ? Service.getMax(sexesPresents) : sexeMax;
		}
		for (int anneeEnCours = anneeMin; anneeEnCours <= anneeMax; anneeEnCours++) {
			// Le cas ou l'année n'est pas renseignée
			if (valeurs.get(anneeEnCours) == null) {
				if (hypothese.getTypeEntite().getAge()) {
					for (int age = ageMin; age <= ageMax; age++) {
						if (hypothese.getTypeEntite().getSexe()) {
							for (int sexe = sexeMin; sexe <= sexeMax; sexe++) {
								erreursCoherenceExhaustivite.add(anneeEnCours
										+ "|" + age + "|" + sexe);
							}
						} else {
							erreursCoherenceExhaustivite.add(anneeEnCours + "|"
									+ age);
						}
					}
				} else {
					if (hypothese.getTypeEntite().getSexe()) {
						for (int sexe = sexeMin; sexe <= sexeMax; sexe++) {
							erreursCoherenceExhaustivite.add(anneeEnCours + "|"
									+ sexe);
						}
					} else {
						erreursCoherenceExhaustivite.add(anneeEnCours + "");
					}
				}
			} else {
				// Le cas ou l'année est renseignée
				if (hypothese.getTypeEntite().getAge()) {
					for (int ageEnCours = ageMin; ageEnCours <= ageMax; ageEnCours++) {
						// Le cas ou l'age n'est pas renseigné
						if (valeurs.get(anneeEnCours).get(ageEnCours) == null) {
							if (hypothese.getTypeEntite().getSexe()) {
								for (int sexe = sexeMin; sexe <= sexeMax; sexe++) {
									erreursCoherenceExhaustivite
											.add(anneeEnCours + "|"
													+ ageEnCours + "|" + sexe);
								}
							} else {
								erreursCoherenceExhaustivite.add(anneeEnCours
										+ "|" + ageEnCours);
							}
						} else {
							// Le cas ou l'age est renseigné
							if (hypothese.getTypeEntite().getSexe()) {
								for (int sexeEnCours = sexeMin; sexeEnCours <= sexeMax; sexeEnCours++) {
									if (valeurs.get(anneeEnCours).get(
											ageEnCours).get(sexeEnCours) == null) {
										erreursCoherenceExhaustivite
												.add(anneeEnCours + "|"
														+ ageEnCours + "|"
														+ sexeEnCours);
									}
								}
							}
						}
					}
				} else {
					if (hypothese.getTypeEntite().getSexe()) {
						for (int sexe = sexeMin; sexe <= sexeMax; sexe++) {
							if (valeurs.get(anneeEnCours).get(-10).get(sexe) == null) {
								erreursCoherenceExhaustivite.add(anneeEnCours
										+ "|" + sexe);
							}
						}
					}
				}
			}
		}
		return erreursCoherenceExhaustivite;
	}

	/**
	 * vérifie que le sexe est bien renseigné
	 * 
	 * @param sexe
	 * @return {@link Boolean}
	 */
	private boolean testFormatSexe(String sexe) {
		if (sexe.equals("1") || sexe.equals("2")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * vérifie que les valeurs min et max sont bien renseignées
	 * 
	 * @param valeur
	 * @param type
	 * @return {@link Boolean}
	 */
	private boolean testFormatValeur(String valeur, TypeEntite type) {
		try {
			if (type.getMin() != null) {
				if (Double.valueOf(valeur) < type.getMin()) {
					return false;
				}
			}
			if (type.getMax() != null) {
				if (Double.valueOf(valeur) > type.getMax()) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Lit les données d'un ficher
	 * 
	 * @param fichierATraiter
	 * @return {@link List}<{@link String}>
	 * @throws Exception
	 */
	public List<String> lireDonneesFichier(File fichierATraiter)
			throws Exception {
		return new FichierService().lireDonneesFichier(fichierATraiter);
	}

	/**
	 * Lire la première ligne d'un fichier
	 * 
	 * @param fichierATraiter
	 * @return {@link List}<{@link String}>
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> lirePremiereLigne(File fichierATraiter)
			throws FileNotFoundException, IOException {
		List<String> resultat = new ArrayList<String>();
		InputStream ips = new FileInputStream(fichierATraiter);
		InputStreamReader ipsr = new InputStreamReader(ips);
		BufferedReader br = new BufferedReader(ipsr);
		String premiereLigne = br.readLine();
		String[] elem = premiereLigne.split(separateur);
		for (String string : elem) {
			resultat.add(string);
		}
		ips.close();
		ipsr.close();
		br.close();
		return resultat;
	}

}
