package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * - données flux sortant de la zone d'étude, par zone d'étude, zone d'échange
 * <br>
 * - tables :
 * <br>
 * prefixe_iduser_CSV_AGREGE_TOP_FLUX 
 * <br>
 * prefixe_iduser_CSV_TOP_FLUX 
 */
public interface ITableau2TousAgesPlus5VersDAO extends IGenericDAO<String, Integer> {

	/**
	 * Recherche données flux sortant de la zone d'étude, par zone d'étude, zone d'échange
	 * <br>
	 * Flux observés au RP
	 * <br>
	 * table prefixe_iduser_CSV_AGREGE_TOP_FLUX 
	 * @param idUser idep
	 * @param prefixe préfixe table 
	 * @param zonesEtude liste des zones d'étude. Ex. : "('464', '481')"
	 * @return flux sortant de la zone d'étude, par zone d'étude, zone d'échange
	 * <br>
	 * Flux observés au RP
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getVersAnneeDebut(..).get(0) ;
	 * <br> 
	 * object[0] --&gt. identifiant zone d'étude
	 * <br> 
	 * object[1] --&gt. identifiant zone d'échange
	 * <br> 
	 * object[2] --&gt. flux de la zone d'étude vers la zone d'échange
	 * <br> 
	 * object = getVersAnneeDebut(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getVersAnneeDebut(
			String idUser, 
			String prefixe,
			String zonesEtude) ;
	
	/**
	 * Recherche données flux sortant de la zone d'étude, par zone d'étude, zone d'échange
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
	 * table prefixe_iduser_CSV_TOP_FLUX 
	 * @param idUser idep
	 * @param prefixe préfixe table 
	 * @param annee année
	 * <br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * anneeDebut ou anneeFinMoins1
	 * @param zonesEtude liste des zones d'étude. Ex. : "('464', '481')"
	 * @return flux sortant de la zone d'étude, par zone d'étude, zone d'échange
	 * <br>
	 * Ex. :
	 * <br>
	 * anneeDebut ou anneeFinMoins1
	 * <br> 
	 * Object[] object = getVersAnnee(..).get(0) ;
	 * <br> 
	 * object[0] --&gt. identifiant zone d'étude
	 * <br> 
	 * object[1] --&gt. identifiant zone d'échange
	 * <br> 
	 * object[2] --&gt. flux de la zone d'étude vers la zone d'échange
	 * <br> 
	 * object = getVersAnnee(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getVersAnnee(
			String idUser, 
			String prefixe, 
			String anneeDebut,
			String zonesEtude) ;
	
	/**
	 * Recherche données flux sortant de la zone d'étude, par zone d'étude, zone d'échange
	 * <br>
	 * Flux cumulés (2006-2031)
	 * <br>
	 * table prefixe_iduser_CSV_TOP_FLUX 
	 * @param idUser idep
	 * @param prefixe préfixe table 
	 * @param zonesEtude liste des zones d'étude. Ex. : "('464', '481')"
	 * @return flux sortant de la zone d'étude, par zone d'étude, zone d'échange
	 * <br>
	 * flux cumulés (ex. 2006-2031)
	 * <br>
	 * Ex. :
	 * <br> 
	 * Object[] object = getVers(..).get(0) ;
	 * <br> 
	 * object[0] --&gt. identifiant zone d'étude
	 * <br> 
	 * object[1] --&gt. identifiant zone d'échange
	 * <br> 
	 * object[2] --&gt. flux de la zone d'étude vers la zone d'échange
	 * <br> 
	 * object = getVers(..).get(1)
	 * <br> 
	 * etc.
	 * @
	 */
	public List<Object[]> getVers(
			String idUser, 
			String prefixe,
			String zonesEtude) ;
}
