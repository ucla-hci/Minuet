package edu.cmu_ucla.minuet.model;

import java.util.Set;

public class Projector  extends VitalObject  {
//    @Override
//    public String[] execuate(TokenNode node) {
//        return new String[0];
//    }

    @Override
    public String[] execuate(Set<String> command, String gesture) {
        return new String[]{"  ",""};
    }

    @Override
    public String[] resumeObject() { return new String[]{"  ",""}; }

    @Override
    public String[] enteringObject() {
        return new String[]{"  ",""};
    }

    @Override
    public String[] leavingObject() {
        return new String[]{"  ",""};
    }

    @Override
    public String[] selectedObject() {
        return new String[]{"  ",""};
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
        String[][]show={{"show"},{"display"},{"open"}};
        String[][]close={{"turn","off"},{"off"},{"close"}};
        String[][]next={{"next"},{"slide","next"}};
        String[][]previous={{"previous"},{"slide","last"},{"slide","previous"}};
        addExecuableWords(show,"show");
        addExecuableWords(close,"quit");
        addExecuableWords(next,"next");
        addExecuableWords(previous,"previous");
        String[]gestures={"upSwap","downSwap","leftSwap","rightSwap"};
//        addExecuableWord(excuWords);
        supportedGestures(gestures);
    }
}
