package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * données pour les graphiques de Flux de &nbsp;&nbsp;&nbsp;&nbsp; vers &nbsp;&nbsp;&nbsp;&nbsp;
 */
public interface IGraphiquePointFluxDAO extends IGenericDAO<String, Integer> {



	public List<Object[]> getPointFluxSortant(
			String idUser, 
			String prefixe, 
			String anneeDebut, 
			Integer age100,
			String zonesEtude)  ;
	
	public List<Object[]> getPointFluxEntr(
			String idUser, 
			String prefixe, 
			String anneeDebut, 
			Integer age100,
			String zonesEtude)  ;
}
