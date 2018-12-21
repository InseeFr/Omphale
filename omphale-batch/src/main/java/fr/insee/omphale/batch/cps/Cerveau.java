package fr.insee.omphale.batch.cps;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe du cerveau du pattern CPS. Fonctionnellement, cette classe est un
 * singleton, capable de gérer de manière autonome, un nombre quelconque de
 * synapses. Il dispose d'une connexion DAO, qui lui permet d'assurer le service
 * de récupération des faits de la mémoire profonde stockés dans une base de
 * données pour les neurones implémentant l'interface INeuroneDAO. La connexion
 * du cerveau est unique, ce qui suppose que toutes les synapses DAO
 * s'alimentent dans la même base de données. Si ce n'est pas le cas, il faut
 * utiliser autant de cerveaux que de bases de données. Les synapses crées par
 * le cerveau, sont stockées dans une map. La clé de cette map est la classe de
 * neurone correspondant à la synapse. Si on veut gérer plusieurs synapses
 * correspondant au même type de neurone, il faut soit dériver cette classe de
 * neurones, soit utiliser plusieurs cerveaux.
 *
 */
public class Cerveau {
	/**
	 * Map de rangement des synapses.
	 */
	@SuppressWarnings("rawtypes")
	private Map<Class, Synapse> synapses = new HashMap<Class, Synapse>();

	/**
	 * Map des synapses DAO en reuse curseur.
	 */
	private ArrayList<Synapse> reglesDao = new ArrayList<Synapse>();

	/**
	 * Connexion vers la base de données.
	 */
	private Connection connexion = null;

	/**
	 * Constructeur si on utilise des INeuroneDAO. C'est la seule façon de
	 * fournir la connexion DAO au cerveau. Le cerveau saura également traiter
	 * les INeuroneNonDAO.
	 *
	 * @param connexion
	 */

	public Cerveau(Connection connexion) {
		this.connexion = connexion;
	}

	/**
	 * Constructeur si on n'utilise pas les services DAO. Le cerveau ne saura
	 * pas traiter les INeuroneDAO.
	 */
	public Cerveau() {
		this.connexion = null;
	}

	/**
	 * Classe interne des synapses. Une synapse gère des collections de neurones
	 * d'une certaine classe dans une map. Chacune des collections de neurones
	 * correspond à une entrée dans la map. Chaque neurone est chargé dans la
	 * collection qui lui convient, au fur et à mesure de son arrivée. Si à
	 * l'intérieur d'une collection, l'ordre est important, il doit donc être
	 * pris en compte au moment de la fourniture.
	 *
	 */
	private class Synapse {
		/**
		 * Neurone messager associé à la synapse. Ce neurone est de même classe
		 * que les neurones de la synapse. Son rôle principal est la
		 * communication entre la synapse et l'extérieur. Il peut également
		 * jouer le rôle de porteur de valises (informations globales). Ceci
		 * peut éviter le recours aux propriétés statiques , mais suppose de
		 * l'associer aux autres neurones, ce qui n'est pas fait en standard,
		 * mais peut-être réalisé dans la méthode collabo.
		 */
		INeurone modele = null;

		/**
		 * PreparedStatement utilisé par les services DAO du cerveau, en
		 * particulier permet l'optimisation en évitant de reparser les requêtes
		 * SQL, dont seuls les paramètres ont changé.
		 */
		PreparedStatement ps = null;

		/**
		 * Map de stockage des collections de neurones associés à la synapse.
		 */
		Map<String, ArrayList<INeurone>> map = null;

		/**
		 * Méthode de création et chargement de la map des neurones de la
		 * synapse.
		 *
		 * @param faits
		 *            faits issus de la mémoire profonde.
		 * @throws Exception
		 *             en cas d'erreur DAO, ou d'instanciation de neurones.
		 */
		public void create(ArrayList<Object> faits) throws Exception {
			Map<String, ArrayList<INeurone>> mapRegles = new HashMap<String, ArrayList<INeurone>>();
			// appel de la méthode de création des neurones à partir d'une
			// collection
			// de faits issus de la mémoire profonde.
			ArrayList<INeurone> regles = this.buildSynapse(faits);
			INeurone regle = null;
			for (int i = 0; i < regles.size(); i++) {
				regle = regles.get(i);
				if (mapRegles.get(regle.getMapKey()) == null) {
					mapRegles.put(regle.getMapKey(), new ArrayList<INeurone>());
				}
				mapRegles.get(regle.getMapKey()).add(regle);
			}
			this.map = mapRegles;
			// appel de la méthode collabo, permettant d'ajouter de
			// l'interopérabilité aux neurones
			modele.collabo(regles);
		}

		/**
		 * Méthode de création des neurones de la synapse à partir d'une
		 * collection de faits issus de la mémoire profonde.
		 *
		 * @param faits
		 *            faits issus de la mémoire profonde.
		 * @return une collection de neurones.
		 * @throws Exception
		 *             en cas d'erreur DAO, ou d'instanciation de neurones.
		 */
		private ArrayList<INeurone> buildSynapse(ArrayList<Object> faits)
				throws Exception {
			ArrayList<INeurone> regles = new ArrayList<INeurone>();
			for (int i = 0; i < faits.size(); i++) {
				INeurone regle = this.createNeurone();
				regle.setDeepFait(faits.get(i));
				regles.add(i, regle);
			}
			return regles;
		}

		/**
		 * Méthode d'instanciation d'un nouveau neurone de la classe du neurone
		 * modèle.
		 *
		 * @return un nouveau neurone.
		 * @throws InstantiationException
		 *             si erreur
		 * @throws IllegalAccessException
		 *             si erreur
		 */
		private INeurone createNeurone() throws InstantiationException,
				IllegalAccessException {
			INeurone nouveau = this.modele.getClass().newInstance();
			return nouveau;
		}

		public Map<String, ArrayList<INeurone>> getMap() {
			return map;
		}


	}

	/**
	 * Méthode de création d'une nouvelle synapse du cerveau, pour les neurones
	 * implémentant l'interface INeuroneDAO.
	 *
	 * @param neurone
	 *            neurone modèle de la synapse. Sa classe caractérise la
	 *            synapse, et ses arguments contiennent les paramètres éventuels
	 *            de la requête d'extraction des faits de la base de données.
	 *            Cette méthode est appelée par les programmes utilisateurs. Si
	 *            la synapse existe déjà, elle est conservée. Seule sa map de
	 *            neurones va changer. En particulier, le neurone modèle de la
	 *            synapse est conservé.
	 * @throws Exception
	 *             en cas d'erreur DAO, ou d'instanciation de neurone.
	 */

	public void createSynapse(INeuroneDAO neurone, Object faitModele)
			throws Exception {
		neurone.setCreateSynapseParameters(faitModele);
		createSynapse(neurone);
	}

	public void createSynapse(INeuroneDAO neurone) throws Exception {
		if (connexion == null)
			throw new Exception("Pas de connexion SQL");
		Synapse synapse = synapses.get(neurone.getClass());
		if (synapse != null) {
			synapse.map = null;
			if (!neurone.isReuseCursor()) {
				synapse.ps = null;
			}

		} else {
			synapse = new Synapse();
			synapse.modele = neurone;
		}
		// récupération des faits de la mémoire profonde par les services DAO du
		// cerveau.
		ArrayList<Object> faits = executeDAO(synapse);
		// chargement de la synapse.
		synapse.create(faits);
		// stockage de la synapse.
		synapses.put(neurone.getClass(), synapse);
	}

	/**
	 * Méthode de création d'une nouvelle synapse du cerveau, pour les neurones
	 * implémentant l'interface INeuroneNonDAO.
	 *
	 * @param neurone
	 *            neurone modèle de la synapse. Sa classe caractérise la
	 *            synapse, et ses arguments contiennent les paramètres éventuels
	 *            pour l'extraction des faits de la mémoire profonde. Cette
	 *            méthode est appelée par les programmes utilisateurs. Si la
	 *            synapse existe déjà, elle est conservée. Seule sa map de
	 *            neurones va changer. En particulier, le neurone modèle de la
	 *            synapse est conservé.
	 * @throws Exception
	 *             en cas d'erreur d'extraction ou d'instanciation de neurones.
	 */
	public void createSynapse(INeuroneNonDAO neurone,
			ArrayList<Object> faitModele) throws Exception {
		neurone.setCreateSynapseParameters(faitModele);
		createSynapse(neurone);
	}

	public void createSynapse(INeuroneNonDAO neurone) throws Exception {
		Synapse synapse = synapses.get(neurone.getClass());
		if (synapse != null) {
			synapse.map = null;

		} else {
			synapse = new Synapse();
			synapse.modele = neurone;
		}
		// récupération des faits de la mémoire profonde par la méthode portée
		// par le neurone modèle.
		ArrayList<Object> faits = neurone.getDeepMemory();
		// chargement de la synapse.
		synapse.create(faits);
		// stockage de la synapse.
		synapses.put(neurone.getClass(), synapse);
	}

	/**
	 * Méthode d'interrogation d'une synapse. Cette méthode est appelée par les
	 * programmes utilisateurs. Elle renvoie le premier neurone compatible avec
	 * le fait. Cette compatibilité est vérifiée par la méthode parseNeurone
	 * portée par le neurone modèle de la synapse.
	 *
	 * @param neurone
	 *            neurone de même classe que la synapse.
	 * @param faitModele
	 *            fait à comparer aux neurones.
	 * @return le premier neurone compatible ou null sinon.
	 * @throws IOException
	 */
	public INeurone getRegle(INeurone neurone, Object faitModele)
			throws Exception {
		neurone.setRechercheParameters(faitModele);
		return getRegle(neurone);
	}

	/**
	 * Méthode d'interrogation d'une synapse. Cette méthode est appelée par les
	 * programmes utilisateurs. Elle renvoie le premier neurone compatible avec
	 * le fait. Cette compatibilité est vérifiée par la méthode parseNeurone
	 * portée par le neurone modèle de la synapse.
	 *
	 * @param neurone
	 *            neurone de même classe que la synapse.
	 * @return le premier neurone compatible ou null sinon.
	 * @throws IOException
	 */
	public INeurone getRegle(INeurone neurone) throws Exception {
		Synapse synapse = synapses.get(neurone.getClass());
		if (synapse == null) {
			return null;
		}
		INeurone modele = neurone;
		ArrayList<INeurone> regles = synapse.getMap().get(modele.getMapKey());
		if (regles == null) {
			return null;
		}
		INeurone regle;
		for (int i = 0; i < regles.size(); i++) {
			regle = regles.get(i);
			if (regle == null) {
				continue;
			}
			if (regle.parseNeurone(modele)) {
				return regle;
			}
		}
		return null;
	}

	/**
	 * renvoie une séquence de la synapse (arraylist des neurones de même mapKey
	 * que le neurone paramètre.
	 *
	 * @param neurone
	 * @return la sous liste des neurones correspondant à la même MapKey que le
	 *         neurone modèle.
	 */
	public ArrayList<INeurone> getSequence(INeurone neurone) {
		if (neurone == null)
			return null;
		Synapse synapse = synapses.get(neurone.getClass());
		if (synapse == null) {
			return null;
		}
		if (synapse.getMap() == null) {
			return null;
		}
		return synapse.getMap().get(neurone.getMapKey());
	}

	/**
	 * renvoie un arraylist de l'ensemble des règles compatibles avec le neurone
	 * paramètre.
	 *
	 * @param neurone
	 * @return la liste des neurones compatibles avec le neurone parametre.
	 * @throws IOException
	 *             en cas d'erreur.
	 */
	public ArrayList<INeurone> getAllRegles(INeurone neurone) throws Exception {
		Synapse synapse = synapses.get(neurone.getClass());
		if (synapse == null) {
			return null;
		}
		ArrayList<INeurone> regles = synapse.getMap().get(neurone.getMapKey());
		if (regles == null) {
			return null;
		}
		ArrayList<INeurone> reglesOk = new ArrayList<INeurone>();
		INeurone regle;
		for (int i = 0; i < regles.size(); i++) {
			regle = regles.get(i);
			if (regle == null) {
				continue;
			}
			if (regle.parseNeurone(neurone)) {
				reglesOk.add(regle);
			}
		}
		if (reglesOk.size() < 1) {
			return null;
		}
		return reglesOk;
	}

	/**
	 * Méthode responsable des services DAO assurés par le cerveau pour
	 * l'extraction SQL des faits de la mémoire profonde.
	 *
	 * @param synapse
	 *            La synapse contient le neurone modèle et le preparedstatement.
	 *            La requete SQL et son paramétrage sont assurés par les
	 *            méthodes getSql et setSqlParameters du neurone modèle.
	 * @return une collection de faits issus de la base de données.
	 * @throws CpsException
	 * @throws SQLException
	 *             selon erreur DAO.
	 */
	protected final ArrayList<Object> executeDAO(Synapse synapse)
			throws CpsException {
		INeuroneDAO modele = (INeuroneDAO) synapse.modele;
		ArrayList<Object> faits = new ArrayList<Object>();
		PreparedStatement ps = null;
		ResultSet r = null;
		Connection c = connexion;
		try {
			if (modele.isReuseCursor()) {
				ps = synapse.ps;
				// stockage de la synapse en reuse curseur pour fermer
				// proprement
				// les preparedstatements lors de la fermeture du cerveau.
				if (ps == null)
					reglesDao.add(synapse);
			}
			if (ps == null) {
				ps = c.prepareStatement(modele.getSql());

			}
			if (!modele.isStringColumns()) {
				modele.setSqlParameters(ps);
			} else {
				ArrayList<String> fait = modele.getCreateSynapseParameters();
				if (fait != null) {
					for (int i = 0; i < fait.size(); i++) {
						ps.setString(i + 1, (String) fait.get(i));
					}
				}

			}

			r = ps.executeQuery();
			int row = 0;
			while (r.next()) {
				if (!modele.isStringColumns()) {
					faits.add(row, modele.getSqlColumns(r));
				} else {
					ArrayList<Object> fait = new ArrayList<Object>();
					for (int i = 0; i < r.getMetaData().getColumnCount(); i++) {
						fait.add(i, r.getString(i + 1));
					}
					faits.add(row, fait);
				}
				row++;
			}

		} catch (SQLException e) {
			throw new CpsException("erreur DAO dans le Cerveau", e);
		} finally {
			/**
			 * sauvegarde du ps dans la synapse à des fins d'optimisation par
			 * réutilisation.
			 */
			if (modele.isReuseCursor()) {
				synapse.ps = ps;
				try {
					// pour que le close ferme le resultset : sinon
					// maxopencurseor
					// ...
					if (r != null) {
						r.close();
						r = null;
					}

				} catch (SQLException e) {
					throw new CpsException(
							"impossible de fermer un objet vers la base de données",
							e);

				}
			}
			/**
			 * fermeture des curseurs, mais pas de la connexion.
			 */
			else {
				// pour que le close ferme le statement et le resultset sinon
				// maxopencurseor
				// ...
				try {

					if (r != null) {
						r.close();
						r = null;
					}
					if (ps != null) {
						ps.close();
						ps = null;
						synapse.ps = null;
					}
				} catch (SQLException e) {
					throw new CpsException(
							"impossible de fermer un objet vers la base de données",
							e);
				}
			}

		}

		return faits;
	}

	/**
	 * fermeture des preparedstatements des synapses en reuse curseur.
	 *
	 * @throws CpsException
	 */
	public void closeReglesDao() throws CpsException {
		INeuroneDAO regle = null;
		Synapse synapse = null;
		for (int i = 0; i < reglesDao.size(); i++) {
			synapse = reglesDao.get(i);
			if (synapse == null) {
				continue;
			}
			regle = (INeuroneDAO) synapse.modele;

			if (regle == null) {
				continue;
			}
			if (regle.isReuseCursor()) {
				Statement s = synapse.ps;
				try {
					if (s != null) {
						s.close();
						s = null;
					}
				} catch (SQLException e) {
					throw new CpsException(
							"impossible de fermer un objet vers la base de données",
							e);

				}
			}
		}
	}

	/**
	 * fermeture du cerveau. La connexion n'est pas fermée, car elle n'est pas
	 * de la responsabilité du cerveau.
	 *
	 * @throws CpsException
	 */
	public void close() throws CpsException {
		closeReglesDao();
		synapses = null;
	}

}
