package fr.insee.omphale.generationDuPDF.dao.donnees.impl;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import fr.insee.omphale.generationDuPDF.dao.GenericDAO;
import fr.insee.omphale.generationDuPDF.dao.donnees.IGraphiquePointFluxSoldeDAO;
import fr.insee.omphale.generationDuPDF.util.ParametresMessages;

public class GraphiquePointFluxSoldeDAO extends GenericDAO<String, Integer> implements
		IGraphiquePointFluxSoldeDAO {

	
	// renvoie List<Object[]>
	// ex.
	// List<Object[]> list = getPointFluxSolde(..
	// liste.get(0) est un tableau d'Object
	// liste.get(0)[0] --> zone
	// liste.get(0)[1] --> sexe
	// liste.get(0)[2] --> age
	// liste.get(0)[3] --> flux
	@SuppressWarnings("unchecked")
	public List<Object[]> getPointFluxSolde(
			String idUser, 
			String prefixe, 
			String anneeDebut,
			Integer age100,
			String zonesEtude)  {
		Session session = getSession();
		try {
			StringBuffer  str = new StringBuffer();
			str.append("select ZONE, SEXE, AGE, nvl(FLUX,0) from (");
			str.append("	select nvl(flux_sortants.zone_etude, flux_entr.zone_etude) ZONE, ");
			str.append(" 	nvl(flux_sortants.sexe, flux_entr.sexe) SEXE,");
			str.append(" 	nvl(flux_sortants.age, flux_entr.age) AGE,");
			str.append(" 	(nvl(flux_entr.flux_entr, 0) - nvl(flux_sortants.flux_sortant, 0)) FLUX");
			str.append(" 	from (");
			str.append("		select ORIGINE zone_etude, sexe, age, sum(FLUX) flux_sortant");
			str.append(" 		from ");
			str.append(			prefixe);
			str.append(			"_");
			str.append(			idUser);
			str.append(			"_csv_agrege_top_flux");
			str.append("		where annee_ref = ");
			str.append(			anneeDebut);
			str.append(" 		and ORIGINE != DESTINATION ");
			str.append(" 		and ORIGINE in ");
			str.append(			zonesEtude);
			str.append("		group by ORIGINE, sexe, age");
			str.append("		order by ORIGINE, sexe, age");
			str.append("	) flux_sortants");
			str.append(" 	full outer join");
			str.append("		(");
			str.append("		select DESTINATION zone_etude, sexe, age, sum(FLUX) flux_entr");
			str.append(" 		from ");
			str.append(			prefixe);
			str.append(			"_");
			str.append(			idUser);
			str.append(			"_csv_agrege_top_flux");
			str.append("		where annee_ref = ");
			str.append(			anneeDebut);
			str.append(" 		and ORIGINE != DESTINATION ");
			str.append(" 		and DESTINATION in ");
			str.append(			zonesEtude);
			str.append("		group by DESTINATION, sexe, age");
			str.append("		order by DESTINATION, sexe, age");
			str.append("	) flux_entr");
			str.append(" 	on flux_sortants.zone_etude = flux_entr.zone_etude");
			str.append(" 	and flux_sortants.sexe = flux_entr.sexe");
			str.append(" 	and flux_sortants.age = flux_entr.age");
			str.append(")");
			str.append(" where AGE <= ");
			str.append(age100);
			str.append(" order by ZONE, SEXE, AGE");
			Query query = session.createSQLQuery(str.toString());
			return query.list();
		} catch (HibernateException he) {
			throw new RuntimeException(ParametresMessages
					.getString("error.dao.001"), he);
		}
	}
}
