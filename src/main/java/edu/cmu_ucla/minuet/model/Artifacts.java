package edu.cmu_ucla.minuet.model;

import edu.cmu_ucla.minuet.NLP.TokenNode;

import java.util.Set;

public class Artifacts extends VitalObject {
    @Override
    public String[] execuate(TokenNode node) {
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

    @Override
    public String[] resumeObject() {
        return new String[0];
    }

    @Override
    public String[] enteringObject() {
        return new String[0];
    }

    @Override
    public String[] leavingObject() {
        return new String[0];
    }

    @Override
    public String[] selectedObject() {
        return new String[0];
    }
}
