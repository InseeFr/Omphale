package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * données pour le graphique Evolution de la population
 */
public interface IGraphiquePopulationDAO extends IGenericDAO<String, Integer> {


	
	public List<Object[]> getPopulation(
			String idUser, 
			String prefixe, 
			String calage, 
			Integer age100)  ;
}
