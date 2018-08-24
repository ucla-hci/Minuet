package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.NLP.NLPHandler;
import edu.cmu_ucla.minuet.NLP.TokenNode;

import java.util.Set;

public class Light extends VitalObject {
//    @Override
//    public String[] execuate(TokenNode userCommand) {
//        String[] topicNMes = new String[2];
//        for(TokenNode node : execuableWords){
//            if(NLPHandler.isExecutable(node,userCommand)){
//                topicNMes[0]=getTopic();
//                topicNMes[1] = node.getCommand();
//                return topicNMes;
//            }
//        }
//
//        return topicNMes;
//    }
    @Override
    public  String[] execuate(TokenNode userCommand){
        String[] topicNMes = new String[2];
        for(TokenNode node : execuableWords){
            if(NLPHandler.isExecutable(node,userCommand)){
                topicNMes[0]=getTopic();
                topicNMes[1] = node.getCommand();
                if(topicNMes[1].equals("ON"))lastStatus=true;
                else lastStatus=false;
                return topicNMes;
            }
        }

        return topicNMes;
    }
    private volatile boolean lastStatus = false;
    @Override
    public String[] execuate(Set<String> command, String gesture) {
        return new String[0];
    }

    @Override
    public String[] execuate(String gesture) {
        String[] topicNMes = new String[2];
        if(gesture.equals("upSwap")){

            topicNMes[0] = getTopic();
            topicNMes[1]="ON";
            lastStatus=true;
        }
        if(gesture.equals("downSwap")){

            topicNMes[0] = getTopic();
            topicNMes[1]="OFF";
            lastStatus=false;
        }

        return topicNMes;
    }

    public Light(BoundingObject boundingObject, String name, String topic) {
        super(boundingObject, name, topic);

        String[][] turnOn={{"turn","on"},{"on"},{"light"}};
        String[][] turnOff={{"turn","off"},{"off"},{"dim"}};




        String[]gestures={"upSwap","downSwap"};
        for(int i=0;i<turnOn.length;i++){
            addExecuableWord(turnOn[i],"ON");
        }
        for(int j=0;j<turnOff.length;j++){
            addExecuableWord(turnOff[j],"OFF");
    }

        supportedGestures(gestures);
    }

    @Override
    public String[] selectedObject() {
        String[] topicNMes = {"trash","on"};
        return topicNMes;
    }

    @Override
    public String[] resumeObject() {
        String[] topicNMes = new String[2];
        if(lastStatus)  {
            topicNMes[0]= getTopic();
            topicNMes[1]="ON";
        }
        else {
            topicNMes[0]= getTopic();
            topicNMes[1]= "OFF";
        }
        return topicNMes;
    }

    @Override
    public String[] enteringObject() {


        String[] topicNMes = {getTopic().substring(0,getTopic().length()-5)+"dimmer","10"};
        return topicNMes;
    }

    @Override
    public String[] leavingObject() {
        String[] topicNMes = {getTopic().substring(0,getTopic().length()-5)+"dimmer","100"};
        return topicNMes;
    }
    //    @Override
//    public String[] commandItToSelectedStatus() {
//        String[] topicNMes = {getTopic(),"toggle"};
//        return topicNMes;
//    }


}
