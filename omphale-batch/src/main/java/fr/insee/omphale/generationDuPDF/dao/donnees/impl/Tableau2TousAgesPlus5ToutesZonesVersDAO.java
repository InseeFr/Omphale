package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5ToutesZonesVersDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class Tableau2TousAgesPlus5ToutesZonesVersDAO extends GenericDAO<String, Integer> implements
		ITableau2TousAgesPlus5ToutesZonesVersDAO {

	
	

	@SuppressWarnings("unchecked")
	public List<Object[]> getVersAnneeDebut(
			String idUser, 
			String prefixe)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ZONE, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_agrege_emig");
			str.append(" group by ZONE");
			str.append(" order by ZONE");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getVersAnnee(
			String idUser, 
			String prefixe, 
			String anneeDebut)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ZONE, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_emig");
			str.append(" where annee_pas = ");
			str.append(anneeDebut);
			str.append(" group by ZONE");
			str.append(" order by ZONE");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getVers(
			String idUser, 
			String prefixe)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ZONE, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_emig");
			str.append(" group by ZONE");
			str.append(" order by ZONE");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
