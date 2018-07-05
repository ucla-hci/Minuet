package edu.cmu_ucla.minuet.mqtt;

import edu.cmu_ucla.minuet.$1.Point;
import edu.cmu_ucla.minuet.$1.Recognizer;
import edu.cmu_ucla.minuet.$1.Result;
import org.eclipse.paho.client.mqttv3.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class MqttNGUI extends JFrame {
    private static Boolean isPressed = false;
    private static Boolean isRecognizing = false;
    private static Boolean isTraining = false;
    private static boolean isDead = false;


    private class MQTTdataLogger extends SwingWorker<Void, Boolean> implements MqttCallback {
        private Boolean isMoving = false;
        private Vector<Point> dataQueue;
        private Recognizer recognizer;
        private List<Double> trainList;
        private MqttClient client;

        public MQTTdataLogger() throws MqttException {
            System.out.println("Start connecting MQTT");
            this.client = new MqttClient("tcp://192.168.1.8:1883", "logger");
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("admin");
            options.setPassword("19930903".toCharArray());
            client.setCallback(this);
            client.connect(options);
            client.subscribe("data");
            this.dataQueue = new Vector<>(15);
            this.trainList = new ArrayList<>();
            this.recognizer = new Recognizer(1);
            System.out.println("Mqtt connection finished");
        }

        @Override
        protected Void doInBackground() throws Exception {
            while (!isDead) {
            }
            return null;
        }

        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            synchronized (isPressed) {
                if (isPressed) {
                    String newData = new String(message.getPayload());
                    String[] splitedString = newData.split("\\s+");
                    double ax = Double.parseDouble(splitedString[0]);
                    double ay = Double.parseDouble(splitedString[1]);
                    double az = Double.parseDouble(splitedString[2]);
                    double x = Double.parseDouble(splitedString[3]);
                    double y = Double.parseDouble(splitedString[4]);
                    double z = Double.parseDouble(splitedString[5]);
                    double movingResult = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0) + Math.pow(z, 2.0));
                    double result = y;
                    double result0 = z;

                    checkIsMoving(movingResult);
                    if (dataQueue.size() == 15) {
                        this.dataQueue.remove(0);

                    }
                    dataQueue.addElement(new Point(result0, result));

                    synchronized (isTraining) {
                        if (isTraining) {
                            trainList.add(result0);
                            trainList.add(result);
                        } else if (!trainList.isEmpty()) {
                            System.out.println("training end");
                            double[] arr = trainList.stream().mapToDouble(d -> d).toArray();
                            this.recognizer.addTemplate(this.recognizer.loadTemplate("circle", arr));
                            System.out.println(Arrays.toString(arr));
                            trainList.clear();
                            System.out.println("system has :" + this.recognizer.Templates.size());
                        }
                    }
                    synchronized (isRecognizing) {
                        if (isRecognizing) {
                            if (dataQueue.size() == 15) {
                                Result newResult = this.recognizer.Recognize(dataQueue);
                                if (newResult.Score >= 0.71) {
                                    this.client.publish("trigger", new MqttMessage("send".getBytes()));
                                    System.out.println(newResult.Name);
                                    System.out.println(newResult.Score);
                                    dataQueue.clear();
                                }
                            }
                        }
                    }

                }
            }

        }

        private void checkIsMoving(double result) {
            boolean curIsMoving = false;
            if (result > 15) {
                curIsMoving = true;
            }
            if (curIsMoving != isMoving) {
                isMoving = curIsMoving;
                publish(curIsMoving);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }


    private JLabel pressedLabel = new JLabel("Is Pressed: ");
    private JLabel movingLabel = new JLabel("Is Moving: ");
    private JButton startButton = new JButton("Start");
    private JButton trainButton = new JButton("Train");
    private JButton RecogButton = new JButton("Recog");

    public MqttNGUI(String title) {
        super(title);

        setLayout(new GridBagLayout());

        pressedLabel.setFont(new Font("serif", Font.BOLD, 28));
        movingLabel.setFont(new Font("serif", Font.BOLD, 28));

        GridBagConstraints gc = new GridBagConstraints();

        gc.fill = GridBagConstraints.NONE;

        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        add(pressedLabel, gc);

        gc.gridx = 0;
        gc.gridy = 1;
        gc.weightx = 1;
        gc.weighty = 1;
        add(movingLabel, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.weightx = 1;
        gc.weighty = 1;
        add(startButton, gc);
        gc.gridx = 0;
        gc.gridy = 3;
        gc.weightx = 1;
        gc.weighty = 1;
        add(trainButton, gc);
        gc.gridx = 0;
        gc.gridy = 4;
        gc.weightx = 1;
        gc.weighty = 1;
        add(RecogButton, gc);


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                synchronized (isPressed) {

                    isPressed = !isPressed;

                    pressedLabel.setText("Program Start: " + isPressed);
                }
            }

        });
        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                synchronized (isTraining) {

                    isTraining = !isTraining;


                }
            }

        });
        RecogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                synchronized (isRecognizing) {

                    isRecognizing = !isRecognizing;


                }
            }

        });


        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        SwingWorker<Void, Boolean> swingWorker;
        try {
            swingWorker = new MQTTdataLogger() {
                @Override
                protected void process(List<Boolean> chunks) {
                    movingLabel.setText("Is Moving: " + chunks.get(chunks.size() - 1));
                }
            };
            swingWorker.execute();

        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MqttNGUI("$1 Demo");
            }
        });

    }

}




