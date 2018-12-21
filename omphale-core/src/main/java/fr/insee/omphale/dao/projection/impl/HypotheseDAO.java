package fr.insee.omphale.dao.projection.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.dao.projection.IHypotheseDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.TypeEntite;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour Hypothese
 *
 */
public class HypotheseDAO extends GenericDAO<Hypothese, Integer> implements
		IHypotheseDAO {

	public long compterHypotheses(Utilisateur utilisateur) {
		Session session = getSession();
		try {
			String select = "select count(*) from Hypothese h where h.utilisateur = :utilisateur";
			return (Long) session.createQuery(select).setParameter(
					"utilisateur", utilisateur).uniqueResult();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Hypothese> findByUtilisateur(Utilisateur utilisateur) {
		Session session = getSession();
		try {
			String select = "from Hypothese h where h.utilisateur = :utilisateur and h.standard = false";
			return session.createQuery(select).setParameter("utilisateur",
					utilisateur).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Hypothese> findByTypeEntite(TypeEntite typeEntite) {
		Session session = getSession();
		try {
			String select = "from Hypothese hy where hy.typeEntite = :typeEntite";

			return session.createQuery(select).setParameter("typeEntite",
					typeEntite).list();
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
			.append("select distinct(enl.id_hypothese) ")
			.append("		from ")
			.append("				evol_non_loc enl ")
			.append("					inner join ")
			.append("				(")
			.append("                   select id_hypothese from user_hypothese where id_user = :id_user")
			.append("                )temp")
			.append("					on (enl.id_hypothese = temp.id_hypothese)")
			.append("        where id_user != :id_user");
			
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
			.append("select distinct(hypo.id_hypothese) ")
			.append("    from user_hypothese hypo  ")
			.append("    where hypo.id_hypothese not in (")
			.append("                                    select distinct(enl.id_hypothese) ")
			.append("                                      from evol_non_loc enl ")
			.append("                                        inner join (")
			.append("                                          select id_hypothese from user_hypothese where id_user = :id_user")
			.append("                                        )temp")
			.append("                                        on (enl.id_hypothese = temp.id_hypothese)")
			.append("                                        where id_user != :id_user")
			.append("                                   ) ")
			.append("    and hypo.id_user = :id_user");
			
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
			.append(" select hypo.nom ")
			.append("            from ")
			.append("                  user_hypothese hypo ")
			.append("            where ")
			.append("              hypo.id_hypothese ")
			.append("                    in (	select distinct(enl.id_hypothese)")
			.append("                              from ")
			.append("                                  evol_non_loc enl ")
			.append("                                    inner join ")
			.append("                                  (")
			.append("                                             select id_hypothese from user_hypothese where id_user = :id_user")
			.append("                                          )temp")
			.append("                                    on (enl.id_hypothese = temp.id_hypothese)")
			.append("                                  where id_user != :id_user")
			.append("                                )");
			
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
			.append("select distinct(hypo.nom) ")
			.append("    from user_hypothese hypo  ")
			.append("    where hypo.id_hypothese not in (")
			.append("                                    select distinct(enl.id_hypothese) ")
			.append("                                      from evol_non_loc enl ")
			.append("                                        inner join (")
			.append("                                          select id_hypothese from user_hypothese where id_user = :id_user")
			.append("                                        )temp")
			.append("                                        on (enl.id_hypothese = temp.id_hypothese)")
			.append("                                        where id_user != :id_user")
			.append("                                   ) ")
			.append("    and hypo.id_user = :id_user");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@Override
	public int deleteHypotheseByIdListeHypothese(List<Integer> hypothesesASupprimerId) {
 		Session session = getSession();
		
			try {
			
				StringBuffer selectBufferise = new StringBuffer();
				
				selectBufferise
					.append("DELETE FROM USER_HYPOTHESE US ")
					.append("       WHERE US.ID_HYPOTHESE IN (:listeIdHypotheseASupprimer)");

				int updated = session
									.createSQLQuery(selectBufferise.toString())
									.setParameterList("listeIdHypotheseASupprimer",hypothesesASupprimerId)
									.executeUpdate();
				return updated;
				
			} catch (HibernateException he) {
				throw new RuntimeException(ParametresMessages
						.getString("error.dao.001"), he);
			}
	}

	@Override
	public int deleteCbHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId) {
 		Session session = getSession();
		
			try {

				// on désactive la clé primaire composite
				session.createSQLQuery("alter table CB_Hypothese disable constraint CB_HYPOTHESE_CLE").executeUpdate();
				 // on désactive la clé étrangère
				session.createSQLQuery("alter table CB_Hypothese disable constraint CB_HYPOTHESE_ETR_UHY_0").executeUpdate();
	
				StringBuffer selectBufferise = new StringBuffer(); 
				int updated = 0;

				
				selectBufferise
				.append("DELETE FROM CB_HYPOTHESE CH ")
				.append("       WHERE CH.ID_HYPOTHESE IN (:listeIdHypotheseASupprimer)");
	
				updated = session
									.createSQLQuery(selectBufferise.toString())
									.setParameterList("listeIdHypotheseASupprimer",hypothesesASupprimerId)
									.executeUpdate();



				return updated;
				
			} catch (HibernateException he) {
				throw new RuntimeException(ParametresMessages
						.getString("error.dao.001"), he);
			}finally{
				// on active la clé primaire composite CB_HYPOTHESE_CLE
				session.createSQLQuery("alter table CB_Hypothese enable constraint CB_HYPOTHESE_CLE").executeUpdate();
				 // on active la clé étrangère CB_HYPOTHESE_ETR_UHY_0
				session.createSQLQuery("alter table CB_Hypothese enable constraint CB_HYPOTHESE_ETR_UHY_0").executeUpdate();
			}
	}
}
