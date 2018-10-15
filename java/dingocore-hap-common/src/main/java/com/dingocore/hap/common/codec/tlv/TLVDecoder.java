package com.dingocore.hap.common.codec.tlv;

import java.util.List;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by bob on 8/28/18.
 */
public class TLVDecoder extends MessageToMessageDecoder<FullHttpMessage> {
    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpMessage msg, List<Object> out) throws Exception {
        String contentType = msg.headers().get(HttpHeaderNames.CONTENT_TYPE);
        if ( contentType == null || ! contentType.equals( "application/pairing+tlv8")) {
            ReferenceCountUtil.retain(msg);
            out.add( msg );
            return;
        }
        TLV tlv = TLV.decodeFrom(msg.content());
        if ( msg instanceof FullHttpRequest ) {
            tlv.addAttachment(TLV.HTTP_REQUEST, (FullHttpRequest) msg);
            tlv.addAttachment(TLV.HTTP_REQUEST_PATH, ((FullHttpRequest) msg).getUri());
        } else if ( msg instanceof FullHttpResponse ) {
            tlv.addAttachment(TLV.HTTP_RESPONSE, (FullHttpResponse) msg);
        }
        out.add( tlv );
    }
}
