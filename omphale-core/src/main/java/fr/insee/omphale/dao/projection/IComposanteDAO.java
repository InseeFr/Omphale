package fr.insee.omphale.dao.projection;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.projection.Composante;

/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour Composante
 *
 */
public interface IComposanteDAO extends IGenericDAO<Composante, String> {

	/**
	 * Récupère la liste des composantes associée 
	 * <BR> 
	 * à des évolutions non localisées standards ou non standards
	 * 
	 * @param standard
	 * @return List(Composante)
	 */
	public List<Composante> findAllReferenceesEvolutionNL(Boolean standard);
	
	
	/**
	 * Récupère la liste des composantes associée 
	 * <BR> 
	 * à des évolutions non localisées standards ou non standards
	 * <BR>
	 * et en fonction de l'utilisateur
	 * 
	 * @param standard
	 * @return List(Composante)
	 */
	public List<Composante> findAllReferenceesEvolutionNL(String idUser, Boolean standard);
}
