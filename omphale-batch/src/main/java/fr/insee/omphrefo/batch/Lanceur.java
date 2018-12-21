package fr.insee.omphrefo.batch;


import fr.insee.omphale.batch.transversal.exception.OmphaleException;

public class Lanceur {

    static private EBatchs batchEnum;
    static private String nomBatch;

    
	public static void main(String[] args) {
	     try {
	            nomBatch = args[0];

	            batchEnum = null;
	            try {
	                batchEnum = EBatchs.getEnum(nomBatch);
	            } catch (OmphaleException e) {
	                throw new OmphaleException(
	                        "Erreur batch lanceur"
	                                + nomBatch, e);
	            }

	            Batch batch = Class
	                    .forName(
	                            batchEnum.getNomPackage() + "."
	                                    + batchEnum.getNomClasse())
	                    .asSubclass(Batch.class).newInstance();

	            String[] pargs;
	            pargs = new String[args.length - 1];
	            System.arraycopy(args, 1, pargs, 0, pargs.length);

	            RetourBatch retourbatch = batch.executer(pargs);

	            endProgram(retourbatch);
	      } catch (Exception e) {
	            endProgram(new RetourBatch(ECodeRetour.ECHEC.getCode(),
	                    e.getMessage()));
	        }
	}
	     
	     
	 private static void endProgram(RetourBatch retourbatch) {
	        
	         System.exit(retourbatch.getStatus());
	     }

	}


