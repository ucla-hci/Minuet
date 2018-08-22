package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.NLP.TokenNode;

import java.util.Set;

public class MusicPlayer extends VitalObject {
    @Override
    public String[] execuate(TokenNode userCommand) {
        String[] topicNMes = new String[2];
//        if (command.contains("next")) {
//            topicNMes[0] = getTopic();
//            topicNMes[1] = "2";
//        } else if (command.contains("start")) {
//            topicNMes[0] = getTopic();
//            topicNMes[1] = "0";
//        } else if (command.contains("stop")) {
//            topicNMes[0] = getTopic();
//            topicNMes[1] = "1";
//        }
        return topicNMes;
    }

    @Override
    public String[] execuate(Set<String> command, String gesture) {
        String[] topicNMes = new String[2];
//        if (command.contains("volume") && gesture.equals("circleCW")) {
//            topicNMes[0] = getTopic();
//            topicNMes[1] = "3";
//        } else if (command.contains("volume") && gesture.equals("circleCCW")) {
//            topicNMes[0] = getTopic();
//            topicNMes[1] = "4";
//        }
        return  new String[0];
    }

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
        }
        return topicNMes;

    }

    public MusicPlayer(BoundingObject boundingObject, String name, String topic) {
        super(boundingObject, name, topic);
        String[] excuWords = {"next", "start", "stop","music","the","song"};
        String[] gestures = {"rightSwap", "upSwap", "downSwap"};
//        addExecuableWord(excuWords);
        supportedGestures(gestures);
    }
}
