package fr.insee.omphale.core.service.geographie.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.List;

import fr.insee.omphale.core.service.geographie.ITypeZoneStandardService;
import fr.insee.omphale.dao.geographie.ITypeZoneStandardDAO;
import fr.insee.omphale.domaine.geographie.TypeZoneStandard;


/**
 * Classe gérant les fonctionnalités de la couche service pour les  "TypeZoneStandard"
 *
 */
public class TypeZoneStandardService implements ITypeZoneStandardService {

	private ITypeZoneStandardDAO tzsDao;

	public TypeZoneStandardService() {
		this.tzsDao = daoFactory.getTypeZoneStandardDAO();
	}

	public TypeZoneStandardService(ITypeZoneStandardDAO tzsDAO) {
		this.tzsDao = tzsDAO;
	}

	public TypeZoneStandard findById(int id) {
		return tzsDao.findById(id);
	}

	public List<TypeZoneStandard> findAll() {
		return tzsDao.findAll();
	}

}
