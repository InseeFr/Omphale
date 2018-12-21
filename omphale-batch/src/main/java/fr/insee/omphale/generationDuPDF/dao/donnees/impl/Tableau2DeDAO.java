package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2DeDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class Tableau2DeDAO extends GenericDAO<String, Integer> implements
		ITableau2DeDAO {

	
	

	@SuppressWarnings("unchecked")
	public List<Object[]> getDeAnneeDebut(
			String idUser, 
			String prefixe,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select DESTINATION, ORIGINE, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60') tr_age, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_agrege_top_flux");
			str.append(" where ");
			str.append(" DESTINATION != ORIGINE");
			str.append(" and (DESTINATION, ORIGINE) in (");
			str.append("	select zone_etude, zone_echange");
			str.append(" 	from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init)");
			str.append(" and DESTINATION in ");
			str.append(zonesEtude);
			str.append(" group by ORIGINE, DESTINATION, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60')");
			str.append(" order by ORIGINE, DESTINATION, tr_age");
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
			String annee,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select DESTINATION, ORIGINE, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60') tr_age, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_top_flux");
			str.append(" where annee_pas = ");
			str.append(annee);
			str.append(" and DESTINATION != ORIGINE");
			str.append(" and (DESTINATION, ORIGINE) in (");
			str.append("	select zone_etude, zone_echange");
			str.append(" 	from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init)");
			str.append(" and DESTINATION in ");
			str.append(zonesEtude);
			str.append(" group by ORIGINE, DESTINATION, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60')");
			str.append(" order by ORIGINE, DESTINATION, tr_age");
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
			String prefixe,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select DESTINATION, ORIGINE, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60') tr_age, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_top_flux");
			str.append(" where DESTINATION != ORIGINE");
			str.append(" and (DESTINATION, ORIGINE) in (");
			str.append("	select zone_etude, zone_echange");
			str.append(" 	from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init)");
			str.append(" and DESTINATION in ");
			str.append(zonesEtude);
			str.append(" group by ORIGINE, DESTINATION, decode( trunc((age+10)/35), 0, '1-24', 1, '25-59', 2, '60', 3, '60')");
			str.append(" order by ORIGINE, DESTINATION, tr_age");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
