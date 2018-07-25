package edu.cmu_ucla.minuet.mqtt;

import edu.cmu_ucla.minuet.model.Struct;
import org.eclipse.paho.client.mqttv3.*;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.lazy.IBk;
import weka.core.SerializationHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class MqttNGUI extends JFrame {
    private static Boolean isPressed = false;
    private static boolean isDead = false;


    private class MQTTdataLogger extends SwingWorker<Void, String> implements MqttCallback {
        private Boolean isMoving = false;
        private List<Struct> dataQueue;
        private MqttClient client;
        private AbstractClassifier model;

        public MQTTdataLogger() throws MqttException {
            try {
                model = (IBk) SerializationHelper.read(new FileInputStream("weka/KNNgestures.model"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Start connecting MQTT");
            this.client = new MqttClient("tcp://192.168.1.8:1883", "logger");
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("admin");
            options.setPassword("19930903".toCharArray());
            client.setCallback(this);
            client.connect(options);
            client.subscribe("data");
            this.dataQueue = new ArrayList<>(15);
            System.out.println("Mqtt connection finished");

        }

        @Override
        protected Void doInBackground() throws Exception {
            while (!isDead) {
            }
            return null;
        }

        @Override
        protected void process(List<String> chunks) {
            super.process(chunks);
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
                    Struct tmpStruct = new Struct(ax, ay, az, x, y, z);
                    if (dataQueue.size() == 15) {
                        this.dataQueue.remove(0);

                    }
                    dataQueue.add(tmpStruct);


                    if (dataQueue.size() == 15) {
                        String result = ClassifierUtil.Classify(model, dataQueue,0);
                        if (!result.equals("noInteraction")) {

                            dataQueue.clear();

                        }
                        publish(result);

                    }
                }

            }

        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }




    private JLabel pressedLabel = new JLabel("Start the Program: ");
    private JLabel movingLabel = new JLabel("result: ");
    private JButton startButton = new JButton("Start");


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




        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                synchronized (isPressed) {

                    isPressed = !isPressed;

                    pressedLabel.setText("Program Start: " + isPressed);
                }
            }

        });




        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        SwingWorker<Void, String> swingWorker;
        try {
            swingWorker = new MQTTdataLogger() {
                @Override
                protected void process(List<String> chunks) {
                    movingLabel.setText("Cur Gesture: " + chunks.get(chunks.size() - 1));
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
                new MqttNGUI("recognizer demo");
            }
        });

    }

}




