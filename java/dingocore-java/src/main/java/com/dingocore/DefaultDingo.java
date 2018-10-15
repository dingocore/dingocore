package com.dingocore;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.dingocore.endpoint.impl.DefaultEndpoint;
import com.dingocore.endpoint.Endpoint;
import com.dingocore.mqtt.BusConnection;
import com.dingocore.surface.Surface;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static com.dingocore.State.INIT;
import static com.dingocore.State.READY;

public class DefaultDingo implements Dingo, TopicNode {

    DefaultDingo(String identifier, String host, int port) {
        this.identifier = identifier;
        try {
            MqttClientPersistence persistence = new MemoryPersistence();
            MqttClient mqttClient = new MqttClient("tcp://" + host + ":" + port, "client", persistence);
            //MqttClient mqttClient = new MqttClient("tcp://localhost:9001", "client", persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setWill($topic("state"), State.LOST.getIdentifier().getBytes(StandardCharsets.UTF_8), 1, true);
            mqttClient.connect(options);
            this.busConnection = new BusConnection(mqttClient);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        state(INIT);
    }

    void state(State state) {
        getBusConnection().publish($topic("state"), state.getIdentifier(), 1, true);
    }

    public String topic() {
        return "dingo/" + this.identifier;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    public Endpoint addEndpoint(String identifier) {
        return new DefaultEndpoint(this, identifier);
    }

    @Override
    public void addSurface(Surface surface) {
        this.surfaces.add(surface);
    }

    public BusConnection getBusConnection() {
        return this.busConnection;
    }

    public void ready() {
        state(READY);
    }

    private final String identifier;

    private BusConnection busConnection;

    private List<Surface> surfaces = new ArrayList<>();


}
