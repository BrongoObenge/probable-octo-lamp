package nl.hr.services;

import nl.hr.config.Config;
import nl.hr.domain.TwoDPoints;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoadDataService {
    public List<TwoDPoints<Double>> read() {
        List<TwoDPoints<Double>> dataset = new ArrayList<TwoDPoints<Double>>();
        try{
            Scanner data = new Scanner(new FileReader(Config.swordForecastingDataFileLocation));
            while (data.hasNext()) {
                String[] temp = data.next().split(",");
                dataset.add(new TwoDPoints<Double>(Double.valueOf(temp[0]), Double.valueOf(temp[1])));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return dataset;
    }
}