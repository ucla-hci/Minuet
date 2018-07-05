package edu.cmu_ucla.minuet.mqtt;

import org.knowm.xchart.QuickChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MQTTscatter {

    private List<Double> x = new Vector<>(50);
    private List<Double> y= new Vector<>(50);
    private List<Double> z= new Vector<>(50);
     org.knowm.xchart.XYChart chart;
    private List<Double> index = new ArrayList<>();
    private double[] se;
    public MQTTscatter() {
        for(int i = 0; i<50;i++){
            this.index.add((double)i);
            x.add(0.0);
        }
        se = index.stream().mapToDouble(d -> d).toArray();
        double[] initx = x.stream().mapToDouble(d -> d).toArray();
        chart = QuickChart.getChart("Gyro Scatter", "sequence", "m/s^2", "x",se ,initx);


    }

    public static void main(String[] args) {

//        SwingWrapper<XYChart> sw = new SwingWrapper<XYChart>(chart);
//        sw.displayChart();

    }
}
