package com.dingocore.hap.common.codec.tlv;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Created by bob on 8/28/18.
 */
public class TLVRequestEncoder extends MessageToMessageEncoder<TLV> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TLV tlv, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        tlv.encodeInto(buf);

        HttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, tlv.getAttachment(TLV.HTTP_METHOD), tlv.getAttachment(TLV.HTTP_REQUEST_PATH), buf);
        request.headers().set(HttpHeaderNames.CONTENT_LENGTH, buf.readableBytes());
        request.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/pairing+tlv8");
        request.headers().set(HttpHeaderNames.HOST, "192.168.1.147:80");

        out.add(request);
    }
}
