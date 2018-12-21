package fr.insee.omphale.core.service.projection;

import java.util.List;
import java.util.Set;

import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.projection.EvolDeScenario;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Hypothese;

public interface IEvolutionNonLocaliseeService {

	/**
	 * Déplace vers le haut ou vers le bas des EvolutionNonLocalisee d'une liste <br>
	 * ex. : <br>
	 * EvolutionNonLocalisee evolutionNL1 = new EvolutionNonLocalisee(); <br>
	 * evolutionNL1.setId(1); <br>
	 * EvolutionNonLocalisee evolutionNL2 = new EvolutionNonLocalisee(); <br>
	 * evolutionNL1.setId(2); <br>
	 * EvolutionNonLocalisee evolutionNL3 = new EvolutionNonLocalisee(); <br>
	 * evolutionNL1.setId(3); <br>
	 * EvolutionNonLocalisee evolutionNL4 = new EvolutionNonLocalisee(); <br>
	 * evolutionNL1.setId(4); <br>
	 * EvolutionNonLocalisee evolutionNL5 = new EvolutionNonLocalisee(); <br>
	 * evolutionNL1.setId(5); <br>
	 * List evolutionsNL = new ArrayList(5); <br>
	 * evolutionsNL.add(evolutionNL1); <br>
	 * evolutionsNL.add(evolutionNL2); <br>
	 * evolutionsNL.add(evolutionNL3); <br>
	 * evolutionsNL.add(evolutionNL4); <br>
	 * evolutionsNL.add(evolutionNL5); <br>
	 * <br>
	 * advance(evolutionsNL, {new Integer[3], new Integer[4]}, String "avancer") <br>
	 * <br>
	 * Résultat : <br>
	 * <br>
	 * evolutionsNL.get(0) = evolutionNL1 <br>
	 * evolutionsNL.get(1) = evolutionNL3 <br>
	 * evolutionsNL.get(2) = evolutionNL4 <br>
	 * evolutionsNL.get(3) = evolutionNL2 <br>
	 * evolutionsNL.get(4) = evolutionNL5 <br>
	 * <br>
	 * si advance(evolutionsNL, {new Integer[2]}, String "bottom"), nouveau
	 * résultat : <br>
	 * <br>
	 * evolutionsNL.get(0) = evolutionNL1 <br>
	 * evolutionsNL.get(1) = evolutionNL3 <br>
	 * evolutionsNL.get(2) = evolutionNL4 <br>
	 * evolutionsNL.get(3) = evolutionNL5 <br>
	 * evolutionsNL.get(4) = evolutionNL2
	 * 
	 * @param evolutionsNL
	 *            liste d'évolutions non localisees
	 * @param evolutionsNLId
	 *            identifiants des évolutions non localisees à avancer vers le
	 *            haut ou vers la bas
	 * @param advance
	 *            "avancer", "bottom"
	 * 
	 */
	public void advance(List<EvolutionNonLocalisee> evolutionsNL,
			List<Integer> evolutionsNLId, String advance);

	/**
	 * Méthode pour compter le nombre de ENL créé par un utilisateur 
	 * 
	 * @param utilisateur
	 * @return long
	 */
	public long compterEvolutionsNonLocalisees(Utilisateur utilisateur);

	/**
	 * supprime en base une évolution non localisée à partir de son instance
	 * 
	 * @param evolution
	 */
	public void delete(EvolutionNonLocalisee evolution);

	/**
	 * Recherche dans la base de données la liste des évolutions non localisées
	 * ayant pour standard un standard donné et pour composante une composante
	 * donnée
	 * 
	 * @param standard
	 *            standard
	 * @param composante
	 *            composante return liste d'évolutions non localisées
	 */
	public List<EvolutionNonLocalisee> findAll(Boolean standard,
			String composante);

	/**
	 * Recherche dans la base de données la liste des évolutions non localisées
	 * ayant pour identifiant l'un des identifiants d'un tableau d'identifiants
	 * donnés. <br>
	 * Le résultat est classé suivant le tableau d'identifiants donné
	 * 
	 * @param listeIdentifiantENL
	 *            tableau d'identifiants return liste d'évolutions non
	 *            localisées
	 */
	public List<EvolutionNonLocalisee> findAll(List<Integer> listeIdentifiantENL);

	/**
	 * Recherche dans la base de données la liste des évolutions non localisées
	 * ayant pour idUser un idUser donné, pour standard un standard donné et
	 * pour composante une composante donnée
	 * 
	 * @param idUser
	 *            idUser
	 * @param standard
	 *            standard
	 * @param composante
	 *            composante return liste d'évolutions non localisées
	 */
	public List<EvolutionNonLocalisee> findAll(String idUser, Boolean standard,
			String composante);

	/**
	 * Recherche dans la base de données la liste des évolutions non localisées
	 * ayant pour idUser un idUser donné, pour standard un standard donné, pour
	 * composante une composante donnée et n'appartenant pas à une liste
	 * d'évolutions non localisées donnée.
	 * 
	 * @param idUser
	 *            idUser
	 * @param standard
	 *            standard
	 * @param composante
	 *            composante
	 * @param listeEvolutionNonLocalisee
	 *            liste d'évolutions non localisées donnée return liste
	 *            EvolutionNonLocalisee
	 */
	public List<EvolutionNonLocalisee> findAll(String idUser, Boolean standard,
			String composante,
			List<EvolutionNonLocalisee> listeEvolutionNonLocalisee);

	/**
	 * Recherche dans la base de données la liste des évolutions non localisées
	 * ayant pour identifiant l'un des identifiants d'un tableau d'identifiants
	 * donnés. <br>
	 * Le résultat est classé suivant l'ordre par défaut : <br>
	 * anneeDeb croissant, <br>
	 * anneeFin décroissant, <br>
	 * sexeDeb != sexeFin, <br>
	 * sexeDeb = sexeFin = 1, <br>
	 * sexeDeb = sexeFin = 2, <br>
	 * ageDeb croissant, <br>
	 * ageFin décroissant
	 * 
	 * @param listeIdentifiantENL
	 * @return liste d'évolutions non localisées
	 */
	public List<EvolutionNonLocalisee> findAllOrdreDefaut(
			List<Integer> listeIdentifiantENL);

	/**
	 * Méthode qui ramène les évolutions utilisant l'hypothèse passée en
	 * argument
	 */
	public List<EvolutionNonLocalisee> findByHypothese(Hypothese hypothese);

	/**
	 * Méthode pour trouver une ENL en fonction de son id 
	 * 
	 * @param utilisateur
	 * @return List(EvolutionNonLocalisee)
	 */

	public EvolutionNonLocalisee findById(int id);

	/**
	 * Méthode pour récupérer les évolutions non localisées personnelles créées
	 * par un utilisateur 
	 * 
	 * @param utilisateur
	 * @return List(EvolutionNonLocalisee)
	 */

	public List<EvolutionNonLocalisee> findByUtilisateur(Utilisateur utilisateur);

	/**
	 * Recherche les ENL standards 
	 * 
	 * @param id
	 * @return EvolutionNonLocalisee
	 */
	public List<EvolutionNonLocalisee> findStandard();

	/**
	 * Recherche une EvolutionNonLocalisee dans une liste d'évolutions non
	 * localisees à partir d'un identifiant
	 * 
	 * @param liste
	 *            liste d'évolutions non localisees
	 * @param id
	 *            identifiant d'évolutions non localisees
	 * @return
	 */
	public EvolutionNonLocalisee get(List<EvolutionNonLocalisee> liste,
			Integer id);

	/**
	 * Donne les évolutions non localisées d'une liste d'évolutions de scenario,
	 * classées par rang de chaque évolution de scenario
	 * 
	 * @param evolutionsDeScenario
	 *            liste d'évolutions de scenario
	 * @return liste d'évolutions non localisées
	 */
	public List<EvolutionNonLocalisee> getListe(
			Set<EvolDeScenario> evolutionsDeScenario);

	/**
	 * identifiants d'une liste d'évolutions non localisees
	 * 
	 * @param liste
	 *            liste d'évolutions non localisees
	 * @return
	 */
	public List<Integer> getTableauId(List<EvolutionNonLocalisee> liste);

	/**
	 * Sauvegarde ou met à jour dans la base de données une évolution non
	 * localisée une liste d'évolutions non localisées donnée.
	 * 
	 * @param evolutionNonLocalisee
	 *            evolutionNonLocalisee
	 */
	public EvolutionNonLocalisee insertOrUpdate(EvolutionNonLocalisee evolutionNonLocalisee);

	/**
	 * Méthode pour tester si un nom d'évol est déjà utilisé par une évol créée
	 * par l'utilisateur 
	 * 
	 * @param utilisateur
	 * @param nomEvolution
	 * @return boolean
	 */
	/**
	 * vérifie que le nom de l'évolution choisie n'existe pas déjà pour 
	 * <BR>
	 * pour les évolutions non localisées de l'utilisateur
	 * 
	 * @param utilisateur
	 * @param nomEvolution
	 * @return {@link Boolean}
	 */
	public boolean testerNomEvolutionNonLocalisee(Utilisateur utilisateur,
			String nomEvolution);

	/**
	 * Récupérer une liste d'évolution non localisée
	 * <BR>
	 * selon quelles soient standards ou non 
	 * <BR>
	 * et selon la composante choisie
	 * 
	 * @param standard
	 * @param codeComposante
	 * @param enls
	 * @return {@link List}<{@link EvolutionNonLocalisee}>
	 */
	public List<EvolutionNonLocalisee> filtrerParStandardEtComposante(
			Integer standard, String codeComposante,
			List<EvolutionNonLocalisee> enls);
	
	/**
	 * Recherche les évolutions non localisées standards d'un utilisateur
	 * 
	 * @param utilisateur
	 * @return {@link List}<{@link EvolutionNonLocalisee}>
	 */
	public List<EvolutionNonLocalisee> findENLStandardsByUtilisateur(Utilisateur utilisateur);
	
	/**
	 * Recherche les évolutions non localisées NON standards d'un utilisateurs
	 * 
	 * @param utilisateur
	 * @return {@link List}<{@link EvolutionNonLocalisee}>
	 */
	public List<EvolutionNonLocalisee> findENLNonStandardsByUtilisateur(Utilisateur utilisateur);
	
	
	
	/**
	 * Recherche les propriétaires d'une Evolution Non Localisee associée à une zone précise
	 * <BR>
	 * Cela permet dans la fonctionnalité suppression de savoir
	 * <BR>
	 * si une zone d'un utilisateur est utilisée par d'autre utilisateur par l'intermédiaire d'une ENL
	 * 
	 * @param zone
	 * @return List<String>
	 */
	public List<String> findUtilisateurs(Zone zone);
	
	/**
	 * Recherche les évolutions non localisées d'un utilisateur standard et non standard
	 * 
	 * 
	 * @param utilisateur
	 * @return List<EvolutionNonLocalisee>
	 */
	public List<EvolutionNonLocalisee> findByUtilisateurStandardOuNon(Utilisateur utilisateur);
	
	
	public List<Object> rechercheEvolutionsNonLocaliseesPourFonctionSuppression(	Utilisateur utilisateur,
			List<Integer> evolutionsNonLocaliseesUtiliseesParUtilisateur,
			List<Integer> evolutionsNonLocaliseesUtiliseesParDautres,
			List<String> nomEvolutionsNonLocaliseesUtiliseesParUtilisateur,
			List<String> nomEvolutionsNonLocaliseesUtiliseesParDautres);

	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId);

	public int deleteHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId);
}
