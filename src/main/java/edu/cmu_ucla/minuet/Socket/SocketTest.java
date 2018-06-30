package edu.cmu_ucla.minuet.Socket;

import java.io.IOException;

public class SocketTest {
    public static void main(String[] args) throws IOException {
        VitalSocketClient client1 = new VitalSocketClient("test client 1");
        VitalSocketClient client2 = new VitalSocketClient("test client 2");

        while(true){
            client1.sendToServer("Hello from client 1");
            client2.sendToServer("Hello from client 2");
        }
    }
}
