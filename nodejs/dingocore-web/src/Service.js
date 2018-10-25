import React from 'react';
import PropertyList from './PropertyList';
import { connect } from 'react-redux';
import './Service.css';

const Service = ({ service, properties }) => {
    return (
        <div className="Service">
            <div>{service.name}</div>
            <PropertyList properties={properties} />
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
