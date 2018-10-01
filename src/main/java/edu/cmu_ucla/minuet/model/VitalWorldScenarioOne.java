package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.NLP.TokenNode;
import edu.cmu_ucla.minuet.frameWork.Plugable;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.*;

public class VitalWorldScenarioOne implements World {
    private final static double STAND_Z = 1400.0;
    private final static double SIT_Z = 1000.0;
    private final static double L = 350.0;
    private final static double DISTANCE = 3000.0;

    private final String DEFAULT_USER = "Richard";

    private Map<String, User> userMap = new HashMap<>();
    private Set<VitalObject> vitalObjects = new HashSet<>();
    private MQTT mqtt = new MQTT();
    private CommandFrame curFrame = null;
    private Plugable curPlugin = null;
    private Set<Zone> registeredZones = new HashSet<>();
    private Zone currentSettingZone ;

    public VitalWorldScenarioOne() throws MqttException { }
    public Map<String, User> getUserMap() {
        return userMap;
    }
    public void addObject(VitalObject s) {
        this.vitalObjects.add(s);
    }

    public void addUser(User user) {
        Zone userZone = new Zone(user.getName());
        userZone.setCentroid(user.getLoc());
        userZone.setRadius(3000);
        this.userMap.put(user.getName(), user);
        user.setZone(userZone);
    }

    public void updateUserLoc(String[] splitedString) {
            String userName = splitedString[6];
            userMap.get(userName).updateLoc(LocationParser.parseLocation(splitedString));
            if(currentSettingZone!=null){
                Vector3D tmpLoc = LocationParser.parseLocation(splitedString);
                Vector3D newLoc = new Vector3D(tmpLoc.getX(),tmpLoc.getY(),0);
                currentSettingZone.addToList(newLoc);
            }
    }
    public void getVoice(String newData){
        newData = newData.trim();
        String[] splitedString = newData.split("\\s+");
        Set<String> tmpWords = new HashSet<>(Arrays.asList(splitedString));
        Set<Light> tmpSelectSet = new HashSet<>();
        System.out.println(newData);
        if (tmpWords.contains("turn")) {
            for(Zone zone:registeredZones){
                if(tmpWords.contains(zone.getName())){

                    for (VitalObject object : vitalObjects){
                        Vector3D tmpObjLoc = new Vector3D(object.getBoundingObject().getCenter().getX(),object.getBoundingObject().getCenter().getY(),0);
                        if(Vector3D.distance(tmpObjLoc,zone.getCentroid())<=zone.getRadius()){
                            if (Light.class.isInstance(object)) {
                                tmpSelectSet.add((Light) object);
                            }
                        }
                    }
                    break;
                }
            }
            for(VitalObject object:vitalObjects){
                if (tmpWords.contains(object.getNickname())){
                    tmpSelectSet.add((Light) object);
                    break;
                }
            }
            //found no zone is being called
            if(tmpSelectSet.isEmpty()){

                for (VitalObject object : vitalObjects){
                    Vector3D tmpObjLoc = new Vector3D(object.getBoundingObject().getCenter().getX(),object.getBoundingObject().getCenter().getY(),0);
                    if(Vector3D.distance(tmpObjLoc,userMap.get(DEFAULT_USER).getZone().getCentroid())<=10000){
                        if (Light.class.isInstance(object)) {
                            tmpSelectSet.add((Light) object);
                        }
                    }
                }
            }
            TokenNode node = new TokenNode(newData);

            if (!tmpSelectSet.isEmpty()) {
                for (Light object : tmpSelectSet){
                    String[] retirmData = object.execuateOne(node);
                    sendMqtt(retirmData);
                }

                Thread notifyVoiceThread = new Thread(() -> {
                    try {
                        mqtt.sendMessage("connectedVoice", "$OK, "+ node.getText());
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                });
                notifyVoiceThread.start();
            }
        }
        else if(tmpWords.contains("register")){

            this.currentSettingZone = new Zone(splitedString[splitedString.length-1]);
            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mqtt.sendMessage("trigger/" + "Richard", "1");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start();
            System.out.println("start setting zone");
            sendVoiceCommand("OK, please walk around");
        }else if(tmpWords.contains("set")&&currentSettingZone!=null){
            currentSettingZone.generateCentroid();
            registeredZones.add(currentSettingZone);
            currentSettingZone=null;
            sendVoiceCommand(" Zone is set");
            Thread sendThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        mqtt.sendMessage("trigger/" + "Richard", "0");
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            });
            sendThread.start();
            System.out.println("end setting zone " );
        }
        else if(tmpWords.contains("name")){
            String name = splitedString[splitedString.length-1];
            System.out.println("naming");
            Vector3D curUser = userMap.get("Richard").getLoc();
            final Comparator<VitalObject> comparator = (p1,p2)-> Double.compare(Vector3D.distance(p1.getBoundingObject().getCenter(),curUser),Vector3D.distance(p2.getBoundingObject().getCenter(),curUser));
            vitalObjects.stream().min(comparator).orElse(null).setNickname(name);
            System.out.println("named");




        }
    }
    public void revceiveData(String data) {

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
