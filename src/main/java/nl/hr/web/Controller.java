package nl.hr.web;

/**
 * Created by j on 6/20/16.
 */

import nl.hr.Algorithms.DoubleES;
import nl.hr.Algorithms.SimpleES;
import nl.hr.domain.AllInOneStuff;
import nl.hr.domain.MetaData;
import nl.hr.domain.Tuple;
import nl.hr.domain.TwoDPoints;
import nl.hr.services.LoadDataService;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

@RestController
public class Controller {
    private List<TwoDPoints<Double>> dataset = new LoadDataService().read();

    private String saveLocationPrefix = "C:/Users/j/";
    @RequestMapping("/data")
    public List<TwoDPoints<Double>> home(){
        return dataset;
    }

    @RequestMapping("/ses")
    public Tuple<List<AllInOneStuff<Double>>, MetaData> ses(){
        Tuple<List<AllInOneStuff<Double>>, MetaData> result = new SimpleES(dataset).run(12, 500);
        this.createAndSaveChart(result, "SES-");
        return result;
    }

    @RequestMapping("/des")
    public Object des(){
        double alpha = 0.659100046560163;
        double gamma = 0.0531171804609812;


        Tuple<List<AllInOneStuff<Double>>, MetaData> result = new DoubleES(dataset).run(155.88, 0.8369, 500, 500, 12);
        this.createAndSaveChart(result, "DES-");
        return result;
    }

    private XYDataset createDataset(List<AllInOneStuff<Double>> data) {
        XYSeries sword = new XYSeries("Swords",false,false);
        XYSeries smoothing = new XYSeries("smoothing",false,false);
        XYSeries forecast = new XYSeries("forecast",false,false);
        for (int i = 0; i < dataset.size(); i++) {
            sword.add(i+1,dataset.get(i).getY());

        }
        for (int i = 0; i < data.size(); i++) {
            smoothing.add(i+1,data.get(i).getPredictedY());
        }

        XYSeriesCollection xyDataset = new XYSeriesCollection();
        xyDataset.addSeries(sword);
        xyDataset.addSeries(smoothing);
        xyDataset.addSeries(forecast);

        return xyDataset;
    }
    private void createAndSaveChart(Tuple<List<AllInOneStuff<Double>>, MetaData> data, String prefix){
        XYDataset dataset = createDataset(data.get_1());
        MetaData metaData = data.get_2();
        String chartTitle = String.format("Sword Forecasting SES \n" +
                "Alpha: %s  \nGamma: %s \nSSE: %s",metaData.getOptimalAlpha(),metaData.getOptimalGamma(), metaData.getSSE());
        JFreeChart chart = ChartFactory.createXYLineChart(chartTitle,
                "Months","Demands",dataset,
                PlotOrientation.VERTICAL,true,false,false);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint( 0 , Color.BLACK );
        renderer.setSeriesPaint( 1 , Color.GREEN );
        renderer.setSeriesPaint( 2 , Color.RED );
        renderer.setSeriesStroke( 0 , new BasicStroke( 3.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 2.0f ) );
        renderer.setSeriesStroke( 2 , new BasicStroke( 2.0f ) );
        plot.setRenderer(renderer);

        try {
            ChartUtilities.saveChartAsJPEG(new File(getSaveLocation(prefix)), chart, 800, 600);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getSaveLocation(String prefix){
        return saveLocationPrefix + prefix + (new BigInteger(13, new SecureRandom()).toString(32)) + ".jpeg";
    }
}
