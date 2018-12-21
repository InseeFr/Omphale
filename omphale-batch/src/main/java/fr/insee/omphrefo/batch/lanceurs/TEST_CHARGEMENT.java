package fr.insee.omphrefo.batch.lanceurs;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import fr.insee.omphale.utilitaireDuGroupeJava2010.util.ContexteApplication;
import fr.insee.omphrefo.batch.Batch;
import fr.insee.omphrefo.batch.ECodeRetour;
import fr.insee.omphrefo.batch.RetourBatch;



public class TEST_CHARGEMENT implements Batch {

	

    
    public RetourBatch executer(String[] args) {
    	try {

    		String nomTable = "zz_temp_"+args[1];
    		Connection connection = ContexteApplication.getConnectionManager().getConnection("omphale");
    		PreparedStatement st = connection.prepareStatement("SELECT * from "+ nomTable);
    		
    		st.execute();
    		
    		// initialisation de la table et de ses colonnes
    		Table table = initialisationTable(st, nomTable);
    		
    		//initialisation du fichier et de la ligne d'entête
    		File file = new File("//opt//insee//omphrefo//recette//files//chargement//csv//"+args[1]+".csv");
    		BufferedReader reader = new BufferedReader(new FileReader(file));
    		String entetesFichier = reader.readLine(); //header
    		
    		// l'ordre des entêtes du fichier et de la table peut être différent
    		//  initialisation de la table de correspondance des entêtes du fichier et de la table
    		// donc vérification du nombre et de l'égalité des libellés de la table et du fichier
    		Map<Integer, String> correspondanceFichierVsTable = verificationEtInitialisationEnteteEnVueDeLaRequete(table, entetesFichier);
    		
    		// initialisation de la chaîne de ? pour l'insert fonction du nombre de colonne de la table à charger
    		String chaineIndexInsertSQL = renvoieChaineIndexInsertSQL(correspondanceFichierVsTable);
    		
    		// préparation de la requête insert 
    		st = connection.prepareStatement("INSERT INTO "+nomTable+" VALUES("+chaineIndexInsertSQL+")");
    		int batchSize = 200000;
    		
    		// lecture des lignes du fichier source
        	String ligne = null;

        	int nbLigne = 0;

        	while ((ligne = reader.readLine()) != null) {
        		String ligneCorrigee = ligne.replace("\r", "").trim();
        		
        		if (!("").equals(ligneCorrigee)){
            		chargementDuneLigneDuFichier(table, ligneCorrigee, correspondanceFichierVsTable, st);
            		
            		
        			if (nbLigne % batchSize == 0) {
        				st.executeBatch();
        				nbLigne = 0;
        			}
        			nbLigne++;
        		}
    		}
    		st.executeBatch();
    		reader.close();
    		connection.commit();
    		connection.close();
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	 return new RetourBatch(
                 ECodeRetour.EXECUTION_CORRECTE.getCode(), "Tout est OK");
    }
    public String renvoieChaineIndexInsertSQL(Map<Integer, String> correspondanceFichierVsTable){
    	String resultat = "?,";
    	
    	for (int i = 1; i < correspondanceFichierVsTable.size(); i++){
    		resultat = resultat + "?,";
    	}
    	

    	return resultat.substring(0,resultat.length()-1);
    }
    public void chargementDuneLigneDuFichier(
    														Table table, 
    														String ligneCorrigee, 
    														Map<Integer, String> correspondanceFichierVsTable,
    														PreparedStatement st
    													) 
    													throws Exception{
    		
			String[] data = ligneCorrigee.split(",");
        	// on parcourt la map de correspondance 
    		Set<Integer> listeRangDonnee = correspondanceFichierVsTable.keySet();
    		Iterator<Integer> it = listeRangDonnee.iterator();
    		while (it.hasNext()){

    			Integer randDonnee = it.next(); 
				String nomColonne = correspondanceFichierVsTable.get(randDonnee);
				Colonne colonne = table.getColonnes().get(nomColonne);
				
					// si la donnée ne dépasse pas la taille limite en BDD
					if(data[randDonnee].trim().length() <= colonne.getTaille()){
						// initialisation de setter correspondant au type de la colonne cible
						if(BigDecimal.class.getName().equals(colonne.getType())){
							st.setBigDecimal(colonne.getRang(), new BigDecimal(data[randDonnee]));
						}
						else if(String.class.getName().equals(colonne.getType())){
							st.setString(colonne.getRang(), data[randDonnee]);
						}else{
							throw new Exception("le type de la donnee n'est pas prévu dans les colonnes de la table cible");
						}
					}else{
						throw new Exception("la taille de la donnee : '"+data[randDonnee]+"' de la ligne '"+ ligneCorrigee + "' dépasse la taille de la colonne de la table");
					}

				}
    		st.addBatch();
    }
    
    public Table initialisationTable(PreparedStatement st, String nomTable) throws SQLException{
    	
    	Table table = new Table();
		table.setNom(nomTable);
		table.setColonnes(new LinkedHashMap<String, Colonne>());
		for(int i =0 ; i < st.getMetaData().getColumnCount(); i++){
			Colonne col = new Colonne();
			col.setNom(st.getMetaData().getColumnName(i+1).toUpperCase());
			col.setType(st.getMetaData().getColumnClassName(i+1));
			col.setTaille(st.getMetaData().getColumnDisplaySize(i+1));
			col.setRang(i+1);
			table.getColonnes().put(col.getNom(),col);
		}
    	return table;
    }
    
    public Map<Integer, String> verificationEtInitialisationEnteteEnVueDeLaRequete(Table table, String entetesFichier) throws Exception{
    	
    	Map<Integer, String> resultat = new LinkedHashMap<Integer, String>();
    	
    	String[] data = entetesFichier.split(",");

    	// on vérifie le nombre d'entêtes 
    	if(!(table.getColonnes().size() == data.length)){
    		throw new Exception("Le nombre de colonne n'est pas identique entre la table cible et le fichier source.");
    	}
    	
    	// on vérifie que les entêtes de la table et du fichier sont identiques

		Set<String> colonnes = table.getColonnes().keySet();
		Iterator<String> it = colonnes.iterator();
		// pour chaque colonne
		while (it.hasNext()){
			String nomColonne = it.next(); 

			boolean estEgale =  false;
			int numeroEntete = 0;
	
			// on vérifie qu'un entête du fichier est égal au nom de la colonne courante
			for(int i = 0; i < data.length; i++){
				// si égalité
				if(data[i].trim().toUpperCase().equals(nomColonne)){
					// on fait correspondre le rang de l'entête du fichier et le nom de la colonne 
					resultat.put(i, nomColonne);
					// on indique que l'égalité est valide
					estEgale = true;
					break;
				}
				numeroEntete++;
			}
			
			if(!estEgale){
	    		throw new Exception("L'entête du fichier source '" + data[numeroEntete]+"' ne correspond à aucun nom de colonne de la table cible"  );
			}
			

		}
		
    	return resultat;
    }
    
 
	

}
