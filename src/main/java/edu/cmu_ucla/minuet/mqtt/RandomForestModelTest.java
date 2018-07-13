package edu.cmu_ucla.minuet.mqtt;

import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.io.*;
import java.util.Arrays;

public class RandomForestModelTest {


    public static void main(String[] args) {
        try {
            IBk rf = (IBk) SerializationHelper.read(new FileInputStream("weka/KNN.model"));
//            Logistic logistic = (Logistic) SerializationHelper.read(new FileInputStream("weka/LOGV2.model"));
            BufferedReader bf = new BufferedReader(new FileReader("weka/test4.arff"));
            Instances test = new Instances(bf);
            bf.close();
//            BufferedReader bf2 = new BufferedReader(new FileReader("weka/test4.arff"));
//            Instances test2 = new Instances(bf2);
            test.setClassIndex(test.numAttributes()-1);
//            test2.setClassIndex(test2.numAttributes()-1);
            for (int i = 0; i<test.numInstances();i++){
                double clasLabel = rf.classifyInstance(test.instance(i));
//                double clasLabel2 = logistic.classifyInstance(test2.instance(i));
//                rf.distributionForInstance()
//                System.out.println("MP: "+clasLabel);
//                System.out.println("LOG: "+clasLabel2);

                System.out.println(Arrays.toString(rf.distributionForInstance(test.instance(i))));

                test.instance(i).setClassValue(clasLabel);
//                test2.instance(i).setClassValue(clasLabel2);
    //            System.out.println(clasLabel);
            }

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("weka/MPtestResult.arff"));
            bufferedWriter.write(test.toString());
            bufferedWriter.close();
//            BufferedWriter bufferedWriter2 = new BufferedWriter(new FileWriter("weka/logTestResult.arff"));
//            bufferedWriter2.write(test2.toString());
//            bufferedWriter2.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
