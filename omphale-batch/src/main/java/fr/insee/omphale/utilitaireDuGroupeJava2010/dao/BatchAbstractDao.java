package fr.insee.omphale.utilitaireDuGroupeJava2010.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;

import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.IConnectionManager;

/**
 * Classe abstraite de DAO pour un fonctionnement transactionnel avec la base de
 * données.
 * 
 * @param de classe T
 *            le bean associé à la table gérée par cette classe.
 */
public abstract class BatchAbstractDao<T> extends AbstractDao {

	/** Le prepared Statement qui sert pour l'insertion d'objets. */
	private PreparedStatement preparedStmt = null;

	/**
	 * Nombre d'insertion batch en attente.
	 */
	private int batchPending;

	/**
	 * Constructeur.
	 * 
	 * @param nomPool
	 *            Le nom du pool de connexion à la base de données
	 * @param connectionManager
	 *            le connectionManager gérant les connexions vers la base de
	 *            données.
	 */
	public BatchAbstractDao(String nomPool,
			IConnectionManager connectionManager) {
		super(nomPool, connectionManager);
	}

	/**
	 * Fournit la requete SQL à exécuter pour ce DAO.
	 * 
	 * @return la requete SQL.
	 */
	protected abstract String genereOrdreSQL();

	/**
	 * Affecte les valeurs au prepareStatement.
	 * 
	 * @param objet
	 *            le type de bean associé à la table ORACLE géré par le DAO.
	 * @throws SQLException
	 *             en cas d'erreur.
	 */
	protected abstract void setObjectValeurs(T objet) throws SQLException;

	/**
	 * Execute les insertions batchs en attente.
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur.
	 */
	public void purgeBatchPending() throws GroupeJavaDaoException {
		if (this.batchPending > 0) {
			this.executeBatch();
			this.batchPending = 0;
		}
	}

	/**
	 * Insère une ligne dans la table liée au DAO.
	 * 
	 * @param objet
	 *            le type de bean associé à la table ORACLE géré par le DAO.
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur.
	 */
	public void insereObject(T objet) throws GroupeJavaDaoException {
		if (objet == null) {
			return;
		}
		if (preparedStmt == null) {
			try {
				connection();
				preparedStmt = c.prepareStatement(this.genereOrdreSQL());
			} catch (SQLException e) {
				final String message = "impossible de créer l'ordre SQL de ce DAO";
				throw new GroupeJavaDaoException(message, e);
			}
		}
		try {
			this.setObjectValeurs(objet);
			this.preparedStmt.addBatch();
			this.batchPending++;
		} catch (SQLException e) {
			final String message = "Impossible d'ajouter cet élement au batch";
			throw new GroupeJavaDaoException(message, e);
		}
	}

	// ----------------------------------------------------- Méthodes protégées

	/**
	 * Affecte une valeur Integer à un paramètre jdbc.
	 * 
	 * @param position
	 *            numéro ordinal du paramètre dans le preparedStatement
	 * @param value
	 *            valeur du paramètre
	 * @throws SQLException
	 *             en cas d'erreur dans la mise en place du paramètre
	 */
	protected void setParameterInt(int position, Integer value)
			throws SQLException {
		if (value == null) {
			preparedStmt.setNull(position, Types.INTEGER);
		} else {
			preparedStmt.setObject(position, value);
		}
	}

	/**
	 * Affecte une valeur Long à un paramètre jdbc.
	 * 
	 * @param position
	 *            numéro ordinal du paramètre dans le preparedStatement
	 * @param value
	 *            valeur du paramètre
	 * @throws SQLException
	 *             en cas d'erreur dans la mise en place du paramètre
	 */
	protected void setParameterLong(int position, Long value)
			throws SQLException {
		if (value == null) {
			preparedStmt.setNull(position, Types.NUMERIC);
		} else {
			preparedStmt.setObject(position, value);
		}
	}

	/**
	 * Affecte une valeur Double à un paramètre jdbc.
	 * 
	 * @param position
	 *            numéro ordinal du paramètre dans le preparedStatement
	 * @param value
	 *            valeur du paramètre
	 * @throws SQLException
	 *             en cas d'erreur dans la mise en place du paramètre
	 */
	protected void setParameterDouble(int position, Double value)
			throws SQLException {
		if (value == null) {
			preparedStmt.setNull(position, Types.DOUBLE);
		} else {
			preparedStmt.setObject(position, value);
		}
	}

	/**
	 * Affecte une valeur Date à un paramètre jdbc.
	 * 
	 * @param position
	 *            numéro ordinal du paramètre dans le preparedStatement
	 * @param value
	 *            valeur du paramètre
	 * @throws SQLException
	 *             en cas d'erreur dans la mise en place du paramètre
	 */
	protected void setParameterDate(int position, Date value)
			throws SQLException {
		if (value == null) {
			preparedStmt.setNull(position, Types.DATE);
		} else {
			preparedStmt
					.setObject(position, new java.sql.Date(value.getTime()));
		}
	}

	/**
	 * Affecte une valeur String à un paramètre jdbc.
	 * 
	 * @param position
	 *            numéro ordinal du paramètre dans le preparedStatement
	 * @param value
	 *            valeur du paramètre
	 * @throws SQLException
	 *             en cas d'erreur dans la mise en place du paramètre
	 */
	protected void setParameterString(int position, String value)
			throws SQLException {
		if (value == null) {
			preparedStmt.setNull(position, Types.VARCHAR);
		} else {
			preparedStmt.setObject(position, value);
		}
	}

	/**
	 * Execute le Batch JDBC contenu dans cet objet.
	 * 
	 * @return le nombre de lignes de la base affectées
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur SQL lors de l'execution du batch
	 */
	protected int executeBatch() throws GroupeJavaDaoException {
		int affectedRows = 0;
		try {
			int errorRows = 0;
			if (preparedStmt != null) {
				int[] resultats = preparedStmt.executeBatch();
				for (int resultat : resultats) {
					if (Statement.EXECUTE_FAILED == resultat) {
						errorRows++;
					} else if (resultat != Statement.SUCCESS_NO_INFO) {
						affectedRows += resultat;
					}
				}
				if (errorRows > 0) {
					final String message = Integer.toString(errorRows)
							+ " erreurs dans l'insertion";
					throw new GroupeJavaDaoException(message);
				}
			}
		} catch (SQLException e) {
			final String message = "erreur SQL lors de l'execution du batch";
			throw new GroupeJavaDaoException(message, e);
		} finally {
			try {
				preparedStmt.clearBatch();
			} catch (SQLException e) {
				final String message = "impossible de fermer correctement le prepared statement";
				throw new GroupeJavaDaoException(message, e);
			}
		}
		return affectedRows;
	}

}
