package fr.insee.omphale.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import fr.insee.omphale.dao.IUtilisateurDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Scenario;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour Utilisateur
 *
 */
public class UtilisateurDAO extends GenericDAO<Utilisateur, String> implements
		IUtilisateurDAO {

	
	public Utilisateur findByIdep(String idep) {
		try {					
	
				Criteria cri = getSession().createCriteria(Utilisateur.class)
						.add(Restrictions.eq("idep", idep));
				Utilisateur u = (Utilisateur)cri.uniqueResult();
			return u;
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}	
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Utilisateur> findByEvolutionNonLocalisee(
			EvolutionNonLocalisee evolutionNonLocalisee) {
		try {
			Criteria cri = null;
				cri = getSession().createCriteria(EvolutionNonLocalisee.class)
						.add( 			
								Restrictions.eq("id", evolutionNonLocalisee.getId())
							)
						.setProjection(
											Projections.distinct(Projections.property("utilisateur"))
									);
				
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Utilisateur> findByZonage(Zonage zonage) {
		try {
			Criteria cri = null;
				cri = getSession().createCriteria(Zonage.class)
						.add( 			
								Restrictions.eq("id", zonage.getId())
							)
						.setProjection(
											Projections.distinct(Projections.property("utilisateur"))
									);
				
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Utilisateur> findByZone(Zone zone) {
		try {
			Criteria cri = null;
				cri = getSession().createCriteria(Zone.class)
						.add( 			
								Restrictions.eq("id", zone.getId())
							)
						.setProjection(
											Projections.distinct(Projections.property("utilisateur"))
									);
				
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Utilisateur> findByScenario(Scenario scenario) {
		try {
			Criteria cri = null;
				cri = getSession().createCriteria(Scenario.class)
						.add( 			
								Restrictions.eq("id", scenario.getId())
							)
						.setProjection(
											Projections.distinct(Projections.property("utilisateur"))
									);
				
			return cri.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Utilisateur> findByRole(int role) {
		
		String queryString = "from Utilisateur u where u.role = :role";
		
		
		return (List<Utilisateur>)(getSession().createQuery(queryString).setParameter("role", role).list());
	}

}
