package fr.insee.omphale.dao.projection;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.ValeurCubeHypothese;
import fr.insee.omphale.domaine.projection.ValeurCubeHypotheseId;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour ValeurCubeHypothese
 *
 */
public interface IValeurCubeHypotheseDAO extends
		IGenericDAO<ValeurCubeHypothese, ValeurCubeHypotheseId> {

	/**
	 * supprime en base les objets valeurCubeHypothese associés à une hypothèse donnée
	 * 
	 * @param hypothese
	 */
	public void deleteByHypothese(Hypothese hypothese);

	/**
	 * Recherche les valeurCubeHypothese associés à une hypothèse
	 * 
	 * @param hypothese
	 * @return {@link List}<{@link ValeurCubeHypothese}>
	 */
	public List<ValeurCubeHypothese> findByHypothese(Hypothese hypothese);

}
