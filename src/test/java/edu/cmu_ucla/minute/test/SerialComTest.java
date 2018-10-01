package edu.cmu_ucla.minute.test;

import edu.cmu_ucla.minuet.model.*;
import edu.cmu_ucla.minuet.mqtt.SystemSubscriber;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

public class SerialComTest {


    @Before
    public void setUp() throws Exception {

        final VitalWorld world = new VitalWorld();
        User user1 = new User("Richard",0,0,0,new Vector3D(0,0,0));
        User user = new User("Tom",0,0,0,new Vector3D(0,0,0));
        User user2 = new User("Mike",0,0,0,new Vector3D(0,0,0));
//        VitalObject object = new VitalObject(new BoundingSphere(new Vector3D(3300,2890,1900),1000),"sonoffSwitch","cmnd/sonoff1/POWER");
//        VitalObject object2 = new VitalObject(new BoundingSphere(new Vector3D(1900,2700,1300),1000),"tv","cmnd/sonoff3/POWER");
//        VitalObject object3 = new VitalObject(new BoundingSphere(new Vector3D(0,3650,1000),1000),"musicPlayer","cmnd/musicPlayer");
<<<<<<< HEAD
        Light light = new Light(new BoundingSphere(new Vector3D(5640,3080,1500),1000),"ikea lamp","cmnd/sonoff1/POWER");
        MusicPlayer musicPlayer = new MusicPlayer(new BoundingSphere(new Vector3D(0,3650,1000),1000),"musicPlayer","cmnd/MusicPlayer");
//        Roomba roomba = new Roomba((new BoundingSphere(new Vector3D(1240,1680,0),1000)),"Roomba","roomba");
//        Projector projector = new Projector((new BoundingSphere(new Vector3D(0,3650,2100),1000)),"projector","cmnd/Projector");
        world.addUser(user);
        world.addUser(user1);
        world.addUser(user2);
        world.addObject(light);
        world.addObject(musicPlayer);
//        world.addObject(roomba);
//        world.addObject(projector);
=======
        Light light = new Light(new BoundingSphere(new Vector3D(5080,0,2390),1000),"wall light","cmnd/sonoff1/POWER");
//        Light light = new Light(new BoundingSphere(new Vector3D(5150,0,2650),1000),"wall light","cmnd/sonoff1/POWER");
        Light dight = new Light(new BoundingSphere(new Vector3D(2300,900,1400),1000),"dual-head lamp 1","cmnd/sonoffB1/POWER");
        Light dight2 = new Light(new BoundingSphere(new Vector3D(2400,900,1400),1000),"dual-head lamp 2","cmnd/sonoffB2/POWER");
        MusicPlayer musicPlayer = new MusicPlayer(new BoundingSphere(new Vector3D(4030,6240,1000),1000),"musicPlayer","cmnd/MusicPlayer");
//        MusicPlayer musicPlayer = new MusicPlayer(new BoundingSphere(new Vector3D(3850,6500,900),1000),"musicPlayer","cmnd/MusicPlayer");
//        Roomba roomba = new Roomba((new BoundingSphere(new Vector3D(5640,3080,0),1000)),"Roomba","roomba");
        Roomba roomba = new Roomba((new BoundingSphere(new Vector3D(4300,2800,0),1000)),"Roomba","roomba");
        Projector projector = new Projector((new BoundingSphere(new Vector3D(-450,3880,2140),1000)),"projector","cmnd/Projector");
//        Projector projector = new Projector((new BoundingSphere(new Vector3D(-250,3880,2750),1000)),"projector","cmnd/Projector");

        world.addUser(user);
        world.addUser(user1);
        world.addUser(user2);
//        world.addObject(light);
        world.addObject(dight);
        world.addObject(dight2);
//        world.addObject(musicPlayer);
        world.addObject(roomba);
        world.addObject(projector);
>>>>>>> study
//
//        Artifacts monaLisa = new Artifacts(new BoundingSphere(new Vector3D(5400,3650,1200),1000),"Mona_Lisa","museum");
//        Artifacts theBurthOfVenus = new Artifacts(new BoundingSphere(new Vector3D(5400,2000,1200),1000),"The_Birth_of_Venus","museum");
//        Artifacts theStarryNight = new Artifacts(new BoundingSphere(new Vector3D(3300,0,1200),1000),"The_Starry_Night","museum");
//        world.addArtifact(monaLisa);
//        world.addArtifact(theBurthOfVenus);
//        world.addArtifact(theStarryNight);
        SystemSubscriber systemSubscriber = new SystemSubscriber(world);

    }

    @Test
    public void build() {
        while(true){}
    }

}
