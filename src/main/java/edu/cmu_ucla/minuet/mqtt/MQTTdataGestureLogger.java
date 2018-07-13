package edu.cmu_ucla.minuet.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.util.List;

public class MQTTdataGestureLogger extends JFrame {

    private static Boolean isPressed = false;
    private JList<String> names = new JList<>();

    private JLabel pressedLabel = new JLabel("Is Pressed: ");
    private String curLabel;
    private JPanel rightPanel = new JPanel();
    private  JSplitPane splitPane = new JSplitPane();
    private Map<String,List<Vector<Struct>>> dataMap = new HashMap<>();
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

    private class MQTTdataLogger extends SwingWorker<Void, Vector<Struct>> implements MqttCallback {

        private Vector<Struct> curTrainVec;



        private MqttClient client;
        private boolean isDead = false;


        public MQTTdataLogger() throws MqttException {
            System.out.println("Start connecting data logger MQTT");
            this.client = new MqttClient("tcp://192.168.1.8:1883", "dataLogger");
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("admin");
            options.setPassword("19930903".toCharArray());
            client.setCallback(this);
            client.connect(options);
            client.subscribe("data");
            this.curTrainVec = new Vector<Struct>(15);
            System.out.println("Mqtt connection finished");
        }

        @Override
        protected Void doInBackground() throws Exception {
            while (!isDead) {
            }
            return null;
        }

        @Override
        public void connectionLost(Throwable cause) {

        }
        @Override
        protected void process(List<Vector<Struct>> chunks) {
            if (!dataMap.containsKey(curLabel)){
                dataMap.put(curLabel,new ArrayList<Vector<Struct>>());
            }
            dataMap.get(curLabel).add(chunks.get(chunks.size()-1));


            pressedLabel.setText("Start Training " + isPressed);
            splitPane.validate();
            splitPane.repaint();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            synchronized (this) {
                if (isPressed) {
                    String newData = new String(message.getPayload());
                    String[] splitedString = newData.split("\\s+");
                    double cax = Double.parseDouble(splitedString[0]);
                    double cay = Double.parseDouble(splitedString[1]);
                    double caz = Double.parseDouble(splitedString[2]);
                    double cgx = Double.parseDouble(splitedString[3]);
                    double cgy = Double.parseDouble(splitedString[4]);
                    double cgz = Double.parseDouble(splitedString[5]);
                    Struct curStruct = new Struct(cax, cay, caz, cgx, cgy, cgz);
                    if (curTrainVec.size() == 15) {
                        System.out.println(curTrainVec.toString());
                        Vector<Struct> tmp = new Vector<>();
                        for(Struct s:curTrainVec){
                            tmp.addElement(new Struct(s.ax,s.ay,s.az,s.gx,s.gy,s.gz));
                        }
                        publish(tmp);
                        curTrainVec.clear();
                        isPressed = false;


                    } else curTrainVec.addElement(curStruct);
                }
            }
        }


        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    }

    private class DataToFile{
        private Map<String,List<Vector<Struct>>> dataMap;
        public DataToFile(Map<String,List<Vector<Struct>>> dataMap) throws IOException {
            this.dataMap = dataMap;
            FileWriter writer = new FileWriter("src/resources/train2.csv");
            for (String s: dataMap.keySet()){
                for(Vector<Struct> vs : dataMap.get(s)){
                   for(Struct cs : vs){
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
                   writer.write(s);
                   writer.write("\n");
                }
            }
            writer.close();

        }
    }
    public MQTTdataGestureLogger(String title) {

        super(title);


        DefaultListModel<String> model = new DefaultListModel<>();
        JButton trainButton = new JButton("Train");
        JButton stortButton = new JButton("save All");

        //set up JLIST

        model.addElement("leftSwap");
        model.addElement("rightSwap");
        model.addElement("upSwap");
        model.addElement("downSwap");
        model.addElement("circleCW");
        model.addElement("circleCCW");
        model.addElement("rigtArc");
        model.addElement("leftArc");
        model.addElement("z");
        model.addElement("NOTHING");
        names.setModel(model);
        names.getSelectionModel().addListSelectionListener(e -> {
                    synchronized (this) {
                        this.curLabel = names.getSelectedValue();
                    }
                }
        );
        splitPane.setLeftComponent(new JScrollPane(names));

        rightPanel.setLayout(new GridLayout());
        rightPanel.setPreferredSize(new Dimension(500, 500));


        pressedLabel.setFont(new Font("serif", Font.BOLD, 28));


        trainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                synchronized (this) {
                    isPressed = true;
                    pressedLabel.setText("Start Training " + true);
                    splitPane.validate();
                    splitPane.repaint();
                }
            }
        });

        stortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                synchronized (dataMap) {
                    int i = 0;
                    for(String s:dataMap.keySet()){
                        i+=dataMap.get(s).size();
                    }
                    System.out.println("Currently has data amount: "+i);
                    try {
                        DataToFile dataToFile = new DataToFile(dataMap);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        rightPanel.add(pressedLabel);
        rightPanel.add(trainButton);
        rightPanel.add(stortButton);

        splitPane.setRightComponent(rightPanel);
        add(splitPane);

        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        SwingWorker<Void, Vector<Struct>> swingWorker;
       try {
           swingWorker = new MQTTdataLogger(){
               @Override
               protected void process(List<Vector<Struct>> chunks) {

                       if (!dataMap.containsKey(curLabel)){
                           dataMap.put(curLabel,new ArrayList<Vector<Struct>>());
                       }
                       dataMap.get(curLabel).add(chunks.get(chunks.size()-1));


                       pressedLabel.setText("Start Training " + isPressed);
                       splitPane.validate();
                       splitPane.repaint();

               }
           };
           swingWorker.execute();
       }catch (Exception e){e.printStackTrace();}

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new MQTTdataGestureLogger("Trianing data logger");
            }
        });

    }
}
