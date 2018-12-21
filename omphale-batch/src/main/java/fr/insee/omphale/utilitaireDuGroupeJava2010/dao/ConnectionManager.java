package fr.insee.omphale.utilitaireDuGroupeJava2010.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;

import fr.insee.config.InseeConfig;
import fr.insee.config.exception.PoolException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.ConnectionException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.ContexteApplicationException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaBatchSQLException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaTransactionException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.util.IConnectionManager;

/**
 * Classe de gestion des connexions vers une base de données.
 * 
 */

public class ConnectionManager implements IConnectionManager {

	/**
	 * la Map des Connexions utilisées par l'application. Leur référence est
	 * conservée dans une Map pour gérer les transactions de base de données.
	 */
	Map<String, Connection> cacheConnections;
	/**
	 * la Map des cacheBatchManager utilisées par l'application.
	 */
	Map<String, BatchOrderedManager> cacheBatchManager;

	/** Le nombre total de connexions réalisées. */
	private int totalConnectionCount = 0;
	/** Le nombre de connexions simultannées courantes en mémoire. */
	private int currentConnectionCount = 0;
	/** Le nombre maximum de connexions atteintes à un moment donné. */
	private int maxConnectionCount = 0;

	/**
	 * Constructeur. Initialisation de la Map des connexions de base de données.
	 */
	public ConnectionManager() {
		cacheConnections = Collections
				.synchronizedMap(new HashMap<String, Connection>());
		cacheBatchManager = Collections
				.synchronizedMap(new HashMap<String, BatchOrderedManager>());
	}

	/**
	 * Accesseur sur le nombre total de connexions réalisées.
	 * 
	 * @return Le nombre total de connexions réalisées
	 */
	public int getTotalConnectionCount() {
		return (totalConnectionCount);
	}

	/**
	 * Accesseur sur le nombre de connexions simultannées courantes en mémoire.
	 * 
	 * @return Le nombre de connexions simultannées courantes en mémoire.
	 */
	public int getCurrentConnectionCount() {
		return (currentConnectionCount);
	}

	/**
	 * Accesseur sur le nombre maximum de connexions atteintes à un moment donné.
	 * 
	 * @return Le nombre maximum de connexions atteintes à un moment donné.
	 */
	public int getMaxConnectionCount() {
		return (maxConnectionCount);
	}

	/**
	 * Renvoie la DataSource correspondante.
	 * 
	 * @param nomDataSource
	 *            le nom de la DataSource recherchée.
	 * @return la DataSource trouvée.
	 * @throws ConnectionException
	 *             en cas d'impossibilité de trouver la DataSource souhaitée.
	 */
	public DataSource getDataSource(String nomDataSource)
			throws ConnectionException {
		DataSource ds = null;
		try {
			ds = InseeConfig.getPool(nomDataSource);
		} catch (PoolException e) {
			throw new ConnectionException(
					"Impossible d'accéder à la data source " + nomDataSource);
		}
		return ds;
	}

	/**
	 * Teste s'il est possible d'allouer une nouvelle connexion pour cette
	 * DataSource.
	 * 
	 * @param nomConnexion
	 *            le nom de la connexion recherchée.
	 * @return true si la connexion est possible, false sinon
	 * @throws ConnectionException
	 *             en cas d'impossibilité de trouver la connexion souhaitée.
	 */
	private boolean isConnexionDisponible(String nomConnexion)
			throws ConnectionException {
		BasicDataSource ds = (BasicDataSource) getDataSource(nomConnexion);
		if (ds.getMaxActive() > ds.getNumActive())
			return true;
		else
			return false;
	}

	/**
	 * Retourne une nouvelle connexion de base de données pour la data source.
	 * 
	 * @param nomConnexion
	 *            le nom de la connexion.
	 * @return la connexion à la base de données.
	 * @throws ConnectionException
	 *             en cas de pb de connexion à la base de données.
	 */
	private Connection getNouvelleConnection(String nomConnexion)
			throws ConnectionException {
		DataSource ds = getDataSource(nomConnexion);
		Connection c = null;
		try {
			synchronized (this) {
				if (isConnexionDisponible(nomConnexion)) {
					c = ds.getConnection();
					totalConnectionCount++;
					currentConnectionCount++;
					if (currentConnectionCount > maxConnectionCount) {
						maxConnectionCount = currentConnectionCount;
					}
				} else {
					throw new ConnectionException(
							"Nouvelle connexion impossible pour cette base "
									+ nomConnexion
									+ ". Le nombre maximum de connexion est atteint.");
				}
			}
		} catch (SQLException e) {
			throw new ConnectionException("Impossible de trouver la connexion "
					+ nomConnexion, e);
		}
		return c;
	}


	public void beginTransaction(String nomConnexion)
			throws ConnectionException {
		if (!cacheConnections.containsKey(nomConnexion)) {
			Connection connexionCible = getNouvelleConnection(nomConnexion);
			try {
				connexionCible.setAutoCommit(false);
			} catch (SQLException e) {
				throw new ConnectionException(
						"Impossible de positionner le mode Autocommit à false sur la connexion "
								+ nomConnexion, e);
			}
			cacheConnections.put(nomConnexion, connexionCible);
		}
	}


	public void commitTransaction(String nomConnexion)
			throws ContexteApplicationException, GroupeJavaTransactionException {
		Connection connexion = null;
		if (cacheConnections.containsKey(nomConnexion)) {
			synchronized (this) {
				connexion = cacheConnections.get(nomConnexion);
				try {
					if (cacheBatchManager.containsKey(nomConnexion)) {
						synchronized (this) {
							cacheBatchManager.get(nomConnexion).close();
							cacheBatchManager.remove(nomConnexion);
						}
					}
					connexion.commit();
					connexion.close();
					cacheConnections.remove(nomConnexion);
					currentConnectionCount--;
				} catch (SQLException e) {
					throw new GroupeJavaTransactionException(
							"Impossible de valider la transaction sur la connexion "
									+ nomConnexion, e);
				} catch (GroupeJavaBatchSQLException e) {
					throw new GroupeJavaTransactionException(
							"Impossible de valider la transaction sur la connexion "
									+ nomConnexion, e);
				}
			}
		} else {
			throw new ContexteApplicationException(
					"Impossible de trouver la définition de la connexion "
							+ nomConnexion);
		}
	}


	public void rollbackTransaction(String nomConnexion)
			throws ContexteApplicationException, GroupeJavaTransactionException {
		Connection connexion = null;
		if (cacheConnections.containsKey(nomConnexion)) {
			synchronized (this) {
				connexion = cacheConnections.get(nomConnexion);
				try {
					if (cacheBatchManager.containsKey(nomConnexion)) {
						synchronized (this) {
							cacheBatchManager.remove(nomConnexion);
						}
					}
					connexion.rollback();
					connexion.close();
					cacheConnections.remove(nomConnexion);
					currentConnectionCount--;
				} catch (SQLException e) {
					throw new GroupeJavaTransactionException(
							"Impossible d'annuler la transaction sur la connexion "
									+ nomConnexion, e);
				}
			}
		} else {
			throw new ContexteApplicationException(
					"Impossible de trouver la définition de la connexion "
							+ nomConnexion);
		}
	}


	public void postTransaction(String nomConnexion)
			throws ContexteApplicationException, GroupeJavaTransactionException {
		Connection connexion = null;
		if (cacheConnections.containsKey(nomConnexion)) {
			synchronized (this) {
				connexion = cacheConnections.get(nomConnexion);
				try {
					if (cacheBatchManager.containsKey(nomConnexion)) {
						synchronized (this) {
							if (cacheBatchManager.get(nomConnexion).post()) {
								connexion.commit();
							}
						}
					}
				} catch (SQLException e) {
					throw new GroupeJavaTransactionException(
							"Impossible de poster la transaction sur la connexion "
									+ nomConnexion, e);
				} catch (GroupeJavaBatchSQLException e) {
					throw new GroupeJavaTransactionException(
							"Impossible de poster la transaction sur la connexion "
									+ nomConnexion, e);
				}
			}
		} else {
			throw new ContexteApplicationException(
					"Impossible de trouver la définition de la connexion "
							+ nomConnexion);
		}
	}


	public Connection getConnection(String nomConnexion)
			throws ConnectionException {
		if (cacheConnections.containsKey(nomConnexion)) {
			return cacheConnections.get(nomConnexion);
		} else {
			return getNouvelleConnection(nomConnexion);
		}
	}


	public void releaseConnection(Connection c, String nomConnexion)
			throws SQLException {
		// Si la nomConnexion est contenue dans cacheConnections, il s'agit
		// d'une transaction en cours, donc on ne la libère pas. Il faut
		// dans ce cas utiliser CommitTransaction ou RollbackTransaction.
		if (!cacheConnections.containsKey(nomConnexion)) {
			synchronized (this) {
				c.close();
				currentConnectionCount--;
			}
		}
	}


	public BatchOrderedManager createBatchManager(String nomConnexion)
			throws ContexteApplicationException {
		if (cacheConnections.containsKey(nomConnexion)) {
			if (!cacheBatchManager.containsKey(nomConnexion)) {
				BatchOrderedManager batchManager = new BatchOrderedManager();

				cacheBatchManager.put(nomConnexion, batchManager);
			}
			return cacheBatchManager.get(nomConnexion);

		} else {
			throw new ContexteApplicationException(
					"Impossible de trouver la définition de la connexion "
							+ nomConnexion
							+ ". Création de BatchManager impossible.");
		}

	}

}
