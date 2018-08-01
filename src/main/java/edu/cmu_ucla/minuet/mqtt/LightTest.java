package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class LightTest implements MqttCallback {
    MqttClient client;
    public LightTest() throws MqttException{

        client = new MqttClient("tcp://192.168.1.8:1883", "LIGHTTEST");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        System.out.println("LIGHTTEST connected");

    }
    public void publishMessage(String topic,String message){
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        try {
            client.publish(topic, mqttMessage);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        try {
            LightTest mqttClient = new LightTest();

            Thread loopThread = new Thread(()->{

                for (int tmp = 153;tmp<=500;tmp++) {
                    mqttClient.publishMessage("cmnd/sonoffB1/ct",Integer.toString(tmp));
                    try
                    {
                        Thread.sleep(100);
                    }
                    catch(InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
            });
            loopThread.start();
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }
}
