package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class User {

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public double getYaw() {
        return yaw;
    }

    private double pitch;
    private double roll;
    private double yaw;

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    private Zone zone;

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
        if(zone!=null)zone.setCentroid(loc);
    }
    public void updateDir(double pitch, double yaw, double roll){
        this.pitch = pitch;
        this.yaw= yaw;
        this.roll = roll;
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


    public boolean checkBePointed(Vector3D target, Vector3D pointingVec) {
        Vector3D oc = loc.subtract(target);
        double projectoc = oc.dotProduct(pointingVec);
        if (projectoc<=0)return false;
        return (Math.toDegrees(Vector3D.angle(oc,pointingVec))<=14.0);

    }

}
