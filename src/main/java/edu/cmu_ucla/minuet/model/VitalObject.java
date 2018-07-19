package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.*;

public abstract class VitalObject {
    private String name;
    private String topic;
    private BoundingObject boundingObject;
    private Set<String> keyWords = new HashSet<>();
    private Set<String> execuableWords = new HashSet<>();
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

    public void addExecuableWord(String[] s) {
        this.execuableWords.addAll(Arrays.asList(s));
    }

    public boolean canExecuCommand(Set<String> command) {
        if (!command.isEmpty() && execuableWords.containsAll(command)) {

            return true;
        }
        return false;
    }

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
        return boundingObject.calculate(target, pointingVec);
    }

    public BoundingObject getBoundingObject() {
        return boundingObject;
    }
    public abstract String[] execuate(Set<String> command);
    public abstract String[] execuate(Set<String> command,String gesture);
    public abstract String[] execuate(String gesture);
}
