package fr.insee.omphale.core.service.geographie;

import java.util.List;

import fr.insee.omphale.domaine.geographie.TypeZoneStandard;

public interface ITypeZoneStandardService {

	/**
	 * Recherche un TypeZoneStandard en base, selon son identifiant
	 * 
	 * @param id
	 * @return TypeZoneStandard
	 */
	public TypeZoneStandard findById(int id);

	/**
	 * Recherche la liste des TypeZoneStandard en base
	 * 
	 * @return List(TypeZoneStandard)
	 */
	public List<TypeZoneStandard> findAll();
}
