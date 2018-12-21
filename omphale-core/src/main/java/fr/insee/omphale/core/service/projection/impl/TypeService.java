package fr.insee.omphale.core.service.projection.impl;

import java.util.TreeMap;

import fr.insee.omphale.core.service.projection.ITypeService;
import fr.insee.omphale.ihm.util.ParametresForm;

/**
 * Classe gérant les fonctions de la couche service pour Type
 *
 */
public class TypeService implements ITypeService {
	
	public TreeMap<Boolean, String> getTreeMap(){
		
		TreeMap<Boolean, String> treeMap = new TreeMap<Boolean, String>();
		treeMap.put(new Boolean(false), ParametresForm.getString("nonStandard.libelle"));
		treeMap.put(new Boolean(true), ParametresForm.getString("standard.libelle"));
		return treeMap;
	}

}