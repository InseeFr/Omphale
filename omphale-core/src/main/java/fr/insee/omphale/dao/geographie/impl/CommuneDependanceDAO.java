package fr.insee.omphale.dao.geographie.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.core.service.impl.Service;
import fr.insee.omphale.dao.geographie.ICommuneDependanceDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.geographie.CommuneDependance;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour CommuneDependance
 *
 */
public class CommuneDependanceDAO extends
		GenericDAO<CommuneDependance, Integer> implements ICommuneDependanceDAO {

	@SuppressWarnings("unchecked")
	public List<Integer> findIdByIdCommunes(List<String> idCommunes) {
		Session session = getSession();
		List<BigDecimal> retourQuery = new ArrayList<BigDecimal>();
		List<Integer> retour = new ArrayList<Integer>();

		int nbPaquets = Service.calculNbPaquets(idCommunes);

		try {
			for (int i = 0; i < nbPaquets; i++) {
				List<String> sousListe = idCommunes.subList(i
						* Service.NB_MAX_LISTE_ORACLE, Math.min((i + 1)
						* Service.NB_MAX_LISTE_ORACLE, idCommunes.size()));

				String select = "select dependance "//
						+ "from COMMUNE_DEPENDANCE "//
						+ "where commune in ("
						+ Service.afficherListeSQL(sousListe) + ")";
				Query query = session.createSQLQuery(select);
				retourQuery.addAll(query.list());
			}
			for (BigDecimal bd : retourQuery) {
				retour.add(bd.intValue());
			}
			return retour;
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	public List<CommuneDependance> findById(List<Integer> idDependances) {
		List<CommuneDependance> retour = new ArrayList<CommuneDependance>();
		int nbPaquets = Service.calculNbPaquets(idDependances);
		for (int j = 0; j < nbPaquets; j++) {
			List<Integer> sousListe = idDependances.subList(
					Service.NB_MAX_LISTE_ORACLE * j, Math.min(
							Service.NB_MAX_LISTE_ORACLE * (j + 1),
							idDependances.size()));
			retour.addAll(findByIdSousListe(sousListe));

		}
		return retour;
	}

	@SuppressWarnings("unchecked")
	private List<CommuneDependance> findByIdSousListe(
			List<Integer> idDependances) {
		Session session = getSession();
		try {
			String select = "from CommuneDependance cd where cd.id in (:listeId)";
			return session.createQuery(select)
					.setParameterList("listeId", idDependances).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
