package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VitalWorldScenarioTwo implements World {
    private final static double STAND_Z = 1400.0;
    private final static double SIT_Z = 1000.0;
    private final static double L = 350.0;

    public Map<String, User> getUserMap() {
        return userMap;
    }

    private Map<String, User> userMap = new HashMap<>();
    private Set<VitalObject> vitalObjects = new HashSet<>();
    private MQTT mqtt = new MQTT();
    private ContinuesFrame curFrame = null;
    public VitalWorldScenarioTwo() throws MqttException { }
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
            double roll = Double.parseDouble(splitedString[5]);
            double x = Double.parseDouble(splitedString[0]) + L * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
            double y = Double.parseDouble(splitedString[1]) + L * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
            double proxyZ = Double.parseDouble(splitedString[2]) - Math.sin(Math.toRadians(pitch)) * L;
            double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;
            Vector3D loc = new Vector3D(x, y, z);
            String userName = splitedString[6];
            userMap.get(userName).updateLoc(loc);
            if(curFrame!=null) {
                curFrame.setCurDir(pitch, yaw, roll);
                }
            }

    }

    public void revceiveData(String data) {
        String[] splitedString = data.split("\\s+");

        if (splitedString.length == 7) {

            double yaw = (Double.parseDouble(splitedString[3]) - 30);
            yaw = (yaw >= 0) ? yaw : 360 + yaw;
            double pitch = Double.parseDouble(splitedString[4])-5;
            double roll = Double.parseDouble(splitedString[5]);
            double x = Double.parseDouble(splitedString[0]) + L * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
            double y = Double.parseDouble(splitedString[1]) + L * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
            double proxyZ = Double.parseDouble(splitedString[2]) - Math.sin(Math.toRadians(pitch)) * L;
//            double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;
            double z = STAND_Z;
            Vector3D pos = new Vector3D(x, y, z);
            String userName = splitedString[6];
            userMap.get(userName).updataData(pitch, roll, yaw, pos);

            if (curFrame == null) {
                ObjectBox box = new ObjectBox();

                for (VitalObject object : vitalObjects) {

                    if (object.checkBePointed(userMap.get(userName).getPos(), userMap.get(userName).getPointVec())) {
                            box.addToBox(object);
                    }
                }
                if (!box.isEmpty()) {
                    synchronized (this) {
                        System.out.println("currentUserName: " + userName);
                        Thread sendThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    mqtt.sendMessage("trigger/" + userName, "1");

                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        sendThread.start();
                        curFrame = new ContinuesFrame(box, this, userName);

                    }
                }

            } else {
                for (VitalObject object : vitalObjects) {

                    if (object.checkBePointed(userMap.get(userName).getPos(), userMap.get(userName).getPointVec())) {
                        if(object==curFrame.getBox().getCurObject()){
                            curFrame=null;
                            Thread sendThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mqtt.sendMessage("trigger/" + userName, "0");
                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            sendThread.start();
                            System.out.println("device removed");
                            break;
                        }
                        else{
                            curFrame.getBox().addToBox(object);
                            Thread sendThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        mqtt.sendMessage("trigger/" + userName, "0");
                                        try {
                                            Thread.sleep(100);
                                            mqtt.sendMessage("trigger/" + userName, "1");

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (MqttException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            sendThread.start();

                        }

                    }


                    for(String tmpUserName : userMap.keySet()) {
                        if (!tmpUserName.equals(userName)) {
                            if (userMap.get(tmpUserName).checkBePointed(userMap.get(userName).getPos(), userMap.get(userName).getPointVec())) {

                                    curFrame.addUser(tmpUserName);
                                    System.out.println(userMap.get(tmpUserName).getName() + " is authorized");
                                Thread sendThread = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            mqtt.sendMessage("trigger/" + userName, "0");
                                            try {
                                                Thread.sleep(100);
                                                mqtt.sendMessage("trigger/" + userName, "1");

                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
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
            }
        }
    }



    public ContinuesFrame getCurFrame() {

        synchronized (this) {
            return curFrame;
        }
    }

    public void sendVoiceCommand(String command) {
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mqtt.sendMessage("connectedVoice", "$" + command);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        sendThread.start();
    }
    public void sendMqtt(String[] command){
        Thread sendThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mqtt.sendMessage(command[0] , command[1]);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        sendThread.start();
    }
    public void execuFrame(int execuType) {
        synchronized (this) {
            this.curFrame.execuate(this.mqtt);

        }
    }
}
