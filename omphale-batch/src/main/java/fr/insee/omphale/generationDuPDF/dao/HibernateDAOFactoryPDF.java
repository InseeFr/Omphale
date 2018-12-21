package fr.insee.omphale.generationDuPDF.dao;

import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiqueDecDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiqueFeconditeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePointFluxDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePointFluxRangeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePointFluxSoldeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePopulationDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePyramideDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1AgeMoyenDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1EDVDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1ICFDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1PopulationTableauDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2DeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5DeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5ToutesZonesDeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5ToutesZonesVersDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5VersDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2ToutesZonesDeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2ToutesZonesVersDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2VersDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IZoneEtudeZoneEchangeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.GraphiqueDecDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.GraphiqueFeconditeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.GraphiquePointFluxDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.GraphiquePointFluxRangeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.GraphiquePointFluxSoldeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.GraphiquePopulationDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.GraphiquePyramideDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau1AgeMoyenDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau1EDVDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau1ICFDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau1PopulationTableauDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau2DeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau2TousAgesPlus5DeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau2TousAgesPlus5ToutesZonesDeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau2TousAgesPlus5ToutesZonesVersDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau2TousAgesPlus5VersDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau2ToutesZonesDeDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau2ToutesZonesVersDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.Tableau2VersDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.impl.ZoneEtudeZoneEchangeDAO;
import fr.insee.omphale.generationDuPDF.dao.geographie.IZonageDAO;
import fr.insee.omphale.generationDuPDF.dao.geographie.IZoneDAO;
import fr.insee.omphale.generationDuPDF.dao.geographie.impl.ZonageDAO;
import fr.insee.omphale.generationDuPDF.dao.geographie.impl.ZoneDAO;
import fr.insee.omphale.generationDuPDF.dao.projection.IProjectionDAO;
import fr.insee.omphale.generationDuPDF.dao.projection.impl.ProjectionDAO;



/**
 * Returns Hibernate-specific instances of DAOs.
 * <p/>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * a nested static class to implement the interface in a generic way. This
 * allows clean refactoring later on, should the interface implement business
 * data access methods at some later time. Then, we would externalize the
 * implementation into its own first-level class.
 */
public class HibernateDAOFactoryPDF extends DAOFactoryPDF {

	@SuppressWarnings({ "rawtypes" })
	private GenericDAO instantiateDAO(Class daoClass) {
		try {
			GenericDAO dao = (GenericDAO) daoClass.newInstance();
			return dao;
		} catch (Exception ex) {
			throw new RuntimeException("impossible d'instancier le DAO: "
					+ daoClass, ex);
		}
	}



	public IGraphiquePyramideDAO getPointPyramideDAO() {
		return (IGraphiquePyramideDAO) instantiateDAO(GraphiquePyramideDAO.class);
	}
	public IGraphiquePopulationDAO getPopulationDAO() {
		return (IGraphiquePopulationDAO) instantiateDAO(GraphiquePopulationDAO.class);
	}
	public IGraphiqueFeconditeDAO getPointCourbeDAO() {
		return (IGraphiqueFeconditeDAO) instantiateDAO(GraphiqueFeconditeDAO.class);
	}
	public IGraphiqueDecDAO getDecDAO() {
		return (IGraphiqueDecDAO) instantiateDAO(GraphiqueDecDAO.class);
	}
	public IGraphiquePointFluxDAO getPointFluxDAO() {
		return (IGraphiquePointFluxDAO) instantiateDAO(GraphiquePointFluxDAO.class);
	}
	public IGraphiquePointFluxRangeDAO getPointFluxRangeDAO() {
		return (IGraphiquePointFluxRangeDAO) instantiateDAO(GraphiquePointFluxRangeDAO.class);
	}
	public IGraphiquePointFluxSoldeDAO getPointFluxSoldeDAO() {
		return (IGraphiquePointFluxSoldeDAO) instantiateDAO(GraphiquePointFluxSoldeDAO.class);
	}
	public IZonageDAO getZonageDAO() {
		return (IZonageDAO) instantiateDAO(ZonageDAO.class);
	}
	public IZoneDAO getZoneDAO() {
		return (IZoneDAO) instantiateDAO(ZoneDAO.class);
	}
	public ITableau1PopulationTableauDAO getPopulationTableauDAO() {
		return (ITableau1PopulationTableauDAO) instantiateDAO(Tableau1PopulationTableauDAO.class);
	}
	public ITableau1EDVDAO getEDVDAO() {
		return (ITableau1EDVDAO) instantiateDAO(Tableau1EDVDAO.class);
	}
	public ITableau1ICFDAO getICFDAO() {
		return (ITableau1ICFDAO) instantiateDAO(Tableau1ICFDAO.class);
	}
	public ITableau1AgeMoyenDAO getAgeMoyenDAO() {
		return (ITableau1AgeMoyenDAO) instantiateDAO(Tableau1AgeMoyenDAO.class);
	}
	
	public ITableau2VersDAO getVersDAO() {
		return (ITableau2VersDAO) instantiateDAO(Tableau2VersDAO.class);
	}
	public ITableau2DeDAO getDeDAO() {
		return (ITableau2DeDAO) instantiateDAO(Tableau2DeDAO.class);
	}
	public ITableau2ToutesZonesVersDAO getToutesZonesVersDAO() {
		return (ITableau2ToutesZonesVersDAO) instantiateDAO(Tableau2ToutesZonesVersDAO.class);
	}
	public ITableau2ToutesZonesDeDAO getToutesZonesDeDAO() {
		return (ITableau2ToutesZonesDeDAO) instantiateDAO(Tableau2ToutesZonesDeDAO.class);
	}
	public ITableau2TousAgesPlus5VersDAO getTousAgesPlus5VersDAO() {
		return (ITableau2TousAgesPlus5VersDAO) instantiateDAO(Tableau2TousAgesPlus5VersDAO.class);
	}
	public ITableau2TousAgesPlus5DeDAO getTousAgesPlus5DeDAO() {
		return (ITableau2TousAgesPlus5DeDAO) instantiateDAO(Tableau2TousAgesPlus5DeDAO.class);
	}
	public ITableau2TousAgesPlus5ToutesZonesVersDAO getTousAgesPlus5ToutesZonesVersDAO() {
		return (ITableau2TousAgesPlus5ToutesZonesVersDAO) instantiateDAO(Tableau2TousAgesPlus5ToutesZonesVersDAO.class);
	}
	public ITableau2TousAgesPlus5ToutesZonesDeDAO getTousAgesPlus5ToutesZonesDeDAO() {
		return (ITableau2TousAgesPlus5ToutesZonesDeDAO) instantiateDAO(Tableau2TousAgesPlus5ToutesZonesDeDAO.class);
	}
	
	public IProjectionDAO getProjectionDAO() {
		return (IProjectionDAO) instantiateDAO(ProjectionDAO.class);
	}
	
	public IZoneEtudeZoneEchangeDAO getZoneEtudeZoneEchangeDAO() {
		return (IZoneEtudeZoneEchangeDAO) instantiateDAO(ZoneEtudeZoneEchangeDAO.class);
	}
}
