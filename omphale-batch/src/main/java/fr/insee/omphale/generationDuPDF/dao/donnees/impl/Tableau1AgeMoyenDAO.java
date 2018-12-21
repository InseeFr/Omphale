package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1AgeMoyenDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class Tableau1AgeMoyenDAO extends GenericDAO<String, Integer> implements
		ITableau1AgeMoyenDAO {

	
	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getAgeMoyen(.. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> age moyen
	@SuppressWarnings("unchecked")
	public List<Object[]> getAgeMoyen(
			String idUser, 
			String prefixe, 
			String calageSuffixe, 
			String annee)  {
		Session session = getSession();
		try {
			StringBuffer str = new StringBuffer();
			str.append("select zone, nvl(decode( sum(population), 0, 0, sum(age * population) / sum(population)),0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_population_");
			str.append(calageSuffixe);
			str.append(" where annee = ");
			str.append(annee);
			str.append(" group by zone");
			str.append(" order by zone");
			return session.createSQLQuery(str.toString()).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	
}




