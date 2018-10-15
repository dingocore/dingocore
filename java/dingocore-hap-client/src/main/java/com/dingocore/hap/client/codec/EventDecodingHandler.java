package com.dingocore.hap.client.codec;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.dingocore.hap.client.SimpleClient;
import com.dingocore.hap.client.impl.EventableCharacteristicImpl;
import com.dingocore.hap.client.impl.PairedConnectionImpl;
import com.dingocore.hap.client.model.Accessories;
import com.dingocore.hap.client.model.Format;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;

public class EventDecodingHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            if (response.protocolVersion().protocolName().equals("EVENT")) {
                PairedConnectionImpl pairedConn = ctx.channel().attr(SimpleClient.PAIRED_CONNECTION).get();
                Accessories db = pairedConn.accessories();
                if (db == null) {
                    return;
                }
                try (ByteBufInputStream in = new ByteBufInputStream(response.content())) {
                    JsonReader reader = Json.createReader(in);
                    JsonObject rootJson = reader.readObject();
                    JsonArray array = rootJson.getJsonArray("characteristics");
                    array.forEach(e -> {
                        JsonObject chrJson = (JsonObject) e;
                        int aid = chrJson.getInt("aid");
                        db.find(aid).ifPresent(acc -> {
                            int iid = chrJson.getInt("iid");
                            acc.findCharacteristic(iid)
                                    .ifPresent(characteristic -> {
                                        if (characteristic instanceof EventableCharacteristicImpl) {
                                            JsonValue value = chrJson.get("value");
                                            if (value != null) {
                                                JsonValue.ValueType valueType = value.getValueType();

                                                switch (valueType) {
                                                    case ARRAY:
                                                        break;
                                                    case OBJECT:
                                                        break;
                                                    case STRING:
                                                        ((EventableCharacteristicImpl) characteristic).fireEvent(chrJson.getString("value"));
                                                        break;
                                                    case NUMBER:
                                                        if (characteristic.getType().getFormat() == Format.FLOAT) {
                                                            ((EventableCharacteristicImpl) characteristic).fireEvent(chrJson.getJsonNumber("value").doubleValue());
                                                        } else {
                                                            ((EventableCharacteristicImpl) characteristic).fireEvent(chrJson.getJsonNumber("value").longValue());
                                                        }
                                                        break;
                                                    case TRUE:
                                                        ((EventableCharacteristicImpl) characteristic).fireEvent(Boolean.TRUE);
                                                        break;
                                                    case FALSE:
                                                        ((EventableCharacteristicImpl) characteristic).fireEvent(Boolean.FALSE);
                                                        break;
                                                    case NULL:
                                                        break;
                                                }
                                            }
                                        }
                                    });

                        });
                    });
                }
            }
        }
        super.channelRead(ctx, msg);
    }
}
