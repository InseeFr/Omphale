package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * - données ICF pour une année donnée
 * <br>
 * - table prefixe_iduser_CSV_QF
 */
public interface ITableau1ICFDAO extends IGenericDAO<String, Integer> {

	/**
	 * Recherche ICF par zone pour une année donnée
	 * @param idUser idep
	 * @param prefixe préfixe table ex. "ZP"
	 * @param annee année
	 * @return ICF par zone
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getListe(..).get(0) ;
	 * <br> 
	 * object[0] --&gt.  identifiant zone
	 * <br> 
	 * object[1] --&gt.  ICF
	 * <br> 
	 * object = getListe(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getListe(
			String idUser, 
			String prefixe, 
			String annee)  ;
	
	
}
