package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePointFluxDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class GraphiquePointFluxDAO extends GenericDAO<String, Integer> implements
		IGraphiquePointFluxDAO {

	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getPointFluxSortant(.. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> zone d'échange
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> sexe
	// liste.get(0)[4] --> flux sortant
	@SuppressWarnings("unchecked")
	public List<Object[]> getPointFluxSortant(
			String idUser, 
			String prefixe, 
			String anneeDebut,
			Integer age100,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ORIGINE, DESTINATION, AGE, SEXE, nvl(FLUX,0)");
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
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
		
	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getPointFluxEntr(.. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> zone d'échange
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> sexe
	// liste.get(0)[4] --> flux entr
	@SuppressWarnings("unchecked")
	public List<Object[]> getPointFluxEntr(
			String idUser, 
			String prefixe, 
			String anneeDebut,
			Integer age100,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select DESTINATION, ORIGINE, AGE, SEXE, nvl(FLUX,0)");
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
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		
	}
}
