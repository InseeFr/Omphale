package fr.insee.omphale.core.service.projection;

import java.util.List;

import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.TypeEntite;


public interface IHypotheseService {

	/**
	 * Compter le nombre d'hypothèses créées par un utilisateur
	 * 
	 * @param utilisateur
	 * @return {@link Long}
	 */
	public long compterHypotheses(Utilisateur utilisateur);

	/**
	 * Recherche les hypothèses d'un utilisateur
	 * 
	 * @param utilisateur
	 * @return {@link List}<{@link Hypothese}>
	 */
	public List<Hypothese> findByUtilisateur(Utilisateur utilisateur);

	/**
	 * vérifie que le nom d'une hypothèse choisie n'existe pas déjà 
	 * <BR>
	 * parmi les hypothèses existentes d'un utilisateur
	 * 
	 * @param utilisateur
	 * @param nom
	 * @return {@link Boolean}
	 */
	public boolean testerNomHypothese(Utilisateur utilisateur, String nom);

	/**
	 * recherche les hypothèses selon un type d'entité choisi
	 * 
	 * @param typeEntite
	 * @return {@link List}<{@link Hypothese}>
	 */
	public List<Hypothese> findByTypeEntite(TypeEntite typeEntite);

	/**
	 * insère en base une hypothèse à partir de son instance
	 * 
	 * @param hypothese
	 * @return {@link Hypothese}
	 */
	public Hypothese insertOrUpdate(Hypothese hypothese);

	/**
	 * Recherche une hypothèse selon son identifiant
	 * 
	 * @param id
	 * @return {@link Hypothese}
	 */
	public Hypothese findById(int id);

	/**
	 * Supprime en base une hypothèse à partir de son instance
	 * 
	 * @param hypothese
	 */
	public void delete(Hypothese hypothese);

	/**
	 * Recherche toutes les hypothèses standards
	 * 
	 * @return {@link List}<{@link Hypothese}>
	 */
	public List<Hypothese> findHypothesesStandards();

	/**
	 * recherche les hypothèses 
	 * <BR>
	 * selon quelles soient standards ou non
	 * <BR>
	 * selon leur type d'entité 
	 * 
	 * @param standard
	 * @param codeTypeEntite
	 * @param hypotheses
	 * @param hypotheseService
	 * @return {@link List}<{@link Hypothese}>
	 */
	public List<Hypothese> filtrerParStandardEtTypeEntite(Integer standard,
			String codeTypeEntite, List<Hypothese> hypotheses, IHypotheseService hypotheseService);
	
	/**
	 * Recherche les hypothèses standards d'un utilisateur
	 * 
	 * @param utilisateur
	 * @return
	 */
	public List<Hypothese> findHypothesesStandardsByUtilisateur(Utilisateur utilisateur);
	
	/**
	 * Recherche les hypothèses NON standards d'un utilisateur
	 * 
	 * @param utilisateur
	 * @return {@link List}<{@link Hypothese}>
	 */
	public List<Hypothese> findHypothesesNonStandardsByUtilisateur(Utilisateur utilisateur);
	
	/**
	 * Recherche les hypothèses selon les paramètres suivantes
	 * 
	 * @param entite
	 * @param idep
	 * @param hypotheseService
	 * @return {@link List}<{@link Hypothese}>
	 */
	public List<Hypothese> findByTypeEntitePourENLGestionParams(TypeEntite entite, String idep, IHypotheseService hypotheseService);
	
	/**
	 * Renvoie un boolean correspondant selon la valeur des paramètres suivants
	 * 
	 * @param standard
	 * @param codeTypeEntite
	 * @param hypothese
	 * @return {@link Boolean}
	 */
	public boolean filtrerHypothese(Integer standard, String codeTypeEntite,
			Hypothese hypothese);
	
	/**
	 * Supprime les données d'une hypothese dans table CB_Hypothese
	 * 
	 * @param hypothese
	 */
	public void supprimerAvecCB_Hypothese(Hypothese hypothese);
	
	/**
	 * recherche si les hypothéses d'un utilisateur
	 * <BR>
	 * utilisées seulement par l'utilisateur (non partagé)
	 * <BR>
	 * utilisées par d'autres utilisateurs (partagé)
	 * @param utilisateur
	 * @param hypothesesUtiliseesParUtilisateur
	 * @param hypothesesUtiliseesParDautres
	 * @param nomHypothesesUtiliseesParUtilisateur
	 * @param nomHypothesesUtiliseesParDautres
	 */
	public List<Object> rechercheHypothesesPourFonctionSuppression( 	Utilisateur utilisateur,
																		List<Integer> hypothesesASupprimer,
																		List<Integer> hypothesesAConserver,
																		List<String> nomHypothesesASupprimer,
																		List<String> nomHypothesesAConserver);

	public int deleteHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId);

	public int deleteCbHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId);
}
