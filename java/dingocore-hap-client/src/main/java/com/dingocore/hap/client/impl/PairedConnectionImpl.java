package com.dingocore.hap.client.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import com.dingocore.hap.client.codec.AccessoriesRequest;
import com.dingocore.hap.client.codec.CharacteristicEventsRequest;
import com.dingocore.hap.client.codec.UpdateCharacteristicRequest;
import com.dingocore.hap.client.PairedConnection;
import com.dingocore.hap.client.model.Accessories;
import com.dingocore.hap.client.model.Characteristic;
import io.netty.channel.Channel;

/**
 * Created by bob on 8/30/18.
 */
public class PairedConnectionImpl implements PairedConnection {

    public PairedConnectionImpl(Channel channel) {
        this.channel = channel;
    }

    //public void setAccessories(AccessoriesImpl accessories) {
    //this.accessories.set(accessories);
    //}

    @Override
    public Accessories accessories() throws ExecutionException, InterruptedException {
        return this.accessories.updateAndGet((result) -> {
            if (result != null) {
                return result;
            }
            AccessoriesRequest req = new AccessoriesRequest();
            this.channel.pipeline().addLast("request", req);
            this.channel.pipeline().writeAndFlush(req);
            try {
                return req.getFuture().get();
            } catch (InterruptedException | ExecutionException e) {
                return null;
            } finally {
                this.channel.pipeline().remove("request");
            }
        });
    }

    void updateValue(Characteristic characteristic, Object value) throws ExecutionException, InterruptedException {
        UpdateCharacteristicRequest req = new UpdateCharacteristicRequest(characteristic, value);
        System.err.println("**** WRITE AND FLUSH");
        this.channel.pipeline().writeAndFlush(req);
        System.err.println("**** WRITE AND FLUSH AWAIT");
        req.getFuture().get();
        System.err.println("**** WRITE AND FLUSH COMPLETE");
    }

    void enableEvents(Characteristic characteristic) throws ExecutionException, InterruptedException {
        CharacteristicEventsRequest req = new CharacteristicEventsRequest(characteristic, true);
        this.channel.pipeline().writeAndFlush(req);
        req.getFuture().get();
    }

    void disableEvents(Characteristic characteristic) throws ExecutionException, InterruptedException {
        CharacteristicEventsRequest req = new CharacteristicEventsRequest(characteristic, false);
        this.channel.pipeline().writeAndFlush(req);
        req.getFuture().get();
    }

    private final Channel channel;

    private AtomicReference<Accessories> accessories = new AtomicReference<>();
}
