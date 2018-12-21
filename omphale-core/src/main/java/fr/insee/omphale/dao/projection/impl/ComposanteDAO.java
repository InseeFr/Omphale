package fr.insee.omphale.dao.projection.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import fr.insee.omphale.dao.projection.IComposanteDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour Composante
 *
 */
public class ComposanteDAO extends GenericDAO<Composante, String> implements
		IComposanteDAO {
	
	@SuppressWarnings("unused")
	private Session session;

	@SuppressWarnings("unchecked")
	public List<Composante> findAllReferenceesEvolutionNL(Boolean standard) {
		session = getSession();
		try {				
			DetachedCriteria cri2 = null;
			// standard = true
			if (standard != null && standard.booleanValue() == true) {
				cri2 = DetachedCriteria.forClass(EvolutionNonLocalisee.class)
						.add(Restrictions.eq("standard", standard))
							.setProjection(
									Projections.distinct(Projections.property("composante.code")));	
			}
			// standard = false ou null
			else {
				cri2 = DetachedCriteria.forClass(EvolutionNonLocalisee.class)
						.add(Restrictions.or(Restrictions.eq("standard", standard), Restrictions.isNull("standard")))
								.setProjection(
										Projections.distinct(Projections.property("composante.code")));	
			}
			Criteria cri = getSession().createCriteria(Composante.class).add(Subqueries.propertyIn("code", cri2));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Composante> findAllReferenceesEvolutionNL(String idUser, Boolean standard) {
		session = getSession();
		try {				
			DetachedCriteria cri2 = null;
			// standard = true
			if (standard != null && standard.booleanValue() == true) {
				cri2 = DetachedCriteria.forClass(EvolutionNonLocalisee.class)
						.add(Restrictions.eq("utilisateur.id", idUser))
						.add(Restrictions.eq("standard", standard))
							.setProjection(
									Projections.distinct(Projections.property("composante.code")));	
			}
			// standard = false ou null
			else {
				cri2 = DetachedCriteria.forClass(EvolutionNonLocalisee.class)
						.add(Restrictions.eq("utilisateur.id", idUser))
						.add(Restrictions.or(Restrictions.eq("standard", standard), Restrictions.isNull("standard")))
								.setProjection(
										Projections.distinct(Projections.property("composante.code")));	
			}
			Criteria cri = getSession().createCriteria(Composante.class).add(Subqueries.propertyIn("code", cri2));
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
