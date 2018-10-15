package com.dingocore.hap.bridge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.dingocore.hap.client.auth.ClientAuthStorage;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.junit.Before;
import org.junit.Test;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DingoCoreBridgeTest {
    @Before
    public void setUp() throws IOException {
        this.authStorage = ClientAuthStorage.filesystem("footbridge");
    }

    @Test
    public void test() throws Exception {
        MqttClientPersistence persistence = new MemoryPersistence();
        MqttClient mqttClient = new MqttClient("tcp://localhost:1883", "testclient", persistence);
        mqttClient.connect();
        mqttClient.subscribe("#", (topic, msg) -> {
            System.err.println("message on " + topic + " // " + msg);
        });


        DingoCoreBridge bridge = new DingoCoreBridge(this.authStorage);
        bridge.bridge("16:BD:8D:FE:CC:B6");
        Thread.sleep(1000);

        /*
        SimpleClient client = new SimpleClient(this.authStorage);

        PairedConnection conn = client.connect("16:BD:8D:FE:CC:B6").pairVerify();
        Accessories db = conn.accessories();
        db.find(1)
                .ifPresent( (acc)->{
                    acc.findCharacteristic(11).ifPresent( (chr)->{
                        chr.updateValue( ! (Boolean)chr.getValue()  );
                    });
                });
                */

        /*
        boolean val = true;

        for ( int i = 0 ; i < 5 ; ++i ) {
            //mqttClient.publish("/kite/16:BD:8D:FE:CC:B6-1/10/On/set", new MqttMessage("false".getBytes(UTF_8)));
            mqttClient.publish("/dingo/16:BD:8D:FE:CC:B6-1/10/11/set", new MqttMessage(("" + val).getBytes(UTF_8)));
            val = ! val;
            Thread.sleep(1000);
        }
        */

        for (int i = 0; i < 5; ++i) {
            mqttClient.publish("dingo/16:BD:8D:FE:CC:B6/1/20/Hue/set", ("" + i).getBytes(StandardCharsets.UTF_8), 1, false);
            Thread.sleep(250);
        }
        Thread.sleep(3000);
    }

    private ClientAuthStorage authStorage;
}
