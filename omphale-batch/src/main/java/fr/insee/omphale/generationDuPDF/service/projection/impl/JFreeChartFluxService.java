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


public class JFreeChartFluxService {
	
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
     * @param fluxMax fluxMax utilisé pour définir le range du graphique
     * @param legende true si visible, false sinon
     */
    @SuppressWarnings("static-access")
	public JFreeChartFluxService(
    		String title, 
    		Integer age100,
    		Map<Integer, ArrayList<Double>> hashMap, 
    		Double fluxMax) {

        JFreeChartGraphiqueService graphiqueService = new JFreeChartGraphiqueService();
        
        // create the chart...

        chart = ChartFactory.createXYLineChart(
                title, 
                "Age", "Flux",
                createDataset(age100, hashMap),
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
            graphiqueService.setLegend(chart, legendeFont, false);
            
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
            NumberAxis rangeAxis = graphiqueService.getAxeOrdonnees(plot, 0, axisLabelFont);
            
            rangeAxis.setLabelAngle(Math.PI / 2.0); 
            
            // range
            rangeAxis.setRange(0, fluxMax * 1.02);
            
            // tickUnits
            rangeAxis.setAutoTickUnitSelection(true);
            tickUnits = new TickUnits();
            tickUnits.add(new NumberTickUnit(1));
            tickUnits.add(new NumberTickUnit(2));
            tickUnits.add(new NumberTickUnit(5));
            tickUnits.add(new NumberTickUnit(10));
            tickUnits.add(new NumberTickUnit(25));
            tickUnits.add(new NumberTickUnit(50));
            tickUnits.add(new NumberTickUnit(100));
            tickUnits.add(new NumberTickUnit(250));
            tickUnits.add(new NumberTickUnit(500));
            tickUnits.add(new NumberTickUnit(1000));
            tickUnits.add(new NumberTickUnit(2500));
            tickUnits.add(new NumberTickUnit(5000));
            tickUnits.add(new NumberTickUnit(10000));
            tickUnits.add(new NumberTickUnit(25000));
            tickUnits.add(new NumberTickUnit(50000));
            tickUnits.add(new NumberTickUnit(100000));
            tickUnits.add(new NumberTickUnit(200000));
            
            rangeAxis.setStandardTickUnits(tickUnits);

            graphiqueService.formatAxe(rangeAxis, tickLabelFont);
            
            // 2ème axe ordonnées
            graphiqueService.add2emeAxeCoordonnees(plot, rangeAxis);
    }

    /**
    *
    * Ex. : années 2006, 2011, 2031.
    * <br>
    * series0 = new XYSeries("Hommes 2006");   
    * <br>
    * series1 = new XYSeries("Femmes 2006"); 
    * <br>
    * dataset.addSeries(series0);
    * <br>
    * dataset.addSeries(series1);
    * <br>
    * return dataset;
    * 
	* @param hashMap 
	* <br>
	* Ex. :
	* <br>
    * hashMap.get(0) = ArrayList  hommes 2006
    * <br>
    * hashMap.get(1) = ArrayList  femmes 2006
    * <br>
    * hashMap.get(2) = ArrayList  hommes 2011
    * <br>
    * hashMap.get(3) = ArrayList  hommes 2031
    * <br>
    * hashMap.get(4) = ArrayList  femmes 2011
    * <br>
    * hashMap.get(5) = ArrayList  femmes 2031
    */

    
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
    		Map<Integer, ArrayList<Double>> hashMap) {
 
        XYSeries series0 = new XYSeries("Hommes");   
        XYSeries series1 = new XYSeries("Femmes");
        for (int i = 0; i <= age100; i++) {
        	series0.add(Integer.valueOf(i), (Double) hashMap.get(Integer.valueOf(0)).get(i));
        	series1.add(Integer.valueOf(i), (Double) hashMap.get(Integer.valueOf(1)).get(i));
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

