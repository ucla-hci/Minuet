package edu.cmu_ucla.minute.test;

import edu.cmu_ucla.minuet.model.BoundingSphere;
import edu.cmu_ucla.minuet.model.VitalObject;
import edu.cmu_ucla.minuet.model.*;
import edu.cmu_ucla.minuet.mqtt.VoiceMQTTSub;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

public class SerialComTest {


    @Before
    public void setUp() throws Exception {

        VitalWorld world = new VitalWorld();
        User user = new User("testUser",0,0,0,new Vector3D(0,0,0));
        VitalObject object = new VitalObject(new BoundingSphere(new Vector3D(3300,2890,1700),1500),"sonoffSwitch","cmnd/sonoff1/POWER");
        VitalObject object2 = new VitalObject(new BoundingSphere(new Vector3D(2000,3000,1000),1500),"tv","cmnd/sonoff3/POWER");

        world.addUser(user);
        world.addObject(object);
        world.addObject(object2);
//        MQTTSubscriber subscriber = new MQTTSubscriber(world);
        VoiceMQTTSub sub = new VoiceMQTTSub(world);

//            edu.cmu_ucla.minuet.SerialComm.NewSerialCom serialCom = new edu.cmu_ucla.minuet.SerialComm.NewSerialCom(world);
    }

    @Test
    public void build() {
        while(true){}
    }

}
