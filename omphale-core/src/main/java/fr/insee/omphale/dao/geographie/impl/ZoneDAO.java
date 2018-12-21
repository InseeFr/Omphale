package fr.insee.omphale.dao.geographie.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.core.service.geographie.ITypeZoneStandardService;
import fr.insee.omphale.core.service.geographie.impl.TypeZoneStandardService;
import fr.insee.omphale.core.service.impl.Service;
import fr.insee.omphale.dao.geographie.IZoneDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.ihm.util.ParametresMessages;
import fr.insee.omphale.ihm.util.dataTable.ZoneAffichageDataTable;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour Zone
 *
 */
public class ZoneDAO extends GenericDAO<Zone, String> implements IZoneDAO {
	
	public long compterZones(Utilisateur utilisateur) {
		Session session = getSession();
		try {
			String select = "select count(*) from Zone z where z.utilisateur = :utilisateur";
			return (Long) session.createQuery(select).setParameter("utilisateur", utilisateur).uniqueResult();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Zone> findByUtilisateur(Utilisateur utilisateur) {
		Session session = getSession();
		try {
			String select = "from Zone z where z.utilisateur = :utilisateur order by z.nom";
			return session.createQuery(select).setParameter(
					"utilisateur", utilisateur).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Zone> findZonesStandard() {
		Session session = getSession();
		ITypeZoneStandardService typeService = new TypeZoneStandardService();
		try {
			String select = "from Zone z where z.typeZoneStandard != :typeUtilisateur";
			return session.createQuery(select).setParameter(
					"typeUtilisateur", typeService.findById(0)).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Zone> findByUtilisateurEtZonesStandard(Utilisateur utilisateur){
		Session session = getSession();
		ITypeZoneStandardService typeService = new TypeZoneStandardService();
		try {
			String select = "from Zone z where z.typeZoneStandard != :typeUtilisateur or  z.utilisateur = :utilisateur order by z.nom";
			return session.createQuery(select)
					.setParameter("typeUtilisateur", typeService.findById(0))
					.setParameter("utilisateur", utilisateur).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ZoneAffichageDataTable> findByUtilisateurEtZonesStandardPourAffichage(Utilisateur utilisateur){
		Session session = getSession();
		ITypeZoneStandardService typeService = new TypeZoneStandardService();
		try {
			String select = "select  zone.id_zone id, zone.nom , zone.libelle, zone.type_zone_standard typeZoneStandard, reg_impact.id_region regionImpactee  from "
					+ "zone zone "
					+ "     inner join"
					+ " departement_impact dep_impact "
					+ "  on (zone.id_zone = dep_impact.zone)"
					+ " inner join"
					+ " departement reg_impact "
					+ " on (dep_impact.dept =  reg_impact.id_dept) "
					+ " where zone.TYPE_ZONE_STANDARD<> :typeUtilisateur or  zone.id_user = :utilisateur order by zone.nom";
			Query q = session.createSQLQuery(select)
					.addEntity(ZoneAffichageDataTable.class)					
					.setParameter("typeUtilisateur", typeService.findById(0).getId())
					.setParameter("utilisateur", utilisateur.getId());
			
			List<ZoneAffichageDataTable> resultatsAvecRedondance = q
					.list();
			
			String idZonePrecedent = null;
			ZoneAffichageDataTable zoneFinaleActuelle = null;
			ZoneAffichageDataTable zoneFinalePrecedente = null;
			List<ZoneAffichageDataTable> resultatsFinaux = new ArrayList<ZoneAffichageDataTable>();
			
			for (ZoneAffichageDataTable zoneAvecRedondance : resultatsAvecRedondance) {
				if(idZonePrecedent == null || idZonePrecedent != zoneAvecRedondance.getId()){
					if(idZonePrecedent != null){
						zoneFinalePrecedente = zoneFinaleActuelle;
					}
					
					zoneFinaleActuelle = new ZoneAffichageDataTable(	zoneAvecRedondance.getId(), 
																		zoneAvecRedondance.getNom(), 
																		zoneAvecRedondance.getLibelle(), 
																		zoneAvecRedondance.getTypeZoneStandard(), 
																		null);
					
					zoneFinaleActuelle.setRegionsImpactees(new HashSet<String>());
					zoneFinaleActuelle.getRegionsImpactees().add(zoneAvecRedondance.getRegionImpactee());
					
					if(idZonePrecedent != null){
						resultatsFinaux.add(zoneFinalePrecedente);
					}
					
					idZonePrecedent = zoneAvecRedondance.getId();
				}
				else if(  idZonePrecedent == zoneAvecRedondance.getId()){
					zoneFinaleActuelle.getRegionsImpactees().add(zoneAvecRedondance.getRegionImpactee());
				}
			}
			
			
			return resultatsFinaux;
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Zone> findZonesDestandardisable() {
		Session session = getSession();
		ITypeZoneStandardService typeService = new TypeZoneStandardService();
		try {
			String select = "from Zone z where z.typeZoneStandard = :typeUtilisateur1 " +
											"or z.typeZoneStandard = :typeUtilisateur2";
			return session.createQuery(select).setParameter("typeUtilisateur1", typeService.findById(4))
												.setParameter("typeUtilisateur2", typeService.findById(5))
												.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Zone> findZonesStandardisable() {
		Session session = getSession();
		ITypeZoneStandardService typeService = new TypeZoneStandardService();
		try {
			String select = "from Zone z where z.typeZoneStandard = :typeUtilisateur";
			return session.createQuery(select).setParameter(
					"typeUtilisateur", typeService.findById(0))
					.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	public List<Zone> findBylisteIdZones(List<String> listeIdZones) {
		List<Zone> retour = new ArrayList<Zone>();
		int nbPaquets = Service.calculNbPaquets(listeIdZones);
		for (int j = 0; j < nbPaquets; j++) {
			List<String> sousListe = listeIdZones.subList(
					Service.NB_MAX_LISTE_ORACLE * j, Math.min(
							Service.NB_MAX_LISTE_ORACLE * (j + 1),
							listeIdZones.size()));
			retour.addAll(findByIdSousListe(sousListe));

		}
		return retour;
	}

	@SuppressWarnings("unchecked")
	private List<Zone> findByIdSousListe(
			List<String> listeIdZones) {
		Session session = getSession();
		try {
			String select = "from Zone z where z.id in (:listeId)";
			return session.createQuery(select)
					.setParameterList("listeId", listeIdZones).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Zone> rechercheZonesUtilisateurUtiliseesParDautres(Utilisateur utilisateur){
		Session session = getSession();
		try {
			
			String select ="select zdz from Zonage as zage fetch all properties inner join zage.zones as zdz " +
                                  " where zdz.utilisateur = :utilisateur "+
                                  " and zage.utilisateur != :utilisateur ";
			return session.createQuery(select).setParameter(
					"utilisateur", utilisateur).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Zone> findZonesPersonnelsUtiliseesQueParCreateur(Utilisateur utilisateur){
		Session session = getSession();
		try {
			String select = "fROM Zone zoneTable where zoneTable.utilisateur = :utilisateur and zoneTable.typeZoneStandard = '0' ";
			return session.createQuery(select).setParameter(
					"utilisateur", utilisateur).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Zone> rechercheZonesPersonnelles(Utilisateur utilisateur){
		Session session = getSession();
		ITypeZoneStandardService typeService = new TypeZoneStandardService();
		try {
			String select = "from Zone z where z.typeZoneStandard = :typeUtilisateur and  z.utilisateur = :utilisateur order by z.nom";
			return session.createQuery(select)
					.setParameter("typeUtilisateur", typeService.findById(0))
					.setParameter("utilisateur", utilisateur).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Integer> findByIdPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("select zone_partagee.id_zone ")
			.append("    from ")
			.append("        zone zone_partagee")
			.append("          inner join")
			.append("        (")
			.append("          select distinct(zone_de_zonage_req.zone) id_zone")
			.append("            from ")
			.append("              zonage zonage_req")
			.append("            inner join")
			.append("              zone_de_zonage zone_de_zonage_req")
			.append("             on ")
			.append("            (")
			.append("              zonage_req.id_zonage = zone_de_zonage_req.zonage")
			.append("                and")
			.append("              zonage_req.id_user != :id_user")
			.append("            )")
			.append("        ) zdz_autre_utilisateur")
			.append("          on")
			.append("        (")
			.append("            zone_partagee.id_zone = zdz_autre_utilisateur.id_zone")
			.append("        )")
			.append("    where zone_partagee.id_user = :id_user");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId()).list();
		
	
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Integer> findByIdNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("select zone_non_partagee.id_zone ")
			.append("    from ")
			.append("        zone zone_non_partagee")
			.append("      where")
			.append("      zone_non_partagee.id_zone")
			.append("      not in")
			.append("           (")
			.append("              select distinct(zone_de_zonage_req.zone) id_zone")
			.append("                from ")
			.append("                  zonage zonage_req")
			.append("                inner join")
			.append("                  zone_de_zonage zone_de_zonage_req")
			.append("                 on ")
			.append("                (")
			.append("                  zonage_req.id_zonage = zone_de_zonage_req.zonage")
			.append("                    and")
			.append("                  zonage_req.id_user != :id_user")
			.append("               )")
			.append("            )")
			.append("    and zone_non_partagee.id_user = :id_user");
			
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findByNomPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("select zone_partagee.nom ")
			.append("    from ")
			.append("        zone zone_partagee")
			.append("          inner join")
			.append("        (")
			.append("          select distinct(zone_de_zonage_req.zone) id_zone")
			.append("            from ")
			.append("              zonage zonage_req")
			.append("            inner join")
			.append("              zone_de_zonage zone_de_zonage_req")
			.append("             on ")
			.append("            (")
			.append("              zonage_req.id_zonage = zone_de_zonage_req.zonage")
			.append("                and")
			.append("              zonage_req.id_user != :id_user")
			.append("            )")
			.append("        ) zdz_autre_utilisateur")
			.append("          on")
			.append("        (")
			.append("            zone_partagee.id_zone = zdz_autre_utilisateur.id_zone")
			.append("        )")
			.append("    where zone_partagee.id_user = :id_user");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findByNomNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("select zone_non_partagee.nom ")
			.append("    from ")
			.append("        zone zone_non_partagee")
			.append("      where")
			.append("      zone_non_partagee.id_zone")
			.append("      not in")
			.append("           (")
			.append("              select distinct(zone_de_zonage_req.zone) id_zone")
			.append("                from ")
			.append("                  zonage zonage_req")
			.append("                inner join")
			.append("                  zone_de_zonage zone_de_zonage_req")
			.append("                 on ")
			.append("                (")
			.append("                  zonage_req.id_zonage = zone_de_zonage_req.zonage")
			.append("                    and")
			.append("                  zonage_req.id_user != :id_user")
			.append("               )")
			.append("            )")
			.append("    and zone_non_partagee.id_user = :id_user");
			Query query = session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId());

			return query
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@Override
	public int deleteDepartement_impactByListeIdZone( List<Integer> zonesASupprimerId) {
		
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM Departement_impact di")
				.append("       WHERE di.zone  IN (:listeIdZoneASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdZoneASupprimer",zonesASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@Override
	public int deleteZonesByListeIdZone(List<Integer> zonesASupprimerId) {
		Session session = getSession();
		
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM zone z")
				.append("       WHERE z.id_zone  IN (:listeIdZoneASupprimer)");
			
			int updated = session
					.createSQLQuery(selectBufferise.toString())
					.setParameterList("listeIdZoneASupprimer",zonesASupprimerId)
					.executeUpdate();


			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}	
	}

	@Override
	public int deleteCommuneDeZoneByListeIdZone(List<Integer> zonesASupprimerId) {
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM COMMUNE_DE_ZONE CDZ")
				.append("       WHERE CDZ.zone  IN (:listeIdZoneASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdZoneASupprimer",zonesASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}	
	}
	
	
	@Override
	public int deleteNomTableByListeId(String nomTable, String nomChamp, List<Integer> elementsASupprimerId) {
		
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM ")
				.append(nomTable)
				.append("       WHERE ")
				.append(nomChamp)
				.append("  IN (:listeIdASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdASupprimer",elementsASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}


}
