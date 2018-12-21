package fr.insee.omphale.dao.geographie;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.geographie.Commune;
import fr.insee.omphale.domaine.geographie.Departement;


/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour Commune
 *
 */
public interface ICommuneDAO extends IGenericDAO<Commune, String> {
	/**
	 * Recherche la liste des identifiants commune présents dans la liste en
	 * argument qui ne sont pas présents en base de données
	 * 
	 * @param idCommunes
	 * @return
	 */
	public List<String> findIdNonPresents(List<String> idCommunes);

	/**
	 * Ramene les communes à partir de leurs identifiants
	 * 
	 * @param idCommunes
	 * @return
	 */
	public List<Commune> findById(List<String> idCommunes);
	
	/**
	 * recherche les communes d'un département
	 * 
	 * @param departement
	 * @return {@link List}<{@link Commune}>
	 */
	public List<Commune> findAllByDepartement(Departement departement);
}
