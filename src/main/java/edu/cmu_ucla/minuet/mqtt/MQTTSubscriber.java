package edu.cmu_ucla.minuet.mqtt;

import edu.cmu_ucla.minuet.model.VitalWorld;
import org.eclipse.paho.client.mqttv3.*;

public class MQTTSubscriber implements MqttCallback {
    MqttClient client;
    VitalWorld world;
    MqttConnectOptions options;

    public MQTTSubscriber(VitalWorld world) throws MqttException {


        this.world = world;
        client = new MqttClient("tcp://192.168.1.8:1883", "client");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);

        client.subscribe("data");

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
        String newData = new String(message.getPayload());
        newData += " testUser";
        System.out.println("DATA received:" + newData);
        world.revceiveData(newData);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}
