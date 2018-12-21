package fr.insee.omphale.generationDuPDF.service.geographie;

import fr.insee.omphale.generationDuPDF.domaine.geographie.Zonage;


public interface IZonageService {

	public Zonage findById(String id);
	
	/**
	 * renvoie "1" si idZonage == null ou idZonage != "0", renvoie "0" sinon
	 * @param idZonage
	 * @return "1" si idZonage == null ou idZonage != "0", "0" sinon
	 */
	public String isProjectionUtilisateur(String idZonage);
}
