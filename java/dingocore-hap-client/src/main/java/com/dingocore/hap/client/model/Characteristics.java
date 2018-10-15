package com.dingocore.hap.client.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class Characteristics {
    private static final Map<UUID, CharacteristicType> INDEX = new HashMap<>();

    public static CharacteristicType lookup(String uuid) {
        return lookup(CharacteristicTypeBuilder.toUUID(uuid));
    }

    public static CharacteristicType lookup(UUID uuid) {
        return INDEX.get(uuid);
    }

    static CharacteristicType register(String name, Consumer<CharacteristicTypeBuilder> config) {
        CharacteristicType type = CharacteristicTypeBuilder.configure(name, config );
        INDEX.put(type.getUUID(), type);
        return type;
    }

    public static final CharacteristicType ADMINISTRATOR_ONLY_ACCESS = register("Administrator Only Access", c -> c
            .uuid("01")
            .boolFormat()
    );

    public static final CharacteristicType AUDIO_FEEDBACK = register("Audio Feedback", c -> c
            .uuid("05")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .boolFormat()
    );

    public static final CharacteristicType BRIGHTNESS = register("Brightness", c -> c
            .uuid("08")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .intFormat(0, 100, 1)
            .unit(Unit.PERCENTAGE)
    );

    public static final CharacteristicType COOLING_TEMPERATURE_THRESHOLD = register("Cooling Temperature Treshold", c -> c
            .uuid("0D")
            .type("temperature.cooling-threshold")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .floatFormat(10, 35, 0.1)
            .unit(Unit.CELSIUS)
    );

    public static final CharacteristicType CURRENT_DOOR_STATE = register("Current Door State", c -> c
            .uuid("0E")
            .type("door-state.current")
            .pairedReadPermission()
            .notifyPermission()
            .uint8Format(0, 4, 1)
            .valid(0, "Open. The door is fully open.")
            .valid(1, "Closed. The door is fully closed.")
            .valid(2, "Opening. The door actively opening.")
            .valid(3, "Closing. The door is actively closing.")
            .valid(4, "Stopped. The door is not moving, and it is not fully open nor fully closed.")
    );

    public static final CharacteristicType CURRENT_HEATING_COOLING_STATE = register("Current Heating Cooling State", c -> c
            .uuid("0F")
            .type("heating-cooling.current")
            .pairedReadPermission()
            .notifyPermission()
            .uint8Format(0, 2, 1)
            .valid(0, "Off")
            .valid(1, "Heat. The Heater is currently on.")
            .valid(2, "Cool. Cooler is currently on.")
    );

    public static final CharacteristicType CURRENT_RELATIVE_HUMIDITY = register("Current Relative Humidity", c -> c
            .uuid("10")
            .type("relative-humidity.current")
            .pairedReadPermission()
            .notifyPermission()
            .floatFormat(0, 100, 1)
            .unit(Unit.PERCENTAGE)
    );

    public static final CharacteristicType CURRENT_TEMPERATURE = register("Current Temperature", c -> c
            .uuid("11")
            .type("temperature.current")
            .pairedReadPermission()
            .notifyPermission()
            .floatFormat(0, 100, 0.1)
            .unit(Unit.CELSIUS)
    );

    public static final CharacteristicType FIRMWARE_REVISION = register("Firmware Revision", c -> c
            .uuid("52")
            .type("firmware.revision")
            .pairedReadPermission()
            .format(Format.STRING)
    );

    public static final CharacteristicType HARDWARE_REVISION = register("Hardware Revision", c -> c
            .uuid("53")
            .type("hardware.revision")
            .pairedReadPermission()
            .stringFormat()
    );

    public static final CharacteristicType HEATING_THRESHOLD_TEMPERATURE = register("Heating Threshold Temperature", c -> c
            .uuid("12")
            .type("temperature.heating-threshold")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .floatFormat(0, 25, 0.1)
            .unit(Unit.CELSIUS)
    );

    public static final CharacteristicType HUE = register("Hue", c -> c
            .uuid("13")
            .type("hue")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .floatFormat(0, 360, 1)
            .unit(Unit.ARCDEGREES)
    );

    public static final CharacteristicType IDENTIFY = register("Identify", c -> c
            .uuid("14")
            .type("identify")
            .pairedWritePermission()
            .boolFormat()
    );

    public static final CharacteristicType LOCK_CONTROL_POINT = register("Lock Control Point", c -> c
            .uuid("19")
            .type("lock-management.control-point")
            .pairedWritePermission()
            .tlv8Format()
    );

    public static final CharacteristicType LOCK_CURRENT_STATE = register("Lock Current State", c -> c
            .uuid("1D")
            .type("lock-mechanism.current-state")
            .pairedReadPermission()
            .notifyPermission()
            .uint8Format(0, 3, 1)
            .valid(0, "Unsecured")
            .valid(1, "Secured")
            .valid(2, "Jammed")
            .valid(3, "Unknown")
    );

    public static final CharacteristicType LOCK_LAST_KNOWN_ACTION = register("Lock Last Known Action", c -> c
            .uuid("1C")
            .type("lock-mechanism.last-known-action")
            .pairedReadPermission()
            .notifyPermission()
            .uint8Format(0, 8, 1)
            .valid(0, "Secured using physical movement, interior")
            .valid(1, "Unsecured using physical movement, interior")
            .valid(2, "Secured using physical movement, exterior")
            .valid(3, "Secured using physical movement, exterior")
            .valid(4, "Secured with keypad")
            .valid(5, "Unsecured with keypad")
            .valid(6, "Secured remotely")
            .valid(7, "Unsecured remotely")
            .valid(8, "Secured with Automatic Secure timeout")
    );

    public static final CharacteristicType LOCK_MANAGEMENT_AUTO_SECURITY_TIMEOUT = register("Lock Management Auto Security Timeout", c -> c
            .uuid("1A")
            .type("lock-management.auto-security-timeout")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .uint32Format()
            .unit(Unit.SECONDS)
    );

    public static final CharacteristicType LOCK_TARGET_STATE = register("Lock Target State", c -> c
            .uuid("1E")
            .type("lock-mechanism.target-state")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .uint8Format(0, 1, 1)
            .valid(0, "Unsecured")
            .valid(1, "Secured")
    );

    public static final CharacteristicType LOGS = register("Logs", c -> c
            .uuid("1F")
            .type("logs")
            .pairedReadPermission()
            .notifyPermission()
            .tlv8Format()
    );

    public static final CharacteristicType MANUFACTURER = register("Manufacturer", c -> c
            .uuid("20")
            .type("manufacturer")
            .pairedReadPermission()
            .stringFormat(64)
    );

    public static final CharacteristicType MODEL = register("Model", c -> c
            .uuid("21")
            .type("model")
            .pairedReadPermission()
            .stringFormat(64)
    );

    public static final CharacteristicType MOTION_DETECTED = register("Motion Detected", c -> c
            .uuid("22")
            .type("motion-detected")
            .pairedReadPermission()
            .notifyPermission()
            .boolFormat()
    );

    public static final CharacteristicType NAME = register("Name", c -> c
            .uuid("23")
            .type("name")
            .pairedReadPermission()
            .stringFormat(64)
    );

    public static final CharacteristicType OBSTRUCTION_DETECTED = register("Obstruction Detected", c -> c
            .uuid("24")
            .type("obstruction-detected")
            .pairedReadPermission()
            .notifyPermission()
            .boolFormat()
    );

    public static final CharacteristicType ON = register("On", c -> c
            .uuid("25")
            .type("on")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .boolFormat()
    );

    public static final CharacteristicType OUTLET_IN_USE = register("Outlet In Use", c -> c
            .uuid("26")
            .type("outlet-in-use")
            .pairedReadPermission()
            .notifyPermission()
            .boolFormat()
    );

    public static final CharacteristicType ROTATION_DIRECTION = register("Rotation Direction", c -> c
            .uuid("28")
            .type("rotation.direction")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .intFormat(0, 1, 1)
            .valid(0, "Clockwise")
            .valid(1, "Counter-clockwise")
    );

    public static final CharacteristicType ROTATION_SPEED = register("Rotation Speed", c -> c
            .uuid("29")
            .type("rotation.speed")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .floatFormat(0, 100, 1)
            .unit(Unit.PERCENTAGE)
    );

    public static final CharacteristicType SATURATION = register("Saturation", c -> c
            .uuid("2F")
            .type("saturation")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .floatFormat(0, 100, 1)
            .unit(Unit.PERCENTAGE)
    );

    public static final CharacteristicType SERIAL_NUMBER = register("Serial Number", c -> c
            .uuid("30")
            .type("serial-number")
            .pairedReadPermission()
            .stringFormat(64)
    );

    public static final CharacteristicType TARGET_DOOR_STATE = register("Target Door State", c -> c
            .uuid("32")
            .type("door-state.target")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .uint8Format(0, 1, 1)
            .valid(0, "Open")
            .valid(1, "Closed")
    );

    public static final CharacteristicType TARGET_HEATING_COOLING_STATE = register("Target Heating Cooling State", c -> c
            .uuid("33")
            .type("heating-cooling.target")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .uint8Format(0, 3, 1)
            .valid(0, "Off")
            .valid(1, "Heat. If the current temperature is below the target temperature then turn on heating.")
            .valid(2, "Cool. If the current temperature is above the target temperature then turn on cooling.")
    );

    public static final CharacteristicType TARGET_RELATIVE_HUMIDITY = register("Target Relative Humidity", c -> c
            .uuid("34")
            .type("relative-humidity.target")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .floatFormat(0, 100, 1)
            .unit(Unit.PERCENTAGE)
    );

    public static final CharacteristicType TARGET_TEMPERATURE = register("Target Temperature", c -> c
            .uuid("35")
            .type("temperature.target")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .floatFormat(10, 38, 0.1)
            .unit(Unit.CELSIUS)
    );

    public static final CharacteristicType TEMPERATURE_DISPLAY_UNITS = register("Temperature Display Units", c -> c
            .uuid("36")
            .type("temperature.units")
            .pairedReadPermission()
            .pairedWritePermission()
            .notifyPermission()
            .uint8Format(0, 1, 1)
            .valid(0, "Celsius")
            .valid(1, "Fahrenheit")
    );

    public static final CharacteristicType VERSION = register("Version", c -> c
            .uuid("37")
            .type("version")
            .pairedReadPermission()
            .stringFormat(64)
    );

}
