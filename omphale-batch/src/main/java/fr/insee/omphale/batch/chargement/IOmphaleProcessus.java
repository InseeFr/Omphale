package fr.insee.omphale.batch.chargement;


/**
 * Interface définissant un plugin de traitement des fichiers Dads-u. Cette
 * interface doit être implémentée par tout nouveau processus devant être
 * intégré à MainDadsProcessus.
 *
 */
public interface IOmphaleProcessus {
	/**
	 * Exécution d'un plugin. Cette methode constitue le "main" d'un plugin.
	 *
	 * @throws SiaspDadsuException
	 *             en cas d'erreur.
	 */
	public boolean executePlugin() throws Exception;
}
