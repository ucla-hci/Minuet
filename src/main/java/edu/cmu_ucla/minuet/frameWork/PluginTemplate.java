package edu.cmu_ucla.minuet.frameWork;

import edu.cmu_ucla.minuet.model.VitalObject;
import edu.cmu_ucla.minuet.model.VitalWorld;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

public abstract class PluginTemplate implements Plugable {
    protected MQTT mqtt;
    protected VitalWorld world;
    protected VitalObject targetObject;
    protected int curStage = 0;
    protected boolean isRunning = true;
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
    public void kill() {
        isRunning = false;
    }
}
