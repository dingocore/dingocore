package com.dingocore.surface;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultSurface implements Surface {

    @Override
    public void endpointAdded(Endpoint endpoint) {
        this.endpoints.put(endpoint.getIdentifier(), endpoint);
    }

    @Override
    public void endpointRemoved(Endpoint endpoint) {
        this.endpoints.remove(endpoint.getIdentifier());
    }

    protected Endpoint getEndpoint(String identifier) {
        return this.endpoints.get(identifier);
    }

    protected Collection<Endpoint> getEndpoints() {
        return Collections.unmodifiableCollection(this.endpoints.values());
    }

    @Override
    public void serviceAdded(Service service) {
        // no-op
    }

    @Override
    public void serviceRemoved(Service service) {
        // no-op
    }

    @Override
    public void propertyAdded(BooleanProperty property) {

    }

    @Override
    public void propertyValueChanged(BooleanProperty property) {

    }

    @Override
    public void propertyAdded(FloatProperty property) {

    }

    @Override
    public void propertyValueChanged(FloatProperty property) {

    }

    @Override
    public void propertyRemoved(Property property) {
        // no-op
    }

    private Map<String, Endpoint> endpoints = new HashMap<>();
}
