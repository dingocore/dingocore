package com.dingocore.hap.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

import com.dingocore.hap.client.auth.ClientAuthStorage;
import com.dingocore.hap.client.impl.ConnectionImpl;
import com.dingocore.hap.client.impl.PairedConnectionImpl;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.AttributeKey;


/**
 * Created by bob on 8/27/18.
 */
public class SimpleClient implements Closeable  {

    public static AttributeKey<SimpleClient> CLIENT = AttributeKey.valueOf("hap.client");

    public static AttributeKey<ConnectionImpl> CONNECTION = AttributeKey.valueOf("hap.connection");

    public static AttributeKey<PairedConnectionImpl> PAIRED_CONNECTION = AttributeKey.valueOf("hap.paired-connection");

    public SimpleClient(ClientAuthStorage authStorage) {
        this(authStorage, new NioEventLoopGroup());
    }

    public SimpleClient(ClientAuthStorage authStorage, NioEventLoopGroup workerGroup) {
        this.authStorage = authStorage;
        this.workerGroup = workerGroup;
    }

    public NioEventLoopGroup getWorkerGroup() {
        return this.workerGroup;
    }

    public Connection connect(String hostname, int port) throws InterruptedException, ExecutionException {
        return connect(new InetSocketAddress(hostname, port));
    }

    public Connection connect(InetSocketAddress socketAddress) throws InterruptedException, ExecutionException {
        return new ConnectionImpl(this, socketAddress, this.authStorage).connect().get();
    }

    public Connection connect(String id) throws IOException, ExecutionException, InterruptedException {
        return connect(Discovery.discover(id));
    }

    @Override
    public void close() throws IOException {
        try {
            this.workerGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private final ClientAuthStorage authStorage;

    private final NioEventLoopGroup workerGroup;
}
