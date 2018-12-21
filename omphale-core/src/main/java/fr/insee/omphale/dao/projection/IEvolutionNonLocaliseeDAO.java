package fr.insee.omphale.dao.projection;

import java.util.List;

import fr.insee.omphale.dao.util.IGenericDAO;
import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.geographie.Zone;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Hypothese;


/**
 * Interface définissant le contrat de méthode pour les fonctionnalités de la couche DAO pour EvolutionNonLocalisee
 *
 */
public interface IEvolutionNonLocaliseeDAO extends IGenericDAO<EvolutionNonLocalisee, Integer> {
	
	/**
	 * Recherche dans la base de données la liste des évolutions non localisées ayant pour standard 
	 * un standard donné et pour composante une composante donnée
	 * 
	 * @param standard standard
	 * @param composante composante
	 * return liste EvolutionNonLocalisee
	 */
	public List<EvolutionNonLocalisee> findAll(Boolean standard, String composante);
	
	/**
	 * Recherche dans la base de données la liste des évolutions non localisées ayant pour idUser un idUser donné, pour standard 
	 * un standard donné et pour composante une composante donnée
	 * 
	 * @param idUser idUser
	 * @param standard standard
	 * @param composante composante
	 * return liste EvolutionNonLocalisee
	 */
	public List<EvolutionNonLocalisee> findAll(String idUser, Boolean standard, String composante);
	
	public long compterEvolutionsNonLocalisees(Utilisateur utilisateur);
	
	public List<EvolutionNonLocalisee> findByUtilisateur(Utilisateur utilisateur);
	
	public List<EvolutionNonLocalisee> findStandard();
	
	public List<EvolutionNonLocalisee> findByHypothese(Hypothese hypothese);
	
	/**
	 * Recherche dans la base de données la liste des évolutions non localisées ayant pour identifiant 
	 * l'un des identifiants d'un tableau d'identifiants donnés.
	 * <br>
	 * Le résultat est classé suivant l'ordre par défaut :
	 * <br>
	 * anneeDeb croissant,
	 * <br>
	 * anneeFin décroissant,
	 * <br>
	 * sexeDeb != sexeFin,
	 * <br>
	 * sexeDeb = sexeFin = 1,
	 * <br>
	 * sexeDeb = sexeFin = 2,
	 * <br>
	 * ageDeb croissant,
	 * <br>
	 * ageFin décroissant
	 * @param listeIdentifiantENL ex. listeIdentifiantENL = "('1', '2', '3', '4')"
	 * @return liste d'évolutions non localisées
	 */
	public List<EvolutionNonLocalisee> findAllOrdreDefaut(String listeIdentifiantENL);
	
	/**
	 * Trouve les évolutions non localisées utilisant une zone précise
	 * <BR>
	 * soit une zone, soit une zone d'origine, soit une zone de destination
	 * 
	 * @param idZone
	 * @return List<Integer>
	 */
	public List<Integer> findByZone(Zone zone);
	
	
	
	/**
	 * Recherche les évolutions non localisées d'un utilisateur standards et non standards
	 * 
	 * 
	 * @param utilisateur
	 * @return List<EvolutionNonLocalisee>
	 */
	public List<EvolutionNonLocalisee> findByUtilisateurStandardOuNon(Utilisateur utilisateur);

	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId);

	public int deleteHypotheseByIdListeHypothese(
			List<Integer> hypothesesASupprimerId);
}
