package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * données pour le graphique Solde migratoire au RP
 */
public interface IGraphiquePointFluxSoldeDAO extends IGenericDAO<String, Integer> {

	
	public List<Object[]> getPointFluxSolde(
			String idUser, 
			String prefixe, 
			String anneeDebut, 
			Integer age100,
			String zonesEtude)  ;
}
