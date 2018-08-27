package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.NLP.NLPHandler;
import edu.cmu_ucla.minuet.NLP.TokenNode;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import edu.stanford.nlp.util.ArraySet;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CommandFrame {
    private boolean isExecAble = false;
    private TokenNode curCommand;
    private String curGesture = "";

    private final VitalWorld world;
    private int execuType = 0;
    private volatile boolean isDead = false;
    private String userName;
    private LocData secLoc = null;
    private ObjectBox box;
    private ScheduledExecutorService scheduledExecutorService;


    public String getUserName() {
        return userName;
    }

    public void setSecLoc(LocData secLoc) {
        this.secLoc = secLoc;
    }

    public CommandFrame(ObjectBox box, VitalWorld world, String userName) {
        this.box = box;
        this.userName = userName;
        this.world = world;
        this.scheduledExecutorService = Executors.newScheduledThreadPool(5);
        this.world.sendMqtt(box.getCurObject().enteringObject());
        if (!box.isOne())  {
            Thread underSelectedThread = new Thread(() -> {
                while (!isDead) {

                        world.sendMqtt(box.getCurObject().selectedObject());


                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            underSelectedThread.start();
            runTimeOut(15);
        }
        else{
            runTimeOut(8);
        }

    }

    public void runTimeOut(int second) {

        final Runnable cancellation = new Runnable() {
            @Override
            public void run() {
                synchronized (this) {

                    if (!isDead && !isExecAble) {
                        System.out.println("time out ");
                        world.killCurFrame();
                    }
                }
            }
        };
        scheduledExecutorService.schedule(cancellation, second, TimeUnit.SECONDS);
    }

    public void kill() {
        this.isDead = true;
    }

    private void checkExcuable() {

        if (box.getCurObject().canExecuCommand(curCommand)) {
            isExecAble = true;
            execuType = 1;
        } else if (box.getCurObject().canExcuCommandWithGesture(new ArraySet<>(), curGesture)) {
            isExecAble = true;
            execuType = 2;
        } else if (box.getCurObject().canExcuGesture(curGesture)) {
            isExecAble = true;
            execuType = 3;
        }
        if (isExecAble) {
//            kill();
            world.execuFrame(execuType);


        }
//        System.out.println(execuType);

    }


    public void setCurCommand(String text) {
        Set<String> mySet = new HashSet<String>(Arrays.asList(text.split("\\s+")));
        for (String s : box.getCurObject().rootSet) {
            if (mySet.contains(s)) {
                try {
                    world.sendVoiceCommand("OK, working on it");
                    box.checkLeftRight(text,world.getUserMap().get(userName).getLoc());
                    TokenNode command = NLPHandler.parse(text);
                    if (box.getCurObject().canExecuCommand(command)) {
                        this.curCommand = command;
                        checkExcuable();
                    }
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public void setCurGesture(String curGesture) {
        if (!box.isOne()) {
            if (curGesture.equals("rightSwap")) {
                world.sendMqtt(box.getCurObject().leavingObject());
                world.sendMqtt(box.getCurObject().resumeObject());
                box.setNext();
                world.sendMqtt(box.getCurObject().enteringObject());
            } else if (curGesture.equals("leftSwap")) {
                world.sendMqtt(box.getCurObject().leavingObject());
                world.sendMqtt(box.getCurObject().resumeObject());
                box.setPrevious();
                world.sendMqtt(box.getCurObject().enteringObject());
            } else if (box.getCurObject().canExcuGesture(curGesture)) {
                this.curGesture = curGesture;
//                world.sendMqtt(box.getCurObject().leavingObject());
                checkExcuable();
            }
        } else {
            if (box.getCurObject().canExcuGesture(curGesture)) {
                this.curGesture = curGesture;

                checkExcuable();
            }
        }

    }


    public TokenNode getCurCommand() {
        return curCommand;
    }

    public String getCurGesture() {
        return curGesture;
    }

    public void execuate(MQTT mqtt) {
        String[] retirmData = new String[2];
        switch (execuType) {
            case 1:
                retirmData = box.getCurObject().execuate(curCommand);
                break;
            case 2:
                //discard
                retirmData = box.getCurObject().execuate(new ArraySet<>(), curGesture);
                break;
            case 3:
                retirmData = box.getCurObject().execuate(curGesture);
                break;
        }
        if (retirmData.length == 2) {
            try {

                if (Roomba.class.isInstance(box.getCurObject()) && retirmData[1].equals("g") && secLoc != null) {

                    retirmData[1] = retirmData[1] + " " + (int) secLoc.getPos().getX() + " " + (int) secLoc.getPos().getY();
                    System.out.println(retirmData[0] + " " + retirmData[1]);
                }
                if (Projector.class.isInstance(box.getCurObject()) && retirmData[1].equals("show")) {

                    retirmData[1] = retirmData[1] + " " + userName;

                }
                mqtt.sendMessage(box.getCurObject().leavingObject()[0],box.getCurObject().leavingObject()[1]);
                mqtt.sendMessage(retirmData[0], retirmData[1]);
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
    }
}

