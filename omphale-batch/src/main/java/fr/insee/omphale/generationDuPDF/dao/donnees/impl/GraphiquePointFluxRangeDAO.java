package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePointFluxRangeDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class GraphiquePointFluxRangeDAO extends GenericDAO<String, Integer> implements
		IGraphiquePointFluxRangeDAO {

	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getPointFluxSortantRange(.. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> zone d'échange
	// liste.get(0)[2] --> flux max sortant
	@SuppressWarnings("unchecked")
	public List<Object[]> getPointFluxSortantRange(
			String idUser, 
			String prefixe, 
			String anneeDebut,
			Integer age100,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ORIGINE, DESTINATION, nvl(MAX(FLUX),0) flux_max");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_agrege_top_flux");
			str.append(" where annee_ref = ");
			str.append(anneeDebut);
			str.append(" and age <= ");
			str.append(age100);
			str.append(" and (ORIGINE, DESTINATION) in (");
			str.append("	select zone_etude, zone_echange");
			str.append(" 	from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init)");
			str.append(" and ORIGINE in ");
			str.append(zonesEtude);
			str.append(" group by origine, destination");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	
	
	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getPointFluxEntrRange(.. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> zone d'échange
	// liste.get(0)[2] --> flux max entr
	@SuppressWarnings("unchecked")
	public List<Object[]> getPointFluxEntrRange(
			String idUser, 
			String prefixe, 
			String anneeDebut,
			Integer age100,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select DESTINATION, ORIGINE, nvl(MAX(FLUX),0) flux_max");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_agrege_top_flux");
			str.append(" where annee_ref = ");
			str.append(anneeDebut);
			str.append(" and age <= ");
			str.append(age100);
			str.append(" and (DESTINATION, ORIGINE) in (");
			str.append("	select zone_etude, zone_echange");
			str.append(" 	from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init)");
			str.append(" and DESTINATION in ");
			str.append(zonesEtude);
			str.append(" group by origine, destination");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		
	}
	
	
}
