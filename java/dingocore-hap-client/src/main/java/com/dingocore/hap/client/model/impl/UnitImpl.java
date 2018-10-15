package com.dingocore.hap.client.model.impl;

import com.dingocore.hap.client.model.Unit;

/**
 * Created by bob on 9/14/18.
 */
public class UnitImpl implements Unit {
    public UnitImpl(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return this.name;
    }

    private final String name;
}
