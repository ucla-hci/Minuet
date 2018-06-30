package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public interface BoundingObject {

    Vector3D getCenter();

    boolean calculate(Vector3D target, Vector3D pointingVec);

}
