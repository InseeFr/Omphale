package fr.insee.omphale.core.service.projection;

import java.util.List;

import fr.insee.omphale.domaine.Utilisateur;
import fr.insee.omphale.domaine.projection.Scenario;

/**
 * Interface de service <br>
 * objet métier {@link fr.insee.omphale.domaine.projection.Scenario}
 * 
 */
public interface IScenarioService {

	/**
	 * Recherche dans la base de données un objet scénario ayant pour
	 * identifiant un identifiant donné
	 * 
	 * @param id
	 *            identifiant de scenario
	 * @return scenario
	 */
	public Scenario findById(Integer id);

	/**
	 * Méthode qui va vérifier que le nom de scénario n'est pas déjà utilisé
	 * pour un utilisateur
	 * 
	 * @param idUser
	 *            idUser
	 * @param nom
	 *            nom
	 * @return true si le nom de scénario n'est pas déjà utilisé
	 */
	public boolean testerNomScenario(String idUser, String nom, IScenarioService scenarioService);

	/**
	 * Méthode qui va vérifier que le nom de scénario n'est pas déjà utilisé
	 * (sauf éventuellement par un scénario d'identifiant donné) pour un
	 * utilisateur
	 * 
	 * @param idUser
	 *            idUser
	 * @param nom
	 *            nom
	 * @param id
	 *            identifiant de scénario
	 * @return true si le nom de scénario n'est pas déjà utilisé
	 */
	public boolean testerNomScenario(String idUser, String nom, int id, IScenarioService scenarioService);

	/**
	 * Compte le nombre de scenario dans la base de données pour un utilisateur
	 * 
	 * @param idUser
	 * @return nombre de scenario
	 */
	public int compterScenario(String idUser, IScenarioService scenarioService);

	/**
	 * Recherche dans la base de données tous les scenarios pour un utilisateur <br>
	 * order dateCreation desc
	 * 
	 * @param idUser
	 *            idUser
	 * @return liste de scenarios
	 */
	public List<Scenario> findAll(String idUser);

	/**
	 * Recherche dans la base de données tous les scenarios pour un utilisateur
	 * et un standard donné<br>
	 * order dateCreation desc
	 * 
	 * @param idUser
	 *            idUser
	 * @return liste de scenarios
	 */
	public List<Scenario> findAll(String idUser, Boolean standard);
	
	
	
	/**
	 * Recherche dans la base de données tous les scenarios pour un utilisateur
	 * et un standard donné<br>
	 * et un boolean validation donné
	 * order dateCreation desc
	 * 
	 * @param idUser
	 *            idUser
	 * @return liste de scenarios
	 */
	public List<Scenario> findAll(String idUser, Boolean standard, Boolean validation);

	/**
	 * Sauvegarde ou met à jour un scénario dans la base de données
	 * 
	 * 
	 */
	public Scenario insertOrUpdate(Scenario scenario);

	/**
	 * Met à jour un scénario dans la base de données
	 * 
	 */
	public void update();

	/**
	 * Supprime un scénario dans la base de données
	 * 
	 */
	public void remove(Scenario scenario);

	public void flush();

	/**
	 * Recherche dans la base de données tous les scenarios standards si true,
	 * non standards si false <br>
	 * order dateCreation desc
	 * 
	 * @return liste de scenarios
	 */
	public List<Scenario> findAll(Boolean standard);


	
	/**
	 * Tri scenarios par ordre alpha sur le nom
	 * 
	 * @return void
	 */
	public void triScenarioParOrdreAlphabetique(List<Scenario> scenarios);
	
	/**
	 * recherche si les scénarios et évol de scénarios d'un utilisateur
	 * <BR>
	 * utilisées seulement par l'utilisateur (non partagé)
	 * <BR>
	 * utilisées par d'autres utilisateurs (partagé)
	 * 
	 * 
	 * @param utilisateur
	 * @param scenariosUtilisesParUtilisateur
	 * @param scenariosUtiliseesParDautres
	 * @param evolDeScenarioUtilisesParUtilisateur
	 * @param evolDeScenarioUtiliseesParDautres
	 * @param nomScenariosUtilisesParUtilisateur
	 * @param nomScenariosUtiliseesParDautres
	 */
	public List<Object> rechercheScenariosPourFonctionSuppression(	Utilisateur utilisateur,
			List<Integer> scenariosUtilisesParUtilisateur,
			List<Integer> scenariosUtiliseesParDautres,
			List<String> nomScenariosUtilisesParUtilisateur,
			List<String> nomScenariosUtiliseesParDautres);

	public int deleteByIdScenario(List<Integer> scenariosASupprimerId);

}
