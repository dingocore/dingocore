package com.dingocore.hap.common.codec;

import com.dingocore.hap.common.codec.pair.SessionKeys;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.LastHttpContent;

public class EncryptableMessageHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.err.println("forward message " + msg + " // " + (msg instanceof LastHttpContent ) );

        super.write(ctx, msg, promise);

        if (msg instanceof SessionKeys) {
            System.err.println( "enabling encryption handling");
            this.enabled = true;
        }

        if (msg instanceof LastHttpContent) {
            System.err.println( "sending encryption complete");
            super.write(ctx, new EncryptableMessageComplete(), ctx.voidPromise());

        }
    }

    private boolean enabled = false;
}
