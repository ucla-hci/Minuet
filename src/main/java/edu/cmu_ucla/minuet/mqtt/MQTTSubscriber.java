package edu.cmu_ucla.minuet.mqtt;

import edu.cmu_ucla.minuet.model.VitalWorld;
import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;

public class MQTTSubscriber implements MqttCallback {
    private MqttClient client;
    private VitalWorld world;
    private MqttConnectOptions options;
    private Thread audioThread;
    public MQTTSubscriber(VitalWorld world) throws MqttException {


        this.world = world;
        client = new MqttClient("tcp://192.168.1.8:1883", "system");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        String[] strings = {"locData","speechResult"};
        client.subscribe(strings);
//        client.subscribe("speechResult");
         audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process p = Runtime.getRuntime().exec("python /Users/runchangkang/Documents/Minuet/pySpeech/liveTest.py");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
//        System.out.println(newData);
        if (topic.equals("locData")){
        newData += " testUser";
        System.out.println("LOCDATA received:" + newData);
        synchronized (world){
        world.revceiveData(newData);}
//        audioThread.start();
            Process p = Runtime.getRuntime().exec("python /Users/runchangkang/Documents/Minuet/pySpeech/liveTest.py");

        }
         if (topic.equals("speechResult")){
            System.out.println("SpeechDataReceived: "+newData);
            synchronized (world){
                world.revceiveSpeechData(newData);}

        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}
