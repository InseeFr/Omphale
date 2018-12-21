package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * données pour le graphique Pyramide des âges
 */
public interface IGraphiquePyramideDAO extends IGenericDAO<String, Integer> {


	// order zone, annee, sexe, age
	public List<Object[]> findAll(
			String idUser, 
			String prefixe, 
			String calageSuffixe, 
			String anneeDebut, 
			String anneeDebutPlus5, 
			String anneeFin, 
			Integer age100)  ;
}
