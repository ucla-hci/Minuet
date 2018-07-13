package edu.cmu_ucla.minuet.mqtt;

import edu.cmu_ucla.minuet.model.VitalObject;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MQTT {
    private MqttClient client;

    public MQTT() throws MqttException{
        System.out.println("connecting mqtt");
        client = new MqttClient("tcp://192.168.1.8:1883", MqttClient.generateClientId());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.connect(options);
        System.out.println("edu.cmu_ucla.minuet.mqtt.MQTT connected");
    }
    public void toggleSonoff(VitalObject object) throws MqttException{
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload("toggle".getBytes());
        client.publish(object.getTopic(),mqttMessage);
    }

    public void sendMessage(String topic,String message) throws MqttException{
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        client.publish(topic, mqttMessage);
    }

    public MqttClient getClient() {
        return client;
    }


}
