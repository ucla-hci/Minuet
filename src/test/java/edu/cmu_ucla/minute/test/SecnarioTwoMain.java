package edu.cmu_ucla.minute.test;

import edu.cmu_ucla.minuet.model.*;
import edu.cmu_ucla.minuet.mqtt.ScenarioTwoMqtt;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

public class SecnarioTwoMain {

    @Before
    public void setUp() throws Exception {

        final VitalWorldScenarioTwo world = new VitalWorldScenarioTwo();
        User user1 = new User("Richard",0,0,0,new Vector3D(0,0,0));
        User user = new User("Tom",0,0,0,new Vector3D(0,0,0));
        User user2 = new User("Mike",0,0,0,new Vector3D(0,0,0));
//        Light light = new Light(new BoundingSphere(new Vector3D(2900,4850,400),1000),"wall light","cmnd/sonoff1/POWER");
//        Light dight2 = new Light(new BoundingSphere(new Vector3D(4450,5050,1000),1000),"dual-head lamp 2","cmnd/sonoffB2/POWER");
//        Light dight = new Light(new BoundingSphere(new Vector3D(5350,2880,1000),1000),"dual-head lamp 1","cmnd/sonoffB1/POWER");
        Projector projector = new Projector((new BoundingSphere(new Vector3D(-450,3880,2140),1000)),"projector","cmnd/Projector");
//        MusicPlayer musicPlayer = new MusicPlayer(new BoundingSphere(new Vector3D(5200,3800,1000),1000),"musicPlayer","cmnd/MusicPlayer");

        world.addUser(user);
        world.addUser(user1);
        world.addUser(user2);
        world.addObject(projector);
//        world.addObject(musicPlayer);
//        world.addObject(light);
//        world.addObject(dight);
//        world.addObject(dight2);
        ScenarioTwoMqtt systemSubscriber = new ScenarioTwoMqtt(world);

    }

    @Test
    public void build() {
        while(true){}
    }


}




