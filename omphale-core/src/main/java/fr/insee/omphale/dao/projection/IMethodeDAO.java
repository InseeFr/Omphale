package fr.insee.omphale.dao.projection;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.MethodeEvolution;


/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour MethodeEvolution
 *
 */
public interface IMethodeDAO extends IGenericDAO<MethodeEvolution, String> {

	/**
	 * recherche les méthodeEvolution associées à une composante donnée
	 * 
	 * @param composante
	 * @return {@link List}<{@link MethodeEvolution}>
	 */
	public List<MethodeEvolution> findByComposante(Composante composante);
	
}
