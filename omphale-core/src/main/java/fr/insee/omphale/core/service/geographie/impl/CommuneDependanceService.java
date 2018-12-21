package fr.insee.omphale.core.service.geographie.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.insee.omphale.core.service.geographie.ICommuneDependanceService;
import fr.insee.omphale.dao.geographie.ICommuneDependanceDAO;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.CommuneDependance;


/**
 * Classe gérant les fonctionnalités de la couche service pour les "CommuneDependance"
 *
 */
public class CommuneDependanceService implements ICommuneDependanceService {

	private ICommuneDependanceDAO cdDao;

	public CommuneDependanceService() {
		this.cdDao = daoFactory.getCommuneDependanceDAO();
	}

	public CommuneDependanceService(ICommuneDependanceDAO cdDAO) {
		this.cdDao = cdDAO;
	}

	public List<CommuneDependance> findByCommunes(List<Commune> communes, ICommuneDependanceService cdService) {
		List<CommuneDependance> resultat = new ArrayList<CommuneDependance>();
		Set<Commune> freres = cdService.trouverFreresProches(new HashSet<Commune>(
				communes));
		if (freres == null) {
			return resultat;
		} else {
			int nbFreres = freres.size();
			boolean freresTrouves = false;
			while (!freresTrouves) {
				freres = cdService.trouverFreresProches(freres);
				if (nbFreres == freres.size()) {
					freresTrouves = true;
				} else {
					nbFreres = freres.size();
				}
			}
		}

		List<String> idCommunes = new ArrayList<String>();
		for (Commune com : freres) {
			idCommunes.add(com.getId());
		}
		List<Integer> idDependances = cdDao.findIdByIdCommunes(idCommunes);
		return cdDao.findById(idDependances);
	}

	public Set<Commune> trouverFreresProches(Set<Commune> communes) {
		Set<Commune> resultat = new HashSet<Commune>();
		// On va utiliser les identifiants des communes pour faire la recherche
		// plus rapidement
		List<String> idCommunes = new ArrayList<String>();
		for (Commune com : communes) {
			idCommunes.add(com.getId());
		}
		// On récupère les identifiants des dépendances
		List<Integer> idDependances = cdDao.findIdByIdCommunes(idCommunes);
		// On récupère les dépendances correspondantes à ces identifiants
		List<CommuneDependance> communesDependances = cdDao
				.findById(idDependances);
		// On ajoute toutes les communes de ces dépendances
		for (CommuneDependance communeDependance : communesDependances) {
			resultat.addAll(communeDependance.getCommunes());
		}
		return resultat.isEmpty() ? null : resultat;
	}

}
