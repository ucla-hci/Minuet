package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class User {

    private double pitch;
    private double roll;
    private double yaw;



    private Vector3D pointVec;

    private String name;
    private Vector3D pos;
    private Vector3D loc= null;

    public User(String name, double pitch, double roll, double yaw, Vector3D pos) {
        this.loc = pos;
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
        this.pos = pos;
        this.name = name;
        double x = -Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw));
        double y = -Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw));
        double z = Math.sin(Math.toRadians(pitch));
        this.pointVec = new Vector3D(x,y,z);
    }
    public Vector3D getPointVec() {
        return pointVec;
    }
    public void updateLoc(Vector3D loc){
        this.loc = loc;
    }

    public Vector3D getLoc() {
        return loc;
    }

    public void updataData(double pitch, double roll, double yaw, Vector3D pos){
        this.pitch = pitch;
        this.roll = roll;
        this.yaw = yaw;
        this.pos = pos;

        double x = -Math.cos(Math.toRadians(pitch))*Math.sin(Math.toRadians(yaw));
        double y = -Math.cos(Math.toRadians(pitch))*Math.cos(Math.toRadians(yaw));
        double z = Math.sin(Math.toRadians(pitch));
        this.pointVec = new Vector3D(x,y,z);
    }
    public String getName() {

        return name;
    }

    public Vector3D getPos() {
        return pos;
    }

}
