package com.dingocore.surface;

public interface Surface {

    void endpointAdded(Endpoint endpoint);
    void endpointRemoved(Endpoint endpoint);

    void serviceAdded(Service service);
    void serviceRemoved(Service service);

    void propertyAdded(BooleanProperty property);
    void propertyValueChanged(BooleanProperty property);

    void propertyAdded(FloatProperty property);
    void propertyValueChanged(FloatProperty property);

    void propertyRemoved(Property property);

}
