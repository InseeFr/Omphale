/**
 * 
 */
package fr.insee.omphale.dao.projection;


import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.projection.MethodeEvolution;
import fr.insee.omphale.domaine.projection.ParamMethodeEvolution;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour ParamMethodeEvolution
 *
 */
public interface IParamMethodeEvolutionDAO extends
		IGenericDAO<ParamMethodeEvolution, String> {
	
	/**
	 * Recherche les paramMethodeEvolutions associés à une methodeEvolution
	 * 
	 * @param methode
	 * @return {@link List}<{@link ParamMethodeEvolution}>
	 */
	public List<ParamMethodeEvolution> findByMethodeEvolution(MethodeEvolution methode);
}
