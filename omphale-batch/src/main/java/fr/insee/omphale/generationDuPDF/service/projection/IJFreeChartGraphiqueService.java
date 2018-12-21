package fr.insee.omphale.generationDuPDF.service.projection;

import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;

/**
 * Utilitaire
 * 
 * @see fr.insee.omphale.generationDuPDF.service.projection.impl.ITextEGraphiques1Service
 */
public interface IJFreeChartGraphiqueService {
	
	/**
	 * ajout 2ème axe des coordonnées
	 * @param plot
	 * @param rangeAxis
	 */
	public void add2emeAxeCoordonnees(XYPlot plot, NumberAxis rangeAxis);
	
	/**
	 * ajout 2ème axe des abcisses
	 * @param plot
	 * @param domainAxis
	 */
	public void add2emeAxeAbcisses(XYPlot plot, ValueAxis domainAxis);

}
