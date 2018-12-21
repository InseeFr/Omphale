package fr.insee.omphale.utilitaireDuGroupeJava2010.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaBatchSQLException;
import fr.insee.omphale.utilitaireDuGroupeJava2010.exception.GroupeJavaDaoException;


/**
 * Classe permettant la gestion groupée et ordonnée des ordres sql avec
 * plusieurs objets {@link fr.insee.omphale.utilitaireDuGroupeJava2010.dao.BatchAbstractDao}. <br>
 * Pour cela, il est nécessaire d'enregistrer les DAOs nécessaires dans le
 * BatchManager à l'aide de la methode {@link #addBatchDao}. La purge des ordres
 * SQL en attente sera déclenchée si le nombre de lignes postées par la methode
 * {@link #post()} dépasse {@link #frequenceBatch}.<br>
 * Cette fréquence est fixée :<br>
 * <ul>
 * <li>par la methode {@link #setFrequenceBatch}</li>
 * <li>par défaut {@link #TAILLE_BATCH}</li>
 * </ul>
 * 
 */
public class BatchOrderedManager {
	/**
	 * Conservation de la liste des BatchAbstractDaos de la transaction.
	 */
	@SuppressWarnings("rawtypes")
	private List<BatchAbstractDao> listBatchDao = new ArrayList<BatchAbstractDao>();

	/**
	 * La fréquence par défaut de soumission du batch SQL.
	 */
	private static final int TAILLE_BATCH = 100;
	/**
	 * Nombre d'ordres SQL en attente.
	 */
	private int nbSqlPending;
	/**
	 * La fréquence de soumission du batch SQL (nombre d'ordres SQL).
	 */
	private int frequenceBatch;

	/**
	 * Constructeur par défaut.
	 */
	public BatchOrderedManager() {
		this(TAILLE_BATCH);
	}

	/**
	 * Constructeur permettant de fixer la fréquence de soumission du batch SQL.
	 * 
	 * @param frequenceBatch
	 *            la fréquence de soumission du batch SQL.
	 */
	public BatchOrderedManager(int frequenceBatch) {
		this.frequenceBatch = frequenceBatch;
		this.nbSqlPending = 0;
	}

	/**
	 * Fixe la fréquence de soumission du batch SQL.
	 * 
	 * @param frequenceBatch
	 *            la fréquence de soumission du batch SQL.
	 */
	public void setFrequenceBatch(int frequenceBatch) {
		this.frequenceBatch = frequenceBatch;
	}

	/**
	 * Ajoute un {@link BatchAbstractDao} dans le manager. L'ordre des Dao doit
	 * refléter l'ordre des contraintes d'intégrité de base de données : il faut
	 * créer les 'peres' avant les 'fils'.
	 * 
	 * @param de classe T
	 *            le bean gérant l'ordre SQL
	 * @param dao
	 *            le DAO d'accès à la base de données
	 */
	public <T> void addBatchDao(BatchAbstractDao<T> dao) {
		listBatchDao.add(dao);
	}

	/**
	 * Poste une ligne dans la transaction. Si le nombre de lignes postées
	 * dépasse la taille de la transaction définie lors de la construction de
	 * l'objet, les DAO composant la transaction sont purgés.
	 * 
	 * @return true si les ordres SQL en attete ont été purgés, false sinon.
	 * @throws GroupeJavaBatchSQLException
	 *             en cas d'erreur déclenchée par la purge des ordres SQL en
	 *             attente.
	 */
	@SuppressWarnings({ "rawtypes" })
	public boolean post() throws GroupeJavaBatchSQLException {
		boolean purgeBatch = false;
		this.nbSqlPending++;
		if (this.nbSqlPending >= this.frequenceBatch) {
			// parcours des DAOs de la transaction
			Iterator<BatchAbstractDao> iterator = this.listBatchDao.iterator();
			while (iterator.hasNext()) {
				BatchAbstractDao element = (BatchAbstractDao) iterator.next();
				try {
					element.purgeBatchPending();
					purgeBatch = true;
				} catch (GroupeJavaDaoException e) {
					throw new GroupeJavaBatchSQLException(
							"Impossible de poster la transaction pour "
									+ element, e);
				}
			}
			this.nbSqlPending = 0;
		}
		return purgeBatch;
	}

	/**
	 * Purge les ordres SQL en attente et close les DAO enregistrés.
	 * 
	 * @throws GroupeJavaBatchSQLException
	 *             en cas d'erreur déclenchée par la purge des ordres SQL en
	 *             attente.
	 */
	@SuppressWarnings({ "rawtypes" })
	public void close() throws GroupeJavaBatchSQLException {
		// parcours des DAOs de la transaction
		Iterator<BatchAbstractDao> iterator = this.listBatchDao.iterator();
		while (iterator.hasNext()) {
			BatchAbstractDao element = (BatchAbstractDao) iterator.next();

			try {
				element.purgeBatchPending();
				element.close();
			} catch (GroupeJavaDaoException e) {
				throw new GroupeJavaBatchSQLException(
						"Impossible de fermer la transaction pour " + element,
						e);
			}

		}
		this.nbSqlPending = 0;

	}
}
