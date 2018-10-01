package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VitalWorldScenarioThree implements World{
    private final static double STAND_Z = 1400.0;
    private final static double SIT_Z = 1000.0;
    private final static double L = 350.0;

    public Map<String, User> getUserMap() {
        return userMap;
    }

    private Map<String, User> userMap = new HashMap<>();
    private Set<VitalObject> vitalObjects = new HashSet<>();
    private MQTT mqtt = new MQTT();



    private CommandFrame curFrame = null;


    public VitalWorldScenarioThree() throws MqttException {

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

    public void monitor(String data) {
        String[] splitedString = data.split("\\s+");
        if (splitedString.length == 6) {
            boolean isSend = false;
            double yaw = (Double.parseDouble(splitedString[3]) - 30);
            yaw = (yaw >= 0) ? yaw : 360 + yaw;
            double pitch = Double.parseDouble(splitedString[4]);
            double roll = Double.parseDouble(splitedString[5]);
            double x = Double.parseDouble(splitedString[0]);
            double y = Double.parseDouble(splitedString[1]);
            double z = 1200;

            double px = -Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
            double py = -Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
            double pz = Math.sin(Math.toRadians(pitch));

            Vector3D pointVec = new Vector3D(px, py, pz);
            Vector3D pos = new Vector3D(x, y, z);
            String currentCloest = "";
            double currentAngle = 90.0;
            for(String userName: userMap.keySet()){

                System.out.println(userName);
                double tmpAngle = LocationParser.calculatePoinging(pos,pointVec,userMap.get(userName).getLoc());
                if (tmpAngle>0 && tmpAngle<90.0 && tmpAngle<currentAngle){
                    currentCloest=userName;
                    currentAngle = tmpAngle;
                }
//                if(LocationParser.calculatePoinging(pos,pointVec,userMap.get(userName).getLoc())){
//                    String[] topicNMes = new String[2];
//                    topicNMes[0] = "monitorDemo";
//                    topicNMes[1] = userName;
//                    sendMqtt(topicNMes);
//                    System.out.println("change to "+userName);
//                    isSend=true;
//                    break;
//                }
            }
            if(!currentCloest.equals("")){
                String[] topicNMes = new String[2];
                    topicNMes[0] = "monitorDemo";
                    topicNMes[1] = currentCloest;
                    sendMqtt(topicNMes);
                    System.out.println("change to "+currentCloest);
                    isSend=true;
            }
            if(!isSend){
                String[] topicNMes = new String[2];
                topicNMes[0] = "monitorDemo";
                topicNMes[1] = "nothing";
                System.out.println("change to "+"nothing");
                sendMqtt(topicNMes);
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
            double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;
            Vector3D pos = new Vector3D(x, y, z);
            System.out.println((z == STAND_Z) ? "Standing" : "Siting");
            String userName = splitedString[6];
            System.out.println("user current loc is :" + x + " " + y + " yaw: " + yaw + " pitch: " + pitch);
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
//                        curFrame = new CommandFrame(box, this, userName);
                        Thread notifyVoiceThread = new Thread(() -> {
                            try {
                                if (!box.isOne()) mqtt.sendMessage("connectedVoice", "$multiple devices");
                                else mqtt.sendMessage("connectedVoice", "*" + box.getCurObject().getName());
                            } catch (MqttException e) {
                                e.printStackTrace();
                            }
                        });
                        notifyVoiceThread.start();

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
                            mqtt.sendMessage("trigger/" + tmpName, "0");
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
            final String tmpUserName = curFrame.getUserName();
            this.curFrame.execuate(this.mqtt);
            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mqtt.sendMessage("trigger/" + tmpUserName, "0");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start();
            curFrame.kill();
            this.curFrame = null;

            if (execuType == 3) {
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
}
