package edu.cmu_ucla.minute.test;

import java.io.IOException;

public class TestPythonRun {
    public static void main(String[] args) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process p = Runtime.getRuntime().exec("python /Users/runchangkang/Documents/Minuet/pySpeech/liveTest.py");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        while (true){

        }
    }

}
