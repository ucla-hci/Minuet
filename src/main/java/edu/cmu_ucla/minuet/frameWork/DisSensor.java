package edu.cmu_ucla.minuet.frameWork;

import edu.cmu_ucla.minuet.model.VitalObject;
import edu.cmu_ucla.minuet.model.VitalWorld;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

public class DisSensor extends PluginTemplate implements Plugable {
    private int distant = 0;
    private Set<String> inRangeCommand = new HashSet<>();
    private Set<String> outRangeCommand = new HashSet<>();
    public DisSensor(MQTT mqtt, VitalWorld world) {
        super(mqtt, world);
    }
    private ScheduledFuture workHandler;
    private int status = 0;

    @Override
    public void kill() {
        super.kill();
        workHandler.cancel(true);
    }

    @Override
    public void setTargetObject(VitalObject targetObject) {
        this.targetObject = targetObject;

        Thread t = new Thread(() -> {
            try {
                mqtt.sendMessage("connectedVoice", "$please set distant");
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


//    @Override
//    public void getVoiceCommand(String voice) {
//
//        switch (this.curStage) {
//            case 0:
//                this.distant =  Integer.parseInt(voice.split("\\s+")[0]);
//                curStage+=1;
//                Thread t1 = new Thread(() -> {
//                    try {
//                        mqtt.sendMessage("connectedVoice","$please set command when user within the distance");
//                    } catch (MqttException e) {
//                        e.printStackTrace();
//                    }
//                });
//                t1.start();
//                break;
//            case 1:
//                Set<String> tmpCommand = new HashSet<>(Arrays.asList(voice.split("\\s+")));
//                if(this.targetObject.canExecuCommand(tmpCommand)){
//                    this.inRangeCommand = tmpCommand;
//                    this.curStage +=1;
//                    Thread t2 = new Thread(() -> {
//                        try {
//                            mqtt.sendMessage("connectedVoice","$please set command when user out of the distance");
//                        } catch (MqttException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                    t2.start();
//                    break;
//                }
//            case 2:
//                Set<String> tmpCommand1 = new HashSet<>(Arrays.asList(voice.split("\\s+")));
//                if(this.targetObject.canExecuCommand(tmpCommand1)){
//                    this.outRangeCommand = tmpCommand1;
//
//                    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
//                    workHandler= scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                synchronized (world){
//                                    for(String userName :world.getUserMap().keySet()){
//                                        double distance = world.getUserMap().get(userName).getLoc().distance(targetObject.getBoundingObject().getCenter());
//
//                                        synchronized (this) {
//                                            if (isRunning) {
//                                                if(distance>=distant*1000 && status==1){
//                                                    String[] retirmData = targetObject.execuate(outRangeCommand);
//                                                    try {
//                                                        mqtt.sendMessage(retirmData[0], retirmData[1]);
//                                                        status = 0;
//
//                                                    } catch (MqttException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                                else if(distance<distant*1000 && status==0) {
//                                                    String[] retirmData = targetObject.execuate(inRangeCommand);
//                                                    try {
//                                                        mqtt.sendMessage(retirmData[0], retirmData[1]);
//                                                        status = 1;
//
//                                                    } catch (MqttException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }, 0, 1, TimeUnit.SECONDS);
//
//                    Thread t3 = new Thread(() -> {
//                        try {
//                            mqtt.sendMessage("connectedVoice","$distance sensor added");
//                        } catch (MqttException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                    t3.start();
//
//                    synchronized (world){
//                        world.removeCurPlugin();
//                    }
//
//                }
//
//
//        }
//    }


}
