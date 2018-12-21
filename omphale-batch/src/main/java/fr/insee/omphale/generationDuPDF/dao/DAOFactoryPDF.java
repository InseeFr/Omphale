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
import fr.insee.omphale.generationDuPDF.dao.geographie.IZonageDAO;
import fr.insee.omphale.generationDuPDF.dao.geographie.IZoneDAO;
import fr.insee.omphale.generationDuPDF.dao.projection.IProjectionDAO;
import fr.insee.omphale.generationDuPDF.util.Parametres;


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
public abstract class DAOFactoryPDF {

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
	public static final Class PERSISTANCE;
	static {
		try {
			PERSISTANCE = Class.forName(Parametres
					.getString("Persistance.factory"));
		} catch (Exception e) {
			throw new RuntimeException(
					"Impossible de trouver la classe Factory PDF de persistance de la clé",
					e);
		}
	}

	/**
	 * Factory method for instantiation of concrete factories.
	 */
	public static DAOFactoryPDF instance(@SuppressWarnings("rawtypes") Class factory) {
		try {
			return (DAOFactoryPDF) factory.newInstance();
		} catch (Exception ex) {
			throw new RuntimeException("Impossible de créer la DAOFactoryPDF: "
					+ factory, ex);
		}
	}


	public abstract IGraphiquePyramideDAO getPointPyramideDAO();
	public abstract IGraphiquePopulationDAO getPopulationDAO();
	public abstract IGraphiqueFeconditeDAO getPointCourbeDAO();
	public abstract IGraphiqueDecDAO getDecDAO();
	public abstract IGraphiquePointFluxSoldeDAO getPointFluxSoldeDAO();
	public abstract IGraphiquePointFluxDAO getPointFluxDAO();
	public abstract IGraphiquePointFluxRangeDAO getPointFluxRangeDAO();
	public abstract IZonageDAO getZonageDAO();
	public abstract IZoneDAO getZoneDAO();
	public abstract ITableau1PopulationTableauDAO getPopulationTableauDAO();
	public abstract ITableau1EDVDAO getEDVDAO();
	public abstract ITableau1ICFDAO getICFDAO();
	public abstract ITableau1AgeMoyenDAO getAgeMoyenDAO();
	public abstract ITableau2VersDAO getVersDAO();
	public abstract ITableau2DeDAO getDeDAO();
	public abstract ITableau2ToutesZonesVersDAO getToutesZonesVersDAO();
	public abstract ITableau2ToutesZonesDeDAO getToutesZonesDeDAO();
	public abstract ITableau2TousAgesPlus5VersDAO getTousAgesPlus5VersDAO();
	public abstract ITableau2TousAgesPlus5DeDAO getTousAgesPlus5DeDAO();
	public abstract ITableau2TousAgesPlus5ToutesZonesVersDAO getTousAgesPlus5ToutesZonesVersDAO();
	public abstract ITableau2TousAgesPlus5ToutesZonesDeDAO getTousAgesPlus5ToutesZonesDeDAO();
	public abstract IProjectionDAO getProjectionDAO();
	public abstract IZoneEtudeZoneEchangeDAO getZoneEtudeZoneEchangeDAO();
}
