package com.dingocore.hap.server.codec;

import java.util.Optional;

import com.dingocore.hap.common.codec.tlv.TLV;
import com.dingocore.hap.common.codec.tlv.Type;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by bob on 8/29/18.
 */
public class ServerPairVerifyHandler extends ChannelInboundHandlerAdapter {

    public ServerPairVerifyHandler(ServerPairVerifyManager manager) {
        this.manager = manager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof TLV)) {
            super.channelRead(ctx, msg);
            return;
        }

        TLV tlv = (TLV) msg;
        String path = tlv.getAttachment(TLV.HTTP_REQUEST_PATH);
        if (path == null) {
            super.channelRead(ctx, msg);
            return;
        }

        if (!path.equals("/pair-verify")) {
            super.channelRead(ctx, msg);
            return;
        }
        TLV result = this.manager.handle(tlv);
        if (result != null) {
            Optional<Integer> err = Type.ERROR.get(result);
            if ( err.isPresent() ) {
                this.manager.reset();
            }
            ctx.pipeline().write(result);
            Type.STATE.get(result).ifPresent( (state)->{
                if ( state == 4 ) {
                    ctx.pipeline().write( this.manager.getSessionKeys().forServer() );
                }
            });
            ctx.pipeline().flush();
        }
    }

    private final ServerPairVerifyManager manager;
}
