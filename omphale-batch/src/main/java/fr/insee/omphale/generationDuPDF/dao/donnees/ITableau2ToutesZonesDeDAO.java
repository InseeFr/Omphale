package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * - données flux entrants de la zone d'étude, par zone d'étude, tranche d'âge
 * <br>
 * - tables :
 * <br>
 * prefixe_iduser_CSV_AGREGE_IMMIG
 * <br>
 * prefixe_iduser_CSV_IMMIG 
 */
public interface ITableau2ToutesZonesDeDAO extends IGenericDAO<String, Integer> {

	/**
	 * Recherche données flux entrants de la zone d'étude, par zone d'étude, tranche d'âge
	 * <br>
	 * Flux observés au RP
	 * <br>
	 * table prefixe_iduser_CSV_AGREGE_IMMIG 
	 * @param idUser idep
	 * @param prefixe préfixe table 
	 * @return flux entrants de la zone d'étude, par zone d'étude, tranche d'âge
	 * <br>
	 * Flux observés au RP
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getDeAnneeDebut(..).get(0) ;
	 * <br> 
	 * object[0] --&gt.  identifiant zone d'étude
	 * <br> 
	 * object[1] --&gt.  tranche d'âge
	 * <br> 
	 * object[2] --&gt.  flux entrants de la zone d'étude
	 * <br> 
	 * object = getDeAnneeDebut(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getDeAnneeDebut(
			String idUser, 
			String prefixe) ;
	
	/**
	 * Recherche données flux entrants de la zone d'étude, par zone d'étude, tranche d'âge
	 * <br>
	 * année : anneeDebut ou anneeFinMoins1
	 * <br>
	 * Ex. :
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * annee = 2006 --&gt. Flux projetés 2006-2011
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * annee = 2026 --&gt. Flux projetés 2026-2031
	 * <br>
	 * table prefixe_iduser_CSV_IMMIG 
	 * @param idUser idep
	 * @param prefixe préfixe table 
	 * @param annee année
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * anneeDebut ou anneeFinMoins1
	 * @return flux entrants de la zone d'étude, par zone d'étude, tranche d'âge
	 * <br>
	 * Ex. :
	 * <br>
	 * anneeDebut ou anneeFinMoins1
	 * <br> 
	 * Object[] object = getDeAnnee(..).get(0) ;
	 * <br> 
	 * object[0] --&gt.  identifiant zone d'étude
	 * <br> 
	 * object[1] --&gt.  tranche d'âge
	 * <br> 
	 * object[2] --&gt. flux entrants de la zone d'étude
	 * <br> 
	 * object = getDeAnnee(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getDeAnnee(
			String idUser, 
			String prefixe, 
			String annee) ;
	
	/**
	 * Recherche données flux entrants de la zone d'étude, par zone d'étude, tranche d'âge
	 * <br>
	 * Flux cumulés (2006-2031)
	 * <br>
	 * table prefixe_iduser_CSV_IMMIG
	 * @param idUser idep
	 * @param prefixe préfixe table
	 * @return flux entrants de la zone d'étude, par zone d'étude, tranche d'âge
	 * <br>
	 * flux cumulés (ex. 2006-2031)
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getDe(..).get(0) ;
	 * <br> 
	 * object[0] --&gt.  identifiant zone d'étude
	 * <br> 
	 * object[1] --&gt.  tranche d'âge
	 * <br> 
	 * object[2] --&gt.  flux entrants de la zone d'étude
	 * <br> 
	 * object = getDe(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getDe(
			String idUser, 
			String prefixe) ;
}
