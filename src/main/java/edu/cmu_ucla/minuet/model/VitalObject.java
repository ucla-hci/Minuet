package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class VitalObject {
    private String name;
    private String topic;
    private BoundingObject boundingObject;



    public VitalObject(BoundingObject boundingObject, String name, String topic){
        this.boundingObject = boundingObject;
        this.name = name;

        this.topic = topic;

    }

    public String getTopic() {return topic;}
    public String getName() {
        return name;
    }

    public boolean checkBePointed(Vector3D target, Vector3D pointingVec){
        return boundingObject.calculate(target,pointingVec);
    }
    public BoundingObject getBoundingObject() {
        return boundingObject;
    }

}
