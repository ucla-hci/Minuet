package edu.cmu_ucla.minuet.model;

public class ObjectCommandPacket {
    private String topic;
    private String command;

    public ObjectCommandPacket(String topic, String command) {
        this.topic = topic;
        this.command = command;
    }

    public String getTopic() {
        return topic;
    }

    public String getCommand() {
        return command;
    }
}
