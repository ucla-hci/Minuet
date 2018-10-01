package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class LocationParser {
    private final static double STAND_Z = 1400.0;
    private final static double SIT_Z = 1000.0;
    private final static double L = 350.0;
    public static Vector3D parseLocation(String[] splitedString){
        double yaw = (Double.parseDouble(splitedString[3]) - 30);
        yaw = (yaw >= 0) ? yaw : 360 + yaw;
        double pitch = Double.parseDouble(splitedString[4]);
        double x = Double.parseDouble(splitedString[0]) + L * Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw));
        double y = Double.parseDouble(splitedString[1]) + L * Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw));
        double proxyZ = Double.parseDouble(splitedString[2]) - Math.sin(Math.toRadians(pitch)) * L;
        double z = (Math.abs(proxyZ - STAND_Z) >= Math.abs(proxyZ - SIT_Z)) ? SIT_Z : STAND_Z;
        Vector3D loc = new Vector3D(x, y, z);
        return loc;
    }
    public static  double calculatePoinging(Vector3D pointerLoc, Vector3D pointingVec, Vector3D targetLoc){
        Vector3D oc = targetLoc.subtract(pointerLoc);
        double projectoc = oc.dotProduct(pointingVec);

        double oc2 = oc.dotProduct(oc);
        System.out.println("angle is : "+Math.toDegrees(Vector3D.angle(oc,pointingVec)));

       return Math.toDegrees(Vector3D.angle(oc,pointingVec));
    }
}
