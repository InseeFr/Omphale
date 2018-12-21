package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiqueDecDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class GraphiqueDecDAO extends GenericDAO<String, Integer> implements
		IGraphiqueDecDAO {

	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getListe(idUser, .. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone
	// liste.get(0)[1] --> annee
	// liste.get(0)[2] --> sexe
	// liste.get(0)[3] --> age
	// liste.get(0)[4] --> qd
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Object[]> getListe(String idUser, String prefixe, String anneeDebut, Integer age100)  {
		Session session = getSession();
		try {
			StringBuffer str = new StringBuffer();
			str.append("select zone, annee, sexe, age, nvl(qd,0) qd");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_qd");
			str.append(" where annee = ");
			str.append(anneeDebut);
			str.append(" and age <= ");
			str.append(age100);
			str.append(" order by zone, sexe, age");
			return (List) session.createSQLQuery(str.toString()).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	

}




