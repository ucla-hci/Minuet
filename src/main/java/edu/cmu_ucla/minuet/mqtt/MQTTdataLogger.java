package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MQTTdataLogger implements MqttCallback{

    private static Boolean isPressed = false;
    private List<Double> x = new Vector<>(50);
    private List<Double> y= new Vector<>(50);
    private List<Double> z= new Vector<>(50);
    private org.knowm.xchart.XYChart chart;
    private List<Double> index = new ArrayList<>();
    private double[] se;
    private SwingWrapper<XYChart>sw;
    public MQTTdataLogger() throws MqttException {
        for(int i = 0; i<50;i++){
            this.index.add((double)i);
            y.add(0.0);
        }

        MqttClient client = new MqttClient("tcp://192.168.1.8:1883", "logger");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);

        se = index.stream().mapToDouble(d -> d).toArray();
        double[] initx = y.stream().mapToDouble(d -> d).toArray();
        chart = QuickChart.getChart("Gyro Scatter", "sequence", "m/s^2", "z",se ,initx);
        chart.getStyler().setYAxisMax(200.0).setYAxisMin(-200.0);
        Thread swingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (chart){

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
//        synchronized (isPressed) {
//            if (isPressed) {
//                boolean curIsMoving = false;
                String newData = new String(message.getPayload());
                String[] splitedString = newData.split("\\s+");
                if(y.size()==50){
                    y.remove(0);
                }
                y.add( Double.parseDouble(splitedString[5]));
                synchronized (chart){
                chart.updateXYSeries("z",se,y.stream().mapToDouble(d -> d).toArray(),null);
                sw.repaintChart();}
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

//    public static void main(String[] args) throws MqttException{
//        MQTTdataLogger logger = new MQTTdataLogger();
//
////            MQTTdataLogger logger = new MQTTdataLogger();
//            JFrame frame = new JFrame();
//
//            JPanel panel = new JPanel(new BorderLayout());
//            panel.setPreferredSize(new Dimension(500,500));
//            JLabel label = new JLabel("No tracking: ");
//            label.addKeyListener(new KeyListener() {
//                @Override
//                public void keyTyped(KeyEvent e) {
//                    System.out.println("！！！！");
//                }
//
//                @Override
//                public void keyPressed(KeyEvent e) {
//                    System.out.println("！！！！");
//                    if(e.getKeyCode() == KeyEvent.VK_SPACE){
//                        logger.isPressed = true;
//                        label.setText("Is Moving: "+logger.isMoving);
//
//                    }
//                    panel.validate();
//                    panel.repaint();
//
//                }
//
//                @Override
//                public void keyReleased(KeyEvent e) {
//                    if(e.getKeyCode() == KeyEvent.VK_SPACE){
//                        logger.isPressed = false;
//                        label.setText("No tracking: ");
//                    }
//                    panel.validate();
//                    panel.repaint();
//                }
//            });
//            panel.add(label,BorderLayout.CENTER);
//            frame.add(panel);
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            frame.pack();
//
//            frame.setResizable(true);
//            frame.setVisible(true);
//        }
//
//    }
}
