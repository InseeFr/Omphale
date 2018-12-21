package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.List;

import fr.insee.omphale.core.service.projection.IMethodeService;
import fr.insee.omphale.dao.projection.IMethodeDAO;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.MethodeEvolution;

/**
 * Classe pour gérer les fonctionnalités de la couche service pour Methode
 *
 */
public class MethodeService implements IMethodeService {

	private IMethodeDAO methodeDAO;
	
	public MethodeService(){
		this.methodeDAO = daoFactory.getMethodeDAO();
	}
	
	public MethodeService(IMethodeDAO methodeDAO){
		this.methodeDAO = methodeDAO;
	}
	
	public List<MethodeEvolution> findAll() {
		return methodeDAO.findAll();
	}

	public MethodeEvolution findById(String id) {
		return methodeDAO.findById(id);
	}
	
	public List<MethodeEvolution> findByComposante(Composante composante){
		return methodeDAO.findByComposante(composante);
	}

}
