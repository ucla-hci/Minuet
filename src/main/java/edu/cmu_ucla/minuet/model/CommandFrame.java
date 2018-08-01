package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommandFrame {
    private boolean isExecAble = false;
    private Set<String> curCommand = new HashSet<>();
    private String curGesture = "";
    private VitalObject curObject;
    private final VitalWorld world;
    private int execuType = 0;
    private boolean isDead = false;

    public String getUserName() {
        return userName;
    }

    private String userName;

    public void setSecLoc(LocData secLoc) {
        this.secLoc = secLoc;
    }

    private LocData secLoc = null;

    public CommandFrame(VitalObject object, VitalWorld world,String userName) {
        this.curObject = object;
        this.userName = userName;
        this.world = world;

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        final Runnable cancellation = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {

                    if (!isDead&&!isExecAble) {
                        System.out.println("time out ");
                        world.killCurFrame();
                    }
                }
            }
        };
        scheduledExecutorService.schedule(cancellation, 8, TimeUnit.SECONDS);

    }
    public void kill(){
        this.isDead = true;
    }
    private void checkExcuable() {

        if (curObject.canExecuCommand(curCommand)) {
            isExecAble = true;
            execuType = 1;
        } else if (curObject.canExcuCommandWithGesture(curCommand, curGesture)) {
            isExecAble = true;
            execuType = 2;
        } else if (curObject.canExcuGesture(curGesture)) {
            isExecAble = true;
            execuType = 3;
        }
        if (isExecAble) {
            world.execuFrame();

        }
        System.out.println(execuType);

    }


    public void setCurCommand(Set<String> curCommand) {

        if (curObject.hasCommand(curCommand) || curObject.canExecuCommand(curCommand)) {
            System.out.println("set command entered");
            this.curCommand = curCommand;
            System.out.println("set command 1");


            checkExcuable();
            System.out.println("set command finished");
        }


    }

    public void setCurGesture(String curGesture) {
        if (curObject.canExcuGesture(curGesture)) {
            this.curGesture = curGesture;
            checkExcuable();
        }
    }

    public boolean isExecAble() {

        return isExecAble;
    }

    public Set<String> getCurCommand() {
        return curCommand;
    }

    public String getCurGesture() {
        return curGesture;
    }

    public void execuate(MQTT mqtt) {
        String[] retirmData = new String[2];
        switch (execuType) {
            case 1:
                retirmData = curObject.execuate(curCommand);
                break;
            case 2:
                retirmData = curObject.execuate(curCommand, curGesture);
                break;
            case 3:
                retirmData = curObject.execuate(curGesture);
                break;
        }
        if (retirmData.length == 2) {
            try {

                if(Roomba.class.isInstance(curObject)&&retirmData[1].equals("g")&&secLoc!=null){

                    retirmData[1]=retirmData[1]+" "+(int)secLoc.getPos().getX()+" "+(int)secLoc.getPos().getY();
                    System.out.println(retirmData[0]+" "+retirmData[1]);
                }

                mqtt.sendMessage(retirmData[0], retirmData[1]);
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
    }
}

