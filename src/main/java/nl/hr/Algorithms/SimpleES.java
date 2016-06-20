package nl.hr.Algorithms;

import lombok.AllArgsConstructor;
import nl.hr.domain.AllInOneStuff;
import nl.hr.domain.Tuple;
import nl.hr.domain.TwoDPoints;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j on 6/20/16.
 */

public class SimpleES {

    private List<TwoDPoints<Double>> dataset;
    private List<AllInOneStuff<Double>> allinone = new ArrayList<AllInOneStuff<Double>>();


    public SimpleES(List<TwoDPoints<Double>> _dataset){
        dataset = _dataset;
    }

    public List<AllInOneStuff<Double>> run(double smoothingFactor, int addedTime){
        for(int i = 0; i < dataset.size(); i++){
            Tuple<Double, Double> predictedY = calcPredictedY(smoothingFactor, i);
            double error = Math.abs(dataset.get(i).getY() - predictedY.get_1());
            allinone.add(new AllInOneStuff<Double>(dataset.get(i).getX(),
                                               dataset.get(i).getY(),
                                               predictedY.get_1(),
                                               smoothingFactor, null, predictedY.get_2(), Math.pow(error, 2)));
        }

        for(int i = 0; i < addedTime; i++){
           allinone.add(new AllInOneStuff<Double>(
                   Double.valueOf(allinone.size()+i),
                   allinone.get(allinone.size()-1).getPredictedY(),
                   allinone.get(allinone.size()-1).getPredictedY(),
                   smoothingFactor,
                   null, null, null));
        }

        return allinone;
    }

    public Tuple<Double, Double> calcPredictedY(double smoothingFactor, int time){
        if(time == 0) {
            return firstForecast(smoothingFactor);
        }


        double prevForecast = allinone.get(time-1).getPredictedY();
        double curr = dataset.get(time).getY();
        double forecastError = curr-prevForecast;
        return new Tuple<Double, Double>(prevForecast+(smoothingFactor*forecastError), forecastError);
    }

    private Tuple<Double, Double> firstForecast(double smoothingFactor){
        double avg = calcAvg();
        double forecastError = (dataset.get(0).getY() - avg);
        return new Tuple<Double, Double>(avg + (smoothingFactor * forecastError), forecastError);
    }

    private double calcAvg(){
        double sum = 0;
        int cardinality = 0;
        for(TwoDPoints<Double> element : dataset){
            sum += element.getY();
            cardinality++;
            if(cardinality == 12){ break; }
        }
        return sum/cardinality;
    }
}
