package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.List;

import fr.insee.omphale.core.service.projection.IValeurCubeHypotheseService;
import fr.insee.omphale.dao.projection.IValeurCubeHypotheseDAO;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.ValeurCubeHypothese;

/**
 * Classe gérant les fonctions de la couche service pour ValeurCubeHypothese
 *
 */
public class ValeurCubeHypotheseService implements IValeurCubeHypotheseService {
	private IValeurCubeHypotheseDAO valeurDao;

	public ValeurCubeHypotheseService() {
		this.valeurDao = daoFactory.getValeurCubeHypotheseDAO();
	}

	public ValeurCubeHypotheseService(IValeurCubeHypotheseDAO valeurDAO) {
		this.valeurDao = valeurDAO;
	}

	public ValeurCubeHypothese insertOrUpdate(
			ValeurCubeHypothese valeurCubeHypothese) {
		return valeurDao.insertOrUpdate(valeurCubeHypothese);
	}

	public void deleteByHypothese(Hypothese hypothese) {
		valeurDao.deleteByHypothese(hypothese);
	}

	public List<ValeurCubeHypothese> findByHypothese(Hypothese hypothese) {
		return valeurDao.findByHypothese(hypothese);
	}

}
