package com.dingocore.surface;

import java.util.Set;

public interface Endpoint {
    String getIdentifier();
    Set<Service> getServices();
}
