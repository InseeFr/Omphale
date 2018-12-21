package fr.insee.omphale.generationDuPDF.service.projection.impl;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import fr.insee.omphale.generationDuPDF.service.projection.IJFreeChartDecService;


public class JFreeChartDecService implements IJFreeChartDecService {
	
	private JFreeChart chart;
	
	/**
     * Création org.jfree.chart.JFreeChart
     *
     * @param titre titre du graphique
     * @param age100 ex. 99
     * @param hashMap contient :
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- les années de début (ex. 2006), de début + 5 (ex. 2011), de fin -5 (ex. 2026), de fin (ex. 2031)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- des libellés "Flux observés au RP", "au RP"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- le libellé du zonage
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- un indicateur si le zonage est un zonage utilisateur 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- le libellé de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- la date d'exécution de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- un indicateur si la projection est calée ou non
	 * @param donnees données
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(0) = ArrayList  hommes
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(1) = ArrayList  femmes
     */
    @SuppressWarnings("static-access")
	public JFreeChartDecService(
    		String titre, 
    		Integer age100,
    		Map<String, String> hashMap,
    		Map<Integer, ArrayList<Double>> donnees) {


        JFreeChartGraphiqueService graphiqueService = new JFreeChartGraphiqueService();
        
        // create the chart...

        chart = ChartFactory.createXYLineChart(
        		titre, 
                "Age", "",
                createDataset(age100, hashMap, donnees),
                PlotOrientation.VERTICAL, 
                true,  // legend
                true,  // tool tips
                false  // URLs
            );
            
            XYPlot plot = (XYPlot) chart.getPlot(); 
            
            XYItemRenderer xYItemRenderer = plot.getRenderer(); 
            
    		/* Color */
    		xYItemRenderer.setSeriesPaint(0, Color.blue); 
            xYItemRenderer.setSeriesPaint(1, Color.red); 

            //
            graphiqueService.setColor(chart, plot);
            
            /* stroke */
    		BasicStroke stroke1 = new BasicStroke(1); // 1 : pour impression
    		xYItemRenderer.setSeriesStroke(0, stroke1);
            xYItemRenderer.setSeriesStroke(1, stroke1);
            
            graphiqueService.setTickMarkStroke(plot, stroke1);
            
            /* Font */
            Font titleFont = graphiqueService.titleFont; 
            Font axisLabelFont = graphiqueService.axisLabelFont;
            Font tickLabelFont = graphiqueService.tickLabelFont;
            Font legendeFont = graphiqueService.legendeFont;  
            
            /* title */
            graphiqueService.setTitle(chart, titleFont);       

            /* légende */
            graphiqueService.setLegend(chart, legendeFont, true);
            
            /* abcisses */
            NumberAxis abscisses = graphiqueService.getAxeAbscisses(plot, 0, age100, axisLabelFont);
            
            // tickUnits
            abscisses.setAutoTickUnitSelection(true);
            TickUnits tickUnits = new TickUnits();
            tickUnits.add(new NumberTickUnit(5));
            tickUnits.add(new NumberTickUnit(10));
            abscisses.setStandardTickUnits(tickUnits);
            
            //
            graphiqueService.formatAxe(abscisses, tickLabelFont);
            
            // 2ème axe abcisses
            graphiqueService.add2emeAxeAbcisses(plot, abscisses);
           
            /* ordonnées */
            NumberAxis rangeAxis = graphiqueService.getAxeOrdonnees(plot, 2, axisLabelFont);
            
            // tickUnits
            rangeAxis.setAutoTickUnitSelection(true);
            tickUnits = new TickUnits();
            tickUnits.add(new NumberTickUnit(0.05));
            tickUnits.add(new NumberTickUnit(0.1));
            tickUnits.add(new NumberTickUnit(0.25));
            tickUnits.add(new NumberTickUnit(0.5));
            tickUnits.add(new NumberTickUnit(1));
            
            rangeAxis.setStandardTickUnits(tickUnits);

            graphiqueService.formatAxe(rangeAxis, tickLabelFont);
            
            // 2ème axe ordonnées
            graphiqueService.add2emeAxeCoordonnees(plot, rangeAxis);
    }
    
    
    /**
    *
    * Ex. : 
    * <br>
    * series0 = new XYSeries("Hommes");   
    * <br>
    * series1 = new XYSeries("Femmes");
    * <br>
    * dataset.addSeries(series0);
    * <br>
    * return dataset;
    * 
	 * @param age100 ex. 99
	 * @param hashMap contient :
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- les années de début (ex. 2006), de début + 5 (ex. 2011), de fin -5 (ex. 2026), de fin (ex. 2031)
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- des libellés "Flux observés au RP", "au RP"
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- le libellé du zonage
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- un indicateur si le zonage est un zonage utilisateur 
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- le libellé de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- la date d'exécution de la projection
	 * 		<br>
	 * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 * 		- un indicateur si la projection est calée ou non
	 * @param donnees données
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(0) = ArrayList  hommes
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(1) = ArrayList  femmes
	 * @return dataset
	 */
    private XYDataset createDataset(
    		Integer age100,
    		Map<String, String> hashMap,
    		Map<Integer, ArrayList<Double>> donnees) {
 
    	StringBuffer str = new StringBuffer();
    	str.append("Hommes ");
    	str.append(hashMap.get("anneeDebut"));
        XYSeries series0 = new XYSeries(str.toString());   
        str = new StringBuffer();
    	str.append("Femmes ");
    	str.append(hashMap.get("anneeDebut"));
        XYSeries series1 = new XYSeries(str.toString());
        for (int i = 0; i <= age100; i++) {
        	series0.add(Integer.valueOf(i), (Double) donnees.get(Integer.valueOf(0)).get(i));
        	series1.add(Integer.valueOf(i), (Double) donnees.get(Integer.valueOf(1)).get(i));
		}

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series0);
        dataset.addSeries(series1);
        dataset.setIntervalWidth(0.0);
        return dataset;
    }
       
	public JFreeChart getChart() {
		return chart;
	}

}

