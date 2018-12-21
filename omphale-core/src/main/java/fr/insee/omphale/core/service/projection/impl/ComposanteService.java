package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.List;

import fr.insee.omphale.core.service.projection.IComposanteService;
import fr.insee.omphale.dao.projection.IComposanteDAO;
import fr.insee.omphale.domaine.projection.Composante;

/**
 * Classe gérant les fonctionnalités de la couche service pour les composantes
 *
 */
public class ComposanteService implements IComposanteService {

	private IComposanteDAO composanteDAO;

	public ComposanteService() {
		this.composanteDAO = daoFactory.getComposanteDAO();
	}

	public ComposanteService(IComposanteDAO composanteDAO) {
		this.composanteDAO = composanteDAO;
	}

	public boolean contains(List<Composante> liste, String codeComposante) {
		if (liste == null || codeComposante == null) {
			return false;
		} else {
			for (Composante composante : liste) {
				if (composante.getCode() != null
						&& composante.getCode().equals(codeComposante)) {
					return true;
				}
			}
			return false;
		}
	}

	public List<Composante> findAll() {
		return composanteDAO.findAll();
	}

	public Composante findById(String id) {
		return composanteDAO.findById(id);
	}

	public List<Composante> findAllReferenceesEvolutionNL(String idUser,
			Boolean standard) {
		if (standard != null && standard.booleanValue()) {
			return composanteDAO.findAllReferenceesEvolutionNL(standard);
		}
		return composanteDAO.findAllReferenceesEvolutionNL(idUser, standard);
	}

}
