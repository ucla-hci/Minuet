package edu.cmu_ucla.minute.test;

import edu.cmu_ucla.minuet.model.*;
import edu.cmu_ucla.minuet.mqtt.ScenarioOneMqtt;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

public class ScenarioOneMain {
    @Before
    public void setUp() throws Exception {

        final VitalWorldScenarioOne world = new VitalWorldScenarioOne();
        User user1 = new User("Richard",0,0,0,new Vector3D(0,0,0));
        User user = new User("Tom",0,0,0,new Vector3D(0,0,0));
        User user2 = new User("Mike",0,0,0,new Vector3D(0,0,0));
//        VitalObject object = new VitalObject(new BoundingSphere(new Vector3D(3300,2890,1900),1000),"sonoffSwitch","cmnd/sonoff1/POWER");
//        VitalObject object2 = new VitalObject(new BoundingSphere(new Vector3D(1900,2700,1300),1000),"tv","cmnd/sonoff3/POWER");
//        VitalObject object3 = new VitalObject(new BoundingSphere(new Vector3D(0,3650,1000),1000),"musicPlayer","cmnd/musicPlayer");


//        Light light = new Light(new BoundingSphere(new Vector3D(3500,6200,1300),1000),"wall light","cmnd/sonoff1/POWER");
//        Light dight2 = new Light(new BoundingSphere(new Vector3D(4900,3200,1000),1000),"dual-head lamp 2","cmnd/sonoffB2/POWER");
//        Light dight = new Light(new BoundingSphere(new Vector3D(4300,1600,1000),1000),"dual-head lamp 1","cmnd/sonoffB1/POWER");
//

        Light light = new Light(new BoundingSphere(new Vector3D(2900,4850,400),1000),"wall light","cmnd/sonoff1/POWER");
//        Light dight2 = new Light(new BoundingSphere(new Vector3D(4450,5050,1000),1000),"dual-head lamp 2","cmnd/sonoffB2/POWER");
//        Light dight = new Light(new BoundingSphere(new Vector3D(5350,2880,1000),1000),"dual-head lamp 1","cmnd/sonoffB1/POWER");

//        Light light = new Light(new BoundingSphere(new Vector3D(3500,6200,2390),1000),"wall light","cmnd/sonoff1/POWER");
//        Light dight = new Light(new BoundingSphere(new Vector3D(4050,8500,1000),1000),"dual-head lamp 1","cmnd/sonoffB1/POWER");
//        Light dight2 = new Light(new BoundingSphere(new Vector3D(4200,6200,1000),1000),"dual-head lamp 2","cmnd/sonoffB2/POWER");
//        MusicPlayer musicPlayer = new MusicPlayer(new BoundingSphere(new Vector3D(3700,6240,1000),1000),"musicPlayer","cmnd/MusicPlayer");
//
//
//
//
//
// MusicPlayer musicPlayer = new MusicPlayer(new BoundingSphere(new Vector3D(3850,6500,900),1000),"musicPlayer","cmnd/MusicPlayer");
//        Roomba roomba = new Roomba((new BoundingSphere(new Vector3D(5640,3080,0),1000)),"Roomba","roomba");
//        Roomba roomba = new Roomba((new BoundingSphere(new Vector3D(5450,3500,0),1000)),"Roomba","roomba");
//        Projector projector = new Projector((new BoundingSphere(new Vector3D(-450,3880,2140),1000)),"projector","cmnd/Projector");
//        Projector projector = new Projector((new BoundingSphere(new Vector3D(-250,3880,2750),1000)),"projector","cmnd/Projector");

        world.addUser(user);
        world.addUser(user1);
        world.addUser(user2);
        world.addObject(light);
//        world.addObject(dight);
//        world.addObject(dight2);
//        world.addObject(musicPlayer);
//        world.addObject(roomba);
//        world.addObject(projector);
//

        ScenarioOneMqtt systemSubscriber = new ScenarioOneMqtt(world);

    }

    @Test
    public void build() {
        while(true){}
    }
}
