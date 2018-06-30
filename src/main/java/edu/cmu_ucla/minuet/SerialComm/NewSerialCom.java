package edu.cmu_ucla.minuet.SerialComm;

import com.rm5248.serial.NoSuchPortException;
import com.rm5248.serial.NotASerialPortException;
import com.rm5248.serial.SerialPort;
import edu.cmu_ucla.minuet.model.VitalWorld;

import java.io.IOException;
import java.io.InputStream;

public class NewSerialCom {
    final String PORT_NAME = "/dev/cu.wchusbserial1450";
    final int BAUD_RATER = 9600;
    SerialPort serialPort;
    String string = "";
    int counter = 0;
    VitalWorld world;
    //    boolean flag = false;
    public NewSerialCom(VitalWorld world) {
        world = world;
        try {
            serialPort = new SerialPort(PORT_NAME, SerialPort.NO_CONTROL_LINE_CHANGE);
            serialPort.setBaudRate(SerialPort.BaudRate.B9600);
            while (true) {
                InputStream stream = serialPort.getInputStream();
                int available = stream.available();
                if (available > 0) {
                    byte[] bytes = new byte[available];
                    stream.read(bytes, 0, available);
                    string += new String(bytes, "ASCII");
                    counter += 1;

                } else {
                    if (!string.equals("") && counter == 2) {
                        world.revceiveData(string);
                        string = "";
                        counter = 0;
                    }
                }
            }

        } catch (NoSuchPortException e) {
            System.err.println("Oh no!  That port doesn't exist!");
        } catch (NotASerialPortException e) {
            System.err.println("Oh no!  That's not a serial port!");
        } catch (IOException e) {
            System.err.println("An IOException occured");
        }

    }
}
