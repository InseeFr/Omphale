package fr.insee.omphale.batch.cps;

import java.util.ArrayList;

/**
 * Interface de base des neurones du pattern CPS. Un neurone est une sorte de
 * bean intelligent. Il est constitué de propriétés, et équipé de méthodes. Les
 * méthodes indispensables sont signifiées dans les interfaces. Les propriétés
 * d'un neurone correspondent à 4 catégories : - La propriété CLE, qui indique à
 * la synapse, la séquence de rangement du neurone. - Les arguments de création
 * correspondent aux propriétés utilisées pour créer la synapse. Il s'agit des
 * informations qui paramètrent la récupération des faits de la mémoire
 * profonde. - Les arguments de recherche. Il s'agit des propriétés à partir
 * desquelles on interroge la synapse. - Les résultats. Il s'agit des
 * informations supplémentaires portées par les faits de la mémoire profonde.
 *
 */
public interface INeurone {
	/**
	 * renvoie la clé de rangement dans la map de la synapse.
	 *
	 * @return la clé (string)
	 */
	public String getMapKey();

	/**
	 * Méthode vérifiant si la règle est applicable.
	 *
	 * @param modele
	 *            regle modèle
	 * @return vrai ou faux selon que la règle est applicable ou non
	 * @throws Exception en cas d'erreur d'entrée sortie ou applicative
	 */
	public boolean parseNeurone(INeurone modele) throws Exception;

	/**
	 * modifier les propriétés d'un neurone à partir d'un ArrayList fait. Il
	 * s'agit des propriétés de type arguments de recherche .
	 *
	 * @param fait
	 *            ArrayList
	 */
	/**
	 * modifier les propriétés d'un neurone à partir d'un ArrayList fait. Il
	 * s'agit des propriétés de type arguments de création .
	 *
	 * @param fait
	 *            ArrayList
	 */
	public void setCreateSynapseParameters(Object fait);

	public void setRechercheParameters(Object fait);

	/**
	 * modifier les propriétés d'un neurone à partir d'un ArrayList fait. Cette
	 * méthode est utilisée par la synapse pour charger les faits issus de la
	 * mémoire profonde.
	 *
	 * @param fait
	 *            ArrayList
	 */
	public void setDeepFait(Object fait);

	/**
	 * cette méthode, appelée à l'issue de la création d'une synapse, est
	 * destinée à permettre d'ajouter de l'interopérabilité aux neurones. Soit
	 * par l'initialisation de propriétés d'état, soit par chainâge des
	 * neurones. Par exemple, structuration des neurones en arbre.
	 *
	 * @param neurones
	 *            Il s'agit de la collection complète des neurones de la
	 *            synapse.
	 * @throws Exception en cas d'erreur applicative
	 */
	public void collabo(ArrayList<INeurone> neurones) throws Exception;
}
