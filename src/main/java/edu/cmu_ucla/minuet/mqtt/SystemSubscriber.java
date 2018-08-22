package edu.cmu_ucla.minuet.mqtt;

import edu.cmu_ucla.minuet.model.Struct;
import edu.cmu_ucla.minuet.model.VitalWorld;
import org.eclipse.paho.client.mqttv3.*;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.RandomForest;
import weka.core.SerializationHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class SystemSubscriber implements MqttCallback {
    MqttClient client;
    private Map<String, List<Struct>> gestureDict = new HashMap<>();
    private Map<String, List<Struct>> triggerDict = new HashMap<>();
    private final VitalWorld world;
    private AbstractClassifier model;
    private AbstractClassifier triggerModel;
    private String tmpVoiceCommand = "";

    public SystemSubscriber(VitalWorld world) throws MqttException, FileNotFoundException, Exception {
        model = (IBk) SerializationHelper.read(new FileInputStream("weka/KNNgestures.model"));
        triggerModel = (RandomForest) SerializationHelper.read(new FileInputStream("weka/RTpointing.model"));
        this.world = world;
        client = new MqttClient("tcp://192.168.1.8:1883", "systemSubscriber");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        String[] strings = {"locData", "speechResult", "data", "sonoffB1", "userLoc"};
        client.subscribe(strings);
        Thread audioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process p = Runtime.getRuntime().exec("python /Users/runchangkang/Documents/Minuet/pySpeech/liveTest.py");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        audioThread.start();
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String newData = new String(message.getPayload());
        newData = newData.trim();
        String[] splitedString = newData.split("\\s+");

        if (topic.equals("locData")) {
            System.out.println("loc got: " + newData);

//            world.directReceiveData(newData);
            world.revceiveData(newData);
            if (world.getCurFrame() != null) {
                world.getCurFrame().setCurCommand(new HashSet<>(Arrays.asList(tmpVoiceCommand.split("\\s+"))));
                tmpVoiceCommand = "";
            }
            if (triggerDict.containsKey(splitedString[splitedString.length - 1]))
                triggerDict.get(splitedString[splitedString.length - 1]).clear();
            if (gestureDict.containsKey(splitedString[splitedString.length - 1]))
                gestureDict.get(splitedString[splitedString.length - 1]).clear();
        } else if (topic.equals("speechResult")) {
            System.out.println("Speech got: " + newData);
            tmpVoiceCommand = newData;
            if (world.getCurPlugin() == null && world.getCurFrame() == null) {
                world.ifMeansPlugin(new HashSet<String>(Arrays.asList(splitedString)));

            } else if (world.getCurPlugin() != null && world.getCurFrame() == null) {

                world.getCurPlugin().getVoiceCommand(newData);
                tmpVoiceCommand = "";

            } else if (world.getCurFrame() != null && world.getCurFrame().getCurCommand().isEmpty()) {
                world.getCurFrame().setCurCommand(new HashSet<>(Arrays.asList(splitedString)));
            }

        } else if (topic.equals("data")) {
            Struct curStruct = new Struct(Double.parseDouble(splitedString[0]),
                    Double.parseDouble(splitedString[1]),
                    Double.parseDouble(splitedString[2]),
                    Double.parseDouble(splitedString[3]),
                    Double.parseDouble(splitedString[4]),
                    Double.parseDouble(splitedString[5]));
            String curUserName = splitedString[6];

            if (world.getCurFrame() != null && world.getCurFrame().getCurGesture().equals("") && world.getCurFrame().getUserName().equals(curUserName)) {
                if (!gestureDict.containsKey(curUserName)) {
                    gestureDict.put(curUserName, new ArrayList<Struct>(15));
                }
                if (gestureDict.get(curUserName).size() == 15) gestureDict.get(curUserName).remove(0);
                gestureDict.get(curUserName).add(curStruct);
                if (gestureDict.get(curUserName).size() == 15) checkGesture(gestureDict.get(curUserName), curUserName);


            }
            if (world.getCurFrame() == null) {


                if (!triggerDict.containsKey(curUserName)) {
                    triggerDict.put(curUserName, new ArrayList<Struct>(15));
                }
                if (triggerDict.get(curUserName).size() == 15) triggerDict.get(curUserName).remove(0);

                triggerDict.get(curUserName).add(curStruct);
                if (triggerDict.get(curUserName).size() == 15)
                    checkTriggerGesture(triggerDict.get(curUserName), curUserName);

            }
        } else if (topic.equals("sonoffB1")) {
            System.out.println(newData);
            world.lightData(newData);

        } else if (topic.equals("userLoc")) {

            world.updateUserLoc(splitedString);


        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private void checkGesture(List<Struct> curIMUDatas, String userName) {
        String gesture = ClassifierUtil.Classify(model, curIMUDatas, 0);

        if (!gesture.equals("noInteraction") && !gesture.equals("")) {
            System.out.println("Gesture get: " + gesture);
            world.getCurFrame().setCurGesture(gesture);
            curIMUDatas.clear();
            gestureDict.get(userName).clear();
        }
    }

    private void checkTriggerGesture(List<Struct> curTriggerIMUDatas, String userName) {

        String gesture = ClassifierUtil.Classify(triggerModel, curTriggerIMUDatas, 1);

        if (gesture.equals("pointing")) {
            System.out.println("Gesture get: " + gesture);
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload("1".getBytes());

            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        client.publish("point/"+userName, mqttMessage);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start();


            triggerDict.get(userName).clear();
        }
    }
}
