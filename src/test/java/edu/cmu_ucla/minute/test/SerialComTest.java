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
        Light light = new Light(new BoundingSphere(new Vector3D(3300,2890,1500),1000),"ikea lamp","cmnd/sonoff1/POWER");
        MusicPlayer musicPlayer = new MusicPlayer(new BoundingSphere(new Vector3D(0,3650,1000),1000),"musicPlayer","cmnd/MusicPlayer");
//        Roomba roomba = new Roomba((new BoundingSphere(new Vector3D(1240,1680,0),1000)),"Roomba","roomba");
        Projector projector = new Projector((new BoundingSphere(new Vector3D(0,3650,2100),1000)),"projector","cmnd/Projector");
        world.addUser(user);
        world.addUser(user1);
        world.addUser(user2);
        world.addObject(light);
        world.addObject(musicPlayer);
//        world.addObject(roomba);
        world.addObject(projector);
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
