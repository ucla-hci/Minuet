package edu.cmu_ucla.minuet.apiTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

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
        String[] commands = {"osascript","-e","display notification \"New meeting created\" with title \"Calendar\" sound name \"default\""};
        try {
            Runtime.getRuntime().exec(commands);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            VolumeTest test = new VolumeTest();
//            System.out.print("Enter the volume:  ");
//            String input = scanner.nextLine();
//
//            test.setMasterVolume(Float.parseFloat(input));
//        }


    }


}
