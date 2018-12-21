package fr.insee.omphale.core.service.geographie.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.List;

import fr.insee.omphale.core.service.geographie.ICommuneService;
import fr.insee.omphale.dao.geographie.ICommuneDAO;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;


/**
 * Classe gérant les fonctionnalités de la couche service pour les "Commune"
 *
 */
public class CommuneService implements ICommuneService {

	private ICommuneDAO communeDao;

	public CommuneService() {
		this.communeDao = daoFactory.getCommuneDAO();
	}

	public CommuneService(ICommuneDAO communeDAO) {
		this.communeDao = communeDAO;
	}

	public List<String> findIdNonPresents(List<String> idCommunes) {
		return communeDao.findIdNonPresents(idCommunes);
	}

	public List<Commune> findById(List<String> idCommunes) {
		return communeDao.findById(idCommunes);
	}

	public List<Commune> findAllByDepartement(Departement departement) {
		return communeDao.findAllByDepartement(departement);
	}

	public Commune findById(String idCommune) {
		return communeDao.findById(idCommune);
	}

}
