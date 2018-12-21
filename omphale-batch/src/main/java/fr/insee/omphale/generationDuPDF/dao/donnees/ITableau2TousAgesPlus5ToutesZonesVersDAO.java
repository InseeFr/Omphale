package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * - données flux sortant de la zone d'étude, par zone d'étude
 * <br>
 * - tables :
 * <br>
 * prefixe_iduser_CSV_AGREGE_EMIG
 * <br>
 * prefixe_iduser_CSV_EMIG
 */
public interface ITableau2TousAgesPlus5ToutesZonesVersDAO extends IGenericDAO<String, Integer> {

	/**
	 * Recherche données flux sortant de la zone d'étude, par zone d'étude
	 * <br>
	 * Flux observés au RP
	 * <br>
	 * table prefixe_iduser_CSV_AGREGE_EMIG 
	 * @param idUser idep
	 * @param prefixe préfixe table 
	 * @return flux sortant de la zone d'étude, par zone d'étude
	 * <br>
	 * Flux observés au RP
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getVersAnneeDebut(..).get(0) ;
	 * <br> 
	 * object[0] --&gt. identifiant zone d'étude
	 * <br> 
	 * object[1] --&gt. flux sortant de la zone d'étude
	 * <br> 
	 * object = getVersAnneeDebut(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getVersAnneeDebut(
			String idUser, 
			String prefixe) ;
	
	/**
	 * Recherche données flux sortant de la zone d'étude, par zone d'étude
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
	 * table prefixe_iduser_CSV_EMIG 
	 * @param idUser idep
	 * @param prefixe préfixe table 
	 * @param annee année
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * anneeDebut ou anneeFinMoins1
	 * @return flux sortant de la zone d'étude, par zone d'étude
	 * <br>
	 * Ex. :
	 * <br>
	 * anneeDebut ou anneeFinMoins1
	 * <br> 
	 * Object[] object = getVersAnnee(..).get(0) ;
	 * <br> 
	 * object[0] --&gt. identifiant zone d'étude
	 * <br> 
	 * object[1] --&gt. flux sortant de la zone d'étude
	 * <br> 
	 * object = getVersAnnee(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getVersAnnee(
			String idUser, 
			String prefixe, 
			String anneeDebut) ;
	
	/**
	 * Recherche données flux sortant de la zone d'étude, par zone d'étude
	 * <br>
	 * Flux cumulés (2006-2031)
	 * <br>
	 * table prefixe_iduser_CSV_EMIG 
	 * @param idUser idep
	 * @param prefixe préfixe table 
	 * @return flux sortant de la zone d'étude, par zone d'étude
	 * <br>
	 * flux cumulés (ex. 2006-2031)
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getVers(..).get(0) ;
	 * <br> 
	 * object[0] --&gt. identifiant zone d'étude
	 * <br> 
	 * object[1] --&gt. flux sortant de la zone d'étude
	 * <br> 
	 * object = getVers(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getVers(
			String idUser, 
			String prefixe) ;
}
