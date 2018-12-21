package fr.insee.omphale.dao.projection.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.dao.projection.IValeurCubeHypotheseDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.ValeurCubeHypothese;
import fr.insee.omphale.domaine.projection.ValeurCubeHypotheseId;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour ValeurCubeHypothese
 *
 */
public class ValeurCubeHypotheseDAO extends
		GenericDAO<ValeurCubeHypothese, ValeurCubeHypotheseId> implements
		IValeurCubeHypotheseDAO {

	public void deleteByHypothese(Hypothese hypothese) {
		Session session = getSession();
		try {
			String delete = "delete from ValeurCubeHypothese v where v.id.hypothese = :hypothese";
			session.createQuery(delete).setParameter("hypothese", hypothese)
					.executeUpdate();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.003"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ValeurCubeHypothese> findByHypothese(Hypothese hypothese) {
		Session session = getSession();
		try {
			String select = "from ValeurCubeHypothese v where v.id.hypothese = :hypothese";
			return session.createQuery(select)
					.setParameter("hypothese", hypothese).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
