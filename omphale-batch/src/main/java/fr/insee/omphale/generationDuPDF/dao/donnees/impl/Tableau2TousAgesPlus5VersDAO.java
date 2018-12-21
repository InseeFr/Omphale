package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau2TousAgesPlus5VersDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class Tableau2TousAgesPlus5VersDAO extends GenericDAO<String, Integer> implements
		ITableau2TousAgesPlus5VersDAO {

	
	

	@SuppressWarnings("unchecked")
	public List<Object[]> getVersAnneeDebut(
			String idUser, 
			String prefixe,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ORIGINE, DESTINATION, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_agrege_top_flux");
			str.append(" where ");
			str.append(" DESTINATION != ORIGINE");
			str.append(" and (ORIGINE, DESTINATION) in (");
			str.append("	select zone_etude, zone_echange");
			str.append(" 	from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init)");
			str.append(" and ORIGINE in ");
			str.append(zonesEtude);
			str.append(" group by ORIGINE, DESTINATION");
			str.append(" order by ORIGINE, DESTINATION");
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
			String anneeDebut,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ORIGINE, DESTINATION, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_top_flux");
			str.append(" where annee_pas = ");
			str.append(anneeDebut);
			str.append(" and DESTINATION != ORIGINE");
			str.append(" and (ORIGINE, DESTINATION) in (");
			str.append("	select zone_etude, zone_echange");
			str.append(" 	from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init)");
			str.append(" and ORIGINE in ");
			str.append(zonesEtude);
			str.append(" group by ORIGINE, DESTINATION");
			str.append(" order by ORIGINE, DESTINATION");
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
			String prefixe,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ORIGINE, DESTINATION, nvl(sum(FLUX),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_top_flux");
			str.append(" where DESTINATION != ORIGINE");
			str.append(" and (ORIGINE, DESTINATION) in (");
			str.append("	select zone_etude, zone_echange");
			str.append(" 	from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init)");
			str.append(" and ORIGINE in ");
			str.append(zonesEtude);
			str.append(" group by ORIGINE, DESTINATION");
			str.append(" order by ORIGINE, DESTINATION");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
