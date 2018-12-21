package fr.insee.omphale.core.service.geographie.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.insee.omphale.core.service.geographie.ICommuneDependanceService;
import fr.insee.omphale.core.service.geographie.ICommuneService;
import fr.insee.omphale.core.service.geographie.IGroupeEtalonService;
import fr.insee.omphale.core.service.geographie.IZonageService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.core.service.impl.Service;
import fr.insee.omphale.dao.geographie.IGroupeEtalonDAO;
import fr.insee.omphale.dao.geographie.IZonageDAO;
import fr.insee.omphale.dao.projection.IProjectionDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;
import fr.insee.omphale.domaine.geographie.EEtatValidation;
import fr.insee.omphale.domaine.geographie.GroupeEtalon;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.service.comparator.ZonageComparator;


/**
 * Classe gérant les fonctionnalités de la couche service pour les  "Zonage"
 *
 */
public class ZonageService implements IZonageService {

	private IZonageDAO zonageDao;
	@SuppressWarnings("unused")
	private IProjectionDAO projectionDao;

	public ZonageService() {
		this.zonageDao = daoFactory.getZonageDAO();
		this.projectionDao = daoFactory.getProjectionDAO();
	}

	public ZonageService(IZonageDAO zonageDAO) {
		this.zonageDao = zonageDAO;
	}
	
	public ZonageService(IZonageDAO zonageDAO, IProjectionDAO projectionDao) {
		this.zonageDao = zonageDAO;
		this.projectionDao = projectionDao;
	}
	
	public ZonageService(IZonageDAO zonageDAO, IGroupeEtalonDAO groupeEtalonDao) {
		this.zonageDao = zonageDAO;
	}

	public boolean testerNomZonage(Utilisateur utilisateur, String nomZonage) {
		List<Zonage> zonages = zonageDao.findByUtilisateur(utilisateur);
		if (zonages == null) {
			return true;
		}
		for (Zonage zonage : zonages) {
			if (nomZonage.equals(zonage.getNom())) {
				return false;
			}
		}
		return true;
	}

	public Zonage insertOrUpdate(Zonage zonage) {
		return zonageDao.insertOrUpdate(zonage);
	}

	public List<Zonage> findByUtilisateur(Utilisateur utilisateur) {
		return zonageDao.findByUtilisateur(utilisateur);
	}

	public Zonage findById(String id) {
		return zonageDao.findById(id);
	}

	public void delete(Zonage zonage) {
		zonageDao.delete(zonage);
	}

	public List<Zonage> findByZone(Zone zone, IZonageService zonageService) {
		List<Zonage> zonages = zonageService.findByUtilisateur(zone
				.getUtilisateur());
		if (zonages == null) {
			return null;
		}
		List<Zonage> resultat = new ArrayList<Zonage>();
		for (Zonage zonage : zonages) {
			if (zonage.getZones().contains(zone)) {
				resultat.add(zonage);
			}
		}
		return (resultat.size() == 0 ? null : resultat);
	}

	public long compterZonages(Utilisateur utilisateur) {
		return zonageDao.compterZonages(utilisateur);
	}

	public Map<String, String> verifierZonesDisjointes(Zonage zonage,
			IZoneService zoneService) {
		Map<String, Set<String>> resultat = new HashMap<String, Set<String>>();
		// On créée une map qui pour chaque zone, nous donne la liste des
		// communes associées
		Map<String, List<String>> zonesEtCommunes = new HashMap<String, List<String>>();
		for (Zone zone : zonage.getZones()) {
			List<String> idCommunes = new ArrayList<String>();
			for (Commune commune : zone.getCommunes()) {
				idCommunes.add(commune.getId());
			}
			zonesEtCommunes.put(zone.getId(), idCommunes);
		}

		for (String idZone : zonesEtCommunes.keySet()) {
			List<String> idCommunes = zonesEtCommunes.get(idZone);
			for (String idZoneCompare : zonesEtCommunes.keySet()) {
				if (!idZone.equals(idZoneCompare)) {
					// on va comparer la liste des communes pour chaque zone,
					// sauf celle en cours
					List<String> idCommunesCompare = zonesEtCommunes
							.get(idZoneCompare);
					for (String idCommune : idCommunes) {
						for (String idCommuneCompare : idCommunesCompare) {
							if (idCommune.equals(idCommuneCompare)) {
								if (resultat.get(idCommune) == null) {
									resultat.put(idCommune,
											new HashSet<String>());
								}
								resultat.get(idCommune).add(idZone);
								resultat.get(idCommune).add(idZoneCompare);
							}
						}
					}
				}
			}
		}
		Map<String, String> resultat2 = new HashMap<String, String>();
		for (String idCommune : resultat.keySet()) {
			String listeNomsZones = "";
			for (String idZone : resultat.get(idCommune)) {
				listeNomsZones = listeNomsZones + ","
						+ zoneService.findById(idZone.toString()).getNom();
			}
			resultat2.put(idCommune.toString(), listeNomsZones.substring(1));
		}
		return resultat2.isEmpty() ? null : resultat2;
	}

	/**
	 * Méthode qui valide un zonage
	 */
	public Map<String, Object> validerZonage(Zonage zonage,
			IZoneService zoneService, IGroupeEtalonService groupeEtalonService,
			ICommuneService communeService,
			ICommuneDependanceService cdService, IZonageService zonageService, Service service)
	{
		// clés de la map de retour
		String cleDependances = "dependances";
		String cleIntersectionZones = "intersectionZones";

		// Maps servant à rendre compte du déroulement de la validation :
		Map<String, String> dependancesAjouteesParZone = new HashMap<String, String>();
		Map<String, String> zonesNonDisjointesParCommune = new HashMap<String, String>();

		// Etape 0 : on supprime les groupes étalon eventuellement déja créé par
		// une validation précédente (échouée ou réussie)
		List<GroupeEtalon> groupesEtalonsPrecedents = groupeEtalonService
				.findByZonage(zonage);
		Set<Integer> idGroupesEtalons = new HashSet<Integer>();
		for (GroupeEtalon ge : groupesEtalonsPrecedents) {
			idGroupesEtalons.add(Integer.valueOf(ge.getId().getSignature()
					.substring(2)));
			groupeEtalonService.delete(ge);
		}
		int idMax = Service.getMax(idGroupesEtalons);

		// Etape 1 : on actualise les zones en fonction des dépendances
		Set<Zone> zonesActualisees = new HashSet<Zone>();
		for (Zone zone : zonage.getZones()) {
			Set<Commune> communes = zone.getCommunes();
			Set<Commune> communesSupplementaires = zoneService
					.findCommunesDependantes(zone, cdService);
			if (communesSupplementaires != null) {
				communes.addAll(communesSupplementaires);
				String communesSupp = "";
				for (Commune com : communesSupplementaires) {
					communesSupp = communesSupp + "," + com.getId();
				}
				dependancesAjouteesParZone.put(zone.getNom(), communesSupp
						.substring(1));
			}
			zone.setCommunes(communes);
			zonesActualisees.add(zone);
		}
		zonage.setZones(zonesActualisees);

		// Etape 2 : on recréée les départements impactés des zones
		for (Zone zone : zonage.getZones()) {
			Set<Departement> departementsImpactes = new HashSet<Departement>();
			for (Commune commune : zone.getCommunes()) {
				departementsImpactes.add(commune.getDepartement());
			}
			zone.setDepartementsImpactes(departementsImpactes);
		}

		// Etape 3 : on vérifie que les zones sont disjointes
		zonesNonDisjointesParCommune = zonageService.verifierZonesDisjointes(zonage,
				zoneService);

		if (zonesNonDisjointesParCommune != null) {
			zonage.setEtatValidation(EEtatValidation.NON_VALIDE);
			Map<String, Object> resultat = new HashMap<String, Object>();
			zonageDao.insertOrUpdate(zonage);
			resultat.put(cleDependances, dependancesAjouteesParZone);
			resultat.put(cleIntersectionZones, zonesNonDisjointesParCommune);
			resultat.put("zonage", zonage);
			return resultat;
		} else {

			// Etape 4 : On créée les groupes de départements étalon
			List<GroupeEtalon> groupesEtalons = groupeEtalonService
					.creerGroupesEtalon(zonage, idMax);


			// Etape 5 : On recherche les communes résiduelles

			boolean pasDeCommunesResiduelles = true;
			for (GroupeEtalon groupeEtalon : groupesEtalons) {
				List<Commune> communesResiduelles = groupeEtalonService
						.findCommunesResiduelles(groupeEtalon, communeService);
				if (communesResiduelles != null) {
					groupeEtalon.setCommunesResiduelles(new HashSet<Commune>(
							communesResiduelles));
					pasDeCommunesResiduelles = false;
				}
			}

			// Etape 6 : On Sauvegarde les groupes étalons

			for (GroupeEtalon groupeEtalon : groupesEtalons) {
				groupeEtalonService.insertOrUpdate(groupeEtalon);
			}

			// Etape 7 : On valide le zonage (ou non...)
			Map<String, Object> resultat = new HashMap<String, Object>();
			resultat.put(cleDependances, dependancesAjouteesParZone);
			if (pasDeCommunesResiduelles) {
				zonage.setEtatValidation(EEtatValidation.VALIDE);
				resultat.put("zonage", zonage);
				return resultat;
			} else {
				zonage.setEtatValidation(EEtatValidation.NON_VALIDE);
				resultat.put("zonage", zonage);
				return resultat;
			}
		}
	}
	
	/**
	 * Méthode qui valide un zonage
	 */
	public Map<String, Object> validerZonageOld(Zonage zonage,
			IZoneService zoneService, IGroupeEtalonService groupeEtalonService,
			ICommuneService communeService,
			ICommuneDependanceService cdService, IZonageService zonageService) {

		// clés de la map de retour
		String cleDependances = "dependances";
		String cleIntersectionZones = "intersectionZones";

		// Maps servant à rendre compte du déroulement de la validation :
		Map<String, String> dependancesAjouteesParZone = new HashMap<String, String>();
		Map<String, String> zonesNonDisjointesParCommune = new HashMap<String, String>();

		// Etape 0 : on supprime les groupes étalon eventuellement déja créé par
		// une validation précédente (échouée ou réussie)
		List<GroupeEtalon> groupesEtalonsPrecedents = groupeEtalonService
				.findByZonage(zonage);
		Set<Integer> idGroupesEtalons = new HashSet<Integer>();
		for (GroupeEtalon ge : groupesEtalonsPrecedents) {
			idGroupesEtalons.add(Integer.valueOf(ge.getId().getSignature()
					.substring(2)));
			groupeEtalonService.delete(ge);
		}
		int idMax = Service.getMax(idGroupesEtalons);

		// Etape 1 : on actualise les zones en fonction des dépendances
		Set<Zone> zonesActualisees = new HashSet<Zone>();
		for (Zone zone : zonage.getZones()) {
			Set<Commune> communes = zone.getCommunes();
			Set<Commune> communesSupplementaires = zoneService
					.findCommunesDependantes(zone, cdService);
			if (communesSupplementaires != null) {
				communes.addAll(communesSupplementaires);
				String communesSupp = "";
				for (Commune com : communesSupplementaires) {
					communesSupp = communesSupp + "," + com.getId();
				}
				dependancesAjouteesParZone.put(zone.getNom(), communesSupp
						.substring(1));
			}
			zone.setCommunes(communes);
			zonesActualisees.add(zone);
		}
		zonage.setZones(zonesActualisees);

		// Etape 2 : on recréée les départements impactés des zones
		for (Zone zone : zonage.getZones()) {
			Set<Departement> departementsImpactes = new HashSet<Departement>();
			for (Commune commune : zone.getCommunes()) {
				departementsImpactes.add(commune.getDepartement());
			}
			zone.setDepartementsImpactes(departementsImpactes);
		}

		// Etape 3 : on vérifie que les zones sont disjointes
		zonesNonDisjointesParCommune = zonageService.verifierZonesDisjointes(zonage,
				zoneService);

		if (zonesNonDisjointesParCommune != null) {
			zonage.setEtatValidation(EEtatValidation.NON_VALIDE);
			Map<String, Object> resultat = new HashMap<String, Object>();
			zonageDao.insertOrUpdate(zonage);
			resultat.put(cleDependances, dependancesAjouteesParZone);
			resultat.put(cleIntersectionZones, zonesNonDisjointesParCommune);
			resultat.put("zonage", zonage);
			return resultat;
		} else {

			// Etape 4 : On créée les groupes de départements étalon
			List<GroupeEtalon> groupesEtalons = groupeEtalonService
					.creerGroupesEtalon(zonage, idMax);

			// Etape 5 : On recherche les communes résiduelles
			boolean pasDeCommunesResiduelles = true;
			for (GroupeEtalon groupeEtalon : groupesEtalons) {
				List<Commune> communesResiduelles = groupeEtalonService
						.findCommunesResiduelles(groupeEtalon, communeService);
				if (communesResiduelles != null) {
					groupeEtalon.setCommunesResiduelles(new HashSet<Commune>(
							communesResiduelles));
					pasDeCommunesResiduelles = false;
				}
			}

			// Etape 6 : On Sauvegarde les groupes étalons

			for (GroupeEtalon groupeEtalon : groupesEtalons) {
				groupeEtalonService.insertOrUpdate(groupeEtalon);
			}

			// Etape 7 : On valide le zonage (ou non...)
			Map<String, Object> resultat = new HashMap<String, Object>();
			resultat.put(cleDependances, dependancesAjouteesParZone);
			if (pasDeCommunesResiduelles) {
				zonage.setEtatValidation(EEtatValidation.VALIDE);
				resultat.put("zonage", zonage);
				return resultat;
			} else {
				zonage.setEtatValidation(EEtatValidation.NON_VALIDE);
				resultat.put("zonage", zonage);
				return resultat;
			}
		}
	}

	public List<Zonage> findNonValidesByUtilisateur(Utilisateur utilisateur,
			IZonageService zonageService) {
		List<Zonage> zonagesUtilisateur = zonageService
				.findByUtilisateur(utilisateur);
		List<Zonage> resultat = new ArrayList<Zonage>();
		for (Zonage zonage : zonagesUtilisateur) {
			if (zonage.getEtatValidation() == EEtatValidation.NON_VALIDE) {
				resultat.add(zonage);
			}
		}
		return resultat.isEmpty() ? null : resultat;
	}

	public List<Zonage> findValidesByUtilisateur(Utilisateur utilisateur,
			IZonageService zonageService) {
		List<Zonage> zonagesUtilisateur = zonageService
				.findByUtilisateur(utilisateur);
		List<Zonage> resultat = new ArrayList<Zonage>();
		for (Zonage zonage : zonagesUtilisateur) {
			if (zonage.getEtatValidation() == EEtatValidation.VALIDE) {
				resultat.add(zonage);
			}
		}
		return resultat.isEmpty() ? null : resultat;
	}
	
	public void triZonageParOrdreAlphabetique(List<Zonage> zonages){
		Collections.sort(zonages, new ZonageComparator());
	}

	public List<Zonage> findStandards() {
		return null;
	}
	
	public List<Zonage> findByZone(Zone zone){		
		return zonageDao.findByZone(zone);
	}
	
	public List<Zonage> findByZone_ZONE_DE_ZONAGE(Zone zone){
		return zonageDao.findByZone_ZONE_DE_ZONAGE(zone);
	}

	public List<Zonage> findByZone_ZONE_DE_GROUPET(Zone zone){
		return zonageDao.findByZone_ZONE_DE_GROUPET(zone);
	}
	
	
	public List<Object> rechercheZonagesPourFonctionSuppression(	Utilisateur utilisateur,
																	List<Integer> zonagesASupprimer,
																	List<Integer> zonagesAConserver,
																	List<String> nomZonagesASupprimer,
																	List<String> nomZonagesAConserver){

			List<Object> resultat = new ArrayList<Object>();
	
			zonagesASupprimer = zonageDao.findByIdNonPartagee(utilisateur);
			zonagesAConserver = zonageDao.findByIdPartagee(utilisateur);
			nomZonagesASupprimer = zonageDao.findByNomNonPartagee(utilisateur);
			nomZonagesAConserver = zonageDao.findByNomPartagee(utilisateur);
			
			resultat.add(zonagesASupprimer);
			resultat.add(zonagesAConserver);
			resultat.add(nomZonagesASupprimer);
			resultat.add(nomZonagesAConserver);
			
			return resultat;
}
	


	@Override
	public int deleteZoneDeZonageByListeIdZonage(List<Integer> zonagesASupprimerId) {
		return zonageDao.deleteZoneDeZonageByListeIdZonage(zonagesASupprimerId) ;
	}

	@Override
	public int deleteZonageByListeIdZonage(List<Integer> zonagesASupprimerId) {
		return zonageDao.deleteZonageByListeIdZonage(zonagesASupprimerId) ;
	}
}
