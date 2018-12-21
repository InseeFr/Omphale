package fr.insee.omphale.generationDuPDF.service.donnees;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.insee.omphale.generationDuPDF.domaine.BeanParametresResultat;

/**
 * Recherche des données dans la base de données, pour le tableau Flux avec les principales zones d'échange
 * <br>
 * données par tranche d'âge, par zone
 * <br>
 * interface utilisée par {@link fr.insee.omphale.generationDuPDF.service.lancer.LancementPdfService}
 */
public interface ITableau2FluxService {

	public List<Object[]> getVersAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude);
	
	public List<Object[]> getVersAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude);
	
	public List<Object[]> getVersAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude);
	
	public List<Object[]> getVers(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude);
	
	public List<Object[]> getDeAnneeDebut(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude);
	
	public List<Object[]> getDeAnneeDebutPlus5(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude);
	
	public List<Object[]> getDeAnneeFin(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude);
	
	public List<Object[]> getDe(
			BeanParametresResultat beanParametresResultat, 
			Map<String, String> hashMap,
			String zonesEtude);
	/**
	 * 
	 * @param donnees
	 * @param beanParametresResultat
	 * @param treeMapZonesEtudeZoneEch
	 * @param hashMap
	 * @return HashMap(zone d'étude, HashMap(zone d'échange, HashMap(tranche d'âge, flux)))
	 */
	public Map<String, HashMap<String, HashMap<String, Double>>> getDonnees(
			List<Object[]> donnees,
			BeanParametresResultat beanParametresResultat,
			Map<String,
			List<String>> treeMapZonesEtudeZoneEch, 
			Map<String, String> hashMap);
}
