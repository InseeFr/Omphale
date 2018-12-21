package fr.insee.omphale.core.service.projection;

import java.util.List;
import java.util.Set;

import fr.insee.omphale.domaine.projection.Composante;
import fr.insee.omphale.domaine.projection.EvolutionLocalisee;
import fr.insee.omphale.domaine.projection.EvolutionNonLocalisee;
import fr.insee.omphale.domaine.projection.Projection;

public interface IEvolutionLocaliseeService {

	
	/**
	 * Recherche une liste d'EL à partir d'une liste d'identifiant d'EL
	 * @param listeIdentifiantEL
	 * @return Set(EvolutionLocalisee)
	 */
	public Set<EvolutionLocalisee> findAll(Integer[] listeIdentifiantEL);
	
	/**
	 * Modifie le rang de l'évolution localisée
	 * <BR>
	 * quand monte ou descend cette derniière dans la liste
	 * @param evolutionsLocalisees
	 * @param evolutionsLocaliseesSelectionnees
	 * @param modifierDisposition void
	 */
	public void advance(Set<EvolutionLocalisee> evolutionsLocalisees, List<Integer> evolutionsLocaliseesSelectionnees, String modifierDisposition);

	/**
	 * Trouve une évolution localisée en fonction de son identifiant
	 * @param id
	 * @return EvolutionLocalisee
	 */
	public EvolutionLocalisee findById(int id);
	
	/**
	 * Trouve une évolution localisée d'une projection et d'une composante données,
	 * <BR>
	 * en fonction de son rang
	 * @param projection
	 * @param composante
	 * @param rang
	 * @return EvolutionLocalisee
	 */
	/**
	 * Recherche une evolutionLocalisee selon les paramètres suivantes
	 * 
	 * @param projection
	 * @param composante
	 * @param rang
	 * @return {@link EvolutionLocalisee}
	 */
	public  EvolutionLocalisee findByRang(Projection projection, Composante composante, int rang );
	
	/**
	 * Recherche les évolutions localisées dépendantes d'une évolution non localisée
	 * 
	 * @param enl
	 * @return {@link List}<{@link EvolutionLocalisee}>
	 */
	public List<EvolutionLocalisee> findByENL(EvolutionNonLocalisee enl);
	
	/**
	 * insère en base une évolution localisée à partir de son instance
	 * 
	 * @param evolutionLocalisee
	 * @return {@link EvolutionLocalisee}
	 */
	public EvolutionLocalisee insertOrUpdate(EvolutionLocalisee evolutionLocalisee);
	
	/**
	 * Supprime en base une évolution localisée à partir de son instance
	 * 
	 * @param evolutionLocalisee
	 */
	public void delete(EvolutionLocalisee evolutionLocalisee);
	
	/**
	 * Recherche les évolutions localisées reliées à une projection 
	 * <BR>
	 * à partir de l'instance de la projection
	 *  
	 * @param projection
	 * @return {@link Set}<{@link EvolutionLocalisee}>
	 */
	public Set<EvolutionLocalisee> findByProjection(Projection projection);
	
	/**
	 * supprime en base les évolutions localisées reliées à une projection
	 * <BR>
	 * à partir de l'instance de la projection
	 * 
	 * @param projection
	 * @return {@link Projection}
	 */
	public Projection supprimerListeEvolutionLocalisees(Projection projection);

	
	/**
	 * Supprimer une evolutionLocalisee
	 * <BR>
	 * utilisée pour supprimer les objets chaînés d'un utilisateur
	 * 
	 * @param evolutionLocalisee
	 */
	public void supprimer(EvolutionLocalisee evolutionLocalisee);
	
	
	/**
	 * Recherche toutes les evolutionsLocalisees en base de données
	 * 
	 * @return
	 */
	public List<EvolutionLocalisee> findAll();
	
	
	public Integer deleteByListIdProjection(List<Integer> IdsProjectionsASupprimer);

	public int deleteByListeIdEvolutionNonLocalisee(
			List<Integer> evolutionsNonLocaliseesASupprimerId);
}
