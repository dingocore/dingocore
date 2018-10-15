package com.dingocore.hap.common.codec;

import java.nio.charset.Charset;

import com.dingocore.hap.common.codec.tlv.TLV;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.HttpContent;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by bob on 8/27/18.
 */
public class DebugHandler extends ChannelDuplexHandler {

    public DebugHandler(String tag) {
        this.tag = tag;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println( this.tag + " :READ: " + msg );
        if ( msg instanceof ByteBuf ) {
            System.err.println(this.tag + " READ --> ");
            System.err.println(((ByteBuf) msg).toString(UTF_8));
            System.err.println("<--- READ");
        } else if ( msg instanceof TLV) {
            System.err.println(this.tag + " READ --> ");
            System.err.println( msg );
            System.err.println("<--- READ");
        } else {
            System.err.println( this.tag + " :READ: " + msg );
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.err.println( this.tag + " channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.err.println( this.tag + " :WRITE: " + msg );
        try {
            if ( msg instanceof ByteBuf ) {
                System.err.println(((ByteBuf) msg).toString(Charset.forName("UTF8")));
            } else if ( msg instanceof HttpContent ) {
                System.err.println( this.tag + " :WRITE-HTTP: " + ((HttpContent) msg).content().toString(UTF_8));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        //promise.addListener((data)->{
            //System.err.println( "promise: " + data.get());
//
        //});
        super.write(ctx, msg, promise);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.err.println( this.tag + "Exception caught: " + cause );
        super.exceptionCaught(ctx, cause);
    }

    private final String tag;
}
