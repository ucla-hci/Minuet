package edu.cmu_ucla.minute.test;

import edu.cmu_ucla.minuet.model.*;
import edu.cmu_ucla.minuet.mqtt.MQTTSubscriber;
import edu.cmu_ucla.minuet.mqtt.SystemSubscriber;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

public class SerialComTest {


    @Before
    public void setUp() throws Exception {

        VitalWorld world = new VitalWorld();
        User user = new User("testUser",0,0,0,new Vector3D(0,0,0));
//        VitalObject object = new VitalObject(new BoundingSphere(new Vector3D(3300,2890,1900),1000),"sonoffSwitch","cmnd/sonoff1/POWER");
//        VitalObject object2 = new VitalObject(new BoundingSphere(new Vector3D(1900,2700,1300),1000),"tv","cmnd/sonoff3/POWER");
//        VitalObject object3 = new VitalObject(new BoundingSphere(new Vector3D(0,3650,1000),1000),"musicPlayer","cmnd/musicPlayer");
        Light light = new Light(new BoundingSphere(new Vector3D(3300,2890,1900),1000),"sonoffSwitch","cmnd/sonoff1/POWER");
        world.addUser(user);
        world.addObject(light);
//        world.addObject(object);
//        world.addObject(object2);
//        world.addObject(object3);
        MQTTSubscriber subscriber = new MQTTSubscriber(world);
        SystemSubscriber systemSubscriber = new SystemSubscriber(world);
//        VoiceMQTTSub sub = new VoiceMQTTSub(world);
//        Thread trigger = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                MqttNGUI mqttNGUI = new MqttNGUI("DEMO");
//            }
//        });
//        trigger.start();

//            edu.cmu_ucla.minuet.SerialComm.NewSerialCom serialCom = new edu.cmu_ucla.minuet.SerialComm.NewSerialCom(world);
    }

    @Test
    public void build() {
        while(true){}
    }

}
