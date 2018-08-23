package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.NLP.NLPHandler;
import edu.cmu_ucla.minuet.NLP.TokenNode;

import java.util.Set;

public class Light extends VitalObject {
    @Override
    public String[] execuate(TokenNode userCommand) {
        String[] topicNMes = new String[2];
        for(TokenNode node : execuableWords){
            if(NLPHandler.isExecutable(node,userCommand)){
                topicNMes[0]=getTopic();
                topicNMes[1] = node.getCommand();
                return topicNMes;
            }
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

        String[][] turnOn={{"turn","on"},{"on"},{"light"}};
        String[][] turnOff={{"turn","off"},{"off"},{"dim"}};



//        String[]excuWords = {"turn","on","off"};
        String[]gestures={"upSwap"};
        for(int i=0;i<turnOn.length;i++){
            addExecuableWord(turnOn[i],"ON");
        }
        for(int j=0;j<turnOff.length;j++){
            addExecuableWord(turnOff[j],"OFF");
        }

//        addExecuableWord({"turn","on"});
        supportedGestures(gestures);
    }


}
