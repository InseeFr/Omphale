package fr.insee.omphale.utilitaireDuGroupeJava2010.tablescriptsql;

import java.util.List;
import java.util.Map;

import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;


public interface ITableScriptSqlDao {

	/**
	 * Exécute les ordres sql contenus dans une table de type script_sql et les
	 * execute instruction par instruction. Les ordres sql peuvent être de type
	 * sql ou plsql. Ces ordres peuvent être paramétrés. La valeur des
	 * parametres est donnée par une Map donnant l'association nomParametre =>
	 * valeur. Les lignes commençant par REM, -- ou // sont considérées comme
	 * commentaire et sont ignorées.
	 * 
	 * @param nomOperation
	 *            le nom de l'opération sql contenannt le script à exécuter.
	 * @param listeParametres
	 *            les parametres éventuels des ordres sql
	 * @throws GroupeJavaDaoException
	 *             en cas d'erreur dans la récupération des ordres sql ou de
	 *             leur exécution
	 */
	public abstract void execute(String nomOperation,
			Map<String, String> listeParametres) throws GroupeJavaDaoException;

	/**
	 * Vérifie si la table de type script_sqp existe dans le schéma oracle.
	 * 
	 * @return true si la table existe, false sinon
	 * @throws GroupeJavaDaoException
	 *             en cas de problème d'accès à la base de données.
	 */
	public abstract boolean existe() throws GroupeJavaDaoException;

	/**
	 * Réinitialise la table de script_sql. Delete toutes les lignes.
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas de problème d'accès à la base de données.
	 */
	public abstract void reinit() throws GroupeJavaDaoException;

	/**
	 * Détruit la table de script_sql.
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas de problème d'accès à la base de données.
	 */
	public abstract void dropTable() throws GroupeJavaDaoException;

	/**
	 * Crée la table de script_sql. Si la table exite, une exception est
	 * déclenchée.
	 * 
	 * @throws GroupeJavaDaoException
	 *             en cas de problème d'accès à la base de données.
	 */
	public abstract void creTable() throws GroupeJavaDaoException;

	/**
	 * Renvoie la liste des noms d'opérations sql répondant à certains critères.
	 * Ces critères sont donnés par le parametres clauseWhere.
	 * 
	 * @param clauseWhere
	 *            le filtre à appliquer lors de la recherche des noms
	 *            d'opérations sql.
	 * @return la liste des opérations.
	 * @throws GroupeJavaDaoException
	 *             en cas de problème d'accès à la base de données.
	 */
	public abstract List<String> getListOperations(String clauseWhere)
			throws GroupeJavaDaoException;


	/**
	 * Crée une opération dans la table des scripts à partir d'un fichier
	 * externe.
	 * 
	 * @param readerSql
	 *            le lecteur de scripts sql.
	 * @throws GroupeJavaDaoException
	 *             en cas d'errreur de lecture du fichier.
	 */
	public void creOperation(IReaderScriptSql readerSql)
			throws GroupeJavaDaoException;
	/**
	 * Accesseur sur le nom de la table de script sql.
	 * 
	 * @return le nom de la table de script sql.
	 */
	public abstract String getNomTableScript();

	/**
	 * Accesseur sur le nom de la table de script sql.
	 * 
	 * @param nomTableScript
	 *            le nom de la table de script sql.
	 */
	public abstract void setNomTableScript(String nomTableScript);
}