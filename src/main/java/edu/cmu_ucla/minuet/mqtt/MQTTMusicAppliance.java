package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;

public class MQTTMusicAppliance implements MqttCallback {

    private MqttClient client;

    private MqttConnectOptions options;
    public MQTTMusicAppliance() throws MqttException{

        client = new MqttClient("tcp://192.168.1.8:1883", "MusicPlayer");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        System.out.println("MusicPlayer connected");
        client.subscribe("cmnd/MusicPlayer");

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String input = new String(message.getPayload());
        switch (input){
            case "0":
                try {
                    Runtime.getRuntime().exec("spotify start");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "1":
                try {
                    Runtime.getRuntime().exec("spotify pause");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "2":
                try {
                    Runtime.getRuntime().exec("spotify next");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case "3":
                try {
                    Runtime.getRuntime().exec("spotify volume up");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "4":
                try {
                    Runtime.getRuntime().exec("spotify volume down");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;


        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        try {
            MQTTMusicAppliance m = new MQTTMusicAppliance();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
