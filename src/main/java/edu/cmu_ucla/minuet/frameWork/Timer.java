package edu.cmu_ucla.minuet.frameWork;

import edu.cmu_ucla.minuet.model.VitalObject;
import edu.cmu_ucla.minuet.model.VitalWorld;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Set;
import java.util.concurrent.ScheduledFuture;

public class Timer extends PluginTemplate implements Plugable{
    private int curStage = 0;
    private String NAME = "timer";

    private Integer duration;
    private Set<String> command;

    private VitalObject targetObject;
    private ScheduledFuture workHandler;

    @Override
    public void kill() {
        super.kill();
        workHandler.cancel(true);
    }

    public void setTargetObject(VitalObject targetObject) {
        this.targetObject = targetObject;

        Thread t = new Thread(() -> {
            try {
                mqtt.sendMessage("connectedVoice","$please set duration");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    @Override
    public VitalObject getTargetObject() {
        return this.targetObject;
    }

    @Override
    public void getVoiceCommand(String voice) {

    }


    public Timer(MQTT mqtt, VitalWorld world) {
       super(mqtt, world);
    }


//    public void getVoiceCommand(String voice){
//        switch (curStage){
//            case 0:
//
//
//                this.duration = Integer.parseInt(voice.split("\\s+")[0]);
//                this.curStage+=1;
//                Thread t1 = new Thread(() -> {
//                    try {
//                        mqtt.sendMessage("connectedVoice","$please set command");
//                    } catch (MqttException e) {
//                        e.printStackTrace();
//                    }
//                });
//                t1.start();
//                break;
//            case 1:
//                Set<String> tmpCommand = new HashSet<>(Arrays.asList(voice.split("\\s+")));
//                if(this.targetObject.canExecuCommand(tmpCommand)){
//                    command = tmpCommand;
//                    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//                    synchronized (world) {
//                        world.removeCurPlugin();
//
//                    }
//                    final Runnable execution = new Runnable() {
//                        @Override
//                        public void run() {
//
//                            String[] retirmData = targetObject.execuate(command);
//                            try {
//                                mqtt.sendMessage(retirmData[0], retirmData[1]);
//
//                            } catch (MqttException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    };
//                    workHandler = scheduledExecutorService.schedule(execution, this.duration, TimeUnit.SECONDS);
//                    Thread t = new Thread(() -> {
//                        try {
//                            mqtt.sendMessage("connectedVoice","$timer scheduled");
//                        } catch (MqttException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                    t.start();
//                }
//
//
//        }
//    }

}
