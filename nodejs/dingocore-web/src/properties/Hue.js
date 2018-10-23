import React, { Component } from 'react'
import { HuePicker } from 'react-color';
import { connect } from 'react-redux';

import { push_update_property_value } from 'dingocore-redux/dist/actions/properties';

const Hue = ({ hue, onChange }) => {
    console.log("my onchange", onChange);
    return (
        <HuePicker
            onChange={onChange}
        />
    )
}

export default connect(
    null,
    (dispatch, props) => {
        console.log("map dispatch to props", dispatch, props);
        return {
            onChange: (value) => {
                console.log( "dispatch onchange", value );
                dispatch(push_update_property_value(
                    props.property.connection_id,
                    props.property.endpoint_id,
                    props.property.service_id,
                    props.property.id,
                    value.hsl.h
                ))
            }
        }
    }
)(Hue);