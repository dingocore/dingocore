import React, { Component } from 'react';
import { Button } from '@patternfly/react-core';
import { connect } from 'react-redux';
import { push_update_property_value } from 'dingocore-redux/dist/actions/properties';

import 'rc-slider/assets/index.css';

class On extends Component {
    constructor(props) {
        super(props);
        this.onClick = this.onClick.bind(this);
        console.log("===>", this.props.value, typeof this.props.value);
    }

    onClick(event) {
        //this.on = this.toggleState();
        //console.log("button click", this.on);
        if (this.props.value == 'true') {
            this.props.onChange('false');
        } else {
            this.props.onChange('true');
        }
    }

    render() {
        let variant = 'secondary';
        let text = 'off';
        console.log("RENDER RENDER", this.props);
        if (this.props.value == 'true') {
            console.log("*** RENDER AS ON");
            variant = 'primary';
            text = 'on';
        }
        return (
            <div>
                <Button variant={variant} onClick={this.onClick}>{text}</Button>
            </div>
        )
    }
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
                    value,
                ))
            }
        }
    }
)(On);