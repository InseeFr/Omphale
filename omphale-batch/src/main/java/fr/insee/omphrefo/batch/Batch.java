package fr.insee.omphrefo.batch;

import fr.insee.omphale.batch.transversal.exception.OmphaleException;

public interface Batch {
	 RetourBatch executer(String[] args) throws OmphaleException;
}
