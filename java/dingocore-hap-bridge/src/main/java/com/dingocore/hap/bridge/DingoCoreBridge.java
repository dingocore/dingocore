package com.dingocore.hap.bridge;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.dingocore.Dingo;
import com.dingocore.endpoint.Endpoint;
import com.dingocore.endpoint.Property;
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
import com.dingocore.hap.client.model.Service;
import com.dingocore.hap.client.model.ServiceType;
import com.dingocore.hap.client.model.Services;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DingoCoreBridge {

    public DingoCoreBridge(ClientAuthStorage authStorage) {
        this.hapClient = new SimpleClient(authStorage);
    }

    public void bridge(String id) throws InterruptedException, ExecutionException, IOException, MqttException {
        this.id = id;
        Dingo system = Dingo.connect(id, "localhost", 1883);
        Connection connection = this.hapClient.connect(id);
        PairedConnection pairedConnection = connection.pairVerify();
        Accessories db = pairedConnection.accessories();

        for (Accessory accessory : db.accessories()) {

            Endpoint endpoint = system.addEndpoint("" + accessory.getAID());

            endpoint.setName(accessory.getName());
            endpoint.setManufacturer(accessory.getManufacturer());
            endpoint.setModel(accessory.getModel());
            endpoint.setHardwareRevision(accessory.getHardwareRevision());
            endpoint.setFirmwareRevision(accessory.getFirmwareRevision());

            for (Service each : accessory.getServices()) {
                ServiceType type = each.getType();
                if (type != null && type != Services.ACCESSORY_INFORMATION) {

                    Optional<Characteristic> nameChr = each.findCharacteristic(Characteristics.NAME);
                    String name = null;
                    if (nameChr.isPresent()) {
                        name = nameChr.get().getValue().toString();
                    }

                    com.dingocore.endpoint.Service service = endpoint.addService("" + each.getIID());
                    service.setName(name);
                    service.setType(com.dingocore.ServiceType.SWITCH);

                    for (Characteristic e : each.getCharacteristics()) {
                        System.err.println("chr: " + e);
                        if (e.getType() != Characteristics.NAME) {
                            Format format = e.getType().getFormat();
                            if (format == Format.BOOL) {
                                Consumer<Boolean> updater = e.isWritable() ? e::updateValue : null;
                                Property<Boolean> property = service.addBooleanProperty("" + e.getType().getName(), updater);
                                property.updateValue(e.getValue(Boolean.class));
                                if (e instanceof EventableCharacteristic) {
                                    ((EventableCharacteristic) e).addListener((chr) -> {
                                        property.updateValue(chr.getValue(Boolean.class));
                                    });
                                }
                            } else if (format == Format.FLOAT) {
                                Consumer<Double> updater = e.isWritable() ? e::updateValue : null;
                                Property<Double> property = service.addFloatProperty("" + e.getType().getName(), updater);
                                property.updateValue(e.getValue(Double.class));
                                if (e instanceof EventableCharacteristic) {
                                    ((EventableCharacteristic) e).addListener((chr) -> {
                                        property.updateValue(chr.getValue(Double.class));
                                    });
                                }
                            }

                        }
                    }
                }
            }
        }
        system.ready();
    }

    private final SimpleClient hapClient;

    private MqttClient mqttClient;

    private String id;
}
