package edu.cmu_ucla.minute.test;

import edu.cmu_ucla.minuet.mqtt.MQTTMonitorVolumeSub;
import org.junit.Before;
import org.junit.Test;

public class MonitorVolumeMqtt {
    @Before
    public void setUp() throws Exception {
        MQTTMonitorVolumeSub monitorVolumeMqtt = new MQTTMonitorVolumeSub();

    }

    @Test
    public void run() {
        while(true){}
    }
}
