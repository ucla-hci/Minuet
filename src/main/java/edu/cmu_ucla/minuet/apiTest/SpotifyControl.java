package edu.cmu_ucla.minuet.apiTest;

import java.io.IOException;
import java.util.Scanner;

public class SpotifyControl {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.print("Enter the command ID:  ");
            System.out.print("0:start 1:pause 2:next 3:Volume up 4:Volume down");
            String input = scanner.nextLine();
            switch (input){
                case "0":
                    try {
                        Runtime.getRuntime().exec("spotify start");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "1":
                    try {
                        Runtime.getRuntime().exec("spotify pause");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "2":
                    try {
                        Runtime.getRuntime().exec("spotify next");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case "3":
                    try {
                        Runtime.getRuntime().exec("spotify volume up");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "4":
                    try {
                        Runtime.getRuntime().exec("spotify volume down");

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;


            }

        }

    }
}
