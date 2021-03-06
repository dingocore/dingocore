import React, { Component } from 'react'
import { HuePicker } from 'react-color';
import { connect } from 'react-redux';

import { push_update_property_value } from 'dingocore-redux/dist/actions/properties';

const Hue = ({ property, onChange }) => {
    console.log("my onchange", onChange);
    const hsl = { h: property.value, s: 0.5, l: 100}
    return (
        <HuePicker
            color={hsl}
            onChangeComplete={onChange}
        />
    )
}

export default connect(
    (state, props) => {
        const found = state.automation.properties.find(e => 
            e.id == props.property.id &&
            e.service_id == props.property.service_id &&
            e.endpoint_id == props.property.endpoint_id &&
            e.connection_id == props.property.connection_id
        ).value
        console.log( "map property", props.property, found );
        return {
            value: state.automation.properties.find(e => 
                e.id == props.property.id &&
                e.service_id == props.property.service_id &&
                e.endpoint_id == props.property.endpoint_id &&
                e.connection_id == props.property.connection_id
            ).value
        }
    },
    (dispatch, props) => {
        return {
            onChange: (value) => {
                console.log("dispatch onchange", value);
                dispatch(push_update_property_value(
                    props.property,
                    Math.trunc( value.hsl.h).toString()
                ))
            }
        }
    }
)(Hue);
