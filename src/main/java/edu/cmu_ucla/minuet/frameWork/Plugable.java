package edu.cmu_ucla.minuet.frameWork;

import edu.cmu_ucla.minuet.model.VitalObject;

public interface Plugable {

    void setTargetObject(VitalObject targetObject);
    VitalObject getTargetObject();
    void getVoiceCommand(String voice);
    void kill();
}
