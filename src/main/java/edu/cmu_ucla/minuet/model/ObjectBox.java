package edu.cmu_ucla.minuet.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectBox {
    private List<VitalObject> box = new ArrayList<>();

    private int curIndex = 0;
    public ObjectBox() {

    }
    public boolean isEmpty(){return box.isEmpty();}
    public boolean isOne(){return box.size()==1;}
    public synchronized void addToBox(VitalObject object){box.add(object);}
    public synchronized VitalObject getNextObject(){
        curIndex = (curIndex+1)%box.size();
        return box.get(curIndex);

    }
    public synchronized VitalObject getCurObject(){
        if(box.size()==1)return box.get(0);
        else return box.get(curIndex);
    }
    public synchronized void setNext(){
        curIndex = (curIndex+1)%box.size();
    }
    public synchronized void setPrevious(){
        curIndex=Math.floorMod(curIndex-1, box.size());

    }
}
