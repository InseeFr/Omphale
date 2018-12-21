package fr.insee.omphale.generationDuPDF.service.projection.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.Axis;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.HorizontalAlignment;

import fr.insee.omphale.generationDuPDF.service.projection.IJFreeChartGraphiqueService;

public class JFreeChartGraphiqueService implements IJFreeChartGraphiqueService{

	static final String strTimesNewRoman = "Times New Roman";
	static Font titleFont = new Font(strTimesNewRoman,Font.PLAIN,JFreeChart.DEFAULT_TITLE_FONT.getSize() - 6); 
	static Font axisLabelFont = new Font(strTimesNewRoman,Font.PLAIN, JFreeChart.DEFAULT_TITLE_FONT.getSize() - 6); 
	static Font tickLabelFont = new Font(strTimesNewRoman,Font.PLAIN,JFreeChart.DEFAULT_TITLE_FONT.getSize()- 7); 
	static Font legendeFont = new Font(strTimesNewRoman,Font.PLAIN,JFreeChart.DEFAULT_TITLE_FONT.getSize()- 7);  
	
	public void setTitle(JFreeChart chart, Font titleFont) {
		
		TextTitle title = chart.getTitle();
		HorizontalAlignment horizontalAlignment = HorizontalAlignment.CENTER;
		title.setHorizontalAlignment(horizontalAlignment);
		title.setFont(titleFont);    
	}
	
	public void setLegend(JFreeChart chart, Font legendeFont, boolean visible) {
		
        LegendTitle legende = chart.getLegend();
        legende.setFrame(new BlockBorder(Color.white));
        
        legende.setVisible(visible);
        
        legende.setItemFont(legendeFont); 
	}
	
	public void setTickMarkStroke(XYPlot plot, BasicStroke stroke1) {
		
		plot.getDomainAxis().setTickMarkStroke(stroke1);
        plot.getRangeAxis().setTickMarkStroke(stroke1);
	}
	
	// utilisé sauf PyramideAgeService
	public NumberAxis getAxeAbscisses(XYPlot plot, double debut, double fin, Font axisLabelFont) {
		
		NumberAxis domainAxis = (NumberAxis) plot.getDomainAxis();
        domainAxis.setRange(debut, fin);
		domainAxis.setLabelFont(axisLabelFont);
        domainAxis.setLabelAngle(0); 
        
        return domainAxis;
	}
	
	public void formatAxe(Axis axis, Font tickLabelFont) {
		
		axis.setTickMarkInsideLength(4);
		
        axis.setTickMarkOutsideLength(0);
        
        axis.setAxisLineVisible(false);

        axis.setTickLabelFont(tickLabelFont);
	}
	
	// ordonnées
	// utilisé sauf PyramideAgeService
	public NumberAxis getAxeOrdonnees(XYPlot plot, int maximumFractionDigits, Font axisLabelFont) {
		
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setLabelFont(axisLabelFont);

        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(maximumFractionDigits);
        rangeAxis.setNumberFormatOverride(decimalFormat);		
        
        return rangeAxis;
	}
	
	public void setColor(JFreeChart chart, XYPlot plot) {
		
		Color c = Color.BLACK;
	    chart.getTitle().setPaint(c);
	    plot.getDomainAxis().setLabelPaint(c);
	    plot.getDomainAxis().setTickLabelPaint(c);
	    plot.getRangeAxis().setLabelPaint(c);
	    plot.getRangeAxis().setTickLabelPaint(c);
	    chart.getLegend().setItemPaint(c);
	    
	    plot.getDomainAxis().setTickMarkPaint(c);
	    plot.getRangeAxis().setTickMarkPaint(c);
	               
	    plot.setBackgroundPaint(Color.white);
	}
	
	public void add2emeAxeCoordonnees(XYPlot plot, NumberAxis rangeAxis) {
		
		NumberAxis rangeAxis2 = new NumberAxis("");
        plot.setRangeAxis(1, rangeAxis2);
        plot.setRangeAxisLocation(1, AxisLocation.TOP_OR_RIGHT);
        rangeAxis2.setRange(rangeAxis.getRange());
        rangeAxis2.setLabelFont(rangeAxis.getLabelFont());
        rangeAxis2.setTickLabelFont(rangeAxis.getTickLabelFont()); 
        rangeAxis2.setTickLabelPaint(rangeAxis.getTickLabelPaint());
        rangeAxis2.setStandardTickUnits(rangeAxis.getStandardTickUnits());
        rangeAxis2.setNumberFormatOverride(rangeAxis.getNumberFormatOverride());
        rangeAxis2.setTickLabelsVisible(false);
        rangeAxis2.setTickMarkStroke(rangeAxis.getTickMarkStroke());
        rangeAxis2.setTickMarkInsideLength(rangeAxis.getTickMarkInsideLength());
        rangeAxis2.setTickMarkOutsideLength(rangeAxis.getTickMarkOutsideLength());
        rangeAxis2.setTickMarkPaint(rangeAxis.getTickMarkPaint());
        rangeAxis2.setAxisLineVisible(rangeAxis.isAxisLineVisible());
	}
	
	public void add2emeAxeAbcisses(XYPlot plot, ValueAxis domainAxis) {
		
        NumberAxis domainAxis2 = new NumberAxis("");
        plot.setDomainAxis(1, domainAxis2);
        plot.setDomainAxisLocation(1, AxisLocation.TOP_OR_RIGHT);
        domainAxis2.setRange(domainAxis.getRange());
        domainAxis2.setLabelFont(domainAxis.getLabelFont());
        domainAxis2.setTickLabelFont(domainAxis.getTickLabelFont());
        domainAxis2.setTickLabelPaint(domainAxis.getTickLabelPaint());
        domainAxis2.setStandardTickUnits(domainAxis.getStandardTickUnits());
        domainAxis2.setTickLabelsVisible(false);
        domainAxis2.setTickMarkStroke(domainAxis.getTickMarkStroke());
        domainAxis2.setTickMarkInsideLength(domainAxis.getTickMarkInsideLength());
        domainAxis2.setTickMarkOutsideLength(domainAxis.getTickMarkOutsideLength());
        domainAxis2.setTickMarkPaint(domainAxis.getTickMarkPaint());
        domainAxis2.setAxisLineVisible(domainAxis.isAxisLineVisible());
	}
}

