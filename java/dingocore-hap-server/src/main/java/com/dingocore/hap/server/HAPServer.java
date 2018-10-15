package com.dingocore.hap.server;

import java.net.InetSocketAddress;

import com.dingocore.hap.server.auth.ServerAuthStorage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by bob on 8/29/18.
 */
public class HAPServer {

    public HAPServer(ServerAuthStorage authStorage) {
        this.authStorage = authStorage;
    }

    public void start(InetSocketAddress bind) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap(); // (1)
        b.group(workerGroup); // (2)
        b.channel(NioServerSocketChannel.class); // (3)
        //b.option(ChannelOption.TCP_NODELAY, true); // (4)
        b.childHandler(new ServerInitializer(this.authStorage));

        this.channel = b.bind(bind.getAddress(), bind.getPort()).sync().channel();
    }

    private final ServerAuthStorage authStorage;

    private Channel channel;
}
