package fr.insee.omphale.core.service.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import fr.insee.omphale.core.service.IParametresService;
import fr.insee.omphale.dao.IParametresDAO;

public class ParametresService implements IParametresService {
	private IParametresDAO paramDao;

	public ParametresService() {
		this.paramDao = daoFactory.getParametresDAO();
	}

	public ParametresService(IParametresDAO paramDAO) {
		this.paramDao = paramDAO;
	}

	public List<Integer> getCyclesOuverts() {
		List<BigDecimal> cycles = paramDao.getCyclesOuverts();
		List<Integer> retour = new ArrayList<Integer>();
		for (BigDecimal annee : cycles) {
			retour.add(annee.intValue());
		}
		return retour;
	}

}
