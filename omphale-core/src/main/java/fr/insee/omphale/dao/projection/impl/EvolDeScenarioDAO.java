package fr.insee.omphale.dao.projection.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fr.insee.omphale.dao.projection.IEvolDeScenarioDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.projection.EvolDeScenario;
import fr.insee.omphale.domaine.projection.EvolDeScenarioId;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour EvolDeScenario
 *
 */
public class EvolDeScenarioDAO extends GenericDAO<EvolDeScenario, EvolDeScenarioId> implements
		IEvolDeScenarioDAO {

	@SuppressWarnings("unchecked")
	public List<EvolDeScenario> getListe(int scenarioId) {
		try {					
			Criteria cri = getSession().createCriteria(EvolDeScenario.class).add(
					Restrictions.eq("id.scenario.id", scenarioId)).addOrder(Order.desc("rang"));
			return cri.list();
		} 
		catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	

	@SuppressWarnings("unchecked")
	public List<EvolDeScenario> findByEvolution(EvolutionNonLocalisee evolution){
		try {					
			Criteria cri = getSession().createCriteria(EvolDeScenario.class).add(
					Restrictions.eq("id.evolutionNonLocalisee", evolution));
					List<EvolDeScenario> liste = cri.list();
					return liste;
		} 
		catch (HibernateException he) {
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
				.append("DELETE FROM EVOL_DE_SCENAR ")
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


	@Override
	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId)  {
 		Session session = getSession();
		
			try {
			
				StringBuffer selectBufferise = new StringBuffer();
				
				selectBufferise
					.append("DELETE FROM EVOL_DE_SCENAR ")
					.append("       WHERE ID_EVOL_NON_LOC IN (:listeIdEvolutionNonLocaliseeoASupprimer)");
				
				int updated = session
									.createSQLQuery(selectBufferise.toString())
									.setParameterList("listeIdEvolutionNonLocaliseeoASupprimer",evolutionsNonLocaliseesASupprimerId)
									.executeUpdate();
				
				return updated;
				
			} catch (HibernateException he) {
				throw new RuntimeException(ParametresMessages
						.getString("error.dao.001"), he);
			}

	}
}



