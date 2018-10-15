package com.dingocore.hap.client.codec;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import com.dingocore.hap.client.model.Characteristic;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import static javax.json.Json.createArrayBuilder;
import static javax.json.Json.createObjectBuilder;

public class CharacteristicEventsRequestEncoder extends MessageToMessageEncoder<CharacteristicEventsRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, CharacteristicEventsRequest msg, List<Object> out) throws Exception {
        Characteristic characteristic = msg.getCharacteristic();

        JsonObject object = createObjectBuilder()
                .add("characteristics",
                     createArrayBuilder()
                             .add(createCharacteristicJSON(characteristic, msg.getValue())
                             ).build()
                ).build();

        ByteBuf buf = ctx.alloc().buffer();

        try (ByteBufOutputStream stream = new ByteBufOutputStream(buf)) {
            JsonWriter writer = Json.createWriter(stream);
            writer.write(object);
            writer.close();
            stream.flush();
        }

        buf.retain();

        DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/characteristics", buf);
        httpRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/hap+json");
        httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());

        out.add(httpRequest);
    }

    private static JsonObject createCharacteristicJSON(Characteristic characteristic, boolean value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add("aid", characteristic.getService().getAccessory().getAID());
        builder.add("iid", characteristic.getIID());
        builder.add("ev", value);

        return builder.build();
    }
}
