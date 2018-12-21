package fr.insee.omphale.generationDuPDF.service.projection;

import org.jfree.chart.JFreeChart;

/**
 * Création org.jfree.chart.JFreeChart à partir des données.
 * <br>
 * classe appelée par {@link fr.insee.omphale.generationDuPDF.service.projection.impl.ITextEGraphiques1Service}.
 * 
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextEGraphiques1Service
 */
public interface IJFreeChartCourbeService {

    
	/**
	 * Création org.jfree.chart.JFreeChart à partir des données.
	 * 
	 * @return org.jfree.chart.JFreeChart
	 */
	public JFreeChart getChart();
	
	
}
