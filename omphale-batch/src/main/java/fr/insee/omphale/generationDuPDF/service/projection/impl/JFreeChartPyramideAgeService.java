package fr.insee.omphale.generationDuPDF.service.projection.impl;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import fr.insee.omphale.generationDuPDF.service.projection.IJFreeChartPyramideAgeService;




public class JFreeChartPyramideAgeService implements IJFreeChartPyramideAgeService {

	
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
     * 		Ex. : années 2006, 2011, 2031
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(0) = ArrayList  hommes 2006
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(1) = ArrayList  femmes 2006
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(2) = ArrayList  hommes 2011
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(3) = ArrayList  hommes 2031
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(4) = ArrayList  femmes 2011
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(5) = ArrayList  femmes 2031
     */
    @SuppressWarnings("static-access")
	public JFreeChartPyramideAgeService(
    		String titre, 
    		Integer age100, 
    		Map<String, String> hashMap,
    		Map<Integer, ArrayList<Double>> donnees) {
        
        JFreeChartGraphiqueService graphiqueService = new JFreeChartGraphiqueService();
        
        // create the chart...
    	
        chart = ChartFactory.createXYLineChart(
        		titre, 
                "Age", "Effectifs",
                createDataset1(age100, hashMap, donnees), // données 2012, 2042
                PlotOrientation.HORIZONTAL, 
                true,  // legend
                true,  // tool tips
                false  // URLs
            );
            
            XYPlot plot = (XYPlot) chart.getPlot(); 
            
            XYItemRenderer xYItemRenderer = plot.getRenderer(); // renderer pyramides des âges 2012, 2042
            
            XYAreaRenderer xYAreaRenderer = new XYAreaRenderer(); // renderer pyramide des âges 2007
            plot.setRenderer(1, xYAreaRenderer); 
            plot.setDataset(1, createDataset(age100, hashMap, donnees)); // données 2007         
            
    		/* Color */
	        xYAreaRenderer.setSeriesPaint(0, new Color(125,125,255));
	        xYAreaRenderer.setSeriesPaint(1, new Color(255,125,125));  
    		xYItemRenderer.setSeriesPaint(0, Color.blue); 
            xYItemRenderer.setSeriesPaint(1, new Color(80,80,255)); 
    		xYItemRenderer.setSeriesPaint(2, Color.red); 
            xYItemRenderer.setSeriesPaint(3, new Color(255,80,80)); 

            //
            graphiqueService.setColor(chart, plot); 
            
            /* stroke */
    		BasicStroke stroke1 = new BasicStroke(1); // 1 : pour impression
    		xYItemRenderer.setSeriesStroke(0, stroke1);
            xYItemRenderer.setSeriesStroke(1, stroke1);
    		xYItemRenderer.setSeriesStroke(2, stroke1);
            xYItemRenderer.setSeriesStroke(3, stroke1);
            
            graphiqueService.setTickMarkStroke(plot, stroke1);
            
            /* Font */
            Font titleFont = graphiqueService.titleFont; 
            Font axisLabelFont = graphiqueService.axisLabelFont;
            Font tickLabelFont = graphiqueService.tickLabelFont;
            Font legendeFont = graphiqueService.legendeFont; 
            
            /* title */
            graphiqueService.setTitle(chart, titleFont);      

            /* légende */
            LegendItemCollection legendItemCollectionRenderer = plot.getLegendItems();
            LegendItemCollection legendItemCollection = new LegendItemCollection();
            legendItemCollection.add(legendItemCollectionRenderer.get(4)); 
            legendItemCollection.add(legendItemCollectionRenderer.get(0)); 
            legendItemCollection.add(legendItemCollectionRenderer.get(1)); 
            legendItemCollection.add(legendItemCollectionRenderer.get(5)); 
            legendItemCollection.add(legendItemCollectionRenderer.get(2)); 
            legendItemCollection.add(legendItemCollectionRenderer.get(3));  
            plot.setFixedLegendItems(legendItemCollection);
            
            graphiqueService.setLegend(chart, legendeFont, true);
                       
            /* ordonnées */
            NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
    		domainAxis.setLabelFont(axisLabelFont);
            domainAxis.setLabelAngle(Math.PI / 2.0);    
            
            // zoom
            domainAxis.setLowerMargin(0.0);
            domainAxis.setUpperMargin(0.0);
                        
            // tickUnits
            domainAxis.setAutoTickUnitSelection(true);
            TickUnits tickUnits = new TickUnits();
            tickUnits.add(new NumberTickUnit(5));
            tickUnits.add(new NumberTickUnit(10));
            domainAxis.setStandardTickUnits(tickUnits);
            
            //
            graphiqueService.formatAxe(domainAxis, tickLabelFont);
            
            // 2ème axe ordonnées
            graphiqueService.add2emeAxeAbcisses(plot, domainAxis);
           
            /* abcisses */
            NumberAxis abscisses = (NumberAxis) plot.getRangeAxis();
    		abscisses.setLabelFont(axisLabelFont);

    		// decimalFormat
            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setNegativePrefix(""); // valeur absolue affichée
            decimalFormat.setMaximumFractionDigits(0);
            abscisses.setNumberFormatOverride(decimalFormat);
            
            // tickUnits
            abscisses.setAutoTickUnitSelection(true);
            tickUnits = new TickUnits();
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
            
            abscisses.setStandardTickUnits(tickUnits);

            graphiqueService.formatAxe(abscisses, tickLabelFont);
            
            // 2ème axe abcisses
            graphiqueService.add2emeAxeCoordonnees(plot, abscisses);
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

    * @param age100 ex. 99
	* @param hashMapcontient :
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
     * 		Ex. : années 2006, 2011, 2031
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(0) = ArrayList  hommes 2006
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(1) = ArrayList  femmes 2006
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(2) = ArrayList  hommes 2011
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(3) = ArrayList  hommes 2031
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(4) = ArrayList  femmes 2011
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(5) = ArrayList  femmes 2031
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
    
	/**
	 * Ex. : années 2006, 2011, 2031.
	 * <br>
	 * series2 = new XYSeries("Hommes 2011");  
	 * <br> 
     * series3 = new XYSeries("Hommes 2031");
     * <br>
     * series4 = new XYSeries("Femmes 2011");
     * <br>
     * series5 = new XYSeries("Femmes 2031");	
     * <br>
     * dataset.addSeries(series2);
     * <br>
     * dataset.addSeries(series3);
     * <br>
     * dataset.addSeries(series4);
     * <br>
     * dataset.addSeries(series5);
     * <br>
     * return dataset;
     * 
	 * @param age100 ex. 99
	 * @param hashMapcontient :
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
     * 		Ex. : années 2006, 2011, 2031
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(0) = ArrayList  hommes 2006
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(1) = ArrayList  femmes 2006
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(2) = ArrayList  hommes 2011
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(3) = ArrayList  hommes 2031
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(4) = ArrayList  femmes 2011
     * 		<br>
     * 		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
     * 		donnees.get(5) = ArrayList  femmes 2031
	 * @return dataset
	 */
	private XYDataset createDataset1(
    			Integer age100,
    			Map<String, String> hashMap,
	    		Map<Integer, ArrayList<Double>> donnees) {
 
    	StringBuffer str = new StringBuffer();
    	str.append("Hommes ");
    	str.append(hashMap.get("anneeDebutPlus5"));
        XYSeries series2 = new XYSeries(str.toString());   
        str = new StringBuffer();
    	str.append("Hommes ");
    	str.append(hashMap.get("anneeFin"));
        XYSeries series3 = new XYSeries(str.toString());
        str = new StringBuffer();
    	str.append("Femmes ");
    	str.append(hashMap.get("anneeDebutPlus5"));
        XYSeries series4 = new XYSeries(str.toString());
        str = new StringBuffer();
    	str.append("Femmes ");
    	str.append(hashMap.get("anneeFin"));
        XYSeries series5 = new XYSeries(str.toString());	 
        for (int i = 0; i <= age100; i++) {
        	series2.add(Integer.valueOf(i), (Double) donnees.get(Integer.valueOf(2)).get(i));
        	series3.add(Integer.valueOf(i), (Double) donnees.get(Integer.valueOf(3)).get(i));
        	series4.add(Integer.valueOf(i), (Double) donnees.get(Integer.valueOf(4)).get(i));
        	series5.add(Integer.valueOf(i), (Double) donnees.get(Integer.valueOf(5)).get(i));
		}

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
        dataset.addSeries(series5);
        dataset.setIntervalWidth(0.0);
        return dataset;
    }
       
	public JFreeChart getChart() {
		return chart;
	}
}

