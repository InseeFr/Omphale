package fr.insee.omphale.dao.projection.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fr.insee.omphale.dao.projection.IScenarioDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Scenario;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour Scenario
 *
 */
public class ScenarioDAO extends GenericDAO<Scenario, Integer> implements
		IScenarioDAO {

	@Override
	@SuppressWarnings("unchecked")
	public List<Scenario> findAll(String idUser) {

		try {
			Criteria cri = getSession().createCriteria(Scenario.class).add(
					Restrictions.eq("utilisateur.id", idUser)).addOrder(
					Order.desc("dateCreation"));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Scenario> findAll(String idUser, Boolean standard) {

		try {
			Criteria cri = getSession().createCriteria(Scenario.class).add(
					Restrictions.eq("utilisateur.id", idUser)).add(
					Restrictions.eq("standard", standard)).addOrder(
					Order.desc("dateCreation"));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Scenario> findAll(String idUser, Boolean standard, Boolean validation) {

		try {
			Criteria cri = getSession().createCriteria(Scenario.class).add(
					Restrictions.eq("utilisateur.id", idUser)).add(
					Restrictions.eq("standard", standard)).add(
					Restrictions.eq("validation", validation)).addOrder(
					Order.desc("dateCreation"));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Scenario> findAll(Boolean standard) {
		Session session = getSession();
		try {
			Criteria cri = session.createCriteria(Scenario.class).add(
					Restrictions.eq("standard", standard)).addOrder(
					Order.desc("dateCreation"));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> findByIdPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("select distinct(scenario_user.id_scenario) ") 
			 .append("    from ") 
			 .append("        scenar_non_loc scenario_user ") 
			 .append("              inner join ") 
			 .append("        (select * from def_projection ) dp ") 
			 .append("        on (") 
			 .append("              dp.id_scenario = scenario_user.id_scenario") 
			 .append("                and") 
			 .append("              dp.id_user != scenario_user.id_user") 
			 .append("            ) ") 
			 .append("        where scenario_user.id_user = :id_user");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> findByIdNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			 .append("select distinct(snl.id_scenario) from scenar_non_loc snl where snl.id_scenario not in (") 
			 .append("select distinct(scenario_user.id_scenario) ") 
			 .append("    from ") 
			 .append("        scenar_non_loc scenario_user ") 
			 .append("              inner join ") 
			 .append("        (select * from def_projection ) dp ") 
			 .append("        on (") 
			 .append("              dp.id_scenario = scenario_user.id_scenario") 
			 .append("                and") 
			 .append("              dp.id_user != scenario_user.id_user") 
			 .append("            ) ") 
			 .append("        where scenario_user.id_user = :id_user") 
			 .append(") and  snl.id_user = :id_user");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findByNomPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			 .append("select distinct(scenario_user.nom) ") 
			 .append("    from ") 
			 .append("        scenar_non_loc scenario_user ") 
			 .append("              inner join ") 
			 .append("        (select * from def_projection ) dp ") 
			 .append("        on (") 
			 .append("              dp.id_scenario = scenario_user.id_scenario") 
			 .append("                and") 
			 .append("              dp.id_user != scenario_user.id_user") 
			 .append("            ) ") 
			 .append("        where scenario_user.id_user = :id_user");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findByNomNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			 .append("select distinct(snl.nom) from scenar_non_loc snl where snl.id_scenario not in (") 
			 .append("select distinct(scenario_user.id_scenario) ") 
			 .append("    from ") 
			 .append("        scenar_non_loc scenario_user ") 
			 .append("              inner join ") 
			 .append("        (select * from def_projection ) dp ") 
			 .append("        on (") 
			 .append("              dp.id_scenario = scenario_user.id_scenario") 
			 .append("                and") 
			 .append("              dp.id_user != scenario_user.id_user") 
			 .append("            ) ") 
			 .append("        where scenario_user.id_user = :id_user") 
			 .append(") and  snl.id_user = :id_user");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@Override
	public int deleteByIdScenario(List<Integer> scenariosASupprimerId) {
 		Session session = getSession();
		
			try {
			
				StringBuffer selectBufferise = new StringBuffer();
				
				selectBufferise
					.append("DELETE FROM SCENAR_NON_LOC ")
					.append("       WHERE ID_SCENARIO IN (:listeIdScenarioASupprimer)");
				
				int updated = session
									.createSQLQuery(selectBufferise.toString())
									.setParameterList("listeIdScenarioASupprimer",scenariosASupprimerId)
									.executeUpdate();
				return updated;
				
			} catch (HibernateException he) {
				throw new RuntimeException(ParametresMessages
						.getString("error.dao.001"), he);
			}
	}
}
