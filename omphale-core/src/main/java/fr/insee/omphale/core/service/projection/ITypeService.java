package fr.insee.omphale.core.service.projection;

import java.util.TreeMap;


public interface ITypeService {

	/**
	 * générèe une treeMap pour la liste déroulante
	 * <BR>
	 * standard | personnel
	 * 
	 * @return TreeMap(Boolean,String)
	 */
	public TreeMap<Boolean, String> getTreeMap();
}
