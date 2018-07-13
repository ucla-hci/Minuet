package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NoInteractionLogger implements MqttCallback {

    private MqttClient client;
    private MqttConnectOptions options;
    private List<Struct> data = new ArrayList<>();
    private class Struct {
        double ax;
        double ay;
        double az;
        double gx;
        double gy;
        double gz;

        public Struct(double ax, double ay, double az, double gx, double gy, double gz) {
            this.ax = ax;
            this.ay = ay;
            this.az = az;
            this.gx = gx;
            this.gy = gy;
            this.gz = gz;
        }

        @Override
        public String toString() {
            return new String(""+ax+","+ay+","+az+","+gx+","+gy+","+gz);
        }
    }

    public NoInteractionLogger() throws MqttException {
        client = new MqttClient("tcp://192.168.1.8:1883", "noInteractionLogger");
        options = new MqttConnectOptions();
        options.setUserName("admin");
        options.setPassword("19930903".toCharArray());
        client.setCallback(this);
        client.connect(options);
        client.subscribe("data");
        System.out.println("matt started");

    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String newData = new String(message.getPayload());
        if(newData.equals("end")){
            storeTheData();
            client.disconnect();

        }
        String[] splitedString = newData.split("\\s+");
        double cax = Double.parseDouble(splitedString[0]);
        double cay = Double.parseDouble(splitedString[1]);
        double caz = Double.parseDouble(splitedString[2]);
        double cgx = Double.parseDouble(splitedString[3]);
        double cgy = Double.parseDouble(splitedString[4]);
        double cgz = Double.parseDouble(splitedString[5]);
        Struct curStruct = new Struct(cax, cay, caz, cgx, cgy, cgz);
        data.add(curStruct);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }
    private void storeTheData() throws IOException {
        FileWriter writer = new FileWriter("src/resources/trainNoInteraction2.csv");
        System.out.println(data.size());
        for(int i =0;i<data.size()-15;i++){
            for(int j = 0;j<15;j++){
                Struct cs = data.get(i+j);
                writer.write(Double.toString(cs.ax));
                writer.write(",");
                writer.write(Double.toString(cs.ay));
                writer.write(",");
                writer.write(Double.toString(cs.az));
                writer.write(",");
                writer.write(Double.toString(cs.gx));
                writer.write(",");
                writer.write(Double.toString(cs.gy));
                writer.write(",");
                writer.write(Double.toString(cs.gz));
                writer.write(",");
            }
            writer.write("noInteraction");
            writer.write("\n");
        }
        writer.close();
    }

    public static void main(String[] args) {
        try {
            NoInteractionLogger noInteractionLogger = new NoInteractionLogger();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
