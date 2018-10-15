package com.dingocore.hap.client.model;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

import com.dingocore.hap.client.model.impl.ServiceTypeImpl;

public class ServiceTypeBuilder {

    public static ServiceType configure(String name, Consumer<ServiceTypeBuilder> config) {
        ServiceTypeBuilder builder = new ServiceTypeBuilder(name);
        config.accept(builder);
        return builder.build();
    }

    public ServiceTypeBuilder(String name) {
        this.name = name;
    }

    public ServiceTypeBuilder type(String type) {
        if (type.startsWith( "public.hap.service") ) {
            this.type = type;
        } else {
            this.type = "public.hap.service." + type;
        }
        return this;
    }

    public ServiceTypeBuilder uuid(String uuid) {
        this.uuid = toUUID(uuid);
        return this;
    }

    public ServiceTypeBuilder required(CharacteristicType characteristic) {
        this.requiredCharacteristics.add( characteristic );
        return this;
    }

    public ServiceTypeBuilder optional(CharacteristicType characteristic) {
        this.optionalCharacteristics.add( characteristic );
        return this;
    }

    static UUID toUUID(String uuid) {
        if ( uuid.length() == 2 ) {
            return UUID.fromString("000000" + uuid + "-0000-1000-8000-0026BB765291");
        } else {
            return UUID.fromString(uuid);
        }
    }

    protected ServiceType build() {
        return new ServiceTypeImpl(this.name,
                                   this.uuid,
                                   this.type,
                                   this.requiredCharacteristics,
                                   this.optionalCharacteristics);
    }

    private final String name;

    private UUID uuid;

    private Set<CharacteristicType> requiredCharacteristics = new HashSet<>();
    private Set<CharacteristicType> optionalCharacteristics = new HashSet<>();

    private String type;
}
