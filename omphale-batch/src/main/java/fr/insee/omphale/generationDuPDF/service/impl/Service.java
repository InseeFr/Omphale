package fr.insee.omphale.generationDuPDF.service.impl;

import java.util.Iterator;
import java.util.Set;

import fr.insee.omphale.generationDuPDF.dao.DAOFactoryPDF;


public class Service {
	static public DAOFactoryPDF daoFactoryPDF;
	static {
		daoFactoryPDF = DAOFactoryPDF.instance(DAOFactoryPDF.PERSISTANCE);
	}

	
	public static String tableauToString(Set<String> set) {

		StringBuffer str = new StringBuffer();
		int i = 0;
		str.append("(");
		Iterator<String> it1 = set.iterator();
		while(it1.hasNext()) {
			str.append("'");
			str.append(it1.next());
			str.append("'");
			if (i < set.size() - 1) {
				str.append(", ");
			}
			i++;
		}
		str.append(")");
		return str.toString();
	}

}