package fr.insee.omphale.dao.projection;

import java.util.List;
import java.util.Set;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.EvolutionLocalisee;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Projection;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour EvolutionLocalisee
 *
 */
public interface IEvolutionLocaliseeDAO extends
		IGenericDAO<EvolutionLocalisee, Integer> {

	
	
	/**
	 * Trouve une évolution localisée d'une projection et d'une composante données,
	 * <BR>
	 * en fonction de son rang
	 * @param projection
	 * @param composante
	 * @param rang
	 * @return EvolutionLocalisee
	 */
	public  EvolutionLocalisee findByRang(Projection projection, Composante composante, int rang );
	
	
	/**
	 * Recherche les évolutions localisées associées à une évolution non localisée donnée
	 * 
	 * @param enl
	 * @return {@link List}<{@link EvolutionLocalisee}>
	 */
	public List<EvolutionLocalisee> findByENL(EvolutionNonLocalisee enl);
	
	/**
	 * Lance un flush des objets evolution localisée
	 * 
	 * @return Boolean
	 */
	public Boolean nettoyage();
	
	/**
	 * recherche les évolutions localisées associées à une projection donnée
	 * 
	 * @param projection
	 * @return {@link Set}<{@link EvolutionLocalisee}>
	 */
	public Set<EvolutionLocalisee> findByProjection(Projection projection );
	
	
	public List<Integer> findIdENLPartagee(Utilisateur utilisateur);
	
	public List<Integer> findIdENLNonPartagee(Utilisateur utilisateur);
	
	public List<String> findNomENLPartagee(Utilisateur utilisateur);
	
	public List<String> findNomENLNonPartagee(Utilisateur utilisateur);

	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer);


	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId);


}
