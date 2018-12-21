package fr.insee.omphale.dao.projection;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.TypeEntite;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour Hypothese
 *
 */
public interface IHypotheseDAO extends IGenericDAO<Hypothese, Integer> {

	/**
	 * Compte le nombre d'hypothèses créées par un utilisateur donné
	 * 
	 * @param utilisateur
	 * @return {@link Long}
	 */
	public long compterHypotheses(Utilisateur utilisateur);

	/**
	 * recherche les hypothèses d'un utilisateur
	 * 
	 * @param utilisateur
	 * @return {@link List}<{@link Hypothese}>
	 */
	public List<Hypothese> findByUtilisateur(Utilisateur utilisateur);
	
	/**
	 * recherche les hypothèses associées à un type d'entité
	 * 
	 * @param typeEntite
	 * @return {@link List}<{@link Hypothese}>
	 */
	public List<Hypothese> findByTypeEntite(TypeEntite typeEntite) ;
	
	public List<Integer> findByIdPartagee(Utilisateur utilisateur);
	
	public List<Integer> findByIdNonPartagee(Utilisateur utilisateur);

	public List<String> findByNomPartagee(Utilisateur utilisateur);

	public List<String> findByNomNonPartagee(Utilisateur utilisateur);

	public int deleteHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId);

	public int deleteCbHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId);
	
}
