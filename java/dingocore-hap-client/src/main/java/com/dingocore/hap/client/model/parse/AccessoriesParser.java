package com.dingocore.hap.client.model.parse;

import java.io.IOException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;

import com.dingocore.hap.client.impl.AccessoriesImpl;
import com.dingocore.hap.client.impl.AccessoryImpl;
import com.dingocore.hap.client.impl.CharacteristicImpl;
import com.dingocore.hap.client.impl.EventableCharacteristicImpl;
import com.dingocore.hap.client.impl.ServiceImpl;
import com.dingocore.hap.client.model.Service;
import com.dingocore.hap.client.model.ServiceType;
import com.dingocore.hap.client.model.Services;
import com.dingocore.hap.client.impl.PairedConnectionImpl;
import com.dingocore.hap.client.model.Accessory;
import com.dingocore.hap.client.model.Characteristic;
import com.dingocore.hap.client.model.CharacteristicType;
import com.dingocore.hap.client.model.Characteristics;
import com.dingocore.hap.client.model.Format;
import com.dingocore.hap.client.model.Permission;
import com.dingocore.hap.client.model.Permissions;
import com.dingocore.hap.client.model.impl.PermissionsImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;

public class AccessoriesParser {

    public AccessoriesParser(PairedConnectionImpl pairedConnection) {
        this.pairedConnection = pairedConnection;
    }

    public AccessoriesImpl parse(ByteBuf buf) throws IOException {

        AccessoriesImpl accessories = new AccessoriesImpl(this.pairedConnection);
        try (InputStream in = new ByteBufInputStream(buf)) {
            JsonReader reader = Json.createReader(in);
            JsonObject object = reader.readObject();

            JsonArray accessoriesJson = object.getJsonArray("accessories");

            accessoriesJson.forEach(e -> {
                accessories.addAccessory(parseAccessory(accessories, (JsonObject) e));
            });
        }

        return accessories;
    }

    protected Accessory parseAccessory(AccessoriesImpl accessories, JsonObject json) {
        int aid = json.getInt("aid");
        AccessoryImpl accessory = new AccessoryImpl(accessories, aid);
        json.getJsonArray("services").forEach(e -> {
            accessory.addService(parseService(accessory, (JsonObject) e));
        });
        return accessory;
    }

    protected Service parseService(AccessoryImpl accessory, JsonObject json) {
        int iid = json.getInt("iid");
        String type = json.getString("type");
        ServiceType serviceType = Services.lookup(type);
        ServiceImpl service = new ServiceImpl(accessory, iid, serviceType);
        json.getJsonArray("characteristics").forEach(e -> {
            service.addCharacteristic(parseCharacteristic(service, (JsonObject) e));
        });

        return service;
    }

    protected Characteristic parseCharacteristic(ServiceImpl service, JsonObject json) {
        int iid = json.getInt("iid");
        String type = json.getString("type");

        CharacteristicType characteristicType = Characteristics.lookup(type);
        Permissions permissions = parsePermissions(json.getJsonArray("perms"));

        CharacteristicImpl characteristic = null;

        if (permissions.contains(Permission.NOTIFY)) {
            characteristic = new EventableCharacteristicImpl(service, iid, characteristicType);
        } else {
            characteristic = new CharacteristicImpl(service, iid, characteristicType);
        }

        characteristic.setPermissions(parsePermissions(json.getJsonArray("perms")));

        JsonValue value = json.get("value");
        if (value != null) {
            JsonValue.ValueType valueType = value.getValueType();

            switch (valueType) {
                case ARRAY:
                    break;
                case OBJECT:
                    break;
                case STRING:
                    characteristic.setStoredValue(json.getString("value"));
                    break;
                case NUMBER:
                    if (characteristicType.getFormat() == Format.FLOAT) {
                        characteristic.setStoredValue(json.getJsonNumber("value").doubleValue());
                    } else {
                        characteristic.setStoredValue(json.getJsonNumber("value").longValue());
                    }
                    break;
                case TRUE:
                    characteristic.setStoredValue(Boolean.TRUE);
                    break;
                case FALSE:
                    characteristic.setStoredValue(Boolean.FALSE);
                    break;
                case NULL:
                    break;
            }
        }

        return characteristic;
    }

    private Permissions parsePermissions(JsonArray json) {
        PermissionsImpl permissions = new PermissionsImpl();
        json.forEach(e -> {
            permissions.addPermission(parsePermission((JsonString) e));
        });
        return permissions;
    }

    private Permission parsePermission(JsonString json) {
        String str = json.getString();
        if (str.equals("pr")) {
            return Permission.PAIRED_READ;
        }
        if (str.equals("pw")) {
            return Permission.PAIRED_WRITE;
        }
        if (str.equals("ev")) {
            return Permission.NOTIFY;
        }
        return null;
    }

    private final PairedConnectionImpl pairedConnection;
}
