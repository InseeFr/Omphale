package fr.insee.omphale.utilitaireDuGroupeJava2010.dao;

import java.sql.SQLException;
import java.util.Collection;

import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaSQLException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.IConnectionManager;

public class SimpleDao extends AbstractDao implements ISimpleDao {


	public SimpleDao(String nomPool, IConnectionManager connectionManager) {
		super(nomPool, connectionManager);
	}

	
	public void executeSQL(Collection<String> ordresSQL)
			throws GroupeJavaDaoException {
		connection();
		try {
			s = c.createStatement();
		} catch (SQLException e) {
			final String message = "Création du statement impossible";
			throw new GroupeJavaSQLException(message, e);
		}
		try {
			for (String ordreSQL : ordresSQL) {
				try {
					// Pour chaque ordreSQL de la collection, Executer à travers
					// JDBC
					s.execute(ordreSQL);
				} catch (SQLException e) {
					if ((!ordreSQL.startsWith("drop"))
							&& (!ordreSQL.startsWith("DROP"))) {
						throw e;
					}
				}
			}
		} catch (SQLException e) {
			final String message = "Erreur d'execution d'un script SQL";
			throw new GroupeJavaSQLException(message, e);
		} finally {
			close();
		}
	}
}
