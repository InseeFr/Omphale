package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * - données EDV pour une année donnée
 * <br>
 * - table prefixe_iduser_CSV_QD
 */
public interface ITableau1EDVDAO extends IGenericDAO<String, Integer> {

	/**
	 * Recherche quotients de décès par zone, sexe, âge pour une année donnée
	 * <br>
	 * pour calculer EDV
	 * @param idUser idep
	 * @param prefixe préfixe table ex. "ZP"
	 * @param annee année
	 * @return quotients de décès par zone, sexe, âge
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getListe(..).get(0) ;
	 * <br> 
	 * object[0] --&gt. identifiant zone
	 * <br> 
	 * object[1] --&gt.  sexe
	 * <br> 
	 * object[2] --&gt.  âge
	 * <br> 
	 * object[3] --&gt.  quotient de décès
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
