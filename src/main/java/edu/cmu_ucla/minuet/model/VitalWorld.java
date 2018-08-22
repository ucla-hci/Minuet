package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.frameWork.DisSensor;
import edu.cmu_ucla.minuet.frameWork.Plugable;
import edu.cmu_ucla.minuet.frameWork.PluginRemover;
import edu.cmu_ucla.minuet.frameWork.Timer;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.*;

public class VitalWorld {
    private final static double STAND_Z = 1400.0;
    private final static double SIT_Z = 1000.0;
    private final static double L = 350.0;

    public Map<String, User> getUserMap() {
        return userMap;
    }

    private Map<String, User> userMap = new HashMap<>();
    private Set<VitalObject> vitalObjects = new HashSet<>();
    private MQTT mqtt = new MQTT();
    private BoundingSphere BED = new BoundingSphere(new Vector3D(2000, 5300, 500), 1000);
    private BoundingSphere DESK = new BoundingSphere(new Vector3D(3100, 8000, 700), 1000);
    private Set<Artifacts> artifacts = new HashSet<>();
    private CommandFrame curFrame = null;
    private Plugable curPlugin = null;
    private Set<String> supportedPluginNames = new HashSet<String>(Arrays.asList("timer","sensor","remove"));
    private Set<Plugable> workingPlugins = new HashSet<>();


    public Set<Plugable> getWorkingPlugins() {
        return workingPlugins;
    }


    public synchronized Plugable getCurPlugin() {
        return curPlugin;
    }
    public void removeCurPlugin(){
        workingPlugins.add(this.curPlugin);
        this.curPlugin = null;
    }


    public void ifMeansPlugin(Set<String> words) {
        synchronized (this) {
            for(String word : words){
                if(supportedPluginNames.contains(word)){
                    switch (word){
                        case "timer":
                            this.curPlugin = new Timer(this.mqtt,this);
                            break;
                        case "sensor":
                            this.curPlugin = new DisSensor(this.mqtt,this);
                            break;

                        case "remove":
                            this.curPlugin = new PluginRemover(this.mqtt,this);
                            break;
                    }

                }

            }
        }
    }

    public VitalWorld() throws MqttException {

    }
    public void addArtifact(Artifacts artifacts) {
        this.artifacts.add(artifacts);
    }
    public void addObject(VitalObject s) {
        this.vitalObjects.add(s);
    }

    public void addUser(User user) {

        this.userMap.put(user.getName(), user);
    }

    public void updateUserLoc(String[] splitedString) {

        if (splitedString.length == 7) {
            double yaw = (Double.parseDouble(splitedString[3]) - 30);
            yaw = (yaw >= 0) ? yaw : 360 + yaw;
            double pitch = Double.parseDouble(splitedString[4]);
            double x = Double.parseDouble(splitedString[0]) + L * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
            double y = Double.parseDouble(splitedString[1]) + L * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
            double proxyZ = Double.parseDouble(splitedString[2]) - Math.sin(Math.toRadians(pitch)) * L;
            double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;
            Vector3D loc = new Vector3D(x, y, z);
            String userName = splitedString[6];
            userMap.get(userName).updateLoc(loc);
        }
    }

    public void lightData(String data) {
        String[] splitedString = data.split("\\s+");
        if (splitedString.length == 6) {
            double yaw = (Double.parseDouble(splitedString[3]) - 30);
            yaw = (yaw >= 0) ? yaw : 360 + yaw;
            double pitch = Double.parseDouble(splitedString[4]);
            double roll = Double.parseDouble(splitedString[5]);
            double x = Double.parseDouble(splitedString[0]);
            double y = Double.parseDouble(splitedString[1]);
            double z = 1300;
            Vector3D pos = new Vector3D(x, y, z);
            double px = -Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
            double py = -Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
            double pz = Math.sin(Math.toRadians(pitch));
            Vector3D pointVec = new Vector3D(px, py, pz);
            if (pitch >= 0) {
                Thread sendThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mqtt.sendMessage("cmnd/sonoffB1/POWER", "OFF");
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                });
                sendThread.start();
            } else {
                if (BED.calculate(pos, pointVec)) {
                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mqtt.sendMessage("cmnd/sonoffB1/POWER", "ON");
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    sendThread.start();
                    Thread colorThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mqtt.sendMessage("cmnd/sonoffB1/CT", "500");
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    colorThread.start();
                } else if (DESK.calculate(pos, pointVec)) {
                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mqtt.sendMessage("cmnd/sonoffB1/POWER", "ON");
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    sendThread.start();
                    Thread colorThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mqtt.sendMessage("cmnd/sonoffB1/CT", "156");
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    colorThread.start();
                } else {
                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mqtt.sendMessage("cmnd/sonoffB1/POWER", "OFF");
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    sendThread.start();
                }
            }

        }
    }

    public void directReceiveData(String data) {
        String[] splitedString = data.split("\\s+");
        if (splitedString.length == 7) {
            double yaw = (Double.parseDouble(splitedString[3]) - 30);
            yaw = (yaw >= 0) ? yaw : 360 + yaw;
            double pitch = Double.parseDouble(splitedString[4]);
            double roll = Double.parseDouble(splitedString[5]);
            double x = Double.parseDouble(splitedString[0]) + L * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
            double y = Double.parseDouble(splitedString[1]) + L * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
            double proxyZ = Double.parseDouble(splitedString[2]) - Math.sin(Math.toRadians(pitch)) * L;
            double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;
            Vector3D pos = new Vector3D(x, y, z);
            String userName = splitedString[6];
            System.out.println("stand or sit?: " + z);
            userMap.get(userName).updataData(pitch, roll, yaw, pos);
            for (Artifacts artifact : artifacts) {
                if (curFrame == null && artifact.checkBePointed(userMap.get(userName).getPos(), userMap.get(userName).getPointVec())) {
                    Thread sendThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                mqtt.sendMessage("museum/" + userName, artifact.getName());
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
        String[] splitedString = data.split("\\s+");

        if (splitedString.length == 7) {
            double yaw = (Double.parseDouble(splitedString[3]) - 30);
            yaw = (yaw >= 0) ? yaw : 360 + yaw;
            double pitch = Double.parseDouble(splitedString[4]);
            double roll = Double.parseDouble(splitedString[5]);
            double x = Double.parseDouble(splitedString[0]) + L * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
            double y = Double.parseDouble(splitedString[1]) + L * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
            double proxyZ = Double.parseDouble(splitedString[2]) - Math.sin(Math.toRadians(pitch)) * L;
            double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;
            Vector3D pos = new Vector3D(x, y, z);
            System.out.println((z == STAND_Z) ? "Standing" : "Siting");
            String userName = splitedString[6];
            System.out.println("user current loc is :" + x + " " + y + " yaw: " + yaw + " pitch: " + pitch);
            userMap.get(userName).updataData(pitch, roll, yaw, pos);
            if (curFrame == null) {
                for (VitalObject object : vitalObjects) {
                    if (curFrame == null && object.checkBePointed(userMap.get(userName).getPos(), userMap.get(userName).getPointVec())&&curPlugin==null) {
                        synchronized (this) {
                            System.out.println("currentUserName: "+userName);
                            Thread sendThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mqtt.sendMessage("trigger/"+userName, "1");

                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            sendThread.start();
                            curFrame = new CommandFrame(object, this,userName);
                            Thread notifyVoiceThread = new Thread(() -> {
                                try {
                                    mqtt.sendMessage("connectedVoice", "*" + object.getName());
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            });
                            notifyVoiceThread.start();
                            System.out.println("connected with " + object.getName());
                        }
                        break;
                    }
                    else if (curPlugin!=null && curFrame == null && object.checkBePointed(userMap.get(userName).getPos(), userMap.get(userName).getPointVec())){
                        curPlugin.setTargetObject(object);
                    }
                }
            } else {
                synchronized (this) {
                    double tmpz = userMap.get(userName).getPos().getZ();
                    double tmpX = Math.abs(tmpz / Math.tan(Math.toRadians(pitch))) * -Math.sin(Math.toRadians(yaw)) + userMap.get(userName).getPos().getX();
                    double tmpY = Math.abs(tmpz / Math.tan(Math.toRadians(pitch))) * -Math.cos(Math.toRadians(yaw)) + userMap.get(userName).getPos().getY();
                    curFrame.setSecLoc(new LocData(0, 0, 0, new Vector3D(tmpX, tmpY, 0)));
                    System.out.println("update sec loc to " + tmpX + " " + tmpY);
                }
            }
        }
    }

    public void killCurFrame() {

        synchronized (this) {
            final String tmpName = curFrame.getUserName();
            if (curFrame != null) {
                Thread sendThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mqtt.sendMessage("trigger/"+tmpName, "0");
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    }
                });
                sendThread.start();
                curFrame.kill();
                Thread notifyVoiceThread = new Thread(() -> {
                    try {
                        mqtt.sendMessage("connectedVoice", "$timeout");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                });
                notifyVoiceThread.start();
                curFrame = null;

            }
        }

    }

    public CommandFrame getCurFrame() {

        synchronized (this) {
            return curFrame;
        }
    }


    public void execuFrame() {
        synchronized (this) {
            final String tmpUserName = curFrame.getUserName();
            this.curFrame.execuate(this.mqtt);
            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mqtt.sendMessage("trigger/"+tmpUserName, "0");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start();
            curFrame.kill();
            this.curFrame = null;

            Thread notifyVoiceThread = new Thread(() -> {
                try {
                    mqtt.sendMessage("connectedVoice", "$command executed");
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            });
            notifyVoiceThread.start();
        }
    }
}
