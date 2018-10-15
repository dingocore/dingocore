package com.dingocore.hap.client.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import com.dingocore.hap.client.impl.CharacteristicTypeImpl;

public class CharacteristicTypeBuilder {



    static CharacteristicType configure(String name, Consumer<CharacteristicTypeBuilder> config) {
        CharacteristicTypeBuilder builder = new CharacteristicTypeBuilder(name);
        config.accept(builder);
        return builder.build();
    }

    private CharacteristicTypeBuilder(String name) {
        this.name = name;
        type(name.toLowerCase().replaceAll("-", " "));
    }

    private CharacteristicType build() {
        return new CharacteristicTypeImpl(this.uuid,
                                          this.type,
                                          this.name,
                                          this.format,
                                          this.unit,
                                          this.minimumValue,
                                          this.maximumValue,
                                          this.stepValue,
                                          this.permissions);
    }

    public CharacteristicTypeBuilder uuid(String uuid) {
        this.uuid = toUUID(uuid);
        return this;
    }

    public CharacteristicTypeBuilder type(String type) {
        if (type.startsWith("public.hap.characteristic.")) {
            this.type = type;
        } else {
            this.type = "public.hap.characteristic." + type;
        }
        return this;
    }

    public CharacteristicTypeBuilder format(Format format) {
        this.format = format;
        return this;
    }

    public CharacteristicTypeBuilder boolFormat() {
        return format(Format.BOOL);
    }

    public CharacteristicTypeBuilder intFormat(int min, int max, int step) {
        return format(Format.INT)
                .minimumValue(min)
                .maximumValue(max)
                .stepValue(step);
    }

    public CharacteristicTypeBuilder uint8Format(int min, int max, int step) {
        return format(Format.UINT8)
                .minimumValue(min)
                .maximumValue(max)
                .stepValue(step);
    }

    public CharacteristicTypeBuilder uint32Format() {
        return format(Format.UINT32);

    }

    public CharacteristicTypeBuilder floatFormat(double min, double max, double step) {
        return format(Format.FLOAT)
                .minimumValue(min)
                .maximumValue(max)
                .stepValue(step);
    }

    public CharacteristicTypeBuilder stringFormat() {
        return format(Format.STRING);
    }

    public CharacteristicTypeBuilder stringFormat(int maximumLength) {
        return format(Format.STRING)
                .maximumLength(maximumLength);
    }

    public CharacteristicTypeBuilder tlv8Format() {
        return format(Format.TLV8);
    }

    public CharacteristicTypeBuilder unit(Unit unit) {
        this.unit = unit;
        return this;
    }

    public CharacteristicTypeBuilder minimumValue(Number minimumValue) {
        this.minimumValue = minimumValue;
        return this;
    }

    public CharacteristicTypeBuilder maximumValue(Number maximumValue) {
        this.maximumValue = maximumValue;
        return this;
    }

    public CharacteristicTypeBuilder stepValue(Number stepValue) {
        this.stepValue = stepValue;
        return this;
    }

    public CharacteristicTypeBuilder permission(Permission permission) {
        this.permissions.add(permission);
        return this;
    }

    public CharacteristicTypeBuilder pairedReadPermission() {
        return permission(Permission.PAIRED_READ);
    }

    public CharacteristicTypeBuilder pairedWritePermission() {
        return permission(Permission.PAIRED_WRITE);
    }

    public CharacteristicTypeBuilder notifyPermission() {
        return permission(Permission.NOTIFY);
    }

    public CharacteristicTypeBuilder valid(Object value, String description) {
        this.validValues.put(value, description);
        return this;
    }

    public CharacteristicTypeBuilder maximumLength(int maximumLength) {
        this.maximumLength = maximumLength;
        return this;
    }

    static UUID toUUID(String uuid) {
        if (uuid.length() < 8 ) {
            int prefixLen = 8 - uuid.length();
            String prefix = "";
            for ( int i = 0 ; i < prefixLen ; ++i ) {
                prefix += "0";
            }
            return UUID.fromString(prefix + uuid + "-0000-1000-8000-0026BB765291");
        } else {
            return UUID.fromString(uuid);
        }
    }

    private final String name;

    private UUID uuid;

    private String type;

    private Format format;

    private Unit unit;

    private Number minimumValue;

    private Number maximumValue;

    private Number stepValue;

    private Set<Permission> permissions = new HashSet<>();

    private Map<Object, String> validValues = new HashMap<>();

    private int maximumLength = -1;
}
