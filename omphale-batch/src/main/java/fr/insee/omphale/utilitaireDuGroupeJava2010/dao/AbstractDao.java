package fr.insee.omphale.utilitaireDuGroupeJava2010.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.jdbc.driver.OracleConnection;

import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.ConnectionException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.ContexteApplicationException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaSQLException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.IConnectionManager;

/**
 * Classe abstraite définissant un objet d'accès aux données. <br>
 * Cette classe définit un certain nombre de méthodes permettant une
 * manipulation plus facile des données contenues dans la base de données.
 * 
 */
public class AbstractDao {

	/** Le nom du pool de connexion à la base de données */
	private String nomPool;

	/** La connexion vers la base de données. */
	protected Connection c = null;

	/** Le statement sur cette connexion. */
	protected Statement s = null;

	/** Le resultset associé au statement. */
	protected ResultSet r = null;

	/**
	 * Le connectionManager gérant les connexions et les transactions à la base
	 * de données
	 */
	protected IConnectionManager connectionManager;

	/**
	 * Constructeur.
	 * 
	 * @param nomPool
	 *            Le nom du pool de connexion à la base de données
	 * @param connectionManager
	 *            le connectionManager gérant les connexions vers la base de
	 *            données.
	 */
	public AbstractDao(String nomPool, IConnectionManager connectionManager) {
		super();
		this.nomPool = nomPool;
		this.connectionManager = connectionManager;
	}

	/**
	 * Etape d'initialisation du DAO. <br>
	 * Cette méthode est vide par défaut, elle peut éventuellement être
	 * redéfinie dans les classes filles.
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur d'initialisation du DAO
	 */
	public void initDAO() throws GroupeJavaDaoException {
	}


	/**
	 * Instancie la connexion vers le serveur de données.
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur de recherche de la connexion à la base
	 */
	protected void connection() throws GroupeJavaDaoException {
		if (c == null) {
			try {
				c = connectionManager.getConnection(nomPool);
				if (c instanceof OracleConnection) {
					((OracleConnection) c).getDefaultRowPrefetch();

				}
			} catch (ContexteApplicationException e) {
				final String message = "Erreur d'accès au contexte de l'application "
						+ nomPool;
				throw new GroupeJavaDaoException(message, e);
			} catch (ConnectionException e) {
				final String message = "Erreur d'accès au pool de connexion "
						+ nomPool;
				throw new GroupeJavaDaoException(message, e);
			}
		}
	}

	/**
	 * Execute une requete basique sur le statement courant.
	 * 
	 * @param sqlQuery
	 *            la requete SQL à executer
	 * @return le resultset résultat de la requete
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur lors de la recherche de la connexion
	 * @throws GroupeJavaSQLException
	 *             en cas d'erreur lors de l'execution de la requete
	 */
	protected ResultSet executeQuery(String sqlQuery)
			throws GroupeJavaDaoException {
		connection();
		try {
			s = c.createStatement();
			r = s.executeQuery(sqlQuery);
		} catch (SQLException e) {
			final String message = "Erreur dans le traitement de la requete SQL";
			throw new GroupeJavaSQLException(message, e);
		}
		return r;
	}

	/**
	 * Execute une requete de type update/insert basique sur le statement
	 * courant.
	 * 
	 * @param sqlQuery
	 *            la requete SQL à executer
	 * @return le nombre de lignes impactées, <code>-1</code> si aucune ligne
	 *         n'est affectée
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur lors de la recherche de la connexion
	 */
	public int executeUpdate(String sqlQuery) throws GroupeJavaDaoException {
		int affectedRows = -1;
		connection();
		try {
			s = c.createStatement();
			affectedRows = s.executeUpdate(sqlQuery);
		} catch (SQLException e) {
			final String message = "Erreur dans le traitement de la requete SQL";
			throw new GroupeJavaSQLException(message, e);
		}
		return affectedRows;
	}

	/**
	 * Ferme la connexion et les objets liés. <br>
	 * Cette méthode ferme de façon <code>quiet</code> la connexion et les
	 * objets associés (ResultSet et Statement).
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur lors de la liberation des objets d'accès à la
	 *             base de données.
	 */
	protected void close() throws GroupeJavaDaoException {
		try {
			if (r != null) {
				r.close();
				r = null;
			}
			if (s != null) {
				s.close();
				s = null;
			}
		} catch (SQLException e) {
			final String message = "impossible de fermer un objet vers la base de données";
			throw new GroupeJavaSQLException(message, e);
		}
		try {
			if (c != null) {
				connectionManager.releaseConnection(c, nomPool);
			}
		} catch (SQLException e) {
			final String message = "impossible de terminer la connexion";
			throw new GroupeJavaSQLException(message, e);
		}

		c = null;
	}

	public String getNomPool() {
		return nomPool;
	}

	public void setNomPool(String nomPool) {
		this.nomPool = nomPool;
	}
}
