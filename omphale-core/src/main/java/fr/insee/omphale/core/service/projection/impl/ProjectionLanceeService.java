package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.Date;
import java.util.List;

import fr.insee.omphale.core.service.projection.IProjectionLanceeService;
import fr.insee.omphale.dao.projection.IProjectionLanceeDAO;
import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.domaine.projection.ProjectionLancee;


/**
 * Classe pour gérer les fonctionnalités de la couche service pour ProjectionLancee
 *
 */
public class ProjectionLanceeService implements IProjectionLanceeService {

	private IProjectionLanceeDAO projectionLanceeDao;
	
	public ProjectionLanceeService(){
		this.projectionLanceeDao = daoFactory.getProjectionLanceeDAO();
	}
	
	public ProjectionLanceeService(IProjectionLanceeDAO projectionLanceeDao){
		this.projectionLanceeDao = projectionLanceeDao;
	}

	public ProjectionLancee findById(Integer idProjectionLancee) {
		return projectionLanceeDao.findById(idProjectionLancee);
	}

	public boolean exist(Integer idProjection) {
		if (projectionLanceeDao.findAll(idProjection) != null
				&& projectionLanceeDao.findAll(idProjection).size() >= 1) {
			return true;
		}

		return false;
	}


	@SuppressWarnings("rawtypes")
	public List findAll(String idUser) {
		return projectionLanceeDao.findAll(idUser);
	}

	public void remove(ProjectionLancee projectionLancee) {
		projectionLanceeDao.delete(projectionLancee);
	}

	public void setAnnulationUtilisateur(ProjectionLancee projectionLancee) {
		projectionLancee.setCodeRetour("60");
	}

	public ProjectionLancee insertOrUpdate(Projection projection, Integer donnees) {

		ProjectionLancee projectionLancee = new ProjectionLancee();
		projectionLancee.setProjection(projection);
		projectionLancee.setDateLancement(new Date());
		projectionLancee.setDonnees(donnees);
		
		// nbrZones
		projectionLancee.setNbrZone(projection.getZonage().getZones().size());
		
		return projectionLanceeDao.insertOrUpdate(projectionLancee);
	}
	
	public List<ProjectionLancee> findByProjection(Projection projection){
		return projectionLanceeDao.findByProjection(projection);
	}

	public void flush() {
		projectionLanceeDao.flush();
	}
	
	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer){
		return projectionLanceeDao.deleteByListIdProjection(IdsProjectionsASupprimer);
	}
}
