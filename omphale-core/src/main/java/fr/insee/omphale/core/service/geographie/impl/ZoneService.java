package fr.insee.omphale.core.service.geographie.impl;

import static fr.insee.omphale.core.service.impl.Service.daoFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import fr.insee.omphale.core.service.geographie.ICommuneDependanceService;
import fr.insee.omphale.core.service.geographie.IZoneService;
import fr.insee.omphale.dao.geographie.IZonageDAO;
import fr.insee.omphale.dao.geographie.IZoneDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.CommuneDependance;
import fr.insee.omphale.domaine.geographie.Departement;
import fr.insee.omphale.domaine.geographie.Region;
import fr.insee.omphale.domaine.geographie.TypeZoneStandard;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.ihm.util.dataTable.ZoneAffichageDataTable;

/**
 * Classe gérant les fonctionnalités de la couche service pour les  "Zone"
 *
 */
public class ZoneService implements IZoneService {
	
	private IZoneDAO zoneDao;
	private IZonageDAO zonageDao;


	public ZoneService() {
		this.zoneDao = daoFactory.getZoneDAO();
		this.zonageDao = daoFactory.getZonageDAO();

	}

	public ZoneService(IZoneDAO zoneDAO) {
		this.zoneDao = zoneDAO;
	}
	
	public ZoneService(IZoneDAO zoneDAO, IZonageDAO zonageDao) {
		this.zoneDao = zoneDAO;
		this.zonageDao = zonageDao;
	}

	public Set<Commune> findCommunesDependantes(Zone zone,
			ICommuneDependanceService cdService) {
		// On va chercher toutes les dépendances des communes présentes
		// dans la zone
		List<Commune> communes = new ArrayList<Commune>(zone.getCommunes());
		List<CommuneDependance> listeCommunesDependance = cdService
				.findByCommunes(communes, cdService);
		// On créée un Set avec toutes les communes de toutes les dépendances
		// trouvées plus haut
		Set<Commune> retour = new HashSet<Commune>();
		if (listeCommunesDependance.size() > 0) {
			for (CommuneDependance communeDependance : listeCommunesDependance) {
				retour.addAll(communeDependance.getCommunes());
			}
		} else {
			return null;
		}
		// On enlève de ce Set toutes les communes déjà présentes dans la zone
		// (pour ne garder que les communes ajoutées par le processus de
		// dépendance)
		for (Commune commune : zone.getCommunes()) {
			if (retour.contains(commune)) {
				retour.remove(commune);
			}
		}
		if (retour.size() > 0) {
			return retour;
		} else {
			return null;
		}
	}

	public long compterZones(Utilisateur utilisateur) {
		return zoneDao.compterZones(utilisateur);
	}

	public boolean testerNomZone(Utilisateur utilisateur, String nomZone) {
		for (Zone zone : zoneDao.findByUtilisateur(utilisateur)) {
			if (nomZone.equals(zone.getNom())) {
				return false;
			}
		}
		return true;
	}

	public boolean insertOrUpdate(Zone zone) {
		try {
			zoneDao.insertOrUpdate(zone);
			return true;
		} catch (Exception e) {
			return false;
		}
	}


	
	public int findNbCommunesParDepartement(Zone zone, Departement departement) {
		if (!zone.getDepartementsImpactes().contains(departement)) {
			return 0;
		} else {
			int resultat = 0;
			for (Commune commune : zone.getCommunes()) {
				if (commune.getDepartement().equals(departement)) {
					resultat++;
				}
			}
			return resultat;
		}
	}

	public Zone findById(String id) {
		return zoneDao.findById(id);
	}

	public List<Zone> filtrerParRegionEtType(Region region,
			TypeZoneStandard type, List<Zone> zones) {
		List<Zone> resultat = new ArrayList<Zone>();
		if (type == null) {
			if (region == null) {
				// type=null & region=null
				resultat = zones;
			} else {
				// type=null & region!=null
				for (Zone zone : zones) {
					if (zone.getRegionsImpactees().contains(region)) {
						resultat.add(zone);
					}
				}
			}
		} else {
			if (region == null) {
				// type!=null & region=null
				for (Zone zone : zones) {
					if (zone.getTypeZoneStandard().equals(type)) {
						resultat.add(zone);
					}
				}
			} else {
				// type!=null & region!=null
				for (Zone zone : zones) {
					if (zone.getTypeZoneStandard().equals(type)
							&& zone.getRegionsImpactees().contains(region)) {
						resultat.add(zone);
					}
				}
			}
		}
		return resultat;
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public List<ZoneAffichageDataTable> filtrerParRegionEtTypePourAffichage(Region region,
			TypeZoneStandard type, List<ZoneAffichageDataTable> zones) {
		List<ZoneAffichageDataTable> resultat = new ArrayList<ZoneAffichageDataTable>();
		if (type == null) {
			if (region == null) {
				// type=null & region=null
				resultat = zones;
			} else {
				// type=null & region!=null
				for (ZoneAffichageDataTable zone : zones) {
					if (zone.getRegionsImpactees().contains(region)) {
						resultat.add(zone);
					}
				}
			}
		} else {
			if (region == null) {
				// type!=null & region=null
				for (ZoneAffichageDataTable zone : zones) {
					if (zone.getTypeZoneStandard()==type.getId()) {
						resultat.add(zone);
					}
				}
			} else {
				// type!=null & region!=null
				for (ZoneAffichageDataTable zone : zones) {
					if (zone.getTypeZoneStandard()==type.getId()
							&& zone.getRegionsImpactees().contains(region.getId())) {
						resultat.add(zone);
					}
				}
			}
		}
		return resultat;
	}

	public List<Zone> findZonesStandard() {
		return zoneDao.findZonesStandard();
	}

	public List<Zone> findZonesDestandardisableByUtilisateur(
			Utilisateur utilisateur) {
		List<Zone> zonesDestandardisables = zoneDao.findZonesDestandardisable();
		List<Zone> resultat = new ArrayList<Zone>();
		for (Zone z : zonesDestandardisables) {
			if (z.getUtilisateur() == utilisateur) {
				resultat.add(z);
			}
		}
		return resultat;
	}

	public List<Zone> findByUtilisateur(Utilisateur utilisateur) {
		return zoneDao.findByUtilisateur(utilisateur);
	}

	public List<Zone> findZonesStandardisablesByUtilisateur(
			Utilisateur utilisateur) {
		List<Zone> zonesStandardisables = zoneDao.findZonesStandardisable();
		List<Zone> resultat = new ArrayList<Zone>();
		for (Zone z : zonesStandardisables) {
			if (z.getUtilisateur() == utilisateur) {
				resultat.add(z);
			}
		}
		return resultat;
	}

	public void delete(Zone zone) {	
		zoneDao.delete(zone);
	}

	public List<Zone> findZonesDisponibles(Utilisateur utilisateur) {
		List<Zone> zonesDisponibles = new ArrayList<Zone>();
		zonesDisponibles.addAll(zoneDao.findByUtilisateurEtZonesStandard(utilisateur));
		return zonesDisponibles;
	}
	
	public List<ZoneAffichageDataTable> findZonesDisponiblesPourAffichage(Utilisateur utilisateur){
		List<ZoneAffichageDataTable> zonesDisponibles = new ArrayList<ZoneAffichageDataTable>();
		zonesDisponibles.addAll(zoneDao.findByUtilisateurEtZonesStandardPourAffichage(utilisateur));
		return zonesDisponibles;
	}

	
	public List<Zone> findBylisteIdZones(List<String> listeIdZones) {
		return zoneDao.findBylisteIdZones(listeIdZones);
	}
	
	
	public List<String> findUtilisateurs(Zone zone){
		List<String> utilisateurs = new ArrayList<String>();
		@SuppressWarnings("unused")
		String idZone =zone.getId().toString();
		List<String> idZonages = zonageDao.findUtilisateurs(zone);
		
		for (String idZonage : idZonages) {
			Zonage zonage = zonageDao.findById(idZonage);
			String idUtilisateur = zonage.getUtilisateur().getId();
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

	public void setZonageDao(IZonageDAO zonageDao) {
		this.zonageDao = zonageDao;
	}
	

	


	
	public List<Object> rechercheZonesPourFonctionSuppression(		Utilisateur utilisateur,
			List<Integer> ZonesASupprimer,
			List<Integer> ZonesAConserver,
			List<String> nomZonesASupprimer,
			List<String> nomZonesAConserver){

		List<Object> resultat = new ArrayList<Object>();
		
		ZonesASupprimer = zoneDao.findByIdNonPartagee(utilisateur);
		ZonesAConserver = zoneDao.findByIdPartagee(utilisateur);
		nomZonesASupprimer = zoneDao.findByNomNonPartagee(utilisateur);
		nomZonesAConserver = zoneDao.findByNomPartagee(utilisateur);
		
		resultat.add(ZonesASupprimer);
		resultat.add(ZonesAConserver);
		resultat.add(nomZonesASupprimer);
		resultat.add(nomZonesAConserver);
		
		return resultat;
	}	

	@Override
	public int deleteDepartement_impactByListeIdZone(String nomTable, String nomChamp,List<Integer> zonesASupprimerId) {
		if(zonesASupprimerId.size()>1000){
			return traiteWhereInSuperieurAMilleElements(nomTable, nomChamp, zonesASupprimerId, zoneDao,"deleteDepartement_impactByListeIdZone");
		}else
		return zoneDao.deleteNomTableByListeId(nomTable, nomChamp,zonesASupprimerId);
	}

	@Override
	public int deleteZonesByListeIdZone(String nomTable, String nomChamp,List<Integer> zonesASupprimerId) {
		if(zonesASupprimerId.size()>1000){
			return traiteWhereInSuperieurAMilleElements(nomTable, nomChamp,zonesASupprimerId, zoneDao,"deleteZonesByListeIdZone");
		}else
		return zoneDao.deleteNomTableByListeId(nomTable, nomChamp,zonesASupprimerId);
	}

	@Override
	public int deleteCommuneDeZoneByListeIdZone(String nomTable, String nomChamp,List<Integer> zonesASupprimerId) {
		if(zonesASupprimerId.size()>1000){
			return traiteWhereInSuperieurAMilleElements(nomTable, nomChamp,zonesASupprimerId, zoneDao,"deleteCommuneDeZoneByListeIdZone");
		}else
		return zoneDao.deleteNomTableByListeId(nomTable, nomChamp,zonesASupprimerId);

	}
	
	/**
	 * utilisé lors de la suppression des objets métiers zone
	 * quand le nombre d'identifiant est supérieur à 1000
	 * le where id in () dans la requête sql de suppression ne fonctionne pas
	 * Il s'agit d'une limite du sql ou d'oracle
	 * 
	 * Cette méthode prend la liste > à 1000 et la découpe par paquet de 1000
	 * 
	 * Pour chaque paquet la requête de suppression est lancée
	 * 
	 * @param listeATraiter
	 * @param nbreElementsSouhaitesDansSousListe
	 */
	public int  traiteWhereInSuperieurAMilleElements(
														String nomTable,
														String nomChamp,
														List<Integer> listeATraiter, 
														IZoneDAO zoneDao, 
														String nomMethodeAppelee){
		
		int resultat = 0;
		
		
		int nbreElementsListeAttendus = 1000;
		
		int nbreListesATraiter = (listeATraiter.size() /nbreElementsListeAttendus);

		int modulo = (listeATraiter.size()  % nbreElementsListeAttendus);
		
		int dernierElementSousListe = 0;
		int premierElementSousListe = 0;
		
		List<Integer> sousListe = new ArrayList<Integer>();
		int numeroListe = 1;
		
		
		for(int i = 0; i < nbreListesATraiter ; i++){

			dernierElementSousListe = (nbreElementsListeAttendus * numeroListe);
			premierElementSousListe = dernierElementSousListe - nbreElementsListeAttendus;
			sousListe = listeATraiter.subList(premierElementSousListe,dernierElementSousListe);
			resultat = resultat + zoneDao.deleteNomTableByListeId(nomTable, nomChamp, sousListe);
			numeroListe++;
		}
		
		if(modulo != 0){
			int premierElementSousListeModulo = (nbreListesATraiter * nbreElementsListeAttendus);
			int dernierElementSousListeModulo = (nbreListesATraiter * nbreElementsListeAttendus) + modulo;
			sousListe = listeATraiter.subList(premierElementSousListeModulo,dernierElementSousListeModulo);
			resultat = resultat + zoneDao.deleteNomTableByListeId(nomTable, nomChamp, sousListe);
		}
		
		return resultat;

	}
	
	
}
