package edu.cmu_ucla.minuet.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectBox {
    private List<VitalObject> box = new ArrayList<>();
    private VitalObject curObject = null;
    public ObjectBox() {

    }
    public void addToBox(VitalObject object){box.add(object);}
    public void setCurObject(int index){curObject = box.get(index);}
    public VitalObject getCurObject(){
        if(box.size()==1)return box.get(0);
        else return curObject;
    }
}
