package nl.hr.Algorithms;

import nl.hr.domain.*;

import java.util.*;


public class SimpleES {

    private List<TwoDPoints<Double>> dataset;
    private List<AllInOneStuff<Double>> allinone = new ArrayList<AllInOneStuff<Double>>();

    public SimpleES(List<TwoDPoints<Double>> _dataset){
        dataset = _dataset;
    }

    public Tuple<List<AllInOneStuff<Double>>, MetaData> run(int addedTime, int limit){
        double smoothingFactor = 0;
        double optimalSmoothing = 0;
        double lowestError = Double.MAX_VALUE;

        for(int i = 0; i < limit; i++) {
            smoothingFactor = new Random().nextDouble();

            Tuple<List<AllInOneStuff<Double>>, Double> result = run(smoothingFactor, addedTime);
            double error = result.get_2();

            if(error < lowestError){
                optimalSmoothing = smoothingFactor;
                lowestError = error;
            }
        }
        Tuple<List<AllInOneStuff<Double>>, Double> result = run(optimalSmoothing, addedTime);
        MetaData meta = new MetaData(optimalSmoothing, 0, new OwnMath().pow(result.get_2(), 2));
        return new Tuple<List<AllInOneStuff<Double>>, MetaData>(result.get_1(), meta);
    }


    public Tuple<List<AllInOneStuff<Double>>, Double> run(double smoothingFactor, int addedTime){
        double sumOfErrors = 0;
        for(int i = 0; i < dataset.size(); i++){
            Tuple<Double, Double> predictedY = calcPredictedY(smoothingFactor, i);
            sumOfErrors += predictedY.get_2();
            allinone.add(new AllInOneStuff<Double>(dataset.get(i).getX(),
                                               dataset.get(i).getY(),
                                               predictedY.get_1(),
                                               smoothingFactor, null, predictedY.get_2(), new OwnMath().pow(predictedY.get_2(), 2)));
        }

        for(int i = 0; i < addedTime; i++){
           allinone.add(new AllInOneStuff<Double>(
                   Double.valueOf(allinone.size()+1),
                   allinone.get(allinone.size()-1).getPredictedY(),
                   allinone.get(allinone.size()-1).getPredictedY(),
                   smoothingFactor,
                   null, null, null));
        }
        List<AllInOneStuff<Double>> mrResult = allinone;
        this.emptyList();
        return new Tuple<List<AllInOneStuff<Double>>, Double>(mrResult, sumOfErrors);
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
    private void emptyList(){
        allinone = new ArrayList<AllInOneStuff<Double>>();
    }
}
