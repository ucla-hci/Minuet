package edu.cmu_ucla.minuet.model;

import java.util.Set;

public class Roomba extends VitalObject {
    public Roomba(BoundingObject boundingObject, String name, String topic) {
        super(boundingObject, name, topic);
        String[]excuWords = {"clean","that","area","here","this","there","stop","go","home"};
        String[]gestures={"circleCCW","upSwap"};
        addExecuableWord(excuWords);
        supportedGestures(gestures);

    }

    @Override
    public String[] execuate(Set<String> command) {
        String[] topicNMes = new String[2];
        if(command.contains("clean")||   (command.contains("go")&&(command.contains("here")))    ){
            topicNMes[0]=getTopic();
            topicNMes[1]="g";
        }
        else if (command.contains("stop")){
            topicNMes[0]=getTopic();
            topicNMes[1]="s";
        }
        else if (command.contains("go")&&command.contains("home")){
            topicNMes[0]=getTopic();
            topicNMes[1]="d";
        }

        return topicNMes;
    }

    @Override
    public String[] execuate(Set<String> command, String gesture) {
        String[] topicNMes = new String[2];
        if(gesture.equals("circleCCW")){
            topicNMes[0] = getTopic();
            topicNMes[1]="s";
        }
        else if(gesture.equals("upSwap")){
            topicNMes[0] = getTopic();
            topicNMes[1]="r";
        }
        return topicNMes;
    }

    @Override
    public String[] execuate(String gesture) {
        return new String[0];
    }
}
