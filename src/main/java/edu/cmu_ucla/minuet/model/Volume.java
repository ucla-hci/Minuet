package edu.cmu_ucla.minuet.model;

public class Volume implements VitalObjectCommand {
    private final static String COMMAND_NAME = "VOLUME";
    @Override
    public String execute(double a) {
        return Double.toString(a);
    }

    @Override
    public String getCommandTopic() {
        return COMMAND_NAME;
    }
}
