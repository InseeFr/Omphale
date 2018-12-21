package fr.insee.omphale.core.service.projection.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.insee.omphale.core.service.projection.IEvolutionLocaliseeService;
import fr.insee.omphale.dao.projection.IEvolutionLocaliseeDAO;
import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.EvolutionLocalisee;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Projection;

/**
 * Classe pour gérer les fonctionnalités de la couche service pour EvolutionLocalisee
 *
 */
public class EvolutionLocaliseeService implements IEvolutionLocaliseeService {

	private IEvolutionLocaliseeDAO elDao = daoFactory
			.getEvolutionLocaliseeDAO();
	
	public EvolutionLocaliseeService(){
		this.elDao = daoFactory.getEvolutionLocaliseeDAO();
	}
	
	public EvolutionLocaliseeService(IEvolutionLocaliseeDAO elDao){
		this.elDao = elDao;
	}

	
	public Set<EvolutionLocalisee> findAll(Integer[] listeIdentifiantEL) {	
		if (listeIdentifiantEL  != null && listeIdentifiantEL.length > 0) {
			Set<EvolutionLocalisee> liste = new HashSet<EvolutionLocalisee>(listeIdentifiantEL.length);
			for (int i = 0; i < listeIdentifiantEL.length; i++) {
				liste.add(elDao.getById(listeIdentifiantEL[i]));
			}
			return liste;
		}
		else {
			return new HashSet<EvolutionLocalisee>(0);
		}
	}
	
	public void advance(Set<EvolutionLocalisee> evolutionsLocalisees,  List<Integer> evolutionsLocaliseesSelectionnees, String modifierDisposition){
		if (evolutionsLocalisees != null && !evolutionsLocalisees.isEmpty() &&
			evolutionsLocaliseesSelectionnees != null && !evolutionsLocaliseesSelectionnees.isEmpty()) {
			Collections.sort(evolutionsLocaliseesSelectionnees);
			
			int pasDeRangAEffectuer = 0;
			if (modifierDisposition.equals("monter")) {	
				pasDeRangAEffectuer = -1;
			}else if (modifierDisposition.equals("descendre")){
				pasDeRangAEffectuer = 1;
			}
			
			for (Integer id :evolutionsLocaliseesSelectionnees){
				EvolutionLocalisee el = elDao.findById(id);
				int rang = el.getRang();
				if (rang >=1 && rang <= evolutionsLocalisees.size()){
					List<EvolutionLocalisee> listeElBcle = new ArrayList<EvolutionLocalisee>();
					listeElBcle.addAll(evolutionsLocalisees);
					int rangProche = rang + pasDeRangAEffectuer;
					EvolutionLocalisee elProche = null;
					for (EvolutionLocalisee elbcle : listeElBcle){
						if (elbcle.getRang() == rangProche){
							elProche = elbcle;
							elProche.setRang(rang);
							el.setRang(rangProche);
							elDao.insertOrUpdate(el);
							elDao.insertOrUpdate(elProche);
							elDao.nettoyage();
						} 
					}
				}
			}
		}
	}
	

	public EvolutionLocalisee findById(int id) {
		return elDao.findById(id);
	}
	
	public  EvolutionLocalisee findByRang(Projection projection, Composante composante, int rang ){
		return elDao.findByRang(projection, composante, rang);
	}
	
	public List<EvolutionLocalisee> findByENL(EvolutionNonLocalisee enl){
		return elDao.findByENL(enl);
	}
	
	public EvolutionLocalisee insertOrUpdate(EvolutionLocalisee evolutionLocalisee){
		return elDao.insertOrUpdate(evolutionLocalisee);
	}
	
	public void delete(EvolutionLocalisee evolutionLocalisee){
		elDao.delete(evolutionLocalisee);
	}
	
	public Set<EvolutionLocalisee> findByProjection(Projection projection){
		return elDao.findByProjection(projection);
	}
	
	public Projection supprimerListeEvolutionLocalisees(Projection projection) {
		Set<EvolutionLocalisee> elsASupprimer = findByProjection(projection);
		if( !elsASupprimer.isEmpty()){
			for (EvolutionLocalisee el : elsASupprimer){
				delete(el);
			}
		projection.setEvolutionsLocalisees(null);
		}
		return projection;
	}
	
	public void supprimer(EvolutionLocalisee evolutionLocalisee){
		elDao.delete(evolutionLocalisee);
	}
	
	
	public List<EvolutionLocalisee> findAll(){
		return elDao.findAll();
	}
	
	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer){
		return elDao.deleteByListIdProjection(IdsProjectionsASupprimer);
	}
	
	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId){
		return elDao.deleteByListeIdEvolutionNonLocalisee(evolutionsNonLocaliseesASupprimerId);
	}

}
