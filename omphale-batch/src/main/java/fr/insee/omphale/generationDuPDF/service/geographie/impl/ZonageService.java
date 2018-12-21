package fr.insee.omphale.generationDuPDF.service.geographie.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;
import fr.insee.omphale.generationDuPDF.dao.geographie.IZonageDAO;
import fr.insee.omphale.generationDuPDF.domaine.geographie.Zonage;
import fr.insee.omphale.generationDuPDF.service.geographie.IZonageService;



public class ZonageService implements IZonageService {

	private IZonageDAO zonageDao = daoFactoryPDF.getZonageDAO();

	public Zonage findById(String id) {
		return zonageDao.findById(id);
	}

	public String isProjectionUtilisateur(String idZonage) {
		if (idZonage != null && idZonage.equals("0")) {
			return "0";
		}
		else { // zonage utilisateur
			return "1";
		}
	}
}
