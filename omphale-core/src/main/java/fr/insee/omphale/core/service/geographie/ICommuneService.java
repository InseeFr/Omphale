package fr.insee.omphale.core.service.geographie;

import java.util.List;

import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;

/**
 * Interface de service pour les communes
 */

public interface ICommuneService {

	/**
	 * Recherche les id communes non présents en base à partir d'une liste
	 * d'identifiants
	 * 
	 * @param idCommunes
	 * @return List(String)
	 */
	public List<String> findIdNonPresents(List<String> idCommunes);

	/**
	 * Permet de ramener les communes par paquets à partir de leurs identifiants
	 * 
	 * @param idCommunes
	 * @return List(Commune)
	 */
	public List<Commune> findById(List<String> idCommunes);

	/**
	 * Ramène toutes les communes d'un département
	 * 
	 * @param departement
	 * @return
	 */
	public List<Commune> findAllByDepartement(Departement departement);
	
	/**
	 * recherche une commune en base selon son identifiant
	 * 
	 * @param idCommune
	 * @return Commune
	 */
	public Commune findById(String idCommune);
}
