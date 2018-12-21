package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2ToutesZonesDeDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class Tableau2ToutesZonesDeDAO extends GenericDAO<String, Integer> implements
		ITableau2ToutesZonesDeDAO {

	
	

	@SuppressWarnings("unchecked")
	public List<Object[]> getDeAnneeDebut(
			String idUser, 
			String prefixe)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ZONE, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60') tr_age, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_agrege_immig");
			str.append(" group by ZONE, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60')");
			str.append(" order by ZONE, tr_age");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDeAnnee(
			String idUser, 
			String prefixe, 
			String annee)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ZONE, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60') tr_age, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_immig");
			str.append(" where annee_pas = ");
			str.append(annee);
			str.append(" group by ZONE, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60')");
			str.append(" order by ZONE, tr_age");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getDe(
			String idUser, 
			String prefixe)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ZONE, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60') tr_age, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_immig");
			str.append(" group by ZONE, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60')");
			str.append(" order by ZONE, tr_age");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
