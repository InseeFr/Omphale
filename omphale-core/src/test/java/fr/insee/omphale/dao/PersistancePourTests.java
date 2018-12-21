package fr.insee.omphale.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import fr.insee.omphale.dao.util.HibernateUtil;



public class PersistancePourTests {
	private static SessionFactory sf;
	public static Session session;  
	public static Transaction transaction;
	
	/**
	 * Initialise la session et renseigne la variable 'session' par la session Hibernate courante
	 */


	static 	{
		HibernateUtil.setFichierConfigHibernate("hibernate/hibernateTest.cfg.xml");
		sf = HibernateUtil.getSessionFactory();
	}
	
	
	
	static public void initSessionEtTransaction() {		
		session = sf.getCurrentSession();
		transaction = session.beginTransaction();
		
	}
	
	static public void flush(){
		session.flush();
	}
	static public void commit() {
		if(transaction!=null && transaction.isActive()){
			transaction.commit();
		}
	}

	static public void shutdown() {
		// Close caches and connection pools
		if (sf!=null){ 
			sf.close();
			sf=null;
		}
	}
}
