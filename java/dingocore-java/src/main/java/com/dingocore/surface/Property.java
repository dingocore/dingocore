package com.dingocore.surface;

public interface Property<T> {
    Service getService();
    T getValue();
    void setValue(T value);
}
