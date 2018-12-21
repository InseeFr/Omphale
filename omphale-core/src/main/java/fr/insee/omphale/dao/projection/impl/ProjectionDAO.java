package fr.insee.omphale.dao.projection.impl;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.IntegerType;

import fr.insee.omphale.dao.projection.IProjectionDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.dao.util.HibernateUtil;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.domaine.projection.Scenario;
import fr.insee.omphale.ihm.util.ParametresMessages;
import fr.insee.omphale.ihm.util.dataTable.ProjectionAffichageDataTable;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour Projection
 *
 */
public class ProjectionDAO extends GenericDAO<Projection, Integer> implements
		IProjectionDAO {

	@Override
	@SuppressWarnings("unchecked")
	public List<Projection> findAll(String idUser) {

		try {
			Criteria cri = getSession().createCriteria(Projection.class).add(
					Restrictions.eq("utilisateur.id", idUser)).addOrder(
					Order.desc("dateCreation"));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Projection> findAll(String idUser, String idZonage) {

		try {
			Criteria cri = getSession().createCriteria(Projection.class).add(
					Restrictions.eq("utilisateur.id", idUser))
					.add(Restrictions.eq("zonage.id", idZonage));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}


	@SuppressWarnings("unchecked")
	public List<Projection> getListe(Integer idScenario) {
		Session session = getSession();
		try {
			String select = "from Projection projection where projection.scenario.id = :id order by projection.nom";

			return session.createQuery(select).setParameter("id",
					idScenario.intValue()).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Projection> findEtalons() {
		Session session = getSession();
		try {
			String select = "from Projection projection where projection.standard = :standard";

			return session.createQuery(select).setBoolean("standard", true)
					.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Projection> findAll(Zonage zonage) {
		Session session = getSession();
		try {
			Criteria cri = session.createCriteria(Projection.class).add(
					Restrictions.eq("zonage", zonage)).addOrder(
					Order.desc("dateCreation"));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<Projection> findByProjectionEnglobante(
			Projection projectionEnglobante) {
		Session session = getSession();
		try {
			String select = "from Projection projection where projection.projectionEnglobante = :projectionEnglobante";
			return session.createQuery(select).setParameter(
					"projectionEnglobante", projectionEnglobante).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Projection> findByProjectionEtalon(Projection projectionEtalon) {
		Session session = getSession();
		try {
			String select = "from Projection projection where projection.projectionEtalon = :projectionEtalon";
			return session.createQuery(select).setParameter("projectionEtalon",
					projectionEtalon).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Projection> findByProjectionEnglobanteEtByProjectionEtalon(Projection projectionEnglobanteOuEtalon){
		Session session = getSession();
		try {
			String select = "from Projection projection where projection.projectionEnglobante = :projectionEnglobante or projection.projectionEtalon = :projectionEtalon";
			return session.createQuery(select).setParameter(
					"projectionEnglobante", projectionEnglobanteOuEtalon).setParameter("projectionEtalon",
							projectionEnglobanteOuEtalon).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Utilisateur> findUtilisateursByProjectionEnglobanteEtByProjectionEtalon(Projection projectionEnglobanteOuEtalon){
		Session session = getSession();
		try {
			String select = "select projection.utilisateur from Projection projection where projection.projectionEnglobante = :projectionEnglobante or projection.projectionEtalon = :projectionEtalon";
			return session.createQuery(select).setParameter(
					"projectionEnglobante", projectionEnglobanteOuEtalon).setParameter("projectionEtalon",
							projectionEnglobanteOuEtalon).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	


	
	@SuppressWarnings("unchecked")
	public List<Projection> findProjectionNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT ")
					.append("proj_utilisateur.ID_PROJECTION,")
					.append("proj_utilisateur.ID_SCENARIO,")
					.append("proj_utilisateur.ID_ZONAGE,")
					.append("proj_utilisateur.ANNEE_REFERENCE,")
					.append("proj_utilisateur.ANNEE_HORIZON,")
					.append("proj_utilisateur.ID_USER,")
					.append("proj_utilisateur.NOM,")
					.append("proj_utilisateur.STANDARD,")
					.append("proj_utilisateur.PYRAMIDE_GENERATION,")
					.append("proj_utilisateur.VALIDATION,")
					.append("proj_utilisateur.CALAGE,")
					.append("proj_utilisateur.ENGLOBANTE,")
					.append("proj_utilisateur.ACTIFS,")
					.append("proj_utilisateur.MENAGES,")
					.append("proj_utilisateur.ELEVES,")
					.append("proj_utilisateur.ID_PROJ_ETALON,")
					.append("proj_utilisateur.ID_PROJ_ENGLOBANTE,")
					.append("proj_utilisateur.DATE_CREATION,")
					.append("proj_utilisateur.LIBELLE")
			.append(" FROM")
			.append("        def_projection proj_utilisateur ")
			.append("WHERE ")              
			.append("		 proj_utilisateur.id_user= :id_user and proj_utilisateur.standard = 0");
			
			return session.createSQLQuery(selectBufferise.toString())
					.addEntity(Projection.class)
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")

	public List<Projection> findProjectionPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT ")
					.append("proj_utilisateur.ID_PROJECTION,")
					.append("proj_utilisateur.ID_SCENARIO,")
					.append("proj_utilisateur.ID_ZONAGE,")
					.append("proj_utilisateur.ANNEE_REFERENCE,")
					.append("proj_utilisateur.ANNEE_HORIZON,")
					.append("proj_utilisateur.ID_USER,")
					.append("proj_utilisateur.NOM,")
					.append("proj_utilisateur.STANDARD,")
					.append("proj_utilisateur.PYRAMIDE_GENERATION,")
					.append("proj_utilisateur.VALIDATION,")
					.append("proj_utilisateur.CALAGE,")
					.append("proj_utilisateur.ENGLOBANTE,")
					.append("proj_utilisateur.ACTIFS,")
					.append("proj_utilisateur.MENAGES,")
					.append("proj_utilisateur.ELEVES,")
					.append("proj_utilisateur.ID_PROJ_ETALON,")
					.append("proj_utilisateur.ID_PROJ_ENGLOBANTE,")
					.append("proj_utilisateur.DATE_CREATION,")
					.append("proj_utilisateur.LIBELLE")
			.append(" FROM")
			.append("        def_projection proj_utilisateur ")
			.append("WHERE ")              
			.append("		 proj_utilisateur.id_user= :id_user and proj_utilisateur.standard = 1");
			
			return session.createSQLQuery(selectBufferise.toString())
					.addEntity(Projection.class)
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findNomProjectionNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT ")
					.append("proj_utilisateur.nom")
			.append(" FROM")
			.append("        def_projection proj_utilisateur ")
			.append("WHERE ")              
			.append("		 proj_utilisateur.id_user= :id_user and proj_utilisateur.standard = 0");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")

	public List<String> findNomProjectionPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT ")
					.append("proj_utilisateur.nom")
			.append(" FROM")
			.append("        def_projection proj_utilisateur ")
			.append("WHERE ")              
			.append("		 proj_utilisateur.id_user= :id_user and proj_utilisateur.standard = 1");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<Integer> findIdProjectionNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT ")
					.append("proj_utilisateur.ID_PROJECTION as id_resultat")
			.append(" FROM")
			.append("        def_projection proj_utilisateur ")
			.append("WHERE ")              
			.append("		 proj_utilisateur.id_user= :id_user and proj_utilisateur.standard = 0");
			
			return session.createSQLQuery(selectBufferise.toString())
					.addScalar("id_resultat", new IntegerType())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")

	public List<Integer> findIdProjectionPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT ")
			.append("proj_utilisateur.ID_PROJECTION as id_resultat")
			.append(" FROM")
			.append("        def_projection proj_utilisateur ")
			.append("WHERE ")              
			.append("		 proj_utilisateur.id_user= :id_user and proj_utilisateur.standard = 1");
			
			return session.createSQLQuery(selectBufferise.toString())
					.addScalar("id_resultat", new IntegerType())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	

	@SuppressWarnings("deprecation")
	public void deleteDonneesResiduelles(int idProjection) {
		Session session = getSession();
		try {
			Statement statement = session.connection().createStatement();
			statement
					.executeUpdate("delete from CB_POPULATION where PROJECTION="
							+ idProjection);
			statement
					.executeUpdate("delete from USER_POPULATION where PROJECTION="
							+ idProjection);
		} catch (SQLException sqle) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.003"), sqle);
		}
	}

	public String findPopulation(Projection projection) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			String select = "select" +
					" CASE count(*)"+
						" when 2 THEN 1" + 
						" else 0" +
						" end" +
					" from USER_POPULATION" +
					" where projection = " + projection.getId();
			return session.createSQLQuery(select).uniqueResult().toString();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String findPopulationEnglobante(Projection projection) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			String select = "select" +
					" count(*)" +
					" from USER_POPULATION" +
					" where projection = " + projection.getId();
			return session.createSQLQuery(select).uniqueResult().toString();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findUtilisateurs(Projection projection){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			String select = "select" +
					" id_user" +
					" from DEF_PROJECTION" +
					" where id_proj_etalon = " + projection.getId() + " OR id_proj_englobante = "+ projection.getId();
			return session.createSQLQuery(select).list();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Projection> findByScenario(Scenario scenario){
		try {					

				Criteria cri = getSession().createCriteria(Projection.class)
				.add(
					Restrictions.eq("scenario", scenario));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Projection> findByZonage(Zonage zonage){
		try {					

				Criteria cri = getSession().createCriteria(Projection.class)
				.add(
					Restrictions.eq("zonage", zonage));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	
	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer){
		
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM DEF_PROJECTION ")
				.append("       WHERE ID_PROJECTION IN (:listeIdProjectionASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdProjectionASupprimer",IdsProjectionsASupprimer)
								.executeUpdate();
			
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	public Integer deleteCbPopulationByListIdProjection(List<Integer> IdsProjectionsASupprimer){
		
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM CB_POPULATION ")
				.append("       WHERE PROJECTION IN (:listeIdProjectionASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdProjectionASupprimer",IdsProjectionsASupprimer)
								.executeUpdate();
			
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	public Integer deleteUserPopulationByListIdProjection(List<Integer> IdsProjectionsASupprimer){
		
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM USER_POPULATION ")
				.append("       WHERE PROJECTION IN (:listeIdProjectionASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdProjectionASupprimer",IdsProjectionsASupprimer)
								.executeUpdate();
			
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectionAffichageDataTable> findAllDTO(String idUser) {
		Session session = getSession();
		try {
			String select = "select new fr.insee.omphale.ihm.util.dataTable.ProjectionAffichageDataTable(projection.id, projection.nom, projection.standard, projection.validation, projection.englobante, projection.libelle, projection.dateCreation) "
					+ "	from Projection projection where projection.utilisateur.id = :idUser ORDER BY projection.dateCreation ASC";

			return session
						.createQuery(select)
						.setParameter("idUser", idUser)
						.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
