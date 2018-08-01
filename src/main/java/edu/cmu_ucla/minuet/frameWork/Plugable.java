package edu.cmu_ucla.minuet.frameWork;

import edu.cmu_ucla.minuet.model.VitalObject;

public interface Plugable {
     int curStage = 0;
    void setTargetObject(VitalObject targetObject);
    void getVoiceCommand(String voice);
}
