package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IZoneEtudeZoneEchangeDAO;
import fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class ZoneEtudeZoneEchangeDAO extends GenericDAO<String, Integer> implements
		IZoneEtudeZoneEchangeDAO {
	
	// Zones d'échange (ID_ZONE, libellé), qui apparaissent dans la liste pour chaque zone d'étude des 5 principales zones d'échange,
	// et qui sont hors zonage
	// renvoie List<Object[]>
	// ex.
	// List<Object[]> liste = getListeZoneEchangeHorsZonage(.. 
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> identifiant de zone d'échange
	// liste.get(0)[1] --> libelle
	@SuppressWarnings("unchecked")
	public List<Object[]> getListeZoneEchangeHorsZonage(
			String idUser, 
			String prefixe)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			
			
			
			
			// modifier
			if (LancementPdfService.identifiant) {
				str.append("select ID_ZONE, LIBELLE");
				str.append(" from ZONE");
				str.append(" where ID_ZONE in (");
				str.append(" 	select distinct substr(ZONE_ECHANGE, 6) from ");
			}
			else {
				str.append("select NOM, LIBELLE");
				str.append(" from ZONE");
				str.append(" where NOM in (");
				str.append(" 	select distinct 'STANDARD_DEP_' || substr(ZONE_ECHANGE, 6) from ");
			}
				
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init");  
			str.append(		" where substr(ZONE_ECHANGE, 1, 1) = '_'");
			str.append(")");  
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> getliste(
			String idUser, 
			String prefixe)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ZONE_ETUDE, ZONE_ECHANGE");
			str.append(" from (");
			str.append(		"select ZONE_ETUDE, ZONE_ECHANGE, FLUX");
			str.append(		" from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init");  
							// zone_etude = zone du zonage
			str.append(		" where substr(ZONE_ETUDE, 1, 1) != '_'");  
							// zone_echange = zone du zonage
			str.append(		" and substr(ZONE_ECHANGE, 1, 1) != '_'");  
			str.append(		" union");
			str.append(		" select ZONE_ETUDE, ZONE_ECHANGE, FLUX");
			str.append(		" from ");
			str.append(		prefixe);
			str.append(		"_");
			str.append(		idUser);
			str.append(		"_csv_top_flux_init");  
							// zone_etude = zone du zonage
			str.append(		" where substr(ZONE_ETUDE, 1, 1) != '_'");  
							// zone_echange = zone hors zonage
			str.append(		" and substr(ZONE_ECHANGE, 1, 1) = '_')");  
			str.append(" order by zone_etude, flux desc");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}	
}
