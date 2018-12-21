package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * - données population pour une année donnée
 * <br>
 * - table prefixe_iduser_CSV_POPULATION_calageSuffixe
 */
public interface ITableau1PopulationTableauDAO extends IGenericDAO<String, Integer> {


	/**
	 * Recherche données population par zone pour une année donnée
	 * @param idUser idep
	 * @param prefixe préfixe table ex. "ZP"
	 * @param calage suffixe table ex. "CAL", "NCAL"
	 * @param annee année
	 * @return population par zone
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getPopulation(..).get(0) ;
	 * <br> 
	 * object[0] --&gt. identifiant zone
	 * <br> 
	 * object[1] --&gt.  population
	 * <br> 
	 * object = getAgeMoyen(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getPopulation(
			String idUser, 
			String prefixe, 
			String calage, 
			String annee)  ;
	
	/**
	 * Compte le nombre de populations négatives dans les données csv sur les populations cal
	 * 
	 * @param idUser et prefixe
	 * @return nombre de populations négatives
	 */
	public int compterPopulationNegativeCal(String idUser, String prefixe);
	
	/**
	 * Compte le nombre de populations négatives dans les données csv sur les populations ncal
	 * 
	 * @param idUser et prefixe
	 * @return nombre de populations négatives
	 */
	public int compterPopulationNegativeNcal(String idUser, String prefixe);
	
	/**
	 * Compte le nombre de populations négatives dans les données csv sur les populations men
	 * 
	 * @param idUser et prefixe
	 * @return nombre de populations négatives
	 */
	public int compterPopulationNegativeMen(String idUser, String prefixe);
	
	/**
	 * Compte le nombre de populations négatives dans les données csv sur les populations act
	 * 
	 * @param idUser et prefixe
	 * @return nombre de populations négatives
	 */
	public int compterPopulationNegativeAct(String idUser, String prefixe);
	
	/**
	 * Renvoie 0 si la table prefixe_iduser_csv_population_act n'existe pas ou un 1 si elle existe
	 * 
	 * @param idUser et prefixe
	 * @return nombre de tables existantes se nommant prefixe_iduser_csv_population_act
	 */
	public int isPopulationAct(String idUser, String prefixe);
	
	/**
	 * Renvoie 0 si la table prefixe_iduser_csv_population_men n'existe pas ou un 1 si elle existe
	 * 
	 * @param idUser et prefixe
	 * @return nombre de tables existantes se nommant prefixe_iduser_csv_population_men
	 */
	public int isPopulationMen(String idUser, String prefixe);
	
	/**
	 * Renvoie 0 si la table prefixe_iduser_csv_population_cal n'existe pas ou un 1 si elle existe
	 * 
	 * @param idUser et prefixe
	 * @return nombre de tables existantes se nommant prefixe_iduser_csv_population_cal
	 */
	public int isPopulationCal(String idUser, String prefixe);
	
	/**
	 * Renvoie 0 si la table prefixe_iduser_csv_population_ncal n'existe pas ou un 1 si elle existe
	 * 
	 * @param idUser et prefixe
	 * @return nombre de tables existantes se nommant prefixe_iduser_csv_population_ncal
	 */
	public int isPopulationNcal(String idUser, String prefixe);
}
