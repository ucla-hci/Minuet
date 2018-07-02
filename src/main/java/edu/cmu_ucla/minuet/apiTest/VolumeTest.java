package edu.cmu_ucla.minuet.apiTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Scanner;

public class VolumeTest {


    public void setMasterVolume(float value) {
        String command = "set volume " + value;
        try {
            ProcessBuilder pb = new ProcessBuilder("osascript", "-e", command);
            pb.directory(new File("/usr/bin"));
            System.out.println(command);
            StringBuffer output = new StringBuffer();
            Process p = pb.start();
            p.waitFor();

            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            System.out.println(output);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            VolumeTest test = new VolumeTest();
            System.out.print("Enter the volume:  ");
            String input = scanner.nextLine();

            test.setMasterVolume(Float.parseFloat(input));
        }


    }


}
