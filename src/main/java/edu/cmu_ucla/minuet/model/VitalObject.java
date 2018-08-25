package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.NLP.NLPHandler;
import edu.cmu_ucla.minuet.NLP.TokenNode;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.*;

public abstract class VitalObject {
    private String name;
    private String topic;
    private BoundingObject boundingObject;
    private Set<String> keyWords = new HashSet<>();

    protected Set<TokenNode> execuableWords = new HashSet<>();

    public Set<String> getRootSet() {
        return rootSet;
    }

    protected Set<String> rootSet = new HashSet<>();
    private Set<String> supportedGestures = new HashSet<>();
    private Map<String, String> keywordNGesture = new HashMap<>();


    public VitalObject(BoundingObject boundingObject, String name, String topic) {
        this.boundingObject = boundingObject;
        this.name = name;
        this.topic = topic;
    }

    public void addKeyWord(String[] s) {
        this.keyWords.addAll(Arrays.asList(s));
    }
    public void supportedGestures(String[] s) {
        this.supportedGestures.addAll(Arrays.asList(s));
    }

    public void addExecuableWord(String[] s,String command) {
        TokenNode root = new TokenNode(s[0]);
        rootSet.add(root.getText());
        root.setCommand(command);
        if (s.length>=2) {
            TokenNode son = new TokenNode(s[1]);
            root.addSon(son,1);
            if(s.length >= 3){
                son.addSon(new TokenNode(s[2]),1);
            }

        }

        this.execuableWords.add(root);
    }
    public abstract String[] enteringObject();
    public abstract String[] leavingObject();
    public abstract String[] resumeObject();
    public abstract String[] selectedObject();
    public void addExecuableWords(String[][]s,String command){
        for(int i = 0; i<s.length;i++){
            addExecuableWord(s[i],command);
        }
    }

    public boolean canExecuCommand(TokenNode userCommand) {
        for(TokenNode command : this.execuableWords){
            if(NLPHandler.isExecutable(command,userCommand))return true;
        }
        return false;
    }
    //discard
    public boolean canExcuCommandWithGesture(Set<String> command, String gesture) {
        if (!command.isEmpty() && !gesture.equals("")) {
            for (String s : command) {
                if (keywordNGesture.containsKey(s) && keywordNGesture.get(s).equals(gesture)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canExcuGesture(String gesture){
        if(supportedGestures.contains(gesture)){
            return true;
        }
        return false;
    }

    public boolean hasCommand(Set<String> command) {
        return keyWords.containsAll(command);
    }



    public String getTopic() {
        return topic;
    }

    public String getName() {
        return name;
    }

    public boolean checkBePointed(Vector3D target, Vector3D pointingVec) {
        System.out.println(this.name+": ");
        return boundingObject.calculate(target, pointingVec);
    }

    public BoundingObject getBoundingObject() {
        return boundingObject;
    }
    public  String[] execuate(TokenNode userCommand){
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
    public abstract String[] execuate(Set<String> command,String gesture);
    public abstract String[] execuate(String gesture);
//    public void toggleTmpSelected(){
//        this.isSelected = !isSelected;
//    };
}
