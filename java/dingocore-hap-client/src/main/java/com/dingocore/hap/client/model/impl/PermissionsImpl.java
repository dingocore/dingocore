package com.dingocore.hap.client.model.impl;

import java.util.EnumSet;
import java.util.Set;

import com.dingocore.hap.client.model.Permission;
import com.dingocore.hap.client.model.Permissions;

/**
 * Created by bob on 9/14/18.
 */
public class PermissionsImpl implements Permissions {

    public PermissionsImpl() {

    }

    public PermissionsImpl(Set<Permission> permissions) {
        this.permissions.addAll(permissions);
    }

    @Override
    public boolean contains(Permission permission) {
        return this.permissions.contains(permission);
    }

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
    }

    @Override
    public String toString() {
        return "[Permissions: " + this.permissions + "]";
    }

    private EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);
}
