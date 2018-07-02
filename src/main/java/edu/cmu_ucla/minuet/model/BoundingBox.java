package edu.cmu_ucla.minuet.model;


import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class BoundingBox implements BoundingObject {



    private Vector3D center;
    private Vector3D topCenter,topLeft,topRight,down;
    private double length;
    private double width;
    private double height;

    public BoundingBox(Vector3D topCenter, Vector3D topLeft, Vector3D topRight, Vector3D down) {
        this.topCenter = topCenter;
        this.topLeft = topLeft;
        this.topRight = topRight;
        this.down = down;
        Vector3D diag =topRight.add(down.subtract(topCenter));
        center = diag.subtract(diag.subtract(topLeft).scalarMultiply(0.5));
        this.length = Vector3D.distance(topCenter,topRight);
        this.width = Vector3D.distance(topCenter,topLeft);
        this.height = Vector3D.distance(topCenter,down);

    }

    @Override
    public Vector3D getCenter() {
        return center;
    }

    @Override
    public boolean calculate(Vector3D target, Vector3D rotation) {
        //to do
        return false;
    }
}
