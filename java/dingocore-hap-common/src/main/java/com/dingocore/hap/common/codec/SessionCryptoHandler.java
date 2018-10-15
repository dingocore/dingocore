package com.dingocore.hap.common.codec;

import java.util.List;

import com.dingocore.hap.common.codec.pair.SessionKeys;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by bob on 8/30/18.
 */
public class SessionCryptoHandler extends ChannelDuplexHandler {


    public SessionCryptoHandler() {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (this.keys != null && msg instanceof ByteBuf) {
            ByteBuf original = (ByteBuf) msg;
            List<ByteBuf> decrypted = this.keys.decrypt(original);
            for (ByteBuf e : decrypted) {
                super.channelRead(ctx, e);
            }
            ;
        } else {
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof SessionKeys) {
            this.keys = (SessionKeys) msg;
            return;
        } else if (this.keys != null && msg instanceof ByteBuf) {
            ByteBuf original = (ByteBuf) msg;
            System.err.println("adding encrypting " + original + "// " + original.toString(UTF_8));
            this.keys.encrypt(original);
            //System.err.println("encrypted " + original + "// " + original.toString(UTF_8) + " to " + encrypted.readableBytes());
            //super.write(ctx, encrypted, promise);
            return;
        } else if (this.keys != null && msg instanceof EncryptableMessageComplete) {
            System.err.println("doing encryption");
            List<ByteBuf> result = this.keys.doEncrypt();
            int numChunks = result.size();
            for (int i = 0; i < numChunks; ++i) {
                if (i + 1 == numChunks) {
                    System.err.println( "forward promise: " + promise );
                    super.write(ctx, result.get(i), promise);
                } else {
                    System.err.println( "forward void promise");
                    super.write(ctx, result.get(i), ctx.voidPromise());
                }
            }
            return;
        }
        super.write(ctx, msg, promise);
    }

    private SessionKeys keys;
}
