package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class LocData {
    private double yaw;
    private double pitch;
    private double roll;
    private Vector3D pos;
    public LocData(double yaw,double pitch, double roll,Vector3D pos) {
              this.pitch = pitch;
              this.roll = roll;
              this.yaw = yaw;
              this.pos = pos;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public Vector3D getPos() {
        return pos;
    }
}
