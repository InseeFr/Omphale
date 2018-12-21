package fr.insee.omphale.generationDuPDF.dao.donnees;

import java.util.List;

import fr.insee.omphale.generationDuPDF.dao.IGenericDAO;

/**
 * range pour les graphiques de Flux de &nbsp;&nbsp;&nbsp;&nbsp; vers &nbsp;&nbsp;&nbsp;&nbsp;
 * <br>
 * range flux sortants, flux entrants
 * <br>
 * permet aux graphiques de flux qui sont en regard d'avoir le même range.
 * <br>
 * Ex. le graphique Flux de Ain vers Rhône est gradué de 0 à 750
 * et le graphique Flux de Rhône vers Ain est gradué aussi de 0 à 750
 */
public interface IGraphiquePointFluxRangeDAO extends IGenericDAO<String, Integer> {


	
	public List<Object[]> getPointFluxSortantRange(
			String idUser, 
			String prefixe, 
			String anneeDebut, 
			Integer age100,
			String zonesEtude)  ;

	
	public List<Object[]> getPointFluxEntrRange(
			String idUser, 
			String prefixe, 
			String anneeDebut, 
			Integer age100,
			String zonesEtude)  ;

}
