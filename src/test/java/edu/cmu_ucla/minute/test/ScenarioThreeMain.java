package edu.cmu_ucla.minute.test;

import edu.cmu_ucla.minuet.model.*;
import edu.cmu_ucla.minuet.mqtt.ScenarioThreeMqtt;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

public class ScenarioThreeMain {

    @Before
    public void setUp() throws Exception {

        final VitalWorldScenarioThree world = new VitalWorldScenarioThree();
        User user1 = new User("Richard",0,0,0,new Vector3D(0,0,0));
        User user = new User("Tom",0,0,0,new Vector3D(0,0,0));
//        User user2 = new User("Mike",0,0,0,new Vector3D(0,0,0));
//        Projector projector = new Projector((new BoundingSphere(new Vector3D(-450,3880,2140),1000)),"projector","cmnd/Projector");
//        MusicPlayer musicPlayer = new MusicPlayer(new BoundingSphere(new Vector3D(4030,6240,1000),1000),"musicPlayer","cmnd/MusicPlayer");
        world.addUser(user);
        world.addUser(user1);
//        world.addUser(user2);
//        world.addObject(projector);
//        world.addObject(musicPlayer);
        ScenarioThreeMqtt systemSubscriber = new ScenarioThreeMqtt(world);

    }

    @Test
    public void build() {
        while(true){}
    }


}




