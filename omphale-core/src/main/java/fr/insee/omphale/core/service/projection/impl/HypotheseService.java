package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.List;

import fr.insee.omphale.core.service.projection.IHypotheseService;
import fr.insee.omphale.dao.projection.IEvolutionNonLocaliseeDAO;
import fr.insee.omphale.dao.projection.IHypotheseDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Hypothese;
import fr.insee.omphale.domaine.projection.TypeEntite;

/**
 * Classe pour gérer les fonctionnalités de la couche service pour Hypothese
 *
 */
public class HypotheseService implements IHypotheseService {

	private IHypotheseDAO hypotheseDao = daoFactory.getHypotheseDAO();
	@SuppressWarnings("unused")
	private IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDao = daoFactory.getEvolutionNonLocaliseeDAO();

	public HypotheseService() {
		this.hypotheseDao = daoFactory.getHypotheseDAO();
		this.evolutionNonLocaliseeDao = daoFactory.getEvolutionNonLocaliseeDAO();
	}

	public HypotheseService(IHypotheseDAO hypotheseDao) {
		this.hypotheseDao = hypotheseDao;
	}
	
	public HypotheseService(IHypotheseDAO hypotheseDao, IEvolutionNonLocaliseeDAO evolutionNonLocaliseeDao) {
		this.hypotheseDao = hypotheseDao;
		this.evolutionNonLocaliseeDao = evolutionNonLocaliseeDao;
	}

	public long compterHypotheses(Utilisateur utilisateur) {
		return hypotheseDao.compterHypotheses(utilisateur);
	}

	public boolean testerNomHypothese(Utilisateur utilisateur, String nom) {
		List<Hypothese> hypothesesUtilisateur = hypotheseDao
				.findByUtilisateur(utilisateur);
		for (Hypothese hypothese : hypothesesUtilisateur) {
			if (hypothese.getNom().equals(nom)) {
				return false;
			}
		}
		return true;
	}

	public List<Hypothese> findByTypeEntite(TypeEntite typeEntite) {
		return hypotheseDao.findByTypeEntite(typeEntite);
	}

	public Hypothese findById(int id) {
		return hypotheseDao.findById(id);
	}

	public Hypothese insertOrUpdate(Hypothese hypothese) {
		return hypotheseDao.insertOrUpdate(hypothese);
	}

	public List<Hypothese> findByUtilisateur(Utilisateur utilisateur) {
		return hypotheseDao.findByUtilisateur(utilisateur);
	}

	public void delete(Hypothese hypothese) {
		hypotheseDao.delete(hypothese);
	}

	public List<Hypothese> findHypothesesStandards() {
		List<Hypothese> allHypotheses = hypotheseDao.findAll();
		List<Hypothese> resultat = new ArrayList<Hypothese>();
		for (Hypothese hypothese : allHypotheses) {
			if (hypothese.isStandard()) {
				resultat.add(hypothese);
			}
		}
		return resultat.isEmpty() ? null : resultat;
	}

	public List<Hypothese> findHypothesesStandardsByUtilisateur(
			Utilisateur utilisateur) {
		List<Hypothese> allHypotheses = hypotheseDao.findAll();
		List<Hypothese> resultat = new ArrayList<Hypothese>();
		for (Hypothese hypothese : allHypotheses) {
			if (hypothese.isStandard()
					&& hypothese.getUtilisateur().equals(utilisateur)) {
				resultat.add(hypothese);
			}
		}
		return resultat.isEmpty() ? null : resultat;
	}

	public List<Hypothese> findHypothesesNonStandardsByUtilisateur(
			Utilisateur utilisateur) {
		List<Hypothese> allHypotheses = hypotheseDao.findAll();
		List<Hypothese> resultat = new ArrayList<Hypothese>();
		for (Hypothese hypothese : allHypotheses) {
			if (!hypothese.isStandard()
					&& hypothese.getUtilisateur().equals(utilisateur)) {
				resultat.add(hypothese);
			}
		}
		return resultat.isEmpty() ? null : resultat;
	}

	public List<Hypothese> filtrerParStandardEtTypeEntite(Integer standard,
			String codeTypeEntite, List<Hypothese> hypotheses, IHypotheseService hypotheseService) {
		
		if (hypotheses == null || hypotheses.isEmpty()) {
			return new ArrayList<Hypothese>();
		}
		List<Hypothese> resultat = new ArrayList<Hypothese>();

		for (Hypothese hypothese : hypotheses) {
			if (hypotheseService.filtrerHypothese(standard, codeTypeEntite,hypothese)) {
				resultat.add(hypothese);
			}
		}
		return resultat;
	}

	/**
	 * Renvoie true si l'hypothese correspond au standard et typeEntite demandé,
	 * false sinon
	 * 
	 * @param standard
	 * @param codeTypeEntite
	 * @param hypothese
	 * @return
	 */
	public boolean filtrerHypothese(Integer standard, String codeTypeEntite,
			Hypothese hypothese) {
		if (standard == -1) {
			if (codeTypeEntite.equals("-1")
					|| hypothese.getTypeEntite().getCode().equals(
							codeTypeEntite)) {
				return true;
			} else {
				return false;
			}
		} else {
			boolean isStandard = (standard == 1) ? true : false;
			if (hypothese.isStandard() == isStandard) {
				if (codeTypeEntite.equals("-1")
						|| hypothese.getTypeEntite().getCode().equals(
								codeTypeEntite)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * Recherche les hypothèses d'une entité uniquement les standards et celles
	 * de l'utilisateur courant
	 * 
	 * @param entite
	 * @param idep
	 * @return List(Hypothese)
	 */
	public List<Hypothese> findByTypeEntitePourENLGestionParams(
			TypeEntite entite, String idep, IHypotheseService hypotheseService) {
		List<Hypothese> all = hypotheseService.findByTypeEntite(entite);
		List<Hypothese> hypotheses = new ArrayList<Hypothese>();
		for (Hypothese hypothese : all) {
			if (hypothese.isStandard()) {
				hypotheses.add(hypothese);
			} else if (hypothese.getUtilisateur().getIdep().equals(idep)) {
				hypotheses.add(hypothese);
			}
		}

		return hypotheses;
	}
	
	public void supprimerAvecCB_Hypothese(Hypothese hypothese){
		hypotheseDao.delete(hypothese);
	}

	
	public List<Object> rechercheHypothesesPourFonctionSuppression( 	Utilisateur utilisateur,
																List<Integer> hypothesesASupprimer,
																List<Integer> hypothesesAConserver,
																List<String> nomHypothesesASupprimer,
																List<String> nomHypothesesAConserver){	
		
		List<Object> resultat = new ArrayList<Object>();
		
		hypothesesASupprimer = hypotheseDao.findByIdNonPartagee(utilisateur);
		hypothesesAConserver =  hypotheseDao.findByIdPartagee(utilisateur);
		nomHypothesesASupprimer = hypotheseDao.findByNomNonPartagee(utilisateur);
		nomHypothesesAConserver = hypotheseDao.findByNomPartagee(utilisateur);

		resultat.add(hypothesesASupprimer);
		resultat.add(hypothesesAConserver);
		resultat.add(nomHypothesesASupprimer);
		resultat.add(nomHypothesesAConserver);	
		
		return resultat;
	}

	@Override
	public int deleteHypotheseByIdListeHypothese(List<Integer> hypothesesASupprimerId) {
		return hypotheseDao.deleteHypotheseByIdListeHypothese(hypothesesASupprimerId);
	}

	@Override
	public int deleteCbHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId) {
		return hypotheseDao.deleteCbHypotheseByIdListeHypothese(hypothesesASupprimerId);

	}	
}
