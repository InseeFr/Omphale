package fr.insee.omphale.dao.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.dao.IParametresDAO;
import fr.insee.omphale.dao.util.HibernateUtil;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour vérifier en base les cycles ouverts
 *
 */
public class ParametresDAO implements IParametresDAO {

	@SuppressWarnings("unchecked")
	public List<BigDecimal> getCyclesOuverts() {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		try {
			String select = "select * from CYCLE_OUVERT order by annee";
			return session.createSQLQuery(select).list();
		} catch (HibernateException e) {
			e.printStackTrace();
			return null;
		}
	}

}
