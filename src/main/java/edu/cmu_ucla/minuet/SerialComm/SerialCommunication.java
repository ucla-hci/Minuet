package edu.cmu_ucla.minuet.SerialComm;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

public class SerialCommunication {

    final String PORT_NAME = "/dev/cu.wchusbserial1450";
    final int BAUD_RATER = 9600;
    SerialPort comPort;
    byte[] mybyte;
//    List<Byte> bytes = new ArrayList<>();
//    final edu.cmu_ucla.minuet.model.VitalWorld world;
    public SerialCommunication() {
//        edu.cmu_ucla.minuet.model.VitalWorld World = world;
        comPort = SerialPort.getCommPort(PORT_NAME);
        comPort.setBaudRate(BAUD_RATER);
        comPort.openPort();
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                if(event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)return;
                byte[] data = new byte[comPort.bytesAvailable()];

                int numRead = comPort.readBytes(data,data.length);
                for(byte a : data){
                    mybyte[mybyte.length]=a;
                }
                System.out.println("Read " + numRead + " bytes.");

                try {
//                    world.revceiveData(new String(data, "ASCII"));
                    if(mybyte.length>=34){
                        System.out.println("Read " + new String(mybyte, "ASCII"));
                    }
                    System.out.println("Read " + new String(data, "ASCII"));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

}
