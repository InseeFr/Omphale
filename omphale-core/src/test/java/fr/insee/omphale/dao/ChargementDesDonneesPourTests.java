package fr.insee.omphale.dao;

import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.operation.DatabaseOperation;
import org.xml.sax.InputSource;

import fr.insee.config.InseeConfig;

public class ChargementDesDonneesPourTests {

	private static String nomDuPool = "hsqldb";
	private static Connection connection;
	private static IDatabaseConnection myDbConnection;
	private static IDataSet mySetUpDataset;

	/**
	 * Méthode Static d'initialisation de la connexion dbUnit et de la
	 * récupération des données. Les données doivent ensuite être chargées. A
	 * Appeler dans le @ BeforeClass.
	 */
	public static void initDonneesDbUnit(String cheminDuFichier) {

		try {
			connection = InseeConfig.getConnection(nomDuPool);
			myDbConnection = new DatabaseConnection(connection);

			FlatXmlProducer producer = new FlatXmlProducer(new InputSource(
					cheminDuFichier));
			mySetUpDataset = new FlatXmlDataSet(producer);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Méthode static de chargement de la base. */
	public static void chargerDonneesDbUnit() throws Exception {
		DatabaseOperation.CLEAN_INSERT.execute(myDbConnection, mySetUpDataset);
	}

	/**
	 * Méthode Static de fermeture de la connection dbUnit. Les données doivent
	 * ensuite être chargées. A appeler dans le @ AfterClass
	 */
	public static void close() throws SQLException {

		connection.close();

	}

}
