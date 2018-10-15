package com.dingocore.hap.client.codec;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.dingocore.hap.client.PairedConnection;
import com.dingocore.hap.common.codec.tlv.TLV;
import com.dingocore.hap.common.codec.tlv.TLVError;
import com.dingocore.hap.common.codec.tlv.Type;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Created by bob on 8/28/18.
 */
public class ClientPairSetupHandler extends ChannelDuplexHandler {

    public ClientPairSetupHandler(ClientPairSetupManager manager) {
        this.manager = manager;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if ( msg instanceof SRPStartRequest ) {
            this.future = ((SRPStartRequest) msg).getFuture();
            TLV tlv = this.manager.startRequest((SRPStartRequest) msg);

            tlv.addAttachment(TLV.HTTP_METHOD, HttpMethod.POST);
            tlv.addAttachment(TLV.HTTP_REQUEST_PATH, "/pair-setup");
            super.write(ctx, tlv, promise);
        } else {
            super.write(ctx, msg, promise);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof HttpResponse) {
            HttpResponseStatus status = ((HttpResponse) msg).status();
            if ( this.manager.isActive() ) {
                this.future.completeExceptionally(new Exception( status.toString()));
                return;
            }

            super.channelRead(ctx, msg);
            return;
        }

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
            return;
        }

        TLV result = this.manager.handle(tlv);
        if ( result == null ) {
            // successful completion
            //ctx.pipeline().write(this.manager.getSessionKeys());
            this.manager.reset();
            ctx.pipeline().writeAndFlush( new VerifyStartRequest(this.future));
            //this.manager.getFuture().complete(new PairedConnectionImpl(ctx.channel()));
        } else {
            result.addAttachment(TLV.HTTP_METHOD, HttpMethod.POST);
            result.addAttachment(TLV.HTTP_REQUEST_PATH, "/pair-setup");
            ctx.pipeline().writeAndFlush(result);
            err = Type.ERROR.get(result);
            if ( err.isPresent() ) {
                this.future.completeExceptionally(TLVError.lookup( err.get() ) );
            }
        }
    }

    private final ClientPairSetupManager manager;

    private CompletableFuture<PairedConnection> future;
}
