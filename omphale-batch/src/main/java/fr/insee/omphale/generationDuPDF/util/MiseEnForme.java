package fr.insee.omphale.generationDuPDF.util;


public class MiseEnForme {

	
	//Methode pour trouver l'annee précédente à partir d'une annee donnee en String
	public Object anneePrecedente(String annee) {
		Integer anneePrecedente = null;
		try {
			anneePrecedente = Integer.parseInt(annee) - 1;
			
		} catch (Exception e) {
			new Exception(e);
		}
		return ""+anneePrecedente;

	}

}
