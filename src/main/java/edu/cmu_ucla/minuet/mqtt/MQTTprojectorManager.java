package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Arrays;

public class MQTTprojectorManager implements MqttCallback {
    private MqttClient client;
    private String[] show1 = {"open","-a","Preview","/Users/runchangkang/Desktop/"};
    private String[] show2 ={"osascript","-e","tell application \"Preview\"",
            "-e","activate",
            "-e","tell application \"System Events\"",
            "-e","keystroke \"f\" using {control down, command down}",
            "-e","end tell",
            "-e","end tell"};

    private String[] quit = {"osascript","-e","tell application \"Preview\" to quit"};
    private String[] next = {"osascript","-e","tell application \"Preview\"",
            "-e", "activate",
            "-e","tell application \"System Events\"",
            "-e","key code 124",
            "-e","end tell",
            "-e","end tell"};
    private String[] previous = {"osascript","-e","tell application \"Preview\"",
            "-e", "activate",
            "-e","tell application \"System Events\"",
            "-e","key code 123",
            "-e","end tell",
            "-e","end tell"};
    private MqttConnectOptions options;
    public MQTTprojectorManager() {
        try {
            client = new MqttClient("tcp://192.168.1.8:1883", "Projector");
            options = new MqttConnectOptions();
            options.setUserName("admin");
            options.setPassword("19930903".toCharArray());
            client.setCallback(this);
            client.connect(options);
            System.out.println("Projector connected");
            client.subscribe("cmnd/Projector");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        String incommingData = new String(message.getPayload());
        String[] splitedString = incommingData.split("\\s+");
        if(splitedString[0].equals("show")){
            String[] tmp = show1.clone();
            tmp[tmp.length-1] += (splitedString[1]+".pdf");
            System.out.println(Arrays.asList(tmp));
            Runtime.getRuntime().exec(tmp);
            Runtime.getRuntime().exec(show2);
        }
        else if(splitedString[0].equals("quit")){
            Runtime.getRuntime().exec(quit);
        }
        else if(splitedString[0].equals("next")){
            Runtime.getRuntime().exec(next);
        }
        else if(splitedString[0].equals("previous")){
            Runtime.getRuntime().exec(previous);
        }





    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        MQTTprojectorManager mqtTprojectorManager= new MQTTprojectorManager();

    }
}
