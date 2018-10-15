package com.dingocore.hap.common.codec.tlv;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Created by bob on 8/28/18.
 */
public class TLVResponseEncoder extends MessageToMessageEncoder<TLV> {

    @Override
    protected void encode(ChannelHandlerContext ctx, TLV tlv, List<Object> out) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        tlv.encodeInto(buf);
        HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf, false);
        response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, buf.readableBytes());
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "application/pairing+tlv8");
        out.add(response);
    }
}
