package fr.insee.omphale.generationDuPDF.service.projection.impl;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.List;
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

import fr.insee.omphale.generationDuPDF.service.projection.IJFreeChartPopulationService;


public class JFreeChartPopulationService implements IJFreeChartPopulationService {

	
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
     * 		ArrayList  population totale
     */
    @SuppressWarnings("static-access")
	public JFreeChartPopulationService(
    		String titre,
    		Map<String, String> hashMap,
    		List<Double> arrayList) {

        
        JFreeChartGraphiqueService graphiqueService = new JFreeChartGraphiqueService();
        
        // create the chart...

        chart = ChartFactory.createXYLineChart(
        		titre, 
                "Annee", "",
                createDataset(hashMap, arrayList),
                PlotOrientation.VERTICAL, 
                true,  // legend
                true,  // tool tips
                false  // URLs
            );
            
            XYPlot plot = (XYPlot) chart.getPlot(); 
           
            XYItemRenderer xYItemRenderer = plot.getRenderer();  
            
    		/* Color */
    		xYItemRenderer.setSeriesPaint(0, Color.green); 

    		graphiqueService.setColor(chart, plot);
            
            /* stroke */
    		BasicStroke stroke1 = new BasicStroke(1); // 1 : pour impression
    		xYItemRenderer.setSeriesStroke(0, stroke1);
            
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
            NumberAxis abscisses = (NumberAxis) graphiqueService.getAxeAbscisses(
													            		plot, 
													            		Integer.valueOf(hashMap.get("anneeDebut")), 
													            		Integer.valueOf(hashMap.get("anneeFin")), 
													            		axisLabelFont);

            // tickUnits 
            abscisses.setAutoTickUnitSelection(true);
            TickUnits tickUnits = new TickUnits();
            tickUnits.add(new NumberTickUnit(5));
            tickUnits.add(new NumberTickUnit(10));
            abscisses.setStandardTickUnits(tickUnits);
            
            graphiqueService.formatAxe(abscisses, tickLabelFont);

            // format
            DecimalFormat decimalFormatAbcisses = new DecimalFormat();
            decimalFormatAbcisses.setMaximumFractionDigits(0);
          	decimalFormatAbcisses.setGroupingUsed(Boolean.valueOf(false));
          	abscisses.setNumberFormatOverride(decimalFormatAbcisses);

            // 2ème axe abcisses
            graphiqueService.add2emeAxeAbcisses(plot, abscisses);           
           
            /* ordonnées */
            NumberAxis rangeAxis = graphiqueService.getAxeOrdonnees(plot, 0, axisLabelFont);
          
    		// range
    		rangeAxis.setAutoRangeIncludesZero(false);
    		rangeAxis.setAutoRange(true);      
            
    		// TickUnits
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
            
            rangeAxis.setStandardTickUnits(tickUnits);

            graphiqueService.formatAxe(rangeAxis, tickLabelFont);
            
            // 2ème axe ordonnées
            graphiqueService.add2emeAxeCoordonnees(plot, rangeAxis);
    }

    /**
    *
    * Ex. : 
    * <br>
    * series0 = new XYSeries("Population totale");   
    * <br>
    * dataset.addSeries(series0);
    * <br>
    * return dataset;
    * 
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
     * 		ArrayList  population totale
	 * @return dataset
	 */
    private XYDataset createDataset(
    		Map<String, String> hashMap,
    		List<Double> donnees) {
 
        XYSeries series0 = new XYSeries("Population totale");   
        
        // 2006 - 2031
        for (int i = 0; i <= Integer.valueOf(hashMap.get("anneeFin")) - Integer.valueOf(hashMap.get("anneeDebut")); i++) {
        	// 2006 + i
        	series0.add(Integer.valueOf(hashMap.get("anneeDebut")) + i, donnees.get(i));
		}

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series0);
        dataset.setIntervalWidth(0.0);
        return dataset;
    }
       
	public JFreeChart getChart() {
		return chart;
	}
}

