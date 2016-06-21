package nl.hr.Algorithms;

import nl.hr.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class DoubleES {

    private List<TwoDPoints<Double>> dataset;
    private List<AllInOneStuff<Double>> allinone;

    public DoubleES(List<TwoDPoints<Double>> dataset){
        this.dataset = dataset;
        this.emptyList();
    }
    public Tuple<List<AllInOneStuff<Double>>, MetaData> run(double firstForecast, double firstTrend, int limitAlpha, int limitGamma, int addedTime){

        boolean isGammaCalculated = false;

        double optimalGamma = 0;
        double lowestErrorGamma = Double.MAX_VALUE;

        double optimalAlpha = 0;
        double lowestErrorAlpha = Double.MAX_VALUE;

        for(int i = 0; i<limitAlpha; i++){
            double alpha = new Random().nextDouble();
            if(!isGammaCalculated){
                for(int g = 0; g<limitGamma; g++){
                    double gamma = new Random().nextDouble();
                    Tuple<List<AllInOneStuff<Double>>, Double> gammaResult = exec(firstForecast, firstTrend, alpha, gamma, addedTime);
                    double error = gammaResult.get_2();
                    if(error < lowestErrorGamma){
                        error = lowestErrorGamma;
                        optimalGamma = gamma;
                    }
                }
                isGammaCalculated = true;
            }else{

                Tuple<List<AllInOneStuff<Double>>, Double> alphaResult = exec(firstForecast, firstTrend, alpha, optimalGamma, addedTime);
                double error = alphaResult.get_2();
                if(error < lowestErrorAlpha){
                    lowestErrorAlpha = error;
                    optimalAlpha = alpha;
                }
            }
        }
        Tuple<List<AllInOneStuff<Double>>, Double> optimalResult = exec(firstForecast, firstTrend, optimalAlpha, optimalGamma, addedTime);
        MetaData meta = new MetaData(optimalAlpha, optimalGamma, new OwnMath().pow(optimalResult.get_2(), 2));

        return new Tuple<List<AllInOneStuff<Double>>, MetaData>(optimalResult.get_1(), meta);
    }
    private Tuple<List<AllInOneStuff<Double>>, Double> exec(double firstForecast, double firstTrend, double alpha, double gamma, int addedTime){
        double sumOfErrors = 0;
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
            sumOfErrors += forecastError;
            allinone.add(new AllInOneStuff<Double>(actualX, actual, predictedY, alpha, gamma, trend, forecastError, new OwnMath().pow(forecastError, 2)));
        }
        int size = allinone.size();
        AllInOneStuff<Double> lastVal = allinone.get(size-1);
        for(int i = 0; i < addedTime; i++){
            double predicted = (lastVal.getTrend() * i+1) + lastVal.getPredictedY();
            allinone.add(new AllInOneStuff<Double>(Double.valueOf(size+i), predicted, predicted, alpha, null, null, null, null));
        }
        List<AllInOneStuff<Double>> result = allinone;
        this.emptyList();
        return new Tuple<List<AllInOneStuff<Double>>, Double>(result, sumOfErrors);
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
