package com.dingocore;

import com.dingocore.endpoint.Endpoint;
import com.dingocore.surface.Surface;

public interface Dingo {

    static Dingo connect(String identifier, String host, int port) {
        return new DefaultDingo(identifier, host, port);
    }

    String getIdentifier();

    Endpoint addEndpoint(String identifier);

    void addSurface(Surface surface);

    void ready();
}
