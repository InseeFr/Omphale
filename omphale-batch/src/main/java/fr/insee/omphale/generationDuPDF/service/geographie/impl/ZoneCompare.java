package fr.insee.omphale.generationDuPDF.service.geographie.impl;

import java.util.Comparator;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class ZoneCompare implements Comparator{
	
	private Map<String, String> zones;//pour garder une copie du Map que l'on souhaite traiter
	
	public ZoneCompare(Map<String, String> zones) {
		this.zones = zones; //stocker la copie pour qu'elle soit accessible dans compare()
	}

	

      
    public int compare(Object key1, Object key2){
        //récupérer les value du Map par leur identifiant
        String value1 = zones.get(key1);
        String value2 = zones.get(key2);
        if (value1.compareTo(value2)==0){
        	 return -1;
        }else {
        	 return value1.compareTo(value2);
        }
    }
}
