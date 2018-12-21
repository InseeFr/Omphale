package fr.insee.omphale.batch.traitementProjection.service;

import fr.insee.omphale.batch.transversal.exception.OmphaleConfigException;
import fr.insee.omphale.batch.transversal.exception.OmphaleMetierException;
import fr.insee.omphale.generationDuPDF.exception.OmphalePopulationNegativeException;

public interface IServiceBatch {
    public void executeService() throws OmphaleConfigException, OmphaleMetierException, OmphalePopulationNegativeException;
}

