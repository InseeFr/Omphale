package fr.insee.omphale.batch.cps;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Interface des neurones spécialisée pour l'utilisation des services
 * DAO du cerveau pour la récupération des faits de la mémoire profonde.
 *
 */
public interface INeuroneDAO extends INeurone{
    /**
     *
     * @return vrai si on veut optimiser les requêtes SQL.
     */
    public boolean isReuseCursor();
    /**
     * Extraction des faits de la mémoire profonde
     * @return la requete SQL pour extraction DAO.
     */
    public String getSql();
    /**
     * fournir des paramètres à une requette SQL.
     * Utilisation des arguments de création de la synapse.
     * @param ps statement à paramètrer.
     * @throws SQLException selon erreur
     */
    public void setSqlParameters(final PreparedStatement ps)
           throws SQLException;
    /**
     * si les colonnes du resultset peuvent être extraites en String, on laisse le cerveau
     * s'en débrouiller. Sinon on doit implémenter l'extraction dans getSqlColumns.
     * @return vrai si les colonnes du resultset peuvent être extraites en String par le cerveau.
     */
    public boolean isStringColumns();
    public ArrayList <String> getCreateSynapseParameters();
    /**
     * Extraire les colonnes du row courant d'un resultset
     * @param r resultset courant.
     * @throws SQLException selon erreur
     */
    public Object getSqlColumns(final ResultSet r)
           throws SQLException;

}
