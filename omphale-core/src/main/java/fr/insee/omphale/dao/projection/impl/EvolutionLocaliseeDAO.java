package fr.insee.omphale.dao.projection.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import fr.insee.omphale.dao.projection.IEvolutionLocaliseeDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.EvolutionLocalisee;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Projection;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour EvolutionLocalisee
 *
 */
public class EvolutionLocaliseeDAO extends
		GenericDAO<EvolutionLocalisee, Integer> implements
		IEvolutionLocaliseeDAO {
	

	
	public EvolutionLocalisee findByRang(Projection projection, Composante composante, int rang ){

		try {					

				Criteria cri = getSession().createCriteria(EvolutionLocalisee.class)
				.add(
					Restrictions.eq("projection", projection))
						.add(Restrictions.eq("composante", composante))
						.add(Restrictions.eq("rang", rang));
			return (EvolutionLocalisee)cri.list().get(0);
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}		
	}
	
	@SuppressWarnings("unchecked")
	public Set<EvolutionLocalisee> findByProjection(Projection projection ){

		Set<EvolutionLocalisee> setels = new HashSet<EvolutionLocalisee>();
		
		try {					

				Criteria cri = getSession().createCriteria(EvolutionLocalisee.class)
				.add(
					Restrictions.eq("projection", projection));
				setels.addAll(cri.list());
			return setels;
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}		
	}
	
	
	@SuppressWarnings("unchecked")
	public List<EvolutionLocalisee> findByENL(EvolutionNonLocalisee enl){

		try {					

				Criteria cri = getSession().createCriteria(EvolutionLocalisee.class)
				.add(
					Restrictions.eq("evolNonLoc", enl));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EvolutionLocalisee> findByZone(Zone zone){
		try {					
				Criteria cri = getSession().createCriteria(EvolutionLocalisee.class)
				.add(
							Restrictions.or(
												Restrictions.eq("zone", zone), 
												Restrictions.eq("zoneDestination", zone)
											)
					);
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	



	public Boolean nettoyage() {
		this.flush();
		return true;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Integer> findIdENLPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT enlbase.id_evol_non_loc ")
			.append("	FROM ")
			.append("		evol_non_loc enlbase ")
			.append("	WHERE ")
			.append("		enlbase.id_user = :id_user ")
			.append("		AND ")
			.append("		enlbase.id_evol_non_loc ")
			.append("		IN ")
			.append("		(")
			.append("			SELECT DISTINCT(enl.id_evol_non_loc) ")
			.append("			FROM ")
			.append("				(")
			.append("					SELECT * ")
			.append("					FROM ")
			.append("						evol_non_loc ")
			.append("					WHERE ")
			.append("						id_user = :id_user ")
			.append("				) enl ")
			.append("			INNER JOIN ")
			.append("				(")
			.append("					SELECT eds.id_evol_non_loc, snl.id_user  ")
			.append("					FROM ")
			.append("						evol_de_scenar eds ")
			.append("					INNER JOIN")
			.append("						scenar_non_loc snl ")
			.append("					ON (eds.id_scenario = snl.id_scenario) ")
			.append("					WHERE id_user != :id_user")
			.append("				) tmp")
			.append("         ON ")
			.append("          (")
			.append("				enl.id_evol_non_loc = tmp.id_evol_non_loc")
			.append("            )")
			.append("         )");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Integer> findIdENLNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT enlbase.id_evol_non_loc ")
			.append("	FROM ")
			.append("		evol_non_loc enlbase ")
			.append("	WHERE ")
			.append("		enlbase.id_user = :id_user ")
			.append("		AND ")
			.append("		enlbase.id_evol_non_loc ")
			.append("		NOT IN ")
			.append("		(")
			.append("			SELECT DISTINCT(enl.id_evol_non_loc) ")
			.append("			FROM ")
			.append("				(")
			.append("					SELECT * ")
			.append("					FROM ")
			.append("						evol_non_loc ")
			.append("					WHERE ")
			.append("						id_user = :id_user ")
			.append("				) enl ")
			.append("			INNER JOIN ")
			.append("				(")
			.append("					SELECT eds.id_evol_non_loc, snl.id_user  ")
			.append("					FROM ")
			.append("						evol_de_scenar eds ")
			.append("					INNER JOIN")
			.append("						scenar_non_loc snl ")
			.append("					ON (eds.id_scenario = snl.id_scenario) ")
			.append("					WHERE id_user != :id_user")
			.append("				) tmp")
			.append("         ON ")
			.append("          (")
			.append("				enl.id_evol_non_loc = tmp.id_evol_non_loc")
			.append("            )")
			.append("         )");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> findNomENLPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT enlbase.nom ")
			.append("	FROM ")
			.append("		evol_non_loc enlbase ")
			.append("	WHERE ")
			.append("		enlbase.id_user = :id_user ")
			.append("		AND ")
			.append("		enlbase.id_evol_non_loc ")
			.append("		IN ")
			.append("		(")
			.append("			SELECT DISTINCT(enl.id_evol_non_loc) ")
			.append("			FROM ")
			.append("				(")
			.append("					SELECT * ")
			.append("					FROM ")
			.append("						evol_non_loc ")
			.append("					WHERE ")
			.append("						id_user = :id_user ")
			.append("				) enl ")
			.append("			INNER JOIN ")
			.append("				(")
			.append("					SELECT eds.id_evol_non_loc, snl.id_user  ")
			.append("					FROM ")
			.append("						evol_de_scenar eds ")
			.append("					INNER JOIN")
			.append("						scenar_non_loc snl ")
			.append("					ON (eds.id_scenario = snl.id_scenario) ")
			.append("					WHERE id_user != :id_user")
			.append("				) tmp")
			.append("         ON ")
			.append("          (")
			.append("				enl.id_evol_non_loc = tmp.id_evol_non_loc")
			.append("            )")
			.append("         )");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<String> findNomENLNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT enlbase.nom ")
			.append("	FROM ")
			.append("		evol_non_loc enlbase ")
			.append("	WHERE ")
			.append("		enlbase.id_user = :id_user ")
			.append("		AND ")
			.append("		enlbase.id_evol_non_loc ")
			.append("		NOT IN ")
			.append("		(")
			.append("			SELECT DISTINCT(enl.id_evol_non_loc) ")
			.append("			FROM ")
			.append("				(")
			.append("					SELECT * ")
			.append("					FROM ")
			.append("						evol_non_loc ")
			.append("					WHERE ")
			.append("						id_user = :id_user ")
			.append("				) enl ")
			.append("			INNER JOIN ")
			.append("				(")
			.append("					SELECT eds.id_evol_non_loc, snl.id_user  ")
			.append("					FROM ")
			.append("						evol_de_scenar eds ")
			.append("					INNER JOIN")
			.append("						scenar_non_loc snl ")
			.append("					ON (eds.id_scenario = snl.id_scenario) ")
			.append("					WHERE id_user != :id_user")
			.append("				) tmp")
			.append("         ON ")
			.append("          (")
			.append("				enl.id_evol_non_loc = tmp.id_evol_non_loc")
			.append("            )")
			.append("         )");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
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
				.append("DELETE FROM EVOL_LOCALISE ")
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

	@Override
	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId) {
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM EVOL_LOCALISE ")
				.append("       WHERE ID_EVOL_NON_LOC IN (:listeIdENLASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdENLASupprimer",evolutionsNonLocaliseesASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

}
