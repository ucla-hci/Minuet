package edu.cmu_ucla.minuet.frameWork;

import edu.cmu_ucla.minuet.model.VitalObject;
import edu.cmu_ucla.minuet.model.VitalWorld;
import edu.cmu_ucla.minuet.mqtt.MQTT;
import org.eclipse.paho.client.mqttv3.MqttException;

public class PluginRemover extends PluginTemplate implements Plugable {
    @Override
    public void setTargetObject(VitalObject targetObject) {
        this.targetObject = targetObject;

        Thread t = new Thread(() -> {
            try {
                mqtt.sendMessage("connectedVoice", "$please select the plugin you want to remove");
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
        t.start();
    }

    @Override
    public VitalObject getTargetObject() {
        return this.targetObject;
    }

    @Override
    public void getVoiceCommand(String voice) {
        synchronized (world) {
            for(Plugable plugin:world.getWorkingPlugins()){
                if(plugin.getTargetObject()==this.targetObject){
                    plugin.kill();
                    world.getWorkingPlugins().remove(plugin);

                    Thread t = new Thread(() -> {
                        try {
                            mqtt.sendMessage("connectedVoice", "$plugin removed");
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                    });
                    t.start();
                    world.removeCurPlugin();
                }

            }
        }

    }

    public PluginRemover(MQTT mqtt, VitalWorld world) {
        super(mqtt, world);
    }
}
