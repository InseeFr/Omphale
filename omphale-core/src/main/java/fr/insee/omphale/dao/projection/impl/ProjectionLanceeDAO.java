package fr.insee.omphale.dao.projection.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.dao.projection.IProjectionLanceeDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.domaine.projection.ProjectionLancee;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour ProjectionLancee
 *
 */
public class ProjectionLanceeDAO extends GenericDAO<ProjectionLancee, Integer> implements
		IProjectionLanceeDAO {

	@Override
	@SuppressWarnings("unchecked")
	public List<ProjectionLancee> findAll(String idUser) {
		
		Session session = getSession();
		try {
			String select = "from ProjectionLancee projectionLancee where " +
					"projectionLancee.projection.utilisateur.id = :idUser";
			return session.createQuery(select)
					.setParameter("idUser", idUser).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ProjectionLancee> findAll(int idProjection) {
		Session session = getSession();
		try {
			String select = "from ProjectionLancee projectionLancee where " +
					"projectionLancee.projection.id = :idProjection";
			return session.createQuery(select)
					.setInteger("idProjection", idProjection).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ProjectionLancee> findByProjection(Projection projection){
		Session session = getSession();
		try {
			String select = "from ProjectionLancee projectionLancee where " +
					"projectionLancee.projection = :projection";
			return session.createQuery(select).setParameter("projection", projection).list();
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
				.append("DELETE FROM PROJECTION_LANCEE ")
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

}
