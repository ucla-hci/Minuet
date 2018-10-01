package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class MqttNightStand implements MqttCallback {
    private MqttClient client;
    String[] topics = {"cmnd/sonoffB1/POWER","cmnd/sonoffB2/POWER","cmnd/sonoff1/POWER"};
    String[] runCmnd = {"ON","ON","ON"};
    String[] stopCmnd = {"OFF","OFF","OFF"};

    private MqttConnectOptions options;
    public MqttNightStand() throws MqttException{
        client = new MqttClient("tcp://192.168.1.8:1883", "nightStand");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        System.out.println("nightStand connected");
        client.subscribe("nightStand");

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String input = new String(message.getPayload());
        System.out.println(input);
        switch (input){
            case "run":
                for(int i=0;i<topics.length;i++){
                    System.out.println("!!!");
                    MqttMessage mqttMessage = new MqttMessage();
                    mqttMessage.setPayload(runCmnd[i].getBytes());

                    String tmpTopic = topics[i];

                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                client.publish(tmpTopic,mqttMessage);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    sendThread.start();

                }
                break;
            case "stop":
                for(int i=0;i<topics.length;i++){
                    MqttMessage mqttMessage = new MqttMessage();
                    mqttMessage.setPayload(stopCmnd[i].getBytes());
                    String tmpTopic = topics[i];

                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                client.publish(tmpTopic,mqttMessage);
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    sendThread.start();

                }
                break;

        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        try {
            MqttNightStand nightStand = new MqttNightStand();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
