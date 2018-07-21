package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.*;

public class VitalWorld {
    private final static double STAND_Z = 1550;
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
            double yaw = (double) Math.floorMod((int) (Double.parseDouble(splitedString[3]) + 25), 360);
            double pitch = Double.parseDouble(splitedString[4]);
            double roll = Double.parseDouble(splitedString[5]);
            Vector3D pos = new Vector3D(Double.parseDouble(splitedString[0]), Double.parseDouble(splitedString[1]), STAND_Z);
            String userName = splitedString[6];
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
        System.out.println("received:" + data);
        String[] splitedString = data.split("\\s+");

        if (splitedString.length == 7) {
            double yaw = (double) Math.floorMod((int) (Double.parseDouble(splitedString[3]) - 40), 360);
            double pitch = Double.parseDouble(splitedString[4]);
            double roll = Double.parseDouble(splitedString[5]);
            Vector3D pos = new Vector3D(Double.parseDouble(splitedString[0]), Double.parseDouble(splitedString[1]), STAND_Z);
            String userName = splitedString[6];
            userMap.get(userName).updataData(pitch, roll, yaw, pos);

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
                    }
                    break;
                } else if (curFrame != null) {
                    synchronized (this) {

                        double tmpX = (STAND_Z / Math.tan(Math.toRadians(pitch))) * Math.cos(Math.toRadians(270 - yaw)) + userMap.get(userName).getPos().getX();
                        double tmpY = (STAND_Z / Math.tan(Math.toRadians(pitch))) * Math.sin(Math.toRadians(270 - yaw)) + userMap.get(userName).getPos().getY();
                        curFrame.setSecLoc(new LocData(0, 0, 0, new Vector3D(tmpX, tmpY, 0)));
                        System.out.println("update sec loc to " + tmpX + " " + tmpY);
                    }
                }
            }
        }
    }

    public void killCurFrame() {
        synchronized (this) {
            if (curFrame != null) {
                curFrame.kill();
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
        }
    }
}
