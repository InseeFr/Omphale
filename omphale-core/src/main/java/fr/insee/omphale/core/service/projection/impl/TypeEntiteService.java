package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.List;

import fr.insee.omphale.core.service.projection.ITypeEntiteService;
import fr.insee.omphale.dao.projection.ITypeEntiteDAO;
import fr.insee.omphale.domaine.projection.TypeEntite;


/**
 * Classe gérant les fonctions de la couche service pour TypeEntite
 *
 */
public class TypeEntiteService implements ITypeEntiteService {

	private ITypeEntiteDAO typeEntiteDao ;
	
	public TypeEntiteService(){
		this.typeEntiteDao = daoFactory.getTypeEntiteDAO();
	}

	public TypeEntiteService(ITypeEntiteDAO typeEntiteDao){
		this.typeEntiteDao = typeEntiteDao ;
	}
	
	public List<TypeEntite> findAll() {
		return typeEntiteDao.findAll();
	}

	public TypeEntite findById(String id) {
		return typeEntiteDao.findById(id);
	}

}
