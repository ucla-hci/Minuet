package edu.cmu_ucla.minute.test;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.junit.Before;
import org.junit.Test;

public class VitalObjectTest {
//    VitalObject testObject = new VitalObject();
    @Before
    public void setUp() throws Exception {
            Vector3D vector3D = new Vector3D(1,2,-3);

        System.out.println("rotation : x: "+vector3D.getX()+" y: "+vector3D.getY()+" z: "+vector3D.getZ() );
//        testObject.setDown(new Vector3D(-2.81781,5.61918,0));
//        testObject.setTopCenter(new Vector3D(-2.81781,5.61918,7.83101));
//        testObject.setTopLeft(new Vector3D(0.639581,5.61918,7.83101));
//        testObject.setTopRight(new Vector3D(-2.81781,-9.79399,7.83101));
//        testObject.setName("testObject1");

    }


    @Test
    public void build() {

//        testObject.build();
//        System.out.println(testObject.getCenter());

    }
}