package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.List;

public class Zone {
    private List<Vector3D> pointList;
    private Vector3D centroid;
    private String name;

    public Vector3D getCentroid() {
        return centroid;
    }
    public void setCentroid(Vector3D center){centroid = center;}
    public String getName() {
        return name;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    private double radius;

    public Zone(String name) {
        this.pointList = new ArrayList<>();
        this.name = name;

    }
    public void addToList(Vector3D point){this.pointList.add(point);}

    public void generateCentroid(){
        double x = pointList.stream().mapToDouble(o->o.getX()).sum();
        x = x/pointList.size();
        double y = pointList.stream().mapToDouble(o->o.getY()).sum();
        y = y/pointList.size();
        centroid = new Vector3D(x,y,0);
        double maxRadius = pointList.stream().mapToDouble(o->Vector3D.distance(o,centroid)).max().getAsDouble();
        double minRadius = pointList.stream().mapToDouble(o->Vector3D.distance(o,centroid)).min().getAsDouble();
        this.radius = (maxRadius+minRadius)/2;
        System.out.println("center "+centroid.toString());
        System.out.println("radius "+radius);
    }

}

