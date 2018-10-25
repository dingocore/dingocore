
import React from 'react';
import ServiceList from './ServiceList';
import { connect } from 'react-redux';
import './Endpoint.css'

const Endpoint = ({endpoint, services})=>{
    return (
        <div className="Endpoint">
            <span>{endpoint.name}</span>
            <ServiceList services={services}/>
        </div>
    )
}

export default connect(
    (state, props)=>{
        return {
            services: state.automation.services.filter(e=>e.endpoint_id = props.endpoint.id)
        }
    }
)(Endpoint)

