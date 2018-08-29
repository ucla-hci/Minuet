package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class BoundingSphere implements BoundingObject {


    private Vector3D center;
    private double radius;

    public BoundingSphere(Vector3D center, double radius) {
        this.center = center;
        this.radius = radius;
    }

    @Override
    public Vector3D getCenter() {
        return center;
    }

    @Override
    public boolean calculate(Vector3D target,Vector3D pointingVec) {

        Vector3D oc = center.subtract(target);
        double projectoc = oc.dotProduct(pointingVec);
        if (projectoc<=0)return false;
        double oc2 = oc.dotProduct(oc);
        double distant2 = oc2 - projectoc*projectoc;
        if (Math.toDegrees(Vector3D.angle(oc, pointingVec)) <= 20) {
            System.out.println("angle= "+ Math.toDegrees(Vector3D.angle(oc,pointingVec)));
            System.out.println("distant= "+ Math.sqrt(distant2));
        }
        return (Math.toDegrees(Vector3D.angle(oc,pointingVec))<=16.0);
    }


}
