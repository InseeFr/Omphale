package fr.insee.omphale.utilitaireDuGroupeJava2010.util;

import java.sql.Connection;
import java.sql.SQLException;

import fr.insee.omphale.utilitaireDuGroupeJava2010.dao.BatchOrderedManager;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.ConnectionException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.ContexteApplicationException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaTransactionException;

/**
 * Interface définissant une source de connexions de base de données.<br>
 * Cette interface permet d'utiliser des connexions base de données sous un
 * environnement batch ou web. La source de connexion doit gérer le cycle de vie
 * des connexions JDBC : <br>
 * <ul>
 * <li>Ouverture de connexion</li>
 * <li>Fermeture de connexion</li>
 * <li>Début de transaction</li>
 * <li>Validation de transaction (commit)</li>
 * <li>Annulation de transaction (rollback)</li>
 * </ul>
 * 
 */
public interface IConnectionManager {
	/**
	 * Renvoie une connexion issue de cette source de données.
	 * 
	 * @param nomConnexion
	 *            nom de la connexion à la base de données.
	 * @return une connexion vers la base de données.
	 * @throws ConnectionException
	 *             en cas d'erreur d'ouverture de la connexion.
	 * @throws ContexteApplicationException
	 *             en cas d'erreur d'utilisation du ContexteApplication du
	 *             groupe java.
	 */
	Connection getConnection(String nomConnexion) throws ConnectionException,
			ContexteApplicationException;

	/**
	 * Libère une connexion et la rend au pool de connexions, sauf si une
	 * transaction est en cours. Si une transaction est en cours, la connexion
	 * n'est pas libérée.
	 * 
	 * @param c
	 *            la connexion à la base de données.
	 * @param nomConnexion
	 *            nom de la connexion à la base de données.
	 * @throws SQLException
	 *             en cas d'erreur d'accès à la base de données.
	 */
	public void releaseConnection(Connection c, String nomConnexion)
			throws SQLException;

	/**
	 * Débute une transaction de base de données sur cette source de données
	 * enregistrée au préalable.
	 * 
	 * @param nomConnexion
	 *            nom de la connexion à la base de données.
	 * @throws ConnectionException
	 *             en cas d'erreur à la connexion à une base de donnée.
	 * @throws ContexteApplicationException
	 * @throws ContexteApplicationException
	 *             en cas d'erreur d'utilisation du ContexteApplication du
	 *             groupe java.
	 * @throws GroupeJavaTransactionException
	 *             en cas d'erreur d'initalisation de la transaction.
	 */
	void beginTransaction(String nomConnexion) throws ConnectionException,
			ContexteApplicationException;

	/**
	 * Valide une transaction de base de données précédemment ouverte sur la
	 * Connection enregistrée au préalable.
	 * 
	 * @param nomConnexion
	 *            nom de la connexion à la base de données.
	 * @throws ContexteApplicationException
	 *             en cas d'erreur d'utilisation du ContexteApplication du
	 *             groupe java.
	 * @throws GroupeJavaTransactionException
	 *             en cas d'erreur d'annulation de transaction.
	 */
	void commitTransaction(String nomConnexion)
			throws GroupeJavaTransactionException, ContexteApplicationException;

	/**
	 * Annule une transaction de base de données précédemment ouverte sur la
	 * connexion enregistrée au préalable.
	 * 
	 * @param nomConnexion
	 *            nom de la connexion à la base de données.
	 * @throws ContexteApplicationException
	 *             en cas d'erreur d'utilisation du ContexteApplication du
	 *             groupe java.
	 * @throws GroupeJavaTransactionException
	 *             en cas d'erreur d'annulation de transaction.
	 */
	void rollbackTransaction(String nomConnexion) throws ConnectionException,
			ContexteApplicationException, GroupeJavaTransactionException;

	/**
	 * Poste une ligne dans la transaction. Si le nombre de lignes postées
	 * dépasse la taille de la transaction définie dans le
	 * {@link fr.insee.omphale.utilitaireDuGroupeJava2010.dao.BatchOrderedManager} associé à la Transaction en
	 * cours, un commit intermédiaire est réalisé.
	 * 
	 * @param nomConnexion
	 *            nom de la connexion à la base de données.
	 * @throws ContexteApplicationException
	 *             en cas d'erreur d'utilisation du ContexteApplication du
	 *             groupe java.
	 * @throws GroupeJavaTransactionException
	 *             en cas d'erreur déclenchée par la transaction.
	 */
	public void postTransaction(String nomConnexion)
			throws ContexteApplicationException, GroupeJavaTransactionException;

	/**
	 * Crée un {@link fr.insee.omphale.utilitaireDuGroupeJava2010.dao.BatchOrderedManager} pour gérer les
	 * traitements batch jdbc. Cette création n'est possible que si une Transaction
	 * a été définie sur cette connexion.
	 * 
	 * @param nomConnexion
	 *            nom de la connexion à la base de données.
	 * @return le BatchManager
	 * @throws ContexteApplicationException
	 *             en cas d'erreur d'utilisation du ContexteApplication du
	 *             groupe java.
	 */
	public BatchOrderedManager createBatchManager(String nomConnexion)
			throws ContexteApplicationException;
}
