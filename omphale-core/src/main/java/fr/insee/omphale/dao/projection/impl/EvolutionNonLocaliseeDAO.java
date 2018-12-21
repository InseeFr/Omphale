package fr.insee.omphale.dao.projection.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fr.insee.omphale.dao.projection.IEvolutionNonLocaliseeDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour EvolutionNonLocalisee
 *
 */
public class EvolutionNonLocaliseeDAO extends
		GenericDAO<EvolutionNonLocalisee, Integer> implements
		IEvolutionNonLocaliseeDAO {

	@SuppressWarnings("unchecked")
	public List<EvolutionNonLocalisee> findAll(Boolean standard,
			String composante) {

		try {
			Criteria cri = null;
			// standard = true
			if (standard != null && standard.booleanValue() == true) {
				cri = getSession().createCriteria(EvolutionNonLocalisee.class)
						.add(Restrictions.eq("standard", standard)).add(
								Restrictions.eq("composante.code", composante))
						.addOrder(Order.asc("nom"));

			}
			// standard = false ou null
			else {
				cri = getSession().createCriteria(EvolutionNonLocalisee.class)
						.add(
								Restrictions.or(Restrictions.eq("standard",
										standard), Restrictions
										.isNull("standard"))).add(
								Restrictions.eq("composante.code", composante))
						.addOrder(Order.asc("nom"));
			}
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<EvolutionNonLocalisee> findAll(String idUser, Boolean standard,
			String composante) {

		try {
			Criteria cri = null;
			// standard = true
			if (standard != null && standard.booleanValue() == true) {
				cri = getSession().createCriteria(EvolutionNonLocalisee.class)
						.add(Restrictions.eq("utilisateur.id", idUser)).add(
								Restrictions.eq("standard", standard)).add(
								Restrictions.eq("composante.code", composante))
						.addOrder(Order.asc("nom"));

			}
			// standard = false ou null
			else {
				cri = getSession().createCriteria(EvolutionNonLocalisee.class)
						.add(Restrictions.eq("utilisateur.id", idUser)).add(
								Restrictions.or(Restrictions.eq("standard",
										standard), Restrictions
										.isNull("standard"))).add(
								Restrictions.eq("composante.code", composante))
						.addOrder(Order.asc("nom"));
			}
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	public long compterEvolutionsNonLocalisees(Utilisateur utilisateur) {
		Session session = getSession();
		try {
			String select = "select count(*) from EvolutionNonLocalisee z where z.utilisateur = :utilisateur";
			return (Long) session.createQuery(select).setParameter(
					"utilisateur", utilisateur).uniqueResult();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<EvolutionNonLocalisee> findByUtilisateur(Utilisateur utilisateur) {
		Session session = getSession();
		try {
			String select = "from EvolutionNonLocalisee ev where ev.utilisateur = :utilisateur and ev.standard = false";
			return session.createQuery(select).setParameter("utilisateur",
					utilisateur).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<EvolutionNonLocalisee> findStandard() {

		try {
			Criteria cri = getSession().createCriteria(
					EvolutionNonLocalisee.class).add(
					Restrictions.eq("standard", true));
			List liste = cri.list();
			return liste;
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<EvolutionNonLocalisee> findByHypothese(Hypothese hypothese) {
		Session session = getSession();
		try {
			String select = "from EvolutionNonLocalisee ev where ev.hypothese = :hypothese";
			return session.createQuery(select).setParameter("hypothese",
					hypothese).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	// http://www.roseindia.net/forum/post-1216.html&sid=007ae3b51e3507a59faa40816c1703f2
	@SuppressWarnings("unchecked")
	public List<EvolutionNonLocalisee> findAllOrdreDefaut(
			String listeIdentifiantENL) {
		Session session = getSession();
		try {
			StringBuffer select = new StringBuffer();
			select.append("from EvolutionNonLocalisee e where e.id in ");
			select.append(listeIdentifiantENL);
			select.append(" order by e.anneeDeb asc, e.anneeFin desc,");
			select
					.append(" decode (sexe_deb||sexe_fin, 12, 0, 21, 0, 11, 1, 22, 2) asc, ");
			select.append(" e.ageDeb asc, e.ageFin desc");
			return session.createQuery(select.toString()).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<Integer> findByZone(Zone zone){
		Session session = getSession();
		try {
			String select = "select id_evol_non_loc from evol_non_loc z where z.zone = :idZone OR z.zone_destination = :zone";
			return session.createQuery(select).setParameter(
					"zone", zone).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<EvolutionNonLocalisee> findByUtilisateurStandardOuNon(Utilisateur utilisateur){
		Session session = getSession();
		try {
			String select = "from EvolutionNonLocalisee ev where ev.utilisateur = :utilisateur";
			return session.createQuery(select).setParameter("utilisateur",
					utilisateur).list();
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
				.append("DELETE FROM EVOL_NON_LOC ")
				.append("       WHERE ID_EVOL_NON_LOC IN (:listeEIdvolutionsNonLocaliseesASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeEIdvolutionsNonLocaliseesASupprimer",evolutionsNonLocaliseesASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@Override
	public int deleteHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId) {
 		Session session = getSession();
		
		try {
		
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM EVOL_NON_LOC ")
				.append("       WHERE ID_HYPOTHESE IN (:listeidHypotheseASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeidHypotheseASupprimer",hypothesesASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
}
