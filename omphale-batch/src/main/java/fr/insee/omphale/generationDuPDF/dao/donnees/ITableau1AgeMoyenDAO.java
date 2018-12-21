package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * - données âge moyen pour une année donnée
 * <br>
 * - table prefixe_iduser_CSV_POPULATION_calageSuffixe
 */
public interface ITableau1AgeMoyenDAO extends IGenericDAO<String, Integer> {


	/**
	 * Recherche données âge moyen par zone pour une année donnée
	 * @param idUser idep
	 * @param prefixe préfixe table ex. "ZP"
	 * @param calage suffixe table ex. "CAL", "NCAL"
	 * @param annee année
	 * @return âge moyen par zone
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getAgeMoyen(..).get(0) ;
	 * <br> 
	 * object[0] --&gt. identifiant zone
	 * <br> 
	 * object[1] --&gt. âge moyen
	 * <br> 
	 * object = getAgeMoyen(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getAgeMoyen(
			String idUser, 
			String prefixe, 
			String calage, 
			String annee)  ;
	
	
}
