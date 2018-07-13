package edu.cmu_ucla.minute.test;

import edu.cmu_ucla.minuet.model.User;
import edu.cmu_ucla.minuet.model.VitalWorld;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

public class VitalWorldTest {


    @Before
    public void setUp() throws Exception {
        VitalWorld vitalWorld = new VitalWorld();
        User user = new User("testUser",0,0,0,new Vector3D(0,0,0));
//        VitalObject object = new VitalObject(new BoundingSphere(new Vector3D(3300,2890,1700),1000),"sonoffSwitch","cmnd/sonoff1/POWER");
//        vitalWorld.addUser(user);
//        vitalWorld.addObject(object);
// User user = new User("user1",-2.912,0,-93.281,new Vector3D(15,58,0));

//        user.setYaw(-93.281);
//        user.setRoll(0);
//        user.setPitch(-2.912);
//        user.setX(15);
//        user.setY(58);
//        user.setZ(0);
//
//        VitalObject testObj = new VitalObject(new BoundingSphere());
//        testObj.setName("test");
//        testObj.setTopRight(new Vector3D(15,-16,8));
//        testObj.setTopCenter(new Vector3D(6,-16,8));
//        testObj.setTopLeft(new Vector3D(6,-25,8));
//        testObj.setDown(new Vector3D(6,-16,0));
//        testObj.build();
//        vitalWorld.addObject(testObj);
//        vitalWorld.addUser(user);
    }

    @Test
    public void checkInteraction() {
        while(true){

        }
//        vitalWorld.checkInteraction();
    }
}