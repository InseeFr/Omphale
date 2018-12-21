package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import fr.insee.omphale.core.service.impl.Service;
import fr.insee.omphale.core.service.projection.IEvolutionNonLocaliseeService;
import fr.insee.omphale.dao.projection.IEvolutionLocaliseeDAO;
import fr.insee.omphale.dao.projection.IEvolutionNonLocaliseeDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.projection.EvolDeScenario;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Hypothese;

/**
 * Classe pour gérer les fonctionnalités de la couche service pour EvolutionNonLocalisee
 *
 */
public class EvolutionNonLocaliseeService implements
		IEvolutionNonLocaliseeService {

	private IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDao;
	private IEvolutionLocaliseeDAO evolutionLocaliseeDao;
	
	public EvolutionNonLocaliseeService(){
		this.evolutionNonLocaliseeDao = daoFactory.getEvolutionNonLocaliseeDAO();
		this.evolutionLocaliseeDao = daoFactory.getEvolutionLocaliseeDAO();
	}
	
	public EvolutionNonLocaliseeService(IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDAO){
		this.evolutionNonLocaliseeDao = evolutionNonLocaliseeDAO;
	}
	
	public EvolutionNonLocaliseeService(IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDAO, IEvolutionLocaliseeDAO evolutionLocaliseeDao){
		this.evolutionNonLocaliseeDao = evolutionNonLocaliseeDAO;
		this.evolutionLocaliseeDao = evolutionLocaliseeDao;
	}

	public void advance(List<EvolutionNonLocalisee> evolutionsNL,
			List<Integer> evolutionsNLId, String advance) {
		if (evolutionsNLId != null && evolutionsNLId.size() >= 1) {
			if (advance.equals("avancer")) {
				int stop = -1; // on ne peut avancer plus que -1;
				for (int i = 0; i < evolutionsNLId.size(); i++) {
					EvolutionNonLocalisee evolutionNL_j = get(evolutionsNL,
							evolutionsNLId.get(i));
					int j = evolutionsNL.indexOf(evolutionNL_j);
					if (j == stop + 1) {
						// on ne peut avancer
						// stop = j
						stop = j;
					} else {
						if (j > 0) {
							EvolutionNonLocalisee evolutionNL_j1 = evolutionsNL
									.get(j - 1);
							evolutionsNL.set(j, evolutionNL_j1);
							evolutionsNL.set(j - 1, evolutionNL_j);
						}
					}
				}
			}

			if (advance.equals("bottom")) {
				int stop = evolutionsNL.size(); // on ne peut avancer plus que
				// evolutionsNL.size();
				for (int i = evolutionsNLId.size() - 1; i >= 0; i--) {
					EvolutionNonLocalisee evolutionNL_j = get(evolutionsNL,
							evolutionsNLId.get(i));
					int j = evolutionsNL.indexOf(evolutionNL_j);
					if (j == stop - 1) {
						// on ne peut avancer
						// stop = j
						stop = j;
					} else {
						if (evolutionsNL.size() > (j + 1)) {
							EvolutionNonLocalisee evolutionNL_j1 = evolutionsNL
									.get(j + 1);
							evolutionsNL.set(j, evolutionNL_j1);
							evolutionsNL.set(j + 1, evolutionNL_j);
						}
					}
				}
			}
		}
	}

	public long compterEvolutionsNonLocalisees(Utilisateur utilisateur) {
		return evolutionNonLocaliseeDao
				.compterEvolutionsNonLocalisees(utilisateur);
	}

	public void delete(EvolutionNonLocalisee evolution) {
		evolutionNonLocaliseeDao.delete(evolution);
	}

	public List<EvolutionNonLocalisee> findAll(Boolean standard,
			String composante) {
		return evolutionNonLocaliseeDao.findAll(standard, composante);
	}

	public List<EvolutionNonLocalisee> findAll(List<Integer> listeIdentifiantENL) {
		if (listeIdentifiantENL != null && listeIdentifiantENL.size() > 0) {
			List<EvolutionNonLocalisee> liste = new ArrayList<EvolutionNonLocalisee>(
					listeIdentifiantENL.size());
			for (int i = 0; i < listeIdentifiantENL.size(); i++) {
				liste.add(evolutionNonLocaliseeDao
						.getById(listeIdentifiantENL.get(i)));
			}
			return liste;
		} else {
			return new ArrayList<EvolutionNonLocalisee>(0);
		}
	}

	public List<EvolutionNonLocalisee> findAll(String idUser, Boolean standard,
			String composante) {
		if (standard != null && standard.booleanValue()) { // standard
			return findAll(standard, composante);
		} else { // non standard
			return evolutionNonLocaliseeDao.findAll(idUser, standard,
					composante);
		}
	}

	public List<EvolutionNonLocalisee> findAll(String idUser, Boolean standard,
			String composante,
			List<EvolutionNonLocalisee> listeEvolutionNonLocalisee) {
		List<EvolutionNonLocalisee> liste = null;
		if (standard != null && standard.booleanValue()) { // standard
			liste = findAll(standard, composante);
		} else { // non standard
			liste = findAll(idUser, standard, composante);
		}
		if (listeEvolutionNonLocalisee != null
				&& listeEvolutionNonLocalisee.size() > 0) {
			liste.removeAll(listeEvolutionNonLocalisee);
		}
		return liste;
	}

	public List<EvolutionNonLocalisee> findAllOrdreDefaut(
			List<Integer> listeIdentifiantENL) {
		if (listeIdentifiantENL != null && listeIdentifiantENL.size() > 0) {
			return evolutionNonLocaliseeDao.findAllOrdreDefaut(Service
					.tableauToString(listeIdentifiantENL));
		} else
			return new ArrayList<EvolutionNonLocalisee>(0);
	}

	public List<EvolutionNonLocalisee> findByHypothese(Hypothese hypothese) {
		return evolutionNonLocaliseeDao.findByHypothese(hypothese);
	}

	public EvolutionNonLocalisee findById(int id) {
		return evolutionNonLocaliseeDao.findById(id);
	}

	public List<EvolutionNonLocalisee> findByUtilisateur(Utilisateur utilisateur) {
		return evolutionNonLocaliseeDao.findByUtilisateur(utilisateur);
	}

	public List<EvolutionNonLocalisee> findStandard() {
		return evolutionNonLocaliseeDao.findStandard();
	}

	public EvolutionNonLocalisee get(List<EvolutionNonLocalisee> liste,
			Integer id) {

		EvolutionNonLocalisee evolutionNL = null;
		boolean ok = false;
		Iterator<EvolutionNonLocalisee> it1 = liste.iterator();
		while (it1.hasNext() && !ok) {
			EvolutionNonLocalisee evolutionNLaux = it1.next();
			if (evolutionNLaux.getId() == id.intValue()) {
				evolutionNL = evolutionNLaux;
				ok = true;
			}
		}
		return evolutionNL;
	}

	public List<EvolutionNonLocalisee> getListe(
			Set<EvolDeScenario> evolutionsDeScenario) {

		if (evolutionsDeScenario != null && evolutionsDeScenario.size() > 0) {

			// initialisation
			List<EvolutionNonLocalisee> evolutionsNL = new ArrayList<EvolutionNonLocalisee>(
					evolutionsDeScenario.size());
			for (int i = 0; i < evolutionsDeScenario.size(); i++) {
				evolutionsNL.add(null);
			}
			// evolutionsNL.set
			for (EvolDeScenario evolDeScenario : evolutionsDeScenario) {
				evolutionsNL.set(evolDeScenario.getRang() - 1, evolDeScenario
						.getId().getEvolutionNonLocalisee());
			}

			return evolutionsNL;
		} else {
			return new ArrayList<EvolutionNonLocalisee>(0);
		}
	}

	public List<Integer> getTableauId(List<EvolutionNonLocalisee> liste) {

		List<Integer> tableau = new ArrayList<Integer>(liste.size());
		if (liste != null && liste.size() > 0) {
			for (int i = 0; i < liste.size(); i++) {
				tableau.add(new Integer(liste.get(i).getId()));
			}
			return tableau;
		}
		tableau.add(new Integer(0));
		return tableau;
	}

	public EvolutionNonLocalisee insertOrUpdate(EvolutionNonLocalisee evolutionNonLocalisee) {
		return evolutionNonLocaliseeDao.insertOrUpdate(evolutionNonLocalisee);
	}

	public boolean testerNomEvolutionNonLocalisee(Utilisateur utilisateur,
			String nomEvolution) {
		for (EvolutionNonLocalisee evolution : evolutionNonLocaliseeDao
				.findByUtilisateur(utilisateur)) {
			if (nomEvolution.equals(evolution.getNom())) {
				return false;
			}
		}
		return true;
	}

	public List<EvolutionNonLocalisee> filtrerParStandardEtComposante(
			Integer standard, String codeComposante,
			List<EvolutionNonLocalisee> enls) {

		if (enls == null || enls.isEmpty()) {
			return new ArrayList<EvolutionNonLocalisee>();
		}

		List<EvolutionNonLocalisee> resultat = new ArrayList<EvolutionNonLocalisee>();
		for (EvolutionNonLocalisee enl : enls) {
			if (standard == -1) {
				if (codeComposante.equals("-1")
						|| enl.getComposante().getCode().equals(codeComposante)) {
					resultat.add(enl);
				}
			} else {
				boolean isStandard = (standard == 1) ? true : false;
				if (isStandard == enl.getStandard()) {
					if (codeComposante.equals("-1")
							|| enl.getComposante().getCode() == null ||enl.getComposante().getCode().equals(
									codeComposante)) {
						resultat.add(enl);
					}
				}
			}
		}

		return resultat;
	}
	
	public List<EvolutionNonLocalisee> findENLStandardsByUtilisateur(Utilisateur utilisateur) {
		List<EvolutionNonLocalisee> allEvolutionNonLocalisees = evolutionNonLocaliseeDao.findAll();
		List<EvolutionNonLocalisee> resultat = new ArrayList<EvolutionNonLocalisee>();
		for (EvolutionNonLocalisee enl : allEvolutionNonLocalisees) {
			if (enl.getStandard()== true && enl.getUtilisateur().equals(utilisateur)) {
				resultat.add(enl);
			}
		}
		return resultat.isEmpty() ? null : resultat;
	}
	
	public List<EvolutionNonLocalisee> findENLNonStandardsByUtilisateur(Utilisateur utilisateur) {
		List<EvolutionNonLocalisee> allEvolutionNonLocalisees = evolutionNonLocaliseeDao.findAll();
		List<EvolutionNonLocalisee> resultat = new ArrayList<EvolutionNonLocalisee>();
		if (allEvolutionNonLocalisees != null && !allEvolutionNonLocalisees.isEmpty()){
			for (EvolutionNonLocalisee enl : allEvolutionNonLocalisees) {
				if (enl.getStandard()== false && enl.getUtilisateur().equals(utilisateur)) {
					resultat.add(enl);
				}
			}
		}
		return resultat.isEmpty() ? null : resultat;
	}
	
	
	public List<String> findUtilisateurs(Zone zone){
		List<String> utilisateurs = new ArrayList<String>();
		
		List<Integer> idEvolutionNonLocalisees = evolutionNonLocaliseeDao.findByZone(zone);
		
		for (Integer idENL : idEvolutionNonLocalisees) {
			EvolutionNonLocalisee enl = evolutionNonLocaliseeDao.findById(idENL);
			String idUtilisateur = enl.getUtilisateur().getId();
			if( CollectionUtils.isEmpty(utilisateurs)){
				utilisateurs.add(idUtilisateur);
			}else{
				if(!utilisateurs.contains(idUtilisateur)){
					utilisateurs.add(idUtilisateur);
				}
			}
		}
		return utilisateurs;

	}

	@Override
	public List<EvolutionNonLocalisee> findByUtilisateurStandardOuNon(
			Utilisateur utilisateur) {
		return evolutionNonLocaliseeDao.findByUtilisateurStandardOuNon(utilisateur);
	}
	
	
	public List<Object> rechercheEvolutionsNonLocaliseesPourFonctionSuppression(	Utilisateur utilisateur,
			List<Integer> evolutionsNonLocaliseesASupprimer,
			List<Integer> evolutionsNonLocaliseesAConserver,
			List<String> nomevolutionsNonLocaliseesASupprimer,
			List<String> nomevolutionsNonLocaliseesAConserver){

		List<Object> resultat = new ArrayList<Object>();
		
		evolutionsNonLocaliseesASupprimer =  evolutionLocaliseeDao.findIdENLNonPartagee(utilisateur);
		evolutionsNonLocaliseesAConserver =  evolutionLocaliseeDao.findIdENLPartagee(utilisateur);
		nomevolutionsNonLocaliseesASupprimer =  evolutionLocaliseeDao.findNomENLNonPartagee(utilisateur);
		nomevolutionsNonLocaliseesAConserver =  evolutionLocaliseeDao.findNomENLPartagee(utilisateur);

		resultat.add(evolutionsNonLocaliseesASupprimer);
		resultat.add(evolutionsNonLocaliseesAConserver);
		resultat.add(nomevolutionsNonLocaliseesASupprimer);
		resultat.add(nomevolutionsNonLocaliseesAConserver);

		return resultat;
	}

	@Override
	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId) {
		return evolutionNonLocaliseeDao.deleteByListeIdEvolutionNonLocalisee(evolutionsNonLocaliseesASupprimerId);
	}

	@Override
	public int deleteHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId) {
		return evolutionNonLocaliseeDao.deleteHypotheseByIdListeHypothese(hypothesesASupprimerId);
	}	
	
	
}