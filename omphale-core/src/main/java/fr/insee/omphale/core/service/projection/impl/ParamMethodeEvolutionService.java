package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.List;

import fr.insee.omphale.core.service.projection.IParamMethodeEvolutionService;
import fr.insee.omphale.dao.projection.IParamMethodeEvolutionDAO;
import fr.insee.omphale.domaine.projection.MethodeEvolution;
import fr.insee.omphale.domaine.projection.ParamMethodeEvolution;

/**
 * Classe pour gérer les fonctionnalités de la couche service pour ParamMethodeEvolution
 *
 */
public class ParamMethodeEvolutionService implements IParamMethodeEvolutionService {

	private IParamMethodeEvolutionDAO paramMethodeEvolutionDAO;
	
	public ParamMethodeEvolutionService(){
		this.paramMethodeEvolutionDAO = daoFactory.getParamMethodeEvolutionDAO();
	}
	
	public ParamMethodeEvolutionService(IParamMethodeEvolutionDAO paramMethodeEvolutionDAO){
		this.paramMethodeEvolutionDAO = paramMethodeEvolutionDAO;
	}
	
	public List<ParamMethodeEvolution> findAll(){
		return paramMethodeEvolutionDAO.findAll();
	}

	public ParamMethodeEvolution findById(String id){
		return paramMethodeEvolutionDAO.findById(id);
	}
	
	public List<ParamMethodeEvolution> findByMethodeEvolution(MethodeEvolution methode){
		return paramMethodeEvolutionDAO.findByMethodeEvolution(methode);
	}

}
