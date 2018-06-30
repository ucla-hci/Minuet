package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class User {
    static final int NOR_X = 0;
    static final int NOR_Y = -1;
    static final int NOR_Z = 0;

    private double pitch;
    private double roll;
    private double yaw;



    private Vector3D pointVec;
    private Rotation rotation;

    private String name;
    private Vector3D pos;


    public Rotation getRotation() {
        return rotation;
    }

    public User(String name, double pitch, double roll, double yaw, Vector3D pos) {
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
        this.pos = pos;
        this.name = name;
        double x = Math.sin(Math.toRadians(360-yaw))*Math.cos(Math.toRadians(pitch));
        double y = -Math.cos(Math.toRadians(360-yaw))*Math.cos(Math.toRadians(pitch));
        double z = Math.sin(Math.toRadians(pitch));
        this.pointVec = new Vector3D(x,y,z);
//        this.rotation = new Rotation(RotationOrder.ZYX,yaw,pitch,roll);
    }
    public Vector3D getPointVec() {
        return pointVec;
    }

    public void updataData(double pitch, double roll, double yaw, Vector3D pos){
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
        this.pos = pos;
        double x = Math.sin(Math.toRadians(360-yaw))*Math.cos(Math.toRadians(pitch));
        double y = -Math.cos(Math.toRadians(360-yaw))*Math.cos(Math.toRadians(pitch));
        double z = Math.sin(Math.toRadians(pitch));
        this.pointVec = new Vector3D(x,y,z);
//        this.pointVec = new Vector3D(Math.sin(360-yaw)*Math.cos(pitch),-Math.cos(360-yaw)*Math.cos(pitch), Math.sin(pitch));
//        this.rotation = new Rotation(RotationOrder.XYZ,roll,pitch,yaw);
    }
    public String getName() {

        return name;
    }

    public Vector3D getPos() {
        return pos;
    }




    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public double getYaw() {
        return yaw;
    }
}
