package fr.insee.omphale.core.service.projection;

import java.util.List;

import fr.insee.omphale.domaine.projection.Composante;

public interface IComposanteService {


	/**
	 * Recherche si une composante est dans une liste de composantes
	 * 
	 * @param liste
	 * @param composante
	 * @return boolean
	 */

	public boolean contains(List<Composante> liste, String composante);
	
	/**
	 * Recherche toutes les composantes
	 * 
	 * @param liste
	 * @param composante
	 * @return boolean
	 */
	public List<Composante> findAll();

	/**
	 * Recherche une composante selon son identifiant
	 * 
	 * @param id
	 * @return Composante
	 */
	public Composante findById(String id);

	/**
	 * Récupère la liste des composantes associée 
	 * 	si standard = 1, composantes référencées dans la table EVOL_NON_LOC pour standard = 1
	 *  si standard = 0, composantes référencées dans la table EVOL_NON_LOC pour userId = session.get("userId") et standard = 0		 
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
