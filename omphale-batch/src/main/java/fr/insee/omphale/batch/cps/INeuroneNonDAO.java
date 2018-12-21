package fr.insee.omphale.batch.cps;

import java.util.ArrayList;
/**
 * Interface des neurones qui n'utilisent pas les services
 * DAO du cerveau.
 * La récupération des faits de la mémoire profonde est entierement
 * sous la responsabilité du neurone (méthode getDeepMemory()).
 *
 */
public interface INeuroneNonDAO extends INeurone{
    /**
     * Extrait les faits de la mémoire profonde
     * Chaque fait est lui_même une collection dont les
     * deux premiers éléments sont le type et la priorité.
     * Un fait constituera les propriétés d'un neurone.
     * @return Collection  des faits bruts issus de la mémoire profonde.
     */
    public ArrayList < Object  > getDeepMemory()throws Exception;
}
