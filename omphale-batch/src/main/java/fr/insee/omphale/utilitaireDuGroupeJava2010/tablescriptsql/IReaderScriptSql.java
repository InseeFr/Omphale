package fr.insee.omphale.utilitaireDuGroupeJava2010.tablescriptsql;

import java.io.IOException;

/**
 * Interface de lecture d'un fichier texte contenant une ou plusieurs oéprations
 * SQL et/ou PL/SQL. Ce lecteur est éventuellement chargé de remettre en forme le
 * contenu du ficheir lu pour le rendre compatible avec le chargement de la
 * table des scripts.
 *
 */
public interface IReaderScriptSql {
	/**
	 * Renvoie une instruction sql de chargement d'une ligne dans la table des
	 * scripts SQL. Cette instruction est de la forme :<br>
	 * <code>insert into <i>nomTable</i> values (<i>nomOperation</i>,<i>no_sql</i>,<i>no_ligne</i>,<i>type_sql</i>,<i>texte_sql</i>);</code>
	 *
	 * @return l'instruction sql mise en forme.
	 * @throws IOException
	 *             en cas d'erreur d'entrée-sortie.
	 */
	public String getInstructionSql() throws IOException;

	/**
	 * Teste s'il existe encore des lignes dans le fichier SQL en cours de
	 * lecture.
	 *
	 * @return true si oui, false sinon.
	 */
	public boolean hasNext();
}
