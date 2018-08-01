package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Arrays;

public class MeetingScheduler implements MqttCallback {
    private MqttClient client;
    private String subTopic;
    private String userName;

    public MeetingScheduler(String userName) throws MqttException {
        this.userName = userName;
        this.subTopic = "meetingScheduler";

        client = new MqttClient("tcp://192.168.1.8:1883", "meetingScheduler");
        MqttConnectOptions options = new MqttConnectOptions();
        client.setCallback(this);
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.connect(options);
        System.out.println("MQTT connected"+" subscribe to topic "+ subTopic);
        client.subscribe(subTopic);


    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String incommingData = new String(message.getPayload());
        System.out.println(incommingData);
        String[] splitedString = incommingData.split("\\s+");
        if (this.userName.equals(splitedString[1])) {
            String[] startTime = Arrays.copyOfRange(splitedString, 2, 8);
            String[] endTime = Arrays.copyOfRange(splitedString, 8, 14);

            String command = String.format("tell application \"Calendar\" to make new event at end of calendar 1 with properties {start date:date \"%s\", end date:date\"%s\", summary:\"Meeting with %s\"}",timeParser(startTime),timeParser(endTime),splitedString[0]);
            String notificationCommand = "display notification \"New meeting created\" with title \"Calendar\" sound name \"default\"";
            String[] commands = {"osascript","-e",command};
            String[] noticommands = {"osascript","-e",notificationCommand};
            System.out.println(command);
            System.out.println(notificationCommand);
            Runtime.getRuntime().exec(commands);
            Runtime.getRuntime().exec(noticommands);

        }

    }

    private String timeParser(String[] data) {
        return String.format("%s, %s %s, %s at %s:%s:00",data[0],data[1],data[2],data[3],data[4],data[5] );
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public static void main(String[] args) {
        try {
            MeetingScheduler meetingScheduler = new MeetingScheduler("Richard");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
