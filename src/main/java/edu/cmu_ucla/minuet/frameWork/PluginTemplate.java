package edu.cmu_ucla.minuet.frameWork;

import edu.cmu_ucla.minuet.model.VitalObject;
import edu.cmu_ucla.minuet.model.VitalWorld;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

public abstract class PluginTemplate {
    private MQTT mqtt;
    private VitalWorld world;
    private VitalObject targetObject;
    public PluginTemplate(MQTT mqtt, VitalWorld world) {
        this.mqtt = mqtt;
        this.world = world;
        Thread t = new Thread(() -> {
            try {
                mqtt.sendMessage("connectedVoice","$please select target object");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

}
