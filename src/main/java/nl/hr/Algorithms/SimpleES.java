package nl.hr.Algorithms;

import lombok.AllArgsConstructor;
import nl.hr.domain.AllInOneStuff;
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
            double predictedY = calcPredictedY(smoothingFactor, i);
            double error = Math.abs(dataset.get(i).getY() - predictedY);
            allinone.add(new AllInOneStuff<Double>(dataset.get(i).getX(),
                                               dataset.get(i).getY(),
                                               predictedY,
                                               smoothingFactor, null, error, Math.pow(error, 2)));
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

    public double calcPredictedY(double smoothingFactor, int time){
        if(time == 0) {
            return smoothingFactor*(dataset.get(time).getY())+((1-smoothingFactor)*c(smoothingFactor));
        }
        double prev = allinone.get(time-1).getPredictedY();
        double curr = dataset.get(time).getY();
        return prev+((curr-prev)*smoothingFactor);
    }

    private double c(double smoothingFactor){
        double avg = calcAvg();
        double forecastError = (dataset.get(0).getY() - avg);
        return avg + (smoothingFactor * forecastError);
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
