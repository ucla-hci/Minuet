package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.HashSet;
import java.util.Set;

public class VitalObject {
    private String name;
    private String topic;
    private BoundingObject boundingObject;
    private Set<VitalObjectCommand> commandList = new HashSet<>();


    public VitalObject(BoundingObject boundingObject, String name, String topic){
        this.boundingObject = boundingObject;
        this.name = name;
        this.topic = topic;

    }
    public void addCommand(VitalObjectCommand a){this.commandList.add(a);}
    /////CHECK!
    public ObjectCommandPacket checkNExeCommand(ProcessorReturn processorReturn){
        for(VitalObjectCommand objectChar : this.commandList){
            if(objectChar.getCommandTopic().equals(processorReturn.getTopic())){
                return new ObjectCommandPacket(this.topic+"/"+objectChar.getCommandTopic(), objectChar.execute(processorReturn.getA()));

            }
        }
         return null;
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
