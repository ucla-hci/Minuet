package edu.cmu_ucla.minuet.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectBox {
    private List<VitalObject> box = new ArrayList<>();
    private Map<String,VitalObject> dictionary = new HashMap<>();
    private int curIndex = 0;
    public ObjectBox() {}

    public boolean isEmpty(){return box.isEmpty();}
    public boolean isOne(){return box.size()==1;}
    public synchronized void addToBox(VitalObject object){box.add(object);}

    public List<VitalObject> getBox() {
        return box;
    }

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
    public synchronized void checkLeftRight(String text, Vector3D userLoc){
        if(box.size()==2){
            setLeftRight(userLoc);
            String[] splitText = text.split("\\s+");
            for (String s:splitText
                 ) {
                if(s.equals("right")) {
                    curIndex = box.indexOf(dictionary.get("right"));
                }else if(s.equals("left")){
                    curIndex = box.indexOf(dictionary.get("left"));
                }
            }
        }
    }
    private void setLeftRight(Vector3D userloc){
        dictionary.clear();
        Vector3D A = box.get(0).getBoundingObject().getCenter().subtract(userloc);
        Vector3D B = box.get(1).getBoundingObject().getCenter().subtract(userloc);


        double adegree = Math.toDegrees(Math.atan2(A.getY(),A.getX()));
        double bdegree = Math.toDegrees(Math.atan2(B.getY(),B.getX()));
//        System.out.println(box.get(0).getName()+": "+adegree);
//        System.out.println(box.get(1).getName()+": "+bdegree);
        String result="";
        if(-180<=adegree&&adegree<=0 && -180<=bdegree&&bdegree<=0 ){
            result = (adegree<=bdegree)?"A":"B";

        }else if (0<=adegree&&adegree<=180 && 0<=bdegree&&bdegree<=180 ){
            result = (adegree>=bdegree)?"A":"B";
        }else if ((adegree*bdegree)<0){
            if(Math.abs(adegree)<=90){
                result = (adegree>=0)?"A":"B";

            }
            else {
                result = (adegree<=0)?"A":"B";
            }
        }
        if(result.equals("A")){
            dictionary.put("left",box.get(0));
            dictionary.put("right",box.get(1));
        }
        else{
            dictionary.put("right",box.get(0));
            dictionary.put("left",box.get(1));
        }
//        System.out.println("left is :" + dictionary.get("left").getName());
//        System.out.println("right is :" + dictionary.get("right").getName());

    }
}
