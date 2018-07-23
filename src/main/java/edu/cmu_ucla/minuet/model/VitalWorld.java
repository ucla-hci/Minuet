package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.*;

public class VitalWorld {
    private final static double STAND_Z = 1400.0;
    private final static double SIT_Z = 1000.0;
    private final static double L = 350.0;
    private Map<String, User> userMap = new HashMap<>();

    private Set<VitalObject> vitalObjects = new HashSet<>();
    private MQTT mqtt = new MQTT();

    public void addArtifact(Artifacts artifacts) {
        this.artifacts.add( artifacts);
    }

    private Set<Artifacts> artifacts = new HashSet<>();
    private CommandFrame curFrame = null;

    /**
     *
     */
    public VitalWorld() throws MqttException {

    }

    public void addObject(VitalObject s) {
        this.vitalObjects.add(s);
    }

    public void addUser(User user) {

        this.userMap.put(user.getName(), user);
    }

    public void directReceiveData(String data) {
        String[] splitedString = data.split("\\s+");
        if (splitedString.length == 7) {
            double yaw = (Double.parseDouble(splitedString[3])-30);
            yaw = (yaw>=0)? yaw: 360+yaw;
            double pitch = Double.parseDouble(splitedString[4]);
            double roll = Double.parseDouble(splitedString[5]);
            double x = Double.parseDouble(splitedString[0])+L*Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw));
            double y = Double.parseDouble(splitedString[1])+L*Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw));
            double proxyZ = Double.parseDouble(splitedString[2])-Math.sin(Math.toRadians(pitch))*L;
            double z =(Math.abs(proxyZ-STAND_Z)>=Math.abs(proxyZ-SIT_Z))?SIT_Z:STAND_Z;
            Vector3D pos = new Vector3D(x,y, z);
            String userName = splitedString[6];
            System.out.println("stand or sit?: "+ z);
            userMap.get(userName).updataData(pitch, roll, yaw, pos);
            for (Artifacts artifact : artifacts) {
                if (curFrame == null && artifact.checkBePointed(userMap.get(userName).getPos(), userMap.get(userName).getPointVec())) {
                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                mqtt.sendMessage( "museum/"+userName, artifact.getName());
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                   sendThread.start();
                   System.out.println(artifact.getName());
                }

            }

        }
    }



    public void revceiveData(String data) {
//        System.out.println("received:" + data);
        String[] splitedString = data.split("\\s+");

        if (splitedString.length == 7) {
            double yaw = (Double.parseDouble(splitedString[3])-30);
            yaw = (yaw>=0)? yaw: 360+yaw;
            double pitch = Double.parseDouble(splitedString[4]);
            double roll = Double.parseDouble(splitedString[5]);
            double x = Double.parseDouble(splitedString[0])+L*Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw));
            double y = Double.parseDouble(splitedString[1])+L*Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw));
            double proxyZ = Double.parseDouble(splitedString[2])-Math.sin(Math.toRadians(pitch))*L;
            double z =(Math.abs(proxyZ-STAND_Z)>=Math.abs(proxyZ-SIT_Z))?SIT_Z:STAND_Z;
            Vector3D pos = new Vector3D(x,y, z);
            System.out.println((z==STAND_Z)?"Standing":"Siting");
            String userName = splitedString[6];
            System.out.println("user current loc is :" + x + " " + y + " yaw: "+ yaw + " pitch: "+ pitch);
            userMap.get(userName).updataData(pitch, roll, yaw, pos);
            if (curFrame == null) {
                for (VitalObject object : vitalObjects) {
                    if (curFrame == null && object.checkBePointed(userMap.get(userName).getPos(), userMap.get(userName).getPointVec())) {
                        synchronized (this) {
                            Thread sendThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mqtt.sendMessage("trigger", "1");
                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            sendThread.start();
                            curFrame = new CommandFrame(object, this);
                            Thread notifyVoiceThread = new Thread(()->{
                                try {
                                    mqtt.sendMessage("connectedVoice","*"+object.getName());
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            });
                            notifyVoiceThread.start();
                            System.out.println("connected with "+ object.getName());
                        }
                        break;
                    }
                }
            }
            else{
                synchronized (this) {
                    double tmpz = userMap.get(userName).getPos().getZ();
                    double tmpX = Math.abs(tmpz / Math.tan(Math.toRadians(pitch))) * -Math.sin(Math.toRadians(yaw)) + userMap.get(userName).getPos().getX();
                    double tmpY =  Math.abs(tmpz / Math.tan(Math.toRadians(pitch))) * -Math.cos(Math.toRadians(yaw)) + userMap.get(userName).getPos().getY();
                    curFrame.setSecLoc(new LocData(0, 0, 0, new Vector3D(tmpX, tmpY, 0)));
                    System.out.println("update sec loc to " + tmpX + " " + tmpY);
                }
            }
        }
    }

    public void killCurFrame() {
        synchronized (this) {
            if (curFrame != null) {
                curFrame.kill();
                Thread notifyVoiceThread = new Thread(()->{
                    try {
                        mqtt.sendMessage("connectedVoice","$timeout");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                });
                notifyVoiceThread.start();
                curFrame = null;

            }
        }
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mqtt.sendMessage("trigger", "0");
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        sendThread.start();
    }

    public CommandFrame getCurFrame() {

        synchronized (this) {
            return curFrame;
        }
    }


    public void execuFrame() {
        synchronized (this) {
            this.curFrame.execuate(this.mqtt);
            curFrame.kill();
            this.curFrame = null;
            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mqtt.sendMessage("trigger", "0");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start();
            Thread notifyVoiceThread = new Thread(()->{
                try {
                    mqtt.sendMessage("connectedVoice","$command executed");
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            });
            notifyVoiceThread.start();
        }
    }
}
