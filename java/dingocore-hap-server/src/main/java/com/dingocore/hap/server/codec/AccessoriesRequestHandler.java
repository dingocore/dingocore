package com.dingocore.hap.server.codec;

import java.nio.charset.StandardCharsets;

import com.dingocore.hap.common.util.ByteUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

/**
 * Created by bob on 9/11/18.
 */
public class AccessoriesRequestHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof FullHttpRequest)) {
            super.channelRead(ctx, msg);
            return;
        }

        String uri = ((FullHttpRequest) msg).uri();

        if (!uri.equals("/accessories")) {
            super.channelRead(ctx, msg);
            return;
        }

        ByteBuf content = ctx.alloc().buffer();
        byte[] bytes = "{\"accessories\": []}".getBytes(StandardCharsets.UTF_8);
        System.err.println( "** RESPONSE plaintext: " + ByteUtil.toString(bytes));
        content.writeBytes(bytes);

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
        ctx.pipeline().writeAndFlush(response);
    }
}
