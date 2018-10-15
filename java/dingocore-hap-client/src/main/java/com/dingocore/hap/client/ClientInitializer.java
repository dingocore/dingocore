package com.dingocore.hap.client;

import com.dingocore.hap.client.codec.CharacteristicEventsRequestEncoder;
import com.dingocore.hap.client.codec.ClientPairSetupHandler;
import com.dingocore.hap.client.codec.ClientPairSetupManager;
import com.dingocore.hap.client.codec.ClientPairVerifyHandler;
import com.dingocore.hap.client.codec.ClientPairVerifyManager;
import com.dingocore.hap.client.codec.EventDecodingHandler;
import com.dingocore.hap.client.codec.UpdateCharacteristicRequestEncoder;
import com.dingocore.hap.common.codec.DebugHandler;
import com.dingocore.hap.common.codec.EncryptableMessageHandler;
import com.dingocore.hap.common.codec.SessionCryptoHandler;
import com.dingocore.hap.common.codec.tlv.TLVDecoder;
import com.dingocore.hap.common.codec.tlv.TLVRequestEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

/**
 * Created by bob on 8/27/18.
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel> {
    public ClientInitializer(ClientPairSetupManager pairSetupManager, ClientPairVerifyManager pairVerifyManager) {
        this.pairSetupManager = pairSetupManager;
        this.pairVerifyManager = pairVerifyManager;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //ch.pipeline().addLast(new DebugHandler("client-head"));
        ch.pipeline().addLast(new SessionCryptoHandler());
        ch.pipeline().addLast(new DebugHandler("client-http-after-encoded"));
        ch.pipeline().addLast(new HttpRequestEncoder());
        //ch.pipeline().addLast(new DebugHandler("client-http-before-encoded"));
        ch.pipeline().addLast(new HttpResponseDecoder());
        ch.pipeline().addLast(new EncryptableMessageHandler());
        ch.pipeline().addLast(new HttpObjectAggregator(4096));
        //ch.pipeline().addLast(new DebugHandler("client-http"));
        ch.pipeline().addLast(new EventDecodingHandler());

        ch.pipeline().addLast(new TLVRequestEncoder());
        ch.pipeline().addLast(new TLVDecoder());

        ch.pipeline().addLast("Pair-Setup", new ClientPairSetupHandler(this.pairSetupManager));
        ch.pipeline().addLast("Pair-Verify", new ClientPairVerifyHandler(this.pairVerifyManager));

        ch.pipeline().addLast(new UpdateCharacteristicRequestEncoder());
        ch.pipeline().addLast(new CharacteristicEventsRequestEncoder());

        ch.pipeline().addLast(new SyncRequestCompleter());
    }

    private final ClientPairVerifyManager pairVerifyManager;

    private final ClientPairSetupManager pairSetupManager;
}
