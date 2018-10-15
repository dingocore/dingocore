package com.dingocore.hap.client.codec;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
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

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.json.Json.createArrayBuilder;
import static javax.json.Json.createObjectBuilder;

public class UpdateCharacteristicRequestEncoder extends MessageToMessageEncoder<UpdateCharacteristicRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, UpdateCharacteristicRequest msg, List<Object> out) throws Exception {
        Characteristic characteristic = msg.getCharacteristic();

        JsonObject object = createObjectBuilder()
                .add("characteristics",
                     createArrayBuilder()
                             .add(createCharacteristicJSON(characteristic, msg.getValue())
                             ).build()
                ).build();

        ByteBuf buf = ctx.alloc().buffer();

        try (ByteBufOutputStream stream = new ByteBufOutputStream(buf)) {
            try (JsonWriter writer = Json.createWriter(stream)) {
                writer.write(object);
            }
        }


        DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.PUT, "/characteristics", buf);
        httpRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/hap+json");
        httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        System.err.println( "UPDATE WITH: " + httpRequest.content() + " // " + httpRequest.content().toString(UTF_8));
        out.add(httpRequest);
    }

    private static JsonObject createCharacteristicJSON(Characteristic characteristic, Object value) {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        builder.add("aid", characteristic.getService().getAccessory().getAID());
        builder.add("iid", characteristic.getIID());
        if (value == Boolean.TRUE) {
            builder.add("value", JsonValue.TRUE);
        } else if (value == Boolean.FALSE) {
            builder.add("value", JsonValue.FALSE);
        } else if (value instanceof String) {
            builder.add("value", (String) value);
        } else if ( value instanceof Double ) {
            builder.add("value", (Double) value);
        }

        return builder.build();
    }

}
