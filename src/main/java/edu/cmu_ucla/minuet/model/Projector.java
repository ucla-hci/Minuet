package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.NLP.TokenNode;

import java.util.Set;

public class Projector  extends VitalObject  {
    @Override
    public String[] execuate(TokenNode node) {
        return new String[0];
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
            topicNMes[1]="show";
        }else if(gesture.equals("downSwap")){
            topicNMes[0] = getTopic();
            topicNMes[1]="quit";
        }else if(gesture.equals("leftSwap")){
            topicNMes[0] = getTopic();
            topicNMes[1]="previous";
        }else if(gesture.equals("rightSwap")){
            topicNMes[0] = getTopic();
            topicNMes[1]="next";
        }
//        else if(gesture.equals("circleCW")){
//            topicNMes[0] = getTopic();
//            topicNMes[1]="draw";
//        }else if(gesture.equals("circleCW")){
//            topicNMes[0] = getTopic();
//            topicNMes[1]="undraw";
//        }
        return topicNMes;
    }

    public Projector(BoundingObject boundingObject, String name, String topic) {
        super(boundingObject, name, topic);
        String[]excuWords = {};
        String[]gestures={"upSwap","downSwap","leftSwap","rightSwap"};
//        addExecuableWord(excuWords);
        supportedGestures(gestures);
    }
}
