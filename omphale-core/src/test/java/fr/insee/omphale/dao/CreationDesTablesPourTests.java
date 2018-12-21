package fr.insee.omphale.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import fr.insee.config.InseeConfig;
import fr.insee.config.exception.PoolException;

public class CreationDesTablesPourTests {


	private static String nomDuPool = "hsqldb";
	private static Connection connection = null;
	private static boolean tableCrees = false;
	private static StringBuffer create = new StringBuffer();

	public static void creerLesTables() throws SQLException {
		if (!tableCrees) {
			try {
				connection = InseeConfig.getConnection(nomDuPool);
				supprimerLesTables();
				creeLesTables();
				tableCrees = true;
			} catch (PoolException e) {
			} finally {
				if (connection != null) {
						connection.close();
				}
			}
		} 

	}



	private static void supprimerLesTables() {
		try {
			executeScrit("DROP TABLE EVOL_NON_LOC IF EXISTS ");
			executeScrit("DROP TABLE COMPOSANTE IF EXISTS ");
			executeScrit("DROP TABLE EVOL_DE_SCENAR IF EXISTS ");
			executeScrit("DROP TABLE USER_OMPHALE IF EXISTS ");
			executeScrit("DROP TABLE USER_HYPOTHESE IF EXISTS ");
			executeScrit("DROP TABLE USER_OMPHALE IF EXISTS ");
			executeScrit("DROP TABLE TYPE_ENTITE IF EXISTS ");
			executeScrit("DROP TABLE USER_HYPOTHESE IF EXISTS ");
			executeScrit("DROP TABLE METHODE_EVOL IF EXISTS ");
			executeScrit("DROP TABLE PARAM_METH_EVOL IF EXISTS ");
			executeScrit("DROP TABLE DEF_PROJECTION IF EXISTS ");
			executeScrit("DROP TABLE USER_POPULATION IF EXISTS ");
			executeScrit("DROP TABLE CB_POPULATION IF EXISTS ");
			executeScrit("DROP TABLE USER_OMPHALE IF EXISTS ");
			executeScrit("DROP TABLE DEF_PROJECTION IF EXISTS ");
			executeScrit("DROP TABLE PROJECTION_LANCEE IF EXISTS ");
			executeScrit("DROP TABLE ZONAGE IF EXISTS ");
			executeScrit("DROP TABLE ZONE IF EXISTS ");
			executeScrit("DROP TABLE ZONE_DE_ZONAGE IF EXISTS ");
			executeScrit("DROP TABLE SCENAR_NON_LOC IF EXISTS ");
			executeScrit("DROP TABLE ZONE_DE_GROUPET IF EXISTS ");

		} catch (SQLException e) {
		}

	}

	public static void supprimerSchema() throws SQLException {
		String drop = "DROP SCHEMA IF EXISTS PUBLIC CASCADE ";
		executeScrit(drop);
	}

	public static void creeLesTables() throws SQLException {

		create.append(creerTableEVOL_NON_LOC) ;
		create.append(creerTableString) ;
		create.append(creerTableEVOL_DE_SCENAR) ;
		create.append(creerTableUSER_OMPHALE) ;
		create.append(creerTableTYPE_ENTITE) ;
		create.append(creerTableUSER_HYPOTHESE) ;
		create.append(creerTableMETHODE_EVOL) ;
		create.append(creerTablePARAM_METH_EVOL) ;
		create.append(creerTableUSER_POPULATION)  ;
		create.append(creerTableCB_POPULATION) ;
		create.append(creerTableDEF_PROJECTION)  ;
		create.append(creerTablePROJECTION_LANCEE);
		create.append(creerTableZONE_DE_ZONAGE);
		create.append(creerTableZONE);
		create.append(creerTableZONAGE);
		create.append(creerTableScenario);
		create.append(creerTableZONE_DE_GROUPET);


		executeScrit(create.toString());
	}

	// Tables en C_
	

	
	private static String creerTableEVOL_NON_LOC = "CREATE TABLE EVOL_NON_LOC(ID_EVOL_NON_LOC INTEGER,ID_USER CHAR(6),NOM CHAR(20),TYPE_ENTITE CHAR(5),COMPOSANTE CHAR(5),ID_HYPOTHESE INTEGER,METHODE_EVOL CHAR(15),AGE_DEB INTEGER,SEXE_DEB INTEGER,ANNEE_DEB INTEGER,AGE_FIN INTEGER,SEXE_FIN INTEGER,ANNEE_FIN INTEGER,TYPE_PARAM1 CHAR(10),TYPE_PARAM2 CHAR(10),TYPE_PARAM3 CHAR(10),TYPE_PARAM4 CHAR(10),VAL_PARAM1 INTEGER,VAL_PARAM2 INTEGER,VAL_PARAM3 INTEGER,VAL_PARAM4 INTEGER,STANDARD INTEGER,DATE_CREATION DATE,COMMENTAIRE CHAR(100))";
	private static String creerTableString = "CREATE TABLE COMPOSANTE(COMPOSANTE CHAR(5),LIBELLE CHAR(50))";
	private static String creerTableEVOL_DE_SCENAR = "CREATE TABLE EVOL_DE_SCENAR(ID_SCENARIO INTEGER,ID_EVOL_NON_LOC INTEGER,RANG INTEGER)";
	private static String creerTableUSER_OMPHALE = "CREATE TABLE USER_OMPHALE(LIBELLE CHAR(50),EMAIL CHAR(50),IDEP CHAR(6),ROLE INTEGER,ID_USER CHAR(6))";
	private static String creerTableTYPE_ENTITE = "CREATE TABLE TYPE_ENTITE(TYPE_ENTITE CHAR(5),DIMENSION_AGE INTEGER,DIMENSION_SEXE INTEGER,RESTRICTION_AGE INTEGER,VAL_MAX INTEGER,VAL_MIN INTEGER,CTRL_EXHAUST INTEGER,LIBELLE CHAR(50))";
	private static String creerTableUSER_HYPOTHESE = "CREATE TABLE USER_HYPOTHESE(ID_HYPOTHESE INTEGER,ID_USER CHAR(6),NOM CHAR(20),TYPE_ENTITE CHAR(5),AGE_DEB INTEGER,SEXE_DEB INTEGER,ANNEE_DEB INTEGER,AGE_FIN INTEGER,SEXE_FIN INTEGER,ANNEE_FIN INTEGER,STANDARD INTEGER,DATE_CREATION DATE,VALIDATION INTEGER,LIBELLE CHAR(50))";
	private static String creerTableMETHODE_EVOL = "CREATE TABLE METHODE_EVOL(METHODE_EVOL CHAR(15),COMPOSANTE CHAR(5),TYPE_ENTITE CHAR(5),HYPOTHESE_REQUISE INTEGER,LIBELLE CHAR(50))";
	private static String creerTablePARAM_METH_EVOL = "CREATE TABLE PARAM_METH_EVOL(METHODE_EVOL CHAR(15),TYPE_PARAM CHAR(10),RANG INTEGER,VAL_DEF INTEGER,LIBELLE CHAR(50))";
	private static String creerTableUSER_POPULATION = "CREATE TABLE USER_POPULATION(PROJECTION INTEGER,SURVIVANT INTEGER,ZONAGE CHAR(5),DATE_CREATION DATE)" ;
	private static String creerTableCB_POPULATION = "CREATE TABLE CB_POPULATION(PROJECTION INTEGER,SURVIVANT INTEGER,AGE INTEGER,SEXE INTEGER,ANNEE INTEGER,ZONE VARCHAR(5),VALEUR INTEGER)";
	private static String creerTableDEF_PROJECTION = "CREATE TABLE DEF_PROJECTION(ID_PROJECTION INTEGER NOT NULL,ID_SCENARIO INTEGER,ID_ZONAGE CHAR(5),ANNEE_REFERENCE INTEGER,ANNEE_HORIZON INTEGER,ID_USER CHAR(6),NOM CHAR(20),STANDARD INTEGER,PYRAMIDE_GENERATION INTEGER,VALIDATION INTEGER,CALAGE INTEGER,ENGLOBANTE INTEGER,ACTIFS INTEGER,MENAGES INTEGER,ELEVES INTEGER,ID_PROJ_ETALON INTEGER,ID_PROJ_ENGLOBANTE INTEGER,DATE_CREATION DATE,LIBELLE CHAR(50))" ;
	private static String creerTablePROJECTION_LANCEE = "CREATE TABLE PROJECTION_LANCEE(ID_PROJECTION_LANCEE INTEGER,ID_PROJECTION INTEGER,DATE_LANCEMENT DATE,DATE_EXEC DATE,DONNEES INTEGER,DATE_DEBUT_EXEC DATE,CODE_RETOUR CHAR(5),MESSAGE CHAR(200),NBR_ZONE INTEGER)";
	private static String creerTableZONAGE = "CREATE TABLE ZONAGE(ID_ZONAGE CHAR(5), ID_USER   CHAR(6), NOM CHAR(20), ANNEE_VALIDATION INTEGER, ETAT_VALIDATION INTEGER, LIBELLE CHAR(100), DATE_CREATION DATE)";
	private static String creerTableZONE_DE_ZONAGE = "CREATE TABLE ZONE_DE_ZONAGE(ZONAGE CHAR(5),   ZONE CHAR(5) )";
	private static String creerTableZONE = "CREATE TABLE ZONE(ID_ZONE CHAR(5),   ID_USER   CHAR(6),  NOM CHAR(20),     STANDARD  INTEGER, TYPE_ZONE_STANDARD INTEGER, LIBELLE  CHAR(100) )";
	private static String creerTableScenario  = "CREATE TABLE SCENAR_NON_LOC (ID_SCENARIO INTEGER, ID_USER CHAR(6),  NOM CHAR(20),  STANDARD INTEGER, VALIDATION INTEGER, DATE_CREATION DATE, LIBELLE CHAR(100)  )";
	private static String creerTableZONE_DE_GROUPET  = "CREATE TABLE ZONE_DE_GROUPET(ZONAGE CHAR(5), SIGNATURE CHAR(20),ZONE  CHAR(5) )";
	
	
	
	
	private static void executeScrit(String script) throws SQLException {
		Statement st = null;
		try {
			st = connection.createStatement();
			st.execute(script);
			st.close();
			connection.commit();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (st != null) {
				st.close();
			}
		}

	}

}
