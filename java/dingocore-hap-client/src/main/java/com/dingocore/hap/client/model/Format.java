package com.dingocore.hap.client.model;

import com.dingocore.hap.client.model.value.BoolValue;
import com.dingocore.hap.client.model.value.DataValue;
import com.dingocore.hap.client.model.value.FloatValue;
import com.dingocore.hap.client.model.value.IntValue;
import com.dingocore.hap.client.model.value.StringValue;
import com.dingocore.hap.client.model.value.TLV8Value;
import com.dingocore.hap.client.model.value.UInt16Value;
import com.dingocore.hap.client.model.value.UInt32Value;
import com.dingocore.hap.client.model.value.UInt64Value;
import com.dingocore.hap.client.model.value.UInt8Value;
import com.dingocore.hap.client.model.value.Value;

/**
 * Created by bob on 9/14/18.
 */
public enum Format {

    BOOL(BoolValue.class),
    UINT8(UInt8Value.class),
    UINT16(UInt16Value.class),
    UINT32(UInt32Value.class),
    UINT64(UInt64Value.class),
    INT(IntValue.class),
    FLOAT(FloatValue.class),
    STRING(StringValue.class),
    TLV8(TLV8Value.class),
    DATA(DataValue.class)
    ;

    Format(Class<? extends Value> valueFormat) {
        this.valueFormat = valueFormat;
    }

    public Class<? extends Value> getValueFormat() {
        return this.valueFormat;
    }

    private final Class<? extends Value> valueFormat;
}
