package fr.insee.omphale.dao.projection.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.dao.projection.IMethodeDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.MethodeEvolution;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour MethodeEvolution
 *
 */
public class MethodeDAO extends GenericDAO<MethodeEvolution, String> implements
		IMethodeDAO {

	/* (non-Javadoc)
	 * @see fr.insee.omphale.dao.projection.IMethodeDAO#findByComposante(fr.insee.omphale.domaine.projection.Composante)
	 */
	@SuppressWarnings("unchecked")
	public List<MethodeEvolution> findByComposante(Composante composante) {
		Session session = getSession();
		try {
			String select = "from MethodeEvolution me where me.composante = :composante";

			return session.createQuery(select)
					.setParameter("composante", composante).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
