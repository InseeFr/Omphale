package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePyramideDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class GraphiquePyramideDAO extends GenericDAO<String, Integer> implements
		IGraphiquePyramideDAO {

	
	// renvoie List<Object[]>
	// ex.
	// List<Object[]> list = findAll(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> annee
	// liste.get(0)[2] --> sexe
	// liste.get(0)[3] --> age
	// liste.get(0)[4] --> population
	@SuppressWarnings("unchecked")
	public List<Object[]> findAll(
			String idUser, 
			String prefixe, 
			String calageSuffixe, 
			String anneeDebut, 
			String anneeDebutPlus5, 
			String anneeFin, 
			Integer age100)  {
		Session session = getSession();
		try {
			StringBuffer str = new StringBuffer();
			str.append("select zone, annee, sexe, age, nvl(population,0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_population_");
			str.append(calageSuffixe);
			str.append(" where annee in (");
			str.append(Integer.parseInt(anneeDebut));
			str.append(", ");
			str.append(Integer.parseInt(anneeDebutPlus5));
			str.append(", ");
			str.append(Integer.parseInt(anneeFin));
			str.append(") ");
			str.append(" and age <= ");
			str.append(age100);
			str.append(" order by zone, annee, sexe, age");
			return session.createSQLQuery(str.toString()).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
}




