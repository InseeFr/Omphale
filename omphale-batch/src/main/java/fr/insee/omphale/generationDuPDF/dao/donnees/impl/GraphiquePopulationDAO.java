package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePopulationDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class GraphiquePopulationDAO extends GenericDAO<String, Integer> implements
		IGraphiquePopulationDAO {

	
	
	// renvoie List<Object[]>
	// ex.
	// List<Object[]> list = getPopulation(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone
	// liste.get(0)[1] --> annee
	// liste.get(0)[2] --> population
	@SuppressWarnings("unchecked")
	public List<Object[]> getPopulation(
			String idUser, 
			String prefixe, 
			String calageSuffixe, 
			Integer age100)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select zone, annee, nvl(sum(population),0) population");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_population_");
			str.append(calageSuffixe);
			str.append(" where age <= ");
			str.append(age100);
			str.append(" group by zone, annee");
			str.append(" order by zone, annee");
			return session.createSQLQuery(str.toString()).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
		
	}
}




