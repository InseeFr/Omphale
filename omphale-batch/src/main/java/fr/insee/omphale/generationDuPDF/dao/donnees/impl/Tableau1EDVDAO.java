package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1EDVDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class Tableau1EDVDAO extends GenericDAO<String, Integer> implements
		ITableau1EDVDAO {

	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getListe(.. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> sexe
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> qd
	@SuppressWarnings("unchecked")
	public List<Object[]> getListe(String idUser, String prefixe, String annee)  {
		Session session = getSession();
		try {
			StringBuffer str = new StringBuffer();
			str.append("select zone, sexe, age, nvl(qd,0)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_csv_qd");
			str.append(" where annee = ");
			str.append(annee);
			str.append(" order by zone, sexe, age");
			return (List<Object[]>) session.createSQLQuery(str.toString()).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages.getString("error.dao.001"), he);
		}
	}
}