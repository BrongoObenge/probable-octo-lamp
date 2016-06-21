package nl.hr.Algorithms;

import lombok.AllArgsConstructor;
import nl.hr.domain.AllInOneStuff;
import nl.hr.domain.OwnMath;
import nl.hr.domain.TwoDPoints;

import java.util.ArrayList;
import java.util.List;



public class DoubleES {

    private List<TwoDPoints<Double>> dataset;
    private List<AllInOneStuff<Double>> allinone;

    public DoubleES(List<TwoDPoints<Double>> dataset){
        this.dataset = dataset;
        this.emptyList();
    }

    public List<AllInOneStuff<Double>> run(double firstForecast, double firstTrend, double alpha, double gamma){

        for(int i = 0; i<dataset.size();i++){
            double prevForecast = 0;
            double prevTrend = 0;
            if(i==0){
                prevForecast = firstForecast;
                prevTrend = firstTrend;
            }else{
                prevForecast = allinone.get(i-1).getPredictedY();
                prevTrend = allinone.get(i-1).getTrend();
            }
            double actual = dataset.get(i).getY();
            double actualX = dataset.get(i).getX();
            double oneStepForecast = oneStepForecast(prevTrend, prevForecast);
            double forecastError = forecastError(actual, oneStepForecast);
            double predictedY = predictedY(oneStepForecast, alpha, forecastError);
            double trend = newTrend(prevTrend, alpha, gamma, forecastError);

            allinone.add(new AllInOneStuff<Double>(actualX, actual, predictedY, alpha, gamma, trend, forecastError, new OwnMath().pow(forecastError, 2)));
        }
        return allinone;
    }

    private double oneStepForecast(double prevTrend, double prevForecast){
        return prevForecast+ prevTrend;
    }
    private double forecastError(double actual, double oneStepForecast){
        return actual - oneStepForecast;
    }

    private double predictedY(double oneStepForecast, double alpha, double forecastError){
        return oneStepForecast+ (alpha * forecastError);
    }

    private double newTrend(double prevTrend, double alpha, double gamma, double forecastError){
        return prevTrend + (alpha*gamma*forecastError);
    }
    private void emptyList(){
        allinone = new ArrayList<AllInOneStuff<Double>>();
    }

}

