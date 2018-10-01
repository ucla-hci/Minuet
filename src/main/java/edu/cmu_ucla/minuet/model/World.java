package edu.cmu_ucla.minuet.model;

public interface World {
     void addObject(VitalObject s);
     void addUser(User user);
    void updateUserLoc(String[] splitedString);
    void revceiveData(String data);

    void sendVoiceCommand(String command);
    void sendMqtt(String[] command);
    void execuFrame(int execuType);
}
