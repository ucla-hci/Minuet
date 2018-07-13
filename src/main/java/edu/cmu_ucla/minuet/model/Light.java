package edu.cmu_ucla.minuet.model;

import java.util.Set;

public class Light extends VitalObject {
    @Override
    public String[] execuate(Set<String> command) {
        String[] topicNMes = new String[2];
        if(command.contains("turn")&&command.contains("on")){
            topicNMes[0]=getTopic();
            topicNMes[1]="ON";
        }
        else if (command.contains("turn")&&command.contains("off")){
            topicNMes[0]=getTopic();
            topicNMes[1]="OFF";
        }
        return topicNMes;
    }

    @Override
    public String[] execuate(Set<String> command, String gesture) {
        return new String[0];
    }

    @Override
    public String[] execuate(String gesture) {
        String[] topicNMes = new String[2];
        if(gesture.equals("upSwap")){
            topicNMes[0] = getTopic();
            topicNMes[1]="toggle";
        }
        return topicNMes;
    }

    public Light(BoundingObject boundingObject, String name, String topic) {
        super(boundingObject, name, topic);
        String[]excuWords = {"turn","on","off"};
        String[]gestures={"upSwap"};
        addExecuableWord(excuWords);
        supportedGestures(gestures);
    }


}
