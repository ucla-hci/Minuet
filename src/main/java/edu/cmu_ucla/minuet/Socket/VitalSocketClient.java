package edu.cmu_ucla.minuet.Socket;

import java.io.*;
import java.net.Socket;

public class VitalSocketClient {
    private String id = null;
    private final String host = "localhost";
    private final int PORT = 8821;
    private Socket clientSocket;
    private PrintWriter os;
    private BufferedReader is;

    public VitalSocketClient(String id) throws IOException {
        this.id = id;
         clientSocket = new Socket(host,PORT);
         os = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()),true);
         is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));


    }
    public void sendToServer(String text) throws IOException {
        os.println(text);
    }



}
