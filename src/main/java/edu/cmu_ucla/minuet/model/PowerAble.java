package edu.cmu_ucla.minuet.model;

public class PowerAble implements VitalObjectCommand {
    private final static String COMMAND_NAME = "POWER";

    @Override
    public String execute(double a) {
       if ((int)a == 0){
           return "OFF";
       }
       else if ((int)a == 1){
           return "ON";
       }
       else {return "error";}
    }

    @Override
    public String getCommandTopic() {
        return COMMAND_NAME;
    }
}
