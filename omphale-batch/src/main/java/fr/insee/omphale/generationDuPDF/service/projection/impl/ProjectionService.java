package fr.insee.omphale.generationDuPDF.service.projection.impl;

import static fr.insee.omphale.generationDuPDF.service.impl.Service.daoFactoryPDF;
import fr.insee.omphale.generationDuPDF.dao.projection.IProjectionDAO;
import fr.insee.omphale.generationDuPDF.domaine.projection.Projection;
import fr.insee.omphale.generationDuPDF.service.projection.IProjectionService;



public class ProjectionService implements IProjectionService {

	private IProjectionDAO projectionDao = daoFactoryPDF.getProjectionDAO();

	public Projection findById(Integer idProjection) {
		return projectionDao.findById(idProjection);
	}

	
}
