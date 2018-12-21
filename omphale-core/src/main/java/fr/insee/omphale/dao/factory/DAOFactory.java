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
import fr.insee.omphale.ihm.util.Parametres;

/**
 * à partir d'une version de <a
 * href="http://www.hibernate.org/400.html">caveatemptor-native-061211</a>
 * Defines all DAOs and the concrete factories to get the conrecte DAOs.
 * <p>
 * To get a concrete DAOFactory, call <tt>instance()</tt> with one of the
 * classes that extend this factory.
 * <p>
 * Implementation: If you write a new DAO, this class has to know about it. If
 * you add a new persistence mechanism, add an additional concrete factory for
 * it as a constant, like <tt>HIBERNATE</tt>.
 */
public abstract class DAOFactory {
	
	private static boolean activeFactoryCoreOmphale;


	/**
	 * Creates a standalone DAOFactory that returns unmanaged DAO beans for use
	 * in any environment Hibernate has been configured for. Uses
	 * HibernateUtil/SessionFactory and Hibernate context propagation
	 * (CurrentSessionContext), thread-bound or transaction-bound, and
	 * transaction scoped. Le nom de la classe de factory est externalisée de
	 * façon à pouvoir changer d'implémentation sans toucher aux couches
	 * appelantes une seule est développée : Hibernate
	 */
	@SuppressWarnings("rawtypes")
	public static Class PERSISTANCE = null;
	
	@SuppressWarnings("rawtypes")
	public static Class init() {
		try {
			if(activeFactoryCoreOmphale == true){
				PERSISTANCE = Class.forName(Parametres.getString("PersistanceCore.factory"));
			}else{
				PERSISTANCE = Class.forName(Parametres.getString("Persistance.factory"));
			}
		} catch (Exception e) {
			throw new RuntimeException(
					"Impossible de trouver la classe Factory de persistance de la clé",
					e);
		}
		return PERSISTANCE;
	}

	/**
	 * Factory method for instantiation of concrete factories.
	 */
	@SuppressWarnings("rawtypes")
	public static DAOFactory instance(Class factory) {
		try {
			return (DAOFactory) factory.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException("Impossible de créer la DAOFactory: "
					+ factory, ex);
		}
	}


	public abstract IZoneDAO getZoneDAO();

	public abstract IUtilisateurDAO getUtilisateurDAO();

	public abstract IZonageDAO getZonageDAO();

	public abstract ITypeZoneStandardDAO getTypeZoneStandardDAO();

	public abstract IGroupeEtalonDAO getGroupeEtalonDAO();

	public abstract ICommuneDependanceDAO getCommuneDependanceDAO();

	public abstract ICommuneDAO getCommuneDAO();

	public abstract IDepartementDAO getDepartementDAO();

	public abstract IRegionDAO getRegionDAO();

	public abstract IScenarioDAO getScenarioDAO();

	public abstract IEvolutionNonLocaliseeDAO getEvolutionNonLocaliseeDAO();

	public abstract ITypeEntiteDAO getTypeEntiteDAO();

	public abstract IComposanteDAO getComposanteDAO();

	public abstract IHypotheseDAO getHypotheseDAO();

	public abstract IEvolDeScenarioDAO getEvolDeScenarioDAO();

	public abstract IMethodeDAO getMethodeDAO();

	public abstract IParamMethodeEvolutionDAO getParamMethodeEvolutionDAO();

	public abstract IValeurCubeHypotheseDAO getValeurCubeHypotheseDAO();

	public abstract IProjectionLanceeDAO getProjectionLanceeDAO();
	
	public abstract IProjectionDAO getProjectionDAO();

	public abstract IEvolutionLocaliseeDAO getEvolutionLocaliseeDAO();
	
	public abstract IParametresDAO getParametresDAO();

	public static boolean isActiveFactoryCoreOmphale() {
		return activeFactoryCoreOmphale;
	}

	public static void setActiveFactoryCoreOmphale(boolean activeFactoryCoreOmphale) {
		DAOFactory.activeFactoryCoreOmphale = activeFactoryCoreOmphale;
	}
}
