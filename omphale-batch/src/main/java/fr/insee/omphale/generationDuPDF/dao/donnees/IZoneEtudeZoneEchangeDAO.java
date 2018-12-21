package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * liste pour chaque zone d'étude des principales zones d'échange
 */
public interface IZoneEtudeZoneEchangeDAO extends IGenericDAO<String, Integer> {

	
	/**
	 * Zones d'échange (ID_ZONE, libellé), qui apparaissent dans la liste pour chaque zone d'étude des 5 principales zones d'échange,
	 * et qui sont hors zonage
	 * @param idUser
	 * @param prefixe
	 * @return
	 * @
	 */
	public List<Object[]> getListeZoneEchangeHorsZonage(
			String idUser, 
			String prefixe) ;
			
	/**
	 * Recherche pour chaque zone d'étude la liste des 5 principales zones d'échange
	 * @param idUser
	 * @param prefixe
	 * @return
	 * @
	 */
	public List<Object[]> getliste(
			String idUser, 
			String prefixe)  ;
}
