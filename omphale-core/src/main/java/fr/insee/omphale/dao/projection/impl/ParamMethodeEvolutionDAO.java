package fr.insee.omphale.dao.projection.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fr.insee.omphale.dao.projection.IParamMethodeEvolutionDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.projection.MethodeEvolution;
import fr.insee.omphale.domaine.projection.ParamMethodeEvolution;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour ParamMethodeEvolution
 *
 */
public class ParamMethodeEvolutionDAO extends GenericDAO<ParamMethodeEvolution, String> implements
		IParamMethodeEvolutionDAO {
	
	@SuppressWarnings("unchecked")
	public List<ParamMethodeEvolution> findByMethodeEvolution(MethodeEvolution methode){
		try {
			Criteria cri = getSession().createCriteria(ParamMethodeEvolution.class).add(Restrictions.eq("methode", methode)).addOrder(Order.asc("rang"));
			List<ParamMethodeEvolution> liste = cri.list();
			
			return liste;
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
