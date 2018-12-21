package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * données pour le graphique Quotients de fécondité
 */
public interface IGraphiqueFeconditeDAO extends IGenericDAO<String, Integer> {


	// order zone, annee, sexe, age
	public List<Object[]> getListe(
			String idUser, 
			String prefixe, 
			String anneeDebut, 
			Integer age100)  ;

}
