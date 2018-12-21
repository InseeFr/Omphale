package fr.insee.omphale.dao;

import java.math.BigDecimal;
import java.util.List;

/**
 * Interface définissant le contrat des méthodes pour les fonctionnalités de la couche DAO pour vérifier en base les cycles ouverts
 *
 */
public interface IParametresDAO {
	public List<BigDecimal> getCyclesOuverts();
}
