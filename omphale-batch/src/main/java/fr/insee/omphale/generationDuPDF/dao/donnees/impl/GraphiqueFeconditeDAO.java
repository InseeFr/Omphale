package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiqueFeconditeDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class GraphiqueFeconditeDAO extends GenericDAO<String, Integer> implements
		IGraphiqueFeconditeDAO {

	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getListe(idUser, .. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone
	// liste.get(0)[1] --> annee
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> qf
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object[]> getListe(
			String idUser, 
			String prefixe, 
			String anneeDebut, 
			Integer age100)  {
		Session session = getSession();
		try {
			StringBuffer str = new StringBuffer();
			str.append("select zone, annee, age, nvl(qf,0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_qf");
			str.append(" where annee = ");
			str.append(anneeDebut);
			str.append(" and age <= ");
			str.append(age100);
			str.append(" order by zone, annee, age");
			return (List) session.createSQLQuery(str.toString()).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}




