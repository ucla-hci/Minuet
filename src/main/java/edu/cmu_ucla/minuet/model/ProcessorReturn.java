package edu.cmu_ucla.minuet.model;

public class ProcessorReturn {
    private String topic;
    private int a;

    public ProcessorReturn(String topic, int a) {
        this.topic = topic;
        this.a = a;
    }

    public String getTopic() {
        return topic;
    }

    public int getA() {
        return a;
    }
}


