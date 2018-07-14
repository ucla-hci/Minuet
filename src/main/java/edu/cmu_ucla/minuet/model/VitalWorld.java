package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.*;

public class VitalWorld {
    private final static double STAND_Z = 1550;
    private Map<String, User> userMap = new HashMap<>();
    private Set<User> userSet = new HashSet<>();
    private Set<VitalObject> vitalObjects = new HashSet<>();
    private MQTT mqtt = new MQTT();
    private List<LocData> currLocData = new ArrayList<>();
    private Set<VitalObject> currSelectedObject = new HashSet<>();
    private List<String> audioAna = new ArrayList<>();
    private CommandProcessor commandProcessor = new CommandProcessor();
    private String currentVoiceCommand = "";
    private CommandFrame curFrame = null;

    /**
     *
     */
    public VitalWorld() throws MqttException {

    }

    //user should hold all of his LOCdata
    public void selection() {
        for (LocData locData : currLocData) {
            Vector3D pointingVec = new Vector3D(
                    (Math.sin(Math.toRadians(360 - locData.getYaw())) * Math.cos(Math.toRadians(locData.getPitch()))),
                    (-Math.cos(Math.toRadians(360 - locData.getYaw())) * Math.cos(Math.toRadians(locData.getPitch()))),
                    (Math.sin(Math.toRadians(locData.getPitch()))));
            for (VitalObject object : vitalObjects) {
                if (object.checkBePointed(locData.getPos(), pointingVec)) {
                    this.currSelectedObject.add(object);
                }
            }
        }
    }

    public void checkInteraction2() {
        ObjectCommandPacket curPacket;
        for (VitalObject selectedObject : currSelectedObject) {
            curPacket = selectedObject.checkNExeCommand(this.commandProcessor.inspectTheString(audioAna));
            try {
                if (curPacket != null)
                    this.mqtt.sendMessage(curPacket.getTopic(), curPacket.getCommand());

            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

//    public void setAudioResult(List<SpeechRecognitionResult> results) {
//        for (SpeechRecognitionResult result : results) {
//            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
//            System.out.printf("Transcription: %s%n", alternative.getTranscript());
//            for (WordInfo wordInfo : alternative.getWordsList()) {
//                if (wordInfo.getWord().equals("this")) {
//                    double startTime = wordInfo.getStartTime().getSeconds() + (double) wordInfo.getStartTime().getNanos() / 1000000000;
//                    double endTime = wordInfo.getEndTime().getSeconds() + (double) wordInfo.getEndTime().getNanos() / 1000000000;
//                    int index = (int) (((startTime + endTime) / 2.0) * 10);
//                    //////!!!!!!!!!!!!!!!!!!!!!!!change it later
//                    for (User user : this.userSet) {
//
//                        LocData curLoc = this.currLocData.get(index);
//                        user.updataData(curLoc.getPitch(), curLoc.getRoll(), curLoc.getYaw(), curLoc.getPos());
//                        checkInteraction(user);
//
//                    }
//
//                }
//
//            }
//
//        }
//        this.currLocData.clear();
//    }


    public void passTheLocs(List<LocData> data) {
        this.currLocData = data;
    }

    public void addObject(VitalObject s) {
        this.vitalObjects.add(s);
    }

    public void addUser(User user) {
        this.userSet.add(user);
        this.userMap.put(user.getName(), user);
    }

    private void checkInteraction(User user) {
        Set<VitalObject> possibleObject = vitalObjects;


        for (VitalObject object : possibleObject) {
            if (object.checkBePointed(user.getPos(), user.getPointVec())) {
                try {
                    Set<String> words = new HashSet<>(Arrays.asList(currentVoiceCommand.split("\\s+")));
                    if (object.getName().equals("musicPlayer")) {
                        if (words.contains("next")) {
                            Process p = Runtime.getRuntime().exec("spotify next");
                        }
                        if (words.contains("start")) {
                            Process p = Runtime.getRuntime().exec("spotify start");
                        }
                        if (words.contains("stop")) {
                            Process p = Runtime.getRuntime().exec("spotify pause");
                        }
                    }
                    if (object.getName().equals("sonoffSwitch")) {
                        if (words.contains("turn") && words.contains("on")) {
                            mqtt.toggleSonoff(object);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void revceiveData(String data) {
        System.out.println("received:" + data);
        String[] splitedString = data.split("\\s+");
        System.out.println(Thread.currentThread().getName());
        if (splitedString.length == 7) {
            double yaw = (double) Math.floorMod((int) (Double.parseDouble(splitedString[3]) - 25), 360);
            double pitch = Double.parseDouble(splitedString[4]);
            double roll = Double.parseDouble(splitedString[5]);
            Vector3D pos = new Vector3D(Double.parseDouble(splitedString[0]), Double.parseDouble(splitedString[1]), STAND_Z);
            String userName = splitedString[6];
            userMap.get(userName).updataData(pitch, roll, yaw, pos);

            for (VitalObject object : vitalObjects) {
                if (curFrame==null && object.checkBePointed(userMap.get(userName).getPos(), userMap.get(userName).getPointVec())) {
                    synchronized (this) {
                        curFrame = new CommandFrame(object, this);
                    }
                    System.out.println("sssss");
//                    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
//                    final Runnable cancellation = new Runnable() {
//                        @Override
//                        public void run() {
//                            synchronized (this) {
//                                if (curFrame!= null &&!curFrame.isExecAble()) {
//                                    curFrame = null;
//                                }
//                            }
//                        }
//                    };
//                    scheduledExecutorService.schedule(cancellation, 20, TimeUnit.SECONDS);

                    break;
                }
            }
        }
    }
//    public void revceiveData(String data) {
//        System.out.println("received:" + data);
//        String[] splitedString = data.split("\\s+");
//
//        if (splitedString.length == 7) {
//            double yaw = (double) Math.floorMod((int) (Double.parseDouble(splitedString[3]) - 25), 360);
//            double pitch = Double.parseDouble(splitedString[4]);
//            double roll = Double.parseDouble(splitedString[5]);
//            System.out.println("angle : yaw: " + yaw + " pitch: " + pitch + " roll: " + roll);
//            Vector3D pos = new Vector3D(Double.parseDouble(splitedString[0]), Double.parseDouble(splitedString[1]), STAND_Z);
//            String userName = splitedString[6];
//            for (User user : userSet) {
//                if (user.getName().equals(userName)) {
//                    System.out.println("user updated");
//                    user.updataData(pitch, roll, yaw, pos);
//                }
////                checkInteraction(user);
//            }
//            System.out.println("checkFinished");
//        }
//    }

    public  CommandFrame getCurFrame() {

        synchronized (this) {
//            System.out.println("getFrame: "+Thread.currentThread().getName());
//            System.out.println("getFrame the frame is :" + curFrame);
            return curFrame;
        }
    }

        public void revceiveSpeechData (String data){

            currentVoiceCommand = data.trim();
            for (User user : userSet) {
                checkInteraction(user);
            }
            currentVoiceCommand = "";
        }

    public void execuFrame() {
//        System.out.println("execuFrame: "+Thread.currentThread().getName());

        synchronized (this) {
            this.curFrame.execuate(this.mqtt);
            this.curFrame=null;
        }
//        System.out.println("curFrame is :"+this.curFrame);
    }
}
