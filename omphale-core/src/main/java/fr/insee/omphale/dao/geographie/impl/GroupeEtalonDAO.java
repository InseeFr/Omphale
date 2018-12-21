package fr.insee.omphale.dao.geographie.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import fr.insee.omphale.dao.geographie.IGroupeEtalonDAO;
import fr.insee.omphale.dao.util.GenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.GroupeEtalon;
import fr.insee.omphale.domaine.geographie.GroupeEtalonId;
import fr.insee.omphale.domaine.geographie.Zonage;
import fr.insee.omphale.ihm.util.ParametresMessages;

/**
 * Classe gérant les fonctionnalités de la couche DAO pour GroupeEtalon
 *
 */
public class GroupeEtalonDAO extends GenericDAO<GroupeEtalon, GroupeEtalonId>
		implements IGroupeEtalonDAO {

	@SuppressWarnings("unchecked")
	public List<GroupeEtalon> findByZonage(Zonage zonage) {
		Session session = getSession();
		try {
			String select = "from GroupeEtalon g where g.id.zonage = :zonage";
			return session.createQuery(select)
					.setParameter("zonage", zonage).list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findByIdPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT grpEtal.zonage || ' ' || grpEtal.signature ")
			.append("	FROM groupe_etalon grpEtal ")
			.append("		WHERE ")
			.append("			grpetal.zonage ")
			.append("				IN")
			.append("					(")
			.append("						(select distinct(zonage_user.id_zonage)")
			.append("                            from  ")
			.append("							  zonage zonage_user  ")
			.append("									inner join  ")
			.append("							  (select * from def_projection ) dp  ")
			.append("							  on ( ")
			.append("									dp.id_zonage = zonage_user.id_zonage")
			.append("									and")
			.append("									dp.id_user != zonage_user.id_user")
			.append("								  )  ")
			.append("							  where zonage_user.id_user = :id_user))");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId()).list();
		
	
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findByIdNonPartagee(Utilisateur utilisateur){
		
		Session session = getSession();
		
		try {
			StringBuffer selectBufferise = new StringBuffer();
			selectBufferise
			.append("SELECT grpEtal.zonage || ' ' || grpEtal.signature ")
			.append("	FROM groupe_etalon grpEtal ")
			.append("		WHERE ")
			.append("			grpetal.zonage ")
			.append("				IN ")
			.append("					(				")
			.append("						select distinct(zonage_non_partagee.id_zonage) ")
			.append("						  from zonage zonage_non_partagee ")
			.append("						  where ")
			.append("						  zonage_non_partagee.id_zonage not in (")
			.append("								(select zonage_user.id_zonage")
			.append("									  from  ")
			.append("										  zonage zonage_user  ")
			.append("												inner join  ")
			.append("										  (select * from def_projection ) dp  ")
			.append("										  on ( ")
			.append("												dp.id_zonage = zonage_user.id_zonage")
			.append("												and")
			.append("												dp.id_user != zonage_user.id_user")
			.append("											  )  ")
			.append("										  where zonage_user.id_user = :id_user))")
			.append("						   and ")
			.append("						  zonage_non_partagee.id_user = :id_user		)");
			
			return session.createSQLQuery(selectBufferise.toString())
					.setString("id_user",utilisateur.getId())
					.list();
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@Override
	public int deleteZoneDeGroupetByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId) {
		
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM ZONE_DE_GROUPET ZDG")
				.append("       WHERE (ZDG.ZONAGE || ' ' || ZDG.SIGNATURE) IN (:listeIdGroupeEtalonASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdGroupeEtalonASupprimer",groupesEtalonsASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@Override
	public int deleteCommuneResiduelleByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId) {
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM COMMUNE_RESIDUELLE CR")
				.append("       WHERE (CR.ZONAGE || ' ' || CR.SIGNATURE) IN (:listeIdGroupeEtalonASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdGroupeEtalonASupprimer",groupesEtalonsASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@Override
	public int deletedept_de_groupetByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId) {
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM dept_de_groupet ddg")
				.append("       WHERE (ddg.ZONAGE || ' ' || ddg.SIGNATURE) IN (:listeIdGroupeEtalonASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdGroupeEtalonASupprimer",groupesEtalonsASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	@Override
	public int deleteGroupeEtalonByListeIdGroupeEtalon(
			List<String> groupesEtalonsASupprimerId) {
		Session session = getSession();
		
		try {
	
			StringBuffer selectBufferise = new StringBuffer();
			
			selectBufferise
				.append("DELETE FROM groupe_etalon ge")
				.append("       WHERE (ge.ZONAGE || ' ' || ge.SIGNATURE) IN (:listeIdGroupeEtalonASupprimer)");

			int updated = session
								.createSQLQuery(selectBufferise.toString())
								.setParameterList("listeIdGroupeEtalonASupprimer",groupesEtalonsASupprimerId)
								.executeUpdate();
			return updated;
			
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}

	

}
