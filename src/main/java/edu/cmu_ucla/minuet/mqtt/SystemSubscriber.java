package edu.cmu_ucla.minuet.mqtt;

import edu.cmu_ucla.minuet.model.Struct;
import edu.cmu_ucla.minuet.model.VitalWorld;
import org.eclipse.paho.client.mqttv3.*;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.SerializationHelper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class SystemSubscriber implements MqttCallback {
    private Queue<Struct> curIMUDatas = new ArrayBlockingQueue<>(15);
    private final VitalWorld world;
    private AbstractClassifier model;

    public SystemSubscriber(VitalWorld world) throws MqttException, FileNotFoundException, Exception {
        model = (MultilayerPerceptron) SerializationHelper.read(new FileInputStream("weka/NNwithNoInter.model"));
        this.world = world;
        MqttClient client = new MqttClient("tcp://192.168.1.8:1883", "systemSubscriber");
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        String[] strings = {"locData", "speechResult", "data"};
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
        String[] splitedString = newData.split("\\s+");

        if (topic.equals("locData")) {
            newData += " testUser";
            world.revceiveData(newData);
        } else if (topic.equals("speechResult")) {
            System.out.println("Speech got: "+newData);
            synchronized (world.getCurFrame()) {
                if (world.getCurFrame() != null && world.getCurFrame().getCurCommand().isEmpty()) {
                    world.getCurFrame().setCurCommand(new HashSet<>(Arrays.asList(splitedString)));
                }
            }

        } else if (topic.equals("data")) {
            synchronized (world.getCurFrame()) {
                if (world.getCurFrame() != null && world.getCurFrame().getCurGesture().equals("")) {
                    Struct curStruct = new Struct(Double.parseDouble(splitedString[0]),
                            Double.parseDouble(splitedString[1]),
                            Double.parseDouble(splitedString[2]),
                            Double.parseDouble(splitedString[3]),
                            Double.parseDouble(splitedString[4]),
                            Double.parseDouble(splitedString[5]));
                    if (curIMUDatas.size() == 15) {
                        curIMUDatas.poll();
                    }
                    curIMUDatas.add(curStruct);
                    if (curIMUDatas.size() == 15) {
                        synchronized (this) {
                            checkGesture();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    private void checkGesture() {
        String gesture = ClassifierUtil.Classify(model, curIMUDatas);
            world.getCurFrame().setCurGesture(gesture);
    }
}
