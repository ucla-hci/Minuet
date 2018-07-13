package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MQTTdataLogger implements MqttCallback {

    private static Boolean isPressed = false;
    private List<Double> x = new Vector<>(50);
    private List<Double> y = new Vector<>(50);
    private List<Double> z = new Vector<>(50);
    private List<Double> magA = new Vector<>(50);
    private List<Double> magG = new Vector<>(50);

    private org.knowm.xchart.XYChart chart;
    private List<Double> index = new ArrayList<>();
    private double[] se;
    private SwingWrapper<XYChart> sw;

    public MQTTdataLogger() throws MqttException {
        for (int i = 0; i < 50; i++) {
            this.index.add((double) i);
            x.add(0.0);
            z.add(0.0);
            y.add(0.0);
            magA.add(0.0);
            magG.add(0.0);
        }

        MqttClient client = new MqttClient("tcp://192.168.1.8:1883", "logger2");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);

        se = index.stream().mapToDouble(d -> d).toArray();
        double[] initx = x.stream().mapToDouble(d -> d).toArray();
        double[] inity = y.stream().mapToDouble(d -> d).toArray();
        double[] initz = z.stream().mapToDouble(d -> d).toArray();
        double[] initma = magA.stream().mapToDouble(d -> d).toArray();
        double[] initmg = magG.stream().mapToDouble(d -> d).toArray();

        chart = QuickChart.getChart("Gyro Scatter", "sequence", "m/s^2", "a", se, initma);
//        chart.getStyler().setYAxisMax(300.0).setYAxisMin(-300.0);
        chart.getStyler().setSeriesColors(new Color[]{Color.GREEN, Color.RED, Color.BLUE});
//        chart.addSeries("g", se, initmg);
//        chart.addSeries("z",se,initz);

        Thread swingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (chart) {

                    sw = new SwingWrapper<XYChart>(chart);
                    sw.displayChart();
                }
            }
        });
        swingThread.start();

        client.subscribe("data");
    }


    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
//        System.out.println("11111");
//        synchronized (isPressed) {
//            if (isPressed) {
//                boolean curIsMoving = false;
        String newData = new String(message.getPayload());
        String[] splitedString = newData.split("\\s+");
        if (x.size() == 50) {
            x.remove(0);
        }
        x.add(Double.parseDouble(splitedString[3]));
        if (y.size() == 50) {
            y.remove(0);
        }
        y.add(Double.parseDouble(splitedString[4]));
        if (z.size() == 50) {
            z.remove(0);
        }
        z.add(Double.parseDouble(splitedString[5]));

        if (magA.size() == 50) {
            magA.remove(0);
        }

        magA.add(Math.sqrt((Double.parseDouble(splitedString[0]) * Double.parseDouble(splitedString[0]))
                        + (Double.parseDouble(splitedString[1]) * Double.parseDouble(splitedString[1]))
                        + (Double.parseDouble(splitedString[2]) * Double.parseDouble(splitedString[2]))));

        if (magG.size() == 50) {
            magG.remove(0);
        }
        magG.add(Math.sqrt((Double.parseDouble(splitedString[3]) * Double.parseDouble(splitedString[3]))
                + (Double.parseDouble(splitedString[4]) * Double.parseDouble(splitedString[4]))
                + (Double.parseDouble(splitedString[5]) * Double.parseDouble(splitedString[5]))));
        synchronized (chart) {
            chart.updateXYSeries("a", se, magA.stream().mapToDouble(d -> d).toArray(), null);
//            chart.updateXYSeries("g", se, magG.stream().mapToDouble(d -> d).toArray(), null);
//            chart.updateXYSeries("y", se, y.stream().mapToDouble(d -> d).toArray(), null);
            sw.repaintChart();
        }
        double x = Double.parseDouble(splitedString[3]);
        double y = Double.parseDouble(splitedString[4]);
        double z = Double.parseDouble(splitedString[5]);
        double result = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0));

//                if (result > 15) {
//                    curIsMoving = true;
//                } else {
//                    curIsMoving = false;
//                }
//                if (curIsMoving != isMoving) {
//                    this.isMoving = curIsMoving;
//                    System.out.println("is Moving: " + curIsMoving);
//                }
//            }
//        }

    }

    public static void main(String[] args) throws MqttException {
        MQTTdataLogger logger = new MQTTdataLogger();

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


}
