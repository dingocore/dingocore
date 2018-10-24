
import React, { Component } from 'react';
import PropertyList from './PropertyList';
import { connect } from 'react-redux';

const Service = ({ service, properties }) => {
    return (
        <div>
            <div>Service {service.name}</div>
            <PropertyList properties={properties}/>
        </div>
    )
}

export default connect(
    (state, props) => {
        return {
            properties: state.automation.properties.filter(e => e.service_id == props.service.id)
        }
    }
)(Service);

//export default Service;
