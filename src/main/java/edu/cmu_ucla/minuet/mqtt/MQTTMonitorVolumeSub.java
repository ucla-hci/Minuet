package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.io.File;

public class MQTTMonitorVolumeSub implements MqttCallback {
    private MqttClient client;

    private MqttConnectOptions options;
    public MQTTMonitorVolumeSub()throws MqttException {

        client = new MqttClient("tcp://192.168.1.8:1883", "monitor");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        client.subscribe("cmnd/Monitor/#");
    }

    @Override
    public void connectionLost(Throwable cause) {
        try {
            client.connect(options);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        if(topic.equals("cmnd/Monitor/VOLUME")){
            String newData = new String(message.getPayload());
            String command = "set volume " + newData;
            try {
                ProcessBuilder pb = new ProcessBuilder("osascript", "-e", command);
                pb.directory(new File("/usr/bin"));
                System.out.println(command);
                Process p = pb.start();
                p.waitFor();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
}
