package com.dingocore.hap.client;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

class Discovery implements ServiceListener {

    static InetSocketAddress discover(String id) throws IOException, InterruptedException {
        return discover(id, 6, TimeUnit.SECONDS);
    }

    static InetSocketAddress discover(String id, long timeout, TimeUnit timeUnit) throws IOException, InterruptedException {
        return new Discovery(id).getSocketAddress(timeout, timeUnit);
    }

    Discovery(String id) throws IOException {
        this.id = id;
        this.latch = new CountDownLatch(1);
        InetAddress localAddr = InetAddress.getLocalHost();
        this.mdns = JmDNS.create(localAddr);
        System.err.println("--> " + this.mdns);
        mdns.addServiceListener("_hap._tcp.local.", this);
    }

    @Override
    public void serviceAdded(ServiceEvent event) {
        //System.err.println("service added: " + event);
    }

    @Override
    public void serviceRemoved(ServiceEvent event) {
        //System.err.println("service removed: " + event);
    }

    @Override
    public void serviceResolved(ServiceEvent event) {
        ServiceInfo info = event.getInfo();
        //System.err.println("service resolved: " + info);
        System.err.println("compare: " + info.getPropertyString("id") + " vs " + this.id);
        if (!info.getPropertyString("id").equals(this.id)) {
            return;
        }
        Inet4Address[] addresses = info.getInet4Addresses();
        int port = info.getPort();
        synchronized (this.sync) {
            this.socketAddress = new InetSocketAddress(addresses[0], port);
            this.latch.countDown();
        }
    }

    InetSocketAddress getSocketAddress(long timeout, TimeUnit timeUnit) throws InterruptedException, IOException {
        try {
            this.latch.await(timeout, timeUnit);
            synchronized (this.sync) {
                if (this.socketAddress != null) {
                    return this.socketAddress;
                }
            }

            throw new UnknownHostException(this.id);
        } finally {
            this.mdns.close();
        }
    }


    private final CountDownLatch latch;

    private final String id;

    private final JmDNS mdns;

    private InetSocketAddress socketAddress;

    private Object sync = new Object();
}
