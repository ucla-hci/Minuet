package edu.cmu_ucla.minuet.model;

import java.util.Set;

public class LifeLess extends VitalObject {
    public LifeLess(BoundingObject boundingObject, String name, String topic) {
        super(boundingObject, name, topic);
        String[][] set={{"set"},{"wake","up"}};
        String[][] stop={{"stop"},{"pause"}};
        String[]gestures={"upSwap","downSwap"};
        for(int i=0;i<set.length;i++){
            addExecuableWord(set[i],"set");
        }
        for(int j=0;j<stop.length;j++){
            addExecuableWord(stop[j],"stop");
        }

        supportedGestures(gestures);
    }

    @Override
    public String[] enteringObject() {
        return new String[]{"  ",""};
    }

    @Override
    public String[] leavingObject() {
        return new String[]{"  ",""};
    }

    @Override
    public String[] resumeObject() {
        return new String[]{"  ",""};
    }

    @Override
    public String[] selectedObject() {
        return new String[]{"  ",""};
    }

    @Override
    public String[] execuate(Set<String> command, String gesture) {
        return new String[]{"  ",""};
    }

    @Override
    public String[] execuate(String gesture) {
        String[] topicNMes = new String[2];
        if(gesture.equals("upSwap")){

            topicNMes[0] = getTopic();
            topicNMes[1]="run";

        }
        if(gesture.equals("downSwap")){

            topicNMes[0] = getTopic();
            topicNMes[1]="stop";

        }

        return topicNMes;
    }
}
