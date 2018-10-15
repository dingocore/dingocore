package com.dingocore.hap.client.codec;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.dingocore.hap.client.SimpleClient;
import com.dingocore.hap.client.PairedConnection;
import com.dingocore.hap.client.impl.PairedConnectionImpl;
import com.dingocore.hap.common.codec.tlv.TLV;
import com.dingocore.hap.common.codec.tlv.TLVError;
import com.dingocore.hap.common.codec.tlv.Type;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Created by bob on 8/28/18.
 */
public class ClientPairVerifyHandler extends ChannelDuplexHandler {

    public ClientPairVerifyHandler(ClientPairVerifyManager manager) {
        this.manager = manager;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if ( msg instanceof VerifyStartRequest) {
            TLV tlv = this.manager.startRequest((VerifyStartRequest) msg);
            tlv.addAttachment(TLV.HTTP_METHOD, HttpMethod.POST);
            tlv.addAttachment(TLV.HTTP_REQUEST_PATH, "/pair-verify");
            this.future = ((VerifyStartRequest) msg).getFuture();
            super.write(ctx, tlv, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof TLV)) {
            super.channelRead(ctx, msg);
            return;
        }
        if (!this.manager.isActive()) {
            super.channelRead(ctx, msg);
            return;
        }

        TLV tlv = (TLV) msg;
        Optional<Integer> err = Type.ERROR.get(tlv);
        if ( err.isPresent() ) {
            this.future.completeExceptionally(TLVError.lookup(err.get()));
            this.manager.reset();
            this.future = null;
            return;
        }

        TLV result = this.manager.handle(tlv);
        if ( result == null ) {
            // successful completion
            ctx.pipeline().write(this.manager.getSessionKeys().forClient());
            PairedConnectionImpl pairedConnection = new PairedConnectionImpl(ctx.channel());
            ctx.channel().attr(SimpleClient.PAIRED_CONNECTION).set(pairedConnection);
            this.future.complete(pairedConnection);
            this.future = null;
        } else {
            result.addAttachment(TLV.HTTP_METHOD, HttpMethod.POST);
            result.addAttachment(TLV.HTTP_REQUEST_PATH, "/pair-verify");
            ctx.pipeline().writeAndFlush(result);
            err = Type.ERROR.get(result);
            if ( err.isPresent() ) {
                this.future.completeExceptionally(TLVError.lookup( err.get() ) );
                this.future = null;
            }
        }
    }

    private final ClientPairVerifyManager manager;

    private CompletableFuture<PairedConnection> future;
}
