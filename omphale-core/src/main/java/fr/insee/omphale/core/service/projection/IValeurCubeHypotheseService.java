package fr.insee.omphale.core.service.projection;

import java.util.List;

import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.ValeurCubeHypothese;

public interface IValeurCubeHypotheseService {

	/**
	 * insère en base un objet {@link ValeurCubeHypothese}
	 * 
	 * @param valeurCubeHypothese
	 * @return {@link ValeurCubeHypothese}
	 */
	public ValeurCubeHypothese insertOrUpdate(
			ValeurCubeHypothese valeurCubeHypothese);

	/**
	 * Supprime en base un objet {@link ValeurCubeHypothese}
	 * <BR>
	 * selon son hypothèse
	 * 
	 * 
	 * @param hypothese
	 */
	public void deleteByHypothese(Hypothese hypothese);

	/**
	 * Recherche les valeurs CubeHypothèse associées à une hypothèse
	 * 
	 * @param hypothese
	 * @return {@link List}<{@link ValeurCubeHypothese}>
	 */
	public List<ValeurCubeHypothese> findByHypothese(Hypothese hypothese);
}
