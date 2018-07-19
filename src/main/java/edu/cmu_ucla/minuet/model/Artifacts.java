package edu.cmu_ucla.minuet.model;

import java.util.Set;

public class Artifacts extends VitalObject {
    @Override
    public String[] execuate(Set<String> command) {
        return new String[0];
    }

    @Override
    public String[] execuate(Set<String> command, String gesture) {
        return new String[0];
    }

    @Override
    public String[] execuate(String gesture) {
        return new String[0];
    }

    public Artifacts(BoundingObject boundingObject, String name, String topic) {
        super(boundingObject, name, topic);

    }
}
