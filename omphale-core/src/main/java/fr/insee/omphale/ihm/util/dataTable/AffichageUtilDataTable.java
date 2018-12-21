package fr.insee.omphale.ihm.util.dataTable;

import java.text.SimpleDateFormat;
import java.util.Date;



public class AffichageUtilDataTable {

	public AffichageUtilDataTable() {
	}
	
	
	/**
	 * utile pour DataTable
	 * DataTable pose problème pour tri colonne avec date eu mais pas avec date uk
	 * 
	 * d'où transformation de la date eu récupérée de la BDD en uk
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateAAfficher(Date date){
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY \340 HH:mm:ss");
		String dateCreationAffichage = sdf.format(date);
		
		return dateCreationAffichage;
	}

}
