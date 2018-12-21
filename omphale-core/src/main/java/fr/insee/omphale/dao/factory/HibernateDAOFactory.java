package fr.insee.omphale.dao.factory;

import fr.insee.omphale.dao.IParametresDAO;
import fr.insee.omphale.dao.IUtilisateurDAO;
import fr.insee.omphale.dao.geographie.ICommuneDAO;
import fr.insee.omphale.dao.geographie.ICommuneDependanceDAO;
import fr.insee.omphale.dao.geographie.IDepartementDAO;
import fr.insee.omphale.dao.geographie.IGroupeEtalonDAO;
import fr.insee.omphale.dao.geographie.IRegionDAO;
import fr.insee.omphale.dao.geographie.ITypeZoneStandardDAO;
import fr.insee.omphale.dao.geographie.IZonageDAO;
import fr.insee.omphale.dao.geographie.IZoneDAO;
import fr.insee.omphale.dao.geographie.impl.CommuneDAO;
import fr.insee.omphale.dao.geographie.impl.CommuneDependanceDAO;
import fr.insee.omphale.dao.geographie.impl.DepartementDAO;
import fr.insee.omphale.dao.geographie.impl.GroupeEtalonDAO;
import fr.insee.omphale.dao.geographie.impl.RegionDAO;
import fr.insee.omphale.dao.geographie.impl.TypeZoneStandardDAO;
import fr.insee.omphale.dao.geographie.impl.ZonageDAO;
import fr.insee.omphale.dao.geographie.impl.ZoneDAO;
import fr.insee.omphale.dao.impl.ParametresDAO;
import fr.insee.omphale.dao.impl.UtilisateurDAO;
import fr.insee.omphale.dao.projection.IComposanteDAO;
import fr.insee.omphale.dao.projection.IEvolDeScenarioDAO;
import fr.insee.omphale.dao.projection.IEvolutionLocaliseeDAO;
import fr.insee.omphale.dao.projection.IEvolutionNonLocaliseeDAO;
import fr.insee.omphale.dao.projection.IHypotheseDAO;
import fr.insee.omphale.dao.projection.IMethodeDAO;
import fr.insee.omphale.dao.projection.IParamMethodeEvolutionDAO;
import fr.insee.omphale.dao.projection.IProjectionDAO;
import fr.insee.omphale.dao.projection.IProjectionLanceeDAO;
import fr.insee.omphale.dao.projection.IScenarioDAO;
import fr.insee.omphale.dao.projection.ITypeEntiteDAO;
import fr.insee.omphale.dao.projection.IValeurCubeHypotheseDAO;
import fr.insee.omphale.dao.projection.impl.ComposanteDAO;
import fr.insee.omphale.dao.projection.impl.EvolDeScenarioDAO;
import fr.insee.omphale.dao.projection.impl.EvolutionLocaliseeDAO;
import fr.insee.omphale.dao.projection.impl.EvolutionNonLocaliseeDAO;
import fr.insee.omphale.dao.projection.impl.HypotheseDAO;
import fr.insee.omphale.dao.projection.impl.MethodeDAO;
import fr.insee.omphale.dao.projection.impl.ParamMethodeEvolutionDAO;
import fr.insee.omphale.dao.projection.impl.ProjectionDAO;
import fr.insee.omphale.dao.projection.impl.ProjectionLanceeDAO;
import fr.insee.omphale.dao.projection.impl.ScenarioDAO;
import fr.insee.omphale.dao.projection.impl.TypeEntiteDAO;
import fr.insee.omphale.dao.projection.impl.ValeurCubeHypotheseDAO;
import fr.insee.omphale.dao.util.GenericDAO;

/**
 * Returns Hibernate-specific instances of DAOs.
 * <p/>
 * If for a particular DAO there is no additional non-CRUD functionality, we use
 * a nested static class to implement the interface in a generic way. This
 * allows clean refactoring later on, should the interface implement business
 * data access methods at some later time. Then, we would externalize the
 * implementation into its own first-level class.
 */
public class HibernateDAOFactory extends DAOFactory  {


	@SuppressWarnings("rawtypes")
	private GenericDAO instantiateDAO(Class daoClass) {
		try {
			GenericDAO dao = (GenericDAO) daoClass.newInstance();
			return dao;
		} catch (Exception ex) {
			throw new RuntimeException("impossible d'instancier le DAO: "
					+ daoClass, ex);
		}
	}

	@Override
	public IUtilisateurDAO getUtilisateurDAO() {
		return (IUtilisateurDAO) instantiateDAO(UtilisateurDAO.class);
	}

	@Override
	public IZoneDAO getZoneDAO() {
		return (IZoneDAO) instantiateDAO(ZoneDAO.class);
	}

	@Override
	public ITypeZoneStandardDAO getTypeZoneStandardDAO() {
		return (ITypeZoneStandardDAO) instantiateDAO(TypeZoneStandardDAO.class);
	}

	@Override
	public ICommuneDAO getCommuneDAO() {
		return (ICommuneDAO) instantiateDAO(CommuneDAO.class);
	}

	@Override
	public ICommuneDependanceDAO getCommuneDependanceDAO() {
		return (ICommuneDependanceDAO) instantiateDAO(CommuneDependanceDAO.class);
	}

	@Override
	public IDepartementDAO getDepartementDAO() {
		return (IDepartementDAO) instantiateDAO(DepartementDAO.class);
	}

	@Override
	public IGroupeEtalonDAO getGroupeEtalonDAO() {
		return (IGroupeEtalonDAO) instantiateDAO(GroupeEtalonDAO.class);
	}

	@Override
	public IRegionDAO getRegionDAO() {
		return (IRegionDAO) instantiateDAO(RegionDAO.class);
	}

	@Override
	public IZonageDAO getZonageDAO() {
		return (IZonageDAO) instantiateDAO(ZonageDAO.class);
	}

	@Override
	public IScenarioDAO getScenarioDAO() {
		return (IScenarioDAO) instantiateDAO(ScenarioDAO.class);
	}

	@Override
	public IEvolutionNonLocaliseeDAO getEvolutionNonLocaliseeDAO() {
		return (IEvolutionNonLocaliseeDAO) instantiateDAO(EvolutionNonLocaliseeDAO.class);
	}

	@Override
	public ITypeEntiteDAO getTypeEntiteDAO() {
		return (ITypeEntiteDAO) instantiateDAO(TypeEntiteDAO.class);
	}

	@Override
	public IHypotheseDAO getHypotheseDAO() {
		return (IHypotheseDAO) instantiateDAO(HypotheseDAO.class);
	}

	@Override
	public IComposanteDAO getComposanteDAO() {
		return (IComposanteDAO) instantiateDAO(ComposanteDAO.class);
	}

	@Override
	public IEvolDeScenarioDAO getEvolDeScenarioDAO() {
		return (IEvolDeScenarioDAO) instantiateDAO(EvolDeScenarioDAO.class);
	}

	@Override
	public IMethodeDAO getMethodeDAO() {
		return (IMethodeDAO) instantiateDAO(MethodeDAO.class);
	}

	@Override
	public IParamMethodeEvolutionDAO getParamMethodeEvolutionDAO() {
		return (IParamMethodeEvolutionDAO) instantiateDAO(ParamMethodeEvolutionDAO.class);
	}

	@Override
	public IValeurCubeHypotheseDAO getValeurCubeHypotheseDAO() {
		return (IValeurCubeHypotheseDAO) instantiateDAO(ValeurCubeHypotheseDAO.class);
	}

	@Override
	public IProjectionLanceeDAO getProjectionLanceeDAO() {
		return (IProjectionLanceeDAO) instantiateDAO(ProjectionLanceeDAO.class);
	}

	@Override
	public IProjectionDAO getProjectionDAO() {
		return (IProjectionDAO) instantiateDAO(ProjectionDAO.class);
	}

	@Override
	public IEvolutionLocaliseeDAO getEvolutionLocaliseeDAO() {
		return (IEvolutionLocaliseeDAO) instantiateDAO(EvolutionLocaliseeDAO.class);
	}

	@Override
	public IParametresDAO getParametresDAO() {
		//Ce n'est pas un DAO generique d'hibernate
		return new ParametresDAO();
	}
}
