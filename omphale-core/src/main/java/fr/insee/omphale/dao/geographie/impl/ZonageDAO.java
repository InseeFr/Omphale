package fr.insee.omphale.dao.geographie.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.dao.geographie.IZonageDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour Zonage
 *
 */
public class ZonageDAO extends GenericDAO<Zonage, String> implements IZonageDAO {
	
	@SuppressWarnings("unchecked")
	public List<Zonage> findByUtilisateur(Utilisateur utilisateur) {
		Session session = getSession();
		try {
			String select = "from Zonage z fetch all properties where z.utilisateur = :utilisateur";
			return session.createQuery(select).setParameter(
					"utilisateur", utilisateur).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	


	public long compterZonages(Utilisateur utilisateur) {
		Session session = getSession();
		try {
			String select = "select count(*) from Zonage z where z.utilisateur = :utilisateur";
			return (Long) session.createQuery(select).setParameter(
					"utilisateur", utilisateur).uniqueResult();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Zonage> findByZone_ZONE_DE_ZONAGE(Zone zone){
		
		Session session = getSession();
		
		try {
			String select = "select zonage from Zone_De_Zonage z where z.zone = :idZone";
			 List<String> idZonages = session.createSQLQuery(select).setParameter(
					"idZone", zone.getId()).list();
			 List<Zonage> zonages = null;
			 if(CollectionUtils.isNotEmpty(idZonages)){
				 zonages = new ArrayList<Zonage>();
					for (String idZonage : idZonages) {			
						zonages.add(this.findById(idZonage));
					}
				return zonages;
			 }
			 return zonages;
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Zonage> findByZone_ZONE_DE_GROUPET(Zone zone){
		Session session = getSession();
		
		
		try {
			String select = "select zonage from ZONE_DE_GROUPET z where z.zone = :idZone";
			List<String> idZonages = session.createSQLQuery(select).setParameter("idZone", zone.getId()).list();
			 List<Zonage> zonages = null;
			 if(CollectionUtils.isNotEmpty(idZonages)){
				 zonages = new ArrayList<Zonage>();
					for (String idZonage : idZonages) {			
						zonages.add(this.findById(idZonage));
					}
				return zonages;
			 }
			 return zonages;
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	public List<Zonage> findByZone(Zone zone){
		
		List<Zonage> Zonages_De_Zone = this.findByZone_ZONE_DE_ZONAGE(zone);
		List<Zonage> Zonages_De_Groupet = this.findByZone_ZONE_DE_GROUPET(zone);
		List<Zonage> zonages = new ArrayList<Zonage>();
		if(CollectionUtils.isNotEmpty(Zonages_De_Zone))
		{
			zonages.addAll(Zonages_De_Zone);
		}
		if(CollectionUtils.isNotEmpty(Zonages_De_Groupet))
		{
			zonages.addAll(Zonages_De_Groupet);
		}
		return zonages;

	}
	


	@Override
	public List<String> findUtilisateurs(Zone zone) {

		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Integer> findByIdPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("(select distinct(zonage_user.id_zonage)")
			.append("      from  ")
			.append("          zonage zonage_user  ")
			.append("                inner join  ")
			.append("          (select * from def_projection ) dp  ")
			.append("          on ( ")
			.append("                dp.id_zonage = zonage_user.id_zonage")
			.append("                and")
			.append("                dp.id_user != zonage_user.id_user")
			.append("              )  ")
			.append("          where zonage_user.id_user = :id_user)");
			
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
			.append("select distinct(zonage_non_partagee.id_zonage) ")
			.append("from zonage zonage_non_partagee ")
			.append("where ")
			.append("zonage_non_partagee.id_zonage not in (")
			.append("      (select zonage_user.id_zonage")
			.append("            from  ")
			.append("                zonage zonage_user  ")
			.append("                      inner join  ")
			.append("                (select * from def_projection ) dp  ")
			.append("                on ( ")
			.append("                      dp.id_zonage = zonage_user.id_zonage")
			.append("                      and")
			.append("                      dp.id_user != zonage_user.id_user")
			.append("                    )  ")
			.append("                where zonage_user.id_user = :id_user))")
			.append(" and ")
			.append("zonage_non_partagee.id_user = :id_user");
			
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
			.append("(select zonage_user.nom")
			.append("      from  ")
			.append("          zonage zonage_user  ")
			.append("                inner join  ")
			.append("          (select * from def_projection ) dp  ")
			.append("          on ( ")
			.append("                dp.id_zonage = zonage_user.id_zonage")
			.append("                and")
			.append("                dp.id_user != zonage_user.id_user")
			.append("              )  ")
			.append("          where zonage_user.id_user = :id_user)");
			
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
			.append("select distinct(zonage_non_partagee.nom) ")
			.append("from zonage zonage_non_partagee ")
			.append("where ")
			.append("zonage_non_partagee.id_zonage not in (")
			.append("      (select zonage_user.id_zonage")
			.append("            from  ")
			.append("                zonage zonage_user  ")
			.append("                      inner join  ")
			.append("                (select * from def_projection ) dp  ")
			.append("                on ( ")
			.append("                      dp.id_zonage = zonage_user.id_zonage")
			.append("                      and")
			.append("                      dp.id_user != zonage_user.id_user")
			.append("                    )  ")
			.append("                where zonage_user.id_user = :id_user))")
			.append(" and ")
			.append("zonage_non_partagee.id_user = :id_user");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}



	@Override
	public int deleteZoneDeZonageByListeIdZonage(List<Integer> zonagesASupprimerId) {
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM zone_de_zonage zdz")
				.append("       WHERE zdz.ZONAGE  IN (:listeIdZonageASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdZonageASupprimer",zonagesASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@Override
	public int deleteZonageByListeIdZonage(List<Integer> zonagesASupprimerId) {
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM zonage z")
				.append("       WHERE z.id_zonage  IN (:listeIdZonageASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdZonageASupprimer",zonagesASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}


}

