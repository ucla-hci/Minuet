package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.NLP.NLPHandler;
import edu.cmu_ucla.minuet.NLP.TokenNode;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import edu.stanford.nlp.util.ArraySet;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ContinuesFrame {
    private boolean isExecAble = false;
    private TokenNode curCommand;
    private String curGesture = "";
    private ExecutorService scheduledExecutorService;
    private final VitalWorldScenarioTwo world;
    private int execuType = 0;
    private volatile boolean continueWake = false;
    private volatile boolean isDead= false;
    private String userName;
    private LocData secLoc = null;
    private ObjectBox box;
    private Set<String> users = new HashSet<>();
    private Vector3D curDir=null;

    public ContinuesFrame(ObjectBox box, VitalWorldScenarioTwo world, String userName) {
        users.add(userName);
        this.box = box;
        this.userName = userName;
        this.world = world;
        this.scheduledExecutorService = Executors.newFixedThreadPool(5);
        runWakeUp();

    }
    public void runWakeUp() {

        final Runnable keepChecking = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {

                    while (!isDead) {

                        if(continueWake){

                            if(MusicPlayer.class.isInstance(box.getCurObject())){
                            int volume = (int)(50+(-curDir.getZ())*5/9);
                                String[] topicNMes = new String[2];
                                topicNMes[0]=box.getCurObject().getTopic();
                                topicNMes[1]= "6 "+volume;

                                System.out.println("volume is "+topicNMes[1]);
                            world.sendMqtt(topicNMes);
                            }

                        }
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        };
        scheduledExecutorService.submit(keepChecking);
    }
    public String getUserName() {
        return userName;
    }

    public void setSecLoc(LocData secLoc) {
        this.secLoc = secLoc;
    }

    private void checkExcuable() {

        if (box.getCurObject().canExecuCommand(curCommand)) {
            isExecAble = true;
            execuType = 1;
        } else if (box.getCurObject().canExcuCommandWithGesture(new ArraySet<>(), curGesture)) {
            isExecAble = true;
            execuType = 2;
        } else if (box.getCurObject().canExcuGesture(curGesture)) {
            isExecAble = true;
            execuType = 3;
        }
        if (isExecAble) {
            world.execuFrame(execuType);
        }
    }


    public void setCurCommand(String text) {
        if(text.equals("volume")&&MusicPlayer.class.isInstance(box.getCurObject())){

            continueWake = true;
        }
        else if(text.equals("set")&&MusicPlayer.class.isInstance(box.getCurObject())){
            continueWake=false;
        }
        else {
            try {
                TokenNode command = NLPHandler.parse(text);
                curCommand=command;
                checkExcuable();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Set<String> getUsers() {
        return users;
    }

    public void addUser(String user){
        users.add(user);
    }
    public void setCurGesture(String curGesture) {
            if (box.getCurObject().canExcuGesture(curGesture)) {
                this.curGesture = curGesture;
                checkExcuable();

            }
        }


    public void setCurDir(double pitch, double yaw, double roll){
        this.curDir = new Vector3D(pitch,yaw,roll);
    }

    public TokenNode getCurCommand() {
        return curCommand;
    }

    public String getCurGesture() {
        return curGesture;
    }

    public ObjectBox getBox() {
        return box;
    }

    public void execuate(MQTT mqtt) {
        String[] retirmData = new String[2];
        switch (execuType) {
            case 1:
                retirmData = box.getCurObject().execuate(curCommand);
                break;
            case 2:

                retirmData = box.getCurObject().execuate(new ArraySet<>(), curGesture);
//                curGesture = null;
                break;
            case 3:
                retirmData = box.getCurObject().execuate(curGesture);
//                curGesture = null;

                break;
        }
        if (retirmData.length == 2) {
            try {

                if (Projector.class.isInstance(box.getCurObject()) && retirmData[1].equals("show")) {

                    retirmData[1] = retirmData[1] + " " + userName;

                }



                mqtt.sendMessage(retirmData[0], retirmData[1]);

                ///only for this this this lights
                for(VitalObject object:box.getBox()){
                    System.out.println("found 1!");
                    if(object.canExcuGesture(curGesture) && object!=box.getCurObject()){
                        System.out.println("enter!!!!!!");
                        world.sendMqtt(object.execuate(curGesture));
                        System.out.println("sentOne!!");
                    }
                    else if(object.canExecuCommand(curCommand) && object!=box.getCurObject()){
                        System.out.println("enter!!!!!!");
                        world.sendMqtt(object.execuate(curCommand));
                        System.out.println("sentOne!!");
                    }
                }
                curGesture = null;
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
    }
}
