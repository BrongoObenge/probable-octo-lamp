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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {
    private List<TwoDPoints<Double>> dataset = new LoadDataService().read();


    @RequestMapping("/data")
    public List<TwoDPoints<Double>> home(){
        return dataset;
    }

    @RequestMapping("/ses")
    public Tuple<List<AllInOneStuff<Double>>, MetaData> ses(){
        return new SimpleES(dataset).run(12, 500);
    }

    @RequestMapping("/des")
    public Object des(){
        double alpha = 0.659100046560163;
        double gamma = 0.0531171804609812;


        return new DoubleES(dataset).run(155.88, 0.8369, alpha, gamma);

    }
}
