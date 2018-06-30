package edu.cmu_ucla.minuet.util;

import edu.cmu_ucla.minuet.model.LocData;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Util {
    private final static double STAND_Z = 1550;
    static public LocData parseLocString(String s){
        ////change it later :
        s+=" testUser";


//        System.out.println("received:"+ s);
        String[] splitedString = s.split("\\s+");
        LocData locData = null;

        if (splitedString.length == 7){
            double yaw = (double)Math.floorMod((int)(Double.parseDouble(splitedString[0])-25),360);
            double pitch = Double.parseDouble(splitedString[1]);
            double roll = Double.parseDouble(splitedString[2]);
//            System.out.println("angle : yaw: "+yaw+" pitch: "+pitch+" roll: "+roll );
            Vector3D pos = new Vector3D(Double.parseDouble(splitedString[3]),Double.parseDouble(splitedString[4]),STAND_Z);
            String userName = splitedString[6];
            locData = new LocData(yaw,pitch,roll,pos);

        }
        else{
//            System.out.println("data corrupted");

        }
        return locData;
    }

}
