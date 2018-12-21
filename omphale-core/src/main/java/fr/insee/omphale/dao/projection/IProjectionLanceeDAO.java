package fr.insee.omphale.dao.projection;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.domaine.projection.ProjectionLancee;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour ProjectionLancee
 *
 */
public interface IProjectionLanceeDAO extends
		IGenericDAO<ProjectionLancee, Integer> {

	/**
	 * Recherche dans la base de données la liste des ProjectionLancee pour un
	 * idUser donné.
	 * 
	 * @param idUser
	 *            idUser
	 * @return liste d'objets
	 *         {@link fr.insee.omphale.domaine.projection.ProjectionLancee}
	 */
	public List<ProjectionLancee> findAll(String idUser);

	/**
	 * Recherche dans la base de données la liste des ProjectionLancee pour une
	 * projection donnée
	 * 
	 * @param idProjection
	 * @return
	 */
	public List<ProjectionLancee> findAll(int idProjection);
	
	/**
	 * Recherche les projections lancees associées à une projection 
	 * 
	 * @param projection
	 * @return {@link List}<{@link ProjectionLancee}>
	 */
	public List<ProjectionLancee> findByProjection(Projection projection);
	
	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer);

}
