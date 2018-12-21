package fr.insee.omphale.dao.geographie;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.geographie.CommuneDependance;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour CommuneDependance
 *
 */
public interface ICommuneDependanceDAO extends
		IGenericDAO<CommuneDependance, Integer> {

	/**
	 * Recherche la liste des identifiants des dépendances (objet
	 * CommuneDependance) qui concernent les communes dont les identifiants
	 * constituent la liste en argument.
	 * 
	 * @param idCommunes
	 *            : liste des identifiants de commune dont on veut retrouver les
	 *            dépendances
	 * @return
	 */
	public List<Integer> findIdByIdCommunes(List<String> idCommunes);

	/**
	 * Ramene les dépendances à partir de leurs identifiants
	 * 
	 * @param idDependance
	 * @return
	 */
	public List<CommuneDependance> findById(List<Integer> idDependance);
}
