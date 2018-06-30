package edu.cmu_ucla.minuet.Socket;

import edu.cmu_ucla.minuet.model.VitalWorld;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VitalSocketServer {

    private final int PORT = 8821;
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private final int POOL_SIZE=10;
    private VitalWorld world;
    public VitalSocketServer(VitalWorld w) throws IOException {
        world = w;
        serverSocket = new ServerSocket(PORT);
        executorService  = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()*POOL_SIZE);
        System.out.println("MultiThread Server Start");
    }

    public void service(){

        while(true){
            Socket socket=null;
            try {
                    socket=serverSocket.accept();
                    executorService.execute(new Handler(socket));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    class Handler implements Runnable {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        private PrintWriter getWriter(Socket socket) throws IOException {
            OutputStream socketOut = socket.getOutputStream();
            return new PrintWriter(socketOut, true);
        }

        private BufferedReader getReader(Socket socket) throws IOException {
            InputStream socketIn = socket.getInputStream();
            return new BufferedReader(new InputStreamReader(socketIn));
        }

        public void run() {
            try {
                System.out.println("New connection accepted " + socket.getInetAddress() + ":" + socket.getPort());
                BufferedReader br = getReader(socket);
                PrintWriter pw = getWriter(socket);
                String msg = null;
                while ((msg = br.readLine()) != null) {
//                    passDataToWorld(msg);
                    if (msg.equals("STOP"))
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (socket != null)
                        socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//    private void passDataToWorld(String data){
//        world.updataUserData(data);
//    }




    }

