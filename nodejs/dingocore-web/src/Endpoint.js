
import React from 'react';
import ServiceList from './ServiceList';
import { connect } from 'react-redux';

const Endpoint = ({endpoint, services})=>{
    return (
        <div>
            <span>Endpoint {endpoint.name}</span>
            <div>my services</div>
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

