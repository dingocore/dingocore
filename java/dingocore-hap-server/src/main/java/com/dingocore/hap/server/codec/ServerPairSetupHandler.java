package com.dingocore.hap.server.codec;

import java.util.Optional;

import com.dingocore.hap.common.codec.tlv.TLV;
import com.dingocore.hap.common.codec.tlv.Type;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by bob on 8/29/18.
 */
public class ServerPairSetupHandler extends ChannelInboundHandlerAdapter {

    public ServerPairSetupHandler(ServerPairSetupManager manager) {
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

        if (!path.equals("/pair-setup")) {
            super.channelRead(ctx, msg);
            return;
        }
        TLV result = this.manager.handle(tlv);
        if (result != null) {
            Optional<Integer> err = Type.ERROR.get(result);
            if ( err.isPresent() ) {
                this.manager.reset();
            }
            //Optional<Integer> state = Type.STATE.get(tlv);
            //if ( state.isPresent() && state.get() == 6 ) {
                // success
                //ctx.write(this.manager.getSessionKeys());
            //}
            ctx.pipeline().writeAndFlush(result);
        }
    }

    private final ServerPairSetupManager manager;
}
