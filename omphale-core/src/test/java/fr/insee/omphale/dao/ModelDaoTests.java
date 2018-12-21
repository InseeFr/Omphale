package fr.insee.omphale.dao;

import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public class ModelDaoTests {


	/**
	 * Cette méthode recrée l'ensemble des tables dans la base hsqldb, et ce à
	 * chaque début de campagne de tests. Les tables sont créées, mais vides.
	 */
	@BeforeClass
	public static void initBasesDeDonnes() {
		try {
			CreationDesTablesPourTests.creerLesTables();
		} catch (Exception e) {
		}
	}

	/**
	 * Cette méthode, lancée avant chaque test unitaire, charge les données dans
	 * la base et ouvre une session hibernate.
	 */
	@Before
	public void initialiseHibernateEtDBUnit() throws Exception {
		try {
			ChargementDesDonneesPourTests.chargerDonneesDbUnit();
			ouvreSession();
		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Cette méthode, lancée après chaque test unitaire, effectue un commit sur
	 * la session hibernate.
	 */
	@After
	public void commitSessionEtTransactionSiActive() {
		PersistancePourTests.commit();
	}

	/**
	 * Cette méthode, lancée après chaque campagne de test, ferme la connexion à
	 * la base de données hsqldb
	 */
	@AfterClass
	public static void closeConnection() throws SQLException {
		ChargementDesDonneesPourTests.close();
	}

	/**
	 * Cette méthode effectue la mise à jour de la base de données, sans fermer
	 * la connexion à la base hsqldb. Il s'agit du flush hibernate. Elle doit
	 * être appelée au sein des méthodes dans laquelle on veut vérifier une
	 * écriture en base : sans elle, le commit n'aura lieu qu'après le test
	 * unitaire.
	 */
	public void miseAJourDeLaBase() {
		PersistancePourTests.flush();
	}

	public void ouvreSession() {
		PersistancePourTests.initSessionEtTransaction();
	}

	public void fermeSession() {
		PersistancePourTests.shutdown();
	}

	/**
	 * Cette méthode doit être appelée dans la partie @BeforeClass de chaque
	 * classe héritée, afin de selectionner le fichier qui sera chargé dans
	 * initialiseHibernateEtDBUnit().
	 */
	public static void selectionneDonnesACharger(String cheminFichierDonnees) {
		ChargementDesDonneesPourTests.initDonneesDbUnit(cheminFichierDonnees);
	}

}
