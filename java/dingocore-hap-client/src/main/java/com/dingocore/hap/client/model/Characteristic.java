package com.dingocore.hap.client.model;

/**
 * Created by bob on 9/13/18.
 */
public interface Characteristic {

    Service getService();
    int getIID();
    CharacteristicType getType();
    void updateValue(Object value);
    Object getValue();
    <T> T getValue(Class<T> asType);

    boolean isWritable();

}
