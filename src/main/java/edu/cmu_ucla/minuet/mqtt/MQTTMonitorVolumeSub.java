package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class MQTTMonitorVolumeSub implements MqttCallback {
    private MqttClient client;
    private String curUserName = "Richard";
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
    public MQTTMonitorVolumeSub()throws MqttException {

        client = new MqttClient("tcp://192.168.1.8:1883", "monitor");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
//        client.subscribe("cmnd/Monitor/#");
        client.subscribe("monitorDemo");
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

        if(topic.equals("monitorDemo")){
            String newData = new String(message.getPayload());
            String[] split = newData.split("\\s+");
            if(!curUserName.equals(split[0])){
                if(curUserName.equals("Richard") && split[0].equals("Tom")){
                    curUserName = "Tom";
                    Runtime.getRuntime().exec(next);
                    Runtime.getRuntime().exec(next);
                }
                else if(curUserName.equals("Tom")&&split[0].equals("Richard")){
                    curUserName = "Richard";
                    Runtime.getRuntime().exec(previous);
                    Runtime.getRuntime().exec(previous);
                }
                else if(curUserName.equals("Tom")&&split[0].equals("nothing")){
                    curUserName = "nothing";
                    Runtime.getRuntime().exec(previous);

                }
                else if(curUserName.equals("Richard")&&split[0].equals("nothing")){
                    curUserName = "nothing";
                    Runtime.getRuntime().exec(next);

                }
                else if(curUserName.equals("nothing")&&split[0].equals("Richard")){
                    curUserName = "Richard";
                    Runtime.getRuntime().exec(previous);

                }
                else if(curUserName.equals("nothing")&&split[0].equals("Tom")){
                    curUserName = "Tom";
                    Runtime.getRuntime().exec(next);

                }
            }

        }


    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        try {
            MQTTMonitorVolumeSub sub = new MQTTMonitorVolumeSub();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
