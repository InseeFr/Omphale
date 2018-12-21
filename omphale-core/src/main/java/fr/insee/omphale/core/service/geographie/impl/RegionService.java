package fr.insee.omphale.core.service.geographie.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.List;

import fr.insee.omphale.core.service.geographie.IRegionService;
import fr.insee.omphale.dao.geographie.IRegionDAO;
import fr.insee.omphale.domaine.geographie.Region;

/**
 * Classe gérant les fonctionnalités de la couche service pour les "Region"
 *
 */
public class RegionService implements IRegionService {

	private IRegionDAO regionDao;

	public RegionService() {
		this.regionDao = daoFactory.getRegionDAO();
	}

	public RegionService(IRegionDAO regionDAO) {
		this.regionDao = regionDAO;
	}

	public List<Region> findAll() {
		return regionDao.findAll();
	}

	public Region findById(String id) {
		return regionDao.findById(id);
	}

}
