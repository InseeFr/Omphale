package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.ITableau1PopulationTableauDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class Tableau1PopulationTableauDAO extends GenericDAO<String, Integer> implements
		ITableau1PopulationTableauDAO {
	

	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getPopulation(.. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone d'étude
	// liste.get(0)[1] --> population
	@SuppressWarnings("unchecked")
	public List<Object[]> getPopulation(
			String idUser, 
			String prefixe, 
			String calageSuffixe, 
			String annee)   {

		Session session = getSession();
		try {
			StringBuffer str = new StringBuffer();
			str.append("select zone, nvl(sum(population),0)");
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
	
	@Override
	public int compterPopulationNegativeCal(String idUser, String prefixe) {

		Session session = getSession();
		
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select count(*)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_CSV_POPULATION_CAL");
			str.append(" where ");
			str.append(" ROUND(POPULATION)<0");
			
			return ((BigDecimal) session.createSQLQuery(str.toString()).list().get(0)).intValue();
					

		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages.getString("error.dao.001"),he);
		}
	
	}

	@Override
	public int compterPopulationNegativeNcal(String idUser, String prefixe) {
Session session = getSession();
		
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select count(*)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_CSV_POPULATION_NCAL");
			str.append(" where ");
			str.append(" ROUND(POPULATION)<0");
			
			return ((BigDecimal) session.createSQLQuery(str.toString()).list().get(0)).intValue();
					

		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages.getString("error.dao.001"),he);
		}
	}

	@Override
	public int compterPopulationNegativeMen(String idUser, String prefixe) {
Session session = getSession();
		
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select count(*)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_CSV_POPULATION_MEN");
			str.append(" where ");
			str.append(" ROUND(MENAGES)<0");
			
			return ((BigDecimal) session.createSQLQuery(str.toString()).list().get(0)).intValue();
					

		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages.getString("error.dao.001"),he);
		}
	}

	@Override
	public int compterPopulationNegativeAct(String idUser, String prefixe) {
		Session session = getSession();
		
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select count(*)");
			str.append(" from ");
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_CSV_POPULATION_ACT");
			str.append(" where ");
			str.append(" ROUND(ACTIFS)<0");
			
			return ((BigDecimal) session.createSQLQuery(str.toString()).list().get(0)).intValue();
					

		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages.getString("error.dao.001"),he);
		}
	}
	
	@Override
	public int isPopulationAct(String idUser, String prefixe) {
		Session session = getSession();
		
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select count(*)");
			str.append(" from ");
			str.append("USER_TABLES");
			str.append(" where ");
			str.append(" table_name = '");			
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_CSV_POPULATION_ACT'");
						
			return ((BigDecimal) session.createSQLQuery(str.toString()).list().get(0)).intValue();
				

		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages.getString("error.dao.001"),he);
		}
		
	}
	
	@Override
	public int isPopulationMen(String idUser, String prefixe) {
		Session session = getSession();
		
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select count(*)");
			str.append(" from ");
			str.append("USER_TABLES");
			str.append(" where ");
			str.append(" table_name = '");			
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_CSV_POPULATION_MEN'");
						
			return ((BigDecimal) session.createSQLQuery(str.toString()).list().get(0)).intValue();
				

		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages.getString("error.dao.001"),he);
		}
		
	}
	
	@Override
	public int isPopulationCal(String idUser, String prefixe) {
		Session session = getSession();
		
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select count(*)");
			str.append(" from ");
			str.append("USER_TABLES");
			str.append(" where ");
			str.append(" table_name = '");			
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_CSV_POPULATION_CAL'");
						
			return ((BigDecimal) session.createSQLQuery(str.toString()).list().get(0)).intValue();
				

		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages.getString("error.dao.001"),he);
		}
		
	}
	
	@Override
	public int isPopulationNcal(String idUser, String prefixe) {
		Session session = getSession();
		
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select count(*)");
			str.append(" from ");
			str.append("USER_TABLES");
			str.append(" where ");
			str.append(" table_name = '");			
			str.append(prefixe);
			str.append("_");
			str.append(idUser);
			str.append("_CSV_POPULATION_NCAL'");
						
			return ((BigDecimal) session.createSQLQuery(str.toString()).list().get(0)).intValue();
				

		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages.getString("error.dao.001"),he);
		}
		
	}

}




