package fr.insee.omphale.dao.geographie.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.core.service.impl.Service;
import fr.insee.omphale.dao.geographie.ICommuneDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour Commune
 *
 */
public class CommuneDAO extends GenericDAO<Commune, String> implements
		ICommuneDAO {

	public List<String> findIdNonPresents(List<String> idCommunes) {
		List<String> idPresents = new ArrayList<String>();
		int nbPaquets = Service.calculNbPaquets(idCommunes);
		for (int j = 0; j < nbPaquets; j++) {
			List<String> sousListe = idCommunes.subList(
					Service.NB_MAX_LISTE_ORACLE * j, Math.min(
							Service.NB_MAX_LISTE_ORACLE * (j + 1), idCommunes
									.size()));
			idPresents.addAll(findIdPresentsSousListe(sousListe));
		}
		idCommunes.removeAll(idPresents);
		return idCommunes;
	}

	@SuppressWarnings("unchecked")
	public List<Commune> findAllByDepartement(Departement departement) {
		Session session = getSession();
		try {
			String select = "from Commune c where c.departement = :departement";
			return session.createQuery(select).setParameter(
					"departement", departement).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	public List<Commune> findById(List<String> idCommunes) {
		List<Commune> retour = new ArrayList<Commune>();
		int nbPaquets = Service.calculNbPaquets(idCommunes);
		for (int j = 0; j < nbPaquets; j++) {
			List<String> sousListe = idCommunes.subList(
					Service.NB_MAX_LISTE_ORACLE * j, Math.min(
							Service.NB_MAX_LISTE_ORACLE * (j + 1), idCommunes
									.size()));
			retour.addAll(findByIdSousListe(sousListe));

		}
		return retour;
	}

	@SuppressWarnings("unchecked")
	private List<Commune> findByIdSousListe(List<String> idCommunes) {
		Session session = getSession();
		try {
			String select = "from Commune c where c.id in (:listeId)";
			return session.createQuery(select)
					.setParameterList("listeId", idCommunes).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	private List<String> findIdPresentsSousListe(List<String> idCommunes) {
		Session session = getSession();
		try {
			String select = "select id_commune "//
					+ "from COMMUNE "//
					+ "where id_commune in ("
					+ Service.afficherListeSQL(idCommunes) + ")";
			Query query = session.createSQLQuery(select);
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

}
