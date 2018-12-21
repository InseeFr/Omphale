package fr.insee.omphale.core.service;

import java.util.List;

public interface IParametresService {
	/**
	 * récupère la liste des cycles ouverts
	 * 
	 * @return {@link List}<{@link Integer}>
	 */
	public List<Integer> getCyclesOuverts();
}
