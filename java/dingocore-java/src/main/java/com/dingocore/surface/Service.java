package com.dingocore.surface;

import java.util.Set;

public interface Service {
    Endpoint getEndpoint();
    Set<Property> getProperties();
}
