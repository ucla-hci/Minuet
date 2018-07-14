package edu.cmu_ucla.minuet.model;

import java.util.Set;

public class MusicPlayer extends VitalObject {
    @Override
    public String[] execuate(Set<String> command) {
        String[] topicNMes = new String[2];
        if(command.contains("next")){
            topicNMes[0]=getTopic();
            topicNMes[1]="next";
        }
        else if (command.contains("previous")){
            topicNMes[0]=getTopic();
            topicNMes[1]="previous";
        }
        else if (command.contains("start")){
            topicNMes[0]=getTopic();
            topicNMes[1]="start";
        }
        else if (command.contains("pause")){
            topicNMes[0]=getTopic();
            topicNMes[1]="pause";
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
        if(gesture.equals("leftSwap")){
            topicNMes[0] = getTopic();
            topicNMes[1]="previous";
        }
        else if(gesture.equals("rightSwap")){
            topicNMes[0] = getTopic();
            topicNMes[1]="next";
        }
        else if(gesture.equals("upSwap")){
            topicNMes[0] = getTopic();
            topicNMes[1]="start";
        }
        else if(gesture.equals("downSwap")){
            topicNMes[0] = getTopic();
            topicNMes[1]="pause";
        }
        return topicNMes;

    }

    public MusicPlayer(BoundingObject boundingObject, String name, String topic) {
        super(boundingObject, name, topic);
        String[]excuWords = {"next","start","pause","previous"};
        String[]gestures={"leftSwap","rightSwap","upSwap","downSwap"};
        addExecuableWord(excuWords);
        supportedGestures(gestures);
    }
}
