package com.dingocore.hap.client.mqtt;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.dingocore.hap.client.Connection;
import com.dingocore.hap.client.PairedConnection;
import com.dingocore.hap.client.SimpleClient;
import com.dingocore.hap.client.auth.ClientAuthStorage;
import com.dingocore.hap.client.model.Accessories;
import com.dingocore.hap.client.model.Accessory;
import com.dingocore.hap.client.model.Characteristic;
import com.dingocore.hap.client.model.Characteristics;
import com.dingocore.hap.client.model.EventableCharacteristic;
import com.dingocore.hap.client.model.Format;
import com.dingocore.hap.client.model.Permission;
import com.dingocore.hap.client.model.Service;
import com.dingocore.hap.client.model.ServiceType;
import com.dingocore.hap.client.model.Services;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MQTTBridge implements Consumer<EventableCharacteristic> {

    public MQTTBridge(ClientAuthStorage authStorage) {
        this.hapClient = new SimpleClient(authStorage);
    }

    public void bridge(String id) throws InterruptedException, ExecutionException, IOException, MqttException {
        this.id = id;
        mqttClient = new MqttClient("tcp://localhost:1883", id);
        mqttClient.connect();
        Connection connection = this.hapClient.connect(id);
        PairedConnection pairedConnection = connection.pairVerify();
        Accessories db = pairedConnection.accessories();

        for (Accessory accessory : db.accessories()) {
            MqttMessage message = null;

            message = new MqttMessage(accessory.getName().getBytes(UTF_8));
            message.setRetained(true);
            mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/$name", message);

            message = new MqttMessage(accessory.getManufacturer().getBytes(UTF_8));
            message.setRetained(true);
            mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/$manufacturer", message);

            message = new MqttMessage(accessory.getModel().getBytes(UTF_8));
            message.setRetained(true);
            mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/$model", message);

            message = new MqttMessage(accessory.getHardwareRevision().getBytes(UTF_8));
            message.setRetained(true);
            mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/$hw/revision", message);

            message = new MqttMessage(accessory.getFirmwareRevision().getBytes(UTF_8));
            message.setRetained(true);
            mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/$fw/revision", message);

            String nodes = accessory.getServices().stream()
                    .filter(e -> e.getType() != null)
                    .filter(e -> e.getType() != Services.ACCESSORY_INFORMATION)
                    .map(e -> e.getIID() + "")
                    .collect(Collectors.joining(","));

            message = new MqttMessage(nodes.getBytes(UTF_8));
            message.setRetained(true);
            mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/$nodes", message);


            for (Service service : accessory.getServices()) {
                ServiceType type = service.getType();
                if (type != null && type != Services.ACCESSORY_INFORMATION) {
                    message = new MqttMessage(service.getName().getBytes(UTF_8));
                    message.setRetained(true);
                    mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/" + service.getIID() + "/$name", message);

                    message = new MqttMessage(type.getName().getBytes(UTF_8));
                    message.setRetained(true);
                    mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/" + service.getIID() + "/$type", message);

                    String properties = service.getCharacteristics().stream()
                            .filter(e -> e.getType() != Characteristics.NAME)
                            .map(e -> e.getType().getName())
                            .collect(Collectors.joining(","));

                    message = new MqttMessage(properties.getBytes(UTF_8));
                    message.setRetained(true);
                    mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/" + service.getIID() + "/$properties", message);

                    for (Characteristic e : service.getCharacteristics()) {
                        if (e.getType() != Characteristics.NAME) {
                            Object value = e.getValue();
                            message = new MqttMessage(value.toString().getBytes(UTF_8));
                            message.setRetained(true);
                            mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/" + service.getIID() + "/" + e.getType().getName(), message);

                            if (e.getType().getPermissions().contains(Permission.PAIRED_WRITE)) {
                                message = new MqttMessage("true".getBytes(UTF_8));
                                message.setRetained(true);
                                mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/" + service.getIID() + "/" + e.getType().getName() + "/$settable", message);

                                mqttClient.subscribe("/kite/" + id + "-" + accessory.getAID() + "/" + service.getIID() + "/" + e.getType().getName() + "/set",
                                                     (topic, msg) -> {
                                                         System.err.println("inbound set: " + topic + " // " + msg);
                                                         Object setValue = null;
                                                         if (e.getType().getFormat() == Format.BOOL) {
                                                             setValue = Boolean.parseBoolean(msg.toString());
                                                         }
                                                         e.updateValue(setValue);
                                                         System.err.println("updated");
                                                     });
                            }
                        }
                    }
                }
            }
        }

        db.addListener(this);
    }

    @Override
    public void accept(EventableCharacteristic characteristic) {
        Object value = characteristic.getValue();
        MqttMessage message = new MqttMessage(value.toString().getBytes(UTF_8));
        message.setRetained(true);
        Service service = characteristic.getService();
        Accessory accessory = service.getAccessory();
        try {
            mqttClient.publish("/kite/" + id + "-" + accessory.getAID() + "/" + service.getIID() + "/" + characteristic.getType().getName(), message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private final SimpleClient hapClient;

    private MqttClient mqttClient;

    private String id;
}
