package edu.cmu_ucla.minuet.mqtt;

import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import edu.cmu_ucla.minuet.model.LocData;
import edu.cmu_ucla.minuet.model.VitalWorld;
import edu.cmu_ucla.minuet.voice.RecordAudio;
import edu.cmu_ucla.minuet.util.Util;
import org.eclipse.paho.client.mqttv3.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class VoiceMQTTSub implements MqttCallback{
    private MqttClient client;
    private volatile VitalWorld world;
    private MqttConnectOptions options;
    private List<LocData> locDatas;
    private boolean isGettingData;
    private String TRIGGER_TOPIC = "data";
    private ExecutorService executorService;
    private Future<List<SpeechRecognitionResult>> future;
    public VoiceMQTTSub(VitalWorld world) throws MqttException {


        this.executorService = Executors.newCachedThreadPool();

        this.isGettingData = false;
        this.locDatas = new ArrayList<>();
        this.world = world;
        client=new MqttClient("tcp://192.168.1.8:1883", "client");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);

        client.subscribe(TRIGGER_TOPIC);

    }

    @Override
    public void connectionLost(Throwable cause) {
        try {
            client.connect(options);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {


        String newData = new String(message.getPayload());
        if(newData.equals("pressed")){
            System.out.println("streaming data pressed!!");
            this.isGettingData = true;
            Callable<List<SpeechRecognitionResult>> callable = new Callable<List<SpeechRecognitionResult>>(){
                @Override
                public List<SpeechRecognitionResult> call() throws Exception {
                    return new RecordAudio().getRecordResult();
                }
            };
            this.future = executorService.submit(callable);
            return;
        }
        else if(newData.equals("end")){
            System.out.println("streaming data end!!");

//                world.passTheLocs(locDatas);
//                world.setAudioResult(this.future.get());

            locDatas.clear();
            isGettingData = false;
            return;

        }
        if(isGettingData){
            locDatas.add(Util.parseLocString(newData));
            return;
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}
