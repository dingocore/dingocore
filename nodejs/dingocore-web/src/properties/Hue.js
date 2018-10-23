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
    null,
    (dispatch, props) => {
        console.log("map dispatch to props", dispatch, props);
        return {
            onChange: (value) => {
                console.log( "dispatch onchange", value );
                dispatch(push_update_property_value(
                    props.property,
                    "" + Math.trunc(value.hsl.h)
                ))
            }
        }
    }
)(Hue);