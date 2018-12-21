package fr.insee.omphale.core.service.projection;

import java.util.List;

import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.domaine.projection.ProjectionLancee;

/**
 * Interface de service 
 * <br>
 * objets métier {@link fr.insee.omphale.domaine.projection.ProjectionLancee}
 *
 */
public interface IProjectionLanceeService {

	/**
	 * Recherche dans la base de données une ProjectionLancee pour un identifiant donné.
	 * @param idProjectionLancee identifiant de projection lancée
	 * @return objet {@link fr.insee.omphale.domaine.projection.ProjectionLancee}
	 */
	public ProjectionLancee findById(Integer idProjectionLancee);
	
	public boolean exist(Integer idProjection);
	
	/**
	 * Recherche dans la base de données la liste des ProjectionLancee pour un idUser donné.
	 * @param idUser idUser
	 * @return liste d'objets {@link fr.insee.omphale.domaine.projection.ProjectionLancee}
	 */
	@SuppressWarnings("rawtypes")
	public List findAll(String idUser);
	
	/**
	 * Supprime une ProjectionLancee dans la base de données.
	 * @param projectionLancee projectionLancee
	 */
	public void remove(ProjectionLancee projectionLancee);
	
	/**
	 * Set dans la base de données le codeRetour à 6 pour une ProjectionLancee.
	 * @param projectionLancee projectionLancee
	 */
	public void setAnnulationUtilisateur(ProjectionLancee projectionLancee);
	
	/**
	 * Crée une ProjectionLancee dans la base de données.
	 * @param projection définition de projection
	 * @param donnees donnees
	 */
	public ProjectionLancee insertOrUpdate(Projection projection, Integer donnees);
	
	
	/**
	 * 
	 * récupére les projections lancées pour une même projection
	 * 
	 * @param projection
	 * @return List(ProjectionLancee)
	 */
	public List<ProjectionLancee> findByProjection(Projection projection);
	
	/**
	 * flush
	 */
	public void flush();
	
	
	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer);
}
