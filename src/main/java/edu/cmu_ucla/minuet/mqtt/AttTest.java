package edu.cmu_ucla.minuet.mqtt;

import edu.cmu_ucla.minuet.model.Struct;
import weka.classifiers.functions.SMO;
import weka.core.SerializationHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a little ARFF file with different attribute types.
 *
 * @author FracPete
 */
public class AttTest {
    public static void main(String[] args) throws IOException, Exception {
        SMO model = (SMO) SerializationHelper.read(new FileInputStream("weka/SVM.model"));

        Struct struct0 = new Struct(-1.41, 0.08, -0.35, -1.00, -4.37, -22.13);
        Struct struct1 = new Struct(-1.25, -0.11, -1.06, 24.25, 5.62, -16.00);
        Struct struct2 = new Struct(-1.18, -0.43, -0.79, 4.81, 4.50, -4.3);
        Struct struct3 = new Struct(-0.45, -0.44, -0.25, 10.69, 10.31, -3.44);
        Struct struct4 = new Struct(-0.36, -0.27, -0.59, 7.31, 3.31, -2.81);
        Struct struct5 = new Struct(-0.54, -0.41, -0.31, -10.19, 0.81, -3.87);
        Struct struct6 = new Struct(-0.60, -0.34, -0.44, 0.56, 2.94, 1.75);
        Struct struct7 = new Struct(-0.41, -0.56, -0.32, 0.44, -0.19, 0.44);
        Struct struct8 = new Struct(-0.51, -0.38, -0.34, -1.31, 3.12, 2.06);
        Struct struct9 = new Struct(-0.43, -0.47, -0.63, 10.94, -6.62, 6.44);
        Struct struct10 = new Struct(-0.30, -0.61, -0.81, -1.87, -2.87, 0.00);
        Struct struct11 = new Struct(-0.24, -0.35, -0.15, -9.00, -6.37, -0.69);
        Struct struct12 = new Struct(-1.91, 0.16, -0.61, 2.81, -4.62, 3.12);
        Struct struct13 = new Struct(-2.33, 0.48, -1.06, -8.75, 3.37, 39.50);
        Struct struct14 = new Struct(-0.69, 0.03, -0.24, -4.12, -3.87, 68.81);
        Struct struct15 = new Struct(1.17, -0.74, 0.27, 18.19, -12.63, 40.06);
        Struct struct16 = new Struct(0.52, -1.15, -0.71, 23.12, 5.87, 13.81);
        Struct struct17 = new Struct(-0.74, -0.53, -1.07, 23.19, -0.25, 24.00);
        List<Struct> queue = new ArrayList<>(15);
        queue.add(struct0);
        queue.add(struct1);
        queue.add(struct2);
        queue.add(struct3);
        queue.add(struct4);
        queue.add(struct5);
        queue.add(struct6);
        queue.add(struct7);
        queue.add(struct8);
        queue.add(struct9);
        queue.add(struct10);
        queue.add(struct11);
        queue.add(struct12);
        queue.add(struct13);
        queue.add(struct14);
        ClassifierUtil.Classify(model,queue);



    }
//    public static void main(String[] args) throws Exception {
//        ArrayList<Attribute> atts;
//        ArrayList<Attribute>      attsRel;
//        ArrayList<String>      attVals;
//        ArrayList<String>      attValsRel;
//        Instances       data;
//        Instances       dataRel;
//        double[]        vals;
//        double[]        valsRel;
//        int             i;
//
//        // 1. set up attributes
//        atts = new ArrayList<>();
//        // - numeric
//        atts.add(new Attribute("att1"));
//        // - nominal
//        attVals = new ArrayList<>();
//        for (i = 0; i < 5; i++)
//            attVals.add("val" + (i+1));
//        atts.add(new Attribute("att2", attVals));
//        // - string
//        atts.add(new Attribute("att3", (ArrayList<String> ) null));
//        // - date
//        atts.add(new Attribute("att4", "yyyy-MM-dd"));
//        // - relational
//        attsRel = new ArrayList<>();
//        // -- numeric
//        attsRel.add(new Attribute("att5.1"));
//        // -- nominal
//        attValsRel = new ArrayList<>();
//        for (i = 0; i < 5; i++)
//            attValsRel.add("val5." + (i+1));
//        attsRel.add(new Attribute("att5.2", attValsRel));
//        dataRel = new Instances("att5", attsRel, 0);
//        atts.add(new Attribute("att5", dataRel, 0));
//
//        // 2. create Instances object
//        data = new Instances("MyRelation", atts, 0);
//
//        // 3. fill with data
//        // first instance
//        vals = new double[data.numAttributes()];
//        // - numeric
//        vals[0] = Math.PI;
//        // - nominal
//        vals[1] = attVals.indexOf("val3");
//        // - string
//        vals[2] = data.attribute(2).addStringValue("This is a string!");
//        // - date
//        vals[3] = data.attribute(3).parseDate("2001-11-09");
//        // - relational
//        dataRel = new Instances(data.attribute(4).relation(), 0);
//        // -- first instance
//        valsRel = new double[2];
//        valsRel[0] = Math.PI + 1;
//        valsRel[1] = attValsRel.indexOf("val5.3");
//        dataRel.add(new DenseInstance(1.0, valsRel));
//        // -- second instance
//        valsRel = new double[2];
//        valsRel[0] = Math.PI + 2;
//        valsRel[1] = attValsRel.indexOf("val5.2");
//        dataRel.add(new DenseInstance(1.0, valsRel));
//        vals[4] = data.attribute(4).addRelation(dataRel);
//        // add
//        data.add(new DenseInstance(1.0, vals));
//
//        // second instance
//        vals = new double[data.numAttributes()];  // important: needs NEW array!
//        // - numeric
//        vals[0] = Math.E;
//        // - nominal
//        vals[1] = attVals.indexOf("val1");
//        // - string
//        vals[2] = data.attribute(2).addStringValue("And another one!");
//        // - date
//        vals[3] = data.attribute(3).parseDate("2000-12-01");
//        // - relational
//        dataRel = new Instances(data.attribute(4).relation(), 0);
//        // -- first instance
//        valsRel = new double[2];
//        valsRel[0] = Math.E + 1;
//        valsRel[1] = attValsRel.indexOf("val5.4");
//        dataRel.add(new DenseInstance(1.0, valsRel));
//        // -- second instance
//        valsRel = new double[2];
//        valsRel[0] = Math.E + 2;
//        valsRel[1] = attValsRel.indexOf("val5.1");
//        dataRel.add(new DenseInstance(1.0, valsRel));
//        vals[4] = data.attribute(4).addRelation(dataRel);
//        // add
//        data.add(new DenseInstance(1.0, vals));
//
//        // 4. output data
//        System.out.println(data);
//    }
}