package fr.insee.omphale.core.service.geographie.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.insee.omphale.core.service.geographie.ICommuneService;
import fr.insee.omphale.core.service.geographie.IGroupeEtalonService;
import fr.insee.omphale.core.service.geographie.ITypeZoneStandardService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.dao.geographie.IGroupeEtalonDAO;
import fr.insee.omphale.dao.geographie.IZonageDAO;
import fr.insee.omphale.dao.projection.IProjectionDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;
import fr.insee.omphale.domaine.geographie.GroupeEtalon;
import fr.insee.omphale.domaine.geographie.GroupeEtalonId;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.geographie.ZoneClassique;


/**
 * Classe gérant les fonctionnalités de la couche service pour les "GroupeEtalon"
 *
 */
public class GroupeEtalonService implements IGroupeEtalonService {

	private IGroupeEtalonDAO groupeEtalonDao;
	@SuppressWarnings("unused")
	private IProjectionDAO projectionDao;
	@SuppressWarnings("unused")
	private IZonageDAO zonageDao;
	
	public GroupeEtalonService() {
		this.groupeEtalonDao = daoFactory.getGroupeEtalonDAO();
		this.projectionDao = daoFactory.getProjectionDAO();
		this.zonageDao = daoFactory.getZonageDAO();
	}

	public GroupeEtalonService(IGroupeEtalonDAO groupeDAO) {
		this.groupeEtalonDao = groupeDAO;
	}
	
	public GroupeEtalonService(IGroupeEtalonDAO groupeDAO, IProjectionDAO projectionDao, IZonageDAO zonageDao) {
		this.groupeEtalonDao = groupeDAO;
		this.projectionDao = projectionDao;
		this.zonageDao = zonageDao;
	}

	public List<GroupeEtalon> creerGroupesEtalon(Zonage zonage, int idMax) {
		List<GroupeEtalon> resultat = new ArrayList<GroupeEtalon>();

		List<Zone> zonesLibres = new ArrayList<Zone>(zonage.getZones());
		int numeroGroupeEtalon = idMax + 1;
		while (!zonesLibres.isEmpty()) {
			Zone zoneTraitee = zonesLibres.get(0);
			Set<Zone> freres = new HashSet<Zone>();
			freres = trouverTousFreres(zoneTraitee, zonesLibres);
			GroupeEtalon groupeEtalon = new GroupeEtalon();
			GroupeEtalonId id = new GroupeEtalonId();
			id.setZonage(zonage);
			id.setSignature("G_" + numeroGroupeEtalon);
			numeroGroupeEtalon++;
			groupeEtalon.setId(id);
			groupeEtalon.setZones(freres);
			Set<Departement> departements = new HashSet<Departement>();
			if (freres != null && !freres.isEmpty()){
				for (Zone zone : freres) {
					departements.addAll(zone.getDepartementsImpactes());
				}
			}
			groupeEtalon.setDepartements(departements);
			resultat.add(groupeEtalon);
			if (freres != null && !freres.isEmpty()){
				zonesLibres.removeAll(freres);
			}
			
		}
		return resultat;
	}

	public List<Commune> findCommunesResiduelles(GroupeEtalon groupeEtalon,
			ICommuneService communeService) {
		List<Commune> communesGroupeEtalon = new ArrayList<Commune>();
		List<Commune> communesGroupeEtalonComplet = new ArrayList<Commune>();

		// On récupère les communes présentes dans le groupe étalon
		for (Zone zone : groupeEtalon.getZones()) {
			communesGroupeEtalon.addAll(zone.getCommunes());
		}

		// On récupère toutes les communes de tous les départements
		// impactés par le groupe étalon
		for (Departement departement : groupeEtalon.getDepartements()) {
			communesGroupeEtalonComplet.addAll(communeService
					.findAllByDepartement(departement));
		}

		communesGroupeEtalonComplet.removeAll(communesGroupeEtalon);
		
		return communesGroupeEtalonComplet.isEmpty() ? null
				: communesGroupeEtalonComplet;
	}

	public Set<Zone> trouverFreresProches(Zone zone, List<Zone> zones) {
		Set<Zone> resultat = new HashSet<Zone>();
		for (Departement departement : zone.getDepartementsImpactes()) {
			for (Zone zoneCompare : zones) {
				if (zoneCompare.getDepartementsImpactes().contains(departement)) {
					resultat.add(zoneCompare);
				}
			}
		}
		return resultat.isEmpty() ? null : resultat;
	}

	public Set<Zone> trouverTousFreres(Zone zone, List<Zone> zones) {
		Set<Zone> resultat = trouverFreresProches(zone, zones);
		if (resultat == null) {
			return null;
		}
		int nbFreres = resultat.size();
		boolean freresTrouves = false;
		while (!freresTrouves) {
			Set<Zone> freresActuels = new HashSet<Zone>(resultat);
			for (Zone frere : freresActuels) {
				resultat.addAll(trouverFreresProches(frere, zones));
			}
			if (nbFreres == resultat.size()) {
				freresTrouves = true;
			} else {
				nbFreres = resultat.size();
			}
		}
		return resultat.isEmpty() ? null : resultat;
	}

	public GroupeEtalon insertOrUpdate(GroupeEtalon groupeEtalon) {
		return groupeEtalonDao.insertOrUpdate(groupeEtalon);
	}

	public boolean delete(GroupeEtalon groupeEtalon) {
		try {
			groupeEtalonDao.delete(groupeEtalon);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<GroupeEtalon> findByZonage(Zonage zonage) {
		return groupeEtalonDao.findByZonage(zonage);
	}

	public GroupeEtalon findById(GroupeEtalonId id) {
		return groupeEtalonDao.findById(id);
	}

	public Zonage addZoneResiduelle(String nomZoneRes ,Zonage zonage, GroupeEtalon groupeEtalon,
			ITypeZoneStandardService typeZoneStandardService,
			IZoneService zoneService, IGroupeEtalonService groupeEtalonService) {

		String departements = "";
		for (Departement dept : groupeEtalon.getDepartements()) {
			departements = departements + "_" + dept.getId();
		}


		
		if (departements.length() >= 20) {
			departements = departements.substring(0, 19);
		}

		String nomZoneResiduelle = null;
		if (nomZoneRes.isEmpty()){
			String nomZonageTronque = zonage.getNom().length() > 20 - 
			departements.length() ? zonage.getNom().substring(0,
					20 - departements.length()) : zonage.getNom();
			nomZoneResiduelle = nomZonageTronque + departements;
		} else {
			nomZoneResiduelle = nomZoneRes;
		}
		
		String departementsLibelle = "";
		for (Departement dept : groupeEtalon.getDepartements()) {
			departementsLibelle = departementsLibelle + " " + dept.getId();
		}
		
		@SuppressWarnings("unused")
		String departementsLibelleTronque = null;
		if (departementsLibelle.length() > 25){
			departementsLibelleTronque = departementsLibelle.substring(0,25);
		} else {
			departementsLibelleTronque = departementsLibelle;
		}
		String nomZonagePourLibelle = null;
		if (zonage.getNom().length() > 20){
			nomZonagePourLibelle = zonage.getNom().substring(0,20);
		} else {
			nomZonagePourLibelle = zonage.getNom();
		}

				
		Set<Departement> departementsImpactes = new HashSet<Departement>();
		for (Commune commune : groupeEtalon.getCommunesResiduelles()) {
			departementsImpactes.add(commune.getDepartement());
		}
		
		String zoneLibelleCorrige = "";
		if( ("CR " + nomZonagePourLibelle + " " + departementsLibelle).length()>=100){
			zoneLibelleCorrige = "CR " + nomZonagePourLibelle ;
		}else{
			zoneLibelleCorrige = "CR " + nomZonagePourLibelle + " " + departementsLibelle;
		}
		
		
		Zone zoneResiduelle = new ZoneClassique(null, nomZoneResiduelle, 
				zoneLibelleCorrige, zonage.getUtilisateur(), typeZoneStandardService.findById(0),
				departementsImpactes, groupeEtalon.getCommunesResiduelles());

		zoneService.insertOrUpdate(zoneResiduelle);
		groupeEtalon.setCommunesResiduelles(null);
		groupeEtalon.getZones().add(zoneResiduelle);
		groupeEtalonService.insertOrUpdate(groupeEtalon);

		zonage.getZones().add(zoneResiduelle);
		return zonage;
	}
	
	
	public List<Object> rechercheGroupesEtalonsPourFonctionSuppression(	Utilisateur utilisateur,
																	List<String> groupesEtalonsASupprimer,
																	List<String> groupesEtalonsAConserver){

		List<Object> resultat = new ArrayList<Object>();

		groupesEtalonsASupprimer = groupeEtalonDao.findByIdNonPartagee(utilisateur);
		groupesEtalonsAConserver = groupeEtalonDao.findByIdPartagee(utilisateur);
		
		resultat.add(groupesEtalonsASupprimer);
		resultat.add(groupesEtalonsAConserver);
		
		return resultat;
	}

	@Override
	public int deleteZoneDeGroupetByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId) {
		return groupeEtalonDao.deleteZoneDeGroupetByListeIdGroupeEtalon(groupesEtalonsASupprimerId);
	}

	@Override
	public int deleteCommuneResiduelleByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId) {
		return groupeEtalonDao.deleteCommuneResiduelleByListeIdGroupeEtalon(groupesEtalonsASupprimerId);
	}

	@Override
	public int deletedept_de_groupetByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId) {
		return groupeEtalonDao.deletedept_de_groupetByListeIdGroupeEtalon(groupesEtalonsASupprimerId);
	}

	@Override
	public int deleteGroupeEtalonByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId) {
		return groupeEtalonDao.deleteGroupeEtalonByListeIdGroupeEtalon(groupesEtalonsASupprimerId);
	}
}
