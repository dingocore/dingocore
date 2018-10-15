package com.dingocore.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class BusConnection {

    public BusConnection(MqttClient client) {
        this.client = client;
        try {
            System.err.println( "ADDED DEBUG");
            this.client.subscribe("#", (t,m)->{
                System.err.println( "debug: " + t + " // " + m);
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String value, int qos, boolean retained) {
        if (value == null) {
            System.err.println("attempt to publish null to " + topic);
            new Exception().printStackTrace();
            return;
        }
        System.err.println("### " + topic + " -> " + value);
        try {
            this.client.publish(topic, value.getBytes(StandardCharsets.UTF_8), qos, retained);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void addListener(String topic, Consumer<byte[]> handler) {
        System.err.println("WIRE LISTENER TO: " + topic);
        try {
            this.client.subscribe(topic, (t, m) -> {
                System.err.println("LISTENER MESSAGE ON " + t + " // " + m);
                byte[] payload = m.getPayload();
                handler.accept(payload);
            });
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    private final MqttClient client;
}
