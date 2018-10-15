package com.dingocore.hap.client;

import java.util.concurrent.CompletableFuture;

import com.dingocore.hap.client.codec.SyncRequest;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;

public class SyncRequestCompleter extends ChannelDuplexHandler {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if ( msg instanceof SyncRequest ) {
            System.err.println( "**** STASH FUTURE");
            this.future = ((SyncRequest) msg).getFuture();
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if ( msg instanceof FullHttpResponse ) {
            if ( this.future != null ) {
                System.err.println( "**** COMPLETE FUTURE");
                this.future.complete(Boolean.TRUE);
                this.future = null;
            }
        }
        super.channelRead(ctx, msg);
    }

    private CompletableFuture<Object> future;
}
