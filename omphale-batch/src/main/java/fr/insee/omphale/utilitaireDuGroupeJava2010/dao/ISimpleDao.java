package fr.insee.omphale.utilitaireDuGroupeJava2010.dao;

import java.util.Collection;

import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;


public interface ISimpleDao {

	/**
	 * Execute d'une collection d'ordres SQL. <br>
	 * Les exceptions soulevées par les ordres sql DROP ne sont pas propagées.
	 *
	 * @param ordresSQL
	 *            la collection des ordres SQL
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur SQL à l'exécution
	 */
	public abstract void executeSQL(Collection<String> ordresSQL)
			throws GroupeJavaDaoException;

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
	public int executeUpdate(String sqlQuery) throws GroupeJavaDaoException;
}