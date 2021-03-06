package edu.cmu_ucla.minuet.model;

import java.util.Set;

public class MusicPlayer extends VitalObject {
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
//        return topicNMes;
//    }

    @Override
    public String[] selectedObject() {
        return new String[]{"  ",""};
    }

    @Override
    public String[] resumeObject() {
        return new String[]{"  ",""};
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
    public String[] execuate(Set<String> command, String gesture) { return new String[]{"",""}; }

    @Override
    public String[] execuate(String gesture) {
        String[] topicNMes = new String[2];
        if (gesture.equals("rightSwap")) {
            topicNMes[0] = getTopic();
            topicNMes[1] = "2";
        } else if (gesture.equals("upSwap")) {
            topicNMes[0] = getTopic();
            topicNMes[1] = "0";
        } else if (gesture.equals("downSwap")) {
            topicNMes[0] = getTopic();
            topicNMes[1] = "1";
        }else if (gesture.equals("circleCW")) {
            topicNMes[0] = getTopic();
            topicNMes[1] = "3";
        }else if (gesture.equals("circleCCW")) {
            topicNMes[0] = getTopic();
            topicNMes[1] = "4";
        }
        return topicNMes;

    }

    public MusicPlayer(BoundingObject boundingObject, String name, String topic) {
        super(boundingObject, name, topic);
        String[][] play={{"play","music"},{"start"},{"music"},{"play","playlist"}};
        String[][] stop={{"stop"},{"pause"}};
        String[][] next={{"music","next"},{"song","next"},{"change"},{"play","music","different"},{"play","music","next"},{"play","song","different"}};
        String[][] volumeUp ={{"volume","up"},{"loud"},{"turn","up"},{"louder"}};
        String[][] volumeDown ={{"volume","down"},{"quiet"},{"turn","down"}};
        //only for continous scenario
//        String[][] volume = {{"volume"}};
//        String[] excuWords = {"next", "start", "stop","music","the","song"};
        String[] gestures = {"rightSwap", "upSwap", "downSwap","circleCW","circleCCW"};
        addExecuableWords(play,"0");
        addExecuableWords(stop,"1");
        addExecuableWords(next,"2");
        addExecuableWords(volumeDown,"4");
        addExecuableWords(volumeUp,"3");
        //only for continous scenario
//        addExecuableWords(volume,"6");
        supportedGestures(gestures);
    }
}
