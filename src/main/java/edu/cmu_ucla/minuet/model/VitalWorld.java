package edu.cmu_ucla.minuet.model;

import com.google.cloud.speech.v1p1beta1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1p1beta1.SpeechRecognitionResult;
import com.google.cloud.speech.v1p1beta1.WordInfo;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VitalWorld {
    private final static double STAND_Z = 1550;
    private Set<User> userSet = new HashSet<>();
    private Set<VitalObject> vitalObjects = new HashSet<>();
    private MQTT mqtt= new MQTT();
    private List<LocData> currLocData = new ArrayList<>();
    private Set<VitalObject> currSelectedObject = new HashSet<>();
    private CommandProcessor commandProcessor = new CommandProcessor();

    /**
     *
     */
    public VitalWorld() throws MqttException {


    }



    public void setAudioResult(List<SpeechRecognitionResult> results) {
        for (SpeechRecognitionResult result : results) {
            SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
            System.out.printf("Transcription: %s%n", alternative.getTranscript());
            for (WordInfo wordInfo : alternative.getWordsList()) {
                if (wordInfo.getWord().equals("this")) {
                    double startTime = wordInfo.getStartTime().getSeconds() + (double) wordInfo.getStartTime().getNanos() / 1000000000;
                    double endTime = wordInfo.getEndTime().getSeconds() + (double) wordInfo.getEndTime().getNanos() / 1000000000;
                    int index = (int) (((startTime + endTime) / 2.0) * 10);
                    //////!!!!!!!!!!!!!!!!!!!!!!!change it later
                    for (User user : this.userSet) {

                            LocData curLoc = this.currLocData.get(index);
                            user.updataData(curLoc.getPitch(), curLoc.getRoll(), curLoc.getYaw(), curLoc.getPos());
                            checkInteraction(user);

                    }

                }

            }

        }
        this.currLocData.clear();
    }



    public void passTheLocs(List<LocData> data) {
            this.currLocData = data;
    }

    public void addObject(VitalObject s) {
        this.vitalObjects.add(s);
    }

    public void addUser(User user) {
        this.userSet.add(user);
    }

    private void checkInteraction(User user) {
        Set<VitalObject> possibleObject = vitalObjects;
//        if(user.getPitch()>=0){
//            for(edu.cmu_ucla.minuet.model.VitalObject object:possibleObject){
//                if(object.getBoundingObject().getCenter().getZ()< user.getPos().getZ()){
//                    possibleObject.remove(object);
//                }
//            }
//        }
//        else {
//            for(edu.cmu_ucla.minuet.model.VitalObject object:possibleObject){
//                if(object.getBoundingObject().getCenter().getZ()> user.getPos().getZ()){
//                    possibleObject.remove(object);
//                }
//            }
//        }

        for (VitalObject object : possibleObject) {
            if (object.checkBePointed(user.getPos(), user.getPointVec())) {
                try {
                    mqtt.toggleSonoff(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        for (edu.cmu_ucla.minuet.model.User user: userSet){
//            Rotation rotation = new Rotation(RotationOrder.ZYX,user.getYaw(),user.getPitch(),user.getRoll());
//            //rotate the (1,0,0) vector to target vector
//            Vector3D pointingVec = rotation.applyTo(new Vector3D(1,0,0));

//            for(edu.cmu_ucla.minuet.model.VitalObject object:this.vitalObjects){
//                Vector3D vecToPos = object.getCenter().subtract(pointingVec);
//                System.out.println(Vector3D.angle(pointingVec,vecToPos));
//                if (Math.abs(Vector3D.angle(pointingVec,vecToPos))<=PRECISION){
//                    System.out.println("connected to "+ object.getName() );
//                }
//
//            }
    }

    public void revceiveData(String data) {
        System.out.println("received:" + data);
        String[] splitedString = data.split("\\s+");

        if (splitedString.length == 7) {
            double yaw = (double) Math.floorMod((int) (Double.parseDouble(splitedString[0]) - 25), 360);
            double pitch = Double.parseDouble(splitedString[1]);
            double roll = Double.parseDouble(splitedString[2]);
            System.out.println("angle : yaw: " + yaw + " pitch: " + pitch + " roll: " + roll);
            Vector3D pos = new Vector3D(Double.parseDouble(splitedString[3]), Double.parseDouble(splitedString[4]), STAND_Z);
            String userName = splitedString[6];
            for (User user : userSet) {
                if (user.getName().equals(userName)) {
                    System.out.println("user updated");
                    user.updataData(pitch, roll, yaw, pos);
                }
                checkInteraction(user);
            }
            System.out.println("checkFinished");


        }


//        for (String s:splitedString)
//        System.out.println(s);


    }

}
